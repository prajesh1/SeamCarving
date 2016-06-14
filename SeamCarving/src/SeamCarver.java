import java.awt.Color;

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
  private Picture picture;  
  private double[][] energyArray;
  private int[][] pathArray;
  private boolean originalPicture = true;
  private int[] topologicallySorted;

  // create a seam carver object based on the given picture
  public SeamCarver(Picture picture) {
    if (picture == null)
      throw new java.lang.NullPointerException();
    this.picture = new Picture(picture); //for immutability
  }

  // current picture
  public Picture picture() {
    if (!originalPicture) {
      transpose();
      originalPicture = true;
    }
    Picture p = new Picture(this.width, this.height);
    for (int i = 0; i < this.width; i++)
      for (int j = 0; j < this.height; j++) {
        if (this.picture[i][j] == null)
          System.out.println("null is" + i + j);
        p.set(i, j, this.picture[i][j]);
      }
    return p;
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
      return this.picture.get(x + 1,y).getRed() - this.picture.get(x - 1,y).getRed();      
    case 'B':
      return this.picture.get(x + 1,y).getBlue() - this.picture.get(x - 1,y).getBlue();
    case 'G':
      return this.picture.get(x + 1,y).getGreen() - this.picture.get(x - 1,y).getGreen();
    default:
      break;
    }
    return 0;
  }

  private double centralYDifference(int x, int y, char color) {
    switch (color) {
    case 'R':
      return this.picture.get(x,y + 1).getRed() - this.picture.get(x,y - 1).getRed();
    case 'B':
      return this.picture.get(x,y + 1).getBlue() - this.picture.get(x,y - 1).getBlue();
    case 'G':
      return this.picture.get(x,y + 1).getGreen() - this.picture.get(x,y - 1).getGreen();
    default:
      break;
    }
    return 0;
  }

  public int[] findHorizontalSeam() // sequence of indices for horizontal seam
  {
    if (originalPicture) {
      transpose();
      originalPicture = false;
    }

    return findSeam();

  }

  private void transpose() {
    int height = this.height();
    int width = this.width();
    Color[][] p = new Color[height][width];

    for (int i = 0; i < this.height(); i++) {
      for (int j = 0; j < this.width(); j++)
        p[i][j] = this.picture[j][i];
    }

    this.picture = p;
    this.height = width;
    this.width = height;
  }

  // sequence of indices for vertical seam
  public int[] findVerticalSeam() {
    if (!originalPicture) {
      transpose();
      originalPicture = true;
    }

    return findSeam();

  }

  private int[] findSeam() {
    energyArray = new double[this.width()][this.height()];
    pathArray = new int[this.width()][this.height()];
    /*
     * for (int i = 0; i < this.width(); i++) {
     
      for (int j = 0; j < this.height(); j++)
        energyArray[i][j] = Double.POSITIVE_INFINITY;
    }

    for (int i = 0; i < this.width(); i++) {
      energyArray[i][0] = this.energy(i, 0); // Assigns first row = 1000
    }

    fillEnergyMatrixDynamically();
    int minColumnPosition = findMinScorePosition();
    int[] seam = seamPath(minColumnPosition);
    this.energyArray = null;
    this.pathArray = null;
    return seam;
    */
    /*
     * Filling corners with energy score =1000, this is to make sure the
     * energy of corners is always greater than the internal pixel energy
     */
    for (int i = 0; i < this.height(); i++) {
      energyArray[i][0] = 1000;  //Top row
      energyArray[i][this.width()-1] = 1000; //Bottom Row
    }
    for (int j = 0; j < this.height(); j++) {
      energyArray[0][j] = 1000; //Left Column
      energyArray[this.height()-1][j] = 1000; //Right Column
    }
    for (int i = 1; i < this.height(); i++) {
      for (int j = 1; j < this.height(); j++) {
        energyArray[i][j] = this.energy(i, j);// Fill internal pixels
      }
    }
    
    createTopologicallySortedArray();
  }
  
  private void createTopologicallySortedArray(){
    TopologicalSort topologicalSort = new TopologicalSort(this.energyArray);
    int[] sortedList = topologicalSort.getSortedList();
  }

  private int findMinScorePosition() {
    int pos = 0;
    double minVal = energyArray[0][this.height() - 1];
    for (int i = 1; i < this.width() - 1; i++) {
      if (minVal > energyArray[i][this.height() - 1]) {
        minVal = energyArray[i][this.height() - 1];
        pos = i;
      }
    }
    return pos;
  }

  private int[] seamPath(int pos) {
    int[] seam = new int[this.height()];
    int j = this.height() - 1;

    while (j >= 0) {
      seam[j] = pos;
      pos = pathArray[pos][j];
      j = j - 1;
    }
    return seam;
  }

  private void fillEnergyMatrixDynamically() {
    for (int i = 0; i < this.height() - 1; i++) {
      for (int j = 0; j < this.width(); j++) {
        if (updateScore(j - 1, i + 1, energyArray[j][i]))
          pathArray[j - 1][i + 1] = j;
        if (updateScore(j, i + 1, energyArray[j][i]))
          pathArray[j][i + 1] = j;
        if (updateScore(j + 1, i + 1, energyArray[j][i]))
          pathArray[j + 1][i + 1] = j;
      }
    }
  }

  private boolean updateScore(int i, int j, double score) {
    if (i > -1 && i < this.width()) {
      if (energyArray[i][j] > score + this.energy(i, j)) {
        energyArray[i][j] = score + this.energy(i, j);
        return true;
      }
    }
    return false;
  }

  public void removeHorizontalSeam(int[] seam) // remove horizontal seam from current picture
  {

    if (originalPicture) {
      transpose();
      originalPicture = false;
    }
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

    if (!originalPicture) {
      transpose();
      originalPicture = true;
    }
    removeSeam(seam);
  }

  private void removeSeam(int[] seam) {

    Color[][] p = new Color[this.width() - 1][this.height()];
    for (int j = 0; j < this.height(); j++) {
      for (int i = 0, k = 0; i < this.width(); i++) {
        if (i != seam[j]) {

          p[k][j] = this.picture[i][j];
          k++;
        }
      }
    }
    this.picture = p;
    this.width = this.width() - 1;
  }

  public static void main(String[] args) {
    // SeamCarver s= new SeamCarver();

  }
}