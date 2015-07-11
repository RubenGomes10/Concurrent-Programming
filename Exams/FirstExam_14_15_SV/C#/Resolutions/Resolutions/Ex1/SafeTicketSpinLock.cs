using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace Resolutions.Ex1
{
    class SafeTicketSpinLock
    {
        private int ticket;
        private int users;

        public void Lock()
        {
            SpinWait sw = new SpinWait();
            do
            {
                int oldUserValue = Volatile.Read(ref users); // atomacity garanted
                if (CompareAndSwap(ref users,oldUserValue,oldUserValue+1)) // compares the oldValue with ref value , if they are the same  
                {                                                           // the new vale is insert in ref value
                    do
                    {
                        int me = Volatile.Read(ref users);// atomacity garanted
                        int oldTicket = Volatile.Read(ref ticket); // atomacity garanted
                        if (me == oldTicket) return;
                        sw.SpinOnce();
                    } while (true);
                }

            } while (true);
        }

        public void Unlock()
        {
            do
            {
                int oldTicket = Volatile.Read(ref ticket); // atomacity garanted
                if (CompareAndSwap(ref ticket, oldTicket, oldTicket + 1))
                    return;
            
            } while (true);
        }
    

    private bool CompareAndSwap( ref int value, int oldValue, int nextValue){
        return Interlocked.CompareExchange(ref value, nextValue, oldValue) == oldValue;
    }
    
    }


}
