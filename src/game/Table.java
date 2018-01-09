package game;

import java.util.Arrays;

import util.Deck;

public class Table {
	public Player[] players;
	private Dealer dealer;
	private int minBet, maxBet;
	public Deck deck;
	
	public Table(int numPlayers, int minBet, int maxBet, int numDecks, Player[] players){
		if(numPlayers > 1 && numPlayers < 7){
			this.players = players;
			deck = new Deck(numDecks);
			dealer = new Dealer();
			this.minBet = minBet;
			this.maxBet = maxBet;
		}
	}
	
	public void deal() throws Exception{
		if((this.players.length+1)*2 < this.deck.cardsRemaining()){
			for(int i = 0; i < players.length*2; i++){
				this.players[i%players.length].hand.add(this.deck.draw());
				if(i == players.length){
					this.dealer.hand.add(this.deck.draw());
				}
			}
			this.dealer.hand.add(this.deck.draw());
		}else{
			throw new Exception("EndOFShoeException");
		}
	}
	
	public void clear(){
		for(int i = 0; i < players.length; i++){
			players[i].clearHand();
		}
		dealer.clearHand();
	}
	
	public String toString(){
		return this.dealer.toString() + ", " + Arrays.toString(this.players);
	}
}
