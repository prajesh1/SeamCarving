package main;

/******************************************************************************
 *  Compilation:  javac ShowSeams.java
 *  Execution:    java ShowSeams input.png
 *  Dependencies: SeamCarver.java SCUtility.java
 *
 *  Read image from file specified as command line argument. Show 3 images 
 *  original image as well as horizontal and vertical seams of that image.
 *  Each image hides the previous one - drag them to see all three.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

public class ShowSeams {

    private static void showHorizontalSeam(SeamCarver sc) {
        Picture picture = SCUtility.toEnergyPicture(sc);
       int[] horizontalSeam = sc.findHorizontalSeam();
       for(int i=0;i<horizontalSeam.length;i++)
           System.out.println(horizontalSeam[i]);
        Picture overlay = SCUtility.seamOverlay(picture, true, horizontalSeam);
        //overlay.show();
    }


    private static void showVerticalSeam(SeamCarver sc) {
        Picture picture = SCUtility.toEnergyPicture(sc);
        int[] verticalSeam = sc.findVerticalSeam();
        for(int i=0;i<verticalSeam.length;i++)
        System.out.println(verticalSeam[i]);
       // Picture overlay = SCUtility.seamOverlay(picture, false, verticalSeam);
       // overlay.show();
    }

    public static void main(String[] args) {
        Picture picture = new Picture(args[0]);
        StdOut.printf("image is %d columns by %d rows\n", picture.width(), picture.height());
        picture.show();        
        SeamCarver sc = new SeamCarver(picture);
        
        StdOut.printf("Displaying horizontal seam calculated.\n");
        showHorizontalSeam(sc);

        StdOut.printf("Displaying vertical seam calculated.\n");
        showVerticalSeam(sc);
        
       /* for (int j = 0; j < sc.height(); j++) {
          for (int i = 0; i < sc.width(); i++)
              StdOut.printf("%.2f ", sc.energyArray[i][j]);
          StdOut.println();
        }
        System.out.println("Value is" +sc.energyArray[0][sc.height()-1]);
       
          for (int j = 0; j < sc.height(); j++) {
            for (int i = 0; i < sc.width(); i++)
                StdOut.printf("%d ", sc.pathArray[i][j]);
            StdOut.println();
        }*/

    }

}
