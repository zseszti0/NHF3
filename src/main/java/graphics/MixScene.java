package graphics;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class MixScene extends BaseScene{
    public MixScene() {
        super(SceneManager.mainStage, "main menu");
    }

    @Override
    protected Parent createContainer() {
        //root container of all objects
        StackPane root = new StackPane();

        //background
        Image bg = new Image(getClass().getResource("/assets/backgrounds/mixMenu.png").toExternalForm());
        ImageView bgView = new ImageView(bg);
        bgView.setFitWidth(BaseScene.WIDTH);
        bgView.setFitHeight(BaseScene.HEIGHT);

        //add everything to root
        root.getChildren().addAll(bgView);

        return root;
    }
}
