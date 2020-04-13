import java.awt.*;
public class MainClass {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Launcher launcher = new Launcher();
                launcher.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}