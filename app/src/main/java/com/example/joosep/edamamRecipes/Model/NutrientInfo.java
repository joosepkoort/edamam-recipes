package com.example.joosep.edamamRecipes.Model;

/**
 * Created by Joosep on 28.04.2018.
 */

public class NutrientInfo {

    public NutrientInfo(String label, String quantity, String unit) {
        this.label = label;
        this.quantity = quantity;
        this.unit = unit;
    }

    public String label;
    public String quantity;
    public String unit;
}