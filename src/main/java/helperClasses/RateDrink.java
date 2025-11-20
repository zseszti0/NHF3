package helperClasses;

import main.Liquid;
import main.Mix;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import static java.lang.Math.abs;
import static java.lang.Math.clamp;

public class RateDrink {
    private final Hashtable<Liquid,Double> mix;

    private double rating = 0;
    private String reaction;

    private double taste = 0;
    private double funnes = 0;
    private double creativity = 0;
    private double craziness = 0;


    private double distributeBetween(double x, double inputMax, double min, double max, String type) {
        // Clamp x to [0, inputMax]
        x = Math.max(0, Math.min(x, inputMax));

        // Normalize input to [0, 1]
        double t = x / inputMax;

        double result;
        switch (type.toLowerCase()) {
            case "quad": // Quadratic growth
                result = min + (max - min) * (t * t);
                break;
            case "exp":  // Exponential growth
                double k = 5.0; // Growth rate; adjust for steeper/softer curve
                double expVal = (Math.exp(k * t) - 1) / (Math.exp(k) - 1);
                result = min + (max - min) * expVal;
                break;
            case "log": // Logarithmic growth (fast start, slow end)
                double kLog = 5.0; // controls curvature; higher = more flattening
                // Avoid log(0) by offsetting t slightly
                double logVal = Math.log(1 + kLog * t) / Math.log(1 + kLog);
                result = min + (max - min) * logVal;
                break;
            default: // "lin" or unrecognized: Linear growth
                result = min + (max - min) * t;
                break;
        }

        return result;
    }

