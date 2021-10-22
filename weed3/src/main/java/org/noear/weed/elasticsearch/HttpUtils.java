package org.noear.weed.elasticsearch;

import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

class HttpUtils {
    private final static Supplier<Dispatcher> okhttp_dispatcher = () -> {
        Dispatcher temp = new Dispatcher();
        temp.setMaxRequests(20000);
        temp.setMaxRequestsPerHost(10000);
        return temp;
    };

    private final static OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(60 * 5, TimeUnit.SECONDS)
            .writeTimeout(60 * 5, TimeUnit.SECONDS)
            .readTimeout(60 * 5, TimeUnit.SECONDS)
            .dispatcher(okhttp_dispatcher.get())
            .build();

    public static HttpUtils http(String url) {
        return new HttpUtils(url);
    }


    private RequestBody _body;

    private Request.Builder _builder;


    public HttpUtils(String url) {
        _builder = new Request.Builder().url(url);
    }


    //@XNote("设置请求头")
    public HttpUtils header(String name, String value) {
        if (name == null || value == null) {
            return this;
        }

        _builder.header(name, value);
        return this;
    }

    //@XNote("设置BODY txt及内容类型")
    public HttpUtils bodyTxt(String txt, String contentType) {
        if (txt == null) {
            return this;
        }

        if (contentType == null) {
            _body = FormBody.create(null, txt);
        } else {
            _body = FormBody.create(MediaType.parse(contentType), txt);
        }

        return this;
    }


    //@XNote("执行请求，返回响应对象")
    public Response exec(String mothod) throws IOException {

        switch (mothod.toUpperCase()) {
            case "GET":
                _builder.method("GET", null);
                break;
            case "POST":
                _builder.method("POST", _body);
                break;
            case "PUT":
                _builder.method("PUT", _body);
                break;
            case "DELETE":
                _builder.method("DELETE", _body);
                break;
            case "PATCH":
                _builder.method("PATCH", _body);
                break;
            case "HEAD":
                _builder.method("HEAD", null);
                break;
            case "OPTIONS":
                _builder.method("OPTIONS", null);
                break;
            case "TRACE":
                _builder.method("TRACE", null);
                break;
            default:
                throw new IllegalArgumentException("This method is not supported");
        }


        Call call = httpClient.newCall(_builder.build());
        return call.execute();
    }

    //@XNote("执行请求，返回字符串")
    public String exec2(String mothod) throws IOException {
        Response tmp = exec(mothod);

        int code = tmp.code();
        String text = tmp.body().string();
        if (code >= 200 && code <= 300) {
            return text;
        } else {
            throw new HttpResultException(code + " 错误：" + text);
        }
    }

    //@XNote("执行请求，返回状态码")
    public int exec3(String mothod) throws IOException {
        return exec(mothod).code();
    }


    //@XNote("发起GET请求，返回字符串（RESTAPI.select 从服务端获取一或多项资源）")
    public String get() throws IOException {
        return exec2("GET");
    }

    //@XNote("发起POST请求，返回字符串（RESTAPI.create 在服务端新建一项资源）")
    public String post() throws IOException {
        return exec2("POST");
    }


    //@XNote("发起PUT请求，返回字符串（RESTAPI.update 客户端提供改变后的完整资源）")
    public String put() throws IOException {
        return exec2("PUT");
    }

    //@XNote("发起PATCH请求，返回字符串（RESTAPI.update 客户端提供改变的属性）")
    public String patch() throws IOException {
        return exec2("PATCH");
    }

    //@XNote("发起DELETE请求，返回字符串（RESTAPI.delete 从服务端删除资源）")
    public String delete() throws IOException {
        return exec2("DELETE");
    }

    public int head() throws IOException {
        return exec3("HEAD");
    }
}
