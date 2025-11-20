package helperClasses;

import main.Mix;

import java.io.*;
import java.util.List;

public class MixSerializer {
    
    public static List<Mix> getMixFromFile(String filePath) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath));
        List<Mix> mixes = (List<Mix>) in.readObject();
        in.close();

        return mixes;
    }
    public static void saveMixesToFile(List<Mix> mixes, String filePath) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath));
        out.writeObject(mixes);
        out.close();
    }
}
