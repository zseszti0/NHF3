package graphics;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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
        //root container of all objects
        StackPane root = new StackPane();

        //background
        Image bg = new Image(Objects.requireNonNull(getClass().getResource("/assets/backgrounds/mainMenu.png")).toExternalForm());
        ImageView bgView = new ImageView(bg);
        bgView.setFitWidth(BaseScene.WIDTH);
        bgView.setFitHeight(BaseScene.HEIGHT);

        //3 menu buttons
        VBox buttonCont = new VBox();
        buttonCont.setPrefHeight(400);
        buttonCont.setPrefWidth(200);

        //init the buttons
        Button startButton = new Button("Mix!");
        startButton.setOnAction(e -> SceneManager.loadScene("mix")); //temporary action

        Button recipesButton = new Button("Recipes");
        recipesButton.setOnAction(e -> {
            RecipesPane recipesPane = new RecipesPane(root);
        });

        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e -> System.exit(0));

        buttonCont.getChildren().addAll(startButton, recipesButton, exitButton);
        buttonCont.setSpacing(20); // space between buttons
        buttonCont.setAlignment(Pos.CENTER);

        //position UI elemnts via Panes (inside StackPane)

        //buttonCont
        Pane buttonContPane = new Pane();
        buttonContPane.setPrefSize(BaseScene.WIDTH, BaseScene.HEIGHT);

        buttonContPane.getChildren().add(buttonCont);
        buttonCont.setLayoutX(20);
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
