using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace NonBlockingConcurrentQueue
{
    class TestConcurrentQueue
    {
        static void Main(string[] args)
        {

            TestConcurrentQueue tests = new TestConcurrentQueue();
            //tests.Test_Simple_Put_And_Take();
            //tests.Test_Take_On_Empty_Queue();
            //tests.Test_Multiple_Queue_Get_And_Put();

            Console.ReadKey();
        }
    

     
        public void Test_Simple_Put_And_Take()
        {
            Console.WriteLine("-------Test_Simple_Put_And_Take - BEGIN--------");
            ConcurrentQueue<string> queue = new ConcurrentQueue<string>();
            Thread t1 = new Thread(() => PutThread(queue,1, "1"));
            Thread t2 = new Thread(() => PutThread(queue,2, "2"));
            Thread t3 = new Thread(() => PutThread(queue,3, "3"));
            Thread t4 = new Thread(() => TakeThread(queue,1));

            t1.Start();
            Thread.Sleep(500);
            t2.Start();
            Thread.Sleep(500);
            t3.Start();
            Thread.Sleep(500);
            t4.Start();
            Thread.Sleep(500);

            Node<string> node = ConcurrentQueue<string>.dummy.next;
            while (node != null)
            {
                Console.WriteLine(node.elem);
                node = Volatile.Read(ref node).next;
            }
            Console.WriteLine("----------------------------------------------------");
        }

        public void Test_Take_On_Empty_Queue()
        {
            Console.WriteLine("-------Test_Take_On_Empty_Queue() - BEGIN--------");
            ConcurrentQueue<string> queue = new ConcurrentQueue<string>();
            
            Thread t1 = new Thread(() => TakeThread(queue,1));

            t1.Start();
            Thread.Sleep(500);
            
            Node<string> node = ConcurrentQueue<string>.dummy.next;
            while (node != null)
            {
                Console.WriteLine(node.elem);
                node = Volatile.Read(ref node.next);
            }
            Console.WriteLine("----------------------------------------------------");
        }


        public void Test_Multiple_Queue_Get_And_Put()  {
        Console.WriteLine("-------Test_Multiple_Queue_Get_And_Put - BEGIN--------");
        
            ConcurrentQueue<String> queue = new ConcurrentQueue<String>();
        Thread  t1 = new Thread(() => PutThread(queue,1,"1")),
                t2 = new Thread(() => PutThread(queue,2,"2")),
                t3 = new Thread(() => PutThread(queue,3,"3")),
                t4 = new Thread(() => PutThread(queue,4,"4"));
        Thread  t5 = new Thread(() => TakeThread(queue,1)),
                t6 = new Thread(() => TakeThread(queue,2));

        t1.Start();
        Thread.Sleep(500);
        t2.Start();
        Thread.Sleep(500);
        t3.Start();
        Thread.Sleep(500);
        t5.Start();
        Thread.Sleep(500);
        t6.Start();
        Thread.Sleep(500);
        t4.Start();
        Thread.Sleep(500);


        Node<string> node = ConcurrentQueue<string>.dummy.next;
            while (node != null)
            {
                Console.WriteLine(node.elem);
                node = Volatile.Read(ref node).next;
            }
        Console.WriteLine("----------------------------------------------------");
    }

        //Delegates
        public void PutThread(ConcurrentQueue<string> queue,int id, string value)
        {
            if (queue.IsEmpty())
                Console.WriteLine("queue is empty in put method!");
            Console.WriteLine("thread with id {0} put value = {1} in queue", id, value);
            queue.Put(value);
            

        }

        public void TakeThread(ConcurrentQueue<string> queue,int id)
        {
            string val = queue.TryTake();
            if (val == null)
                Console.WriteLine("queue is empty in take method");
            else Console.WriteLine("Thread wih id {0} took value = {1} from queue ", id, val);
        }

        
        
    }
}
