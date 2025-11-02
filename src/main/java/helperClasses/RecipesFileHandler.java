package helperClasses;

import main.Liquid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

public class RecipesFileHandler {
    public static Recipes loadRecipes() {
        Recipes recipes = new Recipes();
        List<List<Hashtable<Liquid, Double>>> recipe_mixes = MixFileHandler.getMixeFromFile("src/main/resources/assets/recipesSave_mixes.txt", "src/main/resources/assets/liquids.txt");
        List<List<Double>> recipes_rates = new ArrayList<>();
        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader("src/main/resources/assets/recipesSave_rates.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.trim().split("\\s+");
                List<Double> dblGroup = new ArrayList<>();

                for (String part : parts) {
                    dblGroup.add(Double.parseDouble(part));
                }

                recipes_rates.add(dblGroup);
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();

        }

        for(int i = 0; i < recipe_mixes.size(); i++) {
            recipes.recipeHashes.put(recipe_mixes.get(i), recipes_rates.get(i));
            recipes.recipeKeys.add(recipe_mixes.get(i));

        }

        return recipes;
    }
    public static void saveRecipes(HashMap<List<Hashtable<Liquid,Double>>,List<Double>> recipes) {
        // Placeholder for recipe saving logic
    }
}
