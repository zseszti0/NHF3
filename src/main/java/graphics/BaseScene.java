package graphics;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public abstract class BaseScene {
    protected String name;
    protected final Scene scene;
    protected StackPane root;

    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;

    public BaseScene(Stage stage, String name) {
        this.name = name;
        this.root = createContainer();
        this.scene = new Scene(root, WIDTH, HEIGHT);
    }

    protected abstract StackPane createContainer();

    public void show() {
        SceneManager.mainStage.setScene(scene);
    }
    public String getName() {
        return name;
    }

}
