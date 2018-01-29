package game;

import java.util.ArrayList;
import util.Card;
import java.util.Scanner;


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

	public Player(String name, int chips){
		this.hand = new ArrayList<Card>();
		this.name = name;
		this.chipCount = chips;
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
				handValue.add(0);
			}
		}

		for(int j = 0; j < this.hand.size(); j++){
			handValue.set(0, handValue.get(0) + this.hand.get(j).value);
		}

		if(flag){
			for(int i = 1; i <= aCount; i++){
				temp = (aCount-i)*11 + i;
				handValue.set(i, handValue.get(i) + temp);
				for(int j = 0; j < this.hand.size(); j++){
					if(this.hand.get(j).value != 11) handValue.set(i, handValue.get(i) + this.hand.get(j).value);
				}
			}
		}

		return handValue;
	}

	public boolean placeBet(Scanner input, int maxBet, int minBet){
		int bet;
		System.out.println(this.name + " place your bet. Current chip count: " + this.chipCount);

		bet = input.nextInt();
		if(bet > 0 && bet <= this.chipCount && bet >= minBet && bet <= maxBet){
			this.currentBet = bet;
			this.chipCount = this.chipCount - bet;
		}else{
			System.out.println("Please enter legal bet.");
			return false;
		}

		return true;
	}

	public String toString(){
		return name + ":" + hand;
	}
}
