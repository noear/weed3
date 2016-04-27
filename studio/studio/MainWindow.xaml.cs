using System;
using System.IO;
using System.Windows;
using System.Windows.Controls;
using weedstudio.exts;
using weedstudio.Utils;
using weedstudio.Model;
using weedstudio.ViewModel;

namespace weedstudio {
    /// <summary>
    /// MainWindow.xaml 的交互逻辑
    /// </summary>
    public partial class MainWindow : Window {
        MainViewModel viewModel;
        DbContextEx currentSource;
        ObjectModel currentObject;

        public MainWindow() {
            InitializeComponent();
        }

        private void Window_Loaded(object sender, RoutedEventArgs e) {
            viewModel = new MainViewModel();

            this.DataContext = viewModel;
        }


        private void sourceBox_SelectionChanged(object sender, SelectionChangedEventArgs e) {
            currentSource = DbManager.get((string)sourceBox.SelectedItem);
            viewModel.loadObjects(currentSource);
        }

        private void objectList_SelectedItemChanged(object sender, RoutedPropertyChangedEventArgs<Object> e) {
            currentObject = (ObjectModel)e.NewValue;

            if (currentObject == null)
            {
                viewModel.loadPropertys(null, null);
                return;
            }
            
            if (currentObject.Type > 0) {
                viewModel.loadPropertys(currentSource, currentObject);
                return;
            }
        }


        private void buildButton_Click(object sender, RoutedEventArgs e) {

            if (templetBox.SelectedIndex < 0) {
                MessageBox.Show("请选择模板!");
                return;
            }

            int count = 0;
            foreach (ObjectModel p in viewModel.ObjectList) {
                foreach (ObjectModel obj in p.Children) {
                    if (obj.IsSelected) {
                        DoBuild(obj, true);
                        count++;

                        currentObject = obj;
                    }
                }
            }

            if (count == 0) {
                MessageBox.Show("请选择要生成的[表/视图/存储过程]!");
            }
            else {
                viewModel.loadPropertys(currentSource, currentObject);
                MessageBox.Show("相关文件已成功生成...");
            }
        }
        private void DoBuild(ObjectModel obj, bool isBatch) {
            var cols = viewModel.getPropertyList(currentSource, obj);

            WeedBuilder sdqBuilder = new WeedBuilder(currentSource);

            if (Directory.Exists(Config.DIR_OUT) == false) {
                Directory.CreateDirectory(Config.DIR_OUT);
            }

            sdqBuilder.TargetFolder = Config.DIR_OUT;
            sdqBuilder.NameSpace = "noear.weed.studio";
            sdqBuilder.TableName = obj.Name;
            sdqBuilder.Columns = cols;

            sdqBuilder.TempletFile = (string)templetBox.SelectedItem;


            sdqBuilder.Build();
        }
    }
}
