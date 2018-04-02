package connectFour;

public class Stone implements Drawable {
    
    public enum Style {
        TWO(0xFFFFD600), ONE(0xFFE5350F);
        final int color;
        Style(int color) {
            this.color = color;
        }
    }
    
    final Vector pos;
    final int value;
    
    public Stone(Vector pos, int value) {
        this.pos = pos;
        this.value = value;
    }

    @Override
    public void draw(Main main) {
        if (value > 0) {
            main.fill(Style.ONE.color);
        } else {
            main.fill(Style.TWO.color);
        }
        int quoWidth = main.width / Main.Constants.SIZE_X;
        int quoHeight = main.height / Main.Constants.SIZE_Y;
        main.ellipse(pos.x * quoWidth + quoWidth / 2,
                pos.y * quoHeight + quoHeight / 2,
                quoWidth / 2, quoHeight / 2);
    }
}