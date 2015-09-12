using Noear.Weed;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace weedstudio.Model {
    public class PropertyModel : IBinder {
        public String Name { get; set; }
        public String Type { get; set; }
        public bool IsKey { get; set; }
        public String Note { get; set; }
        public String Default { get; set; }

        public PropertyModel() {
        }

        public PropertyModel(String name, string type, bool isKey,string note,string def) {
            Name = name;
            Type = type;
            IsKey = isKey;
            Note = note;
            Default = def;
        }

        public void bind(GetHandlerEx s) {
            Name = s("Name").value("");
            Type = s("Type").value("");
            object temp = s("IsKey").getValue();
            if (temp is bool)
                IsKey = (bool)temp;
            else if (temp is long)
                IsKey = (long)temp>0;
            else if (temp is int)
                IsKey = (int)temp > 0;
            Note = s("Note").value("");
            Default = s("Default").value("");

        }

        public IBinder clone() {
           return  new PropertyModel();
        }
    }
}
