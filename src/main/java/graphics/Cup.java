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
            20.0, 0.0,   // top left
            100.0, 0.0,  // top right
            80.0, 150.0, // bottom right
            40.0, 150.0  // bottom left
    );

    private Rectangle fillMask = new Rectangle(120, 150);

    private StackPane cupRoot = new StackPane();


    public Cup(double max, Color color){
        maxVolume = max;

        // Color of the liquid
        liquidShape.setFill(color);

        fillMask.setY(150 - fillMask.getHeight());
        liquidShape.setClip(fillMask);

        fillMask.heightProperty().bind(
                currentVolume.divide(maxVolume).multiply(150) // polygon height
        );

        ImageView cupImage = new ImageView(new Image("assets/ui/cup.png"));
        cupImage.setFitWidth(120);
        cupImage.setFitHeight(160);

        cupRoot.getChildren().addAll(liquidShape, cupImage);
    }

    public void updateFill(double newValue){
        currentVolume.set(
                Math.min(maxVolume, newValue)
        );
    }

    public StackPane getRoot(){
        return cupRoot;
    }
}
