package connectFour;

public class Computer extends AI{
private final int stoneValue;
    
// DAS IST EIN TEST NR 2
    public Computer(Network network, int stoneValue) {
        super(network);
        this.stoneValue = stoneValue;
    }
    
    public int move(int[][] board) {
        return (int)transformOutput(
                compute(transformToInput(board)));
    }

    @Override
    public double[] transformToInput(Object o) {
        int[][] board = (int[][]) o;
        double[] input = new double[board.length * board[0].length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                input[board[0].length * i + j] = stoneValue * board[i][j];
            }
        }
        return input;
    }

    @Override
    public Object transformOutput(double[] output) {
        int index = 0;
        double maxValue = output[0];
        for (int i = 1; i < output.length; i++) {
            if (output[i] > maxValue) {
                index = i;
                maxValue = output[i];
            }
        }
        return index;
    }
    
    @Override
    public void handleMove(double valueDiff, boolean bestMove, boolean autoGen) {
        super.handleMove(valueDiff * stoneValue, bestMove, autoGen);
    }
}
