package test;

import util.*;
import java.util.concurrent.TimeUnit;
import game.*;

public class BasicTest {

	public static void main(String[] args) throws InterruptedException {
//		Deck deck = new Deck(1);
//
//		System.out.println(deck);
//		deck.shuffle();
//		System.out.println(deck);
//
//		System.out.println(deck.cardsRemaining());
//		Card c = deck.draw();
//		System.out.println(c);
//		System.out.println(c.value);
//		System.out.println(deck);
//		System.out.println(deck.cardsRemaining());
		Player[] players = {new Player("Mike"), new Player("Joe"), new Player("Cole")};
		Table table = new Table(4, 5, 10, 1, players);
		boolean play = true;

		while(play){
			try {
				table.deal();
			}catch(EndOfShoeException e){
				System.err.println(e);
				break;
			}
			System.out.println(table);

			table.clear();
			TimeUnit.SECONDS.sleep(1);
		}
	}
}
