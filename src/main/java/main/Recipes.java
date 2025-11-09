package main;

import javafx.util.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class Recipes implements Serializable{
    public HashMap<Mix, Pair<Double,Double>> mixRecipes = new HashMap<>();
    public List<Mix> mixes = new ArrayList<>();

    @Override
    public String toString() {
        String recipesString = "";
        for (Mix m : mixRecipes.keySet()) {
            recipesString += m.toString() + " " + mixRecipes.get(m) + "\n";
        }
        return recipesString;
    }

}
