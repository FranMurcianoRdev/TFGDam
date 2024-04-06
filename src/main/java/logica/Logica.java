
package logica;
import java.sql.*;

/**
 *
 * @author franc
 */
public class Logica {

    public static boolean autenticar(String usuario, String password) {
        // Para buscar el usuario por nombre y contraseña
        String query = "SELECT * FROM login WHERE usuario = ? AND password = ?";
        
        try (Connection conn = Conexion.getConexion();
             PreparedStatement statement = conn.prepareStatement(query)) {
            // Se establecen los parámetros en el statement
            statement.setString(1, usuario);
            statement.setString(2, password);
            
            // Se ejecuta la consulta
            ResultSet resultSet = statement.executeQuery();
            
            // Si se encuentra el resultado, es que la autenticación es exitosa
            return resultSet.next();
        } catch (SQLException e) {
            System.err.println("Error al autenticar: " + e.getMessage());
            return false;
        }
    }
  
}


