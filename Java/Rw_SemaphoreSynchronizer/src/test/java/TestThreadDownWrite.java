/**
 * Created by Ruben Gomes on 24/05/2015.
 */
public class TestThreadDownWrite extends Thread{

    int id;
    Rw_Semaphore semaphore;

    public TestThreadDownWrite( int id , Rw_Semaphore semaphore){
        this.id = id;
        this.semaphore = semaphore;
    }

    @Override
    public void run(){
        System.out.printf("Thread with id = %d try to acquire writeAccess\n",id);
        try {
            semaphore.downWrite();
        } catch (InterruptedException e) {
            System.out.printf("Interrupted Exception occurred! \n");
        }
    }
}
