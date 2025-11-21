package main;

import helperClasses.LiquidLoader;
import helperClasses.RecipeSerializer;

import java.io.IOException;
import java.util.List;

public class recipesTest {
    public static void main(String[] args) throws ClassNotFoundException {

        Recipe recipe = new Recipe();
        List<Liquid> availableLiquids = null;
        try {
            availableLiquids = LiquidLoader.loadDrinks("./src/main/resources/assets/liquids.txt");
        } catch (IOException e) {
            System.out.println("Error loading liquids");
        }
        Mix mix = new Mix();
        mix.put(availableLiquids.getFirst(), 1.0);
        recipe.mix = mix;
        recipe.rating = 5.0;
        recipe.reaction = "transcending_to_heaven";

        try {
            RecipeSerializer.saveRecipeToFile(recipe, "./src/main/resources/assets/recipes.ser");
        } catch (IOException e) {
            System.out.println("Error saving mix");
        }

        List<Recipe> recipesFromFIle = RecipeSerializer.getRecipeFromFile("./src/main/resources/assets/recipes.ser");
        for(Recipe mix1: recipesFromFIle){
            System.out.println(mix1);
        }
    }
}
