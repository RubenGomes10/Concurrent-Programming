import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Ruben Gomes on 20/06/2015.
 */
public class NonBlockingFutureHolder<T> {

    private final AtomicReference<T> valueRef = new AtomicReference<T>(null);

    public void setValue(T value) throws IllegalStateException{
        do {
            T old = valueRef.get();
            if (isValueAvailable()) { // if value already inserted throw IllegalStateException
                throw new IllegalStateException("The value already inserted !");
            } else { // if not , set value
                if(valueRef.compareAndSet(old, value)){
                    return;
                }
            }
        }while(true);
    }

    public T getValue(long timeout) throws TimeoutException {

        if (isValueAvailable()) return valueRef.get(); //if value is available return immediately
        if (timeout == 0) throw new TimeoutException("Timeout is 0!");
        do {
            long start = System.currentTimeMillis(); // get start time
            // if value is available return immediately , if not wait´s until
            // timeout expires or value it`s available
            if (isValueAvailable()) return valueRef.get();
            long deltaTime = System.currentTimeMillis() - start;
            timeout = (deltaTime >= timeout) ? 0 : timeout - deltaTime;
            if (timeout == 0) throw  new TimeoutException("Timeout Expired!");
        } while (true);
    }


    public boolean isValueAvailable() {
        return valueRef.get() != null;
    }
}
