using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using System.Threading;
using ExchangerSynchronizer;
using System.Diagnostics;


namespace TestExchangerSynchronizer
{
    [TestClass]
    public class TestExchanger
    {
        [TestMethod]
        public void Test_simple_exchange_message()
        {
            Exchanger<String> exchanger = new Exchanger<String>();
            Thread  t1 = new Thread( () => ThreadFunc(1, 2000, exchanger) ),
                    t2 = new Thread( () => ThreadFunc(2, 2000, exchanger) );
            
            t1.Start();
            Thread.Sleep(1000);
            t2.Start();
            Thread.Sleep(1000);
            
        }

     
        public void ThreadFunc(int id, int timeout, Exchanger<String> sync){

            Thread.Sleep(100);
            String msg = "Message " + id;
            Console.WriteLine("Thread " + id + " is sending " + msg);
            try
            {
                Thread.Sleep(100);
                msg = sync.Exchange(msg, timeout);
            }
            catch (ThreadInterruptedException)
            {
                Thread.Sleep(100);
                Console.WriteLine("Exception ocurred");
                return;
            }
            if (msg == null)
            {
                Thread.Sleep(100);
                Console.WriteLine("Timeout ocurred");
            }
            else
            {
                Thread.Sleep(100);
                Console.WriteLine("Thread " + id + " recieved " + msg);
            }
            Trace.Flush();
         }
        
    }
}

