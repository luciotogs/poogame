import java.util.ArrayList;
import java.util.Collections;

public class JogadorMCTS implements Jogador{
	public Mao mao;
	public String name = "Monte Carlo Tree Search Player";
	public int opponent_hand_amount;
	
	public 	ArrayList<Node> pedras_selected = new ArrayList<>();
	
	public JogadorMCTS(Mao mao) {
		this.mao = mao;
	}
	
	@Override
	public int get_sum() {
		return mao.most_valuable_pedra.get_add();
	}
	
	@Override
	public void play_turn(Mesa mesa) throws CloneNotSupportedException{
		this.pedras_selected.clear();
		select(mesa);
		if(this.pedras_selected.isEmpty()) {
			buy(mesa);
		}else {
			choose_and_play(mesa);
		}
	}
	
	public void buy(Mesa mesa) {
		Collections.shuffle(mesa.monte);
		this.mao.pedras.add(mesa.monte.get(0));
		mesa.monte.remove(mesa.monte.get(0));
	}
	
	public void choose_and_play(Mesa mesa) {
		Node tmp_node = this.pedras_selected.get(0);
		for (int i = 0;i < this.pedras_selected.size();i++) {
			if(this.pedras_selected.get(i).wins > tmp_node.wins) {
				tmp_node = this.pedras_selected.get(i);
			}else if(this.pedras_selected.get(i).wins == tmp_node.wins) {
				if(this.pedras_selected.get(i).draws > tmp_node.draws) {
					tmp_node = this.pedras_selected.get(i);
				}
			}
		}
		do_changes(mesa,tmp_node);
	}
	
	public void do_changes(Mesa mesa,Node node) {
		Pedra pedra = node.pedra;
		
		if(pedra.sideA == mesa.edgeA) {
			mesa.edgeA = pedra.sideB;
			mesa.currentGame.add(0,pedra);
			this.mao.pedras.remove(pedra);
			return;
		}
		
		if(pedra.sideB == mesa.edgeA) {
			mesa.edgeA = pedra.sideA;
			mesa.currentGame.add(0,pedra);
			this.mao.pedras.remove(pedra);
			return;
		}
		
		if(pedra.sideA == mesa.edgeB) {
			mesa.edgeB = pedra.sideB;
			mesa.currentGame.add(pedra);
			this.mao.pedras.remove(pedra);
			return;
		}
		
		if(pedra.sideB == mesa.edgeB) {
			mesa.edgeB = pedra.sideA;
			mesa.currentGame.add(pedra);
			this.mao.pedras.remove(pedra);
			return;
		}
	}
	
	public void select(Mesa mesa) throws CloneNotSupportedException {
		boolean buy = true;
		
		ArrayList<Pedra> hand = new ArrayList<>();
		for(Pedra pedra : this.mao.pedras) {
			hand.add(pedra);
		}
		
		Mesa tmp_mesa;
		Mao tmp_mao;
		
		for(Pedra pedra : hand) {
			if(pedra.sideA == mesa.edgeA || pedra.sideB == mesa.edgeA) {
				if(pedra.sideA == mesa.edgeA) {
					tmp_mesa = (Mesa) mesa.clone();
					tmp_mao = (Mao) this.mao.clone();
					pedra.edge = pedra.sideB;
					tmp_mesa.edgeA = pedra.edge;
					tmp_mesa.currentGame.add(0,pedra);
					tmp_mao.pedras.remove(pedra);
					
					Node node = new Node("me", false, tmp_mesa, tmp_mao, this.opponent_hand_amount);
					node.pedra = pedra;
					node.in();
					pedras_selected.add(node);
				}
				
				if(pedra.sideB == mesa.edgeA) {
					tmp_mesa = (Mesa) mesa.clone();
					tmp_mao = (Mao) this.mao.clone();
					pedra.edge = pedra.sideA;
					tmp_mesa.edgeA = pedra.edge;
					tmp_mesa.currentGame.add(0,pedra);
					tmp_mao.pedras.remove(pedra);
					
					Node node = new Node("me", false, tmp_mesa, tmp_mao, this.opponent_hand_amount);
					node.pedra = pedra;
					node.in();
					pedras_selected.add(node);
				}
			}
			
			if (pedra.sideA == mesa.edgeB || pedra.sideB == mesa.edgeB) {
				if(pedra.sideA == mesa.edgeB) {
					tmp_mesa = (Mesa) mesa.clone();
					tmp_mao = (Mao) this.mao.clone();
					pedra.edge = pedra.sideA;
					tmp_mesa.edgeB = pedra.edge;
					tmp_mesa.currentGame.add(pedra);
					tmp_mao.pedras.remove(pedra);
					
					Node node = new Node("me", false, tmp_mesa, tmp_mao, this.opponent_hand_amount);
					node.pedra = pedra;
					node.in();
					pedras_selected.add(node);
				}
				
				if(pedra.sideB == mesa.edgeB) {
					tmp_mesa = (Mesa) mesa.clone();
					tmp_mao = (Mao) this.mao.clone();
					pedra.edge = pedra.sideB;
					tmp_mesa.edgeB = pedra.edge;
					tmp_mesa.currentGame.add(pedra);
					tmp_mao.pedras.remove(pedra);
					
					Node node = new Node("me", false, tmp_mesa, tmp_mao, this.opponent_hand_amount);
					node.pedra = pedra;
					node.in();
					pedras_selected.add(node);
				}
			}	
		}
		
	}

