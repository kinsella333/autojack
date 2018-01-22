package game;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import util.Deck;

public class Table {
	public ArrayList<Player> players;
	private Dealer dealer;
	public int minBet, maxBet, numDecks, numPlayers;
	public Deck deck;
	private boolean betsEnabled;

	public Table(int minBet, int maxBet, int numDecks, ArrayList<Player> players){
		this.numPlayers = players.size();
		if(numPlayers > 1 && numPlayers < 7){
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
		}else{
			throw new EndOfShoeException("End of Shoe");
		}
	}

	public void clear(){
		for(int i = 0; i < players.size(); i++){
			players.get(i).clearHand();
		}
		dealer.clearHand();
	}

	public void shuffleDeck(){
		this.deck = new Deck(this.numDecks);
	}

	public void evaluate(Scanner input) throws InterruptedException{
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
				if(checkBust(i)) continue;

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
						System.out.println(vList.get(vList.size() - 1) + ": h, s");
					}
					else System.out.println(p + " Value: " + vList.get(0) + ": h, s");

					while(true){
						if(this.players.get(i).getClass() == (new BookAI()).getClass()){
							BookAI b = (BookAI)this.players.get(i);
							choice = b.decide(this.dealer.showCard(), b.hand);
							System.out.println(choice);
						}
						else choice = input.next();

						if(choice.equals("h") || choice.equals("d")){
							hit(i);
							if(checkBust(i)){
								stay = true;
							}
							break;
						}else if(choice.equals("s")){
							stay = true;
							break;
						}else{
							stay = true;
							System.out.println("SPLIT");
							break;
						}
					}
				}

				vList = p.getValue();
				if(vList.size() > 1){
					System.out.print(p + " Value: ");
					for(int k = 0; k < vList.size()-1; k++) System.out.print(vList.get(k) + " or ");
					System.out.println(vList.get(vList.size() - 1));
				}else System.out.println(p + " Value: " + vList.get(0));
			}

			ArrayList<Integer> dList = this.dealer.getValue();
			//Check dealer and hit hand
			if(dList.size() > 1){
				System.out.print(this.dealer.toString(false) + " Value: ");
				for(int k = 0; k < dList.size()-1; k++) System.out.print(dList.get(k) + " or ");
				System.out.println(dList.get(dList.size() - 1));
			}
			else System.out.println(this.dealer.toString(false) + " Value: " + dList.get(0));

			while(!checkBust(-1)){
				TimeUnit.SECONDS.sleep(2);
				dealer.hand.add(this.deck.draw());

				dList = this.dealer.getValue();
				if(dList.size() > 1){
					System.out.print(this.dealer.toString(false) + " Value: ");
					for(int i = 0; i < dList.size()-1; i++) System.out.print(dList.get(i) + " or ");
					System.out.println(dList.get(dList.size() - 1));
				}else System.out.println(this.dealer.toString(false) + " Value: " + dList.get(0));

				if((dValue = checkDone(-1)) >= 0) break;
			}

			if(dValue > 21) System.out.println("\nDealer Busts!");
			else System.out.println("");

			System.out.println("-----------------------------------");
			//Check winning hands
			for(int i = 0; i < this.numPlayers; i++){
				p = this.players.get(i);
				value = checkDone(i);

				if(value <= 21 && (value > dValue || dValue > 21)){
					payout(i, false);
					System.out.println(p.name + " wins with " + p.hand + " Value: " + value);
				}else if(value <= 21 && value == dValue){
					payout(i, true);
					System.out.println(p.name + " pushes with " + p.hand + " Value: " + value);
				}else{
					System.out.println(p.name + " loses with " + p.hand + " Value: " + value);
				}
			}
			System.out.println("-----------------------------------\n");
		}
	}

	private void hit(int index){
		this.players.get(index).hand.add(this.deck.draw());
	}

	private boolean checkBust(int i){
		if(i >= 0){
			Player p = this.players.get(i);
			ArrayList<Integer> vList = p.getValue();
			int bustCount = 0;

			for(int j = 0; j < vList.size(); j++){
				if(vList.get(j) > 21){
					bustCount++;
				}
			}

			if(bustCount == vList.size()){
				System.out.println(p.name + " has busted.");
				return true;
			}
			return false;
		}else{
			ArrayList<Integer> vList = this.dealer.getValue();
			int bustCount = 0;

			for(int j = 0; j < vList.size(); j++){
				if(vList.get(j) > 21){
					bustCount++;
				}
			}

			if(bustCount == vList.size()){
				return true;
			}
			return false;
		}
	}

	private int checkDone(int i){
		if(i >= 0){
			ArrayList<Integer> vList = this.players.get(i).getValue();
			int max = -1, min = 99;

			for(int j = 0; j < vList.size(); j++){
				if(vList.get(j) < 22 && vList.get(j) > max) max = vList.get(j);
				if(vList.get(j) < min) min = vList.get(j);
			}
			if(max == -1) return min;
			return max;
		}else{
			ArrayList<Integer> vList = this.dealer.getValue();
			int max = -1;

			for(int j = 0; j < vList.size(); j++){
				if(vList.size() > 1){
					if(vList.get(j) > 17 && vList.get(j) < 22 && vList.get(j) > max) max = vList.get(j);
				}else if(vList.get(j) > 16 && vList.get(j) < 22 && vList.get(j) > max) max = vList.get(j);
			}
			return max;
		}
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
		int bet = 0;

		for(int i = 0; i < this.numPlayers; i++){
			if(this.players.get(i).chipCount < 1){
				System.out.println(players.remove(i).name + " has run out of chips");
				this.numPlayers--;
				i--;
			}else{
				while(true){
					System.out.println(this.players.get(i).name + " place your bet. Current chip count: " + this.players.get(i).chipCount);
					bet = input.nextInt();
					if(bet > 0 && bet <= this.players.get(i).chipCount && bet >= this.minBet && bet <= this.maxBet){
						this.players.get(i).currentBet = bet;
						this.players.get(i).chipCount = this.players.get(i).chipCount - bet;
						break;
					}else{
						System.out.println("Please enter legal bet.");
					}
				}
			}
		}
	}

	private void payout(int i, boolean push){
		if(this.betsEnabled){
			if(push){
				this.players.get(i).chipCount = this.players.get(i).chipCount + this.players.get(i).currentBet;
				this.players.get(i).currentBet = 0;
			}else{
				this.players.get(i).chipCount = this.players.get(i).chipCount + this.players.get(i).currentBet*2;
				this.players.get(i).currentBet = 0;
			}
		}
	}


	public String toString(boolean first){
		if(first){
			return "\n" + this.dealer.toString(true) + "\n" + "Players: " + this.players;
		}
		return "\n" + this.dealer.toString(false) + "\n" + "Players: " + this.players;
	}
}
