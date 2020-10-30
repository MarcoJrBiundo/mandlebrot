package twostartercode;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * Class that extends thread that will draw mandlebrot in sections(one section per thread)
 */
public class DrawingThread extends Thread {
    private static double MANDELBROT_RE_MIN = -2;
    private static double MANDELBROT_RE_MAX = 1;
    private static double MANDELBROT_IM_MIN = -1.2;
    private static double MANDELBROT_IM_MAX = 1.2;
    //If the current thread is drawing a starting point other than 0 on x axis
    private static int splitPostion = 0;


    /**
     * Empty constructor
     */
    public DrawingThread() {}

    /**
     * Run method which will go on its own thread
     */
    public void run() {
        double precision = Math.max((MANDELBROT_RE_MAX - MANDELBROT_RE_MIN) / 700, (MANDELBROT_IM_MAX - MANDELBROT_IM_MIN) / 600);
        int convergenceSteps = 50;

        //If current column is starting at x axis 0 run normally until split position (aka column is ending)
        if(splitPostion == 0){
        for (double c = MANDELBROT_RE_MIN, xR = TwoStarterCode.splits.get(splitPostion); xR < TwoStarterCode.splits.get(splitPostion + 1); c = c + precision, xR++) {
            for (double ci = MANDELBROT_IM_MIN, yR = 0; yR < 600; ci = ci + precision, yR++) {
                double convergenceValue = checkConvergence(ci, c, convergenceSteps);
                double t1 = (double) convergenceValue / convergenceSteps;
                double c1 = Math.min(255 * 2 * t1, 255);
                double c2 = Math.max(255 * (2 * t1 - 1), 0);

                if (convergenceValue != convergenceSteps) {
                    TwoStarterCode.canvas.getGraphicsContext2D().setFill(Color.color(c2 / 255.0, c1 / 255.0, c2 / 255.0));
                } else {
                    TwoStarterCode.canvas.getGraphicsContext2D().setFill(Color.PURPLE); // Convergence Color
                }
                TwoStarterCode.canvas.getGraphicsContext2D().fillRect(xR, yR, 1, 1);
            }
        }
        //if split position is greater than  0 this means variable c will need to be adjusted for starting at a certian point in the image
        }else{
            //determine what c should actually be at this point in the algorithm
            double m = -2.0;
            for (int i = 0; i < TwoStarterCode.splits.get(splitPostion); i++) {
                m = m + 0.004285714285714286;
            }
            //Run algorithm with new c value and draw from split position to end of split position
            for (double c = m, xR = TwoStarterCode.splits.get(splitPostion); xR < TwoStarterCode.splits.get(splitPostion + 1); c = c + precision, xR++) {
                for (double ci = MANDELBROT_IM_MIN, yR = 0; yR < 600; ci = ci + precision, yR++) {
                    double convergenceValue = checkConvergence(ci, c, convergenceSteps);
                    double t1 = (double) convergenceValue / convergenceSteps;
                    double c1 = Math.min(255 * 2 * t1, 255);
                    double c2 = Math.max(255 * (2 * t1 - 1), 0);

                    if (convergenceValue != convergenceSteps) {
                        TwoStarterCode.canvas.getGraphicsContext2D().setFill(Color.color(c2 / 255.0, c1 / 255.0, c2 / 255.0));
                    } else {
                        TwoStarterCode.canvas.getGraphicsContext2D().setFill(Color.PURPLE); // Convergence Color
                    }
                    TwoStarterCode.canvas.getGraphicsContext2D().fillRect(xR, yR, 1, 1);
                }
            }
        }
        //Not thread safe but threads are join and has shown no problems in testing
        splitPostion ++;
    }

    /**
     * Determines shade of pixel
     * @param ci
     * @param c
     * @param convergenceSteps
     * @return
     */
    private int checkConvergence(double ci, double c, int convergenceSteps) {
        double z = 0;
        double zi = 0;
        for (int i = 0; i < convergenceSteps; i++) {
            double ziT = 2 * (z * zi);
            double zT = z * z - (zi * zi);
            z = zT + c;
            zi = ziT + ci;

            if (z * z + zi * zi >= 4.0) {
                return i;
            }
        }
        return convergenceSteps;
    }
}
