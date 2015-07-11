using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace Resolutions.Ex4
{
    class APMOps
    {
        public interface Services
        {
            IAsyncResult BeginGetAvailableServers(Uri provider, AsyncCallback cb, object state);
            Uri[] EndGetAvailableServers(IAsyncResult ar);
            IAsyncResult BeginGetAverageServiceTime(Uri server, AsyncCallback cb, object state);
            int EndGetAverageServiceTime(IAsyncResult ar);
        }

        public IAsyncResult BeginGetFasterServer(Services svc, Uri provider, AsyncCallback cb, object state)
        {
            var gar = new GenericAsyncResult<Tuple<Uri, int>>(cb, state);
            AsyncCallback onGetServers = null,onGetAverage = null;
            Tuple<Uri, int> result = null;
            int fasterServiceTime = int.MaxValue;
            int count = -1;
            Uri[] servers = null;

            onGetServers = (ar) =>
            {
                servers = svc.EndGetAvailableServers(ar);
                int length = count = servers.Length;

                for (int i = 0; i < length; ++i)
                {
                    svc.BeginGetAverageServiceTime(servers[i], onGetAverage, servers[i]);
                }
            };

            onGetAverage = (ar) =>
            {
                Uri server = (Uri)ar.AsyncState;
                int ast = svc.EndGetAverageServiceTime(ar);
                if (ast < fasterServiceTime)
                {
                    fasterServiceTime = ast;
                    result = new Tuple<Uri, int>(server, ast);
                }
                if (Interlocked.Decrement(ref count) == 0)
                {
                    gar.OnComplete(result, null);
                }
            };
            svc.BeginGetAvailableServers(provider, onGetServers, null);
            return gar;
        }

        public Tuple<Uri, int> EndGetFasterServer(IAsyncResult ar)
        {
            return ((GenericAsyncResult<Tuple<Uri, int>>)ar).Result;
        }
    }
}
