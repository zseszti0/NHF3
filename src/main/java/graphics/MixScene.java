package graphics;

import helperClasses.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import main.Liquid;
import main.Mix;
import main.Recipe;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Objects;

import static helperClasses.MixStage.POUR;
import static helperClasses.MixStage.SHAKE;
import static helperClasses.MixStage.SERVE;
import static java.lang.Thread.sleep;


public class MixScene extends BaseScene{
    public static final int ESC_WIDTH = 820;
    public static final int ESC_HEIGHT = 580;
    
    private Pane escapeMenuRootCont;
    private boolean isEscUp = false;

    private int selectedIndex = 0;
    private List<Liquid> availableLiquids;
    private Mix currentMix = new Mix();
    private MixStage currentStage = POUR;

    private Sprite bartender;

    private GridPane liquidGrid;

    public MixScene() {
        super(SceneManager.mainStage, "mix");


        scene.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ESCAPE) {
                if(isEscUp) {
                    root.getChildren().remove(escapeMenuRootCont);
                    isEscUp = false;
                }
                else {
                    root.getChildren().add(escapeMenuRootCont);
                    AnimationPresets.EscapeMenuAppear(escapeMenuRootCont.getChildren().get(1));
                    isEscUp = true;
                }
            }
        });

        escapeMenuRootCont = new Pane();
        escapeMenuRootCont.setPrefSize(BaseScene.WIDTH, BaseScene.HEIGHT);


        setupEscapeMenu();

    }

    private void setupEscapeMenu() {
        StackPane escapeParentRoot = new StackPane();
        escapeParentRoot.setPrefSize(ESC_WIDTH, ESC_HEIGHT);
        //bg
        Image bg = new Image(Objects.requireNonNull(getClass().getResource("/assets/ui/escapeMenu.png")).toExternalForm());
        ImageView bgView = new ImageView(bg);
        bgView.setFitWidth(ESC_WIDTH);
        bgView.setFitHeight(ESC_HEIGHT);

        //close button
        Button closeButton = new Button("X");
        closeButton.setOnAction(e ->{
            root.getChildren().remove(escapeMenuRootCont);
            isEscUp = false;
        });

        //add the back and recepies buttons
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            root.getChildren().remove(escapeMenuRootCont);
            SceneManager.loadScene("main menu");});

        Button recipesButton = new Button("Recipes");
        recipesButton.setOnAction(e -> {
            RecipesPane recipesPane = new RecipesPane(root);
        });

        //button pane
        Pane buttonPane = new Pane();
        buttonPane.setPrefSize(WIDTH, HEIGHT);

        buttonPane.getChildren().addAll(closeButton, backButton, recipesButton);


        closeButton.setLayoutX((double) ESC_WIDTH - 30);
        closeButton.setLayoutY(10);

        recipesButton.setLayoutX((double) ESC_WIDTH /2);
        recipesButton.setLayoutY((double) ESC_HEIGHT/2 -20);

        backButton.setLayoutX((double) ESC_WIDTH /2);
        backButton.setLayoutY((double) ESC_HEIGHT/2 +20);


        escapeParentRoot.getChildren().addAll(bgView, buttonPane);
        escapeParentRoot.setLayoutX((double) (BaseScene.WIDTH - ESC_WIDTH) /2);
        escapeParentRoot.setLayoutY((double) (BaseScene.HEIGHT - ESC_HEIGHT) /2);


        Image multiplyOverlayImg = new Image(Objects.requireNonNull(getClass().getResource("/assets/backgrounds/mixOverlay.png")).toExternalForm());
        ImageView multiplyOverlay = new ImageView(multiplyOverlayImg);
        multiplyOverlay.setFitWidth(BaseScene.WIDTH);
        multiplyOverlay.setFitHeight(BaseScene.HEIGHT);

        escapeMenuRootCont.getChildren().addAll(multiplyOverlay,escapeParentRoot);

    }
    private void setupBartender(Pane bartenderCont){
        bartender = new Sprite(432,584);

        URL folderURL;
        File folder;
        try{
            folderURL = getClass().getResource("/assets/character_sprite/");
            assert folderURL != null;
            folder = new File(folderURL.toURI());
        }
        catch (URISyntaxException e){
            System.err.println("⚠ Folder not found in resources!");
            return;
        }

        for (File file : Objects.requireNonNull(folder.listFiles())) {
            if (file.getName().toLowerCase().endsWith(".png")) {
                String stateName = file.getName().substring(0, file.getName().lastIndexOf('.'));
                String imagePath = getClass().getResource("/assets/character_sprite/" + file.getName()).toExternalForm();
                bartender.addState(stateName, imagePath);
            }
        }

        bartenderCont.getChildren().add(bartender);
        bartender.setLayoutX(BaseScene.WIDTH-530);
        bartender.setLayoutY(90);

        bartender.setState("idle");
    }
    private void setLiquidButtonOnClick(Button liquidButton, int index){
        liquidButton.setOnAction(e -> {
            selectedIndex = index;
            bartender.setState("choosing");

        });
    }
    private void setupLiquids(List<Liquid> liquids) {
        int rowCount = 0;
        int rowOffset = 0;
        for(int i = 0; i < liquids.size(); i++) {
            if(i % 6 == 0){
                rowCount++;
                rowOffset = 0;
            }

            //load the image
            Image liquidIcon = new Image(Objects.requireNonNull(getClass().getResource("/assets/liquid_sprite/"+liquids.get(i).getName()+".png")).toExternalForm());
            ImageView imgView = new ImageView(liquidIcon);
            imgView.setFitWidth(70);
            imgView.setFitHeight(122.42);
            imgView.setPreserveRatio(true);

            Button liquidButton = new Button();
            liquidButton.setGraphic(imgView);

            liquidButton.setStyle("-fx-background-color: transparent;"); // remove default gray button bg

            liquidButton.setPrefSize(70,122.42);
            liquidButton.setAlignment(Pos.CENTER);
            setLiquidButtonOnClick(liquidButton, i);


            //add the button to the grid
            liquidGrid.add(liquidButton, rowOffset, rowCount);

            rowOffset++;
        }
    }

    private Button setupControlButtonGraphics(Font font, String labelText){
        //make the four control buttons
        Button controlButton = new Button();

        Sprite controlButtonSprite = new Sprite(154,95);
        controlButtonSprite.addState("idle", "/assets/ui/controlButtonHover/buttonBase_00033.png");
        controlButtonSprite.addStateAnimation("hover", "/assets/ui/controlButtonHover/");

        controlButtonSprite.setState("idle");

        Label controlButtonLabel = new Label(labelText);
        //white with balck outline
        controlButtonLabel.setStyle("-fx-text-fill: white; -fx-effect: dropshadow( gaussian , black , 5 , 0.5 , 0 , 0 );");

        controlButtonLabel.setFont(font);
        controlButtonLabel.setAlignment(Pos.CENTER);


        StackPane resetButtonGraphicPane = new StackPane();
        resetButtonGraphicPane.setPrefSize(154,95);
        resetButtonGraphicPane.getChildren().addAll(controlButtonSprite,controlButtonLabel);

        controlButton.setStyle("-fx-background-color: transparent;");
        controlButton.setPrefSize(154,95);
        controlButton.setGraphic(resetButtonGraphicPane);


        controlButton.setOnMouseEntered(e -> {
            controlButtonSprite.setState("hover");
            controlButton.setScaleX(1.1);
            controlButton.setScaleY(1.1);
        });
        controlButton.setOnMouseExited(e -> {
            controlButtonSprite.setState("idle");
            controlButton.setScaleX(1);
            controlButton.setScaleY(1);
        });

        return controlButton;
    }
    private void setupUI(GridPane actionButtonGrid){
        Font controlButtonFont = Font.loadFont(
                getClass().getResource("/fonts/main.ttf").toExternalForm(),
                32
        );
        Button resetMixButton = setupControlButtonGraphics(controlButtonFont, "Reset");
        resetMixButton.setOnAction(e -> {
            if(currentStage != SERVE) {
                currentMix.reset();
                selectedIndex = 0;
                currentStage = POUR;

                bartender.setState("idle");
            }
        });



        Button pourButton = setupControlButtonGraphics(controlButtonFont, "Pour");
        new PourButtonLogic(pourButton,bartender, poured -> {
            double amountToPut = 0;
            if(currentMix.getCurrentVolume() < 0.5) {
                //calculate the viscoucity of the poured liquid
                if(availableLiquids.get(selectedIndex).getType().equals("SPIRIT")){
                    poured *= 0.03;
                }
                else{
                    poured *= 0.2;
                }
                amountToPut = currentMix.get(availableLiquids.get(selectedIndex)) + poured;
                amountToPut = amountToPut + currentMix.getCurrentVolume() >= 0.5 ? 0.5 - currentMix.getCurrentVolume() : amountToPut;
                currentMix.put(availableLiquids.get(selectedIndex), amountToPut);
            }
            System.out.println("Poured yet: " + String.format("%.3f l",amountToPut));

            bartender.setState("choosing");
        });

        Button shakeButton = setupControlButtonGraphics(controlButtonFont, "Shake");
        shakeButton.setOnAction(e -> {
            if(currentStage != SERVE && currentMix.getCurrentVolume() > 0){
                currentStage = SHAKE;
                System.out.println("shaking");

                bartender.setState("shaking");
            }
        });

        Button serveButton = setupControlButtonGraphics(controlButtonFont, "Serve");
        serveButton.setOnAction(e -> {
            if(currentStage == SHAKE){
                currentStage = SERVE;
                System.out.println("serving " + currentMix.toString());

                //rate the mix
                RateDrink rating = new RateDrink(currentMix);
                System.out.println("rating: \n" + rating.toString());
                currentStage = POUR;

                bartender.setState(rating.getReaction());

                Recipe newRecipe = new Recipe();
                newRecipe.mix = currentMix;
                newRecipe.rating = rating.getRating();
                newRecipe.reaction = rating.getReaction();
                try {
                    RecipeSerializer.saveRecipeToFile(newRecipe, "src/main/resources/assets/recipes.ser");
                } catch (IOException ex) {
                    //thats fine.
                } catch (ClassNotFoundException ex) {

                }

                currentMix.reset();
            }
        });

        //Add action buttons to a grid for better layout
        //RESET----POUR----SHAKE----SERVE

        actionButtonGrid.setHgap(6);
        actionButtonGrid.setVgap(-80);
        actionButtonGrid.setAlignment(Pos.CENTER);
        actionButtonGrid.add(resetMixButton, 0, 1);
        actionButtonGrid.add(pourButton, 1, 0);
        actionButtonGrid.add(shakeButton, 2, 1);
        actionButtonGrid.add(serveButton, 3, 0);
    }

    @Override
    protected StackPane createContainer() {
        //root container of all objects
        StackPane root = new StackPane();

        //background
        Image bg = new Image(Objects.requireNonNull(getClass().getResource("/assets/backgrounds/mixMenu.png")).toExternalForm());
        ImageView bgView = new ImageView(bg);
        bgView.setFitWidth(BaseScene.WIDTH);
        bgView.setFitHeight(BaseScene.HEIGHT);

        //LIQUID ICON(button)S----------------
        //load the liquids from the file
        try {
            availableLiquids = LiquidLoader.loadDrinks("./src/main/resources/assets/liquids.txt");
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        //make the buttons, from loading the liquid sprites from name
        liquidGrid = new GridPane();
        setupLiquids(availableLiquids);


        liquidGrid.setHgap(0);
        liquidGrid.setVgap(8);
        liquidGrid.setPadding(new Insets(20));
        liquidGrid.setLayoutX(100);
        liquidGrid.setLayoutY(90);

        //Bartender
        Pane bartenderCont = new Pane();
        bartenderCont.setPrefSize(BaseScene.WIDTH, BaseScene.HEIGHT);
        setupBartender(bartenderCont);

        //UI
        GridPane actionButtonGrid = new GridPane();
        setupUI(actionButtonGrid);
        actionButtonGrid.setLayoutX(60);
        actionButtonGrid.setLayoutY(BaseScene.HEIGHT - 140);


        //put all the parts together and position
        Pane buttonPane = new Pane();
        buttonPane.setPrefSize(BaseScene.WIDTH, BaseScene.HEIGHT);

        buttonPane.getChildren().addAll(liquidGrid,actionButtonGrid);

        //add everything to root
        root.getChildren().addAll(bgView, bartenderCont ,buttonPane);

        return root;
    }
}
