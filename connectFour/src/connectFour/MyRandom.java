package connectFour;

import java.util.Random;

public class MyRandom {
    
    public static int RANDOM_FACTOR = 50;
    public static int CHILD_MUTATION = 2;
    private static final Random RANDOM = new Random();
    
    public static double nextDouble() {
        return 2 * (RANDOM.nextDouble() - 0.5);
    }
    
    public static double nextDouble(int bound) {
        return bound * RANDOM.nextDouble();
    }

    public static int nextInt(int bound) {
        return RANDOM.nextInt(bound);
    }

}