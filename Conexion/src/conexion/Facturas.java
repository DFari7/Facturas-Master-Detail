/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package conexion;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author Diego
 */
public class Facturas {
    
    private JTable TablaProductos = new JTable(new DefaultTableModel(new Object[]{"Codigo", "Descripcion", "Unidad Medida", "Precio","Cantidad","Subtotal"}, 0));
    
    public void vistaProducto(JTable tablaPro, JComboBox<String> codigo, JTextField cantidad){
        Conexion objetoConexion = new Conexion();
        DefaultTableModel modelo = (DefaultTableModel) tablaPro.getModel();
        
        String sql = "SELECT PROCODIGO, PRODESCRIPCION, PROUNIDADMEDIDA, PROPRECIOUM FROM PRODUCTOS WHERE PROCODIGO=?";
        
        Connection conn = null;
        PreparedStatement sta = null;
        ResultSet rs = null;
        
        try{
            conn = objetoConexion.establecerConection();
            if(conn != null){
                sta = conn.prepareStatement(sql);
                sta.setString(1, codigo.getSelectedItem().toString());
                rs = sta.executeQuery();
                while(rs.next()){
                    Object[] datos = new Object[6];
                    datos[0] = rs.getString("PROCODIGO");
                    datos[1] = rs.getString("PRODESCRIPCION");
                    datos[2] = rs.getString("PROUNIDADMEDIDA");
                    datos[3] = rs.getString("PROPRECIOUM");
                    datos[4] = Double.parseDouble(cantidad.getText().toString());
                    datos[5] = Double.parseDouble(cantidad.getText().toString()) * Double.parseDouble(datos[3].toString());
                    
                    modelo.addRow(datos);
                }
               
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "No se pudo agregar el producto: " + e.getMessage());
        }
        
    }
    
    public void mainVistaTabla(JTable selectedTable){
        Conexion objetoConexion = new Conexion();
        DefaultTableModel modelTable = new DefaultTableModel();
        
        TableRowSorter<TableModel> OrdenarTabla = new TableRowSorter<TableModel>(modelTable);
        
        selectedTable.setRowSorter(OrdenarTabla);
        
        modelTable.addColumn("Factura No.");
        modelTable.addColumn("Cliente");
        modelTable.addColumn("Fecha");
        modelTable.addColumn("Subtotal");
        modelTable.addColumn("IVA");
        modelTable.addColumn("Total");
        
        selectedTable.setModel(modelTable);
        
        String sql= "SELECT f.FACNUMERO, c.CLINOMBRE, f.FACFECHA, f.FACSUBTOTAL, f.FACIVA, (f.FACSUBTOTAL + f.FACIVA) AS TOTAL " +
            "FROM FACTURAS f " +
            "JOIN CLIENTES c ON f.CLICODIGO = c.CLICODIGO";

        Connection conexion = null;
        Statement statement = null;
        try{
            conexion = objetoConexion.establecerConection();
            if(conexion != null){
                statement = conexion.createStatement();
                ResultSet rs = statement.executeQuery(sql);
                while(rs.next()){
                    Object[] datos = new Object[6];
                    datos[0] = rs.getString("FACNUMERO");
                    datos[1] = rs.getString("CLINOMBRE");
                    datos[2] = rs.getString("FACFECHA");
                    datos[3] = rs.getDouble("FACSUBTOTAL");
                    datos[4] = rs.getDouble("FACIVA");
                    datos[5] = rs.getDouble("TOTAL");
                    
                    modelTable.addRow(datos);
                }
                selectedTable.setModel(modelTable);
            }
        } catch (SQLException e){
            JOptionPane.showMessageDialog(null, "No se pudieron mostrar los registros: " + e.getMessage());
        }
        
    }
    
    public void fillComboProductos(JComboBox<String> comboBox){
        String sql ="SELECT PROCODIGO FROM PRODUCTOS";
        Conexion conn = new Conexion();
        Connection conexion = null;
        Statement statement = null;
        
        try{
            conexion = conn.establecerConection();
            if(conexion != null){
                statement = conexion.createStatement();
                ResultSet rs = statement.executeQuery(sql);
                while(rs.next()){
                    String item =rs.getString("PROCODIGO");
                    comboBox.addItem(item);
                }
            }
            
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al cargar los productos: " + e.getMessage());
        }
    }
    
