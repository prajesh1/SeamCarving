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
    
    for(int i=0;i<list.length;i++){
    
      if(!marked[i])
        DFS(i);
    } 
    
    computeShortestPath();
   }
  private void DFS(int i){

    this.marked[i]=true;
    int row = i/energyArray.length;
    int col = i%energyArray.length;
    if(row<energyArray[0].length-1){
      
      int bottomLeft = (row+1)*energyArray.length + col-1 ;
      int bottom = (row+1)*energyArray.length + col;
     // System.out.println("Row="+row+"Bottom is +"+bottom+" and i is "+i+"a nd"+energyArray.length);
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
    this.distances = new double[this.energyArray.length*this.energyArray[0].length];
    Arrays.fill(distances, Double.POSITIVE_INFINITY);
    //No need to fill source with 0. As we will traverse later to find who is the real source
    this.parent = new int[this.energyArray.length*this.energyArray[0].length];
    for(int i=0;i<this.energyArray[0].length;i++){
      this.distances[i] = 1000.0d;
      this.parent[i] = -1; // Top Row points to single artificial node created
    }
    for(int x:this.list){
  
      if(x>=this.energyArray[0].length){
        relax(x);
      }
      
    }
  }
  private void relax(int x){
    int row = x/energyArray[0].length;
    int col = x%energyArray[0].length;
    int mul = energyArray[0].length;
    
    double min = this.distances[x];          
  
    double current = this.energyArray[row][col];
    if(min>this.distances[(row-1)*mul+col]+current){  //checking top
      min = this.distances[(row-1)*mul+col]+current;
      this.parent[x] = (row-1)*this.energyArray[0].length+ col;
    }
    
    if(col>0&&min>this.distances[(row-1)*mul+col-1]+current){ //top left
      min = this.distances[(row-1)*mul+col-1]+current;
      this.parent[x] = (row-1)*this.energyArray[0].length+ col-1;
    }
    if((col<this.energyArray.length-1)&&min>this.distances[(row-1)*mul+col+1]+current){ //rop right
      min = this.distances[(row-1)*mul+col+1]+current;
      this.parent[x] = (row-1)*this.energyArray[0].length+ col+1;
    }
    this.distances[x]= min;
  }
  
  public int[] getShortestPath(){
    double bottomRowMin = Double.POSITIVE_INFINITY;
    int bottomRowIndex = 0;
    
    for(int i= this.energyArray[0].length*(this.energyArray.length-1);i<this.energyArray[0].length*this.energyArray.length;i++){
    
      if(bottomRowMin > this.distances[i]){
        bottomRowMin = this.distances[i];
        bottomRowIndex = i;
      }
    }
    
    int[] ll = new int[this.energyArray.length];
    int n = ll.length;
    int parent = bottomRowIndex;
    while(parent!=-1)
    {
     ll[--n] = parent%this.energyArray[0].length;
      //ll[--n] = parent;
     
      parent = this.parent[parent];
    }
    return ll;
  }
  public static void main(String[] args) {
    // TODO Auto-generated method stub

  }

}
