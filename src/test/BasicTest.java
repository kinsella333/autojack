package test;

import util.*;
import java.util.concurrent.TimeUnit;
import game.*;
import java.util.Scanner;
import java.util.ArrayList;

public class BasicTest {

	public static void main(String[] args) throws InterruptedException {

		ArrayList<Player> players = new ArrayList<Player>();
		players.add(new Player("Mike"));
		players.add(new Player("Joe"));
		players.add(new Player("Cole"));

		Table table = new Table(1, players);
		int hands = 0, shuffleCount = 0;
		Scanner input = new Scanner(System.in);
		String play = "";

		while(true){
			try {
				table.deal();
				System.out.println(table.toString(true));
				table.evaluate(input);
			}catch(EndOfShoeException e){
				System.out.println("End of Shoe, shuffling. Shuffled " + shuffleCount + " times.");
				table.shuffleDeck();
				shuffleCount++;
			}

			table.clear();
			hands++;
			System.out.println("Play Again? (y/n)");
			play = input.next();
			if(play.contains("n")){
				break;
			}
		}

		System.out.println("End of Game.");
		input.close();
	}
}
