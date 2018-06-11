package game;

import java.util.ArrayList;
import java.util.Scanner;
import util.*;

public class Table {
	public ArrayList<Player> players;
	private Dealer dealer;
	public int minBet, maxBet, numDecks, numPlayers;
	public Deck deck;
	private boolean betsEnabled;

	public Table(int minBet, int maxBet, int numDecks, ArrayList<Player> players){
		this.numPlayers = players.size();
		if(numPlayers > 0 && numPlayers < 7){
			deck = new Deck(numDecks);
			dealer = new Dealer();

			this.players = players;
			this.minBet = minBet;
			this.maxBet = maxBet;
			this.numDecks = numDecks;
			this.betsEnabled = true;
		}
	}

	public Table(int numDecks, ArrayList<Player> players){
		this.numPlayers = players.size();
		if(numPlayers > 0 && numPlayers < 7){
			deck = new Deck(numDecks);
			dealer = new Dealer();

			this.players = players;
			this.numDecks = numDecks;
			this.betsEnabled = false;
		}
	}

	public void deal() throws EndOfShoeException{
		if((numPlayers+1)*4 < this.deck.cardsRemaining()){
			for(int i = 0; i < players.size()*2; i++){
				this.players.get(i%players.size()).hand.add(this.deck.draw());
				if(i == players.size()){
					this.dealer.hand.add(this.deck.draw());
				}
			}
			this.dealer.hand.add(this.deck.draw());
		}else throw new EndOfShoeException("End of Shoe");
	}

	public void clear(){
		for(int i = 0; i < players.size(); i++) players.get(i).clearHand();
		dealer.clearHand();
	}

	public void shuffleDeck(){
		this.deck = new Deck(this.numDecks);
	}

