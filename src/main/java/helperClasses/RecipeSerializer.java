package helperClasses;

import main.Recipes;
import java.io.*;

public class RecipeSerializer {
    public static Recipes getRecipesFromFile(){
        Recipes recipes;
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("./src/main/resources/assets/recipes.ser"));
            recipes = (Recipes) in.readObject();
            in.close();
        }
        catch (IOException | ClassNotFoundException e) {
            recipes = new Recipes();
        }

        return recipes;
    }
    public static void saveRecipesToFile(Recipes recipes) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("./src/main/resources/assets/recipes.ser"));
        out.writeObject(recipes);
        out.close();
    }
}
