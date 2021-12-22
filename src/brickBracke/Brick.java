package brickBracke;
import java.awt.*;
public class Brick extends Structure implements Constants {
    private int lives, hits;
    private boolean destroyed;


    private Color[] blueColors = {BLUE_BRICK_ONE, BLUE_BRICK_TWO, BLUE_BRICK_THREE, Color.BLACK};
    private Color[] redColors = {RED_BRICK_ONE, RED_BRICK_TWO, RED_BRICK_THREE, Color.BLACK};
    private Color[] purpleColors = {PURPLE_BRICK_ONE, PURPLE_BRICK_TWO, PURPLE_BRICK_THREE, Color.BLACK};
    private Color[] yellowColors = {YELLOW_BRICK_ONE, YELLOW_BRICK_TWO, YELLOW_BRICK_THREE, Color.BLACK};
    private Color[] pinkColors = {PINK_BRICK_ONE, PINK_BRICK_TWO, PINK_BRICK_THREE, Color.BLACK};
    private Color[] grayColors = {GRAY_BRICK_ONE, GRAY_BRICK_TWO, GRAY_BRICK_THREE, Color.BLACK};
    private Color[] greenColors = {GREEN_BRICK_ONE, GREEN_BRICK_TWO, GREEN_BRICK_THREE, Color.BLACK};
    private Color[][] colors = {blueColors, redColors, purpleColors, yellowColors, pinkColors, grayColors, greenColors};


    public Brick(int x, int y, int width, int height, Color color, int lives) {
        super(x, y, width, height, color);
        setLives(lives);
        setHits(0);
        setDestroyed(false);
    }

    //раскраска кирпича
    public void draw(Graphics g) {
        if (!destroyed) {
            g.setColor(color);
            g.fillRect(x, y, width, height);
        }
    }

    //добавьте попадание в кирпич и уничтожьте кирпич при попадании
    public void addHit() {
        hits++;
        nextColor();
        if (hits == lives) {
            setDestroyed(true);
        }
    }

    //изменение цвета до разрушения
    public void nextColor() {
        if (color == colors[0][0] || color == colors[0][1] || color == colors[0][2]) {
            color = blueColors[hits];
        }
        if (color == colors[1][0] || color == colors[1][1] || color == colors[1][2]) {
            color = redColors[hits];
        }
        if (color == colors[2][0] || color == colors[2][1] || color == colors[2][2]) {
            color = purpleColors[hits];
        }
        if (color == colors[3][0] || color == colors[3][1] || color == colors[3][2]) {
            color = yellowColors[hits];
        }
        if (color == colors[4][0] || color == colors[4][1] || color == colors[4][2]) {
            color = pinkColors[hits];
        }
        if (color == colors[5][0] || color == colors[5][1] || color == colors[5][2]) {
            color = grayColors[hits];
        }
        if (color == colors[6][0] || color == colors[6][1] || color == colors[6][2]) {
            color = greenColors[hits];
        }
    }

    //определение с какой стороны был удар по кирпичу
    public boolean hitBottom(int ballX, int ballY) {
        if ((ballX >= x) && (ballX <= x + width + 1) && (ballY == y + height) && (!destroyed)) {
            addHit();
            return true;
        }
        return false;
    }

    public boolean hitTop(int ballX, int ballY) {
        if ((ballX >= x) && (ballX <= x + width + 1) && (ballY == y) && (!destroyed)) {
            addHit();
            return true;
        }
        return false;
    }

    public boolean hitLeft(int ballX, int ballY) {
        if ((ballY >= y) && (ballY <= y + height) && (ballX == x) && (!destroyed)) {
            addHit();
            return true;
        }
        return false;
    }

    public boolean hitRight(int ballX, int ballY) {
        if ((ballY >= y) && (ballY <= y + height) && (ballX == x + width) && (!destroyed)) {
            addHit();
            return true;
        }
        return false;
    }


    public void setLives(int lives) {
        this.lives = lives;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    public boolean isDestroyed() {
        return destroyed;
    }
}