    public void fillComboClientes(JComboBox<String> combocli){
        String sql ="SELECT CLICODIGO FROM CLIENTES";
        Conexion conn = new Conexion();
        Connection conexion = null;
        Statement statement = null;
        
        try{
            conexion = conn.establecerConection();
            if(conexion != null){
                statement = conexion.createStatement();
                ResultSet rs = statement.executeQuery(sql);
                while(rs.next()){
                    String item =rs.getString("CLICODIGO");
                    combocli.addItem(item);
                }
            }
            
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al cargar los clientes: " + e.getMessage());
        }
    }
    
    public void seleccion(JTextField nombre, JComboBox producto){
        String sql="SELECT PRODESCRIPCION FROM PRODUCTOS WHERE PROCODIGO = ?";
        Conexion conn = new Conexion();
        Connection conexion = null;
        PreparedStatement statement = null;
        ResultSet registroInsert = null;
        try{
            conexion = conn.establecerConection();
            if(conexion != null){
                statement = conexion.prepareStatement(sql);
                statement.setString(1, producto.getSelectedItem().toString());
                registroInsert = statement.executeQuery();
                if (registroInsert.next()) {
                String descripcion = registroInsert.getString("PRODESCRIPCION");
                nombre.setText(descripcion);
                } else {
                JOptionPane.showMessageDialog(null, "No se encontró el producto con el código especificado.");
                }
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al encontrar el producto: " + e.getMessage());
        }
    }
    
    public void seleccionCliente(JTextField codigo, JComboBox nombre){
        String sql="SELECT CLINOMBRE FROM CLIENTES WHERE CLICODIGO = ?";
        Conexion conn = new Conexion();
        Connection conexion = null;
        PreparedStatement statement = null;
        ResultSet registroInsert = null;
        try{
            conexion = conn.establecerConection();
            if(conexion != null){
                statement = conexion.prepareStatement(sql);
                statement.setString(1, nombre.getSelectedItem().toString());
                registroInsert = statement.executeQuery();
                if (registroInsert.next()) {
                String descripcion = registroInsert.getString("CLINOMBRE");
                codigo.setText(descripcion);
                } else {
                JOptionPane.showMessageDialog(null, "No se encontró el producto con el código especificado.");
                }
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al encontrar el producto: " + e.getMessage());
        }
    }
    
    public String obtenerNuevoNumeroFactura() {
        Conexion conn = new Conexion();
        Connection connection = conn.establecerConection();
        String ultimoNumeroFactura = null;
        String query = "SELECT MAX(FACNUMERO) AS ultimoNumero FROM FACTURAS";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                ultimoNumeroFactura = resultSet.getString("ultimoNumero");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (ultimoNumeroFactura != null && !ultimoNumeroFactura.isEmpty()) {
            try {
                int ultimoNumero = Integer.parseInt(ultimoNumeroFactura.replace("FAC-", ""));
                return "FAC-" + String.format("%03d", ultimoNumero + 1);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return "FAC-001";
            }
        } else {
            return "FAC-001";
        }
    }
    
    public void obtenertotal(JTable tablaPro, JTextField subtotal, JTextField iva, JTextField total){
        DefaultTableModel modelo = (DefaultTableModel) tablaPro.getModel();
        double sumaTotal = 0.0;
        for(int i = 0; i< modelo.getRowCount();i++){
            sumaTotal += Double.parseDouble(modelo.getValueAt(i, 5).toString());
        }
        iva.setText(String.format("$%.2f",sumaTotal*0.15));
        subtotal.setText(String.format("$%.2f",sumaTotal));
        total.setText(String.format("$%.2f",sumaTotal+(sumaTotal*0.15)));
    }
    
    public void eliminarFilaSeleccionada(JTable tablaPro) {
        DefaultTableModel modelo = (DefaultTableModel) tablaPro.getModel();
        int filaSeleccionada = tablaPro.getSelectedRow();

        if (filaSeleccionada != -1) { // Verifica si hay una fila seleccionada
            modelo.removeRow(filaSeleccionada); // Elimina la fila del modelo
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione una fila para eliminar.");
        }
    }
    
    public void generarFactura(JTable tablaPro,JTextField factura,JComboBox codigo,JLabel fecha,JTextField subtotal,JTextField iva,JComboBox formapago ){
        Conexion conn = new Conexion();
        Connection conexion = conn.establecerConection();
        DefaultTableModel modelo = (DefaultTableModel) tablaPro.getModel();
        PreparedStatement sta = null;
        PreparedStatement sta2 = null;
        String sql1 = "INSERT INTO FACTURAS (FACNUMERO,CLICODIGO,FACFECHA,FACSUBTOTAL,FACIVA,FACFORMAPAGO,FACSTATUS) VALUES (?,?,?,?,?,?,'PAG');";
        String sql2 = "INSERT INTO PXF (FACNUMERO,PROCODIGO,PXFCANTIDAD,PXFVALOR,PXFSUBTOTAL,PXFSTATUS) VALUES(?,?,?,?,?,'ACT');";
        
        try{
            conexion.setAutoCommit(false);
            sta = conexion.prepareStatement(sql1);

            sta.setString(1, factura.getText());
            sta.setString(2, codigo.getSelectedItem().toString());
            sta.setString(3, fecha.getText());
            sta.setString(4,subtotal.getText().replace("$", ""));
            sta.setString(5, iva.getText().replace("$", ""));
            sta.setString(6, formapago.getSelectedItem().toString());
            
            sta.executeUpdate();
            
            sta2 = conexion.prepareStatement(sql2);
            for (int i = 0; i < modelo.getRowCount(); i++) {
                String procodigo = (String) modelo.getValueAt(i, 0);
                String descripcion = (String) modelo.getValueAt(i, 1);
                String unidadMedida = (String) modelo.getValueAt(i, 2);
                double precio = Double.parseDouble(modelo.getValueAt(i, 3).toString());
                double cantidad = Double.parseDouble(modelo.getValueAt(i, 4).toString());
                double subtotall = Double.parseDouble(modelo.getValueAt(i, 5).toString());

                sta2.setString(1, factura.getText());
                sta2.setString(2, procodigo);
                sta2.setDouble(3, cantidad);
                sta2.setDouble(4, precio);
                sta2.setDouble(5, subtotall);
                sta2.addBatch();
                //System.out.println("Producto añadido al batch: " + procodigo);
            }
            sta2.executeBatch();
            conexion.commit();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error:."+ e.getMessage());
        }

    }
    
    public void detail(JTable table, JTextField cliente, JTextField factura, JTextField fecha, JTextField formapago, JTextField iva, JTextField subtotal, JTextField total) {
        Conexion conn = new Conexion();
        Connection conexion = conn.establecerConection();
        PreparedStatement sta = null;
        PreparedStatement sta2 = null;
        PreparedStatement sta3 = null;
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        ResultSet rs = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;

        String sql1 = "SELECT FACNUMERO, CLICODIGO, FACFECHA, FACFORMAPAGO, FACIVA, FACSUBTOTAL, (FACIVA + FACSUBTOTAL) AS TOTAL FROM FACTURAS WHERE FACNUMERO = ?;";
        String sql2 = "SELECT CLINOMBRE FROM CLIENTES WHERE CLICODIGO = ?;";
        String sql3 = "SELECT p.PROCODIGO, p.PRODESCRIPCION, p.PROUNIDADMEDIDA, px.PXFCANTIDAD, px.PXFVALOR, px.PXFSUBTOTAL FROM PXF px JOIN PRODUCTOS p ON px.PROCODIGO = p.PROCODIGO WHERE px.FACNUMERO = ?;";

        try {
            sta = conexion.prepareStatement(sql1);
            sta.setString(1, factura.getText());
            rs = sta.executeQuery();
        
            if (rs.next()) {
                String codcliente = rs.getString("CLICODIGO");
                fecha.setText(rs.getString("FACFECHA"));
                formapago.setText(rs.getString("FACFORMAPAGO"));
                iva.setText(rs.getString("FACIVA"));
                subtotal.setText(rs.getString("FACSUBTOTAL"));
                total.setText(rs.getString("TOTAL"));

                sta2 = conexion.prepareStatement(sql2);
                sta2.setString(1, codcliente);
                rs2 = sta2.executeQuery();

                if (rs2.next()) {
                    cliente.setText(rs2.getString("CLINOMBRE"));
                }

                sta3 = conexion.prepareStatement(sql3);
                sta3.setString(1, factura.getText());
                 rs3 = sta3.executeQuery();

                while (rs3.next()) {
                    Object[] datos = new Object[6];
                    datos[0] = rs3.getString("PROCODIGO");
                    datos[1] = rs3.getString("PRODESCRIPCION");
                    datos[2] = rs3.getString("PROUNIDADMEDIDA");
                    datos[3] = rs3.getDouble("PXFCANTIDAD");
                    datos[4] = rs3.getDouble("PXFVALOR");
                    datos[5] = rs3.getDouble("PXFSUBTOTAL");
                
                    modelo.addRow(datos);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar datos: " + e.getMessage());
        }
    }
}
