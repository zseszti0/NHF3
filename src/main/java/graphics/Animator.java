package graphics;

import helperClasses.AnimationLockRegistry;
import javafx.animation.*;
import javafx.beans.value.WritableValue;

import javafx.scene.Node;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class Animator {

    private Node target;
    private final List<KeyFrame> frames = new ArrayList<>();

    private Timeline timeline;



    public Animator(Node target) {

        this.target = target;

    }

    private static List<WritableValue<?>> getProps(List<KeyFrame> fr){
        List<WritableValue<?>> props = new ArrayList<>();
        for(KeyFrame kf : fr){
            for(KeyValue kv : kf.getValues()){
                props.add(kv.getTarget());
            }
        }
        return props;
    }


    public Animator addKeyFrame(Duration d, KeyValue... kvs) {
        frames.add(new KeyFrame(d, kvs));
        return this;
    }

    public void cascadeAnimation(Animator anim){
        List<KeyFrame> newFrames = anim.getFrames();
        List<KeyFrame> timeAdjustedNewFrames = new ArrayList<>();

        for (KeyFrame kf : newFrames) {
            Duration newDuration = kf.getTime().add(timeline.getTotalDuration());
            List<KeyValue> newValues = new ArrayList<>(kf.getValues());
            for(KeyValue kv: newValues){
                timeAdjustedNewFrames.add(new KeyFrame(newDuration, kv));
            }
        }

        frames.addAll(timeAdjustedNewFrames);

        List<WritableValue<?>> allProps = getProps(frames);

        AnimationLockRegistry.lockProperties(target,allProps);

        timeline.getKeyFrames().addAll(frames);
        Timeline appendedTimeline = new Timeline(timeline.getKeyFrames().toArray(new KeyFrame[0]));
        Duration currentTime = timeline.getCurrentTime();

        timeline = appendedTimeline;
        timeline.playFrom(currentTime);

        timeline.setOnFinished(e -> {
            AnimationLockRegistry.unlockProperties(target,allProps);
        });
    }

    private List<KeyFrame> getFrames() {
        return frames;
    }

    public void play() {
        // Collect all properties that this animation wants to modify
        List<WritableValue<?>> props = getProps(frames);

        // BLOCK THIS ANIMATION if ANY property is already locked
        if (AnimationLockRegistry.anyPropertyLocked(target, props)) {
            System.out.println("⚠ Animation blocked: property conflict");
            return;
        }

        // Lock them before we start
        AnimationLockRegistry.lockProperties(target, props);

        // Create the actual timeline
        timeline = new Timeline();
        timeline.getKeyFrames().addAll(frames);

        // Unlock after animation finishes
        timeline.setOnFinished(e -> {
            AnimationLockRegistry.unlockProperties(target,props);
        });
        timeline.play();
    }

    public void stop() {
        if (timeline != null) {
            timeline.stop();

            // Collect all properties that this animation modified to unlock them manually
            List<WritableValue<?>> props = getProps(frames);

            AnimationLockRegistry.unlockProperties(target, props);
        }
    }
}
