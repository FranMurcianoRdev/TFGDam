
package logica;
import java.sql.*;

/**
 *
 * @author franc
 */
public class Logica {

    public static boolean autenticar(String usuario, String password) {
        // Query SQL para buscar el usuario por nombre y contraseña
        String query = "SELECT * FROM login WHERE usuario = ? AND password = ?";
        
        try (Connection conn = Conexion.getConexion();
             PreparedStatement statement = conn.prepareStatement(query)) {
            // Establecer los parámetros en el statement
            statement.setString(1, usuario);
            statement.setString(2, password);
            
            // Ejecutar la consulta
            ResultSet resultSet = statement.executeQuery();
            
            // Si hay al menos una fila en el resultado, la autenticación es exitosa
            return resultSet.next();
        } catch (SQLException e) {
            System.err.println("Error al autenticar: " + e.getMessage());
            return false;
        }
    }

   /* public static void main(String[] args) {
        // Ejemplo de autenticación
        String nombreUsuario = "usuario";
        String contrasena = "contraseña";
        
        if (autenticar(nombreUsuario, contrasena)) {
            System.out.println("Inicio de sesión exitoso.");
        } else {
            System.out.println("Nombre de usuario o contraseña incorrectos.");
        }
    }*/
}


