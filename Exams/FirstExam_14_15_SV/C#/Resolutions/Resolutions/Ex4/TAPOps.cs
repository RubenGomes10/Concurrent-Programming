using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Resolutions.Ex4
{
    class TAPOps
    {
        public interface Services
        {
            Task<Uri[]> GetAvailableServersAsync(Uri provider);
            Task<int> GetAverageServiceTimeAsync(Uri server);
        }

        public async static Task<Tuple<Uri, int>> GetFasterServerAsync(Services svc, Uri provider)
        {
            Task<Tuple<Uri, int>> result = null;
            int fastServiceTime = int.MaxValue;
            Task<Uri>[] tasks = svc.GetAvailableServersAsync(provider).UnWrap(); // missing UnWrap
            int lenght = tasks.Length;
            for (int i = 0; i < lenght; i++)
            {
                int li = i; // closure
                Uri server = tasks[li].Result;
                int ast = svc.GetAverageServiceTimeAsync(server).Result;
                if (ast < fastServiceTime)
                {
                    fastServiceTime = ast;
                    result = new Task<Tuple<Uri, int>>(new Tuple<Uri, int>(server, ast));
                }
            }
            await Task<Tuple<Uri, int>>.WhenAll(new Task<Tuple<Uri,int>>[] { result });
            return result.Result;
        }
    }
}
