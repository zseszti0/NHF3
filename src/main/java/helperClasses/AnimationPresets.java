package helperClasses;

import graphics.Animator;
import javafx.animation.KeyValue;
import javafx.scene.Node;
import javafx.scene.control.Label;
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

    public static Animator RatingTextThinking(Label target){
        Animator anim = new Animator(target);

        //typewriter effect thinking ... for 3 seconds
        anim.addKeyFrame(Duration.millis(0), new KeyValue(target.textProperty(), ""));

        String thinkingText = "Thinking";
        for(int i = 1; i < thinkingText.length(); i++){
            anim.addKeyFrame(Duration.millis(i*300), new KeyValue(target.textProperty(), thinkingText.substring(0, i)));
        }

        for(int i = 0; i < 8; i++){
            String plusPoints = "Thinking";
            for(int j = 0; j < i%4; j++){
                plusPoints += ".";
            }
            anim.addKeyFrame(Duration.millis(2400+500*i), new KeyValue(target.textProperty(), plusPoints));

        }

        return anim;
    }

    public static Animator RateingTextRating(Label target, Double rating){
        Animator anim = new Animator(target);

        anim.addKeyFrame(Duration.millis(0),
                new KeyValue(target.textProperty(), ""));

        //speed counter effect

        int j = 0;
        for(double i = 0; i < rating; i+= 1/((2000/rating)/100)){
            String ratingText = String.format("%.2f ★", i);
            anim.addKeyFrame(Duration.millis(j*100), new KeyValue(target.textProperty(),ratingText));
            j++;
        }

        String finalRatingText = String.format("%.2f ★", rating);
        anim.addKeyFrame(Duration.millis(2000),
                new KeyValue(target.scaleXProperty(), target.getScaleX()),
                new KeyValue(target.scaleYProperty(), target.getScaleY()),
                new KeyValue(target.textProperty(),finalRatingText)
        );
        anim.addKeyFrame((Duration.millis(2000+100)),
                new KeyValue(target.scaleXProperty(), target.getScaleX()*0.8),
                new KeyValue(target.scaleYProperty(), target.getScaleY()*0.8)
        );
        anim.addKeyFrame((Duration.millis(2000+250)),
                new KeyValue(target.scaleXProperty(), target.getScaleX()*1.3),
                new KeyValue(target.scaleYProperty(), target.getScaleY()*1.3)
        );

        return anim;
    }
}
