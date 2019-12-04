class Oscillator {
    /* TODO Дописать возможность выбора разных типов волны*/
    double getSample(double cyclePosition){
        double result = Math.sin(2*Math.PI * cyclePosition);
        return result;
    }
}