import java.util.ArrayList;
import java.util.Collections;

public class main {
		
	public static void main(String[] args) throws CloneNotSupportedException {
		
		// creates the table
		Mesa mesa = new Mesa();
		
		// creates the Dominó
		ArrayList<Pedra> monte = new ArrayList<>();
		monte = init_game(monte);
		
		// shuffling
		Collections.shuffle(monte);
		
		// Generates a random hand
		ArrayList<Pedra> random_hand = new ArrayList<>();
		for (int i = 0; i < 7;i++) {
			Pedra tmp_pedra = monte.get(i);
			random_hand.add(tmp_pedra);
			monte.remove(tmp_pedra);
		}
		Mao player1_hand = new Mao(random_hand);

		// Creates MCTS player
		//Jogador jogadorMCTS = new JogadorMCTS(player1_hand);
		Jogador jogadorMCTS = new JogadorMCTS(player1_hand);
		jogadorMCTS.set_name("MCTS PLAYER, Disfarçado");
		//jogadorMCTS.show_hand();
		
		// New random hand 
		ArrayList<Pedra> random_hand2 = new ArrayList<>();
		for (int i = 0; i < 7;i++) {
			Pedra tmp_pedra = monte.get(i);
			random_hand2.add(tmp_pedra);
			monte.remove(tmp_pedra);
		}

		Mao player2_hand = new Mao(random_hand2);
		
		// Creates another player
		Jogador jogadorppl = new JogadorPpl(player2_hand);
		jogadorppl.set_name("PPL PLAYER");
		jogadorMCTS.set_opponent_hand_amount(jogadorppl.get_hand_size());
		//jogadorppl.show_hand();
		
		// Mesa receives monte
		mesa.set_monte(monte);
		
		main main = new main();
	
		if(jogadorMCTS.get_sum() > jogadorppl.get_sum()) {
			//mesa.add(jogadorMCTS.get_most_valuable());
			mesa.set_edgeA(jogadorMCTS.get_most_valuable().sideA);
			mesa.set_edgeB(jogadorMCTS.get_most_valuable().sideB);
			mesa.currentGame.add(jogadorMCTS.get_most_valuable());
			jogadorMCTS.remove_pedra(jogadorMCTS.get_most_valuable());
			main.play_turn_shalf(jogadorppl,jogadorMCTS,mesa);
		}else {
			//mesa.add(jogadorppl.get_most_valuable());
			mesa.set_edgeA(jogadorppl.get_most_valuable().sideA);
			mesa.set_edgeB(jogadorppl.get_most_valuable().sideB);
			mesa.currentGame.add(jogadorppl.get_most_valuable());
			jogadorppl.remove_pedra(jogadorppl.get_most_valuable());
			jogadorMCTS.set_opponent_hand_amount(jogadorMCTS.get_opponent_hand_amount()-1);
			main.play_turn_shalf(jogadorMCTS,jogadorppl,mesa);
		}
		
	}
	
	private void play_turn(Jogador playerA, Jogador playerB, Mesa mesa ) throws CloneNotSupportedException {
		//System.out.println("Play turn plays");
		System.out.println("PLAYER = " + playerA.get_name());
		playerA.play_turn(mesa);
		System.out.println("\n" + "Mesa Atual : ");
		System.out.println("Pontas da mesa = " + mesa.edgeA + " " + mesa.edgeB);
		System.out.println("Pedras na mesa = " + mesa.currentGame + "\n");
		play_turn_shalf(playerB,playerA,mesa);
	}
	
	private void play_turn_shalf(Jogador playerA, Jogador playerB, Mesa mesa) throws CloneNotSupportedException {
		//System.out.println("Play turn shalf plays");
		System.out.println("PLAYER = " + playerA.get_name());
		if(playerB.get_hand_size() == 0) {
			playerA.play_turn(mesa);
			if(playerA.get_hand_size() == 0) {
				System.out.println("Jogo terminou empatado");
			}else {
				System.out.println("Jogo terminado, vencedor = " + playerB.get_name());
				return;
			}
		}else {
			playerA.play_turn(mesa);
			if(playerA.get_hand_size() == 0) {
				System.out.println("Jogo terminado, vencedor = " + playerA.get_name());
				return;
			}
			System.out.println("\n" + "Mesa Atual : ");
			System.out.println("Pontas da mesa = " + mesa.edgeA + " " + mesa.edgeB);
			System.out.println("Pedras na mesa = " + mesa.currentGame + "\n");
			play_turn(playerB,playerA,mesa);
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