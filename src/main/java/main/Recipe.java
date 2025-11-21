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
        String recipesString = "";
        recipesString += "Reaction: " + reaction + "\n";
        recipesString += mix.toString();
        recipesString += "\nRating: " + rating;
        return recipesString;
    }

}
