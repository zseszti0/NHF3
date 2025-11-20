package graphics;

import helperClasses.AnimationPresets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


import java.util.HashMap;
import java.util.Map;

public class Sprite extends ImageView {
    private final Map<String, Image> frames = new HashMap<>();
    private String currentFrameKey;

    private final int WIDTH;
    private final int HEIGHT;

    public Sprite(int w, int h) {
        WIDTH = w;
        HEIGHT = h;
    }

    public void addState(String name, String imagePath) {
        frames.put(name,new Image(imagePath));
    }

    public void setState(String name) {
        Image img = frames.get(name);
        if (img != null) {
            setImage(img);
            currentFrameKey = name;

            setFitWidth(WIDTH);
            setFitHeight(HEIGHT);

            AnimationPresets.TwitchDownAndUp(this);
        }
    }

    public String getCurrentState() {
        return currentFrameKey;
    }
}
