class Oscillator {
    double getSample(double cyclePosition){
        double result = Math.sin(2*Math.PI * cyclePosition);
        return result;
    }
}