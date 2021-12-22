package brickBracke;
import java.awt.*;
import javax.swing.*;
import java.util.Random;
import java.lang.Thread;
import java.awt.event.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Board extends JPanel implements Runnable, Constants {
    //на экране
    private Paddle paddle;
    private Ball ball;
    private Brick[][] brick = new Brick[10][5];

    //начальные значения
    private int score = 0;
    private int lives = MAX_LIVES;
    private int bricksLeft = MAX_BRICKS;
    private int xSpeed;

    //имя игрока
    private String playerName;

    //объект класса
    private Thread game;

    private AtomicBoolean isPaused = new AtomicBoolean(true);

    //цвета для кирпичей
    private Color[] blueColors = {BLUE_BRICK_ONE, BLUE_BRICK_TWO, BLUE_BRICK_THREE, Color.BLACK};
    private Color[] redColors = {RED_BRICK_ONE, RED_BRICK_TWO, RED_BRICK_THREE, Color.BLACK};
    private Color[] purpleColors = {PURPLE_BRICK_ONE, PURPLE_BRICK_TWO, PURPLE_BRICK_THREE, Color.BLACK};
    private Color[] yellowColors = {YELLOW_BRICK_ONE, YELLOW_BRICK_TWO, YELLOW_BRICK_THREE, Color.BLACK};
    private Color[] pinkColors = {PINK_BRICK_ONE, PINK_BRICK_TWO, PINK_BRICK_THREE, Color.BLACK};
    private Color[] grayColors = {GRAY_BRICK_ONE, GRAY_BRICK_TWO, GRAY_BRICK_THREE, Color.BLACK};
    private Color[] greenColors = {GREEN_BRICK_ONE, GREEN_BRICK_TWO, GREEN_BRICK_THREE, Color.BLACK};
    private Color[][] colors = {blueColors, redColors, purpleColors, yellowColors, pinkColors, grayColors, greenColors};

    //конструктор
    public Board(int width, int height) {
        super.setSize(width, height);//фиксированный размер
        addKeyListener(new BoardListener());//обработка событий клавиатуры
        setFocusable(true);//это позволяет JPanel получить силу фокусировки.
        makeBricks();
        paddle = new Paddle(PADDLE_X_START, PADDLE_Y_START, PADDLE_WIDTH, PADDLE_HEIGHT, Color.BLACK);
        ball = new Ball(BALL_X_START, BALL_Y_START, BALL_WIDTH, BALL_HEIGHT, Color.BLACK);
        //введение имени игрока
        playerName = JOptionPane.showInputDialog(null, "Введите свое имя или никнейм:", "Brick Breaker", JOptionPane.QUESTION_MESSAGE);
        if (playerName == null) {
            System.exit(0);
        }

        {
            score += 500;
            JOptionPane.showMessageDialog(null, "Ты получаешь 500 очков на свой счет!\n Чтобы начать, нажми два раза на пробел!\n Чтобы управлять платформой нажимайте влево и вправо. \n Вам дается 5 жизней. Для того, чтобы выиграть сбейте все кирпичи! ", "Правило игры", JOptionPane.INFORMATION_MESSAGE);
        }

        game = new Thread(this);
        game.start();
        stop();
        isPaused.set(true);
    }

    //заполнение массива кирпичами
    public void makeBricks() {
        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 5; j++) {
                Random rand = new Random();
                int numLives = 3;
                Color color = colors[rand.nextInt(7)][0];
                brick[i][j] = new Brick((i * BRICK_WIDTH), ((j * BRICK_HEIGHT+(BRICK_HEIGHT / 2))), BRICK_WIDTH - 5, BRICK_HEIGHT - 5, color, numLives);
            }
        }
    }

    //запуск потока вызов метода run
    public void start() {
        game.resume();//возобновление
        isPaused.set(false);
    }

    //остановка потока
    public void stop() {

        game.suspend();//приостановка
    }

    //начало игры
    public void run() {
        xSpeed = 1;
        while(true) {
            int x1 = ball.getX();
            int y1 = ball.getY();

            //гарантия того, что скорость будет оптимальной
            if (Math.abs(xSpeed) > 1) {
                if (xSpeed > 1) {
                    xSpeed--;
                }
                if (xSpeed < 1) {
                    xSpeed++;
                }
            }

            checkPaddle(x1, y1);
            checkWall(x1, y1);
            checkBricks(x1, y1);
            checkLives();
            checkIfOut(y1);
            ball.move();
            repaint();

            try {
                int waitTime = 3;
                Thread.sleep(waitTime); //приостанавление
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    }





    //проверка жизней в игре, если жизней стало меньше нужного, то останавливаем игру
    public void checkLives() {
        if (bricksLeft == NO_BRICKS) {
            try {

            } catch (Exception e) {
                e.printStackTrace();
            }
            ball.reset();
            bricksLeft = MAX_BRICKS;
            makeBricks();
            lives++;
            score += 100;
            repaint();
            stop();
            isPaused.set(true);
        }
        if (lives == MIN_LIVES) {
            repaint();
            stop();
            isPaused.set(true);
        }
    }
    //проверка платформы
    public void checkPaddle(int x1, int y1) {
        if (paddle.hitPaddle(x1, y1) && ball.getXDir() < 0) {
            ball.setYDir(-1);
            xSpeed = -1;
            ball.setXDir(xSpeed);
        }
        if (paddle.hitPaddle(x1, y1) && ball.getXDir() > 0) {
            ball.setYDir(-1);
            xSpeed = 1;
            ball.setXDir(xSpeed);
        }

        if (paddle.getX() <= 0) {
            paddle.setX(0);
        }
        if (paddle.getX() + paddle.getWidth() >= getWidth()) {
            paddle.setX(getWidth() - paddle.getWidth());
        }
    }
   //проверка стенок
    public void checkWall(int x1, int y1) {
        if (x1 >= getWidth() - ball.getWidth()) {
            xSpeed = -Math.abs(xSpeed);
            ball.setXDir(xSpeed);
        }
        if (x1 <= 0) {
            xSpeed = Math.abs(xSpeed);
            ball.setXDir(xSpeed);
        }
        if (y1 <= 0) {
            ball.setYDir(1);
        }
        if (y1 >= getHeight()) {
            ball.setYDir(-1);
        }
    }

    public void checkBricks(int x1, int y1) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 5; j++) {
                if (brick[i][j].hitBottom(x1, y1)) {
                    ball.setYDir(1);
                    if (brick[i][j].isDestroyed()) {
                        bricksLeft--;
                        score += 50;

                    }
                }
                if (brick[i][j].hitLeft(x1, y1)) {
                    xSpeed = -xSpeed;
                    ball.setXDir(xSpeed);
                    if (brick[i][j].isDestroyed()) {
                        bricksLeft--;
                        score += 50;

                    }
                }
                if (brick[i][j].hitRight(x1, y1)) {
                    xSpeed = -xSpeed;
                    ball.setXDir(xSpeed);
                    if (brick[i][j].isDestroyed()) {
                        bricksLeft--;
                        score += 50;

                    }
                }
                if (brick[i][j].hitTop(x1, y1)) {
                    ball.setYDir(-1);
                    if (brick[i][j].isDestroyed()) {
                        bricksLeft--;
                        score += 50;

                    }
                }
            }
        }
    }

    public void checkIfOut(int y1) {
        if (y1 > PADDLE_Y_START + 10) {
            lives--;
            score -= 100;
            ball.reset();
            repaint();
            stop();
            isPaused.set(true);
        }
    }

    //заполнение доски
    @Override
    public void paintComponent(Graphics g) {
        Toolkit.getDefaultToolkit().sync();//сихронизация и связка компонентов
        super.paintComponent(g);
        paddle.draw(g);
        ball.draw(g);

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 5; j++) {
                brick[i][j].draw(g);
            }
        }
        g.setColor(Color.BLACK);
        g.drawString("Жизни: " + lives, 10, getHeight() - (getHeight()/10));
        g.drawString("Счет: " + score, 10, getHeight() - (2*(getHeight()/10)) + 25);
        g.drawString("Игрок: " + playerName, 10, getHeight() - (3*(getHeight()/10)) + 50);;

        if (lives == MIN_LIVES) {
            g.setColor(Color.BLACK);
            g.drawString("Нажмите на пробел два раза, чтобы играть еще раз", getWidth()/5, getHeight()-20);
        }
    }


    //класс, отвечающий за игровой процесс и управление
    private class BoardListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent ke) {
            int key = ke.getKeyCode();
            if (key == KeyEvent.VK_SPACE) {
                if (lives > MIN_LIVES) {
                    if (!isPaused.get()) {
                        stop();
                        isPaused.set(true);
                    }
                    else {
                        start();
                    }
                }
                else {
                    paddle.setWidth(getWidth()/7);
                    lives = MAX_LIVES;
                    score = 0;
                    bricksLeft = MAX_BRICKS;
                    makeBricks();
                    isPaused.set(true);
                    for (int i = 0; i < 10; i++) {
                        for (int j = 0; j < 5; j++) {
                            brick[i][j].setDestroyed(false);
                        }
                    }
                }
            }
            if (key == KeyEvent.VK_LEFT) {
                paddle.setX(paddle.getX() - 50); //влево сдвигаем -50 платф
            }
            if (key == KeyEvent.VK_RIGHT) {
                paddle.setX(paddle.getX() + 50);//вправо сдвигаем +50 платф
            }
        }


    }
}
