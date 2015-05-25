/**
 * Created by Ruben Gomes on 23/05/2015.
 */
public interface ISynchronousQueue<T> {

    void put(T msg) throws InterruptedException;

    T take() throws InterruptedException;

}
