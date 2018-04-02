package connectFour;
import java.util.LinkedList;

import connectFour.Field.Status;

public class ConnectionManager {
    
    private Field.Status status;
    private LinkedList<Connection> connections;
    
    public ConnectionManager(int sizeX, int sizeY, int stonesToWin) {
        reset(sizeX, sizeY, stonesToWin);
    }
    
    public void reset(int sizeX, int sizeY, int stonesToWin) {
        connections = Connection.newCover(sizeX, sizeY, stonesToWin);
        status = Field.Status.NON;
    }

    public void add(Stone stone) {
        for (int i = connections.size() - 1; i >= 0; i--) {
            connections.get(i).add(stone);
            if (connections.get(i).possibleWin) {
                if (connections.get(i).won && connections.get(i).value > 0) {
                    status = Status.WIN_PLAYER_ONE;
                } else if (connections.get(i).won && connections.get(i).value < 0) {
                    status = Status.WIN_PLAYER_TWO;
                }
            } else {
                connections.remove(i);
            }
        }
    }
    
    public Field.Status getStatus() {
        return status;
    }
    
    public double evaluate() {
        int sum = 0;
        for (Connection con : connections) {
            sum += Math.pow(con.value, 1);
        }
        return sum;
    }
    
    public double evaluateMove(Stone stone) {
        LinkedList<Connection> removed = saveAdd(stone);
        double i = evaluate();
        undo(removed, stone);
        return i;
    }
    
    private LinkedList<Connection> saveAdd(Stone stone) {
        LinkedList<Connection> removed = new LinkedList<>();
        for (int i = connections.size() - 1; i >= 0; i--) {
            connections.get(i).add(stone);
            if (!connections.get(i).possibleWin) {
                removed.add(connections.get(i));
                connections.remove(i);
            }
        }
        return removed;
    }
    
    private void undo(LinkedList<Connection> removed, Stone stone) {
        connections.addAll(removed);
        for (Connection con : connections) {
            con.remove(stone);
        }
    }
}