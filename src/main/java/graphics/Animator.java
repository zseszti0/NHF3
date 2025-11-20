package graphics;

import helperClasses.AnimationLockRegistry;
import javafx.animation.*;
import javafx.beans.value.WritableValue;
import javafx.scene.Node;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class Animator {

    private final Node target;
    private final List<KeyFrame> frames = new ArrayList<>();

    public Animator(Node target) {
        this.target = target;
    }

    public Animator addKeyFrame(Duration d, KeyValue... kvs) {
        frames.add(new KeyFrame(d, kvs));
        return this;
    }

    public void play() {
        // Collect all properties that this animation wants to modify
        List<WritableValue<?>> props = new ArrayList<>();
        for (KeyFrame kf : frames) {
            for (KeyValue kv : kf.getValues()) {
                props.add(kv.getTarget());
            }
        }

        // BLOCK THIS ANIMATION if ANY property is already locked
        if (AnimationLockRegistry.anyPropertyLocked(target, props)) {
            System.out.println("⚠ Animation blocked: property conflict");
            return;
        }

        // Lock them before we start
        AnimationLockRegistry.lockProperties(target, props);

        // Create the actual timeline
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().addAll(frames);

        // Unlock after animation finishes
        timeline.setOnFinished(e -> AnimationLockRegistry.unlockProperties(target, props));

        timeline.play();
    }
}
