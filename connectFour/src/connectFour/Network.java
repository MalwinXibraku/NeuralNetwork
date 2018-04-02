package connectFour;

import java.util.Scanner;

public class Network implements Comparable<Network> {

    // Array which saves the layers of this Network
    private final Layer[] layers;
    // fitness of this Network is used for comparing
    private int fitness = 1;

    /*
     * Construct a Network with NUMOFNEURONS - 1 layers.
     * Each layer has NUMOFNEURONS[i + 1] neurons with
     * an input of NUMOFNEURONS[i].
     */
    public Network(int[] numOfNeurons) {
        layers = new Layer[numOfNeurons.length - 1];
        for (int i = 0; i < numOfNeurons.length - 1; i++) {
            layers[i] = new Layer(numOfNeurons[i + 1], numOfNeurons[i]);
        }
    }

    /*
     * Giving the INPUT data to the first layer this method
     * calculates the value the last layer returns. The result
     * can be used to make decisions.
     */
    public double[] compute(double[] input) {
        for (Layer layer : layers) {
            input = layer.compute(input);
        }
        return input;
    }

    /*
     * Mutated NUMOFMUTATIONS of the LAYERS. Every layer changes
     * RANDOM_FACTOR percent of there neurons.
     */
    public void mutate() {
        layers[MyRandom.nextInt(layers.length)]
                .mutate();
    }

    /*
     * This method returns a new Layer with neurons
     * depending on the parents. The FITNESSFACTOR
     * should be a double between 0 and 1. It weights the
     * influence of parents to the child. The concrete neurons
     * are created in the bornChild method of the Neuron class.
     * Return null if the number of neurons doesn't match up.
     */
    public static Network bornChild(Network nOne, Network nTwo) {
        if (nOne.layers.length != nTwo.layers.length) {
            return null;
        }
        Network child = new Network(nOne.layers, nTwo.layers,
                ((double) nOne.fitness) / (nOne.fitness + nTwo.fitness));
        child.fitness = (nOne.fitness + nTwo.fitness) / 2;
        return child;
    }
    
    private Network(Layer[] lOne, Layer[] lTwo, double fitnessFactor) {
        layers = new Layer[lOne.length];
        for (int k = 0; k < lOne.length; k++) {
            layers[k] = Layer.bornChild(lOne[k],
                    lTwo[k], fitnessFactor);
        }
    }



    // save and load algorithms
    private static final String PREFIX = "Network. Layers: ";
    private static final String DELEMITER = "\n\n";

    /*
     * This method brings all data of the Network in one String.
     * The String will begin with the PREFIX continues with the
     * number of layers and ends up with all of them. After all
     * layers the fitness is appending. The elements are
     * separated by the DELEMITER. The Method should be called
     * by the Generation to include the information in its own string.
     */
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(PREFIX + layers.length);
        for (Layer layer : layers) {
            buffer.append(DELEMITER + layer.toString());
        }
        buffer.append(DELEMITER + fitness);
        return buffer.toString();
    }

    /*
     * Generate a new Network with layers encoded in STRING.
     * The input has to be the String which the toString method
     * produce. This method returns null if the STRING doesn't begin
     * with PREFIX or doesn't include the DELEMITER.
     */
    public static Network load(String string) {
        String[] strings = string.split(DELEMITER, 2);
        if (!strings[0].contains(PREFIX) || 
                strings.length == 1 || strings[1] == null) {
            return null;
        }
        int layers = Integer.valueOf((strings[0].split(PREFIX))[1]);
        return new Network(layers, strings[1]);
    }

    /*
     * This constructor should only used by the load method
     */
    private Network(int layers, String string) {
        Scanner scanner = new Scanner(string);
        scanner.useDelimiter(DELEMITER);
        this.layers = new Layer[layers];
        for (int i = 0; i < this.layers.length; i++) {
            if (scanner.hasNext()) {
                this.layers[i] = Layer.load(scanner.next());
            }
        }
        if (scanner.hasNext()) {
            fitness = new Integer(scanner.next());
        }
        scanner.close();
    }

    // unimportant methods
    public int getFitness() {
        return fitness;
    }

    public void increaseFitness(int inc) {
        fitness += inc;
        if (fitness <= 0) {
            fitness = 1;
        }
    }

    @Override
    public int compareTo(Network network) {
        return fitness - network.fitness;
    }
}