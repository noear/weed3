using System;
using System.Collections.Generic;
using System.Text;

namespace Weed.Addins
{
    public interface ICommand
    {
        void Exec(params object[] args);
        T Exec<T>(params object[] args);
    }
}
