/*
  Not my work, please refer to
  https://blog.csdn.net/moon_1991/article/details/80947858
*/

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
public class Calculator {
  private static final Pattern EXPRESSION_PATTERN = Pattern.compile("[0-9\\.+-/*()= ]+");
  private static final Map<String, Integer> OPT_PRIORITY_MAP = new HashMap<String, Integer>(){
    private static final long serialVersionUID = 6968472606692771458L;
    {
      put("(", 0);
      put("+", 2);
      put("-", 2);
      put("*", 3);
      put("/", 3);
      put(")", 7);
      put("=", 20);
    }
  };

  public static double executeExpression(String expression){
    //no empty expression
    if (null == expression || "".equals(expression.trim())){
      throw new IllegalArgumentException();
    }
    //validate expression
    Matcher matcher = EXPRESSION_PATTERN.matcher(expression);
    if(!matcher.matches()){
      throw new IllegalArgumentException();
    }
    Stack<String> optStack = new Stack<String>(); //stack of operators
    Stack<BigDecimal> numStack = new Stack<BigDecimal>(); // stack of numbers, use BigDecimal for more precision
    StringBuilder curNumBuilder = new StringBuilder(16); //building numbers with many digits

    for (int i = 0; i < expression.length(); i++){
      char c = expression.charAt(i);
        if(c != ' ') {
          if((c >= '0' && c <= '9') || c == '.'){
             curNumBuilder.append(c);
          }
          else{
            if(curNumBuilder.length() > 0){
              numStack.push(new BigDecimal(curNumBuilder.toString()));
              curNumBuilder.delete(0, curNumBuilder.length());
            }
            String curOpt = String.valueOf(c);
            if (optStack.empty()){
              optStack.push(curOpt);
            }
            else{
              if(curOpt.equals("(")){
                optStack.push(curOpt);
            }
            else if(curOpt.equals(")")){
              directCalc(optStack, numStack, true);
            }
            else if(curOpt.equals("=")){
              directCalc(optStack, numStack, false);
              return numStack.pop().doubleValue();
            }
            else{
              compareAndCalc(optStack, numStack, curOpt);
            }
          }
        }
      }
    }   
    if (curNumBuilder.length()>0){
      //if the expression does not end with '='
      numStack.push(new BigDecimal(curNumBuilder.toString()));
    }
    directCalc(optStack, numStack, false);
    return numStack.pop().doubleValue();
  }
    
  //recursive calculation with considering priorities
  public static void compareAndCalc(Stack<String> optStack, Stack<BigDecimal> numStack, String curOpt){
    //compare operators
    String peekOpt = optStack.peek();
    int priority = getPriority(peekOpt, curOpt);
    if(priority == -1 || priority == 0){
      String opt = optStack.pop();
      BigDecimal num2 = numStack.pop();
      BigDecimal num1 = numStack.pop();
      BigDecimal bigDecimal = floatingPointCalc(opt, num1, num2);
            
      numStack.push(bigDecimal);

      if(optStack.empty()){
        optStack.push(curOpt);
      }
      else{
        compareAndCalc(optStack, numStack, curOpt);
      }
    }
    else{
      optStack.push(curOpt);
    }
  }
    
    /* right bracket and equator */
  public static void directCalc(Stack<String> optStack, Stack<BigDecimal> numStack, boolean isBracket){
    String opt = optStack.pop();
    BigDecimal num2 = numStack.pop();
    BigDecimal num1 = numStack.pop();
    BigDecimal bigDecimal = floatingPointCalc(opt, num1, num2);

    numStack.push(bigDecimal);
        
    if(isBracket){
      if ("(".equals(optStack.peek())){
        optStack.pop();
      }
      else{
        directCalc(optStack, numStack, isBracket);
      }
    }
    else{
      if(!optStack.empty()){
        directCalc(optStack, numStack, isBracket);
      }
    }
  }
  
  public static BigDecimal floatingPointCalc(String opt, BigDecimal bigDecimal1, 
    BigDecimal bigDecimal2) {
    BigDecimal resultBigDecimal = new BigDecimal(0);
    switch (opt.charAt(0)) {
    case '+':
      resultBigDecimal = bigDecimal1.add(bigDecimal2);
      break;
    case '-':
      resultBigDecimal = bigDecimal1.subtract(bigDecimal2);
      break;
    case '*':
      resultBigDecimal = bigDecimal1.multiply(bigDecimal2);
      break;
    case '/':
      resultBigDecimal = bigDecimal1.divide(bigDecimal2, 10, BigDecimal.ROUND_HALF_DOWN);
      break;
    default:
      break;
    }
    return resultBigDecimal;
  }

  public static int getPriority(String opt1, String opt2) {
    int priority = OPT_PRIORITY_MAP.get(opt2) - OPT_PRIORITY_MAP.get(opt1);
    return priority;
  }
/*
  private static boolean isDoubleEquals (double value1, double value2) {
    System.out.println("Correct answer=" + value1 + ", Practical answer=" + value2);
    return Math.abs(value1 - value2) <= 0.0001;
  }*/
  
}
