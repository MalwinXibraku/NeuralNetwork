package connectFour;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import processing.core.PApplet;

public class Main extends PApplet{

	public static class Constants {
        private static final int WIDTH = 600;
        private static final int HEIGHT = 600;
        public static final int SIZE_X = 7;
        public static final int SIZE_Y = 6;
        
        private static final String FILE_NAME =
                "res/data/networkdata/weights.csv";
        
        private static final int FRAMERAT = 1000;
        private static final int NUM_OF_NETWORKS = 100;
        private static final int[] NUM_OF_NEURONS = 
            {SIZE_X * SIZE_Y, SIZE_X * SIZE_Y / 2, SIZE_X};
        private static final int STONE_VALUE = 10;
        private static final int NUM_OF_ROUNDS = 10;
        
        private static final int STONES_TO_WIN = 4;
        private static final int[] EX = Example.NON;
    }

    private Generation generation;
    private Field field;
    private Computer[] coms;
    private Network[][] pairs;
    private int curPair;
    private int curRound;
    private int curCom;
    private int round;

    public static void main(String[] args){
        PApplet.main("connectFour.Main");
    }

    @Override
    public void settings() {
        size(Constants.WIDTH, Constants.HEIGHT);
    }

    @Override
    public void setup() {
        try {
            byte[] content = Files.readAllBytes(Paths
                    .get(Constants.FILE_NAME));
            generation = Generation.load(new String(content));
        } catch (IOException e) {
            System.out.println("Generation could not load.");
            generation = new Generation(Constants.NUM_OF_NETWORKS,
                    Constants.NUM_OF_NEURONS);
        }
        this.round = 0;
        field = new Field(Constants.SIZE_X, Constants.SIZE_Y,
                Constants.STONES_TO_WIN, Constants.STONE_VALUE);
        pairs = generation.nextRound(false);
        coms = new Computer[2];
        newComputers();
        frameRate(Constants.FRAMERAT);
        setupPosition(Constants.EX);
    }
    
    private void setupPosition(int[] setup) {
        for (int i = 0; i < setup.length; i++) {
            field.putStone(setup[i]);
        }
    }
    
    private void newComputers() {
        if (curPair == pairs.length) {
            if (curRound == Constants.NUM_OF_ROUNDS) {
                curRound = 0;
                generation.nextGeneration();
            }
            curRound++;
            curPair = 0;
            pairs = generation.nextRound(true);
        }
        coms[0] = new Computer(pairs[curPair][0],
                Constants.STONE_VALUE);
        coms[1] = new Computer(pairs[curPair++][1],
                -Constants.STONE_VALUE);
        //System.out.println("new");
    }

    @Override
    public void draw() {
        //field.putStone(field.findBestMoves().get(0));
        computerMove();
        field.draw(this);
        switch (field.checkStatus()) {
        case FULL_FIELD:
        	if(this.round == 1) {
        		newComputers();
        	}
        	changeRound();
            field.reset();
            break;
        case WIN_PLAYER_ONE:
        	coms[0].win();
        	coms[1].loose();
        	if(this.round == 1) {
        		newComputers();
        	}
        	changeRound();
            field.reset();
            break;
        case WIN_PLAYER_TWO:
        	coms[1].win();
        	coms[0].loose();
        	if(this.round == 1) {
        		newComputers();
        	}
        	changeRound();
            field.reset();
            break;
        case NON:
            break;
        }
    }

    private void computerMove() {
        int computerMove = coms[curCom].move(field.getBoard());
        //boolean bestMove = field.findBestMoves().contains(computerMove);
        if (putStone(computerMove)) {
            //coms[curCom].handleMove(field.getValueDiff(), bestMove, true);
        } else {
            // TODO better failing behavior
            computerFails();
            //coms[curCom].handleMove(field.getValueDiff(), bestMove, false);
        }
        curCom = (curCom + 1) % 2;
    }

    private void computerFails() {
        int x = 0;
        while (!putStone(x) && x < Constants.SIZE_X) {
            x++;
        }
    }

    private boolean putStone(int x) {
        if (x >= 0 && x < Constants.SIZE_X) {
            return field.putStone(x);
        }
        return false;
    }

    @Override
    public void exit() {
        try {
            Files.write(Paths.get(Constants.FILE_NAME),
                    generation.toString().getBytes());
        } catch (IOException e) {
            System.out.println("Generation could not saved.");
        }
        super.exit();
    }
    
    private void changeRound() {
    	if(this.round == 0){
    		this.round = 1;
    	} else {
    		this.round = 0;
    	}
    	field.setPlayerOnesTurn(- field.getPlayerOnesTurn());
    }
    
}
