package game;

import java.util.ArrayList;
import util.Card;
import java.util.HashMap;
import java.util.Random;

public class CounterAI extends BookAI{

  private float count;
  // private HashMap<String, HashMap> dMatrix;

  public CounterAI (String name){
    super(name + "_McCheaterpants");
    count = 0;
  }

  public CounterAI (){
    super("");
    count = 0;
  }

  public CounterAI (String name, int chips){
    super(name + "_McCheaterpants", chips);
    // this.dMatrix = buildMatrix();
    count = 0;
  }

  public String decide(ArrayList<Player> players, Card dCard, ArrayList<Card> hand, int numDecks){
    int index;
    ArrayList<Card> tableCards = new ArrayList<Card>();
    tableCards.add(dCard);
    for(Player p : players) tableCards.addAll(p.hand);

    for(Card c : tableCards){
      if(c.value == 2 || c.value == 7) this.count += 0.5;
      else if(c.value == 3 || c.value == 4 || c.value == 6) this.count += 1;
      else if(c.value == 5) this.count += 1.5;
      else if(c.value == 9) this.count -= 1.5;
      else if(c.value > 9) this.count -= 1;
    }

    if(hand.size() < 3 && (index = containsAce(hand)) >= 0){
      return ((HashMap<Integer, String>)((HashMap<Integer, HashMap>)dMatrix.get("Ace")).get(hand.get(index).value)).get(dCard.value);
    }else if(hand.size() < 3 && hand.get(0).number == hand.get(1).number){
      return ((HashMap<Integer, String>)((HashMap<Integer, HashMap>)dMatrix.get("Pairs")).get(hand.get(1).value)).get(dCard.value);
    }else{
      Player p = new Player("T");
      p.hand = hand;
      ArrayList<Integer> vList = p.getValue();

      if(vList.size() < 2) return ((HashMap<Integer, String>)((HashMap<Integer, HashMap>)dMatrix.get("Values")).get(p.getValue().get(0))).get(dCard.value);
      else{
        return ((HashMap<Integer, String>)((HashMap<Integer, HashMap>)dMatrix.get("Values")).get(checkDone(vList))).get(dCard.value);
      }
    }

  }

  public boolean placeBet(int numDecks, int maxBet, int minBet, boolean auto){
    Random rand = new Random();
    int bet, max, min;
    if(!auto){
      System.out.println(this.name + " place your bet. Current chip count: " + this.chipCount);
    }

    if(count/numDecks >= 5){
      if(minBet > this.chipCount/2 - this.chipCount/4) min = minBet;
      else min = this.chipCount/2 - this.chipCount/4;

      if(maxBet < this.chipCount/2) max = maxBet;
      else max = this.chipCount/2;
    }else if(count/numDecks >= 3){
      if(minBet > this.chipCount/4) min = minBet;
      else min = this.chipCount/4;

      if(maxBet < this.chipCount/2) max = maxBet;
      else max = this.chipCount/2;
    }else{
      min = minBet;

      if(minBet + this.chipCount/10 < this.chipCount) max = minBet + this.chipCount/10;
      else max = minBet;
    }

    bet = rand.nextInt(max) + min;
    if(!auto) System.out.println(bet);

    this.currentBet = bet;
    this.chipCount = this.chipCount - bet;
    return true;
  }
}
