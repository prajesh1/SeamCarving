import java.awt.Color;

import edu.princeton.cs.algs4.Picture;
/*
 * Only Picture datastructure is having Cartesian coordinates all other
 * data structures are using normal 2d indices
 */
public class SeamCarver {
  private Picture picture;  
  private int[][] redColorPicture;
  private int[][] greenColorPicture;
  private int[][] blueColorPicture;
  private boolean pictureModified = false;
  private double[][] energyArray;
  private int[][] pathArray;
  private boolean pictureTransposed = false;

  // create a seam carver object based on the given picture
  public SeamCarver(Picture picture) {
    if (picture == null)
      throw new java.lang.NullPointerException();
    this.picture = new Picture(picture); //for immutability
    
    redColorPicture   = new int[this.height()][this.width()];
    greenColorPicture = new int[this.height()][this.width()];
    blueColorPicture  = new int[this.height()][this.width()];
    
    for(int i=0;i<this.height();i++)
      for(int j=0;j<this.width();j++){
        redColorPicture[i][j]   = picture.get(j, i).getRed();
        greenColorPicture[i][j] = picture.get(j, i).getGreen();
        blueColorPicture[i][j]  = picture.get(j, i).getBlue();
      }
  }

  // current picture
  public Picture picture() {
    if(pictureModified==true) //if picture is modified(seam removed) change the picture
      for(int i=0;i<this.height();i++)
        for(int j=0;j<this.width();j++)
          this.picture.set(j, i, new Color(redColorPicture[i][j],
              greenColorPicture[i][j],blueColorPicture[i][j]));
    return this.picture;
  }

  // width of current picture
  public int width() {
    return this.picture.width();
  }

  // height of current picture
  public int height() {
    return this.picture.height();
  }

  // energy of pixel at column x and row y
  public double energy(int x, int y) {
    if (x < 0 || y < 0 || x >= this.width() || y >= this.height())
      throw new java.lang.IndexOutOfBoundsException();
    if (x == 0 || y == 0 || x == this.width() - 1 || y == this.height() - 1) {
      return 1000;
    }

    double xGradient = Math.pow(centralXDifference(x, y, 'R'), 2)
        + Math.pow(centralXDifference(x, y, 'G'), 2) + Math.pow(centralXDifference(x, y, 'B'), 2);
    double yGradient = Math.pow(centralYDifference(x, y, 'R'), 2)
        + Math.pow(centralYDifference(x, y, 'G'), 2) + Math.pow(centralYDifference(x, y, 'B'), 2);

    return Math.sqrt(xGradient + yGradient);

  }

  private double centralXDifference(int x, int y, char color) {
    switch (color) {
    case 'R':
      return this.redColorPicture[y+1][x] - this.redColorPicture[y-1][x];      
    case 'B':
      return this.blueColorPicture[y+1][x] - this.blueColorPicture[y-1][x];
    case 'G':
      return this.greenColorPicture[y+1][x] - this.greenColorPicture[y-1][x];
    default:
      break;
    }
    return 0;
  }

  private double centralYDifference(int x, int y, char color) {
    switch (color) {
    case 'R':
      return this.redColorPicture[y][x+1] - this.redColorPicture[y][x-1];
    case 'B':
      return this.blueColorPicture[y][x+1] - this.blueColorPicture[y][x-1];
    case 'G':
      return this.greenColorPicture[y][x+1] - this.greenColorPicture[y][x-1];
    default:
      break;
    }
    return 0;
  }

  public int[] findHorizontalSeam() // sequence of indices for horizontal seam
  {
    if (this.pictureTransposed==false) {
      transpose();
      pictureTransposed = true;
    }

    return findSeam();

  }

  private void transpose() { 
    int[][] tempRedColor = new int[this.redColorPicture[0].length][this.redColorPicture.length];
    int[][] tempGreenColor = new int[this.redColorPicture[0].length][this.redColorPicture.length];
    int[][] tempBlueColor = new int[this.redColorPicture[0].length][this.redColorPicture.length];
    
    for (int i = 0; i < this.redColorPicture.length; i++) {
      for (int j = 0; j <this.redColorPicture[0].length; j++){
        tempRedColor[j][i] = this.redColorPicture[i][j];
        tempGreenColor[j][i] = this.greenColorPicture[i][j];
        tempBlueColor[j][i] = this.blueColorPicture[i][j];
      }      
    }   
    this.redColorPicture = tempRedColor;
    this.greenColorPicture = tempGreenColor;
    this.blueColorPicture = tempRedColor;
  }

  // sequence of indices for vertical seam
  public int[] findVerticalSeam() {
    if (this.pictureTransposed==true) {
      transpose();
      this.pictureTransposed= true;
    }
       return findSeam();

  }

  private int[] findSeam() {
    energyArray = new double[this.height()][this.width()];
    pathArray = new int[this.height()][this.width()];
 
  //  System.out.println(this.energyArray[1][4]);
  //  System.out.println(this.redColorPicture[0][5]);
  //  System.out.println(this.energy(4, 1));
    
    for (int i = 0; i < this.height(); i++) {
      for (int j = 0; j < this.width(); j++) {
        energyArray[i][j] = this.energy(j, i);
      }
    }
    
    TopologicalSort topologicalSort = new TopologicalSort(this.energyArray);
    int[] sortedList = topologicalSort.getShortestPath();
    return sortedList;
  }  
  
  public void removeHorizontalSeam(int[] seam) // remove horizontal seam from current picture
  {

    if (invalidSeam(seam))
      throw new java.lang.IllegalArgumentException();
    removeSeam(seam);

  }

  private boolean invalidSeam(int[] seam) {
    if (seam.length != this.height())
      throw new java.lang.IllegalArgumentException();
    for (int i = 1; i < seam.length; i++)
      if (Math.abs(seam[i - 1] - seam[i]) > 1)
        return true;
    return false;
  }

  public void removeVerticalSeam(int[] seam) // remove vertical seam from current picture
  {
    if (invalidSeam(seam))
      throw new java.lang.IllegalArgumentException();

    removeSeam(seam);
  }

  private void removeSeam(int[] seam) {

    Color[][] p = new Color[this.width() - 1][this.height()];
    for (int j = 0; j < this.height(); j++) {
      for (int i = 0, k = 0; i < this.width(); i++) {
        if (i != seam[j]) {

       //   p[k][j] = this.picture[i][j];
          k++;
        }
      }
    }
   // this.picture = p;
  //  this.width = this.width() - 1;
  }

  public static void main(String[] args) {
     //SeamCarver s= new SeamCarver();

  }
}