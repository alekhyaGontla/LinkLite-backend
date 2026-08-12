package com.linklite.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Value("${DATABASE_URL:}")
    private String databaseUrl;

    @Value("${spring.datasource.username:postgres}")
    private String defaultUsername;

    @Value("${spring.datasource.password:}")
    private String defaultPassword;

    @Bean
    @Primary
    public DataSource dataSource() {

        if (databaseUrl == null || databaseUrl.isBlank()) {
            throw new IllegalStateException(
                "DATABASE_URL environment variable is not configured"
            );
        }

        String jdbcUrl = databaseUrl;

        // Convert Render/Supabase postgres:// URL to JDBC format
        if (jdbcUrl.startsWith("postgres://")) {
            jdbcUrl = "jdbc:" + jdbcUrl;
        } else if (jdbcUrl.startsWith("postgresql://")) {
            jdbcUrl = "jdbc:" + jdbcUrl;
        }

        // Add SSL if not already specified
        if (!jdbcUrl.contains("?")) {
            jdbcUrl += "?sslmode=require";
        }

        return DataSourceBuilder.create()
                .driverClassName("org.postgresql.Driver")
                .url(jdbcUrl)
                .build();
    }
}
