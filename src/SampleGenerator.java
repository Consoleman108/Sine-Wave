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


    //Get the number of queued samples in the SourceDataLine buffer
    private int getLineSampleCount() {
        return line.getBufferSize() - line.available();
    }


    //Continually fill the audio output buffer whenever it starts to get empty, SINE_PACKET_SIZE/2
    //samples at a time, until we tell the thread to exit
    public void run() {
        //Position through the sine wave as a percentage (i.e. 0-1 is 0-2*PI)
        oscillatorFreqCyclePosition = 0;
        modulationOscillatorFreqCyclePosition = 0;

        System.out.println("Requested line buffer size = " + SINE_PACKET_SIZE * 2);
        System.out.println("Actual line buffer size = " + line.getBufferSize());

        ByteBuffer byteBuffer = ByteBuffer.allocate(SINE_PACKET_SIZE);

        //On each pass main loop fills the available free space in the audio buffer
        //Main loop creates audio samples for sine wave, runs until we tell the thread to exit
        //Each sample is spaced 1/SAMPLING_RATE apart in time

        while (!isThreadEnd) {
            oscillatorFrequencySlider = audioProcessor.getUserInterface().getSliderPitchValue();
            oscillatorVolumeSlider = audioProcessor.getUserInterface().getSliderVolumeValue();
            oscillatorModulationSlider = audioProcessor.getUserInterface().getSliderModulationValue();
            oscillatorFilterSlider = audioProcessor.getUserInterface().getSliderFilterValue();
            waveType = audioProcessor.getUserInterface().getWaveType();

            stepIncrementVolume = Short.MAX_VALUE * oscillatorVolumeSlider /100;

            oscillatorCycleIncrement = oscillatorFrequencySlider/SAMPLING_RATE;                             // Fraction of cycle between samples
            modulationFreqCycleIncrement = ((oscillatorModulationSlider + 0.01)/ 100)/SAMPLING_RATE;   // Frec for modulation

            byteBuffer.clear();                                                       // Toss out samples from previous pass

            //Generate SINE_PACKET_SIZE samples based on the current fCycleInc from fFreq
            for (int i = 0; i < SINE_PACKET_SIZE/SAMPLE_SIZE; i++) {
                //cBuf.putShort((short) filter.Process(stepIncrementVolume * oscillator.getSample(oscillatorFreqCyclePosition,"Sin"), oscillatorFilterSlider));
                byteBuffer.putShort((short) filter.Process(stepIncrementVolume * lowFrecOscillator.getSample(modulationOscillatorFreqCyclePosition,"Sin") * oscillator.getSample(oscillatorFreqCyclePosition, waveType ), oscillatorFilterSlider));

                oscillatorFreqCyclePosition += oscillatorCycleIncrement;
                modulationOscillatorFreqCyclePosition += modulationFreqCycleIncrement;
                System.out.println("FrecCyclePosition = " + oscillatorFreqCyclePosition);
                System.out.println("Wave Type = " + waveType );

                if (oscillatorFreqCyclePosition > 1)
                    oscillatorFreqCyclePosition -= 1;

                if (oscillatorFreqCyclePosition > 1)
                    oscillatorFreqCyclePosition -= 1;
            }

            //Write sine samples to the line buffer
            // If the audio buffer is full, this would block until there is enough room,
            // but we are not writing unless we know there is enough space.
            line.write(byteBuffer.array(), 0, byteBuffer.position());


            //Wait here until there are less than SINE_PACKET_SIZE samples in the buffer
            //(Buffer size is 2*SINE_PACKET_SIZE at least, so there will be room for
            // at least SINE_PACKET_SIZE samples when this is true)
            try {
                while (getLineSampleCount() > SINE_PACKET_SIZE) {
                    Thread.sleep(1);
                }                        // Give UI a chance to run
            }
            catch (InterruptedException e) {                // We don't care about this
            }
        }

        line.drain();
        line.close();
    }
}
