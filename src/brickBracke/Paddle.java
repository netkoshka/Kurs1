package brickBracke;
import java.awt.*;
public class Paddle extends Structure implements Constants {
    public Paddle(int x, int y, int width, int height, Color color) {
        super(x, y, width, height, color);
    }

    //раскрас платформы
    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x, y, width, height);
    }
    //проверка на попадание мяча по платформе
    public boolean hitPaddle(int ballX, int ballY) {
        if ((ballX >= x) && (ballX <= x + width) && ((ballY >= y) && (ballY <= y + height))) {
            return true;
        }
        return false;
    }
}
