package game;

import java.util.ArrayList;
import util.Card;
import java.util.HashMap;
import java.util.Random;

public class CounterAI extends Player{

  private int count;

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
    count = 0;
  }

  public String decide(ArrayList<ArrayList<Card>> pHands, int numDecks){

  }

  public boolean placeBet(int numDecks, int maxBet, int minBet){
    Random rand = new Random();
    int bet, max, min;
    System.out.println(this.name + " place your bet. Current chip count: " + this.chipCount);

    if(count/numDecks >= 3){
      if(minBet > this.chipCount/2 - this.chipCount/4) min = minBet;
      else min = this.chipCount/2 - this.chipCount/4;

      if(maxBet < this.chipCount/2) max = maxBet;
      else max = this.chipCount/2;
    }else{
      min = minBet;

      if(minBet + this.chipCount/10 < this.chipCount) max = minBet + this.chipCount/10;
      else max = minBet;
    }

    bet = rand.nextInt(max) + min;
    System.out.println(bet);

    this.currentBet = bet;
    this.chipCount = this.chipCount - bet;
    return true;
  }
}
