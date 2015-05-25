/**
 * Created by Ruben Gomes on 24/05/2015.
 */
public class TestThreadDownRead extends Thread{

    int id;
    Rw_Semaphore semaphore;

    public TestThreadDownRead(int id , Rw_Semaphore semaphore){
        this.id = id;
        this.semaphore = semaphore;
    }

    @Override
    public void run(){
        System.out.printf("Thread with id = %d try do acquire Read access\n",id);
        try {
            semaphore.downRead();
            System.out.printf("Thread with id = %d read successfully!\n",id);
        } catch (InterruptedException e) {
            System.out.printf("Interrupted Exception occurred\n");
        }
    }

}
