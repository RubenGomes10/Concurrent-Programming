using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace NonBlockingConcurrentQueue
{
    public class Node<T>
    {
        internal T elem;
        internal Node<T> next;

        public Node(T elem)
        {
            this.elem = elem;

        }
    }
   

    class ConcurrentQueue<T> : IConcurrentQueue<T>
    {
      

        public  static Node<T> dummy = new Node<T>(default(T));
        private Node<T> head = dummy;
        private Node<T> tail = dummy;

        public void Put(T elem)
        {
            Node<T> newNode = new Node<T>(elem);
            do
            {
                Node<T> currTail = Volatile.Read(ref tail); //atomicity guaranted
                Node<T> nextTail = currTail.next;

                if (currTail == Volatile.Read(ref tail))
                {
                    if (nextTail != null)
                    {
                        CompareAndSwap(ref tail, currTail, nextTail);
                    }
                    else
                    {
                        if (CompareAndSwap(ref currTail.next, nextTail, newNode))
                        {
                            CompareAndSwap(ref tail, currTail, newNode);
                            return;
                        }
                    }
                }

            } while (true);
        }

        public bool IsEmpty()
        {
            return Volatile.Read(ref head) == Volatile.Read(ref tail);//atomicity guaranted
        }

        public T TryTake()
        {
            do
            {
                if (IsEmpty()) return default(T);
                Node<T> currHead = Volatile.Read(ref head); //atomicity guaranted
                Node<T> toRemove = currHead.next;
                if (toRemove == Volatile.Read(ref head).next)
                {
                    if (CompareAndSwap(ref currHead.next, toRemove, toRemove.next))
                    {
                        return toRemove.elem;
                    }
                }

            } while (true);
        }

        private static bool CompareAndSwap<E>(ref E obj, E expected, E newVal) where E : class // atomicity guaranted for swap values
        {
            return expected == Interlocked.CompareExchange(ref obj, newVal, expected);
        }
    }
}
