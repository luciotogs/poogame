import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class JogadorPpl implements Jogador{

	protected Mao mao;
	protected String name;
	private Scanner read;
	private Pedra selected_pedra;
	
	public JogadorPpl(Mao mao) {
		this.mao = mao;
	}
	
	@Override
	public int get_sum() {
		return mao.most_valuable_pedra.get_add();
	}
	
	@Override
	public void show_hand() {
		System.out.println(mao.toString());
	}
	
	@Override
	public void play_turn(Mesa mesa){
		//System.out.println("Minhas pedras são = " + this.mao.toString());
		ArrayList<Pedra> pedrasDisponiveis = new ArrayList<>();
		System.out.println("Mesa com as pontas = " + mesa.edgeA + " e " + mesa.edgeB );
		System.out.println("Sua mão atual é = ");
		System.out.println(this.mao.pedras.toString());
		System.out.println("Pedras Disponiveis para jogar = ");
		int ind = 1;
		for(Pedra pedra : this.mao.pedras) {
			if(pedra.sideA == mesa.edgeA || pedra.sideB == mesa.edgeA || pedra.sideA == mesa.edgeB || 
					pedra.sideB == mesa.edgeB) {
				pedrasDisponiveis.add(pedra);
				System.out.println(ind+ " - " +pedra);
				ind ++;
			}
		}
		
		if(pedrasDisponiveis.isEmpty()) {
			System.out.println("Você não pode jogar, compre uma pedra !");
			cant_play(mesa);
			return;
		}
		
		selected_pedra = choices(pedrasDisponiveis);
		while(selected_pedra == null) {
			selected_pedra = choices(pedrasDisponiveis);
		}
		
		if((selected_pedra.sideA == mesa.edgeA || selected_pedra.sideB == mesa.edgeA) &&
			(selected_pedra.sideA == mesa.edgeB || selected_pedra.sideB == mesa.edgeB)) {
			System.out.println("Deseja jogar no lado A ou B da mesa ? Insira 0 para o lado A ou 1 para o lado B =}");
			int input = read.nextInt();
			if(input == 0) {
				if(!selected_pedra.is_simetric()) {
					if(selected_pedra.sideA == mesa.edgeA) {
						mesa.edgeA = selected_pedra.sideB;
					}else {
						mesa.edgeA = selected_pedra.sideA;
					}
				}else {
					mesa.edgeA = selected_pedra.sideA;
				}
				mesa.currentGame.add(0,selected_pedra);
			}else if(input == 1) {
				if(!selected_pedra.is_simetric()) {
					if(selected_pedra.sideA == mesa.edgeB) {
						mesa.edgeB = selected_pedra.sideB;
					}else {
						mesa.edgeB = selected_pedra.sideA;
					}
				}else {
					mesa.edgeB = selected_pedra.sideA;
				}
				mesa.currentGame.add(selected_pedra);
			}
			mao.pedras.remove(selected_pedra);
		}else {
			if(selected_pedra.sideA == mesa.edgeA || selected_pedra.sideB == mesa.edgeA) {
				if(selected_pedra.sideA == mesa.edgeA) {
					mesa.edgeA = selected_pedra.sideB;
				}else {
					mesa.edgeA = selected_pedra.sideA;
				}
				mesa.currentGame.add(0,selected_pedra);
				mao.pedras.remove(selected_pedra);
			}else {
				if(selected_pedra.sideA == mesa.edgeB || selected_pedra.sideB == mesa.edgeB) {
					if(selected_pedra.sideA == mesa.edgeB) {
						mesa.edgeB = selected_pedra.sideB;
					}else {
						mesa.edgeB = selected_pedra.sideA;
					}
					mesa.currentGame.add(selected_pedra);
					mao.pedras.remove(selected_pedra);
				}
			}
		}
		return;
	}
	
	public void cant_play(Mesa mesa) {
		System.out.println("Comprando pedra");
		if(mesa.monte.size() > 0) {
			Pedra b_pedra = mesa.monte.get(0);
			mesa.monte.remove(b_pedra);
			this.mao.pedras.add(b_pedra);
			System.out.println("Pedra comprada");
		}
		System.out.println("MONTE VAZIO !");
		return;
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
	public void set_name(String name) {
		this.name = name;
	}

	@Override
	public String get_name() {
		return this.name;
	}
	
	@Override
	public int get_hand_size() {
		return this.mao.pedras.size();
	}
	
	@SuppressWarnings("finally")
	public Pedra choices(ArrayList<Pedra> pedrasDisponiveis) {
		read = new Scanner(System.in);
		int input = read.nextInt();
		Pedra select_pedra = null;
		
		try{
			select_pedra = pedrasDisponiveis.get((input-1));
		}catch(Exception npe) {
			JOptionPane.showMessageDialog(null,"There is no such indice in the available pedras");
			//System.out.println("You have chosen a non valid indice");
		}finally {
			return select_pedra;
		}
	}

	@Override
	public void set_opponent_hand_amount(int hand_amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int get_opponent_hand_amount() {
		// TODO Auto-generated method stub
		return 0;
	}
}