    private double rateTaste(double alcohol, double avgSweet, double avgBitter, int numIngredients, int numAlcohols, int numMixers) {
        double perfectAlcoholRate = 0;
        if(0.025 < alcohol && alcohol < 0.045){
            perfectAlcoholRate = 1;
        } else if (0.015 < alcohol && alcohol <= 0.025) {
            perfectAlcoholRate = 0.8;
        } else if (alcohol < 0.01) {
            perfectAlcoholRate = 0;
        } else if (0.045 < alcohol && alcohol < 0.075) {
            perfectAlcoholRate = 0.3;
        } else {
            perfectAlcoholRate = 0;
        }

        double perfectSweetBitterRate = 0;
        double sweetBitterDiff = abs(avgSweet - (avgBitter*1.2));
        if(4.0 < sweetBitterDiff && sweetBitterDiff < 4.2){
            perfectSweetBitterRate = 1;
        } else if (3.0 < sweetBitterDiff && sweetBitterDiff < 4.0) {
            perfectSweetBitterRate = 0.6;
        } else if (2.0 < sweetBitterDiff && sweetBitterDiff < 3.0) {
            perfectSweetBitterRate = 0.1;
        } else {
            perfectSweetBitterRate = 0;
        }

        double ingredientCountRate = 0;
        if(numIngredients == 3){
            ingredientCountRate = 1;
        } else if (numIngredients == 2) {
            ingredientCountRate = 0.7;
        } else if (numIngredients == 4) {
            ingredientCountRate = 0.4;
        }else {
            ingredientCountRate = 0;
        }

        double alcoholMixerRate = 0;
        if(numAlcohols >= 1 && numMixers >= 1){
            alcoholMixerRate = 1;
        }

        double ret = ((perfectAlcoholRate*1.5) + (perfectSweetBitterRate*1.3) + (ingredientCountRate*0.7) + (alcoholMixerRate*0.5)) / 4 * 5;
        return clamp(ret,0,5);
    }
    private double rateCreativity(double avgCreativity, int numIngredients, int numAlcohols) {
        double creativityRate = distributeBetween(avgCreativity,5,0,1,"quad");
        if(creativityRate < 0.3){
            creativityRate = 0;
        }
        
        double balancedRate = 1;
        if(3 < taste && taste < 4){
            balancedRate = 0.3;
        } else if (4 <= taste) {
            balancedRate = 0;
        } else if(2 < taste) {
            balancedRate = 0.5;
        }
        
        double ingridientCountRate = 1;
        if(numIngredients <= 2 ) {
            ingridientCountRate = 0;
        } else if (numIngredients == 3 && creativityRate > 0.5) {
            ingridientCountRate = 0.9;
        }else if(numIngredients >= 7){
            ingridientCountRate = 1;
        } else if (numIngredients >= 4) {
            ingridientCountRate = 0.6;
        } else if(numAlcohols >= 3){
            ingridientCountRate = 0.5;
        }
        
        double ret = (creativityRate*1.5 + balancedRate*0.2 + ingridientCountRate*1.3) / 3 * 5;
        return clamp(ret,0,5);
    }
    private double rateFun(double alcohol, double avgFun, double avgCreative , int numAlcohols, int numMixers) {
        double alcoholContentRate = distributeBetween(alcohol, 0.215, 0,1, "exp");
        if(alcoholContentRate < 0.5){
            alcoholContentRate = 0;
        }
        
        double ingredientFunRate = distributeBetween(avgFun, 5,0,1,"quad");
        double ingredientAlcoholCountRate = distributeBetween(numAlcohols, 7,0,1,"exp");
        if(ingredientAlcoholCountRate < 0.2){
            ingredientAlcoholCountRate = 0;
        }
        
        double ingredientMixerRate = distributeBetween(numMixers, 5,0,1,"lin");
        if(ingredientAlcoholCountRate == 0){
            ingredientMixerRate = 0;
        }
        
        double ingredientCreativityRate = avgCreative;
        if(ingredientCreativityRate < 0.5){
            ingredientCreativityRate = 0;
        }
        
        double tasteRate = taste;
        if(tasteRate < 4){
            tasteRate = 0;
        }

        double ingridientVarietyRate = 0;
        if(numAlcohols > 2* numMixers) {
            ingridientVarietyRate = 1;
        } else if (numAlcohols > numMixers) {
            ingridientVarietyRate = 0.5;
        }

        double ret = (alcoholContentRate*1.2 + ingredientFunRate*1.4 + ingredientAlcoholCountRate + ingredientMixerRate*0.8 + ingredientCreativityRate*0.5 + tasteRate*0.5 + ingridientVarietyRate*2.5) / 7.9 * 5;
        return clamp(ret,0,5);
    }
    private double rateCrazy(double alcohol, int numIngredients,int numAlcohols, int numMixers) {
        double alcoholRate = distributeBetween(alcohol,0.215,0,1,"exp");
        if(alcoholRate < 0.5){
            alcoholRate = 0;
        }
        double ingredientCountRate = distributeBetween(numIngredients,15,0,1,"exp");
        if(ingredientCountRate < 0.3 && funnes > 3){
            ingredientCountRate = 0.5;
        }
        else if(ingredientCountRate < 0.5 && funnes > 3.5){
            ingredientCountRate = 0.8;
        }
        else if(ingredientCountRate < 0.4){
            ingredientCountRate = 0;
        }

        double ingridientVarietyRate = 0;
        if(numAlcohols > 2* numMixers) {
            ingridientVarietyRate = 1;
        } else if (numAlcohols > numMixers) {
            ingridientVarietyRate = 0.5;
        }
        
        double ret = (alcoholRate*3 + ingredientCountRate + ingridientVarietyRate*3) / 7 * 5;
        return clamp(ret,0,5);
    }
    private void calculateMixStats() {
        // Step 1: Compute averages
        double totalVolume = 0;
        double alcohol = 0, bitter = 0, sweet = 0, fun = 0, creative = 0;


        for (Liquid l : mix.keySet()) {
            double v = mix.get(l);
            totalVolume += v;
            alcohol += (l.getAlcohol()/100) * v;
            bitter += l.getBitterness() * v;
            sweet += l.getSweetness() * v;
            fun += l.getFunness() * v;
            creative += l.getCreativity() * v;
        }

        if (totalVolume == 0) return; // avoid division by zero

        double avgSweet = sweet / totalVolume;
        double avgBitter = bitter / totalVolume;
        double avgFun = fun / totalVolume;
        double avgCreative = creative / totalVolume;
        int numIngredients = mix.size();
        int numMixers = 0;
        int numAlcohols = 0;

        for (Liquid l : mix.keySet()) {
            if (l.getType().equals("MIXER")) {
                numMixers++;
            } else{
                numAlcohols++;
            }
        }




        //taste: how well balanced and tasty is it? Not too much alochol,
        // not too little, more sweet than bitter, not too many ingredients.
        // at least one alcohol and one mixer
        taste = rateTaste(alcohol, avgSweet, avgBitter, numIngredients, numAlcohols,numMixers);
        

        //creativity: how creative is it? Like mixing drinks that dont go together.
        // Mixing a well balanced drink isnt that creative,
        // but mixing a lot of liquids together isnt crative either.
        // Its fun, but not creative. Less is more i think. Something unusual is creative.
        creativity = rateCreativity(avgCreative, numIngredients, numAlcohols);

        //funnes: how funny is the drink. More alcohol more fun, but like not linerally.
        // Many different alchols are fun too (u definitely gonna be sick after the drink),
        // 0.5l of straight alcohol is fun too. A balanced drink is not always fun.
        // A few ingredients is only fun when it has a lot of alcohol in it. and so on.
        funnes = rateFun(alcohol, avgFun, avgCreative, numAlcohols, numMixers);

        //crazyness: how crazy will you be after drinking this.
        // well balanced can be crazy, like a double shot of something will
        // make you drunk. Straight up 0.5l alcohol too. Crazy can be a loot of alcholo.
        // But also like a lot of drinks mixed together, thats crazy too.
        craziness = rateCrazy(alcohol, numIngredients,numAlcohols,numMixers);
        

        //attributums affect each other too.
        taste -= craziness*0.3 + creativity*0.5;
        creativity += -1*taste*0.15;
        funnes += creativity*0.3 + craziness*0.1;
        craziness += funnes*0.5 + creativity*0.2;
        //clamp attributes to [0,5]
        taste = distributeBetween(taste,5,0,5,"log");
        creativity = distributeBetween(creativity,3,0,5,"log");
        funnes = distributeBetween(funnes,5.5,0,5,"log");
        craziness = distributeBetween(craziness,8.5,0,5,"lin");


        rating = (taste*2.8 + funnes*1.2 + creativity*0.9 + craziness*0.6) / 3.8;
        rating = distributeBetween(rating,5,0,5,"quad");

    }

