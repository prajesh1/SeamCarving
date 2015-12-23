package main;

import edu.princeton.cs.algs4.Picture;

public class SeamCarver { 
  private Picture picture;
  private double score;
  private int[] seam;
  private double[][] energyArray;
  private boolean originalPicture = true;
  
  //create a seam carver object based on the given picture
  public SeamCarver(Picture picture) {
    this.picture = picture;
  }
  
  //current picture
  public Picture picture() {
    return this.picture;   
  }
  
  // width of current picture
  public int width() {
    return this.picture.width();
  }
  
  //height of current picture
  public     int height() {
    return this.picture.height();
  }
  
  //energy of pixel at column x and row y
  public  double energy(int x, int y) {
    if(x == 0 || y == 0 || x == this.picture.width()-1 || y == this.picture.height()-1)
      return 1000;
    double xGradient = Math.pow(centralXDifference(x,y,'R'),2) 
                   + Math.pow(centralXDifference(x,y,'G'),2)
                   + Math.pow(centralXDifference(x,y,'B'),2);
    double yGradient = Math.pow(centralYDifference(x,y,'R'),2) 
           + Math.pow(centralYDifference(x,y,'G'),2)
           + Math.pow(centralYDifference(x,y,'B'),2);
  
    return Math.sqrt(xGradient+yGradient);    
  }
    private double centralXDifference(int x, int y, char color)
    {
        switch(color){
        case 'R':
            return this.picture.get(x+1, y).getRed() -this.picture.get(x-1, y).getRed();
        case 'B':
            return this.picture.get(x+1, y).getBlue() -this.picture.get(x-1, y).getBlue();
        case 'G':
            return this.picture.get(x+1, y).getGreen() -this.picture.get(x-1, y).getGreen();
        }
        return 0;
    }
    private double centralYDifference(int x, int y, char color)
        {
            switch(color){
            case 'R':
                return this.picture.get(x, y+1).getRed() -this.picture.get(x, y-1).getRed();
            case 'B':
                return this.picture.get(x, y+1).getBlue() -this.picture.get(x, y-1).getBlue();
            case 'G':
                return this.picture.get(x, y+1).getGreen() -this.picture.get(x, y-1).getGreen();
            }
            return 0;
        }
    public   int[] findHorizontalSeam()               // sequence of indices for horizontal seam
    {
      if(originalPicture)  
        {
        transpose();originalPicture = false;
        }
      
        seam = this.findVerticalSeam();
        
   
        return seam;
    }
    
    private void transpose()
    {
        Picture p = new Picture(this.picture.height(), this.picture.width());
        
        for(int i=0;i<this.picture.height();i++)
            for(int j=0;j<this.picture.width();j++)
                p.set(i, j, this.picture.get(j, i));
        
        this.picture = p;
    }
    

    public   int[] findVerticalSeam()                 // sequence of indices for vertical seam
    {
      if(!originalPicture)  
      {
      transpose();originalPicture = true;
      }
        energyArray = new double[this.width()][this.height()];
        for(int i=0;i<this.width();i++)
        {
            for(int j=0;j<this.height();j++)
                energyArray[i][j] = this.energy(i,j);
        }
        
        this.score = 1000*this.picture.height();
        this.seam = new int[this.height()];
        int[] seam = new int[this.height()];
        for(int i=0;i<this.width();i++)
        {
            DFS(i,0,0,seam);            
        }
        
        return this.seam;
        
    }

    
    private void DFS(int x, int y, double score, int[] seam)
    {
        score = score + energyArray[x][y];
        seam[y] =x;
        
        if(score < this.score)
        {
            if(y == this.height()-1)
                {
                    this.seam = seam.clone();
                    this.score = score;
                    return;
                }
           
        
        if(x-1>=0) DFS(x-1,y+1,score,seam);
        DFS(x,y+1,score,seam);
        if(x+1<this.width()) DFS(x+1,y+1,score,seam);
        }
    }
    public    void removeHorizontalSeam(int[] seam)   // remove horizontal seam from current picture
    {
      if(originalPicture)  
      {
      transpose();originalPicture = false;
      }
    
       
    }
     public    void removeVerticalSeam(int[] seam)     // remove vertical seam from current picture
     {
       if(!originalPicture)  
       {
       transpose();originalPicture = true;
       }
         Picture p = new Picture(this.picture.width()-1,this.picture.height());
         for(int j=0;j<this.picture.height();j++)
         {
             for(int i=0,k=0;i<this.picture.width();i++)
                { 
                 if(i!=seam[j])
                 {
                    
                     p.set(k, j, this.picture.get(i, j)); k++;
                     
                 }
                }
         }
         this.picture = p;
     }
     public static void main(String[] args)
       {
        //  SeamCarver s= new SeamCarver(); 
          
       }
}