using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace RendezvousChannelSynchronizer
{
    
    class RendezvousChannel<S,R>
    {
        LinkedList<RequestServerItem<S>> queueServer = new LinkedList<RequestServerItem<S>>();
        LinkedList<TokenClient<S, R>> queueRequest = new LinkedList<TokenClient<S, R>>();

        public R Request(S service, int timeout)
        {
            lock (this)
            {
                TokenClient<S, R> token = new TokenClient<S, R>(service);
                if (queueServer.Count == 0) // if queue server is empty and timeout expires throw TimeoutException
                {
                    if (timeout == 0)
                        throw new TimeoutException("Timeout is 0");
                    queueRequest.AddFirst(token); // if not expires, add request and start wait until not pending
                    int lastTime = (timeout != Timeout.Infinite) ? Environment.TickCount : 0;
                    do
                    {
                        try { SyncUtils.Wait(this, token, timeout); }
                        catch (ThreadInterruptedException)
                        {
                            if (token.pending) //if is pending wait until rend is done
                            {
                                do
                                {
                                    SyncUtils.Wait(this, token);
                                } while (!token.rendDone); // when its done, interrupt current Thread and return token response
                                Thread.CurrentThread.Interrupt();
                                return token.response;
                            }
                            queueRequest.Remove(token); // if not pending remove request and throw exception
                            throw;
                        }
                        if (AdjustTimeout(ref lastTime, ref timeout) == 0)
                        {
                           queueRequest.Remove(token);
                            throw new TimeoutException("Timeout exceeded");
                        }
                    } while (!token.pending);
                } // if queue server ins`t empty remove item and
                else
                {
                    RequestServerItem<S> item = queueServer.First.Value; 
                    queueServer.Remove(item);
                    item.val = token; // save token into server into
                    item.rendAccepted = true; // rend its accepted
                    SyncUtils.Notify(this, item); // notify waiting thread
                }
                do // waits until token have rend done
                {
                    SyncUtils.Wait(this, token);
                } while (!token.rendDone); // when its rendDone return token response
                return token.response;
            }
        }

        public Token<S> Accept(int timeout)
        {
            lock (this)
            {
                RequestServerItem<S> item = new RequestServerItem<S>();
                TokenClient<S, R> token;
                if (queueRequest.Count == 0) // if request queue is empty
                {
                    if (timeout == 0)
                        throw new TimeoutException("Timeout is 0");
                    queueServer.AddLast(item); // add requestServerItem into server queue and waits until item have rend Accepted
                    int lastTime = (timeout != Timeout.Infinite) ? Environment.TickCount : 0;
                    do
                    {
                        try
                        {
                            SyncUtils.Wait(this, item, timeout);
                        }
                        catch (ThreadInterruptedException)
                        {
                            if (item.rendAccepted) // if rend are accepted
                            {
                                token = (TokenClient<S, R>)item.val; // save item val into token
                                token.pending = true; // pending become true
                                Thread.CurrentThread.Interrupt(); // interrupt current thread 
                                SyncUtils.Notify(this, token);  // notify waiting thread and return token
                                return token;
                            }
                            queueServer.Remove(item); // if ins`t accepted remove item from queue server and throw exception
                            throw;
                        }
                        if (AdjustTimeout(ref lastTime, ref timeout) == 0) // if timeout expires remove item from server queue , throw timeoutException
                        {
                            queueServer.Remove(item);
                            throw new TimeoutException("Timeout Exceeded");
                        }
                    } while (!item.rendAccepted);
                    token = (TokenClient<S, R>)item.val; // if rend accepted save item value into token
                }
                else // if queue request ins`t empty remove first token
                {
                    token = (TokenClient<S, R>)queueRequest.First.Value;
                    queueRequest.RemoveFirst();
                }
                token.pending = true; // set pending a true
                SyncUtils.Notify(this, token); // notify waiting thread and return token
                return token;
            }
        }

        public void Reply(Token<S> token, R response)
        {
            lock (this)
            {
                ((TokenClient<S, R>)token).response = response;  // save response
                ((TokenClient<S, R>)token).rendDone = true; // alert rend its done
                SyncUtils.Notify(this, token); // and notify waiting condition thread
            }
        }

        public static int AdjustTimeout(ref int lastTime, ref int timeout)
        {
            if (timeout != Timeout.Infinite)
            {
                int now = Environment.TickCount;
                int elapsed = (now == lastTime) ? 1 : now - lastTime;
                if (elapsed >= timeout)
                {
                    timeout = 0;
                }
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
