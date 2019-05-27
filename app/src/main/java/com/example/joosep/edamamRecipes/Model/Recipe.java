package com.example.joosep.edamamRecipes.Model;

import android.graphics.Bitmap;

import java.sql.Array;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Joosep on 14.04.2018.
 */

public class Recipe {
    public int recipeId;
    //name of the recipe
    public String recipeName;
    public String recipeDescription;
    //url of the recipe on web
    public String recipeUrl;
    //url of the image on web
    public String recipeImageUrl;
    //wether or not the recipe is favourited yet or not. null and 0 mean not favourited, 1 means favourited
    public int isFavourite;
    public int viewCount;
    //date of when the object was last accessed
    public Timestamp lastModified;
    //list of all ingredients without grams
    public ArrayList < String > ingredientsList;
    //list of hashmaps containing:
    // 1. name of the ingredient
    // 2. how many grams it weighs
    public List < Map < String, String >> list = new ArrayList < Map < String, String >> ();
    public double calories;

    public Recipe(String recipeName, String recipeDescription, String recipeUrl, String recipeImageUrl) {
        this.recipeName = recipeName;
        this.recipeDescription = recipeDescription;
        this.recipeUrl = recipeUrl;
        this.recipeImageUrl = recipeImageUrl;
    }

    public String getIngredients() {
        return Ingredients;
    }

    public void setIngredients(String ingredients) {
        Ingredients = ingredients;
    }

    public String Ingredients;

    public void setYield(String yield) {
        this.yield = yield;
    }

    public String yield;

    public String getSource() {
        return Source;
    }

    public void setSource(String source) {
        Source = source;
    }

    public String Source;

    public ArrayList < NutrientInfo > getTotalNutrients() {
        return totalNutrients;
    }

    public void setTotalNutrients(ArrayList < NutrientInfo > totalNutrients) {
        this.totalNutrients = totalNutrients;
    }

    public ArrayList < NutrientInfo > totalNutrients;

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public Timestamp getLastModified() {
        return lastModified;
    }

    public void setLastModified(Timestamp lastModified) {
        this.lastModified = lastModified;
    }


    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getRecipeDescription() {
        return recipeDescription;
    }

    public void setRecipeDescription(String recipeDescription) {
        this.recipeDescription = recipeDescription;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public String getRecipeUrl() {
        return recipeUrl;
    }

    public void setRecipeUrl(String recipeUrl) {
        this.recipeUrl = recipeUrl;
    }

    public String getRecipeImageUrl() {
        return recipeImageUrl;
    }

    public void setRecipeImageUrl(String recipeImageUrl) {
        this.recipeImageUrl = recipeImageUrl;
    }

    public List < Map < String, String >> getList() {
        return list;
    }

    public void setList(List < Map < String, String >> list) {
        this.list = list;
    }


    public ArrayList < String > getIngredientsList() {
        return ingredientsList;
    }

    public void setIngredientsList(ArrayList < String > ingredientsList) {
        this.ingredientsList = ingredientsList;
    }


    public int getIsFavourite() {
        return isFavourite;
    }

    public void setIsFavourite(int isFavourite) {
        this.isFavourite = isFavourite;
    }


    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Bitmap image;
    public Recipe() {
    }


}