	@Override
	public Pedra get_most_valuable() {
		return mao.most_valuable_pedra;
	}

	@Override
	public boolean remove_pedra(Pedra pedra) {
		return (this.mao.pedras.remove(pedra));
	}
	
	@Override
	public String get_name() {
		return this.name;
	}
	
	@Override
	public void set_name(String name) {
		this.name = name;
	}
	
	@Override
	public void show_hand() {
		System.out.println(mao.toString());
	}
	
	@Override
	public int get_hand_size() {
		return this.mao.pedras.size();
	}

	@Override
	public void set_opponent_hand_amount(int hand_amount) {
		this.opponent_hand_amount = hand_amount;
	}

	@Override
	public int get_opponent_hand_amount() {
		return this.opponent_hand_amount;
	}
}

class Node{
	
	protected Mesa mesa_state;
	protected Mao mao_state;
	
	protected Mao opponent_mao;
	
	protected Pedra pedra;
	
	protected ArrayList<Pedra> game_pedras = new ArrayList<>();
	protected ArrayList<Pedra> game_buy = new ArrayList<>();
	protected ArrayList<Node> childrens = new ArrayList<>();
	
	protected int hand_amount = 0;
	protected int opponent_hand_amount = 0;
	protected int wins = 0;
	protected int losses = 0;
	protected int draws = 0;
	protected int deep = 0;
	
	protected String parent;
	protected boolean won;
	protected boolean opponent_hand_setted = false;
	
	Node(String parent, boolean won, Mesa mesa_state, Mao mao_state,int opponent_hand_amount) throws CloneNotSupportedException {
		this.parent = parent;
		this.won = won;
		this.mesa_state = mesa_state;
		this.mao_state = mao_state;
		this.hand_amount = mao_state.pedras.size();
		this.opponent_hand_amount = opponent_hand_amount;

	}
	
	public void in() throws CloneNotSupportedException {
		init_game(this.game_pedras);
		filter(this.game_pedras);
		init();
	}
	
