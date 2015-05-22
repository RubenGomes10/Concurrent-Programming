using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading;


namespace ExchangerSynchronizer
{
    class TestExchanger
    {
        public static void Main()
        {
            TestExchanger tests = new TestExchanger();
            
            //tests.Test_simple_exchange_message();
            //tests.Test_multiple_exchange_Message();
            //tests.Test_zero_timeout_exchange_message();
            //tests.Test_timeout_exchange_message();
            //tests.Test_interrupted_exchange_message();
            //tests.Test_interrupted_rearm_exchange_message();

            Console.ReadKey();
        }


        public void Test_simple_exchange_message()
        {
            Exchanger<String> exchanger = new Exchanger<String>();
            Thread t1 = new Thread(() => ThreadFunc(1, 4000, exchanger)),
                    t2 = new Thread(() => ThreadFunc(2, 4000, exchanger));

            t1.Start();
            Thread.Sleep(2000);
            t2.Start();
            Thread.Sleep(2000);

        }

        public void Test_multiple_exchange_Message()
        {

            Exchanger<String> exchanger = new Exchanger<String>();
            Thread t1 = new Thread(() => ThreadFunc(1, 4000, exchanger)),
                   t2 = new Thread(() => ThreadFunc(2, 4000, exchanger)),
                   t3 = new Thread(() => ThreadFunc(3, 4000, exchanger)),
                   t4 = new Thread(() => ThreadFunc(4, 4000, exchanger));

            t1.Start();
            Thread.Sleep(2000);
            t2.Start();
            Thread.Sleep(2000);
            t3.Start();
            Thread.Sleep(2000);
            t4.Start();
            Thread.Sleep(2000);
        }

        public void Test_zero_timeout_exchange_message()
        {
            Exchanger<String> exchanger = new Exchanger<String>();
            Thread t1 = new Thread(() => ThreadFunc(1, 0, exchanger)),
                   t2 = new Thread(() => ThreadFunc(2, 4000, exchanger)),
                   t3 = new Thread(() => ThreadFunc(3, 4000, exchanger));
             
            t1.Start();
            Thread.Sleep(2000);
            t2.Start();
            Thread.Sleep(2000);
            t3.Start();
        }

        public void Test_timeout_exchange_message()
        {
            Exchanger<String> exchanger = new Exchanger<String>();
            Thread t1 = new Thread(() => ThreadFunc(1, 3000, exchanger)),
                   t2 = new Thread(() => ThreadFunc(2, 4000, exchanger)),
                   t3 = new Thread(() => ThreadFunc(3, 4000, exchanger));

            t1.Start();
            Thread.Sleep(3500);
            t2.Start();
            Thread.Sleep(1000);
            t3.Start();
            Thread.Sleep(1000);
        }

        public void Test_interrupted_exchange_message()
        {
            Exchanger<String> exchanger = new Exchanger<String>();
            Thread t1 = new Thread(() => ThreadFunc(1, 2000, exchanger)),
                   t2 = new Thread(() => ThreadFunc(2, 4000, exchanger)),
                   t3 = new Thread(() => ThreadFunc(3, 4000, exchanger));

            t1.Start();
            Thread.Sleep(1000);
            t1.Interrupt();
            Thread.Sleep(10);
            t2.Start();
            Thread.Sleep(1000);
            t3.Start();
            Thread.Sleep(1000);
        }

        public void Test_interrupted_rearm_exchange_message()
        {
            Exchanger<String> exchanger = new Exchanger<String>();
            Thread t1 = new Thread(() => ThreadFunc(1, 2000, exchanger)),
                   t2 = new Thread(() => ThreadFunc(2, 4000, exchanger)),
                   t3 = new Thread(() => ThreadFunc(3, 4000, exchanger));

            t1.Start();
            Thread.Sleep(1000);
            t2.Start();
            Thread.Sleep(5);
            t1.Interrupt();
            Thread.Sleep(10);
            t3.Start();
            Thread.Sleep(1000);
        }



        public void ThreadFunc(int id, int timeout, Exchanger<String> sync)
        {

            String msg = "Message " + id;
            Console.WriteLine("Thread " + id + " send " + msg);
            try
            {
                msg = sync.Exchange(msg, timeout);
            }
            catch (ThreadInterruptedException)
            {
                Console.WriteLine("Thread Interrupted Exception occurred!");
                return;
            }
            if (msg == null)
                Console.WriteLine("Timeout occurred");
            else
                Console.WriteLine("Thread " + id + " received " + msg);

        }
    }
}
