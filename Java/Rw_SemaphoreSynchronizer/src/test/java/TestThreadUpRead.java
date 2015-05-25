/**
 * Created by Ruben Gomes on 24/05/2015.
 */
public class TestThreadUpRead extends Thread{


    Rw_Semaphore semaphore;
    int id;

    public TestThreadUpRead(int id , Rw_Semaphore semaphore){
        this.id = id;
        this.semaphore = semaphore;
    }

    @Override
    public void run(){
        System.out.printf("Thread with id = %d call upRead \n",id);
        semaphore.upRead();
  }


}
