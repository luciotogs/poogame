import java.util.ArrayList;

public class Mesa implements Cloneable{
	protected int edgeA;
	protected int edgeB;
	protected ArrayList<Pedra> monte = new ArrayList<>();
	protected ArrayList<Pedra> currentGame = new ArrayList<>();
	
	public Mesa() {}
	
	public Object clone() throws CloneNotSupportedException{
		Mesa c = new Mesa();
		c.edgeA = this.edgeA;
		c.edgeB = this.edgeB;
        
		if(!this.monte.isEmpty()) {
			for (Pedra pedra : this.monte) {
				c.monte.add(pedra);
			}
		}
		
		if(!this.currentGame.isEmpty()) {
			for (Pedra pedra : this.currentGame) {
				c.currentGame.add(pedra);
			}
		}
		
		return c;
   }
	
	public boolean set_edgeA(int edgeA) {
		this.edgeA = edgeA;
		return true;
	}
	
	public boolean set_edgeB(int edgeB) {
		this.edgeB = edgeB;
		return true;
	}
	
	public boolean set_monte(ArrayList<Pedra> monte) {
		this.monte = monte;
		return true;
	}
	
	public int get_edgeA() {
		return this.edgeA;
	}
	
	public int get_edgeB() {
		return this.edgeB;
	}
	
	public ArrayList<Pedra> get_monte(){
		return this.monte;
	}
}