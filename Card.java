public class Card{
  public int suit; //Club = 0, Diamond = 1, Heart = 2, Spade = 3, joker = 4, JOKER = 5
  public int num; //A = 1, J = 11, Q = 12, K = 13, joker/JOKER = 0
  public int weight; // How powerful this card is
  private StringBuilder pic; //picture id
  private char suitname[] = {0x2663,0x2666,0x2665,0x2660,'j','J'};
  private String ptname[] = {"_A","_2","_3","_4","_5","_6","_7","_8","_9","_10","_J","_Q","_K","oker","OKER"};

  /*constructor*/
  public Card(int suit, int num, int weight){
    this.suit = suit;
    this.num = num;
    this.weight = weight;
    pic = new StringBuilder();
    pic.append(suitname[suit]);
    pic.append(ptname[num]);
  }

  @Override
  public String toString(){
    return pic.toString();
  }
}
