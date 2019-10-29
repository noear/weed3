package webapp.dso;

import com.zaxxer.hikari.HikariDataSource;
import org.noear.weed.DbContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class Db2Config {
    @Bean(name = "dataSource", destroyMethod = "close")
    @ConfigurationProperties(prefix = "test.db2")
    @Primary
    public HikariDataSource dataSource() {
        return new HikariDataSource();
    }

    @Bean("db2")
    public DbContext db2(@Qualifier("dataSource") DataSource dataSource){
        return new DbContext().dataSourceSet(dataSource);
    }
}
