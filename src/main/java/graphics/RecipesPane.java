package graphics;

import helperClasses.AnimationPresets;
import helperClasses.RecipeSerializer;
import javafx.scene.text.Font;
import main.Mix;
import main.Recipe;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import main.Liquid;
import javafx.scene.control.Button;


import java.util.*;
import java.util.function.Consumer;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class RecipesPane {
    private Consumer<Boolean> onClose;

    private Pane recipesRootCont = new Pane();
    private StackPane parentRoot;
    private List<Recipe> recipes;
    private int currentRecipe = 0;

    private GridPane recipeTextCont = new GridPane();

    private final int WIDTH = 702;
    private final int HEIGHT = 485;
    private Pane recipeGridPane = new Pane();
    private Pane buttonPane = new Pane();

    private Font mainFont;
    private Font headerFont;



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
    private void setupRecipeTextPane(){
        recipeTextCont.setHgap(20);
        recipeTextCont.setVgap(8);
        recipeTextCont.setPadding(new Insets(20));
        changeDisplayedRecipe();

        //recipeGridPane
        recipeGridPane = new Pane();
        recipeGridPane.setPrefSize(WIDTH, HEIGHT);

        recipeTextCont.setLayoutX((double) (BaseScene.WIDTH - WIDTH) /2 +  80);
        recipeTextCont.setLayoutY((double) (BaseScene.HEIGHT - HEIGHT) /2 + 200);

        recipeGridPane.getChildren().add(recipeTextCont);
    }

    private void setupRecipeSystem(Sprite bgView){

        //setup recipe text system
        setupRecipeTextPane();


        Button closeButton = MixScene.setupControlButtonGraphics(mainFont, "X", 88, 80, "/assets/ui/recipePaneButtonHover/recipePaneButton_00000.png");
        //recipe change button

        //forwards button
        Button recipeChangeRight = MixScene.setupControlButtonGraphics(mainFont, ">", 88, 80, "/assets/ui/recipePaneButtonHover/recipePaneButton_00000.png");
        Button recipeChangeLeft = MixScene.setupControlButtonGraphics(mainFont, "<", 88, 80, "/assets/ui/recipePaneButtonHover/recipePaneButton_00000.png");
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
        //make the layout o the buttons
        buttonPane = new Pane();
        buttonPane.setPrefSize(WIDTH, HEIGHT);

        buttonPane.getChildren().add(recipeChangeRight);
        recipeChangeRight.setLayoutX((double) (BaseScene.WIDTH + WIDTH) /2);
        recipeChangeRight.setLayoutY((double) (BaseScene.HEIGHT) /2 - 30);

        buttonPane.getChildren().add(recipeChangeLeft);
        recipeChangeLeft.setLayoutX((double) (BaseScene.WIDTH - WIDTH) /2);
        recipeChangeLeft.setLayoutY((double) (BaseScene.HEIGHT) /2 - 30);

        //add the close button to the same pane, to avoid unclickable buttons
        buttonPane.getChildren().add(closeButton);
        closeButton.setLayoutX((double) (BaseScene.WIDTH + WIDTH) /2 -30);
        closeButton.setLayoutY(110);


        //setup the closing animation
        closeButton.setOnAction(e -> {
            bgView.setState("close");

            double animDuration = bgView.getAnimationDuration("close");

            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.millis(animDuration * 0.5), ae -> {
                        recipeGridPane.setVisible(false);
                        buttonPane.setVisible(false);
                    }),
                    new KeyFrame(Duration.millis(animDuration), ae -> {
                        parentRoot.getChildren().remove(recipesRootCont);
                    })
            );
            timeline.play();
            onClose.accept(Boolean.FALSE);
        });
    }
    private void openingAnimation(Sprite bgView, Pane recipeGridPane, Pane buttonPane){

        //make the book open, as soon as the pane is shown
        parentRoot.getChildren().add(recipesRootCont);

        recipeGridPane.setOpacity(0);
        buttonPane.setOpacity(0);
        bgView.setState("open");

        double animDuration = bgView.getAnimationDuration("open");

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(animDuration * 1.1), ae -> {
                    Animator fadeIn1 = AnimationPresets.FadeIn(recipeGridPane);
                    Animator fadeIn2 = AnimationPresets.FadeIn(buttonPane);

                    fadeIn1.play();
                    fadeIn2.play();
                })
        );
        timeline.play();
    }
    private void setupVisuals() {
        recipesRootCont.setPrefSize(BaseScene.WIDTH, BaseScene.HEIGHT);
        
        //recipes pane StackPane for the elemnts itself
        StackPane recipesRoot = new StackPane();
        recipesRoot.setPrefSize(BaseScene.WIDTH, BaseScene.HEIGHT);

        
        //bg
        Sprite bgView = new Sprite(1020,1034);
        bgView.addState("idle","/assets/ui/recipeBookClose/Book_00029.png");
        bgView.addStateAnimation("open", "/assets/ui/recipeBookOpen/");
        bgView.addStateAnimation("close", "/assets/ui/recipeBookClose/");

        Pane bgCont = new Pane();
        bgCont.setPrefSize(BaseScene.WIDTH, BaseScene.HEIGHT);
        bgCont.getChildren().add(bgView);
        bgView.setLayoutX(145);
        bgView.setLayoutY(-380);



        setupRecipeSystem(bgView);


        //add everything to recipesRootCont
        //and make the positions and layout
        recipesRoot.getChildren().addAll(bgCont, recipeGridPane, buttonPane);
        recipesRootCont.getChildren().addAll(recipesRoot);



        openingAnimation(bgView, recipeGridPane, buttonPane);

    }
    public RecipesPane(StackPane parentRoot, Consumer<Boolean> onClose){
        this.onClose = onClose;
        this.parentRoot = parentRoot;
        this.recipesRootCont = new StackPane();

        try {
            recipes = RecipeSerializer.getRecipeFromFile("./src/main/resources/assets/recipes.ser");
        } catch (ClassNotFoundException e) {
            System.out.println("Error loading recipes");
            recipes = new ArrayList<>();
        }

        //mainFont
        mainFont = Font.loadFont(
                getClass().getResource("/fonts/main.ttf").toExternalForm(),
                20
        );
        //headerFont
        headerFont = Font.loadFont(
                getClass().getResource("/fonts/fancy.ttf.ttf").toExternalForm(),
                32
        );

        //setup visuals, and make them shown
        setupVisuals();


    }
}
