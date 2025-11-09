package main;

import graphics.MainMenuScene;
import graphics.MixScene;
import graphics.SceneManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;


public class Main extends Application {
    @Override
    public void start(Stage stage) {
        //make the main stage (window)
        SceneManager.setStage(stage);
        stage.setResizable(false);




        //make the menus and add them to the SM
        MainMenuScene mainMenu = new MainMenuScene();
        SceneManager.addScene("main menu", mainMenu);

        //mix
        MixScene mix = new MixScene();
        SceneManager.addScene("mix", mix);


        //load the main menu as default
        SceneManager.loadScene("main menu");
        stage.setTitle("Bar Simulator 🍸");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