	public void evaluate(Scanner input){
		String choice = "";
		int value = 0, dValue = 1000;
		Player p;
		ArrayList<Integer> vList;

		//Check Dealer
		if(checkBlackjack(-1)){
			System.out.println("Dealer has 21");
		}else{
			//Loop through each player
			for(int i = 0; i < this.numPlayers; i++){
				p = this.players.get(i);
				choice = "";
				boolean stay = false;
				if(checkBust(i) == 1) continue;

				if(checkBlackjack(i)){
					System.out.println(players.get(i).name + " got a Blackjack!");
					continue;
				}

				//Listen to user input
				while(!stay){
					vList = p.getValue();

					if(vList.size() > 1){
						System.out.print(p + " Value: ");
						for(int k = 0; k < vList.size()-1; k++) System.out.print(vList.get(k) + " or ");
						System.out.println(vList.get(vList.size() - 1) + ": h, s, d, spl");
					}
					else System.out.println(p + " Value: " + vList.get(0) + ": h, s, d, spl");

					while(true){
						if(this.players.get(i).getClass() == (new BookAI()).getClass()){
							BookAI b = (BookAI)this.players.get(i);
							choice = b.decide(this.dealer.showCard(), b.hand);
							System.out.println(choice);
						}else if(this.players.get(i).getClass() == (new CounterAI()).getClass()){
							CounterAI c = (CounterAI)this.players.get(i);
							choice = c.decide(this.players, this.dealer.showCard(), c.hand, this.numDecks);
							System.out.println(choice);
						}
						else choice = input.next();

						if(choice.equals("h") || (choice.equals("d") && maxBet == 0) || (choice.equals("d") && this.players.get(i).hand.size() > 2)){
							hit(i);
							if(checkBust(i) == 1) stay = true;
							break;
						}else if(choice.equals("s")){
							stay = true;
							break;
						}else if(p.hand.size() < 3){
							if(choice.equals("d") && maxBet != 0){
								if(p.chipCount >= p.currentBet){
									hit(i);
									this.players.get(i).chipCount = this.players.get(i).chipCount - p.currentBet;
									this.players.get(i).currentBet = p.currentBet*2;
									stay = true;
								}else{
									System.out.println("Not enough chips to double");
									continue;
								}
								if(checkBust(i) == 1) stay = true;
								break;
							}else if(choice.equals("spl") && p.hand.get(0).number == p.hand.get(1).number){
								if(p.chipCount >= p.currentBet) {
									handleSplit(i);
									
									this.players.get(i).chipCount -= p.currentBet;
									this.players.get(i).currentBet = p.currentBet*2;
									break;
								}else System.out.println("Not enough chips to split");
							}else System.out.println("Cards must be equal to split");
						}else System.out.println("Not a legal input.");
					}
			 }
			 vList = p.getValue();
			 if(vList.size() > 1){
				 System.out.print(p + " Value: ");
				 for(int k = 0; k < vList.size()-1; k++) System.out.print(vList.get(k) + " or ");
				 System.out.println(vList.get(vList.size() - 1));
			 }else System.out.println(p + " Value: " + vList.get(0));
		 }

		 //Check dealer and hit hand
			ArrayList<Integer> dList = this.dealer.getValue();
			if(dList.size() > 1){
				System.out.print("\n" + this.dealer.toString(false) + " Value: ");
				for(int k = 0; k < dList.size()-1; k++) System.out.print(dList.get(k) + " or ");
				System.out.println(dList.get(dList.size() - 1));
			}
			else System.out.println("\n" + this.dealer.toString(false) + " Value: " + dList.get(0));

			while((dValue = checkBust(-1)) < 22){

				if((dValue = checkDone(-1)) > 2) break;
				dealer.hand.add(this.deck.draw());

				dList = this.dealer.getValue();
				if(dList.size() > 1){
					System.out.print(this.dealer.toString(false) + " Value: ");
					for(int i = 0; i < dList.size()-1; i++) System.out.print(dList.get(i) + " or ");
					System.out.println(dList.get(dList.size() - 1));
				}else System.out.println(this.dealer.toString(false) + " Value: " + dList.get(0));
			}

			if(dValue > 21) System.out.println("\nDealer Busts!");
			else System.out.println("");

			//Check winning hands
			System.out.println("-----------------------------------");
			for(int i = 0; i < this.numPlayers; i++){
				value = checkDone(i);

				if(value <= 21 && (value > dValue || dValue > 21)) i = payout(i, 1, value);
				else if(value <= 21 && value == dValue) i = payout(i, 2, value);
				else i = payout(i, -1, value);
			}
			System.out.println("-----------------------------------\n");
		}
	}

	private void hit(int index){
		this.players.get(index).hand.add(this.deck.draw());
	}

	private int checkBust(int i){
		int bustCount = 0;
		if(i >= 0){
			Player p = this.players.get(i);
			ArrayList<Integer> vList = p.getValue();

			for(int j = 0; j < vList.size(); j++){
				if(vList.get(j) > 21){
					bustCount++;
				}
			}

			if(bustCount == vList.size()){
				System.out.println(p.name + " has busted.");
				return 1;
			}
			return 0;
		}else{
			ArrayList<Integer> vList = this.dealer.getValue();

			for(int j = 0; j < vList.size(); j++){
				if(vList.get(j) > 21) bustCount++;
			}
			if(bustCount == vList.size()) return vList.get(vList.size()-1);
			return 0;
		}
	}

	private int checkDone(int i){
		int max = -1, min = 99;

		if(i >= 0){
			ArrayList<Integer> vList = this.players.get(i).getValue();

			for(int j = 0; j < vList.size(); j++){
				if(vList.get(j) < 22 && vList.get(j) > max) max = vList.get(j);
				else if(vList.get(j) < min) min = vList.get(j);
			}
			if(max == -1) return min;
		}else{
			ArrayList<Integer> vList = this.dealer.getValue();

			for(int j = 0; j < vList.size(); j++){
				if(vList.size() > 1){
					if(vList.get(j) > 17 && vList.get(j) < 22 && vList.get(j) > max) max = vList.get(j);
				}else if(vList.get(j) > 16 && vList.get(j) < 22 && vList.get(j) > max) max = vList.get(j);
			}
		}
		return max;
	}

