import java.util.Random;

class Oscillator {
    /* TODO Дописать возможность выбора разных типов волны*/
    public double getSample(double cyclePosition){
        return Math.sin(2 * Math.PI * cyclePosition);
    }

    public double getSample(double cyclePosition, String waveForm) {
        double result = 0;
        if(waveForm.equals("Sin")) {
            result = getSinWave(cyclePosition);
        }
        if(waveForm.equals("Saw")) {
            result = getSawWave(cyclePosition);
        }
        if(waveForm.equals("Triangle")) {
            result = getTriangleWave(cyclePosition);
        }
        if(waveForm.equals("Pulse")) {
            result = getPulseWave(cyclePosition);
        }
        if(waveForm.equals("WiteNoise")) {
            result = getWhiteNoise();
        }
        return result;
    }

    private double getSinWave(double cyclePosition) {
        return Math.sin(2 * Math.PI * cyclePosition);
    }

    private double getSawWave(double cyclePosition) {
        return 2 * cyclePosition - 1;
    }

    private double getTriangleWave(double cyclePosition) {
        return cyclePosition  < 0.25 ? cyclePosition * 4 : cyclePosition < 0.75 ? 2 - 4 * cyclePosition : 4 * (cyclePosition - 1);
    }

    private double getPulseWave(double cyclePosition) {
        return (cyclePosition < 0.5) ? 1 : -1;
    }

    private double getWhiteNoise() {
        Random random = new Random();
        return random.nextGaussian();
    }


}