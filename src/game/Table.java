package game;

import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import util.Deck;

public class Table {
	public Player[] players;
	private Dealer dealer;
	public int minBet, maxBet, numDecks, numPlayers;
	public Deck deck;

	public Table(int numPlayers, int minBet, int maxBet, int numDecks, Player[] players){
		if(numPlayers > 1 && numPlayers < 7){
			this.players = players;
			deck = new Deck(numDecks);
			dealer = new Dealer();
			this.minBet = minBet;
			this.maxBet = maxBet;
			this.numDecks = numDecks;
			this.numPlayers = numPlayers;
		}
	}

	public void deal() throws EndOfShoeException{
		if((this.players.length+1)*4 < this.deck.cardsRemaining()){
			for(int i = 0; i < players.length*2; i++){
				this.players[i%players.length].hand.add(this.deck.draw());
				if(i == players.length){
					this.dealer.hand.add(this.deck.draw());
				}
			}
			this.dealer.hand.add(this.deck.draw());
		}else{
			throw new EndOfShoeException("End of Shoe");
		}
	}

	public void clear(){
		for(int i = 0; i < players.length; i++){
			players[i].clearHand();
		}
		dealer.clearHand();
	}

	public void shuffleDeck(){
		this.deck = new Deck(this.numDecks);
	}

	public void evaluate(Scanner input) throws InterruptedException{
		String choice = "";
		int value = 0;

		if(this.dealer.getValue() > 21){
			System.out.println("Dealer has busted.");
		}else if(this.dealer.getValue() == 21){
			System.out.println("Dealer has 21");
		}else{
			for(int i = 0; i < this.numPlayers; i++){
				choice = "";
				if(checkHand(i)) continue;

				while(!choice.contains("s")){
					System.out.println(this.players[i] + ": h, s");

					while(true){
						if(checkHand(i)){
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
			System.out.println("\nDealer has " + this.dealer.toString(false));
			while(this.dealer.getValue() < 17){
				dealer.hand.add(this.deck.draw());
				System.out.println(this.dealer.toString(false));
				TimeUnit.SECONDS.sleep(2);
			}

			for(int i = 0; i < this.numPlayers; i++){
				value = this.players[i].getValue();
				if(value =< 21 && (value > this.dealer.getValue() || this.dealer.getValue() > 21)){
					System.out.println(this.players[i].name + " wins with " + players[i]);
				}else if(value =< 21 && value == this.dealer.getValue()){
					System.out.println(this.players[i].name + " pushes with " + players[i]);
				}else{
					System.out.println(this.players[i].name + " loses with " + players[i]);
				}
			}
		}
	}

	private void hit(int index){
		this.players[index].hand.add(this.deck.draw());
	}

	private boolean checkHand(int i){
		if(this.players[i].getValue() > 21){
			System.out.println(this.players[i].name + " has busted.");
			return true;
		}
		return false;
	}

	public String toString(boolean first){
		if(first){
			return this.dealer.toString(true) + ", " + "Players: " + Arrays.toString(this.players);
		}
		return this.dealer.toString(false) + ", " + "Players: " + Arrays.toString(this.players);
	}
}
