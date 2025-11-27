package helperClasses;

import graphics.Cup;
import graphics.Sprite;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.util.Duration;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class PourButtonLogic {
    private final Button pourButton;
    private final Consumer<Double> onPourFinished; // callback
    private final Supplier<Double> getFlowRate;
    private final Supplier<Double> getBaseVolume;

    private double pouredAmount = 0.0;
    private double currentBaseVolume = 0.0;
    private double currentFlowRate = 0.0;
    private Timeline pouringTimeline;

    private Sprite bartender;

    private Cup cup;



    public PourButtonLogic(Button pourButton, Sprite bartender, Cup cup, Supplier<Double> getFlowRate, Supplier<Double> getBaseVolume, Consumer<Double> onPourFinished) {
        this.cup = cup;
        this.pourButton = pourButton;
        this.onPourFinished = onPourFinished;
        this.bartender = bartender;
        this.getFlowRate = getFlowRate;
        this.getBaseVolume = getBaseVolume;
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
        // Snapshot current state
        currentBaseVolume = getBaseVolume.get();
        currentFlowRate = getFlowRate.get();
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
        double volumeTick = deltaTime * currentFlowRate;
        pouredAmount += volumeTick;
        cup.updateFill(currentBaseVolume + pouredAmount);
    }
}