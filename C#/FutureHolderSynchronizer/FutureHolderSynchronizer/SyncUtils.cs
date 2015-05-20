﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace FutureHolderSynchronizer
{
    class SyncUtils
    {

        internal static long AdjustTimeout(ref int lastTime, ref long timeout)
        {
            if (timeout != Timeout.Infinite)
            {
                int now = Environment.TickCount;
                int elapsed = (now == lastTime) ? 1 : now - lastTime;
                if (elapsed >= timeout)
                    timeout = 0;
                else
                {
                    timeout -= elapsed;
                    lastTime = now;
                }
            }
            return timeout;
        }
    }
}
