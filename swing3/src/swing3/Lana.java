package swing3;

/**
 * @author gampledu
 */
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

class Lana extends JFrame implements ActionListener {

    private JButton b1, b2, b3;
    private JTextField c1, c2, c3, c4;
    private JLabel l1, l2, l3, l4;
    private JTable tabla;
    private DefaultTableModel modelo;

    public Lana() {
        setLayout(null);
        setTitle("Biblioteca");
        setSize(1000, 700);
        setLocation(200, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        b1 = new JButton("Agregar");
        b1.setBounds(350, 50, 150, 30);
        b1.addActionListener(this);
        add(b1);

        b2 = new JButton("Eliminar");
        b2.setBounds(350, 100, 150, 30);
        b2.addActionListener(this);
        add(b2);

        b3 = new JButton("Actualizar");
        b3.setBounds(350, 150, 150, 30);
        b3.addActionListener(this);
        add(b3);

        l1 = new JLabel("Titulo");
        l1.setBounds(50, 50, 150, 30);
        add(l1);

        l2 = new JLabel("Autor");
        l2.setBounds(50, 100, 150, 30);
        add(l2);

        l3 = new JLabel("Año");
        l3.setBounds(50, 150, 150, 30);
        add(l3);

        l4 = new JLabel("Genero");
        l4.setBounds(50, 200, 150, 30);
        add(l4);

        c1 = new JTextField();
        c1.setBounds(150, 50, 150, 30);
        add(c1);

        c2 = new JTextField();
        c2.setBounds(150, 100, 150, 30);
        add(c2);

        c3 = new JTextField();
        c3.setBounds(150, 150, 150, 30);
        add(c3);

        c4 = new JTextField();
        c4.setBounds(150, 200, 150, 30);
        add(c4);

        setVisible(true);

        modelo = new DefaultTableModel();
        modelo.addColumn("ID");
        modelo.addColumn("Titulo");
        modelo.addColumn("Autor");
        modelo.addColumn("Año");
        modelo.addColumn("Genero");

        tabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBounds(50, 250, 500, 300);
        add(scroll);

        cargarDatos();
    }

    public void cargarDatos() {
        Connection con = null;
        Statement sta = null;
        ResultSet rs;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/swing3",
                    "root", "n0m3l0");
            sta = con.createStatement();
            rs = sta.executeQuery("Select * from libro");

            modelo.setRowCount(0);

            while (rs.next()) {
                Object[] fila = {
                    rs.getString("id"),
                    rs.getString("titulo"),
                    rs.getString("autor"),
                    rs.getString("anio"),
                    rs.getString("genero")
                };
                modelo.addRow(fila);
            }

            con.close();

        } catch (Exception error) {
            JOptionPane.showMessageDialog(null, "El error es: " + error);
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String titulo = c1.getText();
        String autor = c2.getText();
        String anio = c3.getText();
        String genero = c4.getText();

        if (e.getSource() == b1) {
            if (titulo.isEmpty() || autor.isEmpty() || anio.isEmpty() || genero.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Ingrese datos correctamente");
                return;
            } else {
                try {
                    Connection con = null;
                    Statement sta = null;
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    con = DriverManager.getConnection("jdbc:mysql://localhost:3306/swing3", "root", "n0m3l0");
                    String sql = "Insert into libro (titulo, autor, anio, genero) values (?, ?, ?, ?)";
                    PreparedStatement ps = con.prepareStatement(sql);
                    ps.setString(1, titulo);
                    ps.setString(2, autor);
                    ps.setString(3, anio);
                    ps.setString(4, genero);
                    ps.executeUpdate();
                    con.close();

                    cargarDatos();
                } catch (Exception error) {
                    JOptionPane.showMessageDialog(null, "El error es: " + error);
                }
            }

        }

        if (e.getSource() == b2) {
            try {
                Connection con = null;
                Class.forName("com.mysql.cj.jdbc.Driver");
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/swing3", "root", "n0m3l0");
                int id = Integer.parseInt(tabla.getValueAt(tabla.getSelectedRow(), 0).toString());

                if (id >= 0) {
                    String sql1 = "delete from libro where id=" + id;
                    PreparedStatement ps = con.prepareStatement(sql1);
                    ps.executeUpdate();
                    ps.close();
                    con.close();

                    cargarDatos();

                } else {
                    JOptionPane.showMessageDialog(null, "No se ha seleccionado niguna fila");
                }
            } catch (Exception error) {
                JOptionPane.showMessageDialog(null, error);
            }
        }

        if (e.getSource() == b3) {
            cargarDatos();
        }

    }

}
