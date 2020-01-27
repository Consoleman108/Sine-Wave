import java.util.Random;

class Oscillator {
    /* TODO Дописать возможность выбора разных типов волны*/
    public double getSample(double cyclePosition){
        double result = Math.sin(2 * Math.PI * cyclePosition);
        return result;
    }

    public double getSample(double cyclePosition, String waveForm) {
        double result = 0;

        if(waveForm == "Sin") {
            result = getSinWave(cyclePosition);
        }
        if(waveForm == "Saw") {
            result = getSawWave(cyclePosition);
        }
        if(waveForm == "Triangle") {
            result = getTriangleWave(cyclePosition);
        }
        if(waveForm == "Pulse") {
            result = getPulseWave(cyclePosition);
        }
        if(waveForm == "WiteNoise") {
            result = getWhiteNoise();
        }
        return result;
    }

    private double getSinWave(double cyclePosition) {
        double result = Math.sin(2 * Math.PI * cyclePosition);
        return result;
    }

    private double getSawWave(double cyclePosition) {
        double result = 2 * cyclePosition - 1;
        return result;
    }

    private double getTriangleWave(double cyclePosition) {
        double result = cyclePosition  < 0.25 ? cyclePosition * 4 : cyclePosition < 0.75 ? 2 - 4 * cyclePosition : 4 * (cyclePosition - 1);
        return result;
    }

    private double getPulseWave(double cyclePosition) {
        double result = (cyclePosition < 0.5) ? 1 : -1;
        return result;
    }

    private double getWhiteNoise() {
        Random random = new Random();
        double result = random.nextGaussian();
        return result;
    }


}