package connectFour;

public class Vector {

    final int x;
    final int y;

    Vector(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    Vector add(Vector v) {
        return new Vector(x + v.x, y + v.y);
    }
    
    Vector times(int i) {
        return new Vector(i * x, i * y);
    }
}