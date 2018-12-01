public interface Jogador {
	
	public void play_turn(Mesa mesa) throws CloneNotSupportedException;
	public int get_sum();
	public Pedra get_most_valuable();
	public boolean remove_pedra(Pedra pedra);
	public String get_name();
	public void set_name(String name);
	public void show_hand();
	public int get_hand_size();
	public void set_opponent_hand_amount(int hand_amount);
	public int get_opponent_hand_amount();
	
}