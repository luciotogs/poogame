public class Pedra {
	
	public int sideA;
	public int sideB;
	public int edge;
	
	public Pedra(int sideA, int sideB) {
		this.sideA = sideA;
		this.sideB = sideB;
	}
	
	public Pedra() {}
	
	public int get_add() {
		return this.sideA + this.sideB;
	}
	
	public boolean is_simetric() {
		return (this.sideA == this.sideB);
	}
	
	public boolean is_carrilhao() {
		if (is_simetric() && sideA == 6) {
			return true;
		}return false;
	}
	
	public boolean set_edge(int side) {
		this.edge = side;
		return true;
	}
	
	public boolean has_equal_side(Pedra pedra) {
		if(pedra.sideA == this.sideA || pedra.sideB == this.sideB) {
			return true;
		}return false;
	}
	
	@Override
	public String toString() {
		String response = "[" + this.sideA + ":" + this.sideB +"]";
		return response;
		
	}
}
