package main;

import helperClasses.LiquidLoader;
import helperClasses.MixSerializer;
import helperClasses.RateDrink;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class recipesTest {
    public static void main(String[] args)  {

        Mix mix = new Mix();
        List<Liquid> availableLiquids = null;
        try {
            availableLiquids = LiquidLoader.loadDrinks("./src/main/resources/assets/liquids.txt");
        } catch (IOException e) {
            System.out.println("Error loading liquids");
        }
        mix.put(availableLiquids.getFirst(), 1.0);

        try {
            MixSerializer.saveMixToFile(mix, "./src/main/resources/assets/recipes.ser");
        } catch (IOException e) {
            System.out.println("Error saving mix");
        }
    }
}
