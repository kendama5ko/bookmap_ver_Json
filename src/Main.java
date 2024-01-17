import javax.swing.SwingUtilities;
import window.mainFrame.MainFrame;

public class Main {
    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                bookMap();
            }
        });
    }

    public static void bookMap() {
        MainFrame mainFrame = new MainFrame();
        mainFrame.run();
    }
}
