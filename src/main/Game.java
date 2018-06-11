package main;

import util.*;
import java.util.concurrent.TimeUnit;
import game.*;
import java.util.Scanner;
import java.util.ArrayList;

public class Game{
  Table table;
  ArrayList<Player> players;

  public static void main(String[] args) throws InterruptedException {

    Game game = new Game();
    Scanner input = new Scanner(System.in);
    String choice_1 = "s", choice_2 = "c";
    while(!choice_1.equals("e")){
      choice_1 = game.mainMenu(input);

      if(choice_1.equals("s")){
        if((game.players = game.playersMenu(input)) == null) continue;
        game.table = game.tableMenu(input);
        game.playGame(choice_2, input);
      }else if(choice_1.equals("r")){
        game.playGame(choice_2, input);
      }else if(choice_1.equals("q")){
    	  	game.players = new ArrayList<Player>();
    		game.players.add(new Player("Joe"));
    		game.players.add(new BookAI("Cole"));
    		game.players.add(new CounterAI("Joker"));

    		game.table = new Table(1, 100, 5, game.players);
        game.playGame(choice_2, input);
      }
    }
		input.close();
	}

  private String mainMenu(Scanner input){
    String choice;

    System.out.println("Welcome to autojack!");
    if(this.table != null) System.out.println("r. Resume Game");
    System.out.println("s. Start Game");
    System.out.println("q. Quick Start");
    System.out.println("e. Exit");

    while(true){
      choice = input.next();
      switch(choice){
        case "s":
          return choice;
        case "e":
          return choice;
        case "r":
          if(this.table == null) System.out.println("No game to resume.");
          else return choice;
          return mainMenu(input);
        case "q":
          return choice;
        default:
          System.out.println("Please enter one of the options.");
          input.nextLine();
      }
		}
  }

  private void playGame(String choice_2, Scanner input){
    while(choice_2.equals("c")){
      try {
        table.takeBets(input);
        table.deal();

        System.out.println(table.toString(true));
        table.evaluate(input);
      }catch(EndOfShoeException e){
        System.out.println("End of Shoe, shuffling.");
        table.shuffleDeck();
      }

      table.clear();
      choice_2 = continuePlay(input);
    }
  }

  private ArrayList<Player> playersMenu(Scanner input){
    String choice;
    ArrayList<Player> players = new ArrayList<Player>();

    System.out.println("Create Game.\n-----------------");
    System.out.println("a. Add Players");
    System.out.println("s. Start Game");
    System.out.println("e. Exit");

    while(true){
      choice = input.next();
      switch(choice){
        case "s":
          if(players.size() < 1) System.out.println("Not enough players.");
          return players;
        case "e":
          return null;
        case "a":
          if(players.size() > 5) System.out.println("Cannot add anymore players.");
          else players.add(getPlayer(input));
          System.out.println("Create Game.\n-----------------");
          System.out.println("a. Add Players");
          System.out.println("s. Start Game");
          System.out.println("e. Exit");
          continue;
        default:
          System.out.println("Please enter one of the options.");
          input.nextLine();
      }
		}
  }

  private Player getPlayer(Scanner input){
    String choice, name;
    boolean done = false;
    Player p = null;
    int chips;

    System.out.println("Create Player.\n-----------------");
    System.out.println("h.Human\nc.Computer?");

    while(!done){
      choice = input.next();
      switch(choice){
        case "h":
          System.out.println("Name?");
          name = input.next();

          System.out.println("Number of chips?");
          while(true){
            try{
              chips = input.nextInt();
              if(chips < 0 || chips > 999999999) System.out.println("\n" + chips + " is not a valid number of chips.");
              else break;
            }catch(Exception e){System.out.println("\nPlease provide a numeric value.");}
          }

          p = new Player(name, chips);
          done = true;
          break;
        case "c":
          System.out.println("Name?");
          name = input.next();

          System.out.println("Number of chips?");
          while(true){
            try{
              chips = input.nextInt();
              if(chips < 0 || chips > 999999999) System.out.println("\n" + chips + " is not a valid number of chips.");
              else break;
            }catch(Exception e){System.out.println("\nPlease provide a numeric value.");}
          }

          p = new BookAI(name, chips);
          done = true;
          break;
        default:
          System.out.println("Please enter one of the options.");
          input.nextLine();
      }
		}
    return p;
  }

  private Table tableMenu(Scanner input){
    String schoice;
    int numDecks, minBet = 0, maxBet = 0, choice;
    boolean bet = false, done = false;

    System.out.println("Table Options.\n-----------------");
    System.out.print("Number of Decks: ");
    while(true){
      try{
        choice = input.nextInt();
        if(choice < 1 || choice > 10){
          System.out.println("\n" + choice + " is not a valid number of decks.");
        }else{
          numDecks = choice;
          break;
        }
      }catch(Exception e){System.out.println("\nPlease provide a numeric value.");}
    }

    System.out.println("Betting or nah?\ny.Yes\nn.No");
    while(!done){
      schoice = input.next();
      switch(schoice){
        case "y":
          bet = true;
          done = true;
          break;
        case "n":
          bet = false;
          done = true;
          break;
        default:
          System.out.println("Please enter one of the options.");
          input.nextLine();
      }
		}

    if(bet){
      System.out.print("Minimum Bet: ");
      while(true){
        try{
          choice = input.nextInt();
          if(choice < 1 || choice > 10){
            System.out.println("\n" + choice + " is not a valid minimum bet.");
          }else{
            minBet = choice;
            break;
          }
        }catch(Exception e){System.out.println("\nPlease provide a numeric value.");}
      }

      System.out.print("Maximum Bet: ");
      while(true){
        try{
          choice = input.nextInt();
          if(choice < 0 || choice > 99999999){
            System.out.println("\n" + choice + " is not a valid maximum bet.");
          }else{
            maxBet = choice;
            break;
          }
        }catch(Exception e){System.out.println("\nPlease provide a numeric value.");}
      }
    }

    if(bet) return new Table(minBet, maxBet, numDecks, this.players);
    else return new Table(numDecks, this.players);
  }

  private String continuePlay(Scanner input){
    String choice;
    boolean done = false;
    int r = 0;

    System.out.println("\nEnd of Hand.\n-----------------\nc.Continue\na.Add Player\nr.Remove Player\ne.Exit");

    while(!done){
      choice = input.next();
      switch(choice){
        case "c":
          return choice;
        case "a":
          if(this.players.size() > 5){
            System.out.println("To many players already in game.");
            return continuePlay(input);
          }
          this.table.players.add(getPlayer(input));
          this.table.numPlayers++;
          return continuePlay(input);
        case "r":
          if(this.players.size() < 2){
            System.out.println("Cannot remove all players from table.");
            return continuePlay(input);
          }
          System.out.println("Which Player should be removed?");
          for(int i = 0; i < this.players.size(); i++){
            System.out.println(i + ". " + this.players.get(i).name);
          }
          while(true){
            try{
              r = input.nextInt();
              if(r < 0 || r > this.players.size() - 1){
                System.out.println("\n" + choice + " is not a valid player number.");
              }else{
                this.table.removePlayer(r);
                break;
              }
            }catch(Exception e){System.out.println("\nPlease provide a numeric value.");}
          }
          return continuePlay(input);
        case "e":
          return choice;
        default:
          System.out.println("Please enter one of the options.");
          input.nextLine();
      }
		}
    return null;
  }
}
