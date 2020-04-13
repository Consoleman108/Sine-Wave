import java.awt.*;


/* TODO Убрать из класса MainClass генерирование волны в класс AudioProcessor*/
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