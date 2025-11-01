package main;

import HelperClasses.LiquidLoader;
import HelperClasses.RateDrink;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;


public class Main extends Application {
    @Override
    public void start(Stage stage) {
        stage.setScene(new Scene(new Label("JavaFX is working!"), 300, 200));
        stage.show();
    }

    public static void main(String[] args) {
        //launch(args);

        List<Liquid> allLiquids;
        try {
            allLiquids = LiquidLoader.loadDrinks("C:/Users/zsesz/IdeaProjects/JavaHF/src/assets/liquids.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        //testing rating system
        List<Hashtable<Liquid,Double>> mix = new ArrayList<>();
        mix.add(new Hashtable<>() {{
            put(allLiquids.stream()
                    .filter(l -> l.getName().equals("white_rum"))
                    .findFirst()
                    .orElseThrow(), 0.25);
        }});
        mix.add(new Hashtable<>() {{
            put(allLiquids.stream()
                    .filter(l -> l.getName().equals("coconut_milk"))
                    .findFirst()
                    .orElseThrow(), 0.25);
        }});


        RateDrink rd = new RateDrink(mix);
        System.out.println(rd.getRating());
        System.out.println(rd.getAttributes());
    }
}
