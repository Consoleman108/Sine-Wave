public class Filter {

    //public class BiquadConvolutionTable
    //{

        public double B0, B1, B2, A1, A2;
        double b0, b1, b2, a0, a1, a2;
        double CutoffFrequencyMin = 100;
        double CutoffFrequencyMax = 2000;

        private double[] _x = new double[3];
        private double[] _y = new double[3];

        public double Process(double s, double filterFreq)
        {
            lowPassFilter(filterFreq);
            //hiPassFilter(filterFreq);
            //bandPassFilter(filterFreq);

            // "сдвигаем" предыдущие семплы
            _x[2] = _x[1];
            _x[1] = _x[0];
            _x[0] = s;

            _y[2] = _y[1];
            _y[1] = _y[0];

            B0 = b0 / a0;
            B1 = b1 / a0;
            B2 = b2 / a0;
            A1 = a1 / a0;
            A2 = a2 / a0;
            // свертка
            _y[0] = B0 * _x[0] + B1 * _x[1] + B2 * _x[2] - A1 * _y[1] - A2 * _y[2];

            return _y[0];
        }

        private void setB0(double B0){
            this.B0 = B0;
        }

        private void setB1(double B1){
            this.B1 = B1;
        }

        private void setB2(double B2){
            this.B2 = B2;
        }

        private void setA1(double A1){
            this.A1 = A1;
        }

        private void setA2(double A2){
            this.A2 = A2;
        }

        private double TransformFrequency(double w)
        {
            return Math.tan(Math.PI * w / 44100);
        }

        public void lowPassFilter(double cutoffFreq) {
            double w = TransformFrequency(cutoffFreq);

            a0 = 1 + Math.sqrt(2) * w + w * w;
            a1 = -2 + 2 * w * w;
            a2 = 1 - Math.sqrt(2) * w + w * w;

            b0 = w * w;
            b1 = 2 * w * w;
            b2 = w * w;
        }

        public void hiPassFilter(double cutoffFreq) {
                    double w = TransformFrequency(cutoffFreq);

                    a0 = 1 + Math.sqrt(2) * w + w * w;
                    a1 = -2 + 2 * w * w;
                    a2 = 1 - Math.sqrt(2) * w + w * w;

                    b0 = 1;
                    b1 = -2;
                    b2 = 1;
         }

       public void bandPassFilter(double cutoffFreq) {
                    double w = cutoffFreq;
                    double d = w / 4;

                    // определим полосу фильтра как [w * 3 / 4, w * 5 / 4]
                    double w1 = Math.max(w - d, CutoffFrequencyMin);
                    double w2 = Math.min(w + d, CutoffFrequencyMax);
                    w1 = TransformFrequency(w1);
                    w2 = TransformFrequency(w2);

                    double w0Sqr = w2 * w1; // w0^2
                    double wd = w2 - w1; // W

                    a0 = -1 - wd - w0Sqr;
                    a1 = 2 - 2 * w0Sqr;
                    a2 = -1 + wd - w0Sqr;
                    b0 = -wd;
                    b1 = 0;
                    b2 = wd;
       }
//        private void CalculateCoefficients(double cutoff)
//        {
//            double CutoffFrequencyMin = 100;
//            double CutoffFrequencyMax = 300;
//            BiquadConvolutionTable _table = new BiquadConvolutionTable();
//            String FilterTypeValue = "LowPass";
//
//            switch (FilterTypeValue)
//            {
//                case "LowPass":
//                {
//                    double w = TransformFrequency(cutoff);
//
//                    a0 = 1 + Math.sqrt(2) * w + w * w;
//                    a1 = -2 + 2 * w * w;
//                    a2 = 1 - Math.sqrt(2) * w + w * w;
//
//                    b0 = w * w;
//                    b1 = 2 * w * w;
//                    b2 = w * w;
//                }
//
//                break;
//
//                case "HiPass":
//                {
//                    double w = TransformFrequency(cutoff);
//
//                    a0 = 1 + Math.sqrt(2) * w + w * w;
//                    a1 = -2 + 2 * w * w;
//                    a2 = 1 - Math.sqrt(2) * w + w * w;
//
//                    b0 = 1;
//                    b1 = -2;
//                    b2 = 1;
//                }
//
//                break;
//
//                case "BandPass":
//                {
//                    double w = cutoff;
//                    double d = w / 4;
//
//                    // определим полосу фильтра как [w * 3 / 4, w * 5 / 4]
//                    double w1 = Math.max(w - d, CutoffFrequencyMin);
//                    double w2 = Math.min(w + d, CutoffFrequencyMax);
//                    w1 = TransformFrequency(w1);
//                    w2 = TransformFrequency(w2);
//
//                    double w0Sqr = w2 * w1; // w0^2
//                    double wd = w2 - w1; // W
//
//                    a0 = -1 - wd - w0Sqr;
//                    a1 = 2 - 2 * w0Sqr;
//                    a2 = -1 + wd - w0Sqr;
//                    b0 = -wd;
//                    b1 = 0;
//                    b2 = wd;
//                }
//
//                break;
//
//            }
//
//        _table.setB0(b0 / a0); //B0 = b0 / a0;
//        _table.setB1(b1 / a0); //B1 = b1 / a0;
//        _table.setB2(b2 / a0); //B2 = b2 / a0;
//        _table.setA1(a1 / a0); //A1 = a1 / a0;
//        _table.setA2(a2 / a0); //A2 = a2 / a0;
//        }
    //}
}
