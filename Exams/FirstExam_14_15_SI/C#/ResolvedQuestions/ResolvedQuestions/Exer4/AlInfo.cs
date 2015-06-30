using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace ResolvedQuestions.Exer4
{
    class AlInfo
    {
        private Reviews reviews = new Reviews();
        private Translactions translactions = new Translactions();

        //APM
        public IAsyncResult BeginObtainAllInfo(int[] reviewsId, string outLang, AsyncCallback cb, object state)
        {
            var gar = new GenericAsyncResult<String[]>(cb, state);
            AsyncCallback onObtainReview = null,
                          onObtainTranslaction = null;
            int count = reviewsId.Length;
            String[] translated = new String[count];

            //AsyncCallback for complete Translaction
            onObtainTranslaction = (ar) =>{
                int id = (int) ar.AsyncState;
                translated[id] = translactions.EndTranslate(ar);
                if(Interlocked.Decrement(ref count) == 0){
                    gar.OnComplete(translated,null);
                }
            };

            //AsyncCallback to begin translation
            onObtainReview = (ar) =>
            {
                int id = (int)ar.AsyncState;
                string review = reviews.EndObtainReview(ar);
                translactions.BeginTranslate(review, "en", outLang, onObtainTranslaction, id);

            };

            //foreach position in array - call BeginObtainReview with callback
            for (int i = 0; i < count; ++i)
            {
                reviews.BeginObtainReview(reviewsId[i], onObtainReview, i);
            }

            return gar;
        }

        //Block if all translactions of all reviews not completed
        public string[] EndObtainAllInfo(IAsyncResult ar)
        {
            return ((GenericAsyncResult<String[]>)ar).Result;
        }

        //TAP
        public Task<string[]> ObtainAllInfoAsync(int[] reviewsId, string outLang)
        {
            int count = reviewsId.Length;
            Task<string>[] tasks = new Task<string>[count];

            for (int i = 0; i < count; ++i)
            {
                int li = i; //closure
                tasks[i] = reviews.ObtainReviewAsync(reviewsId[li])
                    .ContinueWith((ar) =>
                        translactions.TranslateAsync(ar.Result, "en", outLang).UnWrap());//missing UnWrap         
            }

            return Task<string[]>.Factory.ContinueWhenAll(tasks, (ar) =>
            {
                string[] result = new string[count];

                for (int i = 0; i < count; i++)
                {
                    result[i] = tasks[i].Result;
                }
                return result;
            });

        }

        //Using async C#

        public async Task<string[]> ObtainAllInfoAsyncAsync(int [] reviewsId, string outLang)
        {
            int count = reviewsId.Length;
            Task<string>[] tasks = new Task<string>[count];

            for (int i = 0; i < count; ++i)
            {
                int li = i; //closure
                tasks[i] = reviews.ObtainReviewAsync(reviewsId[li])
                    .ContinueWith((ar) =>
                        translactions.TranslateAsync(ar.Result, "en", outLang).UnWrap());//missing UnWrap         
            }
            return await Task<string[]>.WhenAll(tasks);
        }

    }
}
