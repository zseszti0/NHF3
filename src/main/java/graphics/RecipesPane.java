package graphics;

import helperClasses.RecipeSerializer;
import main.Mix;
import main.Recipe;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import main.Liquid;
import javafx.scene.control.Button;


import java.util.*;

public class RecipesPane {
    public static final int WIDTH = 820;
    public static final int HEIGHT = 580;

    private Pane recipesRootCont = new Pane();
    private StackPane parentRoot;
    private List<Recipe> recipes;
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

        // Load the new recepies
        // Remove all nodes except those in row 0
        if(!recipes.isEmpty()) {
            recipeTextCont.getChildren().removeIf(node -> GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) > 0);

            int row = 1;
            Mix currentMix = recipes.get(currentRecipe).mix;
            for (Liquid entry : currentMix.getLiquids()) {
                Text name = new Text(entry.getName());
                Text amount = new Text(String.format("%.0f ml", currentMix.get(entry) * 1000)); // 0.5L total

                recipeTextCont.add(name, 0, row);
                recipeTextCont.add(amount, 1, row);
                row++;
            }

            Text ratingStars = new Text(recipes.get(currentRecipe).ratingToString());
            ratingStars.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");

            recipeTextCont.add(ratingStars, 0, row);
            recipeTextCont.setAlignment(Pos.CENTER);
        }
    }
    private void setupVisuals() {
        recipesRootCont.setPrefSize(BaseScene.WIDTH, BaseScene.HEIGHT);
        
        //recipes pane StackPane for the elemnts itself
        StackPane recipesRoot = new StackPane();
        recipesRoot.setPrefSize(WIDTH, HEIGHT);
        recipesRoot.setLayoutX(100);
        recipesRoot.setLayoutY(500);

        
        //bg
        Image bg = new Image(Objects.requireNonNull(getClass().getResource("/assets/ui/recipesBookBg.png")).toExternalForm());
        ImageView bgView = new ImageView(bg);
        bgView.setFitWidth(WIDTH);
        bgView.setFitHeight(HEIGHT);

        //close button
        Button closeButton = new Button("X");
        closeButton.setOnAction(e -> parentRoot.getChildren().remove(recipesRootCont));
        


        //setup recipe text system
        recipeTextCont.setHgap(20);
        recipeTextCont.setVgap(8);
        recipeTextCont.setPadding(new Insets(20));
        changeDisplayedRecipe();

        //recipeGridPane
        Pane recipeGridPane = new Pane();
        recipeGridPane.setPrefSize(WIDTH, HEIGHT);
        recipeGridPane.getChildren().add(closeButton);
        recipeTextCont.setLayoutX((double) (BaseScene.WIDTH - WIDTH) /2 +  80);
        recipeTextCont.setLayoutY((double) (BaseScene.HEIGHT - HEIGHT) /2 + 200);

        recipeGridPane.getChildren().add(recipeTextCont);
        
        
        //recipe change button

        //forwards button
        Button recipeChangeRight = new Button(">");
        Button recipeChangeLeft = new Button("<");
        recipeChangeRight.setOnAction(e -> {
            if(currentRecipe != recipes.size()-1)
                currentRecipe++;
            changeDisplayedRecipe();

            if(currentRecipe == recipes.size()-1) {
                recipeChangeRight.setDisable(true);
            }
            else if(currentRecipe - 1 >= 0) {
                recipeChangeLeft.setDisable(false);
            }
        });
        //backwards button
        recipeChangeLeft.setOnAction(e -> {
            if(currentRecipe != 0)
                currentRecipe--;
            changeDisplayedRecipe();

            if(currentRecipe == 0) {
                recipeChangeLeft.setDisable(true);
            }
            else if(currentRecipe+1 < recipes.size()-1) {
                recipeChangeRight.setDisable(false);
            }
        });
        recipeChangeLeft.setDisable(true);



        //recipe change button pane cont
        Pane buttonPane = new Pane();
        buttonPane.setPrefSize(WIDTH, HEIGHT);

        buttonPane.getChildren().add(recipeChangeRight);
        recipeChangeRight.setLayoutX((double) (BaseScene.WIDTH - WIDTH) /2 + WIDTH - 30);
        recipeChangeRight.setLayoutY((double) (BaseScene.HEIGHT - HEIGHT) /2 + 300);

        buttonPane.getChildren().add(recipeChangeLeft);
        recipeChangeLeft.setLayoutX((double) (BaseScene.WIDTH - WIDTH) /2 + 30);
        recipeChangeLeft.setLayoutY((double) (BaseScene.HEIGHT - HEIGHT) /2 + 300);
        
        //add the close button to the same pane, to avoid unclickable buttons
        buttonPane.getChildren().add(closeButton);
        closeButton.setLayoutX((double) (BaseScene.WIDTH - WIDTH) /2 + WIDTH - 30);
        closeButton.setLayoutY((double) (BaseScene.HEIGHT - HEIGHT) /2 + 10);




        //add everything to recipesRootCont
        recipesRoot.getChildren().addAll(bgView, recipeGridPane, buttonPane);

        //add the layers to the pain
        recipesRootCont.getChildren().addAll(recipesRoot);
    }
    public RecipesPane(StackPane parentRoot){
        this.parentRoot = parentRoot;
        this.recipesRootCont = new StackPane();

        try {
            recipes = RecipeSerializer.getRecipeFromFile("./src/main/resources/assets/recipes.ser");
        } catch (ClassNotFoundException e) {
            System.out.println("Error loading recipes");
            recipes = new ArrayList<>();
        }

        //setup visuals
        setupVisuals();

        //add it to the current scene
        parentRoot.getChildren().add(recipesRootCont);
    }
}
