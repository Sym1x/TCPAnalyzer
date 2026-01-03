package cracker.algorithms;

import java.util.ArrayList;
import java.util.List;

public class AlgorithmRegistry {

    private static final List<Cracker> algorithms = new ArrayList<>();

    static {
        algorithms.add(new Caesar());
        algorithms.add(new XorCracker());
        algorithms.add(new Vigenere());
    }

    public static List<Cracker> getAlgorithms() {
        return algorithms;
    }

    public static Cracker getByIndex(int index) {
        return algorithms.get(index);
    }
}
