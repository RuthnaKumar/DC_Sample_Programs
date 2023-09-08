import com.me.devicemanagement.onpremise.tools.dbmigration.utils.DBMigrationUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MSSQLServerReachabilityChecker {

    public static void main(String[] args) {
        try {
        String dbUrl = "jdbc:sqlserver://RUTHNA-12510:1433;Domain=ZOHOCORP;authenticationScheme=NTLM;integratedSecurity=true;ssl=request";
        Connection conn = DriverManager.getConnection(dbUrl, "ruthna-12510", "Z0h011@./''");
        System.out.println("SQL Server connection established.");
        conn.close();
    } catch (SQLException e) {
        System.err.println("Error: " + e.getMessage());
        e.printStackTrace();
        System.err.println("SQL Server connection failed.");
    }
    }
}
