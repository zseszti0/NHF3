package helperClasses;

import graphics.Animator;
import javafx.animation.KeyValue;
import javafx.scene.Node;
import javafx.util.Duration;

public class AnimationPresets {
    public static Animator TwitchDownAndUp(Node target){
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

        return anim;
    }

    public static Animator EscapeMenuAppear(Node target){
        Animator anim = new Animator(target);

        //starts invisible, squashed down, with slightly smaller scaling, and 0.5 opacity

        anim.addKeyFrame(Duration.millis(0),
                new KeyValue(target.scaleXProperty(), 0.5),
                new KeyValue(target.scaleYProperty(), 0),
                new KeyValue(target.opacityProperty(), 0.5)
        );
        anim.addKeyFrame(Duration.millis(180),
                new KeyValue(target.scaleXProperty(), 0.8),
                new KeyValue(target.scaleYProperty(), 0.8)
        );
        anim.addKeyFrame(Duration.millis(230),
                new KeyValue(target.scaleXProperty(), 1.1),
                new KeyValue(target.scaleYProperty(), 1.1),
                new KeyValue(target.opacityProperty(), 1)
        );
        anim.addKeyFrame(Duration.millis(300),
                new KeyValue(target.scaleXProperty(), 1),
                new KeyValue(target.scaleYProperty(), 1)
        );

        return anim;
    }
}
