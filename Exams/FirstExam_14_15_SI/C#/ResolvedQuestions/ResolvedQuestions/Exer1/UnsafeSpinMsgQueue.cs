using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace ResolvedQuestions
{
    

    class UnsafeSpinMsgQueue<T>
    {
        public class Node<E>
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
            Node<T> node = new Node<T>(msg);
            node.next = top;
            top = node;
        }

        public T Receive() 
        {
            SpinWait sw = new SpinWait();
            Node<T> oldTop;
            while ((oldTop = top) == null)
                sw.SpinOnce();
            top = oldTop.next;
            return oldTop.msg;
        }
     

    }
}
