using System;
using System.IO;
using System.Windows;
using System.Windows.Controls;
using weedstudio.exts;
using weedstudio.Utils;
using weedstudio.Model;
using weedstudio.ViewModel;
using System.Configuration;
using weedstudio.Dao;
using Noear.Snacks;

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

            string source = Setting.get("source");
            if (string.IsNullOrEmpty(source) == false) {
                sourceBox.SelectedItem = source;
            }

            string templet = Setting.get("templet");
            if (string.IsNullOrEmpty(templet) == false) {
                templetBox.SelectedItem = templet;
            }

            args.Text = Setting.get("def_args", "{namespace:'noear.weed.studio',dbcontext:'config'}");
        }

        private void Window_Unloaded(object sender, RoutedEventArgs e) {
            Setting.set("def_args", args.Text.Trim());
        }

        private void sourceBox_SelectionChanged(object sender, SelectionChangedEventArgs e) {
            var name = (string)sourceBox.SelectedItem;
            currentSource = DbManager.get(name);
            viewModel.loadObjects(currentSource);

            Setting.set("source", name);
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

            var templet = (string)templetBox.SelectedItem;
            Setting.set("templet", templet);

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
            sdqBuilder.args = ONode.tryLoad(args.Text.Trim());
            sdqBuilder.TableName = obj.Name;
            sdqBuilder.Columns = cols;

            sdqBuilder.TempletFile = (string)templetBox.SelectedItem;


            sdqBuilder.Build();
        }
        

        private void menu_all_Click(object sender, RoutedEventArgs e) {
            MenuItem item = sender as MenuItem;
            if (item == null) {
                return;
            }
            var model = item.DataContext as ObjectModel;

            if (model == null) {
                return;
            }

            foreach (var objp in viewModel.ObjectList) {
                foreach (var obj in objp.Children) {
                    if (obj.Type == model.Type) {
                        obj.IsSelected = true;
                    }
                }
            }
        }

        private void menu_unall_Click(object sender, RoutedEventArgs e) {
            MenuItem item = sender as MenuItem;
            if (item == null) {
                return;
            }
            var model = item.DataContext as ObjectModel;

            if (model == null) {
                return;
            }

            foreach (var objp in viewModel.ObjectList) {
                foreach (var obj in objp.Children) {
                    if (obj.Type == model.Type) {
                        obj.IsSelected = !obj.IsSelected;
                    }
                }
            }
        }

        private void templetBox_SelectionChanged(object sender, SelectionChangedEventArgs e) {
            if (templetBox.SelectedIndex < 0) {
                return;
            }

            var templet = (string)templetBox.SelectedItem;
            Setting.set("templet", templet);
        }

       
    }
}
