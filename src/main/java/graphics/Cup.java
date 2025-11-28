package graphics;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;


public class Cup {
    private double maxVolume;
    private DoubleProperty currentVolume = new SimpleDoubleProperty(0.0);


    private Polygon liquidShape = new Polygon(
            3.5, 0.0,   // top left
            64.5, 0.0,  // top right
            51.5, 92.0, // bottom right
            16.5, 92.0  // bottom left
    );

    private Rectangle fillMask = new Rectangle(68, 92);

    private StackPane cupRoot = new StackPane();


    public Cup(double max){
        maxVolume = max;

        // Color of the liquid
        liquidShape.setFill(Color.WHITE);

        // Remove static Y setting. We will bind it below to animate from bottom up.
        liquidShape.setClip(fillMask);

        fillMask.heightProperty().bind(
                currentVolume.divide(maxVolume).multiply(90) // polygon height
        );

        // Bind Y so the mask grows from the bottom upwards
        fillMask.yProperty().bind(
                fillMask.heightProperty().multiply(-1).add(90)
        );

        ImageView cupImage = new ImageView(new Image("assets/ui/cup.png"));
        cupImage.setFitWidth(68);
        cupImage.setFitHeight(111);

        //grey bacgkround, with the same shape as the liquidShape
        Polygon fillBackground = new Polygon();
        fillBackground.getPoints().addAll(liquidShape.getPoints());
        fillBackground.setFill(Color.GREY);

        cupRoot.getChildren().addAll(fillBackground,liquidShape,cupImage);
    }

    public void updateFill(double newValue){
        currentVolume.set(
                Math.min(maxVolume, newValue)
        );
    }

    public StackPane getRoot(){
        return cupRoot;
    }

    public void setColor(Color color) {
         liquidShape.setFill(color);
    }
}
