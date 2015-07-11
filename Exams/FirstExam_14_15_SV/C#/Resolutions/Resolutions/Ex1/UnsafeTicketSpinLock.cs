using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace Resolutions.Ex1
{
    class UnsafeTicketSpinLock
    {
        private int ticket;
        private int users;

        public void Lock()
        {
            SpinWait sw = new SpinWait();
            int me = users++;
            while (me != ticket)
                sw.SpinOnce();
        }

        public void Unlock()
        {
            ticket++;
        }
    }
}
