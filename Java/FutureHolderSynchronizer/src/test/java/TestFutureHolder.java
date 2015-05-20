import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by Ruben Gomes on 20/05/2015.
 */
public class TestFutureHolder {
    static Thread t1;
    static Thread t2;
    static Thread t3;
    static final FutureHolder<Integer>  futureHolder = new FutureHolder<Integer>();


    @BeforeClass
    public static void setUp(){
      t1 = new Thread(){
          @Override
          public void run(){
              Integer i = null;
              try {
                  i = futureHolder.getValue(5000);
              } catch (InterruptedException e) {
                  System.out.print(e.getMessage());
              }
              System.out.printf("value = %d",i);
          }
      };
        t2 = new Thread(){
            @Override
            public void run(){
                Integer i = null;
                try {
                    i = futureHolder.getValue(5000);
                } catch (InterruptedException e) {
                    System.out.print(e.getMessage());
                }
                System.out.printf("value = %d",i);
            }
        };

        t3 = new Thread(){
            @Override
            public void run(){
                futureHolder.setValue(10);
                System.out.println("Valor inserido com sucesso");
            }
        };
    }



    @Test
    public void TestProcessSuccess(){
        t1.start();
        t2.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        t3.start();
    }

}
