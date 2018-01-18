package game;

import java.util.ArrayList;
import util.Card;

public class Player {
	public ArrayList<Card> hand;
	public String name;

	public Player(String name){
		this.hand = new ArrayList<Card>();
		this.name = name;
	}

	public void clearHand(){
		this.hand = new ArrayList<Card>();
	}

	public String toString(){
		return name + ":" + hand.toString() + ", Value: " + getValue();
	}

	public int getValue(){
		int handValue = 0;

		for(int i = 0; i < this.hand.size(); i++){
			handValue += this.hand.get(i).value;
		}

		return handValue;
	}
}
