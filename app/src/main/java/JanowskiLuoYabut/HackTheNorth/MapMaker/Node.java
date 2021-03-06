package JanowskiLuoYabut.HackTheNorth.MapMaker;
//Class to hold information for all nodes in the map
public class Node {

	public int x;
	public int y;
	public int sCost;
	public int eCost;
	public int tCost;
	public Node parent;
	
	public Node(int x0, int y0){
		this.x = x0;
		this.y = y0;
		this.sCost = 0;
		this.eCost = 0;
		this.tCost = 0;
		this.parent = null;
	}
	
	public boolean isEqual(Node nodeA){
		if(this.x == nodeA.x && this.y ==nodeA.y){
			return true;
		}
		return false;
	}
}
