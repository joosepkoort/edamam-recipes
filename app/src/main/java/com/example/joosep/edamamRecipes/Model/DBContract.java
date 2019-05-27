package com.example.joosep.edamamRecipes.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public final class DBContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private DBContract() {}

    /* Inner class that defines the table contents */
    public static class RecipeEntry implements BaseColumns {
        public static final String TABLE_NAME = "Recipes";
        public static final String TABLE_NAME_Settings = "Settings";
        public static final String COLUMN_NAME_RecipeName = "RecipeName";
        public static final String COLUMN_NAME_RecipeDescription = "RecipeDescription";
        public static final String COLUMN_NAME_RecipeUrl = "RecipeUrl";
        public static final String COLUMN_NAME_RecipeImageUrl = "RecipeImageUrl";
        public static final String COLUMN_NAME_isFavourite = "isFavourite";
        public static final String COLUMN_NAME_Ingredients = "Ingredients";

        public static final String COLUMN_NAME_TreeNutFree = "TreeNutFree";
        public static final String COLUMN_NAME_Alcoholfree = "Alcoholfree";
        public static final String COLUMN_NAME_PeaNutFree = "PeanutFree";
        public static final String COLUMN_NAME_NumberOfItems = "NumberOfItems";

    }
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + RecipeEntry.TABLE_NAME + " (" +
                    RecipeEntry._ID + " INTEGER PRIMARY KEY," +
                    RecipeEntry.COLUMN_NAME_RecipeName + " TEXT," +
                    RecipeEntry.COLUMN_NAME_RecipeDescription + " TEXT," +
                    RecipeEntry.COLUMN_NAME_RecipeUrl + " TEXT," +
                    RecipeEntry.COLUMN_NAME_Ingredients + " TEXT," +
                    RecipeEntry.COLUMN_NAME_RecipeImageUrl + " TEXT, " +
                    "isFavourite Integer, " +
                    "sqltime TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL)";

    public static final String SQL_CREATE_SETTINGS =
            "CREATE TABLE Settings(Alcoholfree  Integer, TreeNutFree Integer, PeanutFree Integer, NumberOfItems Integer)";


    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + RecipeEntry.TABLE_NAME;

    public static final String SQL_DELETE_SETTINGS =
            "DROP TABLE IF EXISTS Settings";




}