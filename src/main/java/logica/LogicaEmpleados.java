
package logica;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.sql.*;

public class LogicaEmpleados {
    
    public void imprimirTablaRegistro (){
        
        JFrame frame = new JFrame("Tabla de Empleados");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID Empleado");
        model.addColumn("Nombre");
        model.addColumn("Salario Mensual");
        model.addColumn("Salario Anual");

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton printButton = new JButton("Imprimir");
        printButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                printTable(table);
            }
        });

        JButton sumButton = new JButton("Sumar Salarios");
        sumButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sumSalaries(model);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(printButton);
        buttonPanel.add(sumButton);

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setSize(600, 400);
        frame.setVisible(true);

        loadTableData(model);
    }

    private static void loadTableData(DefaultTableModel model) {
        try {
            Connection connection = Conexion.getConexion();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT ID_Empleado, nombre_empleado, salario_mensual, salario_anual FROM Empleados");

            while (resultSet.next()) {
                Object[] row = {
                        resultSet.getInt("ID_Empleado"),
                        resultSet.getString("nombre_empleado"),
                        resultSet.getFloat("salario_mensual"),
                        resultSet.getFloat("salario_anual")
                };
                model.addRow(row);
            }

            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private static void printTable(JTable table) {
        PrinterJob printerJob = PrinterJob.getPrinterJob();
        printerJob.setPrintable(new Printable() {
            @Override
            public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                if (pageIndex > 0) {
                    return Printable.NO_SUCH_PAGE;
                }

                Graphics2D g2d = (Graphics2D) graphics;
                g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

                table.print(g2d);
                return Printable.PAGE_EXISTS;
            }
        });

        if (printerJob.printDialog()) {
            try {
                printerJob.print();
            } catch (PrinterException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static void sumSalaries(DefaultTableModel model) {
        float totalMensual = 0;
        float totalAnual = 0;

        for (int i = 0; i < model.getRowCount(); i++) {
            totalMensual += (float) model.getValueAt(i, 2);
            totalAnual += (float) model.getValueAt(i, 3);
        }

        Object[] totalRow = {"", "Total", totalMensual, totalAnual};
        model.addRow(totalRow); 
    }
  
    
    public void guardarDatos(JTable tabla) {
        
       String insertQuery = "INSERT INTO Empleados (nombre_empleado, ID_Empleado, salario_mensual, salario_anual) VALUES (?, ?, ?, ?)";

        try {
            // PreparedStatement para ejecutar la consulta
            PreparedStatement preparedStatement = Conexion.getConexion().prepareStatement(insertQuery);

            // Modelo de tabla asociado a la JTable de lform
            DefaultTableModel model = (DefaultTableModel) tabla.getModel();

            // Para iterar sobre las filas del modelo de tabla
            for (int i = 0; i < model.getRowCount(); i++) {
                // Obtener los datos de la fila actual
                String nombreEmpleado = (String) model.getValueAt(i, 0);
                int idEmpleado = Integer.parseInt((String) model.getValueAt(i, 1));
                float salarioMensual = Float.parseFloat((String) model.getValueAt(i, 2));
                float salarioAnual = Float.parseFloat((String) model.getValueAt(i, 3));

                // Establecer los parámetros en el PreparedStatement
                preparedStatement.setString(1, nombreEmpleado);
                preparedStatement.setInt(2, idEmpleado);
                preparedStatement.setFloat(3, salarioMensual);
                preparedStatement.setFloat(4, salarioAnual);

                // Ejecutar la consulta para insertar los datos en la base de datos
                preparedStatement.executeUpdate();
            }

            JOptionPane.showMessageDialog(null, "Datos insertados correctamente en la base de datos.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
      
    }
    
    public void eliminarRegistro () {
        String deleteQuery = "DELETE FROM Empleados WHERE registro = (SELECT MAX(registro) FROM (SELECT registro FROM Empleados) AS registros)";
        try {
            // Statement para ejecutar la consulta
            Statement statement = Conexion.getConexion().createStatement();

            // Ejecutar la consulta para eliminar el último registro
            int rowsAffected = statement.executeUpdate(deleteQuery);

            JOptionPane.showMessageDialog(null, "Se eliminó el último registro correctamente.");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    
}

    
    
    


 


    