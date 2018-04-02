package connectFour;

import java.util.Scanner;

public class Neuron {
    //This array will weights the input
    private double[] weights;
    //The bias is always added to the result of the weighted input.
    private double bias;
    
    /* 
     * Construct a new Neuron. INPUT gives the number of weights
     * the Neuron will have.
     */
    public Neuron(int input) {
        weights = new double[input];
        mutate(MyRandom.RANDOM_FACTOR);
    }
    
    /*
     * This method should be called by the layer the Neuron belongs
     * to. Generating the output value by the formula:
     *      
     *      (1+e^{-2*(\sum w_i*in_i)+ bias)})^{-1}
     *      
     * where w_i is the i-th weight and in_i the INPUT[i].
     * Return 0 if the input length doesn't match with the weight
     * length.
     */
    public double fire(double[] input) {
        if (input.length != weights.length) {
            return 0;
        }
        double d = 0;
        for (int i = 0; i < input.length; i++) {
            d += input[i] * weights[i];
        }
        return (1 / (1 + Math.exp((- 2 * d + bias))));
    }
    
    /*
     * mutate NUM percent of the weights and the bias.
     * The absolute value will be lower than one.
     */
    public void mutate(int num) {
        for (int i = 0; i < num * weights.length / 100; i++) {
            weights[MyRandom.nextInt(weights.length)] =
                    MyRandom.nextDouble();
        }
        bias = MyRandom.nextDouble();
    }
    
    /*
     * This method returns a new Neuron with weights and
     * bias depending on the parents. The FITNESSFACTOR
     * should be a double between 0 and 1. It weights the
     * influence of parents to the child. The concrete value
     * of the properties is calculated in the calcProps method.
     * Return null if the number of weights doesn't match up.
     */
    public static Neuron bornChild(Neuron pOne, Neuron pTwo,
            double fitnessFactor) {
        if (pOne.weights.length != pTwo.weights.length) {
            return null;
        }
        return new Neuron(pOne.weights, pTwo.weights,
                pOne.bias, pTwo.bias, fitnessFactor);
    }
    
    private Neuron(double[] wOne, double[] wTwo,
            double bOne, double bTwo, double fitnessFactor) {
                weights = new double[wOne.length];
        for (int k = 0; k < wOne.length; k++) {
            weights[k] = calcProps(wOne[k],
                    wTwo[k], fitnessFactor);
        }
        mutate(MyRandom.CHILD_MUTATION);
        bias = calcProps(bOne, bTwo, fitnessFactor);
    }
    
    
    /*
     * Calculate a new value from two input values weighting with
     * the FITNESSFACTOR.
     */
    private static double calcProps(double one, double two,
            double fitnessFactor) {
        double res;
        if (one == 0 || two == 0) {
            if (MyRandom.nextDouble(1) < fitnessFactor) {
                res = one;
            } else {
                res = two;
            }
        } else {
            res = one * fitnessFactor +
            two * (1 - fitnessFactor);
        }
        return res;
    }
    
    
    // save and load algorithms
    private static final String PREFIX = "Neuron. Length: ";
    private static final String DELEMITER = "\t";
    
    /*
     * This method brings all data of the Neuron in one String.
     * The String will begin with the PREFIX continues with the
     * number of weights and ends up with all weights. After all
     * the bias is appending. The elements are separated by the
     * DELEMITER. The Method should be called by the Layer to
     * include the information in its own string.
     */
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(PREFIX + weights.length);
        for (int k = 0; k < weights.length; k++) {
            buffer.append(DELEMITER + weights[k]);
        }
        buffer.append(DELEMITER + bias);
        return buffer.toString();
    }
    
    /*
     * Generate a new Neuron with weights and bias encoded in STRING.
     * The input has to be the String which the toString method
     * produce. This method returns null if the STRING doesn't begin
     * with PREFIX or doesn't include the DELEMITER.
     */
    public static Neuron load(String string) {
        String[] strings = string.split(DELEMITER, 2);
        if (!strings[0].contains(PREFIX) || 
                strings.length == 1 || strings[1] == null) {
            return null;
        }
        int length = Integer.valueOf((strings[0].split(PREFIX))[1]);
        return new Neuron(length, strings[1]);
    }
    
    /*
     * This constructor should only used by the load method
     */
    private Neuron(int length, String string) {
        Scanner scanner = new Scanner(string);
        scanner.useDelimiter(DELEMITER);
        weights = new double[length];
        for (int i = 0; i < weights.length; i++) {
            if (scanner.hasNext()) {
                weights[i] = new Double(scanner.next());
            }
        }
        if (scanner.hasNext()) {
            bias = new Double(scanner.next());
        }
        scanner.close();
    }
}