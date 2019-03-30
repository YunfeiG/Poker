import java.util.Vector;
public class Deck{
  private Vector<Card> cards; 
  private int num;

  /*constructor*/
  public Deck(){
    cards = new Vector<Card>();
    reload(0,false);
  }

  public Deck(int sets, boolean joker){
    cards = new Vector<Card>();
    reload(sets,joker);
  }
  
  public Card draw(){
    num -= 1;
    return cards.remove(0);
  }

  public void discard(){
    cards.clear();
    num = 0;
  }

  public void reload(int sets, boolean joker){
    if(sets<=0) return;
    cards.clear();
    int i,j,k;   
    for(k=0;k<sets;k++){
      for(i=0;i<4;i++){
          for(j=0;j<13;j++){
           cards.add(new Card(i,j,j+1));
         }
      }
      num += 52;
      if(joker){
        cards.add(new Card(4,13,99)); //Grey joker
        cards.add(new Card(5,14,99)); //Colored JOKER
        num += 2;
      }
    }
  }

  public int remains(){
    return num;
  }

  public boolean isEmpty(){
    return (num==0);
  }

  public void add(Card c){
    cards.add(c);
    num += 1;
  }

  public void shuffle(){
    if(num<2) return;
    int i,a;
    Card temp;
    for(i=num-1;i>0;i--){
      a = (int) (Math.random()*num);
      temp = cards.get(i);
      cards.set(i,cards.get(a));
      cards.set(a,temp);
    }
  }

  @Override
  public String toString(){
    StringBuilder text = new StringBuilder();
    int i;
    for(i=0;i<num;i++){
      text.append(cards.get(i).toString());
      text.append(" ");
    }
    return text.toString();
  }
}
