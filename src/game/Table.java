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
		if(numPlayers > 1 && numPlayers < 7){
			deck = new Deck(numDecks);
			dealer = new Dealer();

			this.players = players;
			this.numDecks = numDecks;
			this.betsEnabled = false;
		}
	}

	public void deal() throws EndOfShoeException{
		if((this.players.size()+1)*4 < this.deck.cardsRemaining()){
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
		int value = 0;
		Player p;

		//Check Dealer
		if(this.dealer.getValue() == 21){
			System.out.println("Dealer has 21");
		}else{
			//Loop through each player
			for(int i = 0; i < this.numPlayers; i++){
				p = this.players.get(i);
				choice = "";
				if(checkBust(i)) continue;

				if(p.getValue() == 21){
					System.out.println(players.get(i).name + " got a Blackjack!");
					continue;
				}

				//Listen to user input
				while(!choice.contains("s")){
					System.out.println(p + " Value: " + p.getValue() + ": h, s");

					while(true){
						if(checkBust(i)){
							choice = "s";
							break;
						}
						choice = input.next();
						if(choice.contains("h")){
							hit(i);
							break;
						}else if(choice.contains("s")){
							break;
						}
					}
				}
			}

			//Check dealer and hit hand
			System.out.println("\nDealer has " + this.dealer.hand + " Value: " + this.dealer.getValue());
			while(this.dealer.getValue() < 17){
				TimeUnit.SECONDS.sleep(2);
				dealer.hand.add(this.deck.draw());
				System.out.println(this.dealer.toString(false) + " Value: " + this.dealer.getValue());
			}

			if(this.dealer.getValue() > 21) System.out.println("\nDealer Busts!");
			else System.out.println("");

			System.out.println("-----------------------------------");
			//Check winning hands
			for(int i = 0; i < this.numPlayers; i++){
				p = this.players.get(i);
				value = p.getValue();
				if(value <= 21 && (value > this.dealer.getValue() || this.dealer.getValue() > 21)){
					payout(i, false);
					System.out.println(p.name + " wins with " + p.hand + " Value: " + p.getValue());
				}else if(value <= 21 && value == this.dealer.getValue()){
					payout(i, true);
					System.out.println(p.name + " pushes with " + p.hand + " Value: " + p.getValue());
				}else{
					System.out.println(p.name + " loses with " + p.hand + " Value: " + p.getValue());
				}
			}
			System.out.println("-----------------------------------\n");
		}
	}

	private void hit(int index){
		this.players.get(index).hand.add(this.deck.draw());
	}

	private boolean checkBust(int i){
		if(this.players.get(i).getValue() > 21){
			System.out.println(this.players.get(i).name + " has busted.");
			return true;
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
