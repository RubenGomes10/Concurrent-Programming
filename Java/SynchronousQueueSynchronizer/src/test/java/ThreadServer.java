/**
 * Created by Ruben Gomes on 23/05/2015.
 */
public class ThreadServer<T> extends Thread{

    SynchronousQueue<T> queue;

    public ThreadServer(ISynchronousQueue<T> queue){
        this.queue = (SynchronousQueue<T>) queue;
    }

    @Override
    public void run(){
        T msg;
        try
        {

            msg = queue.take();
            System.out.printf("Server Thread with id %d received message : %s \n",queue.threadServer.id,msg);

        }
        catch (InterruptedException e)
        {
            System.out.printf("Server Thread with id %d interrupted \n",queue.threadServer.id);
        }


    }


}
