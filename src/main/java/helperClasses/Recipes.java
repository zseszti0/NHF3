package helperClasses;

import main.Liquid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

public class Recipes {
    public HashMap<List<Hashtable<Liquid,Double>>,List<Double>> recipeHashes = new HashMap<>();
    public List<List<Hashtable<Liquid,Double>>> recipeKeys = new ArrayList<>();
}
