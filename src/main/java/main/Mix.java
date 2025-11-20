package main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class Mix implements Serializable {
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

}
