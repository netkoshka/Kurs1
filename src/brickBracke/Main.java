package brickBracke;
import javax.swing.*;
import java.awt.*;
public class Main extends JFrame implements Constants{

    private static JFrame frame;
    private static Board board;
    private static Container pane;
    private static Dimension dim;
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();//отслеживание исключения
        }
        frame = new JFrame("Brick Breaker");//название
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);//размер
        frame.setResizable(false); //нельзя изменить размер окна
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //закрытие по умолчанию

        board = new Board(WINDOW_WIDTH, WINDOW_HEIGHT);

        pane = frame.getContentPane();//обращение к панели содержимого
        pane.add(board); //добавление элемента board на панель

        //определение размера экрана
        dim = Toolkit.getDefaultToolkit().getScreenSize();//возвращение Toolkit и возвращение размера экрана в виде объекта Dim
        frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
        frame.setVisible(true);//окно видимое
    }
}
