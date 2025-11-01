package graphics;

import helperClasses.RecipesFileHandler;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import main.Liquid;
import javafx.scene.control.Button;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

public class RecipesPane {
    public static final int WIDTH = 820;
    public static final int HEIGHT = 580;

    private Pane recipesRootCont = new Pane();
    private StackPane parentRoot;
    private HashMap<List<Hashtable<Liquid,Double>>,List<Double>> recipes;

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

        //button pane cont
        Pane buttonContPane = new Pane();
        buttonContPane.setPrefSize(WIDTH, HEIGHT);
        buttonContPane.getChildren().add(closeButton);
        closeButton.setLayoutX((double) (BaseScene.WIDTH - WIDTH) /2 + WIDTH - 30);
        closeButton.setLayoutY((double) (BaseScene.HEIGHT - HEIGHT) /2 + 10);

        //add everything to recipesRootCont
        recipesRoot.getChildren().addAll(bgView, buttonContPane);

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
