using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ResolvedQuestions.Exer4
{
    class TAPOps
    {
        interface Services
        {
            public interface Services
            {
                Task<string> GetDeviceAddressAsync(int devId);
                Task<int> GetVersionFromDeviceAsync(String addr);
                Task<int> GetStoredVersionAsync(int devId);
            }

            public Task<bool> CheckDeviceVersionAsync(Services services, int devId)
            {
                Task<string> addr = services.GetDeviceAddressAsync(devId);
                Task<int> stoVersion = services.GetStoredVersionAsync(devId);
                Task<int> devVersion = services.GetVersionFromDeviceAsync(addr.Result);

                return Task<bool>.Factory
                    .ContinueWhenAll(
                        new Task[] { devVersion, stoVersion },
                        (ar) => devVersion.Result == stoVersion.Result
                    ); 
            }

            public async Task<bool> CheckDeviceVersionAsyncAsync(Services services, int devId)
            {
                Task<string> addr = services.GetDeviceAddressAsync(devId);
                Task<int> stoVersion = services.GetStoredVersionAsync(devId);
                Task<int> devVersion = services.GetVersionFromDeviceAsync( await addr);
                await Task<bool>.WhenAll(new Task[] { devVersion, stoVersion});
                return devVersion == stoVersion;
            }
        }
    }
}
