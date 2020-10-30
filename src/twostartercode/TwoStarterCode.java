/*
Original Code from:
http://www.hameister.org/JavaFX_MandelbrotSet.html

COMP 10183 Parallel Processing, Assignment Two, 2018
Take this sample program and convert it to a multi threaded structure.

 */
package twostartercode;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;

public class TwoStarterCode extends Application {
    // Size of the canvas for the Mandelbrot set
    private static final int CANVAS_WIDTH = 700; 
    private static final int CANVAS_HEIGHT = 600;
    // Left and right border
    private static final int X_OFFSET = 25;
    // Top and Bottom border
    private static final int Y_OFFSET = 25;
    // Values for the Mandelbrot set
    private static double MANDELBROT_RE_MIN = -2;  
    private static double MANDELBROT_RE_MAX = 1;       
    private static double MANDELBROT_IM_MIN = -1.2;    
    private static double MANDELBROT_IM_MAX = 1.2;
    //Canvas on which to draw
    public static Canvas canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
    //!!!!Choose number of threads here!!!!!!
    public static final int numberOfWorkers = 10;
    // This variable holds the vertical split points for which the threads will draw there specified section
    public static ArrayList<Integer> splits = new ArrayList<>();

    /**
     * Method that starts and shows the mandlebrot
     * this method calls threads which draws the mandlebrot in columns
     * @param primaryStage
     * @throws InterruptedException
     */
    @Override
    public void start(Stage primaryStage) throws InterruptedException {
        Pane fractalRootPane = new Pane();
        //Create the splits array so threads can draw in their specified Columns
        int numberOfSplits = numberOfWorkers;
        int splitValues = 700 /numberOfSplits;
        for(int i = 0; i <= 700; i= i + splitValues) { splits.add(i); }

        //Create Threads run them and join them so they run in specific order
        for(int i = 0; i < numberOfWorkers; i ++) {
            DrawingThread dt = new DrawingThread();
            dt.run();
            dt.join();
       }



        //Drawing on the canvas I assume
        fractalRootPane.getChildren().add(canvas);
        Scene scene = new Scene(fractalRootPane, CANVAS_WIDTH + 2 * X_OFFSET, CANVAS_HEIGHT + 2 * Y_OFFSET);
        scene.setFill(Color.BLACK);
        primaryStage.setTitle("Mandelbrot Set");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Main run method
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }


}