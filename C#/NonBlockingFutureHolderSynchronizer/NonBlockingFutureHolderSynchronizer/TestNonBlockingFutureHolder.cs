using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;


namespace NonBlockingFutureHolderSynchronizer
{
    class TestNonBlockingFutureHolder
    {

        private static bool GET_VALUE = true;
        private static bool SET_VALUE = false;

        public static void Main()
        {
            Test_simple_thread_waiting_for_integer_value() ;
            Test_multiple_threads_waiting_for_integer_value();
            Test_multiples_set_values_throw_IllegalStateException();
            Test_simple_thread_terminate_by_zero_timeout() ;
            Test_simple_thread_terminate_by_timeout();
            Test_multiple_threads_but_one_terminate_by_timeout_and_the_other_get_the_value();
            Console.ReadLine();

        }

        private static void Test_simple_thread_waiting_for_integer_value()
        {
 	        Console.WriteLine("---------simple_test_thread_waiting_for_integer_value() BEGIN------------");
            NonBlockingFutureHolder<String> futureHolder = new NonBlockingFutureHolder<String>();
            Thread t1 = new Thread(() => NonBlockingThread(futureHolder,1,null,5000,GET_VALUE));
            Thread t2 = new Thread(() => NonBlockingThread(futureHolder,2,"10",5000,SET_VALUE));
            

            t1.Start();
            Thread.Sleep(2000);
            t2.Start();
            Thread.Sleep(1200);
            Console.WriteLine("-------------------------------------------------------------------------");
        }

        private static void Test_multiple_threads_waiting_for_integer_value()
        {
 	        Console.WriteLine("------test_multiple_threads_waiting_for_integer_value() BEGIN-------------");
            NonBlockingFutureHolder<string> futureHolder = new NonBlockingFutureHolder<string>();
            
            Thread t1 = new Thread(()=>NonBlockingThread(futureHolder,1,null,5000,GET_VALUE));
            Thread t2 = new Thread(()=>NonBlockingThread(futureHolder,2,null,5000,GET_VALUE));
            Thread t3 = new Thread(()=>NonBlockingThread(futureHolder,3,null,5000,GET_VALUE));
            Thread t4 = new Thread(()=>NonBlockingThread(futureHolder,4,"20",5000,SET_VALUE));

            t1.Start();
            t2.Start();
            t3.Start();
            Thread.Sleep(2000);
            t4.Start();
            Thread.Sleep(1200);
        
            Console.WriteLine("-------------------------------------------------------------------------");
        }

        private static void Test_multiples_set_values_throw_IllegalStateException()
        {
 	         Console.WriteLine("------test_multiples_set_values_throw_IllegalStateException() BEGIN------");
             NonBlockingFutureHolder<string> futureHolder = new NonBlockingFutureHolder<string>();

             Thread t1 = new Thread(() => NonBlockingThread(futureHolder, 1, "10", 5000, SET_VALUE));
             Thread t2 = new Thread(() => NonBlockingThread(futureHolder,2,"20",5000,SET_VALUE));
            
            t1.Start();
            Thread.Sleep(200);
            t2.Start();
            Thread.Sleep(200);
       
            Console.WriteLine("-------------------------------------------------------------------------");
        }

        private static void Test_simple_thread_terminate_by_zero_timeout()
        {
 	         Console.WriteLine("--------test_simple_thread_terminate_by_zero_timeout() BEGIN--------------");
             NonBlockingFutureHolder<string> futureHolder = new NonBlockingFutureHolder<string>();
             
             Thread t1 = new Thread(() => NonBlockingThread(futureHolder,1,null,0,GET_VALUE));
             t1.Start();
             Thread.Sleep(10);
        
            Console.WriteLine("-------------------------------------------------------------------------");
        }

        private static void Test_simple_thread_terminate_by_timeout()
        {
 	        Console.WriteLine("----------test_simple_thread_terminate_by_timeout() BEGIN----------------");
            NonBlockingFutureHolder<string> futureHolder = new NonBlockingFutureHolder<string>();
            
            Thread t1 = new Thread(() => NonBlockingThread(futureHolder,1,null,2000,GET_VALUE));
            t1.Start();
            Thread.Sleep(4000);
            Console.WriteLine("--------------------------------------------------------------------------");
        }

        private static void Test_multiple_threads_but_one_terminate_by_timeout_and_the_other_get_the_value()
        {
 	        Console.WriteLine("----test_multiple_threads_but_one_terminate_by_timeout_and_the_other_get_the_value() BEGIN---");
            NonBlockingFutureHolder<string> futureHolder = new NonBlockingFutureHolder<string>();
            Thread 
                t1 = new Thread(() =>NonBlockingThread(futureHolder,1,null,1000,GET_VALUE)),
                t2 = new Thread(() =>NonBlockingThread(futureHolder,2,null,4000,GET_VALUE)),
                t3 = new Thread(() => NonBlockingThread(futureHolder,3,"10",1000,SET_VALUE));
            t1.Start();
            Thread.Sleep(2000);
            t2.Start();
            Thread.Sleep(500);
            t3.Start();
            Thread.Sleep(1200);
            Console.WriteLine("----------------------------------------------------------------------------------------------");
        }   

        public static void  NonBlockingThread(NonBlockingFutureHolder<string> nonBlockingFutureHolder, int id, string value, long timeout, bool isGetValue)
        {
             if (isGetValue) {
                Console.WriteLine("Thread with id {0} try get value", id);
                Console.WriteLine("thread with id {0} getValue = {1}",
                        id, (value = nonBlockingFutureHolder.GetValue(timeout)) == null ?  value +" by timeout": value+"");

            }else
            try {
                Console.WriteLine("Thread with id {0} try to put value {1}", id,value);
                nonBlockingFutureHolder.SetValue(value);
                Console.WriteLine("Thread with id {0} put value {1} successfully",id,value);
            }catch(InvalidOperationException){
                Console.WriteLine("Thread with id {0} throw InvalidOperationException - Value already Inserted", id);
            }
            }


   }

}