	public void init() throws CloneNotSupportedException {
		
		if(this.mesa_state.monte.isEmpty()) {
			if(this.hand_amount == this.opponent_hand_amount) {
				this.draws++;
			}else if(this.hand_amount < this.opponent_hand_amount) {
				this.wins++;
			}else {
				this.losses++;
			}
			return;
		}
		
		if(this.hand_amount == 0) {
			if (this.parent.equals("me")){
				this.wins++;
			}else {
				this.draws++;
			}
			return;
		}else {
			if(this.opponent_mao != null) {
			}
			if (this.won) {
				this.losses++;
				return;
			}else {
				if (this.parent.equals("me")) {
					expand();
				}else {
					boolean buy = true;
					for (Pedra pedra : this.mao_state.pedras) {
						if(pedra.sideA == this.mesa_state.edgeA || pedra.sideB == this.mesa_state.edgeA) {
							if(pedra.sideA == this.mesa_state.edgeA) {
								Mesa tmp_mesa = (Mesa) this.mesa_state.clone();
								Mao tmp_mao = (Mao) this.mao_state.clone();
								pedra.edge = pedra.sideB;
								tmp_mesa.edgeA = pedra.edge;
								tmp_mesa.currentGame.add(0,pedra);
								tmp_mao.pedras.remove(pedra);
								if(tmp_mao.pedras.isEmpty()) {
									Node node = new Node("me",true,tmp_mesa,tmp_mao,this.opponent_mao.pedras.size());
									node.opponent_hand_setted = true;
									node.opponent_mao = this.opponent_mao;
									node.deep = deep++;
									node.in();
									count(node);
									this.childrens.add(node);
								}else {
									Node node = new Node("me",false,tmp_mesa,tmp_mao,this.opponent_mao.pedras.size());
									node.opponent_hand_setted = true;
									node.opponent_mao = this.opponent_mao;
									node.deep = deep++;
									node.in();
									count(node);
									this.childrens.add(node);
								}
								
								buy = false;
							}
							
							if(pedra.sideB == this.mesa_state.edgeA) {
								Mesa tmp_mesa = (Mesa) this.mesa_state.clone();
								Mao tmp_mao = (Mao) this.mao_state.clone();
								pedra.edge = pedra.sideA;
								tmp_mesa.edgeA = pedra.edge;
								tmp_mesa.currentGame.add(0,pedra);
								tmp_mao.pedras.remove(pedra);
								if(tmp_mao.pedras.isEmpty()) {
									Node node = new Node("me",true,tmp_mesa,tmp_mao,this.opponent_mao.pedras.size());
									node.opponent_hand_setted = true;
									node.opponent_mao = this.opponent_mao;
									node.deep = deep++;
									node.in();
									count(node);
									this.childrens.add(node);
								}else {
									Node node = new Node("me",false,tmp_mesa,tmp_mao,this.opponent_mao.pedras.size());
									node.opponent_hand_setted = true;
									node.opponent_mao = this.opponent_mao;
									node.deep = deep++;
									node.in();
									count(node);
									this.childrens.add(node);
								}
								
								buy = false;
							}
						}
						
						if(pedra.sideA == this.mesa_state.edgeB || pedra.sideB == this.mesa_state.edgeB) {
							if(pedra.sideA == this.mesa_state.edgeB) {
								Mesa tmp_mesa = (Mesa) this.mesa_state.clone();
								Mao tmp_mao = (Mao) this.mao_state.clone();
								pedra.edge = pedra.sideB;
								tmp_mesa.edgeB = pedra.edge;
								tmp_mesa.currentGame.add(pedra);
								tmp_mao.pedras.remove(pedra);
								if(tmp_mao.pedras.isEmpty()) {
									Node node = new Node("me",true,tmp_mesa,tmp_mao,this.opponent_mao.pedras.size());
									node.opponent_hand_setted = true;
									node.opponent_mao = this.opponent_mao;
									node.deep = deep++;
									node.in();
									count(node);
									this.childrens.add(node);
								}else {
									Node node = new Node("me",false,tmp_mesa,tmp_mao,this.opponent_mao.pedras.size());
									node.opponent_hand_setted = true;
									node.opponent_mao = this.opponent_mao;
									node.deep = deep++;
									node.in();
									count(node);
									this.childrens.add(node);
								}
								
								buy = false;
							}
							
							if(pedra.sideB == this.mesa_state.edgeB) {
								Mesa tmp_mesa = (Mesa) this.mesa_state.clone();
								Mao tmp_mao = (Mao) this.mao_state.clone();
								pedra.edge = pedra.sideA;
								tmp_mesa.edgeB = pedra.edge;
								tmp_mesa.currentGame.add(pedra);
								tmp_mao.pedras.remove(pedra);
								if(tmp_mao.pedras.isEmpty()) {
									Node node = new Node("me",true,tmp_mesa,tmp_mao,this.opponent_mao.pedras.size());
									node.opponent_hand_setted = true;
									node.opponent_mao = this.opponent_mao;
									node.deep = deep++;
									node.in();
									count(node);
									this.childrens.add(node);
								}else {
									Node node = new Node("me",false,tmp_mesa,tmp_mao,this.opponent_mao.pedras.size());
									node.opponent_hand_setted = true;
									node.opponent_mao = this.opponent_mao;
									node.deep = deep++;
									node.in();
									count(node);
									this.childrens.add(node);
								}
								
								buy = false;
							}
						}
					}
					
					if(buy) {
						buy_pedra();
					}
				}
			}
		}
		
	}
	
	public void buy_pedra() {
		Collections.shuffle(this.game_pedras);
		this.mao_state.pedras.add(this.game_pedras.get(0));
		this.game_pedras.remove(0);
	}
	
