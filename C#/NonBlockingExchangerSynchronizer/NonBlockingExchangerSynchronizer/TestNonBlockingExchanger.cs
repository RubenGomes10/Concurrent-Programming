using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace NonBlockingExchangerSynchronizer
{
    class TestNonBlockingExchanger
    {
        static void Main(string[] args)
        {

            TestNonBlockingExchanger tests = new TestNonBlockingExchanger();

            tests.Test_Simple_Exchange_Message();
            tests.Test_Multiple_Exchange_Message();
            tests.Test_Zero_Timeout_Exchange_Message();
            tests.Test_Timeout_Exchange_Message();
            tests.Test_Interrupted_Exchange_Message();
            tests.Test_Interrupted_Rearm_Exchange_Message();

            Console.ReadKey();
        }

        public void Test_Simple_Exchange_Message()
        {
            Console.WriteLine("-------Test_Simple_Exchange_Message - BEGIN--------");
            NonBlockingExchanger<string> exchanger = new NonBlockingExchanger<string>();
            Thread t1 = new Thread(() => ThreadFunc(exchanger,1, 2000)),
                   t2 = new Thread(() => ThreadFunc(exchanger,2, 2000));

            t1.Start();
            Thread.Sleep(500);
            t2.Start();
            Thread.Sleep(500);

            Console.WriteLine("----------------------------------------------------");

        }

        public void Test_Multiple_Exchange_Message()
        {
            Console.WriteLine("-------Test_Multiple_Exchange_Message - BEGIN--------");
            NonBlockingExchanger<string> exchanger = new NonBlockingExchanger<string>();
            Thread t1 = new Thread(() => ThreadFunc(exchanger,1, 2000)),
                   t2 = new Thread(() => ThreadFunc(exchanger,2, 2000)),
                   t3 = new Thread(() => ThreadFunc(exchanger,3, 2000)),
                   t4 = new Thread(() => ThreadFunc(exchanger,4, 2000));

            t1.Start();
            Thread.Sleep(500);
            t2.Start();
            Thread.Sleep(500);
            t3.Start();
            Thread.Sleep(500);
            t4.Start();
            Thread.Sleep(500);
            
            Console.WriteLine("----------------------------------------------------");
        }

        public void Test_Zero_Timeout_Exchange_Message()
        {
            Console.WriteLine("-------Test_Zero_Timeout_Exchange_Message - BEGIN--------");
            NonBlockingExchanger<string> exchanger = new NonBlockingExchanger<string>();
            Thread t1 = new Thread(() => ThreadFunc(exchanger,1, 0)),
                   t2 = new Thread(() => ThreadFunc(exchanger,2, 2000)),
                   t3 = new Thread(() => ThreadFunc(exchanger,3, 2000));

            t1.Start();
            Thread.Sleep(500);
            t2.Start();
            Thread.Sleep(1000);
            t3.Start();
            Thread.Sleep(500);
            Console.WriteLine("----------------------------------------------------");
        }

        public void Test_Timeout_Exchange_Message()
        {
            Console.WriteLine("-------Test_Timeout_Exchange_Message - BEGIN--------");
            NonBlockingExchanger<string> exchanger = new NonBlockingExchanger<string>();
            Thread t1 = new Thread(() => ThreadFunc(exchanger,1, 1000)),
                   t2 = new Thread(() => ThreadFunc(exchanger,2, 2000)),
                   t3 = new Thread(() => ThreadFunc(exchanger,3, 2000));

            t1.Start();
            Thread.Sleep(1500);
            t2.Start();
            Thread.Sleep(500);
            t3.Start();
            Thread.Sleep(500);
            Console.WriteLine("----------------------------------------------------");
        }

        public void Test_Interrupted_Exchange_Message()
        {
            Console.WriteLine("-------Test_Interrupted_Exchange_Message - BEGIN--------");
            NonBlockingExchanger<string> exchanger = new NonBlockingExchanger<string>();
            Thread t1 = new Thread(() => ThreadFunc(exchanger,1, 1000)),
                   t2 = new Thread(() => ThreadFunc(exchanger,2, 3000)),
                   t3 = new Thread(() => ThreadFunc(exchanger,3, 3000));

            t1.Start();
            Thread.Sleep(1000);
            t1.Interrupt();
            Thread.Sleep(20);
            t2.Start();
            Thread.Sleep(500);
            t3.Start();
            Thread.Sleep(500);

            Console.WriteLine("----------------------------------------------------");
        }

        public void Test_Interrupted_Rearm_Exchange_Message()
        {
            Console.WriteLine("-------Test_Interrupted_Rearm_Exchange_Message - BEGIN--------");
            NonBlockingExchanger<string> exchanger = new NonBlockingExchanger<string>();
            Thread t1 = new Thread(() => ThreadFunc(exchanger,1, 1000)),
                   t2 = new Thread(() => ThreadFunc(exchanger,2, 3000)),
                   t3 = new Thread(() => ThreadFunc(exchanger,3, 3000));

            t1.Start();
            Thread.Sleep(500);
            t2.Start();
            Thread.Sleep(10);
            t1.Interrupt();
            Thread.Sleep(20);
            t3.Start();
            Thread.Sleep(500);
            Console.WriteLine("----------------------------------------------------");
        }



        public void ThreadFunc(NonBlockingExchanger<string> nonBlockingExchanger , int id, int timeout)
        {

            String msg = "Message " + id;
            Console.WriteLine("Thread " + id + " send " + msg);
            try
            {
                msg = nonBlockingExchanger.Exchange(msg, timeout);
            }
            catch (ThreadInterruptedException)
            {
                Console.WriteLine("Thread Interrupted Exception occurred!");
                return;
            }
            catch (TimeoutException e)
            {
                Console.WriteLine(e.Message);
                return;
            }
                Console.WriteLine("Thread " + id + " received " + msg);

        }
    }
}
