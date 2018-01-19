package util;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {
	public ArrayList<Card> deck;
	public int deckNumber;

	public Deck(int deckNumber){
		deck = new ArrayList<Card>();
		for(int k = 0; k < deckNumber; k++){
			for(int i = 1; i < 14; i++){
				for(int j = 0; j < 4; j++){
					switch(j){
						case 0:
							deck.add(new Card(i, 'h'));
							break;
						case 1:
							deck.add(new Card(i, 'd'));
							break;
						case 2:
							deck.add(new Card(i, 'c'));
							break;
						case 3:
							deck.add(new Card(i, 's'));
							break;
					}
				}
			}
		}
		Collections.shuffle(this.deck);
	}

	public void shuffle(){
		Collections.shuffle(this.deck);
	}

	public int cardsRemaining(){
		return this.deck.size();
	}

	public Card draw(){
		if(this.deck.size() > 0){
			return this.deck.remove(0);
		}else{
			return null;
		}
	}

	public String toString(){
		return this.deck.toString();
	}
}
