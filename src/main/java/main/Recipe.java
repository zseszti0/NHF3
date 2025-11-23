package main;

import javafx.util.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Recipe implements Serializable{
    public double rating = 0;
    public String reaction;
    public Mix mix;

    @Override
    public String toString() {
        //round rating to 0.25's
        double ratingTemp = Math.round(rating * 4) / 4.0;

        String recipesString = "";
        recipesString += "Reaction: " + reaction + "\n";
        recipesString += mix.toString();
        recipesString += "\nRating: " + String.format("%.2f ",ratingTemp);
        return recipesString;
    }

    public String ratingToString() {
        double ratingTemp = Math.round(rating * 4) / 4.0;

        return String.format("%.2f ★",ratingTemp);
    }
}
