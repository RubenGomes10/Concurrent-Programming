/**
 * Created by Ruben Gomes on 21/05/2015.
 */
public interface IExchanger<T> {

    T exchange(T myMsg, long timeout) throws InterruptedException;
}
