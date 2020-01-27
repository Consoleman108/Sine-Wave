import java.nio.ByteBuffer;
import java.awt.*;
import javax.swing.*;
import javax.sound.sampled.*;

/* TODO Убрать из класса MainClass генерирование волны в класс AudioProcessor*/
public class MainClass extends JFrame {
    private SampleThread m_thread;
    AudioProcessor audioProcessor = new AudioProcessor();


    //Launch the app
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MainClass frame = new MainClass();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public MainClass()
    {
        //Non-UI stuff
        m_thread = new SampleThread();
        m_thread.start();
    }

    class SampleThread extends Thread {

        final static public int SAMPLING_RATE = 44100;
        final static public int SAMPLE_SIZE = 2;                 //Sample size in bytes
        final static public double BUFFER_DURATION = 0.100;      //About a 100ms buffer

        //You can play with the size of this buffer if you want.  Making it smaller speeds up
        //the response to the slider movement, but if you make it too small you will get
        //noise in your output from buffer underflows, etc...
        //final static public double BUFFER_DURATION = 0.100;      //About a 100ms buffer


        // Size in bytes of sine wave samples we'll create on each loop pass
        final static public int SINE_PACKET_SIZE = (int)(BUFFER_DURATION*SAMPLING_RATE*SAMPLE_SIZE);

        SourceDataLine line = audioProcessor.getAudioOutput().getSourceDataLine();
        public double fFreq;                                    //Set from the pitch slider
        public boolean bExitThread = false;
        public double fVol;
        public double fVolDelta;
        public double fFreqDelat;
        public double fFrecCyclePosition;
        public double fModulation;
        public double fFilter;
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
            double fCyclePosition = 0;

            System.out.println("Requested line buffer size = " + SINE_PACKET_SIZE*2);
            System.out.println("Actual line buffer size = " + line.getBufferSize());


            ByteBuffer cBuf = ByteBuffer.allocate(SINE_PACKET_SIZE);

            //On each pass main loop fills the available free space in the audio buffer
            //Main loop creates audio samples for sine wave, runs until we tell the thread to exit
            //Each sample is spaced 1/SAMPLING_RATE apart in time
                       
            while (bExitThread==false) {
                fFreq = audioProcessor.getUserInterface().getSliderPitchValue();
                fFreqDelat = Math.sin(2 * Math.PI * fFrecCyclePosition);
                
                //fFrecCyclePosition += fFreqDelat;
                //fVolDelta = (Short.MAX_VALUE * Math.sin(2*Math.PI * fFrecCyclePosition))/100; 
                
                fVol = audioProcessor.getUserInterface().getSliderVolumeValue();
                fVolDelta = fVol * Short.MAX_VALUE/100;
                
                fModulation = audioProcessor.getUserInterface().getSliderModulationValue();
                fFilter = audioProcessor.getUserInterface().getSliderFilterValue();
                
                
                double fCycleInc = fFreq/SAMPLING_RATE;                             // Fraction of cycle between samples
                double fFrecCycleInc = ((fModulation + 0.01)/ 100)/SAMPLING_RATE;   // Frec for modulation

                cBuf.clear();                                                       // Toss out samples from previous pass

                //Generate SINE_PACKET_SIZE samples based on the current fCycleInc from fFreq
                for (int i = 0; i < SINE_PACKET_SIZE/SAMPLE_SIZE; i++) {
                    //cBuf.putShort((short)(fVolDelta * Math.sin(2*Math.PI * fCyclePosition)));
                    //cBuf.putShort((short) filter.Process(fVolDelta * lowFrecOscillator.getSample(fFrecCyclePosition, "Sin") * oscillator.getSample(fCyclePosition,"Noise") ));
                    cBuf.putShort((short) filter.Process(fVolDelta * oscillator.getSample(fCyclePosition,"Sin"), fFilter ));
                    //cBuf.putShort((short) (fVolDelta * oscillator.getSample(fCyclePosition,"WiteNoise" )));

                    fCyclePosition += fCycleInc; //(fCycleInc + fFrecCyclePosition/100);
                    fFrecCyclePosition += fFrecCycleInc;
                    System.out.println("FrecCyclePosition = " + fFrecCyclePosition);
                    
                    if (fCyclePosition > 1)
                        fCyclePosition -= 1;
                        
                    if (fFrecCyclePosition > 1)
                        fFrecCyclePosition -= 1;
                }

                //Write sine samples to the line buffer
                // If the audio buffer is full, this would block until there is enough room,
                // but we are not writing unless we know there is enough space.
                line.write(cBuf.array(), 0, cBuf.position());


                //Wait here until there are less than SINE_PACKET_SIZE samples in the buffer
                //(Buffer size is 2*SINE_PACKET_SIZE at least, so there will be room for
                // at least SINE_PACKET_SIZE samples when this is true)
                try {
                    while (getLineSampleCount() > SINE_PACKET_SIZE)
                        Thread.sleep(1);                          // Give UI a chance to run
                }
                catch (InterruptedException e) {                // We don't care about this
                }
            }

            line.drain();
            line.close();
        }



        public void exit() {
            bExitThread=true;
        }
    }
    class Sliders {

    }
}
