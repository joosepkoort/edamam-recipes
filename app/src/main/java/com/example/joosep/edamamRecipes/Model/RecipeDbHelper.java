package com.example.joosep.edamamRecipes.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static com.example.joosep.edamamRecipes.Model.DBContract.SQL_CREATE_ENTRIES;
import static com.example.joosep.edamamRecipes.Model.DBContract.SQL_CREATE_SETTINGS;
import static com.example.joosep.edamamRecipes.Model.DBContract.SQL_DELETE_ENTRIES;
import static com.example.joosep.edamamRecipes.Model.DBContract.SQL_DELETE_SETTINGS;

public class RecipeDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Recipes.db";

    public RecipeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SQL_CREATE_ENTRIES);
        db.execSQL(SQL_CREATE_SETTINGS);
        //setting default values
        db.execSQL("insert into settings (Alcoholfree, TreeNutFree, PeanutFree, NumberOfItems) VALUES (1,1,0,5)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(SQL_DELETE_SETTINGS);
        onCreate(db);

    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public List < Recipe > getAllRecipes() {
        List < Recipe > recipesList = new ArrayList < Recipe > ();

        String selectQuery = "SELECT * FROM " + DBContract.RecipeEntry.TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Recipe Recipe = new Recipe();
                Recipe.setRecipeId(Integer.parseInt(cursor.getString(0)));
                Recipe.setRecipeName(cursor.getString(1));
                Recipe.setRecipeDescription(cursor.getString(2));
                Recipe.setRecipeUrl(cursor.getString(3));
                Recipe.setIngredients(cursor.getString(4));
                Recipe.setRecipeImageUrl(cursor.getString(5));
                Recipe.setIsFavourite(cursor.getInt(6));
                recipesList.add(Recipe);
            } while (cursor.moveToNext());
        }

        return recipesList;
    }

    public List < Recipe > getFavourited() {
        List < Recipe > contactList = new ArrayList < Recipe > ();

        String selectQuery = "SELECT * FROM " + DBContract.RecipeEntry.TABLE_NAME + " where isFavourite = 1";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Recipe favRecipe = new Recipe();
                favRecipe.setRecipeId(Integer.parseInt(cursor.getString(0)));
                favRecipe.setRecipeName(cursor.getString(1));
                favRecipe.setRecipeDescription(cursor.getString(2));
                favRecipe.setRecipeUrl(cursor.getString(3));
                favRecipe.setIngredients(cursor.getString(4));
                favRecipe.setRecipeImageUrl(cursor.getString(5));
                favRecipe.setIsFavourite(cursor.getInt(6));
                contactList.add(favRecipe);
            } while (cursor.moveToNext());
        }

        return contactList;
    }

    public void addRecipe(Recipe recipe) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.RecipeEntry.COLUMN_NAME_RecipeName, recipe.getRecipeName());
        values.put(DBContract.RecipeEntry.COLUMN_NAME_RecipeDescription, recipe.getRecipeDescription());
        values.put(DBContract.RecipeEntry.COLUMN_NAME_RecipeUrl, recipe.getRecipeUrl());
        values.put(DBContract.RecipeEntry.COLUMN_NAME_RecipeImageUrl, recipe.getRecipeImageUrl());
        values.put(DBContract.RecipeEntry.COLUMN_NAME_isFavourite, recipe.getIsFavourite());
        values.put(DBContract.RecipeEntry.COLUMN_NAME_Ingredients, recipe.getIngredients());

        db.insert(DBContract.RecipeEntry.TABLE_NAME, null, values);
        db.close();
    }

    public int updateRecipe(Recipe recipe) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.RecipeEntry.COLUMN_NAME_RecipeName, recipe.getRecipeName());
        values.put(DBContract.RecipeEntry.COLUMN_NAME_RecipeDescription, recipe.getRecipeDescription());
        values.put(DBContract.RecipeEntry.COLUMN_NAME_RecipeUrl, recipe.getRecipeUrl());
        values.put(DBContract.RecipeEntry.COLUMN_NAME_RecipeImageUrl, recipe.getRecipeImageUrl());
        values.put(DBContract.RecipeEntry.COLUMN_NAME_isFavourite, recipe.getIsFavourite());
        Timestamp newTimestamp = new Timestamp(System.currentTimeMillis());
        values.put("sqltime", newTimestamp.toString());
        values.put(DBContract.RecipeEntry.COLUMN_NAME_Ingredients, recipe.getIngredients());
        return db.update(DBContract.RecipeEntry.TABLE_NAME,
                values,
                DBContract.RecipeEntry.COLUMN_NAME_RecipeName + " = ? ",
                new String[] {
                        String.valueOf(recipe.getRecipeName())
                });
    }


    public Integer[] getSettings() {
        Integer settings[] = new Integer[4];
        SQLiteDatabase db = this.getReadableDatabase();
        String[] items = {
                DBContract.RecipeEntry.COLUMN_NAME_Alcoholfree,
                DBContract.RecipeEntry.COLUMN_NAME_TreeNutFree,
                DBContract.RecipeEntry.COLUMN_NAME_PeaNutFree,
                DBContract.RecipeEntry.COLUMN_NAME_NumberOfItems
        };

        // Filter results WHERE "title" = 'My Title'
        String selection = DBContract.RecipeEntry.COLUMN_NAME_RecipeName + " = ?";
        String[] selectionArgs = {
                ""
        };
        String sortOrder = " DESC";

        Cursor cursor = db.query(
                DBContract.RecipeEntry.TABLE_NAME_Settings, // The table to query
                items, // The array of columns to return (pass null to get all)
                null, null,
                null, // don't group the rows
                null, // don't filter by row groups
                null // The sort order
        );

        while (cursor.moveToNext()) {
            settings[0] = cursor.getInt(
                    cursor.getColumnIndexOrThrow(DBContract.RecipeEntry.COLUMN_NAME_Alcoholfree));
            settings[1] = cursor.getInt(
                    cursor.getColumnIndexOrThrow(DBContract.RecipeEntry.COLUMN_NAME_TreeNutFree));
            settings[2] = cursor.getInt(
                    cursor.getColumnIndexOrThrow(DBContract.RecipeEntry.COLUMN_NAME_PeaNutFree));
            settings[3] = cursor.getInt(
                    cursor.getColumnIndexOrThrow(DBContract.RecipeEntry.COLUMN_NAME_NumberOfItems));
        }
        return settings;
    }

    public Recipe getByRecipeName(String recipeName) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] items = {
                DBContract.RecipeEntry._ID,
                DBContract.RecipeEntry.COLUMN_NAME_RecipeName,
                DBContract.RecipeEntry.COLUMN_NAME_RecipeDescription,
                DBContract.RecipeEntry.COLUMN_NAME_RecipeUrl,
                DBContract.RecipeEntry.COLUMN_NAME_RecipeImageUrl,
                DBContract.RecipeEntry.COLUMN_NAME_isFavourite,
                DBContract.RecipeEntry.COLUMN_NAME_Ingredients
        };
        Cursor cursor = db.query(
                DBContract.RecipeEntry.TABLE_NAME,
                null, //items array
                DBContract.RecipeEntry.COLUMN_NAME_RecipeName + " = ?",
                new String[] {
                        String.valueOf(recipeName)
                },
                null, //dont group rows
                null, //dont filter row groups
                null); //sortOrder
        if (cursor != null) {
            if (cursor.moveToFirst() == true) {
                cursor.moveToFirst();
                Recipe favRecipe = new Recipe();
                favRecipe.setRecipeId(Integer.parseInt(cursor.getString(0)));
                favRecipe.setRecipeName(cursor.getString(1));
                favRecipe.setRecipeDescription(cursor.getString(2));
                favRecipe.setRecipeUrl(cursor.getString(3));
                favRecipe.setIngredients(cursor.getString(4));
                favRecipe.setRecipeImageUrl(cursor.getString(5));
                favRecipe.setIsFavourite(cursor.getInt(6));
                return favRecipe;
            } else {
                return null;
            }


        }
        return null;
    }
}