package graphics;

import helperClasses.AnimationPresets;
import javafx.animation.Interpolator;
import javafx.animation.KeyValue;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;


import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

public class Sprite extends ImageView {
    private final Map<String, List<Image>> frames = new HashMap<>();
    private String currentFrameKey;

    private final int WIDTH;
    private final int HEIGHT;

    private Animator currentAnimator;


    public Sprite(int w, int h) {
        WIDTH = w;
        HEIGHT = h;
    }

    public void addState(String name, String imagePath) {
        List<Image> newStateImage = new ArrayList<>();
        newStateImage.add(new Image(imagePath));
        frames.put(name, newStateImage);
    }

    public void addStateAnimation(String name, String folderPath) {
        URL folderURL;
        File folder;
        List<Image> newStateAnim = new ArrayList<>();

        try {
            folderURL = getClass().getResource(folderPath);
            assert folderURL != null;
            folder = new File(folderURL.toURI());
        } catch (URISyntaxException e) {
            System.err.println("⚠ Folder not found in resources!");
            return;
        }

        for (File file : Objects.requireNonNull(folder.listFiles())) {
            if (file.getName().toLowerCase().endsWith(".png")) {
                String imagePath = getClass().getResource(folderPath + file.getName()).toExternalForm();
                newStateAnim.add(new Image(imagePath));
            }
        }
        frames.put(name, newStateAnim);
    }

    public void setState(String name) {
        setState(name, false);
    }

    public void setState(String name, boolean cascade) {
        if (!cascade && currentAnimator != null) {
            currentAnimator.stop();
        }

        List<Image> frameList = frames.get(name);
        if (frameList == null || frameList.isEmpty()) return;

        Image img = frameList.getFirst();
        if (img != null) {
            setImage(img);
            currentFrameKey = name;

            setFitWidth(WIDTH);
            setFitHeight(HEIGHT);

            if (frames.get(name).size() == 1) {
                Animator anim = AnimationPresets.TwitchDownAndUp(this);
                if (cascade && currentAnimator != null) {
                    currentAnimator.cascadeAnimation(anim);
                } else {
                    currentAnimator = anim;
                    currentAnimator.play();
                }
            } else {
                startAnimation(name, cascade);
            }
        }
    }

    private void startAnimation(String stateName, boolean cascade) {
        List<Image> animFrames = frames.get(stateName);
        if (animFrames == null || animFrames.isEmpty()) return;

        Animator newAnim = new Animator(this);
        // Default to approx 24 FPS (41ms)
        double frameDuration = 16.0;

        for (int i = 0; i < animFrames.size(); i++) {
            newAnim.addKeyFrame(Duration.millis(i * frameDuration),
                    new KeyValue(this.imageProperty(), animFrames.get(i), Interpolator.DISCRETE)
            );
        }

        if (cascade && currentAnimator != null) {
            currentAnimator.cascadeAnimation(newAnim);
        } else {
            if (currentAnimator != null) currentAnimator.stop();
            currentAnimator = newAnim;
            currentAnimator.play();
        }
    }
}
