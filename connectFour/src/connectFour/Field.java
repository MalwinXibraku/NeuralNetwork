package connectFour;

import java.util.LinkedList;

public class Field implements Drawable {

    public enum Status {
        WIN_PLAYER_ONE, WIN_PLAYER_TWO, FULL_FIELD, NON;
    }

    private final int stonesToWin;
    private final int sizeX;
    private final int sizeY;
    private final int stoneValue;

    private int numOfPieces;
    private int[][] board;
    private LinkedList<Stone> stones;
    private ConnectionManager manager;
    private LinkedList<Double> boardValues;
    private int playerOnesTurn;

    public Field(int sizeX, int sizeY, int stonesToWin, int stoneValue) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.stonesToWin = stonesToWin;
        this.stoneValue = stoneValue;
        setPlayerOnesTurn(1);
        reset();
    }

    public void reset() {
    	setPlayerOnesTurn(- getPlayerOnesTurn());
        numOfPieces = 0;
        board = new int[sizeX][sizeY];
        stones = new LinkedList<>();
        manager = new ConnectionManager(sizeX, sizeY, stonesToWin);
        boardValues = new LinkedList<>();
        boardValues.add(0.0);
    }

    @Override
    public void draw(Main main) {
        main.background(0xcccccc);
        int quoWidth = main.width / sizeX;
        int quoHeight = main.height / sizeY;
        for (int i = 1; i < sizeX; i++) {
            main.line(i * quoWidth, 0, i * quoWidth, main.height);
        }
        for (int i = 1; i < sizeY; i++) {
            main.line(0, i * quoHeight, main.width, i * quoHeight);
        }
        for (Stone stone : stones) {
            stone.draw(main);
        }
    }

    public boolean putStone(int x) {
        Stone stone = generateStone(x);
        if (stone == null) {
            return false;
        } else {
            board[x][stone.pos.y] = stone.value;
            stones.addLast(stone);
            numOfPieces++;
            manager.add(stone);
            boardValues.addLast(manager.evaluate());
            setPlayerOnesTurn(-getPlayerOnesTurn());
            return true;
        }
    }

    private Stone generateStone(int x) {
        if (board[x][0] != 0 || x < 0 || x >= sizeX) {
            return null;
        } else {
            int y = sizeY - 1;
            while (board[x][y] != 0) {
                y--;
            }
            return new Stone(new Vector(x, y), getPlayerOnesTurn() * stoneValue);
        }
    }

    public Status checkStatus() {
        if (sizeX * sizeY == numOfPieces) {
            return Status.FULL_FIELD;
        }
        return manager.getStatus();
    }

    public int[][] getBoard() {
        return board;
    }

    public double getValueDiff() {
        return boardValues.getLast() -
                boardValues.get(boardValues.size() - 2);
    }

    public LinkedList<Integer> findBestMoves() {
        //TODO find really best move
        double value = Integer.MIN_VALUE;
        LinkedList<Integer> indices = new LinkedList<>();
        for (int i = 0; i < sizeX; i++) {
            Stone stone = generateStone(i);
            if (stone == null) {
                continue;
            } else {
                double evaluation = getPlayerOnesTurn() * manager.evaluateMove(stone);
                //System.out.println(evaluation);
                if (evaluation > value) {
                    value = evaluation;
                    indices.clear();
                    indices.add(i);
                } else if (evaluation == value) {
                    indices.add(i);
                }
            }
        }
        //System.out.println(indices.toString());
        return indices;
    }

	public int getPlayerOnesTurn() {
		return playerOnesTurn;
	}

	public void setPlayerOnesTurn(int playerOnesTurn) {
		this.playerOnesTurn = playerOnesTurn;
	}

    

    
}