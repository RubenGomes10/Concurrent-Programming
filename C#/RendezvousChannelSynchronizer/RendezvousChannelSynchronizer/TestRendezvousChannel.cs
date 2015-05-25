using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace RendezvousChannelSynchronizer
{
    class TestRendezvousChannel
    {
        //Responses -> Client gives an integer, server gives back the symmetric of it 
        static void Main(string[] args)
        {
            TestRendezvousChannel tests = new TestRendezvousChannel();
            
            //tests.Test_Client_Starts_First_Rendezvous();
            //tests.Test_Server_Starts_First_Rendezvous();
            //tests.Test_Client_Zero_Time_Rendezvous();
            //tests.Test_Client_Zero_Time_After_Server_Starts_Rendezvous();
            //tests.Test_Server_Zero_Time_Rendezvous();
            //tests.Test_Server_Zero_Time_After_CLient_Starts_Rendezvous();
            //tests.Test_Multiple_Client_One_Server_Rendezvous();
            //tests.Test_Multiple_Server_One_Client_Rendezvous();
            //tests.Test_Multiple_Clients_First_And_Servers();
            //tests.Test_Multiple_Servers_First_And_Clients();
            //tests.Test_Long_Wait_In_Rendezvous();
            //tests.Test_Client_Interrupted_Rendezvous();
            //tests.Test_Server_Interrupted_Rendezvous();

            Console.ReadKey();
        }

        private void Test_Client_Starts_First_Rendezvous()
        {
            RendezvousChannel<int, int> rendChannel = new RendezvousChannel<int, int>();
            Thread t1 = new Thread(() => ServerFunc(1, rendChannel, 5000, 500));
            Thread t2 = new Thread(() => ClientFunc(1, rendChannel, 10, 5000));

            t1.Start();
            Thread.Sleep(500);
            t2.Start();
            Thread.Sleep(500);
        }

        private void Test_Server_Starts_First_Rendezvous()
        {
            RendezvousChannel<int, int> rendChannel = new RendezvousChannel<int, int>();
            Thread t1 = new Thread(() => ClientFunc(1, rendChannel, 30, 5000));
            Thread t2 = new Thread(() => ServerFunc(1, rendChannel, 5000, 500));

            t1.Start();
            Thread.Sleep(500);
            t2.Start();
            Thread.Sleep(500);
        }

        private void Test_Client_Zero_Time_Rendezvous()
        {
            RendezvousChannel<int, int> rendChannel = new RendezvousChannel<int, int>();
            Thread t1 = new Thread(() => ClientFunc(1, rendChannel, 30, 0));
            Thread t2 = new Thread(() => ServerFunc(1, rendChannel, 5000, 500));

            t1.Start();
            Thread.Sleep(500);
            t2.Start();
            Thread.Sleep(500);
        }

        private void Test_Client_Zero_Time_After_Server_Starts_Rendezvous()
        {
            RendezvousChannel<int, int> rendChannel = new RendezvousChannel<int, int>();
            Thread t1 = new Thread(() => ServerFunc(1, rendChannel, 5000, 500));
            Thread t2 = new Thread(() => ClientFunc(1, rendChannel, 21, 0));

            t1.Start();
            Thread.Sleep(500);
            t2.Start();
            Thread.Sleep(500);
        }

        private void Test_Server_Zero_Time_Rendezvous()
        {
            RendezvousChannel<int, int> rendChannel = new RendezvousChannel<int, int>();
            Thread t1 = new Thread(() => ServerFunc(1, rendChannel, 0, 500));
            Thread t2 = new Thread(() => ClientFunc(1, rendChannel, 30, 5000));

            t1.Start();
            Thread.Sleep(500);
            t2.Start();
            Thread.Sleep(500);
        }

        private void Test_Server_Zero_Time_After_CLient_Starts_Rendezvous()
        {
            RendezvousChannel<int, int> rendChannel = new RendezvousChannel<int, int>();
            Thread t1 = new Thread(() => ClientFunc(1, rendChannel, 7, 5000));
            Thread t2 = new Thread(() => ServerFunc(1, rendChannel, 0, 500));

            t1.Start();
            Thread.Sleep(500);
            t2.Start();
            Thread.Sleep(500);
        }

        private void Test_Multiple_Client_One_Server_Rendezvous()
        {
            RendezvousChannel<int, int> rendChannel = new RendezvousChannel<int, int>();
            Thread t1 = new Thread(() => ClientFunc(1, rendChannel, 30, 5000));
            Thread t2 = new Thread(() => ClientFunc(2, rendChannel, 60, 5000));
            Thread t3 = new Thread(() => ServerFunc(1, rendChannel, 5000, 500));

            t1.Start();
            Thread.Sleep(500);
            t2.Start();
            Thread.Sleep(500);
            t3.Start();
            Thread.Sleep(500);
        }

        private void Test_Multiple_Server_One_Client_Rendezvous()
        {
            RendezvousChannel<int, int> rendChannel = new RendezvousChannel<int, int>();
            Thread t1 = new Thread(() => ClientFunc(1, rendChannel, 30, 5000));
            Thread t2 = new Thread(() => ServerFunc(2, rendChannel, 5000, 5000));
            Thread t3 = new Thread(() => ServerFunc(1, rendChannel, 5000, 500));

            t1.Start();
            Thread.Sleep(500);
            t2.Start();
            Thread.Sleep(500);
            t3.Start();
            Thread.Sleep(500);
        }

        private void Test_Multiple_Clients_First_And_Servers()
        {
            RendezvousChannel<int, int> rendChannel = new RendezvousChannel<int, int>();
            Thread t1 = new Thread(() => ClientFunc(1, rendChannel, 84, 5000));
            Thread t2 = new Thread(() => ClientFunc(2, rendChannel, 53, 5000));
            Thread t3 = new Thread(() => ServerFunc(2, rendChannel, 5000, 5000));
            Thread t4 = new Thread(() => ServerFunc(1, rendChannel, 5000, 500));

            t1.Start();
            Thread.Sleep(500);
            t2.Start();
            Thread.Sleep(500);
            t3.Start();
            Thread.Sleep(500);
            t4.Start();
            Thread.Sleep(500);
        }

        private void Test_Multiple_Servers_First_And_Clients()
        {
            RendezvousChannel<int, int> rendChannel = new RendezvousChannel<int, int>();
            Thread t1 = new Thread(() => ServerFunc(1, rendChannel, 5000, 500));
            Thread t2 = new Thread(() => ServerFunc(2, rendChannel, 5000, 500));
            Thread t3 = new Thread(() => ClientFunc(2, rendChannel, 53, 5000));
            Thread t4 = new Thread(() => ClientFunc(1, rendChannel, 84, 5000));

            t1.Start();
            Thread.Sleep(500);
            t2.Start();
            Thread.Sleep(500);
            t3.Start();
            Thread.Sleep(500);
            t4.Start();
            Thread.Sleep(500);
        }

        private void Test_Long_Wait_In_Rendezvous()
        {
            RendezvousChannel<int, int> rendChannel = new RendezvousChannel<int, int>();
            Thread t2 = new Thread(() => ServerFunc(1, rendChannel, 5000, 10000));
            Thread t1 = new Thread(() => ClientFunc(1, rendChannel, 10, 5000));

            t1.Start();
            Thread.Sleep(500);
            t2.Start();
            Thread.Sleep(500);
        }

        private void Test_Client_Interrupted_Rendezvous()
        {
            RendezvousChannel<int, int> rendChannel = new RendezvousChannel<int, int>();
            Thread t1 = new Thread(() => ClientFunc(1, rendChannel, 50, 5000));
            Thread t2 = new Thread(() => ClientFunc(2, rendChannel, 91, 5000));
            Thread t3 = new Thread(() => ServerFunc(1, rendChannel, 5000, 500));

            t1.Start();
            Thread.Sleep(500);
            t1.Interrupt();
            t2.Start();
            Thread.Sleep(500);
            t3.Start();
            Thread.Sleep(500);
        }

        private void Test_Server_Interrupted_Rendezvous()
        {
           RendezvousChannel<int, int> rendChannel = new RendezvousChannel<int, int>();
            Thread t1 = new Thread(() => ServerFunc(1, rendChannel, 5000, 500));
            Thread t2 = new Thread(() => ServerFunc(2, rendChannel, 5000, 500));
            Thread t3 = new Thread(() => ClientFunc(1, rendChannel, 10, 5000));

            t1.Start();
            Thread.Sleep(500);
            t1.Interrupt();
            t2.Start();
            Thread.Sleep(500);
            t3.Start();
            Thread.Sleep(500);
        }



        public static void ServerFunc(int id, RendezvousChannel<int, int> rendChannel, int timeout, int waitForReply)
        {
            try
            {
                Token<int> token = rendChannel.Accept(timeout);
                Console.WriteLine("Server "+ id +" started Rendezvous");
                Thread.Sleep(waitForReply);
                rendChannel.Reply(token,token.Value() *(-1));
            }
            catch (TimeoutException)
            {
                Console.WriteLine("Timeout exceeded in Server");
            }
            catch (ThreadInterruptedException)
            {
                Console.WriteLine("Server thread interrupted");
            }
        }
        public static void ClientFunc(int id, RendezvousChannel<int, int> rendChannel, int request, int timeout)
        {
            try
            {
                int response = rendChannel.Request(request,timeout);
                Console.WriteLine("Client "+ id + " asked double of " + request + " and got " + response);
            }
            catch (TimeoutException)
            {
                Console.WriteLine("Timeout exceeded in Client " + id);
            }
            catch (ThreadInterruptedException)
            {
                Console.WriteLine("Client thread interrupted");
            }
        }
    }
}


