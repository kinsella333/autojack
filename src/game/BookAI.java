package game;

import java.util.ArrayList;
import util.Card;
import java.util.HashMap;

public class BookAI extends Player{

  private HashMap<String, HashMap> dMatrix = new HashMap<String, HashMap>();

  public BookAI (String name){
    super(name + "_AI");
    buildMatrix();
  }

  public BookAI (){
    super("");
    buildMatrix();
  }

  public BookAI (String name, int chips){
    super(name + "_AI", chips);
    buildMatrix();
  }

  public String decide(Card dCard, ArrayList<Card> hand){
    int index;

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

  private void buildMatrix(){
    int[] dCards = {2, 3, 4, 5, 5, 6, 7, 8, 9, 10, 11};
    HashMap<Integer, HashMap<Integer, String>> v = new HashMap<Integer, HashMap<Integer, String>>();
    HashMap<Integer, HashMap<Integer, String>> a = new HashMap<Integer, HashMap<Integer, String>>();
    HashMap<Integer, HashMap<Integer, String>> p = new HashMap<Integer, HashMap<Integer, String>>();
    String t = "";

    //Populate Values Sub-matrix
    for(int i = 2; i < 22; i++){
      v.put(i, new HashMap<Integer, String>());
      for(int j = 0; j < dCards.length; j++){
        switch(Integer.valueOf(i)){
          case 8:
            v.get(i).put(dCards[j], "h");
            break;
    			case 9:
            if(dCards[j] > 2 && dCards[j] < 7) v.get(i).put(dCards[j], "d");
            else v.get(i).put(dCards[j], "h");
            break;
    			case 10:
            if(dCards[j] < 10) v.get(i).put(dCards[j], "d");
            else v.get(i).put(dCards[j], "h");
            break;
    			case 11:
            if(dCards[j] < 11) v.get(i).put(dCards[j], "d");
            else v.get(i).put(dCards[j], "h");
            break;
          case 12:
            if(dCards[j] > 3 && dCards[j]< 7) v.get(i).put(dCards[j], "s");
            else v.get(i).put(dCards[j], "h");
            break;
          case 13:
            if(dCards[j] < 7) v.get(i).put(dCards[j], "s");
            else v.get(i).put(dCards[j], "h");
            break;
          case 14:
            if(dCards[j] < 7) v.get(i).put(dCards[j], "s");
            else v.get(i).put(dCards[j], "h");
            break;
          case 15:
            if(dCards[j] < 7) v.get(i).put(dCards[j], "s");
            else v.get(i).put(dCards[j], "h");
            break;
          case 16:
            if(dCards[j] < 7) v.get(i).put(dCards[j], "s");
            else v.get(i).put(dCards[j], "h");
            break;
          case 17:
            v.get(i).put(dCards[j], "s");
            break;
    			default:
            if(i < 8) v.get(i).put(dCards[j], "h");
            else v.get(i).put(dCards[j], "s");
            break;
    		}
      }
    }

    //Populate Aces Sub-matrix
    for(int i = 2; i < 12; i++){
      a.put(i, new HashMap<Integer, String>());
      for(int j = 0; j < dCards.length; j++){
        switch(Integer.valueOf(i)){
    			case 2:
            if(dCards[j] > 4 && dCards[j] < 7) a.get(i).put(dCards[j], "d");
            else a.get(i).put(dCards[j], "h");
            break;
    			case 3:
            if(dCards[j] > 4 && dCards[j] < 7) a.get(i).put(dCards[j], "d");
            else a.get(i).put(dCards[j], "h");
            break;
    			case 4:
            if(dCards[j] > 3 && dCards[j] < 7) a.get(i).put(dCards[j], "d");
            else a.get(i).put(dCards[j], "h");
            break;
          case 5:
            if(dCards[j] > 3 && dCards[j] < 7) a.get(i).put(dCards[j], "d");
            else a.get(i).put(dCards[j], "h");
            break;
          case 6:
            if(dCards[j] > 2 && dCards[j] < 7) a.get(i).put(dCards[j], "d");
            else a.get(i).put(dCards[j], "h");
            break;
          case 7:
            if(dCards[j] > 2 && dCards[j] < 7) a.get(i).put(dCards[j], "d");
            else if(dCards[j] > 8) a.get(i).put(dCards[j], "h");
            else a.get(i).put(dCards[j], "s");
            break;
          case 8:
            a.get(i).put(dCards[j], "s");
            break;
          case 9:
            a.get(i).put(dCards[j], "s");
            break;
          case 10:
            a.get(i).put(dCards[j], "s");
            break;
          case 11:
            a.get(i).put(dCards[j], "spl");
            break;
    		}
      }
    }

    //Populate Pair Sub-matrix
    for(int i = 2; i < 11; i++){
      p.put(i, new HashMap<Integer, String>());
      for(int j = 2; j < 12; j++){
        switch(Integer.valueOf(i)){
    			case 2:
            if(j < 7) p.get(i).put(j, "spl");
            else p.get(i).put(j, "h");
            break;
    			case 3:
            if(j < 7) p.get(i).put(j, "spl");
            else p.get(i).put(j, "h");
            break;
    			case 4:
            if(j > 4 && j < 7) p.get(i).put(j, "spl");
            else p.get(i).put(j, "h");
            break;
          case 5:
            if(j < 10) p.get(i).put(j, "d");
            else p.get(i).put(j, "h");
            break;
          case 6:
            if(j < 7) p.get(i).put(j, "spl");
            else p.get(i).put(j, "h");
            break;
          case 7:
            if(j < 8) p.get(i).put(j, "spl");
            else if(j > 8) p.get(i).put(j, "h");
            else p.get(i).put(j, "s");
            break;
          case 8:
            p.get(i).put(j, "spl");
            break;
          case 9:
            if(j == 7 || j > 9) p.get(i).put(j, "s");
            else p.get(i).put(j, "spl");
            break;
          case 10:
            p.get(i).put(j, "s");
            break;
    		}
      }
    }

    this.dMatrix.put("Values", v);
    this.dMatrix.put("Ace", a);
    this.dMatrix.put("Pairs", p);
  }

  private int containsAce(ArrayList<Card> hand){
    for(int i = 0; i < hand.size(); i++){
      if(hand.get(i).value == 11){
        if(i == 0) return 1;
        else return 0;
      }
    }
    return -1;
  }

  private int checkDone(ArrayList<Integer> vList){
		int max = -1, min = 99;

		for(int j = 0; j < vList.size(); j++){
			if(vList.get(j) < 22 && vList.get(j) > max) max = vList.get(j);
			if(vList.get(j) < min) min = vList.get(j);
		}
		if(max == -1) return min;
		return max;
	}
}