	public void expand() throws CloneNotSupportedException {
		if(!this.opponent_hand_setted) {
			for (int r = 0;r<5;r++) {
				generate_opponent_hand();
				
				for (Pedra pedra : this.opponent_mao.pedras) {
					if(pedra.sideA == this.mesa_state.edgeA || pedra.sideB == this.mesa_state.edgeA) {
						if(pedra.sideA == this.mesa_state.edgeA) {
							Mesa tmp_mesa = (Mesa) this.mesa_state.clone();
							Mao tmp_opponent_hand = (Mao) this.opponent_mao.clone();
							pedra.edge = pedra.sideB;
							tmp_mesa.edgeA = pedra.edge;
							tmp_mesa.currentGame.add(0,pedra);
							tmp_opponent_hand.pedras.remove(pedra);
							if(tmp_opponent_hand.pedras.isEmpty()) {
								Node node = new Node("opponent",true,tmp_mesa,this.mao_state,tmp_opponent_hand.pedras.size());
								node.opponent_hand_setted = true;
								node.opponent_mao = tmp_opponent_hand;
								node.deep = deep++;
								node.in();
								count(node);
								this.childrens.add(node);
							}else {
								Node node = new Node("opponent",false,tmp_mesa,this.mao_state,tmp_opponent_hand.pedras.size());
								node.opponent_hand_setted = true;
								node.opponent_mao = tmp_opponent_hand;
								node.deep = deep++;
								node.in();
								count(node);
								this.childrens.add(node);
							}
						}
						
						if(pedra.sideB == this.mesa_state.edgeA) {
							Mesa tmp_mesa = (Mesa) this.mesa_state.clone();
							Mao tmp_opponent_hand = (Mao) this.opponent_mao.clone();
							pedra.edge = pedra.sideA;
							tmp_mesa.edgeA = pedra.edge;
							tmp_mesa.currentGame.add(0,pedra);
							tmp_opponent_hand.pedras.remove(pedra);
							if(tmp_opponent_hand.pedras.isEmpty()) {
								Node node = new Node("opponent",true,tmp_mesa,this.mao_state,tmp_opponent_hand.pedras.size());
								node.opponent_hand_setted = true;
								node.opponent_mao = tmp_opponent_hand;
								node.deep = deep++;
								node.in();
								count(node);
								this.childrens.add(node);
							}else {
								Node node = new Node("opponent",false,tmp_mesa,this.mao_state,tmp_opponent_hand.pedras.size());
								node.opponent_hand_setted = true;
								node.opponent_mao = tmp_opponent_hand;
								node.deep = deep++;
								node.in();
								count(node);
								this.childrens.add(node);
							}
						}
					}
					
						if(pedra.sideA == this.mesa_state.edgeB || pedra.sideB == this.mesa_state.edgeB) {
							if(pedra.sideA == this.mesa_state.edgeB) {
								Mesa tmp_mesa = (Mesa) this.mesa_state.clone();
								Mao tmp_opponent_hand = (Mao) this.opponent_mao.clone();
								pedra.edge = pedra.sideB;
								tmp_mesa.edgeB = pedra.edge;
								tmp_mesa.currentGame.add(pedra);
								tmp_opponent_hand.pedras.remove(pedra);
								if(tmp_opponent_hand.pedras.isEmpty()) {
									Node node = new Node("opponent",true,tmp_mesa,this.mao_state,tmp_opponent_hand.pedras.size());
									node.opponent_hand_setted = true;
									node.opponent_mao = tmp_opponent_hand;
									node.deep = deep++;
									node.in();
									count(node);
									this.childrens.add(node);
								}else {
									Node node = new Node("opponent",false,tmp_mesa,this.mao_state,tmp_opponent_hand.pedras.size());
									node.opponent_hand_setted = true;
									node.opponent_mao = tmp_opponent_hand;
									node.deep = deep++;
									node.in();
									count(node);
									this.childrens.add(node);
								}
							}
							
							if(pedra.sideB == this.mesa_state.edgeB) {
								Mesa tmp_mesa = (Mesa) this.mesa_state.clone();
								Mao tmp_opponent_hand = (Mao) this.opponent_mao.clone();
								pedra.edge = pedra.sideA;
								tmp_mesa.edgeB = pedra.edge;
								tmp_mesa.currentGame.add(pedra);
								tmp_opponent_hand.pedras.remove(pedra);
								if(tmp_opponent_hand.pedras.isEmpty()) {
									Node node = new Node("opponent",true,tmp_mesa,this.mao_state,tmp_opponent_hand.pedras.size());
									node.opponent_hand_setted = true;
									node.opponent_mao = tmp_opponent_hand;
									node.deep = deep++;
									node.in();
									count(node);
									this.childrens.add(node);
								}else {
									Node node = new Node("opponent",false,tmp_mesa,this.mao_state,tmp_opponent_hand.pedras.size());
									node.opponent_hand_setted = true;
									node.opponent_mao = tmp_opponent_hand;
									node.deep = deep++;
									node.in();
									count(node);
									this.childrens.add(node);
								}
							}
						}
				}
			}
		}else {
			
			for (Pedra pedra : this.opponent_mao.pedras) {
				if(pedra.sideA == this.mesa_state.edgeA || pedra.sideB == this.mesa_state.edgeA) {
					if(pedra.sideA == this.mesa_state.edgeA) {
						Mesa tmp_mesa = (Mesa) this.mesa_state.clone();
						Mao tmp_opponent_hand = (Mao) this.opponent_mao.clone();
						pedra.edge = pedra.sideB;
						tmp_mesa.edgeA = pedra.edge;
						tmp_mesa.currentGame.add(0,pedra);
						tmp_opponent_hand.pedras.remove(pedra);
						if(tmp_opponent_hand.pedras.isEmpty()) {
							Node node = new Node("opponent",true,tmp_mesa,this.mao_state,tmp_opponent_hand.pedras.size());
							node.opponent_hand_setted = true;
							node.opponent_mao = tmp_opponent_hand;
							node.deep = deep++;
							node.in();
							count(node);
							this.childrens.add(node);
						}else {
							Node node = new Node("opponent",false,tmp_mesa,this.mao_state,tmp_opponent_hand.pedras.size());
							node.opponent_hand_setted = true;
							node.opponent_mao = tmp_opponent_hand;
							node.deep = deep++;
							node.in();
							count(node);
							this.childrens.add(node);
						}
					}
					
					if(pedra.sideB == this.mesa_state.edgeA) {
						Mesa tmp_mesa = (Mesa) this.mesa_state.clone();
						Mao tmp_opponent_hand = (Mao) this.opponent_mao.clone();
						pedra.edge = pedra.sideA;
						tmp_mesa.edgeA = pedra.edge;
						tmp_mesa.currentGame.add(0,pedra);
						tmp_opponent_hand.pedras.remove(pedra);
						if(tmp_opponent_hand.pedras.isEmpty()) {
							Node node = new Node("opponent",true,tmp_mesa,this.mao_state,tmp_opponent_hand.pedras.size());
							node.opponent_hand_setted = true;
							node.opponent_mao = tmp_opponent_hand;
							node.deep = deep++;
							node.in();
							count(node);
							this.childrens.add(node);
						}else {
							Node node = new Node("opponent",false,tmp_mesa,this.mao_state,tmp_opponent_hand.pedras.size());
							node.opponent_hand_setted = true;
							node.opponent_mao = tmp_opponent_hand;
							node.deep = deep++;
							node.in();
							count(node);
							this.childrens.add(node);
						}
					}
				}
				
				if(pedra.sideA == this.mesa_state.edgeB || pedra.sideB == this.mesa_state.edgeB) {
					if(pedra.sideA == this.mesa_state.edgeB) {
						Mesa tmp_mesa = (Mesa) this.mesa_state.clone();
						Mao tmp_opponent_hand = (Mao) this.opponent_mao.clone();
						pedra.edge = pedra.sideB;
						tmp_mesa.edgeB = pedra.edge;
						tmp_mesa.currentGame.add(pedra);
						tmp_opponent_hand.pedras.remove(pedra);
						if(tmp_opponent_hand.pedras.isEmpty()) {
							Node node = new Node("opponent",true,tmp_mesa,this.mao_state,tmp_opponent_hand.pedras.size());
							node.opponent_hand_setted = true;
							node.opponent_mao = tmp_opponent_hand;
							node.deep = deep++;
							node.in();
							count(node);
							this.childrens.add(node);
						}else {
							Node node = new Node("opponent",false,tmp_mesa,this.mao_state,tmp_opponent_hand.pedras.size());
							node.opponent_hand_setted = true;
							node.opponent_mao = tmp_opponent_hand;
							node.deep = deep++;
							node.in();
							count(node);
							this.childrens.add(node);
						}
					}
					
					if(pedra.sideA == this.mesa_state.edgeB) {
						Mesa tmp_mesa = (Mesa) this.mesa_state.clone();
						Mao tmp_opponent_hand = (Mao) this.opponent_mao.clone();
						pedra.edge = pedra.sideA;
						tmp_mesa.edgeB = pedra.edge;
						tmp_mesa.currentGame.add(pedra);
						tmp_opponent_hand.pedras.remove(pedra);
						if(tmp_opponent_hand.pedras.isEmpty()) {
							Node node = new Node("opponent",true,tmp_mesa,this.mao_state,tmp_opponent_hand.pedras.size());
							node.opponent_hand_setted = true;
							node.opponent_mao = tmp_opponent_hand;
							node.deep = deep++;
							node.in();
							count(node);
							this.childrens.add(node);
						}else {
							Node node = new Node("opponent",false,tmp_mesa,this.mao_state,tmp_opponent_hand.pedras.size());
							node.opponent_hand_setted = true;
							node.opponent_mao = tmp_opponent_hand;
							node.deep = deep++;
							node.in();
							count(node);
							this.childrens.add(node);
						}
					}
				}
			}
		}
	}
	
