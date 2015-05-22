import java.util.function.IntPredicate;

/**
 * Created by Ruben Gomes on 21/05/2015.
 */
public interface IMessageQueue<T> {

    void send(T msg, int id);
    T receive(IntPredicate selector,long timeout) throws InterruptedException;



}
