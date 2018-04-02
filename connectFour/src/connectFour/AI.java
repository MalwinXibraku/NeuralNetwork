package connectFour;

public abstract class AI {
	
private final Network network;
    
    public AI(Network network) {
        this.network = network;
    }
    
    public Network getNetwork() {
        return network;
    }
    
    public abstract double[] transformToInput(Object o);
    
    public abstract Object transformOutput(double[] output);
    
    public double[] compute(double[] input) {
        return network.compute(input);
    }
    
    public void handleMove(double valueDiff, boolean bestMove, boolean autoGen) {
        //Todo belohnen/bestrafen
    }
    
    public void win(){
    	network.increaseFitness(1);
    }
    
    public void loose(){
    	network.increaseFitness(-1);
    }
	
}
