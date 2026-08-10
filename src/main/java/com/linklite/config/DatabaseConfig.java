package com.linklite.config; // <-- Change "com.example.linklite.config" to "com.linklite.config"

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class DatabaseConfig {

    @Value("${DATABASE_URL:}")
    private String databaseUrl;

    @Value("${spring.datasource.username:postgres}")
    private String defaultUsername;

    @Value("${spring.datasource.password:admin}")
    private String defaultPassword;

    @Bean
    @Primary
    public DataSource dataSource() throws URISyntaxException {
        // Fallback for local development if DATABASE_URL is empty or doesn't start with postgres://
        if (databaseUrl.isEmpty() || !databaseUrl.startsWith("postgres://")) {
            return DataSourceBuilder.create()
                    .driverClassName("org.postgresql.Driver")
                    .url("jdbc:postgresql://localhost:5432/postgres")
                    .username(defaultUsername)
                    .password(defaultPassword)
                    .build();
        }

        // Parse Render's raw postgres:// URL
        URI dbUri = new URI(databaseUrl);

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        
        int port = dbUri.getPort() == -1 ? 5432 : dbUri.getPort();
        String dbPath = dbUri.getPath(); // includes leading '/'
        
        String jdbcUrl = "jdbc:postgresql://" + dbUri.getHost() + ":" + port + dbPath;

        return DataSourceBuilder.create()
                .driverClassName("org.postgresql.Driver")
                .url(jdbcUrl)
                .username(username)
                .password(password)
                .build();
    }
}