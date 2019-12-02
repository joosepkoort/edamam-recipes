package com.example.joosep.edamamRecipes.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.database.sqlite.*;
import android.widget.Toast;

import com.example.joosep.edamamRecipes.BuildConfig;
import com.example.joosep.edamamRecipes.Model.NutrientInfo;
import com.example.joosep.edamamRecipes.Model.Recipe;
import com.example.joosep.edamamRecipes.Model.RecipeDbHelper;
import com.example.joosep.edamamRecipes.R;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private ArrayAdapter < Recipe > listAdapter;
    //read api data from the properties
    String ApiKey = BuildConfig.ApiKeY;
    String AppId = BuildConfig.AppId;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //set content view AFTER ABOVE sequence (to avoid crash)
        this.setContentView(R.layout.activity_main);
        final RecipeDbHelper dbhelper = new RecipeDbHelper(getApplicationContext());

        listAdapter = new ArrayAdapter < Recipe > (this, R.layout.mylist) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                LayoutInflater inflater = MainActivity.this.getLayoutInflater();

                View View = inflater.inflate(R.layout.mylist, null, true);

                ImageView imageview = (ImageView) View.findViewById(R.id.logo);

                setImage(View, imageview, position);
                TextView extraTxt = (TextView) View.findViewById(R.id.textView1);
                TextView source = (TextView) View.findViewById(R.id.sourceTextView);
                TextView txtTitle = (TextView) View.findViewById(R.id.item);
                txtTitle.setText(this.getItem(position).getRecipeName());
                //source.setText(this.getItem(position).getSource());
                source.setText("");
                Picasso.with(getApplicationContext()).load(this.getItem(position).getRecipeImageUrl()).into(imageview);

                extraTxt.setText("read more at: " + this.getItem(position).getRecipeUrl());

                return View;
            }

        };
        ListView conferences = findViewById(R.id.listView);
        conferences.setAdapter(listAdapter);
        conferences.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            public void onSwipeTop() {
                Toast.makeText(MainActivity.this, "top", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeRight() {
                Toast.makeText(MainActivity.this, "right", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, FavouritesActivity.class);
                // intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(intent);
            }
            public void onSwipeLeft() {
                Toast.makeText(MainActivity.this, "left", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                // intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(intent);
            }
            public void onSwipeBottom() {
                Toast.makeText(MainActivity.this, "bottom", Toast.LENGTH_SHORT).show();
            }

        });

        conferences.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView < ? > parent, View view, final int position, long id) {
                //Toast.makeText(parent.getContext(), "Hello, World!", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder winChoice = new AlertDialog.Builder(parent.getContext());
                winChoice.setNegativeButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface self, int what) {
                        RecipeDbHelper dbHelper = new RecipeDbHelper(getApplicationContext());


                        Recipe selectedRecipe = listAdapter.getItem(position);
                        Recipe checkedRecipe = (dbHelper.getByRecipeName(selectedRecipe.getRecipeName()));
                        //first check if recipe is in database already?
                        if ((checkedRecipe) != null) {
                            //if object has not been favourited already
                            if (checkedRecipe.getIsFavourite() == (0)) {
                                checkedRecipe.setIsFavourite(1);
                                dbHelper.updateRecipe(checkedRecipe);
                                Toast.makeText(getApplicationContext(), "favourited already existing recipe",
                                        Toast.LENGTH_LONG).show();
                            }
                            //otherwise, edit already found item in database and set favourite 0 to remove item from favourites
                            else if (checkedRecipe.getIsFavourite() == 1) {
                                checkedRecipe.setIsFavourite(0);
                                dbHelper.updateRecipe(checkedRecipe);
                                Toast.makeText(getApplicationContext(), "unfavourited already existing recipe",
                                        Toast.LENGTH_LONG).show();
                            }
                        } else if (checkedRecipe == null) {
                            //add a new recipe
                            Recipe recipeToBeAdded = listAdapter.getItem(position);
                            recipeToBeAdded.setIsFavourite(1);
                            dbHelper.addRecipe(recipeToBeAdded);
                            Toast.makeText(getApplicationContext(), "added new recipe to favourites",
                                    Toast.LENGTH_LONG).show();
                        }
                    }






                });

                ArrayList receivedArray = new ArrayList();

                try {
                    JSONObject json = new JSONObject(listAdapter.getItem(position).getIngredients());
                    JSONArray jArray = json.optJSONArray("ab");
                    for (int i = 0; i < jArray.length(); i++) {
                        String str_value = jArray.optString(i);
                        receivedArray.add(str_value);
                    }
                } catch (JSONException e) {

                }

                winChoice
                        .setTitle(listAdapter.getItem(position).getRecipeName())
                        .setMessage(receivedArray.toString())
                        .setPositiveButton("close", null);
                winChoice.show();

            }
        });



        try {
            //adding items to database..
            //--------------------------------------------------------------------------------------------------
            RecipeDbHelper mDbHelper = new RecipeDbHelper(this);

            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            List < Recipe > Recipes2 = mDbHelper.getAllRecipes();
            listAdapter.addAll(Recipes2);
            mDbHelper.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void searchRecipe(View button) {


        EditText searchRecipeText = (EditText) findViewById(R.id.seardRecipeText);
        searchRecipeText.setImeActionLabel("Custom text", KeyEvent.KEYCODE_ENTER);
        String searchString = searchRecipeText.getText().toString();
        try {

            new HttpGetRecipe().execute(searchString).get();
        } catch (Exception e) {
            System.out.println(e);
        }



    }

    public class HttpGetRecipe extends AsyncTask < String, Void, ArrayList < Recipe >> {
        @Override
        protected ArrayList < Recipe > doInBackground(String...params) {
            try {
                ArrayList < Recipe > receivedRecipes = new ArrayList < Recipe > ();
                Log.i("query", "trying to access database");
                String searchString = params[0];
                String url;
                if (params[0] != null) {
                    searchString = params[0];
                }

                //gets settings table required for custom search
                RecipeDbHelper dbhelper = new RecipeDbHelper(MainActivity.this);
                Integer[] settings = dbhelper.getSettings();
                String free = "&more=true";
                //alcoholfree
                if (settings[0] != 0) {
                    free += "&health=alcohol-free";
                }
                //treenutfree
                if (settings[1] != 0) {
                    free += "&health=tree-nut-free";
                }
                //peanutfree
                if (settings[2] != 0) {
                    free += "&health=peanut-free";
                }

                Log.i("freeString", free);
                url = "https://api.edamam.com/search?q=" + searchString + "&app_id=" + AppId + "&app_key=" + ApiKey + "&from=0&to=" + settings[3] + free;

                HttpClient httpclient = new DefaultHttpClient();

                HttpGet httpGet = new HttpGet(url);

                HttpResponse responce = httpclient.execute(httpGet);

                HttpEntity httpEntity = responce.getEntity();

                String response = EntityUtils.toString(httpEntity);
                Log.i("Response: ", response);

                JSONObject jObject = new JSONObject(response);
                JSONArray jArray = jObject.getJSONArray("hits");

                JSONArray newArray = new JSONArray();

                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    newArray.put(json_data);
                    //opens recipe jsonarray
                    JSONObject data = json_data.getJSONObject("recipe");
                    //finds label name in recipe jsonarray
                    String recipelabel = data.getString("label");
                    String recipeUrl = data.getString("url");
                    String recipeImageUrl = data.getString("image");
                    Double calories = data.getDouble("calories");
                    String source = data.getString("source");
                    String yield = data.getString("yield");
                    //creating a new recipe
                    Recipe newRecipe = new Recipe();
                    //looping trough json for ingredients
                    JSONArray ingredientsArray = data.getJSONArray("ingredientLines");
                    ArrayList < String > tempList = new ArrayList < String > ();
                    for (int j = 0; j < ingredientsArray.length(); j++) {
                        tempList.add(ingredientsArray.getString(j));
                    }
                    String[] ingredientsArrayTest = tempList.toArray(new String[tempList.size()]);

                    JSONArray ingredients_detailed = data.getJSONArray("ingredients");
                    List < Map < String, String >> list = new ArrayList < Map < String, String >> ();
                    for (int j = 0; j < ingredients_detailed.length(); j++) {
                        Map newValues = new HashMap();


                        String text = ingredients_detailed.getJSONObject(j).getString("text");
                        String weight = ingredients_detailed.getJSONObject(j).getString("weight");
                        if (!weight.isEmpty()) {
                            String formattedString = String.format("%.02f", Float.valueOf(weight));
                            newValues.put(text, formattedString);
                        } else {
                            newValues.put(text, weight);
                        }


                        list.add(newValues);
                    }

                    JSONObject totalNutrients = data.getJSONObject("totalNutrients");

                    ObjectMapper mapper = new ObjectMapper();
                    Map totalNutrientsMap = mapper.readValue(totalNutrients.toString(), Map.class);
                    ArrayList < NutrientInfo > NutrientsArraylist = new ArrayList < NutrientInfo > ();

                    for (Object key: totalNutrientsMap.keySet()) {
                        String keyString = key.toString();
                        JSONObject nutrientObject = totalNutrients.getJSONObject(keyString);
                        ObjectMapper nutrientMapper = new ObjectMapper();
                        Map nutrientMap = nutrientMapper.readValue(nutrientObject.toString(), Map.class);
                        NutrientInfo newNutrientInfo = new NutrientInfo(
                                nutrientMap.get("label").toString(),
                                nutrientMap.get("quantity").toString(),
                                nutrientMap.get("unit").toString());
                        NutrientsArraylist.add(newNutrientInfo);
                    }
                    ArrayList friends = new ArrayList();

                    newRecipe.setTotalNutrients(NutrientsArraylist);
                    newRecipe.setCalories(calories);
                    newRecipe.setList(list);
                    newRecipe.setIngredientsList(tempList);
                    newRecipe.setRecipeName(recipelabel);
                    newRecipe.setRecipeDescription(null);
                    newRecipe.setRecipeUrl(recipeUrl);
                    newRecipe.setRecipeImageUrl(recipeImageUrl);
                    newRecipe.setSource(source);
                    newRecipe.setYield(yield);
                    newRecipe.setIngredients(tempList.toString());
                    StringBuilder builder = new StringBuilder();
                    int index = 1;
                    for (Map < String, String > element: newRecipe.getList()) {
                        for (String key: element.keySet()) {
                            String value = element.get(key);
                            System.out.println("key = " + key); //
                            System.out.println("value = " + value); //grams
                            if (builder.length() > 0) {
                                builder.append(System.getProperty("line.separator"));
                            }

                            builder.append(index);
                            builder.append(".");
                            builder.append(" ");
                            builder.append(key);
                            builder.append(" ");
                            builder.append("(");
                            builder.append(value);
                            builder.append(" ");
                            builder.append("grams");
                            builder.append(")");
                            builder.append(";");
                            index++;

                        }

                    }
                    String commaSeparated = builder.toString();
                    ArrayList < String > convertedIngredientList = new ArrayList < String > (Arrays.asList(commaSeparated.split(";")));
                    Log.v("testimportan", convertedIngredientList.toString());
                    System.out.println(builder.toString());

                    JSONObject json = new JSONObject();
                    json.put("ab", new JSONArray(convertedIngredientList));
                    String arrayList = json.toString();

                    newRecipe.setIngredients(arrayList);
                    newRecipe.setIngredientsList(convertedIngredientList);
                    receivedRecipes.add(newRecipe);
                }
                dbhelper.close();

                Log.i("successfully received recipes from json", receivedRecipes.toString());
                return receivedRecipes;
            } catch (Exception e) {
                Log.e("caught error:", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(ArrayList < Recipe > recipes) {
            listAdapter.addAll(recipes);
            Toast.makeText(getApplicationContext(), "download finished!",
                    Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPreExecute() {
            listAdapter.clear();
        }


    }

    public static void setImage(View view, final ImageView imageView, Integer position) {

        imageView.setTag(new Integer(position));
        imageView.setAdjustViewBounds(true);
        imageView.setOnClickListener(new android.view.View.OnClickListener() {
            boolean isImageFitToScreen;
            @Override
            public void onClick(View view) {
                if (isImageFitToScreen) {
                    isImageFitToScreen = false;
                    imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    imageView.setAdjustViewBounds(true);
                } else {
                    isImageFitToScreen = true;
                    imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    imageView.setScaleType(ImageView.ScaleType.FIT_START);
                }
            }
        });

    }





}