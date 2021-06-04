package com.github.gdiazs.fortbiz.factories;

import org.flywaydb.core.Flyway;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.sql.DataSource;
import java.sql.SQLException;

@ApplicationScoped
public class FlywayProducer {

    @Resource(name = "jdbc/fortBizDs")
    private DataSource dataSource;

    public void startUp(@Observes @Initialized(ApplicationScoped.class) Object o) throws SQLException {
        Flyway flyway = Flyway.configure().dataSource(this.dataSource).load();
        flyway.migrate();
    }
}
