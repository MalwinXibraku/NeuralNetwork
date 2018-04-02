package connectFour;

import java.util.Scanner;

public class Layer {
    
    // Array which saves the neurons in this Layer.
    private final Neuron[] neurons;
    
    /*
     * Construct a Layer with NEURONS neurons.
     * Each have an input of INPUT.
     */
    public Layer(int neurons, int input) {
        this.neurons = new Neuron[neurons];
        for (int i = 0; i < neurons; i++) {
            this.neurons[i] = new Neuron(input);
        }
    }
    
    /*
     * Calculate the value of each neuron by giving them
     * the INPUT.
     */
    public double[] compute(double[] input) {
        double[] res = new double[neurons.length];
        for (int i = 0; i < neurons.length; i++) {
            res[i] = neurons[i].fire(input);
        }
        return res;
    }
    
    /*
     * Mutated NUM percent of the neurons. Every neuron changes
     * RANDOM_FACTOR percent of there weights and the bias.
     */
    public void mutate() {
        neurons[MyRandom.nextInt(neurons.length)]
                .mutate(MyRandom.RANDOM_FACTOR);
    }
    
    /*
     * This method returns a new Layer with neurons
     * depending on the parents. The FITNESSFACTOR
     * should be a double between 0 and 1. It weights the
     * influence of parents to the child. The concrete neurons
     * are created in the bornChild method of the Neuron class.
     * Return null if the number of neurons doesn't match up.
     */
    public static Layer bornChild(Layer lOne, Layer lTwo,
            double fitnessFactor) {
        if (lOne.neurons.length != lTwo.neurons.length) {
            return null;
        }
        Layer child = new Layer(lOne.neurons, lTwo.neurons, fitnessFactor);
        return child;
    }
    
    private Layer(Neuron[] nOne, Neuron[] nTwo,
            double fitnessFactor) {
        this.neurons = new Neuron[nOne.length];
        for (int k = 0; k < nOne.length; k++) {
            this.neurons[k] = Neuron.bornChild(nOne[k],
                    nTwo[k], fitnessFactor);
        }
    }
    
    // save and load algorithms
    private static final String PREFIX = "\tLayer. Neurons: ";
    private static final String DELEMITER = "\n";

    
    /*
     * This method brings all data of the Layer in one String.
     * The String will begin with the PREFIX continues with the
     * number of neurons and ends up with all of them. The
     * elements are separated by the DELEMITER. The Method
     * should be called by the Network to include the information
     * in its own string.
     */
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(PREFIX + neurons.length);
        for (Neuron neuron : neurons) {
            buffer.append(DELEMITER + neuron.toString());
        }
        return buffer.toString();
    }
    
    
    /*
     * Generate a new Layer with neurons encoded in STRING.
     * The input has to be the String which the toString method
     * produce. This method returns null if the STRING doesn't begin
     * with PREFIX or doesn't include the DELEMITER.
     */
    public static Layer load(String string) {
        String[] strings = string.split(DELEMITER, 2);
        if (!strings[0].contains(PREFIX) || 
                strings.length == 1 || strings[1] == null) {
            return null;
        }
        int neurons = Integer.valueOf((strings[0].split(PREFIX))[1]);
        return new Layer(neurons, strings[1]);
    }
    
    /*
     * This constructor should only used by the load method
     */
    private Layer(int neurons, String string) {
        Scanner scanner = new Scanner(string);
        scanner.useDelimiter(DELEMITER);
        this.neurons = new Neuron[neurons];
        for (int i = 0; i < this.neurons.length; i++) {
            if (scanner.hasNext()) {
                this.neurons[i] = Neuron.load(scanner.next());
            }
        }
        scanner.close();
    }
}
