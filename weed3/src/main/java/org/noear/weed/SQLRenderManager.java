package org.noear.weed;

import org.noear.weed.utils.IOUtils;

import java.util.HashMap;
import java.util.Map;

public class SQLRenderManager implements IRender {
     private final Map<String, IRender> _mapping = new HashMap<>();
     private IRender _def;

     /**
      * 印射后缀和渲染器的关系
      *
      * @param suffix = .ftl
      */
     public void mapping(String suffix, IRender render) {
          //suffix=.ftl
          _def = render;
          _mapping.put(suffix, render);
          System.out.println("Weed3:: sql render: " + suffix + "=" + render.getClass().getSimpleName());
     }

     public SQLRenderManager() {
          String packname = "org.noear.weed.render";
          IStarter tmp = IOUtils.loadEntity(packname + ".freemarker.StarterImp");
          if (tmp != null) {
               tmp.start();
          }

          tmp = IOUtils.loadEntity(packname + ".beetl.StarterImp");
          if (tmp != null) {
               tmp.start();
          }

          tmp = IOUtils.loadEntity(packname + ".enjoy.StarterImp");
          if (tmp != null) {
               tmp.start();
          }

          tmp = IOUtils.loadEntity(packname + ".velocity.StarterImp");
          if (tmp != null) {
               tmp.start();
          }
     }

     //不能放上面
     public static SQLRenderManager global = new SQLRenderManager();

     @Override
     public String render(String path, Map<String, Object> args) throws Throwable {
          if(_def == null){
               throw new RuntimeException("Weed3:Missing sql render");
          }

          //
          //如果有视图
          //
          int suffix_idx = path.lastIndexOf(".");
          if (suffix_idx > 0) {
               String suffix = path.substring(suffix_idx);
               IRender render = _mapping.get(suffix);

               if (render != null) {
                    //如果找到对应的渲染器
                    //
                    return render.render(path, args);
               }
          }

          //如果没有则用默认渲染器
          //
          return _def.render(path, args);
     }
}
