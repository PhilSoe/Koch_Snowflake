package com.company;

import java.awt.image.BufferedImage;

public class Line {

    double x1; // the start x position of the line
    double y1; // the start y position of the line

    double x2; // the end x position of the line
    double y2; // the end y position of the line

    boolean leaf = true; // if this lines node is a leaf

    Line[] child_lines = new Line[4]; // The array to contain the 4 child lines


    /**
     * Constructor of the line
     *
     * @param x1 the x coordinate of the start of the line
     * @param y1 the y coordinate of the start of the line
     * @param x2 the x coordinate of the end of the line
     * @param y2 the y coordinate of the end of the line
     */
    public Line(double x1, double y1, double x2, double y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }


    /**
     * @return Weather this line is the last node in the tree of lines.
     */
    public boolean isLeaf() {
        return leaf;
    }


    /**
     * This method creates 4 lines which result from applying the koch algorithm.
     *
     * If it has already generated these lines it passes the order down to the "Child Lines"
     */
    public void kochStep() {
        if (this.isLeaf()) {

            // x,y of the 1/3 point of the base line
            double xl = (x2 - x1) / 3.0 + x1;
            double yl = (y2 - y1) / 3.0 + y1;

            // x,y of the 2/3 point of the base line
            double xr = x2 - (x2 - x1) / 3.0;
            double yr = y2 - (y2 - y1) / 3.0;

            // this multiplier is added because the Math.atan function only ranges from -pi/2 to pi/2
            int multiplier;
            if (x1 > x2){
                multiplier = 1;
            } else {
                multiplier = 0;
            }

            // theta in radians of the angle of the line.
            double theta = Math.atan((y2 - y1) / (x2 - x1)) - Math.PI * multiplier;

            // the length of the line
            double length = Math.sqrt((Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2)));

            // x,y of the top of the triangle to be added during a koch step
            double xc = Math.cos(theta + Math.PI / 3.0) * (length / 3.0) + xl;
            double yc = Math.sin(theta + Math.PI / 3.0) * (length / 3.0) + yl;

            // adding new child lines which use the new points
            child_lines[0] = new Line(x1, y1, xl, yl);
            child_lines[1] = new Line(xl, yl, xc, yc);
            child_lines[2] = new Line(xc, yc, xr, yr);
            child_lines[3] = new Line(xr, yr, x2, y2);

            // setting this line to no longer be a leaf as it now has children
            this.leaf = false;

        } else { // Passing the order down to the children recursively
            child_lines[0].kochStep();
            child_lines[1].kochStep();
            child_lines[2].kochStep();
            child_lines[3].kochStep();
        }
    }


    /**
     * This method takes an image and colors the pixels green where they intersect the line.
     *
     * If this line is not a leaf it will pass the image down to it's children to complete.
     *
     * @param image the image to be manipulated
     */
    public void drawSelf(BufferedImage image) {
        if (!isLeaf()){ // Passing the image down to the children recursively.
            child_lines[0].drawSelf(image);
            child_lines[1].drawSelf(image);
            child_lines[2].drawSelf(image);
            child_lines[3].drawSelf(image);
        }
        else {
            // x values to be iterated over starting with the left most x value
            double ix = Math.min(x1, x2);

            // right most x value of the line
            double end_x = Math.max(x1, x2);

            // Slope of the line
            double m = (y2 - y1) / (x2 - x1);

            // Y intercept of the line
            double b = y1 - x1 * m;

            // y values to be calculated
            double y;

            while (ix <= end_x) {
                y = m * ix + b; // Using the equation of a line to only have one loop
                try {
                    // Coloring every pixel which intersects the line (and the pixels near it for a nicer final image)
                    image.setRGB((int) ix, (int) y, -16711936);
                    image.setRGB((int) ix, (int) y + 1, -16711936);
                    image.setRGB((int) ix + 1, (int) y, -16711936);
                } catch (ArrayIndexOutOfBoundsException e) { // In case the point of the line ends up out of bounds
                    return;
                }
                ix += 1.0;
            }
        }
    }

}
