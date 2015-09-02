
namespace Noear.Weed {

    /**
     * Created by noear on 14-6-13.
     * 万能绑定接口
     */
    public interface IBinder {
        void bind(GetHandlerEx source);
        IBinder clone();
    }
}
