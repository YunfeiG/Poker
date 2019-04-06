import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
//@ComponentScan
public class Main{
	public static void main(String[] args) {
		//ApplicationContext context = new AnnotationConfigApplicationContext(Message.class);
		//Calc24 game_a = context.getBean(Calc24.class);
		Calc24 game_a = new Calc24();
		game_a.run();
	}
}