	private boolean checkBlackjack(int i){
		ArrayList<Integer> vList;
		if(i >= 0) vList = this.players.get(i).getValue();
		else vList = this.dealer.getValue();

		for(int j = 0; j < vList.size(); j++){
			if(vList.get(j) == 21) return true;
		}
		return false;
	}

	public void takeBets(Scanner input){
		boolean done;

		for(int i = 0; i < this.numPlayers; i++){
			done = false;

			if(this.players.get(i).chipCount < 1 || this.players.get(i).chipCount < minBet){
				System.out.println(players.get(i).name + " does not have enough chips to continue.");
				this.numPlayers--;
				this.players.remove(i);
				i--;
			}else{
				while(!done){
					if(this.players.get(i).getClass() == (new BookAI()).getClass()) done = ((BookAI)this.players.get(i)).placeBet(maxBet, minBet);
					else if(this.players.get(i).getClass() == (new CounterAI()).getClass()) done = ((CounterAI)this.players.get(i)).placeBet(this.numDecks, maxBet, minBet);
					else done = this.players.get(i).placeBet(input, maxBet, minBet);
				}
			}
		}
	}

	private int payout(int i, int push, int value){
		if(this.players.get(i).name.contains("_SPLIT")){
			this.players.remove(i);
			this.numPlayers--;
			i = i-1;
		}

		if(this.betsEnabled){
			if(push == 1){
				this.players.get(i).chipCount = this.players.get(i).chipCount + this.players.get(i).currentBet*2;
				this.players.get(i).currentBet = 0;
				System.out.println(this.players.get(i).name + " wins with " + this.players.get(i).hand + " Value: " + value + " Chips: " + this.players.get(i).chipCount);
			}else if(push == 2){
				this.players.get(i).chipCount = this.players.get(i).chipCount + this.players.get(i).currentBet;
				this.players.get(i).currentBet = 0;
				System.out.println(this.players.get(i).name + " pushes with " + this.players.get(i).hand + " Value: " + value + " Chips: " + this.players.get(i).chipCount);
			}else System.out.println(this.players.get(i).name + " loses with " + this.players.get(i).hand + " Value: " + value + " Chips: " + this.players.get(i).chipCount);
		}
		return i;
	}

	private void handleSplit(int i){
		if(this.players.get(i).getClass() == (new BookAI()).getClass()){
			if(i == this.numPlayers-1) this.players.add(new BookAI(this.players.get(i).name + "_SPLIT"));
			else this.players.add(i+1, new BookAI(this.players.get(i).name + "_SPLIT"));
		}else if(this.players.get(i).getClass() == (new CounterAI()).getClass()){
			if(i == this.numPlayers-1) this.players.add(new CounterAI(this.players.get(i).name + "_SPLIT"));
			else this.players.add(i+1, new CounterAI(this.players.get(i).name + "_SPLIT"));
		}else{
			if(i == this.numPlayers-1) this.players.add(new Player(this.players.get(i).name + "_SPLIT"));
			else this.players.add(i+1, new Player(this.players.get(i).name + "_SPLIT"));
		}

		this.players.get(i+1).hand.add(this.players.get(i).hand.remove(1));
		this.players.get(i).hand.add(this.deck.draw());
		this.players.get(i+1).hand.add(this.deck.draw());
		this.numPlayers++;
	}

	public void removePlayer(int index){
		this.players.remove(index);
		this.numPlayers--;
	}

	public String toString(boolean first){
		if(first){
			return "\n" + this.dealer.toString(true) + "\n" + "Players: " + this.players;
		}
		return "\n" + this.dealer.toString(false) + "\n" + "Players: " + this.players;
	}
}
