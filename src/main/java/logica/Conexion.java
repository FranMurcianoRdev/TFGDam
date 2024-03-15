
package logica;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author franc
 */


public class Conexion {
    
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/TFG";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "Fco123321mr!";

    private static Connection conexion = null;

    // Método para establecer la conexión a la base de datos
    public static Connection getConexion() {
        try {
            if (conexion == null || conexion.isClosed()) {
                conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
                System.out.println("Conexión establecida correctamente.");
            }
        } catch (SQLException e) {
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
        }
        return conexion;
    }

    // Método para cerrar la conexión a la base de datos
    public static void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("Conexión cerrada correctamente.");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }
}

// public static void main(String[] args) {
        // Ejemplo de uso de la clase Conexion
       // Connection conn = Conexion.getConexion();
        // Realiza las consultas o las operaciones necesarias con la base de datos usando la conexión "conn"
        // No olvides cerrar la conexión cuando hayas terminado
       //Conexion.cerrarConexion();
 //

