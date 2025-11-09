package main;

import helperClasses.MixSerializer;
import helperClasses.LiquidLoader;
import helperClasses.RecipeSerializer;
import javafx.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RateTestingMiniApp {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        List<Liquid> drinks = LiquidLoader.loadDrinks("./src/main/resources/assets/liquids.txt");
        List<Liquid> ls = new ArrayList<>();
        ls.add(drinks.get(0));
        ls.add(drinks.get(1));
        ls.add(drinks.get(2));

        List<Double> rates = new ArrayList<>();
        rates.add(0.1);
        rates.add(0.2);
        rates.add(0.3);

        List<Mix> test = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            test.add(new Mix(ls, rates));
        }

        MixSerializer.saveMixesToFile(test, "./src/main/resources/assets/mixes.ser");


        List<Mix> mixes = MixSerializer.getMixeFromFile("./src/main/resources/assets/mixes.ser");


        Recipes recipes = new Recipes();
        recipes.mixes = mixes;
        for (Mix mix : mixes) {
            recipes.mixRecipes.put(mix, new Pair<>(0.0, 0.0));
        }
        RecipeSerializer.saveRecipesToFile(recipes);

        Recipes fromFile = RecipeSerializer.getRecipesFromFile();
        System.out.println(recipes.toString());
    }
}
