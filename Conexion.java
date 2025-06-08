import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase responsable de establecer una conexión con la base de datos PostgreSQL.
 */
public class Conexion {

    /**
     * Abre y devuelve una conexión activa a la base de datos.
     * @return una instancia de {@link Connection} conectada a la base de datos PostgreSQL.
     * @throws SQLException si ocurre un error al intentar conectarse.
     */
    public static Connection abrir() throws SQLException {
        String url  = "jdbc:postgresql://aws-0-eu-west-2.pooler.supabase.com:6543/postgres";
        String user = "postgres.yemzbvaexdjtmqabfacx";
        String pass = "Tatofiguesaraykarly..";
        return DriverManager.getConnection(url, user, pass);
    }
}
