/**
 * Created by Ruben Gomes on 20/05/2015.
 */
public interface IFutureHolder<T> {

    void setValue(T value);
    T getValue(long timeout) throws InterruptedException;
    boolean isValueAvailable();

}
