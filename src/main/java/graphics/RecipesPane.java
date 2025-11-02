package graphics;

import helperClasses.Recipes;
import helperClasses.RecipesFileHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import main.Liquid;
import javafx.scene.control.Button;


import java.util.*;

public class RecipesPane {
    public static final int WIDTH = 820;
    public static final int HEIGHT = 580;

    private Pane recipesRootCont = new Pane();
    private StackPane parentRoot;
    private Recipes recipes;
    private int currentRecipe = 0;

    private GridPane recipeTextCont = new GridPane();



    private void changeDisplayedRecipe(){

        // Column headers
        Text header1 = new Text("Liquid");
        Text header2 = new Text("Amount");

        header1.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");
        header2.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");
        
        recipeTextCont.add(header1, 0, 0);
        recipeTextCont.add(header2, 1, 0);

        // Example data
        int row = 1;
        for (Hashtable<Liquid, Double> ingredient : recipes.recipeKeys.get(currentRecipe)) {
            for(Liquid entry : ingredient.keySet()) {
                Text name = new Text(entry.getName());
                Text amount = new Text(String.format("%.2f ml", ingredient.get(entry) * 1000)); // assuming 0.5L total

                recipeTextCont.add(name, 0, row);
                recipeTextCont.add(amount, 1, row);
                row++;
            }
        }
    }
    private void setupVisuals() {
        recipesRootCont.setPrefSize(BaseScene.WIDTH, BaseScene.HEIGHT);
        
        //recipes pane StackPane for the elemnts itself
        StackPane recipesRoot = new StackPane();
        recipesRoot.setPrefSize(WIDTH, HEIGHT);
        recipesRoot.setLayoutX(100);
        recipesRoot.setLayoutY(150);

        
        //bg
        Image bg = new Image(getClass().getResource("/assets/ui/recipesBookBg.png").toExternalForm());
        ImageView bgView = new ImageView(bg);
        bgView.setFitWidth(WIDTH);
        bgView.setFitHeight(HEIGHT);

        //close button
        Button closeButton = new Button("X");
        closeButton.setOnAction(e -> parentRoot.getChildren().remove(recipesRootCont));

        //close button pane cont
        Pane closeButtonContPane = new Pane();
        closeButtonContPane.setPrefSize(WIDTH, HEIGHT);
        closeButtonContPane.getChildren().add(closeButton);
        closeButton.setLayoutX((double) (BaseScene.WIDTH - WIDTH) /2 + WIDTH - 30);
        closeButton.setLayoutY((double) (BaseScene.HEIGHT - HEIGHT) /2 + 10);


        //setup recipe text system
        recipeTextCont.setHgap(20);
        recipeTextCont.setVgap(8);
        recipeTextCont.setPadding(new Insets(20));
        changeDisplayedRecipe();

        //recipeGridPane
        Pane recipeGridPane = new Pane();
        recipeGridPane.setPrefSize(WIDTH, HEIGHT);
        recipeGridPane.getChildren().add(closeButton);
        recipeTextCont.setLayoutX((double) (BaseScene.WIDTH - WIDTH) /2 +  30);
        recipeTextCont.setLayoutY((double) (BaseScene.HEIGHT - HEIGHT) /2 + 50);

        recipeGridPane.getChildren().add(recipeTextCont);
        
        
        //recipe change button
        Button recipeChange = new Button(">");
        recipeChange.setOnAction(e -> {
            if(currentRecipe != recipes.recipeKeys.size()-1)
                currentRecipe++;
            changeDisplayedRecipe();
        });



        //recipe change button pane cont
        Pane recipeChangeButtonPane = new Pane();
        recipeChangeButtonPane.setPrefSize(WIDTH, HEIGHT);
        recipeChangeButtonPane.getChildren().add(recipeChange);
        recipeChange.setLayoutX((double) (BaseScene.WIDTH - WIDTH) /2 + WIDTH - 30);
        recipeChange.setLayoutY((double) (BaseScene.HEIGHT - HEIGHT) /2 + 300);




        //add everything to recipesRootCont
        recipesRoot.getChildren().addAll(bgView, closeButtonContPane, recipeGridPane, recipeChangeButtonPane);

        //add the layers to the pain
        recipesRootCont.getChildren().addAll(recipesRoot);
    }
    public RecipesPane(StackPane parentRoot) {
        this.parentRoot = parentRoot;
        this.recipesRootCont = new StackPane();

        recipes = RecipesFileHandler.loadRecipes();

        //setup visuals
        setupVisuals();

        //add it to the current scene
        parentRoot.getChildren().add(recipesRootCont);
    }
}
