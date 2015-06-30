using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace ResolvedQuestions
{
    class SafeSpinLifoMsgQueue<T> where T : class
    {
        public class Node<E> where E : class
        {
            internal Node<E> next;
            internal E msg;
            internal Node(E msg)
            {
                this.msg = msg;
            }
        }

        private Node<T> top;


        public void Send(T msg)
        {
            do
            {
                Node<T> oldTop = Volatile.Read(ref top);
                Node<T> newTop = new Node<T>(msg);
                if (oldTop == Volatile.Read(ref top))
                {
                    if (CompareAndSwap(ref newTop.next, oldTop, null)) // newTop.next = top;
                    {
                        if (CompareAndSwap(ref top, oldTop, newTop))// top = newTop;
                        {
                            return;
                        }
                    }
                }
            } while (true);

        }

        public T Receive()
        {
            SpinWait sw = new SpinWait();
            do
            {
                Node<T> currTop = Volatile.Read(ref top);
                Node<T> newTop;
                if (currTop == Volatile.Read(ref top))
                {
                    if (currTop == null)
                    {
                        sw.SpinOnce();
                    }
                    else
                    {
                        newTop = currTop.next;
                        if (CompareAndSwap(ref top, currTop, newTop)) // top = currTop.next
                        {
                            return currTop.msg;
                        }
                    }
                }

            } while (true);
        }

        private bool CompareAndSwap<E>(ref E oldValue, E expected, E newValue ) where E : class{
            return expected == Interlocked.CompareExchange(ref oldValue,newValue,expected);
        }
    }
}
