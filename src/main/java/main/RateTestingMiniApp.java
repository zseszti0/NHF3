package main;

import helperClasses.LiquidLoader;
import helperClasses.RateDrink;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RateTestingMiniApp {
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        List<Liquid> availableLiquids = LiquidLoader.loadDrinks("./src/main/resources/assets/liquids.txt");

        String filename = "src/main/resources/assets/ratesTest.txt"; // replace with your file path
        List<Mix> linesData = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.trim().split("\\s+");
                Mix table = new Mix();

                for (int i = 0; i < tokens.length - 1; i += 2) {
                    String key = tokens[i];
                    double value = Double.parseDouble(tokens[i + 1]);

                    Liquid toput = null;
                    for(Liquid l : availableLiquids) {
                        if(l.getName().equals(key)) {
                            toput = l;
                            break;
                        }
                    }

                    table.put(toput, value);
                }

                linesData.add(table);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Example: print all the hashtables
        for (Mix table : linesData) {
            RateDrink rater = new RateDrink(table);
            System.out.println(rater);
        }
    }
}
