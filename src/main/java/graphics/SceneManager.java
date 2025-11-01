package graphics;

import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.HashMap;

public class SceneManager {
    static Stage mainStage;
    private static final HashMap<String, BaseScene> scenes = new HashMap<>();

    public static void setStage(Stage stage) {
        mainStage = stage;
    }

    public static void addScene(String name, BaseScene newScene) {
        scenes.put(name, newScene);
    }

    public static void loadScene(String name) {
        scenes.get(name).show();
    }


    public SceneManager() {
        mainStage = new Stage();
    }
}
