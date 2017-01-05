package ch.p3n.apps.appfront.domain.schema;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.io.IOUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Helper class which extensions can register as component and will get notified on application boot.
 * Extensions can register database scripts which will then be executed based on database type and flag if the schema is existing.
 *
 * @author deluc1
 * @author zempm3
 */
@Component
public class DatabaseSchemaInstaller implements ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseSchemaInstaller.class);

    private static final String MYSQL_SCHEMA_SCRIPT = "ch/p3n/apps/appfront/domain/schema/schema_mysql.sql";

    private static final String H2_SCHEMA_SCRIPT = "ch/p3n/apps/appfront/domain/schema/schema_h2.sql";

    private DataSource dataSource;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.dataSource = (DataSource) applicationContext.getBean("dataSource");
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        LOGGER.info(String.format("[SchemaBootstrap] running %s", this.getClass().getName()));

        // Get database type and check if schema is already installed.
        final boolean hasDatabaseTables = hasDatabaseTables();
        final String dbTypeString = getDatabaseProductName();
        try {
            final DatabaseType dbType = DatabaseType.valueOf(dbTypeString.toUpperCase());

            LOGGER.info("  Database type is : " + dbType);

            // Create schema if no database tables are existing.
            if (!hasDatabaseTables) {
                LOGGER.info("  No database table are currently existing, therefore installing schema...");
                createSchema(dbType);
            }
        } catch (IllegalArgumentException e) {
            LOGGER.error(String.format("  Unknown database type %s, aborting...", dbTypeString), e);
        }
    }

    /**
     * Checks the meta data of the database connection to gather the database type.
     *
     * @return database type.
     */
    private String getDatabaseProductName() {
        String dbProductName = null;
        Connection c = null;

        try {
            c = dataSource.getConnection();
            dbProductName = c.getMetaData().getDatabaseProductName();
        } catch (SQLException e) {
            LOGGER.error("Unable to select tables from database", e);
        } finally {
            DbUtils.closeQuietly(c);
        }
        return dbProductName;
    }

    /**
     * Executes a select all tables query. If any table is existing it claims that the schema is installed.
     *
     * @return {@code true} if any table is existing in the database.
     */
    private boolean hasDatabaseTables() {
        boolean hasDatabaseTables = false;

        try (Connection c = dataSource.getConnection(); Statement s = c.createStatement()) {

            // Select the tables to determine if the table is empty.
            s.execute("SHOW TABLES");
            try (ResultSet rs = s.getResultSet()) {
                while (rs.next()) {
                    hasDatabaseTables = true;
                    continue;
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Unable to select tables from database", e);
        }
        return hasDatabaseTables;
    }

    private void createSchema(final DatabaseType databaseType) {
        final String script = getDatabaseSchemaScripts(databaseType);
        if (script != null) {
            executeScripts(script);
        } else {
            LOGGER.error("  No schema creation scripts defined for database type : " + databaseType);
        }
    }

    private void executeScripts(final String databaseScript) {
        Connection c = null;
        Statement s = null;

        try {
            c = dataSource.getConnection();
            s = c.createStatement();

            // Disable foreign key checks.
            s.execute("SET foreign_key_checks = 0");

            // Create script runner
            final ScriptRunner runner = new ScriptRunner(c);
            runner.setAutoCommit(true);
            runner.setStopOnError(true);

            // Iterate over the database scripts.
            executeScript(runner, databaseScript);

            // Enable foreign key checks.
            s.execute("SET foreign_key_checks = 1");

        } catch (SQLException e) {
            LOGGER.error("Unable to execute database scripts", e);
        } finally {
            DbUtils.closeQuietly(s);
            DbUtils.closeQuietly(c);
        }
    }

    private void executeScript(final ScriptRunner runner, final String filePath) {
        LOGGER.info("  Executing script : " + filePath);

        Reader reader = null;
        try {

            // Read file input stream.
            reader = Resources.getResourceAsReader(filePath);

            // Run database script.
            runner.runScript(reader);

            // Log successful execution
            LOGGER.info("   -> DONE!");

        } catch (IOException e) {
            LOGGER.error("An io exception occurred while reading patch files", e);
        } finally {
            IOUtils.closeQuietly(reader);
        }
    }

    /**
     * Specify scripts which are claimed to be schema install scripts.
     *
     * @param databaseType database type.
     * @return list of database script buffered reader provider.
     */
    protected String getDatabaseSchemaScripts(final DatabaseType databaseType) {
        if (DatabaseType.H2.equals(databaseType)) {
            return H2_SCHEMA_SCRIPT;
        } else if (DatabaseType.MYSQL.equals(databaseType)) {
            return MYSQL_SCHEMA_SCRIPT;
        }
        return null;
    }

    /**
     * Database type enumeration.
     *
     * @author decla
     */
    public enum DatabaseType {
        H2, MYSQL
    }

}
