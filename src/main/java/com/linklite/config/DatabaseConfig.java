package com.linklite.config;

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

    // Accepts either scheme, e.g.:
    // postgres://user:pass@host:5432/dbname
    // postgresql://user:pass@host:5432/dbname
    @Value("${DATABASE_URL:}")
    private String databaseUrl;

    @Value("${spring.datasource.username:postgres}")
    private String defaultUsername;

    @Value("${spring.datasource.password:}")
    private String defaultPassword;

    @Bean
    @Primary
    public DataSource dataSource() throws URISyntaxException {
        boolean hasDatabaseUrl = !databaseUrl.isEmpty()
                && (databaseUrl.startsWith("postgres://") || databaseUrl.startsWith("postgresql://"));

        // Local development fallback if DATABASE_URL isn't set
        if (!hasDatabaseUrl) {
            return DataSourceBuilder.create()
                    .driverClassName("org.postgresql.Driver")
                    .url("jdbc:postgresql://localhost:5432/postgres")
                    .username(defaultUsername)
                    .password(defaultPassword)
                    .build();
        }

        // Parse a raw postgres:// or postgresql:// URL (Render, Supabase, etc.)
        URI dbUri = new URI(databaseUrl);

        String[] userInfo = dbUri.getUserInfo().split(":", 2);
        String username = userInfo[0];
        String password = userInfo.length > 1 ? userInfo[1] : "";

        int port = dbUri.getPort() == -1 ? 5432 : dbUri.getPort();
        String dbPath = dbUri.getPath(); // includes leading '/'

        String jdbcUrl = "jdbc:postgresql://" + dbUri.getHost() + ":" + port + dbPath;

        // Preserve any query params (e.g. sslmode=require); default to
        // requiring SSL for non-local hosts, since Supabase/Render enforce it.
        String query = dbUri.getQuery();
        jdbcUrl += (query != null && !query.isEmpty()) ? "?" + query : "?sslmode=require";

        return DataSourceBuilder.create()
                .driverClassName("org.postgresql.Driver")
                .url(jdbcUrl)
                .username(username)
                .password(password)
                .build();
    }
}
