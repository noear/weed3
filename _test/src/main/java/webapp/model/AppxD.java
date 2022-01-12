package webapp.model;

public final class AppxD {
    final Integer app_id;
    final String app_key;

    public AppxD(Integer app_id, String app_key) {
        this.app_id = app_id;
        this.app_key = app_key;
    }

    public Integer app_id() {
        return app_id;
    }

    public String app_key() {
        return app_key;
    }
}
