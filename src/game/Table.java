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

	public Table(int minBet, int maxBet, int numDecks, ArrayList<Player> players){
		this.numPlayers = players.size();
		if(numPlayers > 1 && numPlayers < 7){
			this.players = players;
			deck = new Deck(numDecks);
			dealer = new Dealer();
			this.minBet = minBet;
			this.maxBet = maxBet;
			this.numDecks = numDecks;
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

		//Check Dealer
		if(this.dealer.getValue() == 21){
			System.out.println("Dealer has 21");
		}else{
			//Loop through each player
			for(int i = 0; i < this.numPlayers; i++){
				choice = "";
				if(checkBust(i)) continue;

				if(this.players.get(i).getValue() == 21){
					System.out.println(players.get(i).name + " got a Blackjack!");
					continue;
				}

				//Listen to user input
				while(!choice.contains("s")){
					System.out.println(this.players.get(i) + ": h, s");

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
			System.out.println("\nDealer has " + this.dealer.toString(false));
			while(this.dealer.getValue() < 17){
				dealer.hand.add(this.deck.draw());
				System.out.println(this.dealer.toString(false));
				TimeUnit.SECONDS.sleep(2);
			}

			System.out.println("\n-----------------------------------");
			//Check winning hands
			for(int i = 0; i < this.numPlayers; i++){
				value = this.players.get(i).getValue();
				if(value <= 21 && (value > this.dealer.getValue() || this.dealer.getValue() > 21)){
					payout(i, false);
					System.out.println(this.players.get(i).name + " wins with " + players.get(i));
				}else if(value <= 21 && value == this.dealer.getValue()){
					payout(i, true);
					System.out.println(this.players.get(i).name + " pushes with " + players.get(i));
				}else{
					System.out.println(this.players.get(i).name + " loses with " + players.get(i));
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
		if(push){
			this.players.get(i).chipCount = this.players.get(i).chipCount + this.players.get(i).currentBet;
			this.players.get(i).currentBet = 0;
		}else{
			this.players.get(i).chipCount = this.players.get(i).chipCount + this.players.get(i).currentBet*2;
			this.players.get(i).currentBet = 0;
		}

	}

	public String toString(boolean first){
		if(first){
			return this.dealer.toString(true) + ", " + "Players: " + this.players;
		}
		return this.dealer.toString(false) + ", " + "Players: " + this.players;
	}
}
