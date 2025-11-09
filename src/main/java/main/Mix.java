package main;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.List;

public class Mix implements Serializable {
    private Hashtable<Liquid,Double> mix = new Hashtable<>();
    private List<Liquid> liquids;

    public Mix(List<Liquid> liquids, List<Double> amounts) {
        for(int i = 0; i < liquids.size(); i++) {
            mix.put(liquids.get(i), amounts.get(i));
        }
        this.liquids = liquids;
    }
    public Hashtable<Liquid,Double> getMix() {
        return mix;
    }
    public List<Liquid> getLiquids() {
        return liquids;
    }

    public double get(Liquid l) {
        return mix.get(l);
    }

    @Override
    public String toString() {
        String mixString = "";
        for (Liquid l : mix.keySet()) {
            mixString += l.getName() + " " + mix.get(l) + "l  ";
        }
        return mixString;
    }
}
