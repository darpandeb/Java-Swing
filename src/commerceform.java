import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class commerceform extends JDialog {
    private JTextField tfName;
    private JTextField tfEmail;
    private JRadioButton item1;
    private JRadioButton item2;
    private JButton btnSubmit;
    private JButton btnCancel;
    private JTextField tfAddress;
    private JPanel CommerceForm;


    public commerceform(JFrame parent){
        super(parent);
        setTitle("Welcome to Java Swing E-commerce Platform");
        setContentPane(CommerceForm);
        setMinimumSize(new Dimension(600,500));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);


        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitorder(); // adds a record in the database //
            }
        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // clears the java app //
            }
        });
        setVisible(true);
    }

    private void submitorder() {
        String name = tfName.getText();
        String email = tfEmail.getText();
        String address = tfAddress.getText();
        String item = "";
        if (item1.isSelected()) {

            item = "ITEM 1";
        }

        else if (item2.isSelected()) {

            item = "ITEM 2";
        }
        else {

            item = "";
        }

        // Validation //
        if(name.isEmpty() || email.isEmpty() || address.isEmpty() || item.isEmpty())
        {
            JOptionPane.showMessageDialog(this,
                    "Please enter the fields properly",
                    "try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        // add order to database //
        o1 = addordertodatabase(name,email,address,item);
        if(o1!=null)
        {
            dispose();
        }
        else
        {
            JOptionPane.showMessageDialog(this,
                    "Unexpected error occured",
                    "try again",
                    JOptionPane.ERROR_MESSAGE);

        }
    }
    public order o1;
    private order addordertodatabase(String name, String email, String address , String item )
    {
        order o1 = null;
        final String dburl = "jdbc:mysql://localhost:3306/java";
        final String USERNAME = "root";
        final String PASSWORD = "Darpan10@";

        try
        {
            Connection conn = DriverManager.getConnection(dburl,USERNAME,PASSWORD);

            Statement smt = conn.createStatement();
            String sql_query = "INSERT INTO orders (fname , email, address , item)"+" VALUES (?,?,?,?)";

            PreparedStatement preparedStatement = conn.prepareStatement(sql_query);
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,email);
            preparedStatement.setString(3,address);
            preparedStatement.setString(4,item);

            // verifying if the records are inserted //

            int addedrows = preparedStatement.executeUpdate();
            if(addedrows>0)
            {
                o1 =  new order();
                o1.name = name;
                o1.email=email;
                o1.address=address;
                o1.item= item;

            }
            smt.close();
            conn.close();

        }catch(Exception e){
            e.printStackTrace();

        }



        return o1;
    }

    public static void main(String[] args) {
        commerceform myapp = new commerceform(null);
        order o1 = myapp.o1;
        if(o1!=null)
        {
            System.out.println("Successfully placed order for customer name :  " +o1.name);
        }
        else {
            System.out.println("<<  Cancelled  >>");
        }

    }
}
