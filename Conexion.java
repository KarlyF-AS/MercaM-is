import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    public static Connection abrir() throws SQLException {
        String url  = "jdbc:postgresql://aws-0-eu-west-2.pooler.supabase.com:6543/postgres";
        String user = "postgres.yemzbvaexdjtmqabfacx";
        String pass = "Mercamais123.";
        return DriverManager.getConnection(url, user, pass);
    }
}
