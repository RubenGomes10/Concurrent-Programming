using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Resolutions.Ex4
{
    class SyncOps
    {
        public interface Services
        {
            Uri[] GetAvailableServers(Uri provider);
            int GetAverageServiceTime(Uri server);
        }

        public static Tuple<Uri, int> GetFasterServer(Services svc, Uri provider)
        {
            Uri[] servers = svc.GetAvailableServers(provider);
            Tuple<Uri, int> result = null;
            int fasteServiceTime = int.MaxValue;
            for(int i = 0; i< servers.Length; ++i){
                int ast = svc.GetAverageServiceTime(servers[i]);
                if (ast < fasteServiceTime)
                {
                    fasteServiceTime = ast;
                    result = new Tuple<Uri, int>(servers[i], ast);
                }
            }
            return result;
        }
    }
}
