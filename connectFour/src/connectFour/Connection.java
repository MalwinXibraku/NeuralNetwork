package connectFour;

import java.util.LinkedList;

public class Connection {
    
    
    private final Vector dir;
    private final Vector start;
    private final Stone[] stones;
    boolean won = false;
    boolean possibleWin = true;
    int value;

    public static LinkedList<Connection> newCover(int sizeX, int sizeY, int stonesToWin) {
        LinkedList<Connection> res = new LinkedList<>();
        for (int row = 0; row < sizeY; row++) {
            for (int colum = 0; colum < sizeX - stonesToWin + 1; colum++) {
                res.add(new Connection(
                        new Vector(1, 0), new Vector(colum, row), stonesToWin));
            }
        }
        for (int colum = 0; colum < sizeX; colum++) {
            for (int row = 0; row < sizeY - stonesToWin + 1; row++) {
                res.add(new Connection(
                        new Vector(0, 1), new Vector(colum, row), stonesToWin));
            }
        }
        for (int row = 0; row < sizeY - stonesToWin + 1; row++) {
            for (int colum = 0; colum < sizeX - stonesToWin + 1; colum++) {
                res.add(new Connection(
                        new Vector(1, 1), new Vector(colum, row), stonesToWin));
            }
        }
        for (int row = 0; row < sizeY - stonesToWin + 1; row++) {
            for (int colum = stonesToWin - 1; colum < sizeX; colum++) {
                res.add(new Connection(
                        new Vector(-1, 1), new Vector(colum, row), stonesToWin));
            }
        }
        return res;
    }

    public Connection(Vector dir, Vector start, int stonesToWin) {
        this.dir = dir;
        this.start = start;
        stones = new Stone[stonesToWin];
    }

    public boolean add(Stone stone) {
        if (possibleWin) {
            for (int i = 0; i < stones.length; i++) {
                Vector v = start.add(dir.times(i));
                if ((v.x == stone.pos.x) && (v.y == stone.pos.y) &&
                        (stones[i] == null)) {
                    value += stone.value;
                    stones[i] = stone;
                    possibleWin = value * stone.value > 0;
                    won = (value == stones.length * stone.value);
                    return true;
                }
            }
        }
        return false;
    }
    
    public void remove(Stone stone) {
        possibleWin = true;
        won = false;
        for (int i = 0; i < stones.length; i++) {
            Vector v = start.add(dir.times(i));
            if ((v.x == stone.pos.x) && (v.y == stone.pos.y) &&
                    (stones[i] != null)) {
                value -= stone.value;
                stones[i] = null;
                return;
            }
        }
    }
}