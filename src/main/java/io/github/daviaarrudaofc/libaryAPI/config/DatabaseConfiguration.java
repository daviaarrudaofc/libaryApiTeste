package io.github.daviaarrudaofc.libaryAPI.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;


@Configuration
public class DatabaseConfiguration {

    @Value("${spring.datasource.url}")// para pegar do properties ou yml
    String url;
    @Value("${spring.datasource.username}")
    String username;
    @Value("${spring.datasource.password}")
    String password;
    @Value("${spring.datasource.driver-class-name}")
    String driver;

    //@Bean
    public DataSource dataSource(){ // datasource fraco// prove so uma conexao fraca
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setDriverClassName(driver);

        return ds;
    }
    @Bean
    public DataSource hikariDataSource(){// esse method cria e registra no Spring  um datasource e configura como
                                        // a aplicação se conecta ao banco e gerencianado um pool de ate 10 conexao
        HikariConfig config = new HikariConfig();
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName(driver);
        config.setJdbcUrl(url);

        config.setMaximumPoolSize(10);// gerencianado um pool de ate 10 conexao(requisição) RELACIONADA AO BANCO DE DADOS!
        config.setMinimumIdle(1);
        config.setPoolName("libary-db-pool:conexão");
        config.setMaxLifetime(600000);// medida em milisegundos
        config.setConnectionTimeout(100000); // o tempo que vai tentar para obter a conexao
        config.setConnectionTestQuery("select 1"); // vai testar se ta conectando com o banco

        return new HikariDataSource(config);
    }
}
