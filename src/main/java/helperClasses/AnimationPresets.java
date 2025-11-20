package helperClasses;

import graphics.Animator;
import javafx.animation.KeyValue;
import javafx.scene.Node;
import javafx.util.Duration;

public class AnimationPresets {
    public static void TwitchDownAndUp(Node target){
        Animator anim = new Animator(target);

        double startY = target.getLayoutY();

        anim.addKeyFrame(Duration.millis(0),
                new KeyValue(target.layoutYProperty(), startY)
        );

        anim.addKeyFrame(Duration.millis(100),
                new KeyValue(target.layoutYProperty(), startY+10)
        );

        anim.addKeyFrame(Duration.millis(200),
                new KeyValue(target.layoutYProperty(), startY)
        );

        anim.play();
    }
}
