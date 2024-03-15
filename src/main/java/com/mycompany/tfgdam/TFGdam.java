

package com.mycompany.tfgdam;

import igu.FormEmpleados;
import igu.FormLogin;
import java.sql.Connection;
import logica.Conexion;

/**
 *
 * @author franc
 */
public class TFGdam {

    public static void main(String[] args) {
        /*FormLogin objLogin = new FormLogin();
        objLogin.setVisible(true);
        //Para que la ventana siempre aparezca en el medio: 
        objLogin.setLocationRelativeTo(null);
        */
        FormEmpleados obj = new FormEmpleados();
        obj.setVisible(true);
        
    }
}
