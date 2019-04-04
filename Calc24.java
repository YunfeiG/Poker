import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calc24{
  private static final char operators[] = {'+','-','*','/'};
  private static double objective;
  private Calculator calculator; 

  private Vector<Card> deck;
  private Vector<Card> draws;
  /* calculation of 2 numbers */
  private double compute(double a, double b, char operator){
    switch(operator){
    case '+': return a+b;
    case '-': return a-b;
    case '*': return a*b;
    default:
      if(b==0) return Double.NaN;
      return a/b;
    }
  }
  
  //apply operators +-*/ to four ordered integers
  private StringBuilder addOperators(int a, int b, int c, int d){
    StringBuilder expressions = new StringBuilder();
    
    char operator1,operator2,operator3;
    double result_ab,result_bc,result_cd;
    double result_ab_c,result_a_bc,result_bc_d,result_b_cd,result_ab_cd;
    double result;
    int i,j,k;
    //$ <- any operators
    //a$b, b$c, c$d
    for(i=0;i<4;i++){
      operator1=operators[i];
      result_ab = compute((double) a,(double) b,operator1);
      result_bc = compute((double) b,(double) c,operator1);
      result_cd = compute((double) c,(double) d,operator1);
      for(j=0;j<4;j++){
        //(a$b)$c, a$(b$c), (b$c)$d, b($c$d)
        operator2=operators[j];
        result_ab_c = compute(result_ab,(double) c,operator2);
        result_a_bc = compute((double) a,result_bc,operator2);
        result_bc_d = compute(result_bc,(double) d,operator2);
        result_b_cd = compute((double) b,result_cd,operator2);
        result_ab_cd = compute((double) c,(double) d,operator2);
          for(k=0;k<4;k++){
            operator3=operators[k];
            //((a$b)$c)$d
            result = compute(result_ab_c,(double) d,operator3);
            if(result==objective) expressions.append("(("+a+operator1+b+")"+operator2+c+")"+operator3+d+"="+result+"\n");
            //(a$(b$c))$d
            result = compute(result_a_bc,(double) d,operator3);
            if(result==objective) expressions.append("("+a+operator2+"("+b+operator1+c+"))"+operator3+d+"="+result+"\n");
            //a$((b$c)$d)
            result = compute((double) a,result_bc_d,operator3);
            if(result==objective) expressions.append(""+a+operator3+"(("+b+operator1+c+")"+operator2+d+")="+result+"\n");
            //a$(b$(c$d))
            result = compute((double) a,result_b_cd,operator3);
            if(result==objective) expressions.append(""+a+operator3+"("+b+operator2+"("+c+operator1+d+"))="+result+"\n");
            //(a$b)$(c$d)
            result = compute(result_ab,result_ab_cd,operator3);
            if(result==objective) expressions.append("("+a+operator1+b+")"+operator3+"("+c+operator2+d+")="+result+"\n");
          }
       }
    }
    return expressions;
  }

  private StringBuilder attempt(int[] weights){
    int num_expressions;
    int i,index;
    StringBuilder solution = new StringBuilder();

    //detect and remove repeatitives
    Map<Integer,Integer> map = new HashMap<Integer,Integer>();
    for(i=0;i<4;i++){
      if(map.get(weights[i])==null){
        map.put(weights[i],1);
      }
      else{
        map.put(weights[i],map.get(weights[i])+1);
      }
    }
    //how many possible expressions?
    switch(map.size()){
    case 1:
      //aaaa, 4 of a kind
      solution.append(addOperators(weights[3],weights[3],weights[3],weights[3]));
      num_expressions = 1;
      break;
    case 2:
      boolean flag_a = false; //true:aaab, 3 of a kind/ false: aabb, 2 pairs
      index=0;
      for(Integer key : map.keySet()){
        if(map.get(key)==1){
          weights[3] = key;
          flag_a = true;
        }
        else if(map.get(key)==2){
          weights[index++] = key;
          weights[index++] = key;
        }
        else{
          weights[index++] = key;
        }
      }
      if(flag_a){
        solution.append(addOperators(weights[1],weights[1],weights[1],weights[3]));
        solution.append(addOperators(weights[1],weights[1],weights[3],weights[1]));
        solution.append(addOperators(weights[1],weights[3],weights[1],weights[1]));
        solution.append(addOperators(weights[3],weights[1],weights[1],weights[1]));
        num_expressions = 16;
      }
      else{
        solution.append(addOperators(weights[1],weights[1],weights[3],weights[3]));
        solution.append(addOperators(weights[1],weights[3],weights[1],weights[3]));
        solution.append(addOperators(weights[1],weights[3],weights[3],weights[1]));
        solution.append(addOperators(weights[3],weights[1],weights[1],weights[3]));
        solution.append(addOperators(weights[3],weights[1],weights[3],weights[1]));
        solution.append(addOperators(weights[3],weights[3],weights[1],weights[1]));
        num_expressions = 36;
      }
      break;
    case 3:
      //aabc, 1 pair
      index=0;
      for(Integer key : map.keySet()){
        if(map.get(key)==2){
          weights[2] = key;
          weights[3] = key;
        }
        else{
          weights[index++] = key;
        }
      }
      solution.append(addOperators(weights[0],weights[1],weights[3],weights[3]));
      solution.append(addOperators(weights[0],weights[3],weights[1],weights[3]));
      solution.append(addOperators(weights[0],weights[3],weights[3],weights[1]));
      solution.append(addOperators(weights[1],weights[0],weights[3],weights[3]));
      solution.append(addOperators(weights[1],weights[3],weights[0],weights[3]));
      solution.append(addOperators(weights[1],weights[3],weights[3],weights[0]));
      solution.append(addOperators(weights[3],weights[0],weights[1],weights[3]));
      solution.append(addOperators(weights[3],weights[0],weights[3],weights[1]));
      solution.append(addOperators(weights[3],weights[1],weights[0],weights[3]));
      solution.append(addOperators(weights[3],weights[1],weights[3],weights[0]));
      solution.append(addOperators(weights[3],weights[3],weights[0],weights[1]));
      solution.append(addOperators(weights[3],weights[3],weights[1],weights[0]));
      num_expressions = 96;
      break;
    default:
      //abcd, no pair
      solution.append(addOperators(weights[0],weights[1],weights[2],weights[3]));
      solution.append(addOperators(weights[0],weights[1],weights[3],weights[2]));
      solution.append(addOperators(weights[0],weights[2],weights[1],weights[3]));
      solution.append(addOperators(weights[0],weights[2],weights[3],weights[1]));
      solution.append(addOperators(weights[0],weights[3],weights[1],weights[2]));
      solution.append(addOperators(weights[0],weights[3],weights[2],weights[1]));
      solution.append(addOperators(weights[1],weights[0],weights[2],weights[3]));
      solution.append(addOperators(weights[1],weights[0],weights[3],weights[2]));
      solution.append(addOperators(weights[1],weights[2],weights[0],weights[3]));
      solution.append(addOperators(weights[1],weights[2],weights[3],weights[0]));
      solution.append(addOperators(weights[1],weights[3],weights[0],weights[2]));
      solution.append(addOperators(weights[1],weights[3],weights[2],weights[0]));
      solution.append(addOperators(weights[2],weights[0],weights[1],weights[3]));
      solution.append(addOperators(weights[2],weights[0],weights[3],weights[1]));
      solution.append(addOperators(weights[2],weights[1],weights[0],weights[3]));
      solution.append(addOperators(weights[2],weights[1],weights[3],weights[0]));
      solution.append(addOperators(weights[2],weights[3],weights[0],weights[1]));
      solution.append(addOperators(weights[2],weights[3],weights[1],weights[0]));
      solution.append(addOperators(weights[3],weights[0],weights[1],weights[2]));
      solution.append(addOperators(weights[3],weights[0],weights[2],weights[1]));
      solution.append(addOperators(weights[3],weights[1],weights[0],weights[2]));
      solution.append(addOperators(weights[3],weights[1],weights[2],weights[0]));
      solution.append(addOperators(weights[3],weights[2],weights[0],weights[1]));
      solution.append(addOperators(weights[3],weights[2],weights[1],weights[0]));
      num_expressions = 256;
      break;
    }
    return solution;
  }
  
  /*validate user's answer*/
  /*valid inputs:
    integer operator integer operator integer operator integer
    no negative integers, no float numbers
    1st operator must be behind 1st integer
    all integers must be either in 1 or 2 digits
    */
  private boolean validate(String expression, int[] weights){
    int i;
    //make sure user input same 4 integers as card drawn
    String buffs[] = expression.split("[^0-9]+");
    Set<Integer> set1 = new HashSet<Integer>();
    Set<Integer> set2 = new HashSet<Integer>();
    if(buffs==null) return false;
    if(buffs[0].equals("")){
      if(buffs.length!=5) return false;
      buffs[0] = buffs[1];
      buffs[1] = buffs[2];
      buffs[2] = buffs[3];
      buffs[3] = buffs[4];
    }
    else{
      if(buffs.length!=4) return false;
    }
    
    for(i=0;i<4;i++){
      set1.add(weights[i]);
      set2.add(Integer.parseInt(buffs[i]));
    }
    for(i=0;i<4;i++){
      if(!set1.contains(Integer.parseInt(buffs[i]))) return false;
      if(!set2.contains(weights[i])) return false;
    }
    //make sure user input 3 operators
    buffs = expression.split("[^\\+^\\-^\\*^\\/]+");
    if(buffs.length!=4) return false;
    Set<Character> set3 = new HashSet<Character>();
    for(i=0;i<4;i++) set3.add(operators[i]);
    for(i=1;i<4;i++){
      if(!set3.contains(buffs[i].charAt(0))) return false;
    }
    return true;
  }

  /*constructor*/
  public Calc24(){
    Card c;
    int i;
    String solution;
    String input;
    BufferedReader reader;
    
    deck = new Vector<Card>();
    reload(1,false);
    draws = new Vector<Card>();
    int weights[] = {0,0,0,0};
    objective = 24;
    shuffle(deck);
    for(i=0;i<4;i++){
      c = deck.remove(1);
      weights[i] = c.weight;
      draws.add(c);
    }
    System.out.println("4 cards drawn:");
    System.out.println(draws.toString());

    solution = attempt(weights).toString();
    if(solution.length()<=0){
      System.out.println("No solution");
    }
    else{
      System.out.println("Can you get 24?");
      reader = new BufferedReader(new InputStreamReader(System.in));
      try{
        input = reader.readLine();
        if(validate(input,weights) && Calculator.executeExpression(input) == objective){
          System.out.println("Correct!");
        }
        else{
          System.out.println("Wrong. Here are solutions.");
          System.out.print(solution);
        }
      }
      catch(Exception e){
        System.err.println("Invalid input. Here are solutions");
        System.out.print(solution);
      }
    }
  }

  public void reload(int sets, boolean joker){
    if(sets<=0) return;
    deck.clear();
    int i,j,k;  
    for(k=0;k<sets;k++){
      for(i=0;i<4;i++){
          for(j=0;j<13;j++){
           deck.add(new Card(i,j,j+1));
         }
      }
      if(joker){
        deck.add(new Card(4,13,99)); //Grey joker
        deck.add(new Card(5,14,99)); //Colored JOKER
      }
    }
  }
  
  public void shuffle(Vector<Card> cards){
    int num=cards.size();
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
  
  public void discard(){
    deck.clear();
    draws.clear();
  }
  
  /*main*/
  public static void main(String[] args){
    Calc24 a = new Calc24();
  }
}
