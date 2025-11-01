package helperClasses;

import main.Liquid;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class MixFileHandler {
    
    public static List<List<Hashtable<Liquid,Double>>> getMixeFromFile(String filePath, String liquidsPath) {
        List<List<Hashtable<Liquid,Double>>> mixes = new ArrayList<>();
        List<List<String>> allStrings = new ArrayList<>();
        List<List<Double>> allDoubles = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.trim().split("\\s+");
                List<String> strGroup = new ArrayList<>();
                List<Double> dblGroup = new ArrayList<>();

                for (int i = 0; i < parts.length; i += 2) {
                    strGroup.add(parts[i]);
                    dblGroup.add(Double.parseDouble(parts[i + 1]));
                }

                allStrings.add(strGroup);
                allDoubles.add(dblGroup);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //get the mix of liquids
        List<Liquid> allLiquids;
        try {
            allLiquids = LiquidLoader.loadDrinks(liquidsPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for(int i = 0; i < allStrings.size(); i++) {
            List<Hashtable<Liquid, Double>> mix = new ArrayList<>();
            for (int j = 0; j < allStrings.get(i).size(); j++) {
                String liquidName = allStrings.get(i).get(j);
                Double proportion = allDoubles.get(i).get(j);

                try {
                    mix.add(new Hashtable<>() {{
                        put(allLiquids.stream()
                                .filter(l -> l.getName().equals(liquidName))
                                .findFirst()
                                .orElseThrow(), proportion);
                    }});
                }
                catch (Exception e) {
                    System.out.println("Liquid " + liquidName + " not found in liquids list.");
                    mix.removeLast();
                    break;
                }
            }
            mixes.add(mix);
        }
        return mixes;
    }
    public static void saveMixesToFile(List<List<Hashtable<Liquid,Double>>> mixes, String filePath) {
        // Placeholder for saving mixes to file logic
    }
}
