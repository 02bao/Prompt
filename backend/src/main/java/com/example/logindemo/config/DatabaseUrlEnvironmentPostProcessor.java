package com.example.logindemo.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

public class DatabaseUrlEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment env, SpringApplication application) {
        String jdbc = env.getProperty("JDBC_DATABASE_URL");
        if (jdbc == null || jdbc.isBlank()) {
            String dbUrl = env.getProperty("DATABASE_URL");
            if (dbUrl != null && dbUrl.startsWith("postgres://")) {
                // Parse postgres://user:pass@host:port/dbname
                try {
                    String stripped = dbUrl.substring("postgres://".length());
                    int at = stripped.indexOf('@');
                    String userinfo = at > 0 ? stripped.substring(0, at) : "";
                    String hostpart = at > 0 ? stripped.substring(at + 1) : stripped;
                    String[] up = userinfo.split(":", 2);
                    String user = up.length > 0 ? up[0] : "";
                    String pass = up.length > 1 ? up[1] : "";
                    String[] hostAndDb = hostpart.split("/", 2);
                    String hostAndPort = hostAndDb.length > 0 ? hostAndDb[0] : "";
                    String dbname = hostAndDb.length > 1 ? hostAndDb[1] : "";
                    String jdbcUrl = "jdbc:postgresql://" + hostAndPort + "/" + dbname + "?sslmode=require";

                    Map<String, Object> map = new HashMap<>();
                    map.put("JDBC_DATABASE_URL", jdbcUrl);
                    if (env.getProperty("DB_USER") == null && user != null && !user.isBlank()) map.put("DB_USER", user);
                    if (env.getProperty("DB_PASS") == null && pass != null && !pass.isBlank()) map.put("DB_PASS", pass);
                    env.getPropertySources().addFirst(new MapPropertySource("renderDatabaseUrl", map));
                } catch (Exception ex) {
                    // ignore parsing errors; leave original env values
                }
            }
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
