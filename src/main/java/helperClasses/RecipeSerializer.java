package helperClasses;

import main.Mix;
import main.Recipe;

import java.io.*;
import java.util.List;

public class RecipeSerializer {
    
    public static List<Recipe> getRecipeFromFile(String filePath) throws ClassNotFoundException {
        List<Recipe> recipes;
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath));
            recipes = (List<Recipe>) in.readObject();
            in.close();
        } catch (IOException e) {
            recipes = new java.util.ArrayList<>();
        }

        return recipes;
    }
    public static void saveRecipeesToFile(List<Recipe> recipes, String filePath) throws IOException, ClassNotFoundException {
        // READ FIRST
        List<Recipe> savedRecipees = RecipeSerializer.getRecipeFromFile(filePath);
        
        // THEN WRITE
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath));
        savedRecipees.addAll(recipes);
        out.writeObject(savedRecipees);
        out.close();
    }
    public static void saveRecipeToFile(Recipe recipes, String filePath) throws IOException, ClassNotFoundException {
        // READ FIRST
        List<Recipe> savedRecipees;
        try {
            savedRecipees = RecipeSerializer.getRecipeFromFile(filePath);
        } catch (ClassNotFoundException e) {
            savedRecipees = new java.util.ArrayList<>();
        }

        // THEN WRITE (this truncates the file, but we have the data in memory now)
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath));
        savedRecipees.add(recipes);
        out.writeObject(savedRecipees);
        out.close();
    }
}
