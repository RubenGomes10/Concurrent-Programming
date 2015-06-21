package concurrentQueue;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * Created by Ruben Gomes on 21/06/2015.
 */
public class ConcurrentQueue<T> implements IConcurrentQueue<T> {


    public static class Node<T>{
        public T elem;
        public volatile Node<T> next;

        public Node(T elem){
            this.elem = elem;
        }
    }

    public Node<T> dummy = new Node<T>(null);
    private final AtomicReference<Node<T>>
            head = new AtomicReference<Node<T>>(dummy),
            tail = new AtomicReference<Node<T>>(dummy);

    private static AtomicReferenceFieldUpdater<Node,Node> updater =
            AtomicReferenceFieldUpdater.newUpdater(Node.class,Node.class,"next");


    @Override
    public void put(T elem) {
        Node<T> newNode = new Node<T>(elem);
        do{
            Node<T> currTail = tail.get();
            Node<T> nextTail = currTail.next;
            if(currTail == tail.get()){
                if(nextTail != null){
                    tail.compareAndSet(currTail,nextTail); // tail.next = nextTail;
                }else{
                    if(updater.compareAndSet(currTail,nextTail,newNode)){ // currTail.next = newNode;
                        //tail.compareAndSet(nextTail,newNode); // tail.next = newNode;
                        if(tail.compareAndSet(currTail,newNode)){ // currTail = newNode
                            currTail.next = newNode;// currTail.next = newNode;
                        }

                        return;
                    }
                }
            }

        }while(true);
    }

    @Override
    public boolean isEmpty() {
        return head.get() == tail.get();
    }

    @Override
    public T tryTake() {
        do{
            if(isEmpty()) return null;
            Node<T> currHead = head.get();
            Node<T> toRemove = currHead.next;
            if(toRemove == head.get().next){
                if(updater.compareAndSet(currHead,toRemove,toRemove.next)){// currHead.next = toRemove.next;
                    return toRemove.elem;
                }
            }
        }while(true);
    }
}
