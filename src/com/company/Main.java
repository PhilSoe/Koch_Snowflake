package com.company;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {

    final static int SIDE_LIM = 1000; // The base side length of the output image
    final static int MULT = 9; // the size multiplier of the base image and lines within
    final static int ITTER = 10; // the number of iterations

    public static void main(String[] args) {

        // Instantiating the three base lines for the first triangle
        Line line1 = new Line(100 * MULT, 750 * MULT, 900 * MULT, 750 * MULT);
        Line line2 = new Line(900 * MULT, 750 * MULT, 500 * MULT, 57 * MULT);
        Line line3 = new Line(500 * MULT, 57 * MULT, 100 * MULT, 750 * MULT);

        // Applying the recursive fractal step ITTER number of times
        int i = 0;
        while(i < ITTER){
            line1.kochStep();
            line2.kochStep();
            line3.kochStep();
            i ++;
        }


        // Instantiating the image and drawing lines.
        BufferedImage img = new BufferedImage(SIDE_LIM * MULT + 1, SIDE_LIM * MULT + 1, BufferedImage.TYPE_INT_RGB);
        line1.drawSelf(img);
        line2.drawSelf(img);
        line3.drawSelf(img);

        // Saving the Image
        File file = new File("image.jpg");
        try {
            ImageIO.write(img, "jpg", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
