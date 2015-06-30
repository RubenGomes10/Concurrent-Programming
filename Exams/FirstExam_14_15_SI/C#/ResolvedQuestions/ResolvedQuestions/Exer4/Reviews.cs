using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ResolvedQuestions.Exer4
{
    class Reviews
    {
        public IAsyncResult BeginObtainReview(int reviewId, AsyncCallback cb, object state);
        public String EndObtainReview(IAsyncResult ar);
        public Task<String> ObtainReviewAsync(int reviewId);
    }
}
