package test;

import util.*;
import java.util.concurrent.TimeUnit;
import game.*;
import java.util.Scanner;
import java.util.ArrayList;

public class BasicTest {

	public static void main(String[] args) throws InterruptedException, EndOfShoeException {

		ArrayList<Player> players = new ArrayList<Player>();
		Scanner input = new Scanner(System.in);
		// players.add(new Player("Mike"));
		
		players.add(new CounterAI("Joe"));
		
		Table table = new Table(1, 100, 5, players);
		table.deal();
		
		table.players.get(0).hand = new ArrayList<Card>();
		table.players.get(0).hand.add(new Card(9,'c'));
		table.players.get(0).hand.add(new Card(9,'s'));
		table.takeBets(input);
		
		System.out.println(table.toString(true));
        table.evaluate(input);
        System.out.println(table.players.get(0).chipCount);
//		int hands = 0, shuffleCount = 0;
//		String play = "";
//
//		while(true){
//			try {
//				table.takeBets(input);
//
//				table.deal();
//				System.out.println(table.toString(true));
//				table.evaluate(input);
//			}catch(EndOfShoeException e){
//				System.out.println("End of Shoe, shuffling. Shuffled " + shuffleCount + " times.");
//				table.shuffleDeck();
//				shuffleCount++;
//			}
//
//			table.clear();
//			hands++;
//			System.out.println("Play Again? (y/n)");
//			play = input.next();
//			if(play.contains("n")){
//				break;
//			}
//		}
//
//		System.out.println("End of Game.");
//		input.close();
	}
}
