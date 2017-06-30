using System;
using System.Collections.Generic;
using System.Configuration;
using System.Linq;
using System.Text;

namespace weedstudio.Dao {
   public  class Setting {
        static Configuration _config;
         static Setting() {
            //获取Configuration对象
            _config = ConfigurationManager.OpenExeConfiguration(ConfigurationUserLevel.None);
        }

        public static string get(string name) {
            return get(name, "");
        }

        public static string get(string name, string def) {
            var temp = _config.AppSettings.Settings[name];
            if (temp == null)
                return def;
            else
                return temp.Value;
        }

        public static void set(string name, string val) {
            var temp = _config.AppSettings.Settings[name];

            if (temp == null) {
                _config.AppSettings.Settings.Add(name, val);
            }
            else {
                temp.Value = val;
            }
            
            _config.Save(ConfigurationSaveMode.Modified);

            ConfigurationManager.RefreshSection("appSettings");
        }
    }
}
