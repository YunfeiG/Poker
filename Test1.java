import java.io.*;

public class Test1{
  public static void main(String[] args){
    Deck d = new Deck(1,true);
    System.out.println("Before shuffle");
    System.out.println(d.toString());
    d.shuffle();
    System.out.println("After shuffle");
    System.out.println(d.toString());
  }
}