    private void generateReaction() {
        //3 positive reaction:
        // 0=• transcending to heaven
        // 1=• very_drunk
        // 2=• OK
        //3 negative reaction:
        // 3=• not_that_great_drink
        // 4=• ew_barf
        // 5=• taken_by_hell_and_burned_in_fire
        if(rating >= 4 || funnes > 4.8){
            reaction = "transcending_to_heaven";
        }
        else if((taste >= 3.5 || funnes >= 3.5 || creativity >= 3.5 || craziness >= 3.5) && taste > 2.5){
            reaction = "very_tasty_drink";
        }
        else if((taste < 2 && taste > 0.1)){
            reaction = "ew_barf";
        }
        else if(craziness >= 3){
            reaction = "taken_by_hell_and_burned_in_fire";
        }
        else if ( creativity >= 2.5 || craziness >= 2.5 || taste > 3) {
            reaction = "OK";
        }
        else if(funnes <= 1.2 || creativity <= 0.5 || craziness <= 1.2){
            reaction = "taken_by_hell_and_burned_in_fire";
        }
        else{
            reaction = "not_that_great_drink";
        }
    }
    
    public RateDrink(Mix mix) {
        this.mix = mix.getMix();

        //calculate the rating
        calculateMixStats();
        //generate reaction
        generateReaction();
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

    @Override
    public String toString() {
        String mixString = "Mix: ";
        for (Liquid l : mix.keySet()) {
            mixString += l.getName() + " (" + mix.get(l) + "l), ";
        }
        return (mixString + "\n Rating: " + rating +
                "\n Taste: " + taste + " Funnes: " + funnes +
                " Creativity: " + creativity + " Craziness: " + craziness + "\n" + reaction +"\n");
    }
}
