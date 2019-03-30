import java.io.*;
import java.util.*;

public class Calc24{
  private char operators[] = {'+','-','*','/'};
  private double objective;
  
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

  private StringBuilder attempt(Deck draws){
    int cards[] = {0,0,0,0};
    int num_expressions;
    int i,index;
    StringBuilder solution = new StringBuilder();

    //detect and remove repeatitives
    Map<Integer,Integer> map = new HashMap<Integer,Integer>();
    for(i=0;i<4;i++){
      cards[i] = draws.draw().weight;
      if(map.get(cards[i])==null){
        map.put(cards[i],1);
      }
      else{
        map.put(cards[i],map.get(cards[i])+1);
      }
    }
    //how many possible expressions?
    switch(map.size()){
    case 1:
      //aaaa, 4 of a kind
      solution.append(addOperators(cards[3],cards[3],cards[3],cards[3]));
      num_expressions = 1;
      break;
    case 2:
      boolean flag_a = false; //true:aaab, 3 of a kind/ false: aabb, 2 pairs
      index=0;
      for(Integer key : map.keySet()){
        if(map.get(key)==1){
          cards[3] = key;
          flag_a = true;
        }
        else if(map.get(key)==2){
          cards[index++] = key;
          cards[index++] = key;
        }
        else{
          cards[index++] = key;
        }
      }
      if(flag_a){
        solution.append(addOperators(cards[1],cards[1],cards[1],cards[3]));
        solution.append(addOperators(cards[1],cards[1],cards[3],cards[1]));
        solution.append(addOperators(cards[1],cards[3],cards[1],cards[1]));
        solution.append(addOperators(cards[3],cards[1],cards[1],cards[1]));
        num_expressions = 16;
      }
      else{
        solution.append(addOperators(cards[1],cards[1],cards[3],cards[3]));
        solution.append(addOperators(cards[1],cards[3],cards[1],cards[3]));
        solution.append(addOperators(cards[1],cards[3],cards[3],cards[1]));
        solution.append(addOperators(cards[3],cards[1],cards[1],cards[3]));
        solution.append(addOperators(cards[3],cards[1],cards[3],cards[1]));
        solution.append(addOperators(cards[3],cards[3],cards[1],cards[1]));
        num_expressions = 36;
      }
      break;
    case 3:
      //aabc, 1 pair
      index=0;
      for(Integer key : map.keySet()){
        if(map.get(key)==2){
          cards[2] = key;
          cards[3] = key;
        }
        else{
          cards[index++] = key;
        }
      }
      solution.append(addOperators(cards[0],cards[1],cards[3],cards[3]));
      solution.append(addOperators(cards[0],cards[3],cards[1],cards[3]));
      solution.append(addOperators(cards[0],cards[3],cards[3],cards[1]));
      solution.append(addOperators(cards[1],cards[0],cards[3],cards[3]));
      solution.append(addOperators(cards[1],cards[3],cards[0],cards[3]));
      solution.append(addOperators(cards[1],cards[3],cards[3],cards[0]));
      solution.append(addOperators(cards[3],cards[0],cards[1],cards[3]));
      solution.append(addOperators(cards[3],cards[0],cards[3],cards[1]));
      solution.append(addOperators(cards[3],cards[1],cards[0],cards[3]));
      solution.append(addOperators(cards[3],cards[1],cards[3],cards[0]));
      solution.append(addOperators(cards[3],cards[3],cards[0],cards[1]));
      solution.append(addOperators(cards[3],cards[3],cards[1],cards[0]));
      num_expressions = 96;
      break;
    default:
      //abcd, no pair
      solution.append(addOperators(cards[0],cards[1],cards[2],cards[3]));
      solution.append(addOperators(cards[0],cards[1],cards[3],cards[2]));
      solution.append(addOperators(cards[0],cards[2],cards[1],cards[3]));
      solution.append(addOperators(cards[0],cards[2],cards[3],cards[1]));
      solution.append(addOperators(cards[0],cards[3],cards[1],cards[2]));
      solution.append(addOperators(cards[0],cards[3],cards[2],cards[1]));
      solution.append(addOperators(cards[1],cards[0],cards[2],cards[3]));
      solution.append(addOperators(cards[1],cards[0],cards[3],cards[2]));
      solution.append(addOperators(cards[1],cards[2],cards[0],cards[3]));
      solution.append(addOperators(cards[1],cards[2],cards[3],cards[0]));
      solution.append(addOperators(cards[1],cards[3],cards[0],cards[2]));
      solution.append(addOperators(cards[1],cards[3],cards[2],cards[0]));
      solution.append(addOperators(cards[2],cards[0],cards[1],cards[3]));
      solution.append(addOperators(cards[2],cards[0],cards[3],cards[1]));
      solution.append(addOperators(cards[2],cards[1],cards[0],cards[3]));
      solution.append(addOperators(cards[2],cards[1],cards[3],cards[0]));
      solution.append(addOperators(cards[2],cards[3],cards[0],cards[1]));
      solution.append(addOperators(cards[2],cards[3],cards[1],cards[0]));
      solution.append(addOperators(cards[3],cards[0],cards[1],cards[2]));
      solution.append(addOperators(cards[3],cards[0],cards[2],cards[1]));
      solution.append(addOperators(cards[3],cards[1],cards[0],cards[2]));
      solution.append(addOperators(cards[3],cards[1],cards[2],cards[0]));
      solution.append(addOperators(cards[3],cards[2],cards[0],cards[1]));
      solution.append(addOperators(cards[3],cards[2],cards[1],cards[0]));
      num_expressions = 256;
      break;
    }
    return solution;
  }

  /*constructor*/
  public Calc24(){
    Card c;
    int i;
    String solution;
    Deck deck = new Deck(1,false);
    Deck draws = new Deck(0,false);
    objective = 24;
    deck.shuffle();
    for(i=0;i<4;i++){
      draws.add(deck.draw());
    }
    System.out.println("4 cards drawn:");
    System.out.println(draws.toString());

    solution = attempt(draws).toString();
    if(solution.length()<=0){
      System.out.println("No solution");
    }
    else{
      System.out.print(solution);
    }
  }
  
  /*main*/
  public static void main(String[] args){
    Calc24 a = new Calc24();
  }
}
