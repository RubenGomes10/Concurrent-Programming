using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ResolvedQuestions.Exer4
{
    class Translactions
    {
        public IAsyncResult BeginTranslate(string text, string inLang, string outLang, AsyncCallback cb, object state);
        public String EndTranslate(IAsyncResult ar);
        public Task<string> TranslateAsync(string text, string inLang, string outLang);
    }
}
