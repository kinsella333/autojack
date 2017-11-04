package util;

public class Card {
	public int number, value;
	public char suit;
	
	public Card(int number ,char suit){
		this.number = number;
		this.suit = suit;
		if(number > 10){
			this.value = 10;
		}else if(number == 1){
			this.value = 11;
		}else{
			this.value = number;
		}
	}
	
	public String toString(){
		return Integer.toString(this.number)+ suit;
	}
}
