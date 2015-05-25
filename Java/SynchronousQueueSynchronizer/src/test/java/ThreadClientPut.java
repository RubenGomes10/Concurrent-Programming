/**
 * Created by Ruben Gomes on 23/05/2015.
 */
public class ThreadClientPut<T> extends Thread{

    ISynchronousQueue<T> queue;
    int id;


    public ThreadClientPut(int id, ISynchronousQueue<T> queue){
        this.id = id;
        this.queue = queue;
    }

    @Override
    public void run(){
        T msg = (T) ("Message " + id);
        System.out.println("Client Thread " + id + " put message : " + msg);
        try
        {
            queue.put(msg);
            System.out.println("Client Thread " + id + " put message successfully");
        }
        catch (InterruptedException e)
        {
            System.out.println("Client Thread " + id + " Interrupted");
        }
    }
}
