package graphics;

import helperClasses.AnimationPresets;
import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

public class Sprite extends ImageView {
    private final Map<String, List<Image>> frames = new HashMap<>();
    private String currentFrameKey;

    private final int WIDTH;
    private final int HEIGHT;

    private AnimationTimer animationTimer;
    private long lastFrameTime = 0;
    private int frameIndex = 0;
    private double frameDuration = 16_666_666; // 120 ms per frame (in nanoseconds)


    public Sprite(int w, int h) {
        WIDTH = w;
        HEIGHT = h;
    }

    public void addState(String name, String imagePath) {
        List<Image> newStateImage = new ArrayList<>();
        newStateImage.add(new Image(imagePath));
        frames.put(name,newStateImage);
    }

    public void addStateAnimation(String name, String folderPath) {
        URL folderURL;
        File folder;
        List<Image> newStateAnim = new ArrayList<>();

        try{
            folderURL = getClass().getResource(folderPath);
            assert folderURL != null;
            folder = new File(folderURL.toURI());
        }
        catch (URISyntaxException e){
            System.err.println("⚠ Folder not found in resources!");
            return;
        }

        for (File file : Objects.requireNonNull(folder.listFiles())) {
            if (file.getName().toLowerCase().endsWith(".png")) {
                String imagePath = getClass().getResource(folderPath + file.getName()).toExternalForm();
                newStateAnim.add(new Image(imagePath));
            }
        }
        frames.put(name,newStateAnim);
    }

    public void setState(String name) {
        if (animationTimer != null)
            animationTimer.stop();

        Image img = frames.get(name).getFirst();
        if (img != null) {
            setImage(img);
            currentFrameKey = name;

            setFitWidth(WIDTH);
            setFitHeight(HEIGHT);

            if(frames.get(name).size() == 1)
                AnimationPresets.TwitchDownAndUp(this);
            else{

                startAnimation(name);
            }
        }
    }

    private void startAnimation(String stateName) {
        List<Image> animFrames = frames.get(stateName);
        frameIndex = 0;

        if (animationTimer != null) animationTimer.stop();

        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {

                if (now - lastFrameTime >= frameDuration) {
                    lastFrameTime = now;

                    // Set current frame
                    setImage(animFrames.get(frameIndex));

                    // Advance frame if not at the last one
                    if (frameIndex < animFrames.size() - 1) {
                        frameIndex++;
                    } else {
                        // Stop on last frame
                        animationTimer.stop();
                    }
                }
            }
        };

        animationTimer.start();
    }


    public String getCurrentState() {
        return currentFrameKey;
    }
}
