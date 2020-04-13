import javax.sound.sampled.*;

public class AudioOutput {
    private SourceDataLine sourceDataLine;
    final static private int SINE_PACKET_SIZE = (int) (44100 * 2 * 0.100); // (BUFFER_DURATION*SAMPLING_RATE*SAMPLE_SIZE)
    final static private int SAMLPE_RATE = 44100;
    final static private int SAMPLE_SIZE_BITS = 16;
    final static private int CHANNELS = 1;
    final static private boolean SIGNED = true;
    final static private boolean BIG_ENDIAN = true;

    public AudioOutput(){
        initAudioOutput();
    }

    private void initAudioOutput(){
        try {
            AudioFormat  audioFormat= new AudioFormat(SAMLPE_RATE, SAMPLE_SIZE_BITS, CHANNELS, SIGNED, BIG_ENDIAN); // format
            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat, SINE_PACKET_SIZE * 2); // info

            if (!AudioSystem.isLineSupported(dataLineInfo))
                throw new LineUnavailableException();

            sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
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