	public void count(Node node) {
		this.draws += node.draws;
		this.losses += node.losses;
		this.wins += node.wins;
	}
		
	public void generate_opponent_hand() {
		Collections.shuffle(this.game_pedras);
		this.opponent_mao = new Mao();
		for (int i = 0;i < this.opponent_hand_amount; i++) {
			this.opponent_mao.pedras.add(this.game_pedras.get(i));
		}
		return;
	}
	
	public void filter (ArrayList<Pedra> pedras) {
		for (Pedra ped : this.mao_state.pedras) {
			pedras.remove(ped);
		}
		
		for (Pedra ped : this.mesa_state.monte) {
			pedras.remove(ped);
		}
	}
	private static ArrayList<Pedra> init_game(ArrayList<Pedra> pedras){
		Pedra pedra = new Pedra(0,0);
		pedras.add(pedra);
		Pedra pedra1 = new Pedra(0,1);
		pedras.add(pedra1);
		Pedra pedra2 = new Pedra(0,2);
		pedras.add(pedra2);
		Pedra pedra3 = new Pedra(0,3);
		pedras.add(pedra3);
		Pedra pedra4 = new Pedra(0,4);
		pedras.add(pedra4);
		Pedra pedra5 = new Pedra(0,5);
		pedras.add(pedra5);
		Pedra pedra6 = new Pedra(0,6);
		pedras.add(pedra6);
		Pedra pedra7 = new Pedra(1,1);
		pedras.add(pedra7);
		Pedra pedra8 = new Pedra(1,2);
		pedras.add(pedra8);
		Pedra pedra9 = new Pedra(1,3);
		pedras.add(pedra9);
		Pedra pedra10 = new Pedra(1,4);
		pedras.add(pedra10);
		Pedra pedra11 = new Pedra(1,5);
		pedras.add(pedra11);
		Pedra pedra12 = new Pedra(1,6);
		pedras.add(pedra12);
		Pedra pedra13 = new Pedra(2,3);
		pedras.add(pedra13);
		Pedra pedra14 = new Pedra(2,4);
		pedras.add(pedra14);
		Pedra pedra15 = new Pedra(2,5);
		pedras.add(pedra15);
		Pedra pedra16 = new Pedra(2,6);
		pedras.add(pedra16);
		Pedra pedra17 = new Pedra(3,4);
		pedras.add(pedra17);
		Pedra pedra18 = new Pedra(3,5);
		pedras.add(pedra18);
		Pedra pedra19 = new Pedra(3,6);
		pedras.add(pedra19);
		Pedra pedra20 = new Pedra(4,5);
		pedras.add(pedra20);
		Pedra pedra21 = new Pedra(4,6);
		pedras.add(pedra21);
		Pedra pedra22 = new Pedra(5,5);
		pedras.add(pedra22);
		Pedra pedra23 = new Pedra(5,6);
		pedras.add(pedra23);
		Pedra pedra24 = new Pedra(6,6);
		pedras.add(pedra24);
		Pedra pedra25 = new Pedra(2,2);
		pedras.add(pedra25);
		Pedra pedra26 = new Pedra(3,3);
		pedras.add(pedra26);
		Pedra pedra27 = new Pedra(4,4);
		pedras.add(pedra27);
		
		return pedras;
	}	
}