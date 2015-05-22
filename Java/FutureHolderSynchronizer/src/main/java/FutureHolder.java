/**
 * Created by Ruben Gomes on 20/05/2015.
 */
public class FutureHolder<T> implements IFutureHolder<T> {

    private T value ;
   private final Object lock = new Object();

    @Override
    public void setValue(T value) throws IllegalStateException{
        synchronized (lock){
            if(isValueAvailable()){
                throw (new IllegalStateException("The value already inserted !"));
            }else{
                this.value = value;
                lock.notifyAll();
            }
        }
    }

    @Override
    public T getValue(long timeout) throws InterruptedException {
        synchronized (lock){
            if(isValueAvailable()) return value;
            if(timeout == 0) return null;
            long lastTime = System.currentTimeMillis();
            do{
                try{
                    lock.wait(timeout);
                } catch (InterruptedException e) {
                    if(isValueAvailable()){
                        Thread.currentThread().interrupt();
                        return value;
                    }
                   throw e;
                }
                if(isValueAvailable()){
                    return value;
                }
                if(timeout > 0) {
                    long currTime = System.currentTimeMillis();
                    long deltaTime = currTime - lastTime;
                    timeout = (deltaTime >= timeout) ? 0 : timeout - deltaTime;
                }
            }while(timeout > 0);
            return null;
        }

    }

    @Override
    public boolean isValueAvailable() {
        return this.value != null;
    }
}
