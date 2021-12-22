package brickBracke;
import java.awt.*;
public class Ball extends Structure implements Constants {
    private int xDir = 1;//направление
    private int yDir = -1;
    //конструктор
    public Ball(int x, int y, int width, int height, Color color) {
        super(x, y, width, height, color);

    }

    //рисование мяча
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval(x, y, width, height);
    }

    //движение мяча
    public void move() {
        x += xDir;
        y += yDir;
    }

    //положение мяча и возвращение его в центр
    public void reset() {
        x = BALL_X_START;
        y = BALL_Y_START;
        xDir = 1;
        yDir = -1;
    }


    public void setXDir(int xDir) {

        this.xDir = xDir;
    }

    public void setYDir(int yDir) {

        this.yDir = yDir;
    }




    public int getXDir() {

        return xDir;
    }

}
