package com.example.joosep.edamamRecipes.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.joosep.edamamRecipes.Model.Recipe;
import com.example.joosep.edamamRecipes.Model.RecipeDbHelper;
import com.example.joosep.edamamRecipes.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FavouritesActivity extends AppCompatActivity {
    private ArrayAdapter < Recipe > listAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_favourites);


        RecipeDbHelper mDbHelper = new RecipeDbHelper(this);
        List < Recipe > Recipes2 = mDbHelper.getFavourited();

        listAdapter = new ArrayAdapter < Recipe > (this, R.layout.mylist) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                LayoutInflater inflater = FavouritesActivity.this.getLayoutInflater();

                View View = inflater.inflate(R.layout.mylist, null, true);

                ImageView imageview = (ImageView) View.findViewById(R.id.logo);

                MainActivity.setImage(View, imageview, position);
                TextView extraTxt = (TextView) View.findViewById(R.id.textView1);
                TextView txtTitle = (TextView) View.findViewById(R.id.item);
                txtTitle.setText(this.getItem(position).getRecipeName());
                Picasso.with(FavouritesActivity.this).load(this.getItem(position).getRecipeImageUrl()).into(imageview);

                extraTxt.setText("read more at: " + this.getItem(position).getRecipeUrl());

                return View;
            }

        };

        ListView favouritesListView = (ListView) findViewById(R.id.listView2);

        favouritesListView.setAdapter(listAdapter);

        favouritesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView < ? > arg0, View arg1,
                                           int pos, long id) {

                Log.v("long clicked", "pos: " + pos);
                // TODO Auto-generated method stub
                ArrayList receivedArray = new ArrayList();

                try {
                    JSONObject json = new JSONObject(listAdapter.getItem(pos).getIngredients());
                    JSONArray jArray = json.optJSONArray("ab");
                    for (int i = 0; i < jArray.length(); i++) {
                        String str_value = jArray.optString(i); //<< jget value from jArray
                        receivedArray.add(str_value);
                    }
                } catch (JSONException e) {

                }
                //use receivedeArray here


                return true;
            }
        });



        listAdapter.addAll(Recipes2);


    }


}