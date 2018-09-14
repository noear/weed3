using Noear.Weed;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;

namespace weedstudio.Model {
    public class ObjectModel : IBinder, INotifyPropertyChanged {
        public List<ObjectModel> Children { get; set; }

        public String ID { get; set; }
        public String Name { get; set; }
        public int Type { get; set; } //0:...; 1:table; 2:view; 3:store...

        private bool _IsSelected;

        public event PropertyChangedEventHandler PropertyChanged;

        public bool IsSelected {
            get { return _IsSelected; }
            set {
                _IsSelected = value;

                if (PropertyChanged != null) {
                    this.PropertyChanged.Invoke(this, new PropertyChangedEventArgs("IsSelected"));
                }
            }
        }

        public Visibility CanCheck {
            get {
                return Type > 0? Visibility.Visible: Visibility.Hidden;
            }
        }

        public ObjectModel(int type) {
            this.Type = type;
        }
        public ObjectModel(String name,int type ) {
            this.Name = name;
            this.Type = type;
        }

        public void Add(ObjectModel item) {
            if (Children == null)
                Children = new List<ObjectModel>();

            Children.Add(item);
        }

        public void AddRange(IEnumerable<ObjectModel> list) {
            if (Children == null)
                Children = new List<ObjectModel>();

            Children.AddRange(list);
        }

        public void bind(GetHandlerEx s) {
            Name = s("name").value("");
            ID   = s("id").value("");
        }

        public IBinder clone() {
            return new ObjectModel(this.Type);
        }
    }
}
