package game;

import java.util.ArrayList;
import util.Card;

public class Dealer {
	public ArrayList<Card> hand;

	public Dealer(){
		this.hand = new ArrayList<Card>();
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

	public String toString(boolean first){
		if(first){
			return "Dealer:" + hand.get(1);
		}
		return "Dealer:" + hand.toString() + ", Value: " + getValue();
	}
}
