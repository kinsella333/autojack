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

	public ArrayList<Integer> getValue(){
		boolean flag = false;
		int temp = 0, aCount = 0;
		ArrayList<Integer> handValue = new ArrayList<>();
		handValue.add(0);

		for(int i = 0; i < this.hand.size(); i++){
			if(hand.get(i).value == 11){
				flag = true;
				temp = handValue.size();
				aCount++;
				for(int j = 0; j < temp; j++){
					handValue.add(0);
				}
			}
		}

		for(int j = 0; j < this.hand.size(); j++){
			handValue.set(0, handValue.get(0) + this.hand.get(j).value);
		}

		if(flag){
			for(int i = 1; i <= aCount; i++){
				temp = temp + (aCount-i)*11 + i;
				handValue.set(aCount, handValue.get(aCount) + temp);
				for(int j = 0; j < this.hand.size(); j++){
					if(this.hand.get(j).value != 11) handValue.set(aCount, handValue.get(aCount) + this.hand.get(j).value);
				}
			}
		}

		return handValue;
	}

	public String toString(){
		return name + ":" + hand;
	}
}
