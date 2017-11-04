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
	
	public String toString(){
		return "Dealer:" + hand.toString();
	}
}
