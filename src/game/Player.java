package game;

import java.util.ArrayList;
import util.Card;

public class Player {
	public ArrayList<Card> hand;
	public String name;
	public int chipCount, currentBet;

	public Player(String name){
		this.hand = new ArrayList<Card>();
		this.name = name;
		this.chipCount = 25;
		this.currentBet = 0;
	}

	public void clearHand(){
		this.hand = new ArrayList<Card>();
	}

	public int getValue(){
		int handValue = 0;

		for(int i = 0; i < this.hand.size(); i++){
			handValue += this.hand.get(i).value;
		}

		return handValue;
	}

	public String toString(){
		return name + ":" + hand;
	}
}
