package main;

import java.io.Serializable;

public class Liquid implements Serializable {
    private final String name;
    private final String type; // "alcohol" or "mixer"
    private final double alcohol;
    private final double bitterness;
    private final double sweetness;
    private final double funness;
    private final double creativity;

    public Liquid(String name, String type, double alcohol, double bitterness,
                 double sweetness, double funness, double creativity) {
        this.name = name;
        this.type = type;
        this.alcohol = alcohol;
        this.bitterness = bitterness;
        this.sweetness = sweetness;
        this.funness = funness;
        this.creativity = creativity;
    }

    // Getters only (immutable objects are easier to reason about)
    public String getName() { return name; }
    public String getFormatedName() {
        //format the name, capital the first letter of each word, replace _ with space
        String[] words = name.split("_");
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            sb.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(" ");
        }

        return sb.toString().trim();
    }
    public String getType() { return type; }
    public double getAlcohol() { return alcohol; }
    public double getBitterness() { return bitterness; }
    public double getSweetness() { return sweetness; }
    public double getFunness() { return funness; }
    public double getCreativity() { return creativity; }
}
