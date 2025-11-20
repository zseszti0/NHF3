package helperClasses;

import main.Mix;

import java.io.*;
import java.util.List;

public class MixSerializer {
    
    public static List<Mix> getMixFromFile(String filePath) throws ClassNotFoundException {
        List<Mix> mixes;
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath));
            mixes = (List<Mix>) in.readObject();
            in.close();
        } catch (IOException e) {
            mixes = new java.util.ArrayList<>();
        }

        return mixes;
    }
    public static void saveMixesToFile(List<Mix> mixes, String filePath) throws IOException, ClassNotFoundException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath));
        List<Mix> savedMixes = MixSerializer.getMixFromFile(filePath);
        savedMixes.addAll(mixes);
        out.writeObject(savedMixes);
        out.close();
    }
    public static void saveMixToFile(Mix mix, String filePath) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath));

        List<Mix> savedMixes;
        try {
            savedMixes = MixSerializer.getMixFromFile(filePath);
        } catch (ClassNotFoundException e) {
            savedMixes = new java.util.ArrayList<>();
        }
        savedMixes.add(mix);
        out.writeObject(savedMixes);
        out.close();
    }
}
