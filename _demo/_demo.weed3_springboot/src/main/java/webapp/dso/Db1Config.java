package webapp.dso;

import lombok.Setter;
import org.noear.weed.DbContext;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.Properties;

@Setter
@ConfigurationProperties(prefix = "test")
public class Db1Config {
    protected Properties db;

    @Bean("db1")
    public DbContext db1(){
        return new DbContext(db);
    }

}
