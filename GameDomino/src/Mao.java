import java.util.ArrayList;

public class Mao implements Cloneable{
	
	protected ArrayList<Pedra> pedras = new ArrayList<>();
	protected String player_name;
	protected Pedra most_valuable_pedra;
	
	public Mao(ArrayList<Pedra> pedras) {
		this.pedras = pedras;
		this.most_valuable_pedra = most_valuable_pedra();
	}
	
	public Mao(){}
	
	public Object clone() throws CloneNotSupportedException{
		
		Mao c = new Mao();
		c.player_name = this.player_name;
		c.most_valuable_pedra = this.most_valuable_pedra;
		
		for(Pedra pedra : this.pedras) {
			c.pedras.add(pedra);
		}
		
        return c;
	}
	
	private Pedra most_valuable_pedra() {
		Pedra response = this.pedras.get(0);
		for(Pedra pedra : this.pedras) {
			if(pedra.is_carrilhao()) {
				return pedra;
			}else if (pedra.is_simetric() && !response.is_simetric()){
				response = pedra;
			}else if ((pedra.is_simetric() && response.is_simetric()) || !pedra.is_simetric() && !response.is_simetric()) {
				if(pedra.get_add() > response.get_add()) {
					response = pedra;
				}
			}
		}
		//System.out.println(response);
		return response;
	}
	
	private boolean remove_pedra(Pedra pedra) {
		for (Pedra pedrinha : this.pedras) {
			if(pedrinha.equals(pedra)) {
				this.pedras.remove(pedra);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String toString() {
		String response = "Mão = ";
		for (Pedra pedra : pedras) {
			response += pedra.toString()+ " ";
		}
		return response;
	}
}