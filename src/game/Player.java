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
		return name + ":" + hand.toString();
	}
	
}
