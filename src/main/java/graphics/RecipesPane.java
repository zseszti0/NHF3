package graphics;

import helperClasses.AnimationPresets;
import helperClasses.RecipeSerializer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import main.Mix;
import main.Recipe;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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

    private Text ratingStars;

    private final int WIDTH = 702;
    private final int HEIGHT = 485;
    private Pane recipeGridCont = new Pane();
    private Pane buttonPane = new Pane();

    private Font mainFont;
    private Font fancyFont;

    private void adjustArrowVisibility(Button recipeChangeLeft, Button recipeChangeRight){
        if(currentRecipe == 0) {
            recipeChangeLeft.setDisable(true);
        }
        if(currentRecipe < recipes.size()-1) {
            recipeChangeRight.setDisable(false);
        }
        if(currentRecipe == recipes.size()-1) {
            recipeChangeRight.setDisable(true);
        }
        if(currentRecipe - 1 >= 0) {
            recipeChangeLeft.setDisable(false);
        }
    }
    private void changeDisplayedRecipe(Button recipeChangeLeft, Button recipeChangeRight){
        adjustArrowVisibility(recipeChangeLeft,recipeChangeRight);

        recipeTextCont.setHgap(20);
        recipeTextCont.setVgap(8);
        recipeTextCont.setPadding(new Insets(20));

        ColumnConstraints column0 = new ColumnConstraints(120,120,120);
        ColumnConstraints column1 = new ColumnConstraints();
        column0.setHgrow(Priority.ALWAYS);

        recipeTextCont.getColumnConstraints().addAll(column0, column1);


        // Column headers
        Text header1 = new Text("Liquid");
        header1.setFont(mainFont);
        header1.setScaleX(1.5);
        header1.setScaleY(1.5);
        Text header2 = new Text("Amount");
        header2.setFont(mainFont);
        header2.setScaleX(1.5);
        header2.setScaleY(1.5);

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
                name.setFont(fancyFont);

                recipeTextCont.add(name, 0, row);
                recipeTextCont.add(amount, 1, row);
                row++;
            }

            Text pageNumberText = new Text(String.format("%d.",currentRecipe+1));
            recipeTextCont.add(pageNumberText,0,row+1);

            ratingStars.setText(recipes.get(currentRecipe).ratingToString());

            recipeTextCont.setAlignment(Pos.CENTER_LEFT);
        }
    }
    private void setupRecipeTextPane(){

        //rating to the other page
        ratingStars = new Text();
        ratingStars.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");
        ratingStars.setScaleX(2);
        ratingStars.setScaleY(2);

        //recipeGridPane
        recipeGridCont = new Pane();
        recipeGridCont.setPrefSize(WIDTH, HEIGHT);

        recipeTextCont.setLayoutX((double)(BaseScene.WIDTH - WIDTH)/2 + 80);
        recipeTextCont.setLayoutY((double)(BaseScene.HEIGHT - HEIGHT)/2 +40);

        recipeGridCont.getChildren().addAll(recipeTextCont,ratingStars);

        ratingStars.setLayoutX((double) BaseScene.WIDTH /2 + 155);
        ratingStars.setLayoutY(BaseScene.HEIGHT-245);


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
            if(currentRecipe != recipes.size()-1) {
                currentRecipe++;
                changeDisplayedRecipe(recipeChangeLeft,recipeChangeRight);
            }
        });
        //backwards button
        recipeChangeLeft.setOnAction(e -> {
            if(currentRecipe != 0) {
                currentRecipe--;
                changeDisplayedRecipe(recipeChangeLeft,recipeChangeRight);
            }
        });

        //setup the initial state
        recipeChangeLeft.setDisable(true);
        if(recipes.size() < 2) {
            recipeChangeRight.setDisable(true);
        }

        //delete button
        Button deleteButton = new Button();

        Sprite deleteButtonSprite = new Sprite(36,61);
        deleteButtonSprite.addState("base","/assets/ui/trashIconClosed.png");
        deleteButtonSprite.addState("hover","/assets/ui/trashIconOpen.png");
        deleteButtonSprite.setState("base");

        deleteButton.setGraphic(deleteButtonSprite);
        deleteButton.setStyle("-fx-background-color: transparent;");

        deleteButton.setOnMouseEntered(e -> {
            deleteButtonSprite.setState("hover");
            deleteButton.setScaleX(1.1);
            deleteButton.setScaleY(1.1);
        });
        deleteButton.setOnMouseExited(e -> {
            deleteButtonSprite.setState("base");
            deleteButton.setScaleX(1);
            deleteButton.setScaleY(1);
        });

        deleteButton.setOnAction(e -> {
            if(!recipes.isEmpty()) {
                recipes.remove(currentRecipe);
                try {
                    RecipeSerializer.updateRecipeFile(recipes,"./src/main/resources/assets/recipes.ser");
                } catch (Exception ex) {
                    System.out.println("Error saving recipes");
                }
                if(currentRecipe >= recipes.size() && currentRecipe > 0){
                    currentRecipe--;
                }
                changeDisplayedRecipe(recipeChangeLeft,recipeChangeRight);
            }
        });



        //recipe change button pane cont
        //make the layout o the buttons
        buttonPane = new Pane();
        buttonPane.setPrefSize(WIDTH, HEIGHT);

        buttonPane.getChildren().add(recipeChangeRight);
        recipeChangeRight.setLayoutX((double) (BaseScene.WIDTH + WIDTH) /2 -60);
        recipeChangeRight.setLayoutY((double) (BaseScene.HEIGHT) /2 - 80);

        buttonPane.getChildren().add(recipeChangeLeft);
        recipeChangeLeft.setLayoutX((double) (BaseScene.WIDTH - WIDTH) /2 -60);
        recipeChangeLeft.setLayoutY((double) (BaseScene.HEIGHT) /2 - 80);

        //add the close button to the same pane, to avoid unclickable buttons
        buttonPane.getChildren().add(closeButton);
        closeButton.setLayoutX((double) (BaseScene.WIDTH + WIDTH) /2 - 80);
        closeButton.setLayoutY(90);

        buttonPane.getChildren().add(deleteButton);
        deleteButton.setLayoutX((double) BaseScene.WIDTH /2 + 250);
        deleteButton.setLayoutY(BaseScene.HEIGHT-300);


        //setup the closing animation
        closeButton.setOnAction(e -> {
            bgView.setState("close");

            double animDuration = bgView.getAnimationDuration("close");

            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.millis(animDuration * 0.5), ae -> {
                        recipeGridCont.setVisible(false);
                        buttonPane.setVisible(false);
                    }),
                    new KeyFrame(Duration.millis(animDuration), ae -> {
                        parentRoot.getChildren().remove(recipesRootCont);
                    })
            );
            timeline.play();
            onClose.accept(Boolean.FALSE);
        });

        changeDisplayedRecipe(recipeChangeLeft,recipeChangeRight);
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
        bgView.addState("idle","/assets/ui/recipeBookClose/Book_00024.png");
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
        recipesRoot.getChildren().addAll(bgCont, recipeGridCont, buttonPane);
        recipesRootCont.getChildren().addAll(recipesRoot);

        openingAnimation(bgView, recipeGridCont, buttonPane);

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
                32
        );
        //headerFont
        fancyFont = Font.loadFont(
                getClass().getResource("/fonts/fancy.ttf").toExternalForm(),
                40
        );

        //setup visuals, and make them shown
        setupVisuals();

    }
}
