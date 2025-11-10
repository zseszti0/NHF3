package main;

import java.io.Serializable;
import java.util.ArrayList;
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
    public List<Liquid> getLiquids() {
        return liquids;
    }

    public double get(Liquid l) {
        return mix.get(l);
    }
    public void reset(){
        mix = new Hashtable<>();
        liquids = new ArrayList<>();
    }
    public void put(Liquid l,double d){
        mix.put(l,d);
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
