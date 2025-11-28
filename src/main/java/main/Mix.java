package main;

import javafx.scene.paint.Color;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class Mix implements Serializable {
    @java.io.Serial
    private static final long serialVersionUID = 1L;

    private Hashtable<Liquid,Double> mix = new Hashtable<>();
    private List<Liquid> liquids = new ArrayList<>();

    public Mix() {}
    public Mix(List<Liquid> liquids, List<Double> amounts) {
        for(int i = 0; i < liquids.size(); i++) {
            mix.put(liquids.get(i), amounts.get(i));
        }
        this.liquids = liquids;
    }
    public List<Liquid> getLiquids() {
        return liquids;
    }

    public double get(Liquid l) {
        double ret;
        try{
            ret = mix.get(l);
        }
        catch(NullPointerException e){
            ret = 0;
        }
        return ret;
    }
    public Liquid get(int index){
        return liquids.get(index);
    }
    public double getCurrentVolume(){
        double ret = 0;
        for(Liquid l: liquids){
            ret += mix.get(l);
        }
        return ret;
    }
    public void reset(){
        mix = new Hashtable<>();
        liquids = new ArrayList<>();
    }
    public void put(Liquid l,double d){
        mix.put(l,d);
        if(!liquids.contains(l))
            liquids.add(l);
    }

    public Hashtable<Liquid,Double> getMix(){
        return mix;
    }
    @Override
    public String toString() {
        String mixString = "";
        for (Liquid l : mix.keySet()) {
            mixString += l.getName() + " " + String.format("%.3f l",mix.get(l));
        }
        return mixString;
    }

    public static Color calculateColor(Mix mix) {

        Hashtable<Liquid, Double> table = mix.getMix();
        if (table.isEmpty()) return Color.WHITE;

        double total = mix.getCurrentVolume();
        if (total <= 0) return Color.WHITE;

        // We will accumulate weighted RGB values
        double rAcc = 0;
        double gAcc = 0;
        double bAcc = 0;

        for (Liquid l : table.keySet()) {

            double amount = table.get(l);
            double weight = amount / total;

            // 1) Get first capital letter
            char ch = Character.toUpperCase(l.getName().charAt(0));
            if (ch < 'A' || ch > 'Z') ch = 'A';   // safety fallback

            // 2) Map A–Z to hue 0–360
            double index = ch - 'A';
            double hue = (index / 26.0) * 360.0;

            // 3) Pastel = high saturation but raised brightness
            double saturation = 0.45;  // pastel-ish
            double brightness = 1.0;   // max brightness

            Color liquidColor = Color.hsb(hue, saturation, brightness);

            // 4) Weighted RGB mixing
            rAcc += liquidColor.getRed() * weight;
            gAcc += liquidColor.getGreen() * weight;
            bAcc += liquidColor.getBlue() * weight;
        }

        return new Color(rAcc, gAcc, bAcc, 1.0);
    }


}
