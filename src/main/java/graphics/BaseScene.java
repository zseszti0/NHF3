package graphics;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public abstract class BaseScene {
    protected String name;
    protected final Scene scene;

    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;

    public BaseScene(Stage stage, String name) {
        this.name = name;
        this.scene = new Scene(createContainer(), WIDTH, HEIGHT);
    }

    protected abstract Parent createContainer();

    public void show() {
        SceneManager.mainStage.setScene(scene);
    }
    public String getName() {
        return name;
    }
}
