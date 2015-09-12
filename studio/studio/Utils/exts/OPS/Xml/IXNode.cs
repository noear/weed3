using System;
using System.Collections.Generic;
using System.Text;

namespace Weed.OPS
{
    public interface IXNode
    {
        XItem this[string name] { get; }

        string Name { get; }

        bool Contains(string name);
    }
}
