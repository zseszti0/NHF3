package graphics;

import helperClasses.LiquidLoader;
import helperClasses.MixStage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import main.Liquid;
import main.Mix;

import javax.swing.*;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static helperClasses.MixStage.POUR;
import static helperClasses.MixStage.SHAKE;
import static helperClasses.MixStage.SERVE;


public class MixScene extends BaseScene{
    public static final int ESC_WIDTH = 820;
    public static final int ESC_HEIGHT = 580;
    
    private Pane escapeMenuRootCont;
    private boolean isEscUp = false;

    private int selectedIndex = -1;
    private Mix currentMix;
    private MixStage currentStage = POUR;

    public MixScene() {
        super(SceneManager.mainStage, "mix");


        scene.setOnKeyPressed(event -> {
            if(event.getCode() == javafx.scene.input.KeyCode.ESCAPE) {
                if(isEscUp) {
                    root.getChildren().remove(escapeMenuRootCont);
                    isEscUp = false;
                }
                else {
                    root.getChildren().add(escapeMenuRootCont);
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
    private void setupLiquids(GridPane liquidGrid, List<Liquid> liquids) {
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
            int finalI = i;
            liquidButton.setOnAction(e -> {
                selectedIndex = finalI;
            });


            //add the button to the grid
            liquidGrid.add(liquidButton, rowOffset, rowCount);

            rowOffset++;
        }
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
        List<Liquid> liquids;
        try {
            liquids = LiquidLoader.loadDrinks("./src/main/resources/assets/liquids.txt");
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        //make the buttons, from loading the liquid sprites from name
        GridPane liquidGrid = new GridPane();
        setupLiquids(liquidGrid,liquids);


        liquidGrid.setHgap(0);
        liquidGrid.setVgap(8);
        liquidGrid.setPadding(new Insets(20));

        //make the four control buttons
        Button resetMixButton = new Button("Reset");
        resetMixButton.setOnAction(e -> {
            if(currentStage != SERVE) {
                currentMix.reset();
                selectedIndex = -1;
                currentStage = POUR;
            }
        });

        Button pourButton = new Button("Pour");
        pourButton.setOnAction(e -> {
            if(selectedIndex == -1 || currentStage != POUR){
                System.out.println("meow meow");
            }
            else{

            }
        });

        Button shakeButton = new Button("Shake");
        shakeButton.setOnAction(e -> {
            if(currentStage != SHAKE){
                currentStage = SHAKE;
                System.out.println("shaking");
            }
        });

        Button serveButton = new Button("Serve");
        serveButton.setOnAction(e -> {

        });


        //put the grid in a pane
        Pane buttonPane = new Pane();
        buttonPane.setPrefSize(BaseScene.WIDTH, BaseScene.HEIGHT);


        buttonPane.getChildren().add(liquidGrid);
        liquidGrid.setLayoutX(100);
        liquidGrid.setLayoutY(90);

        //add everything to root
        root.getChildren().addAll(bgView, buttonPane);

        return root;
    }
}
