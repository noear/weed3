using Noear.Weed;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Data;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using weedstudio.exts;
using weedstudio.Model;
using weedstudio.Utils;

namespace weedstudio.ViewModel {
    public class MainViewModel : ModelBase {
        public List<String> TempletList { get; set; }
        public List<String> SourceList { get; set; }

        public ObservableCollection<ObjectModel> ObjectList { get; set; }
        public ObservableCollection<PropertyModel> PropertyList { get; set; }


        public MainViewModel() {

            TempletList = new List<string>();
            SourceList = new List<string>();
            ObjectList = new ObservableCollection<ObjectModel>();
            PropertyList = new ObservableCollection<PropertyModel>();

            SourceList.AddRange(DbManager.sources());
            TempletList.AddRange(TempletApi.templets());
        }

        public void loadObjects(DbContextEx db) {
            ObjectList.Clear();

            //1.获取配置里的执行代码
            //
            ObjectItem exeCode = SourceConfig.GetObject(db.builder, db);

            ObjectModel dataTableRoot = new ObjectModel("数据表", 0);
            ObjectModel dataViewRoot = new ObjectModel("数据视图", 0);
            ObjectModel dataSPRoot = new ObjectModel("存储过程", 0);

            //2.执行代码,获取数据
            //
            List<ObjectModel> tableList = db.sql(exeCode.Table).getList(new ObjectModel(1));
            List<ObjectModel> viewList = db.sql(exeCode.View).getList(new ObjectModel(2));
            List<ObjectModel> spList = db.sql(exeCode.StoredProcedure).getList(new ObjectModel(3));

            //3.绑定到树控件上
            //
            dataTableRoot.AddRange(tableList);
            dataViewRoot.AddRange(viewList);
            dataSPRoot.AddRange(spList);

            if (dataTableRoot.Children.Count > 0)
                ObjectList.Add(dataTableRoot);

            if (dataViewRoot.Children.Count > 0)
                ObjectList.Add(dataViewRoot);

            if (dataSPRoot.Children.Count > 0)
                ObjectList.Add(dataSPRoot);

            NotifyPropertyChanged("ObjectList");
        }

        public void loadPropertys(DbContextEx db, ObjectModel obj) {
            PropertyList.Clear();

            if (db == null || obj == null)
                return;

            var popList = getPropertyList(db, obj);
            foreach (var p in popList)
                PropertyList.Add(p);
        }

        public List<PropertyModel> getPropertyList(DbContextEx db, ObjectModel obj) {
            PropertyItem exeCode = SourceConfig.GetProperty(db.builder, db);

            List<PropertyModel> popList = null;

            string sql = null;
            if (obj.Type < 3) {
                sql = exeCode.Table.Replace("{ID}", obj.ID).Replace("{NAME}", obj.Name);
            }
            else {
                sql = exeCode.StoredProcedure.Replace("{ID}", obj.ID).Replace("{NAME}", obj.Name).Trim();

                if (sql[0] == '@')
                    popList = Weed.Addin.Eval<IParamsBuilder>(sql.Substring(1)).GetParams(obj.Name, db);
            }

            if (popList == null)
                popList = db.sql(sql).getList(new PropertyModel());


            return popList;
        }
    }
}
