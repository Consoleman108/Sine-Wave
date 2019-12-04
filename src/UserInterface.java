import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class UserInterface extends JFrame {
    private AudioProcessor audioProcessor;

    public final static Dimension RIGID_DIMENSION = new Dimension(1, 3);

    private JSlider sliderPitch = initSlider("Pitch", 100, 500, 4100,440);
    private JSlider sliderVolume = initSlider("Volume", 0, 50, 100,30);;
    private JSlider sliderModulation = initSlider("Modulation", 0, 500, 1000,50);;


    public UserInterface(AudioProcessor currentAudioProcessor){
        this.audioProcessor = currentAudioProcessor;
        initUserInterface();
    }

    private void initUserInterface(){

        setTitle("Frequency Sine Wave");
        setSize(800, 400);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainJPanel = new JPanel();
        mainJPanel.setLayout(new GridLayout(4, 1));

        JPanel panelRadioButton = new JPanel(new GridLayout(1, 0, 0, 5));
        panelRadioButton.setBorder(BorderFactory.createTitledBorder("Wave Form"));

        // Список для типов волн
        String[] waveForm = { "Sin", "Saw", "Triangle", "Pulse" };

        // Создаем группу радиокнопок
        ButtonGroup buttonGroup = new ButtonGroup();
        // Оъединяем кнопки в группу
        for (int i = 0; i < waveForm.length; i++) {
            JRadioButton jRadioButton = new JRadioButton(waveForm[i]);
            panelRadioButton.add(jRadioButton);
            buttonGroup.add(jRadioButton);
        }
        mainJPanel.add(panelRadioButton);

        mainJPanel.add(initPanel("Pitch", sliderPitch));
        mainJPanel.add(initPanel("Volume", sliderVolume));
        mainJPanel.add(initPanel("Modulation", sliderModulation));

        getContentPane().add(mainJPanel, BorderLayout.CENTER);
        setVisible(true);

    }

    public double getSliderPitchValue(){
        return sliderPitch.getValue();
    }

    public double getSliderVolumeValue(){
        return sliderVolume.getValue();
    }

    public double getSliderModulationValue(){
        return sliderModulation.getValue();
    }

    private JLabel getLable(String lableName){
        JLabel jLabel = new JLabel(lableName);
        jLabel.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        return jLabel;
    }

    private JSlider initSlider(String sliderName, int minimum,int majorTickSpacing, int maximum, int defaultValue){
        JSlider jSlider = new JSlider();
        jSlider.setName(sliderName);
        jSlider.setMinimum(minimum);
        jSlider.setPaintLabels(true);
        jSlider.setPaintTicks(true);
        jSlider.setMajorTickSpacing(majorTickSpacing);
        jSlider.setMaximum(maximum);
        jSlider.setValue(defaultValue);
        return jSlider;
    }

    private JPanel initPanel(String title, JSlider jSlider){
        JPanel jPanel = new JPanel();

        jPanel.setBorder(new TitledBorder(new EtchedBorder(), title));
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
        jPanel.add(Box.createRigidArea(RIGID_DIMENSION));
        jPanel.add(jSlider);
        jPanel.add(Box.createRigidArea(RIGID_DIMENSION));
        return jPanel;
    }
}
