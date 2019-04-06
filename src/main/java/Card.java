public class Card{
  public int suit; //Diamond = 0, Club = 1, Heart = 2, Spade = 3, joker = 4, JOKER = 5
  public int num; //A = 1, J = 11, Q = 12, K = 13, joker/JOKER = 0
  public int weight; // How powerful this card is
  private StringBuilder pic; //picture id
  private char suitname[] = {0x2666,0x2663,0x2665,0x2660,'j','J'};
  private String ptname[] = {" A"," 2"," 3"," 4"," 5"," 6"," 7"," 8"," 9"," 10"," J"," Q"," K","oker","OKER"};

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
