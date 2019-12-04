import javax.sound.sampled.*;

public class AudioOutput {
    private SourceDataLine sourceDataLine;
    final static private int SINE_PACKET_SIZE = (int) (44100 * 2 * 0.100); // (BUFFER_DURATION*SAMPLING_RATE*SAMPLE_SIZE)

    public AudioOutput(){
        initAudioOutput();
    }

    private void initAudioOutput(){
        try {
            AudioFormat  audioFormat= new AudioFormat(44100, 16, 1, true, true); // format
            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat, SINE_PACKET_SIZE * 2); // info

            if (!AudioSystem.isLineSupported(dataLineInfo))
                throw new LineUnavailableException();

            sourceDataLine = (SourceDataLine)AudioSystem.getLine(dataLineInfo);
            sourceDataLine.open(audioFormat);
            sourceDataLine.start();
        }
        catch (LineUnavailableException e) {
            System.out.println("Line of that type is not available");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public SourceDataLine getSourceDataLine() {
        return sourceDataLine;
    }
}
