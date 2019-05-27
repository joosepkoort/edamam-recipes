package com.example.joosep.edamamRecipes.Activities;


import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v7.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;

import com.example.joosep.edamamRecipes.Model.DBContract;
import com.example.joosep.edamamRecipes.Model.RecipeDbHelper;
import com.example.joosep.edamamRecipes.R;

import java.util.List;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends AppCompatPreferenceActivity {


    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {


                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0 ? listPreference.getEntries()[index] : null);

            } else if (preference instanceof RingtonePreference) {
                // For ringtone preferences, look up the correct display value
                // using RingtoneManager.
                if (TextUtils.isEmpty(stringValue)) {
                    // Empty values correspond to 'silent' (no ringtone).
                    preference.setSummary(R.string.pref_ringtone_silent);

                } else {
                    Ringtone ringtone = RingtoneManager.getRingtone(
                            preference.getContext(), Uri.parse(stringValue));

                    if (ringtone == null) {
                        // Clear the summary if there was a lookup error.
                        preference.setSummary(null);
                    } else {
                        // Set the summary to reflect the new ringtone display
                        // name.
                        String name = ringtone.getTitle(preference.getContext());
                        preference.setSummary(name);
                    }
                }

            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }
            return true;
        }
    };



    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
        //super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //set content view AFTER ABOVE sequence (to avoid crash)
        //SettingsActivity.setContentView(R.layout);

    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List < Header > target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName) || GeneralPreferenceFragment.class.getName().equals(fragmentName);
    }

    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference("number_of_items"));
         //   bindPreferenceSummaryToValue(findPreference("example_list"));


        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        // This will get you total fragment in the backStack
        int count = getFragmentManager().getBackStackEntryCount();
        switch (count) {
            case 0:
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

                boolean alcohol_switch_test = prefs.getBoolean("alcohol_switch", false);
                boolean tree_nut_switch_test = prefs.getBoolean("tree_nut_switch", false);
                boolean peanut_switch_test = prefs.getBoolean("peanut_switch", false);
                String numberOfItems = prefs.getString("number_of_items", "");
                Log.i("number_of_items", numberOfItems);
                try {
                    RecipeDbHelper mDbHelper = new RecipeDbHelper(this);
                    ContentValues cv = new ContentValues();
                    cv.put("NumberOfItems", numberOfItems);
                    SQLiteDatabase db = mDbHelper.getWritableDatabase();
                    db.update(DBContract.RecipeEntry.TABLE_NAME_Settings, cv, null, null);
                    mDbHelper.close();
                } catch (RuntimeException e) {
                    Log.i("Caugh Error", e.toString());
                }

                if (alcohol_switch_test) {
                    RecipeDbHelper mDbHelper = new RecipeDbHelper(this);
                    ContentValues cv = new ContentValues();
                    cv.put("Alcoholfree", 1);
                    SQLiteDatabase db = mDbHelper.getWritableDatabase();
                    db.update(DBContract.RecipeEntry.TABLE_NAME_Settings, cv, null, null);
                    mDbHelper.close();
                }
                if (!alcohol_switch_test) {
                    RecipeDbHelper mDbHelper = new RecipeDbHelper(this);
                    ContentValues cv = new ContentValues();
                    cv.put("Alcoholfree", 0);
                    SQLiteDatabase db = mDbHelper.getWritableDatabase();
                    db.update(DBContract.RecipeEntry.TABLE_NAME_Settings, cv, null, null);
                    mDbHelper.close();
                }
                if (peanut_switch_test) {
                    RecipeDbHelper mDbHelper = new RecipeDbHelper(this);
                    ContentValues cv = new ContentValues();
                    cv.put("PeanutFree", 1);
                    SQLiteDatabase db = mDbHelper.getWritableDatabase();
                    db.update(DBContract.RecipeEntry.TABLE_NAME_Settings, cv, null, null);
                    mDbHelper.close();
                }
                if (!peanut_switch_test) {
                    RecipeDbHelper mDbHelper = new RecipeDbHelper(this);
                    ContentValues cv = new ContentValues();
                    cv.put("PeanutFree", 0);
                    SQLiteDatabase db = mDbHelper.getWritableDatabase();
                    db.update(DBContract.RecipeEntry.TABLE_NAME_Settings, cv, null, null);
                    mDbHelper.close();
                }
                if (tree_nut_switch_test) {
                    RecipeDbHelper mDbHelper = new RecipeDbHelper(this);
                    ContentValues cv = new ContentValues();
                    cv.put("TreeNutFree", 1);
                    SQLiteDatabase db = mDbHelper.getWritableDatabase();
                    db.update(DBContract.RecipeEntry.TABLE_NAME_Settings, cv, null, null);
                    mDbHelper.close();
                }
                if (!tree_nut_switch_test) {
                    RecipeDbHelper mDbHelper = new RecipeDbHelper(this);
                    ContentValues cv = new ContentValues();
                    cv.put("TreeNutFree", 0);
                    SQLiteDatabase db = mDbHelper.getWritableDatabase();
                    db.update(DBContract.RecipeEntry.TABLE_NAME_Settings, cv, null, null);
                    mDbHelper.close();
                }

                super.onBackPressed();
                break;
            case 1:
                // handle back press of fragment one
                break;
            case 2:
                // and so on for fragment 2 etc
                break;
            default:
                getFragmentManager().popBackStack();
                break;
        }
    }

}