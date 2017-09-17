package JanowskiLuoYabut.HackTheNorth.MapMaker;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.widget.ImageView;
import java.util.ArrayList;
/**
 * Class that handles the a* algorithm and finds the shortest path between the given nodes
 * @author Christopher Janowski, Hannah Luo, Adrian Yabut
 * Hack the North 2017
 * Sept 16 2017 - Sept 17 2017
 */
public class Astar {
	
	/**
	 * Returns the total cost of getting from start to node and node to end
	 * @param n0 - Node 
	 * @return int total cost
	 */
	public int getTotalCost(Node n0){
		return n0.sCost + n0.eCost;
	}
	
	/**
	 * Returns the distance between 2 nodes
	 * @param n0 - first node
	 * @param n1 - second node
	 * @return int distance
	 */
	public int getDist(Node n0, Node n1){
		//Scales the value up by 25 and rounds to an integer to save memory

		return (int) (25*Math.sqrt(Math.pow(n0.x - n1.x, 2) + Math.pow(n0.y-n1.y, 2)));
		//return Math.abs(n0.x-n1.x)+Math.abs(n0.y-n1.y);
	}
	
	/**
	 * Gets all the neighbours of a node, diagonal and not
	 * @param n - node 
	 * @param grid - map of all nodes
	 * @param maxX - highest x value in the grid
	 * @param maxY - highest y value in the grid
	 * @return list of neighbours
	 */
	public ArrayList<Node> getNeighbours(Node n, Node[][] grid, int maxX, int maxY){
		ArrayList<Node> neighbours = new ArrayList<Node>();
		//Runs for each neighbour adjacent or diagonal to the node
		for(int y=-1;y<2;y++){
			for(int x=-1;x<2;x++){
				//Checks if the indexes are out of bounds
				if(!(n.x+x<0||n.y+y<0||n.y+y>maxY-1||n.x+x>maxX-1)){
					//Avoids checking the node as a neighbour
					if(!(x==0&&y==0)){
						//If the node is walkable it is added to the list
						if(grid[n.x+x][n.y+y]!= null){
							neighbours.add(grid[n.x+x][n.y+y]);
						}
					}
				}
			}
		}
		
		return neighbours;
	}
	
	/**
	 * Finds the shortest path between two nodes
	 * @param start - node
	 * @param end - node
	 * @param grid - all nodes
	 * @param maxX - limit of grid size in x
	 * @param maxY - limit of grid size in y
	 * @return list of nodes to follow to find shortest path
	 */
	public int getShortestPath(Node start, Node end, Node[][] grid, int maxX, int maxY, ImageView iv, Bitmap bm){
		//List of open nodes to visit next
		ArrayList<Node> open = new ArrayList<Node>();
		//List of closed nodes that have been visited
		ArrayList<Node> closed = new ArrayList<Node>();
		Node curr;
		
		//Set the costs of the starting node
		start.sCost = 0;
		start.eCost = this.getDist(start,end);
		start.tCost = start.eCost;
		
		//Add the starting node to the open set so as to visit it next
		open.add(start);
		
		//Runs until all nodes have been exhausted
		while(!open.isEmpty()){
			
			//smallest distance cost and closest node
			int smallestCost = 999;
			Node bestNode = null;
			
			//Get the best open node to use
			for(Node n0:open){
				//if the node has a smaller cost to travel between the start and end, then consider it instead
				if(this.getTotalCost(n0)<smallestCost){
					smallestCost = this.getTotalCost(n0);
					bestNode = n0;
				}
				//If the two have the same total cost, give preference to the one that has a closer path to the end
				if(this.getTotalCost(n0) == smallestCost){
					if(this.getDist(n0, end) < this.getDist(bestNode, end)){
						bestNode = n0;
					}
				}
			}
			
			//The current node is now the best node in the open set
			curr = bestNode;
			//Add the current node to the visted set
			closed.add(curr);
			//Get all the neighbours
			ArrayList<Node> neighbours = this.getNeighbours(curr, grid, maxX, maxY);
			
			//Find each matching neighbour in neighbours
			for(Node neighbour:neighbours){
				//Make sure the neighbour is not already visited
				boolean in = false;
				for(Node n0:closed){
					if(neighbour.equals(n0)){
						in = true;
					}
				}
				
				//If the neighbour has not been visited
				if(!in){
					//The new distance from the start to the neighbour we reached
					int newSCost = this.getDist(curr, neighbour) + curr.sCost;
					
					//Make sure the node is not already in the open list to visit
					boolean inOp = false;
					for(Node n0:open){
						if(neighbour.equals(n0)){
							inOp = true;
						}
					}
					//if the neighbour is not in the open set we add it in
					if(!inOp){
						open.add(neighbour);
						neighbour.sCost = newSCost;
						neighbour.parent = curr;
						neighbour.eCost = this.getDist(neighbour,end);
						neighbour.tCost = this.getTotalCost(neighbour);
					}
					//if the neighbour is in the open set then check if we found a shorter way to get to it
					else if(newSCost < neighbour.sCost){
						neighbour.sCost = newSCost;
						neighbour.parent = curr;
						neighbour.eCost = this.getDist(neighbour, end);
						neighbour.tCost = this.getTotalCost(neighbour);
					}
				}
			}
			
			//Check if the path has reached the end
			if(curr.equals(end)){
				//Create a list of nodes to return as the path

				//Keeps adding nodes into the path backwards
				while(curr.parent != null){

					for (int j = 25*curr.x - 7; j < 25*curr.x + 7; j++) {
						for (int k = 25 * curr.y - 7; k < 25 * curr.y + 7; k++) {
							bm.setPixel(j, k, Color.rgb(198, 42, 42));
						}
					}
					curr = curr.parent;
				}
				//Adds the last node into the path
				for (int j = 25*curr.x - 7; j < 25*curr.x + 7; j++) {
					for (int k = 25 * curr.y - 7; k < 25 * curr.y + 7; k++) {
						bm.setPixel(j, k, Color.rgb(198, 42, 42));
					}
				}
				return curr.tCost;
				//prints out path to console - TESTING PURPOSES
				//this.outPath(path)
			}
			open.remove(curr);
		}
		
		//If no possible path exists return an empty list
		open.clear();
		closed.clear();
		return 0;
	}
	
	/**
	 * TESTING - Prints each node in the list to console
	 * @param path - to be printed
	 */
	public void outPath(ArrayList<Node> path){
		for(Node n0:path){
			System.out.println(n0.x + " "+n0.y);
		}
	}
	
	/**
	 * Main function for testing
	 * @param args
	 */
	/*
	public static void main(String args[]){
		Node[][] grid = new Node[MAX_X][MAX_Y];
		
		for(int x=0;x < MAX_X;x++){
			for(int y=0;y < MAX_Y;y++){
				grid[x][y] = new Node(x, y, true);
			}
		}
		grid[3][3].walkable = false;
		Astar a0 = new Astar();
		a0.getShortestPath(grid[0][0], grid[249][249], grid);
	}
	*/
}
