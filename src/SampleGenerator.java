import javax.sound.sampled.SourceDataLine;
import java.nio.ByteBuffer;

public class SampleGenerator extends Thread {

    final static public int SAMPLING_RATE = 44100;
    final static public int SAMPLE_SIZE = 2;                 //Sample size in bytes
    final static public double BUFFER_DURATION = 0.100;      //About a 100ms buffer
    final static public int SINE_PACKET_SIZE = (int)(BUFFER_DURATION*SAMPLING_RATE*SAMPLE_SIZE);
    AudioProcessor audioProcessor = new AudioProcessor();

    SourceDataLine line = audioProcessor.getAudioOutput().getSourceDataLine();
    public double oscillatorFrequencySlider;                                    //Set from the pitch slider
    public boolean isThreadEnd = false;
    public double oscillatorVolumeSlider;
    public double stepIncrementVolume;
    public double oscillatorModulationSlider;
    public double oscillatorFilterSlider;
    public double oscillatorFreqCyclePosition;
    double        modulationOscillatorFreqCyclePosition;
    double oscillatorCycleIncrement;
    double modulationFreqCycleIncrement;
    String waveType;

    Oscillator oscillator = new Oscillator();
    Oscillator lowFrecOscillator = new Oscillator();
    Filter filter =  new Filter();

    private int getLineSampleCount() {
        return line.getBufferSize() - line.available();
    }


    public void run() {
        oscillatorFreqCyclePosition = 0;
        modulationOscillatorFreqCyclePosition = 0;

        System.out.println("Requested line buffer size = " + SINE_PACKET_SIZE * 2);
        System.out.println("Actual line buffer size = " + line.getBufferSize());

        ByteBuffer byteBuffer = ByteBuffer.allocate(SINE_PACKET_SIZE);

        while (!isThreadEnd) {

            getDataUI();
            stepIncrementVolume = getStepIncrementVolume();
            oscillatorCycleIncrement = getOscillatorCycleIncrement();
            modulationFreqCycleIncrement = getModulationFreqCycleIncrement();

            byteBuffer.clear();

            for (int i = 0; i < SINE_PACKET_SIZE/SAMPLE_SIZE; i++) {
                //cBuf.putShort((short) filter.Process(stepIncrementVolume * oscillator.getSample(oscillatorFreqCyclePosition,"Sin"), oscillatorFilterSlider));
                byteBuffer.putShort((short) filter.Process(stepIncrementVolume * lowFrecOscillator.getSample(modulationOscillatorFreqCyclePosition,"Sin") * oscillator.getSample(oscillatorFreqCyclePosition, waveType ), oscillatorFilterSlider));

                oscillatorFreqCyclePosition += oscillatorCycleIncrement;
                modulationOscillatorFreqCyclePosition += modulationFreqCycleIncrement;
//                System.out.println("FrecCyclePosition = " + oscillatorFreqCyclePosition);
//                System.out.println("Wave Type = " + waveType );

                if (oscillatorFreqCyclePosition > 1)
                    oscillatorFreqCyclePosition -= 1;

                if (oscillatorFreqCyclePosition > 1)
                    oscillatorFreqCyclePosition -= 1;
            }

            line.write(byteBuffer.array(), 0, byteBuffer.position());

            try {
                while (getLineSampleCount() > SINE_PACKET_SIZE) {
                    Thread.sleep(1);
                }
            }
            catch (InterruptedException e) {
            }
        }

        line.drain();
        line.close();
    }

    private void getDataUI() {
        oscillatorFrequencySlider = audioProcessor.getUserInterface().getSliderPitchValue();
        oscillatorVolumeSlider = audioProcessor.getUserInterface().getSliderVolumeValue();
        oscillatorModulationSlider = audioProcessor.getUserInterface().getSliderModulationValue();
        oscillatorFilterSlider = audioProcessor.getUserInterface().getSliderFilterValue();
        waveType = audioProcessor.getUserInterface().getWaveType();
    }

    private double getStepIncrementVolume() {
        return Short.MAX_VALUE * oscillatorVolumeSlider /100;
    }

    private double getOscillatorCycleIncrement() {
        return oscillatorFrequencySlider/SAMPLING_RATE;
    }

    private double getModulationFreqCycleIncrement() {
        return ((oscillatorModulationSlider + 0.01)/ 100)/SAMPLING_RATE;
    }
}
