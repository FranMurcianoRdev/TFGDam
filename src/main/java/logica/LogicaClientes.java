
package logica;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class LogicaClientes {
    
    public void imprimirTablaRegistro (){
        
        JFrame frame = new JFrame("Tabla de Clientes");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Nombre Cliente");
        model.addColumn("ID Cliente");
        model.addColumn("ID Transacción");
        model.addColumn("Cantidad");

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton printButton = new JButton("Imprimir");
        printButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                printTable(table);
            }
        });

        JButton sumButton = new JButton("Sumar Pagos");
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
            ResultSet resultSet = statement.executeQuery("SELECT ID_cliente, nombre_cliente, ID_transacción, cantidad FROM Clientes");

            while (resultSet.next()) {
                Object[] row = {
                        resultSet.getString("nombre_cliente"),
                        resultSet.getInt("ID_Cliente"),
                        resultSet.getInt("ID_transacción"),
                        resultSet.getFloat("cantidad"),
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
        float totalCobros = 0;

        for (int i = 0; i < model.getRowCount(); i++) {
            totalCobros += (float) model.getValueAt(i, 4);
        }

        Object[] totalRow = {"", "Total", totalCobros};
        model.addRow(totalRow); 
    }
  
  
    public void guardarDatos(JTable tabla) {
        
       String insertQuery = "INSERT INTO Clientes (nombre_cliente, ID_cliente, ID_transacción, cantidad) VALUES (?, ?, ?, ?)";

        try {
            // PreparedStatement para ejecutar la consulta
            PreparedStatement preparedStatement = Conexion.getConexion().prepareStatement(insertQuery);

            // Modelo de tabla asociado a la JTable del form
            DefaultTableModel model = (DefaultTableModel) tabla.getModel();

            // Para iterar sobre las filas del modelo de tabla
            for (int i = 0; i < model.getRowCount(); i++) {
                // Obtener los datos de la fila actual
                String nombreCliente = (String) model.getValueAt(i, 0);
                int idCliente = Integer.parseInt((String) model.getValueAt(i, 1));
                int idTransacción = Integer.parseInt((String) model.getValueAt(i, 2));                
                float cantidad = Float.parseFloat((String) model.getValueAt(i, 3));

                // Establecer los parámetros en el PreparedStatement
                preparedStatement.setString(1, nombreCliente);
                preparedStatement.setInt(2, idCliente);
                preparedStatement.setInt(3, idTransacción);
                preparedStatement.setFloat(4, cantidad);

                // Ejecutar la consulta para insertar los datos en la base de datos
                preparedStatement.executeUpdate();
            }

            JOptionPane.showMessageDialog(null, "Datos insertados correctamente en la base de datos.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
      
    }
    
    public void eliminarRegistro () {
        String deleteQuery = "DELETE FROM Clientes WHERE registro = (SELECT MAX(registro) FROM (SELECT registro FROM Proveedores) AS registros)";
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
