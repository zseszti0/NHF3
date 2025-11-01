package HelperClasses;

import main.Liquid;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import static java.lang.Math.abs;

public class RateDrink {
    private final List<Hashtable<Liquid,Double>> mix;

    private double rating = 0;
    private String reaction = "";

    private double taste = 0;
    private double funnes = 0;
    private double creativity = 0;
    private double craziness = 0;


    private double clamp(double value) {
        return Math.max(0, Math.min(5, value));
    }

    private void calculateMixStats() {
        // Step 1: Compute averages
        double totalVolume = 0;
        double alcohol = 0, bitter = 0, sweet = 0, fun = 0, creative = 0;

        for (Hashtable<Liquid, Double> entry : mix) {
            for (Liquid l : entry.keySet()) {
                double v = entry.get(l);
                totalVolume += v;
                alcohol += l.getAlcohol() * v;
                bitter += l.getBitterness() * v;
                sweet += l.getSweetness() * v;
                fun += l.getFunness() * v;
                creative += l.getCreativity() * v;
            }
        }
        if (totalVolume == 0) return; // avoid division by zero

        double avgAlcohol = alcohol / totalVolume;
        double avgSweet = sweet / totalVolume;
        double avgBitter = bitter / totalVolume;
        double avgFun = fun / totalVolume;
        double avgCreative = creative / totalVolume;
        int numIngredients = mix.size();

        taste = 5
                - abs(avgAlcohol - 15) / 15 * 2    // penalize too much/too little alcohol, peak around 15%
                - abs(avgBitter - avgSweet) // imbalance penalty
                - (numIngredients - 3) * 0.4;        // too many ingredients → worse
        taste = clamp(taste);

        int numAlcohols = 0;
        for (Hashtable<Liquid, Double> entry : mix) {
            for (Liquid l : entry.keySet()) {
                if (l.getType().equals("SPIRIT")) {
                    numAlcohols++;
                }
            }
        }
        funnes =
                3 * Math.tanh(avgAlcohol / 25.0)    // diminishing return on alcohol// variety of alcohols = more fun
                        + (numAlcohols == 0 ? -1 : 0)         // no alcohol = boring
                        + (taste < 2.0 ? 0.5 : 0);         // bad-tasting drinks are kinda fun
        funnes = clamp(funnes);

        creativity =
                3 * (numIngredients > 1 ? 1.0 - 1.0 / numIngredients : 0) // more than one ingredient adds some
                        + 2 * (Math.abs(avgSweet - avgBitter)               // unusual taste contrast
                        - (taste > 3.5 ? 1 : 0.5));
        creativity = clamp(creativity);

        double alcoholImpact = 5 * (avgAlcohol / 100.0);
        double mixImpact = Math.min(2.0, numIngredients * 0.3);
        double imbalanceImpact = Math.abs(avgBitter - avgSweet) * 0.5;

        craziness = Math.min(5.0, (1+(avgAlcohol/50))*(alcoholImpact + mixImpact + imbalanceImpact));
        craziness = clamp(craziness);


        //affect reaction
        funnes += 0.3 * craziness;    // the crazier it is, the more fun it gets
        funnes -= 0.1 * taste;        // too balanced = kinda boring
        funnes = clamp(funnes);
        creativity += 0.2 * craziness;   // chaos inspires creativity
        creativity -= 0.3 * taste;       // good taste = safe choice
        creativity = clamp(creativity);
        craziness += 0.2 * creativity;
        craziness += 0.1 * funnes;
        craziness -= 0.1 * taste;     // well-balanced drinks feel less crazy
        craziness = clamp(craziness);


        rating = (taste * 0.4) + (funnes * 0.2) + (creativity * 0.2) + (craziness * 0.2);
        rating = clamp(rating);

    }

    public RateDrink(List<Hashtable<Liquid,Double>> mix) {
        this.mix = mix;

        //calculate the rating
        calculateMixStats();
    }

    public double getRating() { return rating; }
    public String getReaction() { return reaction; }
    public List<Double> getAttributes(){
        ArrayList<Double> attributes = new ArrayList<>();
        attributes.add(taste);
        attributes.add(funnes);
        attributes.add(creativity);
        attributes.add(craziness);
        return attributes;
    }
}
