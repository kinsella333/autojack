package game;

import java.util.Arrays;
import util.Deck;

public class Table {
	public Player[] players;
	private Dealer dealer;
	private int minBet, maxBet, numDecks;
	public Deck deck;

	public Table(int numPlayers, int minBet, int maxBet, int numDecks, Player[] players){
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
		if((this.players.length+1)*2 < this.deck.cardsRemaining()){
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
		this.deck = new Deck(this.numDecks)
	}

	public String toString(){
		return this.dealer.toString() + ", " + "Players: " + Arrays.toString(this.players);
	}
}
