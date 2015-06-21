package concurrentQueue;

/**
 * Created by Ruben Gomes on 21/06/2015.
 */
public interface IConcurrentQueue<T> {

        void put(T elem);
        boolean isEmpty();
        T tryTake();

}
