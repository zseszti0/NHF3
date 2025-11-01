package main;

import helperClasses.MixFileHandler;
import helperClasses.LiquidLoader;

import java.io.IOException;
import java.util.List;

public class RateTestingMiniApp {
    public static void main(String[] args) {
        List<Liquid> allLiquids;
        try {
            allLiquids = LiquidLoader.loadDrinks("./src/main/resources/assets/liquids.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //MixFileHandler testsFromFile = new MixFileHandler("./src/main/resources/assets/ratesTest.txt", "./src/main/resources/assets/liquids.txt");
    }
}
