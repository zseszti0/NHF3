package helperClasses;

import graphics.Sprite;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.util.Duration;
import java.util.function.Consumer;

public class PourButtonLogic {
    private final Button pourButton;
    private final Consumer<Double> onPourFinished; // callback
    private double pouredAmount = 0.0;
    private Timeline pouringTimeline;

    private Sprite bartender;



    public PourButtonLogic(Button pourButton, Sprite bartender, Consumer<Double> onPourFinished) {
        this.pourButton = pourButton;
        this.onPourFinished = onPourFinished;
        this.bartender = bartender;
        setupPourButton();
    }

    private void setupPourButton() {
        pouringTimeline = new Timeline(
                new KeyFrame(Duration.millis(100), e -> pourStep(0.1))
        );
        pouringTimeline.setCycleCount(Timeline.INDEFINITE);

        pourButton.setOnMousePressed(e -> startPouring());
        pourButton.setOnMouseReleased(e -> stopPouring());
    }

    private void startPouring() {
        pouredAmount = 0;
        pouringTimeline.play();

        bartender.setState("filling");
    }

    private void stopPouring() {
        pouringTimeline.stop();

        // send result to parent
        if (onPourFinished != null)
            onPourFinished.accept(pouredAmount);

        bartender.setState("choosing");
    }

    private void pourStep(double deltaTime) {
        pouredAmount +=  deltaTime;
    }
}