import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TopologicalSort {
  int[] list;
  boolean[] marked;
  int N;
  double[][] energyArray;
  double[] distances;
  int[] parent;
  public TopologicalSort(double[][] energyArray){
    N = energyArray.length*energyArray[0].length;
    this.energyArray = energyArray;
    this.list = new int[N];
    this.marked = new boolean[N];
    
    for(int i=0;i<N;i++){
      if(!marked[i])
        DFS(i);
    } 
   }
  private void DFS(int i){
    int row = i/energyArray.length;
    int col = i%energyArray.length;
    if(row<energyArray.length-1){
      
      int bottomLeft = (row+1)*energyArray.length + col-1 ;
      int bottom = (row+1)*energyArray.length + col;
      int bottomRight = (row+1)*energyArray.length + col+1;
      
      if(col>0&&!marked[bottomLeft]) //Bottom Left
        DFS(bottomLeft);
      if(!marked[bottom])
        DFS(bottom); //Bottom
      if(col<this.energyArray.length-1&&!marked[bottomRight]) //Bottom Right
        DFS(bottomRight);
    }
    
    list[--N] = i; // Reverse Post Order
  }
  public int[] getSortedList(){
    return list;
  }
  
  public void computeShortestPath(){
    this.distances = new double[this.energyArray.length];
    Arrays.fill(distances, Double.POSITIVE_INFINITY);
    //No need to fill source with 0. As we will traverse later to find who is the real source
    this.parent = new int[this.energyArray.length];
    
    for(int x:this.list){
      if(x<this.energyArray.length){
        this.distances[x] = 1000.0d;
        this.parent[x] = -1; // Top Row points to single artificial node created
      }
      else relax(x);
    }
  }
  private void relax(int x){
    int row = x/energyArray.length;
    int col = x%energyArray.length;
    
    double min = this.distances[x];          
    
    if(min<this.energyArray[row-1][col]){  //checking top
      min = this.energyArray[row-1][col];
      this.parent[x] = (row-1)*this.energyArray.length+ col;
    }
    
    if(col>0&&min<this.energyArray[row-1][col-1]){ //top left
      min = this.energyArray[row-1][col-1];
      this.parent[x] = (row-1)*this.energyArray.length+ col-1;
    }
    if((col<this.energyArray.length-1)&&min<this.energyArray[row-1][col+1]){ //rop right
      min = this.energyArray[row-1][col+1];
      this.parent[x] = (row-1)*this.energyArray.length+ col+1;
    }
    
  }
  
  public List<Integer> getShortestPath(){
    double bottomRowMin = Double.POSITIVE_INFINITY;
    int bottomRowIndex = 0;
    for(int i= this.energyArray.length*(this.energyArray.length-1);i<this.energyArray.length*this.energyArray.length;i++){
      if(bottomRowMin < this.distances[i]){
        bottomRowMin = this.distances[i];
        bottomRowIndex = i;
      }
    }
    List<Integer> x = new ArrayList();
    int parent = bottomRowIndex;
    while(parent!=-1)
    {
      x.add(bottomRowIndex);
      parent = this.parent[bottomRowIndex];
    }
    
    return x;
  }
  public static void main(String[] args) {
    // TODO Auto-generated method stub

  }

}
