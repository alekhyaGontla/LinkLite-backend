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

    @Value("${DB_USERNAME:postgres}")
    private String username;

    @Value("${DB_PASSWORD:}")
    private String password;

    @Bean
    @Primary
    public DataSource dataSource() {

        if (databaseUrl == null || databaseUrl.isBlank()) {
            throw new IllegalStateException(
                    "DATABASE_URL environment variable is not configured"
            );
        }

        String jdbcUrl = databaseUrl.trim();

        // Convert PostgreSQL URLs to JDBC format
        if (jdbcUrl.startsWith("postgres://")) {
            jdbcUrl = "jdbc:postgresql://"
                    + jdbcUrl.substring("postgres://".length());

        } else if (jdbcUrl.startsWith("postgresql://")) {
            jdbcUrl = "jdbc:postgresql://"
                    + jdbcUrl.substring("postgresql://".length());

        } else if (!jdbcUrl.startsWith("jdbc:postgresql://")) {
            throw new IllegalArgumentException(
                    "Invalid DATABASE_URL. Expected PostgreSQL JDBC URL."
            );
        }

        // Add SSL for Supabase when not already specified
        if (!jdbcUrl.contains("?")) {
            jdbcUrl += "?sslmode=require";
        }

        System.out.println("Connecting to PostgreSQL: "
                + jdbcUrl.replaceAll(
                        "(?i)(://)([^:@]+):([^@]+)@",
                        "$1$2:****@"
                ));

        return DataSourceBuilder.create()
                .driverClassName("org.postgresql.Driver")
                .url(jdbcUrl)
                .username(username)
                .password(password)
                .build();
    }
}
