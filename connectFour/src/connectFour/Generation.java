package connectFour;

import java.util.Arrays;
import java.util.Scanner;

public class Generation {

    private static int generation;

    private final Network[] networks;

    public Generation(int size, int[] numOfNeurons) {
        generation = 1;
        networks = new Network[size];
        for (int i = 0; i < size; i++) {
            networks[i] = new Network(numOfNeurons);
        }
    }
    
    public Network[][] nextRound(boolean random) {
        if (random) {
            shuffle();
        }
        return getPairs();
    }
    
    private void shuffle() {
        for (int i = networks.length; i > 1; i--) {
            int j = MyRandom.nextInt(i);
            Network temp = networks[i-1];
            networks[i-1] = networks[j];
            networks[j] = temp;
        }
    }

    private Network[][] getPairs() {
        Network[][] res = new Network[networks.length / 2][2];
        for (int i = 0; i < networks.length / 2; i++) {
            res[i][0] = networks[2 * i];
            res[i][1] = networks[2 * i + 1];
        }
        return res;
    }

    public void nextGeneration() {
        System.out.println("Generation: " + generation++);
        killHalf();
        Network[][] parents = getPairs();
        for (int i = 0; i < parents.length / 2; i++) {
            networks[parents.length + 2 * i] = Network.bornChild(parents[i][0],
                    parents[i][1]);
            networks[parents.length + 2 * i + 1] = Network.bornChild(parents[i][0],
                    parents[i][1]);
        }
        shuffle();
    }

    private void killHalf() {
        Arrays.sort(networks);
        for (int i = 0; i < networks.length / 2; i++) {
            int index = (int) (Math.pow(MyRandom
                    .nextDouble(1), 0.4) * (networks.length - i));
            for (int j = index; j < networks.length - 1; j++) {
                networks[j] = networks[j + 1];
            }
            networks[networks.length - 1] = null;
        }
    }
    
    // save and load algorithms
    private static final String PREFIX = "Generation. Networks: ";
    private static final String DELEMITER = "\n\n\n";

    /*
     * This method brings all data of one generation in one String.
     * The String will begin with the PREFIX continues with the
     * number of networks and ends up with all of them. After all 
     * the number of this generation is appending. The elements
     * are separated by the DELEMITER.
     */
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(PREFIX + networks.length);
        for (Network network : networks) {
            buffer.append(DELEMITER + network.toString());
        }
        buffer.append(DELEMITER + generation);
        return buffer.toString();
    }

    /*
     * Generate the Generation with networks encoded in STRING.
     * The input has to be the String which the toString method
     * produce. This method returns null if the STRING doesn't
     * begin with PREFIX or doesn't include the DELEMITER.
     */
    public static Generation load(String string) {
        String[] strings = string.split(DELEMITER, 2);
        if (!strings[0].contains(PREFIX) || 
                strings.length == 1 || strings[1] == null) {
            return null;
        }
        int networks = Integer.valueOf((strings[0].split(PREFIX))[1]);
        return new Generation(networks, strings[1]);
    }

    /*
     * This constructor should only used by the load method
     */
    private Generation(int networks, String string) {
        Scanner scanner = new Scanner(string);
        scanner.useDelimiter(DELEMITER);
        this.networks = new Network[networks];
        for (int i = 0; i < this.networks.length; i++) {
            if (scanner.hasNext()) {
                this.networks[i] = Network.load(scanner.next());
            }
        }
        if (scanner.hasNext()) {
            generation = new Integer(scanner.next());
        }
        scanner.close();
    }
}