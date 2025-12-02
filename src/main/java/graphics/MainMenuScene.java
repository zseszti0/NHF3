package graphics;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.geometry.Pos;

import javax.swing.text.Position;
import javafx.scene.control.Button;

import java.io.IOException;
import java.util.Objects;


public class MainMenuScene extends BaseScene{
    public MainMenuScene() {
        super(SceneManager.mainStage, "main menu");
    }

    @Override
    protected StackPane createContainer() {
        //mainFont
        Font mainFont = Font.loadFont(
                getClass().getResource("/fonts/main.ttf").toExternalForm(),
                32
        );

        //root container of all objects
        StackPane root = new StackPane();

        //background
        Image bg = new Image(Objects.requireNonNull(getClass().getResource("/assets/backgrounds/mainMenu.png")).toExternalForm());
        ImageView bgView = new ImageView(bg);
        bgView.setFitWidth(BaseScene.WIDTH);
        bgView.setFitHeight(BaseScene.HEIGHT);

        //3 menu buttons
        VBox buttonCont = new VBox();
        buttonCont.setPrefHeight(700);
        buttonCont.setPrefWidth(200);

        //init the buttons
        Button startButton = MixScene.setupControlButtonGraphics(mainFont, "Mix!",154,95,"/assets/ui/controlButtonHover/buttonBase_00033.png");
        startButton.setOnAction(e -> SceneManager.loadScene("mix")); //temporary action

        Button recipesButton = MixScene.setupControlButtonGraphics(mainFont, "Recipes",154,95,"/assets/ui/controlButtonHover/buttonBase_00033.png");
        recipesButton.setOnAction(e -> {
            RecipesPane recipesPane = new RecipesPane(root, isPaneOpen -> {});
        });

        Button exitButton = MixScene.setupControlButtonGraphics(mainFont, "Exit",154,95,"/assets/ui/controlButtonHover/buttonBase_00033.png");
        exitButton.setOnAction(e -> System.exit(0));

        buttonCont.getChildren().addAll(startButton, recipesButton, exitButton);
        buttonCont.setSpacing(20); // space between buttons
        buttonCont.setAlignment(Pos.CENTER);

        //position UI elemnts via Panes (inside StackPane)

        //buttonCont
        Pane buttonContPane = new Pane();
        buttonContPane.setPrefSize(BaseScene.WIDTH, BaseScene.HEIGHT);

        buttonContPane.getChildren().add(buttonCont);
        buttonCont.setLayoutX(150);
        buttonCont.layoutYProperty().bind(
                buttonContPane.heightProperty()
                        .subtract(buttonCont.heightProperty())
                        .divide(2)
        );



        //add everything to root
        root.getChildren().addAll(bgView,buttonContPane);

        return root;
    }
}
