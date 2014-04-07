package classes;

import javax.swing.JFrame;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import datastructures.Enums.StudentType;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import datastructures.Enums.DancerType;
import datastructures.Enums.ExperienceLevel;
import datastructures.Enums.YesNo;
import drivers.OnsiteRegisterer;

import javax.swing.SwingConstants;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

/**
 * A GUI to be used when registering attendees on site in STAMP
 *
 * @author benjamyn
 */
public class OnsiteRegistrationGUI
{

    private OnsiteRegisterer    or;
    private FestivalClassesTree cTree;

    private JFrame     frame;
    private JTextField textField;   // first name
    private JTextField textField_1; // last name
    private JTextField textField_2; // email
    private JTextField textField_3; // phone number
    private JComboBox  comboBox;    // student status
    private JComboBox  comboBox_1;  // dancer type
    private JComboBox  comboBox_2;  // experience level
    private JComboBox  comboBox_3;  // payment received?
    private JComboBox  comboBox_4;  // send eticket?
    private JTextField textField_4; // subtotal
    private JTextField textField_5; // tax
    private JTextField textField_6; // total

    /**
     * Create the application.
     */
    public OnsiteRegistrationGUI(OnsiteRegisterer or)
    {
        this.or = or;
        initialize();
        frame.setVisible(true);
        cTree.addCheckingListener(this);
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize()
    {
        frame = new JFrame();
        frame.setBounds(100, 100, 1400, 670);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new FormLayout(new ColumnSpec[]{
                                                                                FormFactory.RELATED_GAP_COLSPEC,
                                                                                ColumnSpec.decode("225px"),
                                                                                FormFactory.RELATED_GAP_COLSPEC,
                                                                                ColumnSpec.decode("225px"),
                                                                                FormFactory.RELATED_GAP_COLSPEC,
                                                                                ColumnSpec.decode("300px:grow"),
                                                                                FormFactory.RELATED_GAP_COLSPEC,},
                                                        new RowSpec[]{
                                                                             FormFactory.RELATED_GAP_ROWSPEC,
                                                                             RowSpec.decode("default:grow"),
                                                                             FormFactory.RELATED_GAP_ROWSPEC,}
        ));

        JPanel panel = new JPanel();
        frame.getContentPane().add(panel, "2, 2, fill, fill");
        panel.setLayout(new FormLayout(new ColumnSpec[]{
                                                               FormFactory.RELATED_GAP_COLSPEC,
                                                               ColumnSpec.decode("80dlu:grow"),
                                                               FormFactory.RELATED_GAP_COLSPEC,},
                                       getInformationRowSpec()
        ));

        JButton btnNewRegistrant = new JButton("New Registrant");
        btnNewRegistrant.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                or.newRegistrant();
            }
        });
        panel.add(btnNewRegistrant, "2, 2");

        JLabel lblFirstName = new JLabel("First Name");
        panel.add(lblFirstName, "2, 6");

        JLabel lblLastName = new JLabel("Last Name");
        panel.add(lblLastName, "2, 10");

        JLabel lblEmail = new JLabel("Email");
        panel.add(lblEmail, "2, 14");

        JLabel lblPhoneNumber = new JLabel("Phone Number");
        panel.add(lblPhoneNumber, "2, 18");

        JLabel lblStudentStatus = new JLabel("Student Status");
        panel.add(lblStudentStatus, "2, 22");

        JLabel lblDancerType = new JLabel("Dancer Type");
        panel.add(lblDancerType, "2, 26");

        JLabel lblExperienceLevel = new JLabel("Experience Level");
        panel.add(lblExperienceLevel, "2, 30");

        JLabel lblMarkAsPaid = new JLabel("Mark as Paid?");
        panel.add(lblMarkAsPaid, "2, 34");

        JLabel lblSendEticket = new JLabel("Send E-Ticket?");
        panel.add(lblSendEticket, "2, 38");

        JLabel lblTotal = new JLabel("Subtotal");
        panel.add(lblTotal, "2, 42");

        JButton btnSubmit = new JButton("Submit");
        btnSubmit.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                or.submit();
            }
        });

        JLabel lblTax = new JLabel("Tax");
        panel.add(lblTax, "2, 46");

        JLabel lblTotal_1 = new JLabel("Total");
        panel.add(lblTotal_1, "2, 50");
        panel.add(btnSubmit, "2, 54");

        JPanel panel_1 = new JPanel();
        frame.getContentPane().add(panel_1, "4, 2, fill, fill");
        panel_1.setLayout(new FormLayout(new ColumnSpec[]{
                                                                 FormFactory.RELATED_GAP_COLSPEC,
                                                                 ColumnSpec.decode("80dlu:grow"),
                                                                 FormFactory.RELATED_GAP_COLSPEC,},
                                         new RowSpec[]{
                                                              FormFactory.RELATED_GAP_ROWSPEC,
                                                              RowSpec.decode("30px"),
                                                              FormFactory.RELATED_GAP_ROWSPEC,
                                                              FormFactory.DEFAULT_ROWSPEC,
                                                              FormFactory.RELATED_GAP_ROWSPEC,
                                                              RowSpec.decode("19dlu"),
                                                              FormFactory.RELATED_GAP_ROWSPEC,
                                                              FormFactory.DEFAULT_ROWSPEC,
                                                              FormFactory.RELATED_GAP_ROWSPEC,
                                                              RowSpec.decode("19dlu"),
                                                              FormFactory.RELATED_GAP_ROWSPEC,
                                                              FormFactory.DEFAULT_ROWSPEC,
                                                              FormFactory.RELATED_GAP_ROWSPEC,
                                                              RowSpec.decode("19dlu"),
                                                              FormFactory.RELATED_GAP_ROWSPEC,
                                                              FormFactory.DEFAULT_ROWSPEC,
                                                              FormFactory.RELATED_GAP_ROWSPEC,
                                                              RowSpec.decode("19dlu"),
                                                              FormFactory.RELATED_GAP_ROWSPEC,
                                                              FormFactory.DEFAULT_ROWSPEC,
                                                              FormFactory.RELATED_GAP_ROWSPEC,
                                                              RowSpec.decode("19dlu"),
                                                              FormFactory.RELATED_GAP_ROWSPEC,
                                                              FormFactory.DEFAULT_ROWSPEC,
                                                              FormFactory.RELATED_GAP_ROWSPEC,
                                                              RowSpec.decode("19dlu"),
                                                              FormFactory.RELATED_GAP_ROWSPEC,
                                                              FormFactory.DEFAULT_ROWSPEC,
                                                              FormFactory.RELATED_GAP_ROWSPEC,
                                                              RowSpec.decode("19dlu"),
                                                              FormFactory.RELATED_GAP_ROWSPEC,
                                                              FormFactory.DEFAULT_ROWSPEC,
                                                              FormFactory.RELATED_GAP_ROWSPEC,
                                                              RowSpec.decode("19dlu"),
                                                              FormFactory.RELATED_GAP_ROWSPEC,
                                                              FormFactory.DEFAULT_ROWSPEC,
                                                              FormFactory.RELATED_GAP_ROWSPEC,
                                                              RowSpec.decode("19dlu"),
                                                              FormFactory.RELATED_GAP_ROWSPEC,
                                                              FormFactory.DEFAULT_ROWSPEC,
                                                              FormFactory.RELATED_GAP_ROWSPEC,
                                                              RowSpec.decode("19dlu"),
                                                              FormFactory.RELATED_GAP_ROWSPEC,
                                                              FormFactory.DEFAULT_ROWSPEC,
                                                              FormFactory.RELATED_GAP_ROWSPEC,
                                                              RowSpec.decode("19dlu"),
                                                              FormFactory.RELATED_GAP_ROWSPEC,
                                                              FormFactory.DEFAULT_ROWSPEC,
                                                              FormFactory.RELATED_GAP_ROWSPEC,
                                                              RowSpec.decode("19dlu"),
                                                              FormFactory.RELATED_GAP_ROWSPEC,
                                                              FormFactory.DEFAULT_ROWSPEC,
                                                              FormFactory.RELATED_GAP_ROWSPEC,
                                                              RowSpec.decode("19dlu"),}
        ));

        textField = new JTextField();
        panel_1.add(textField, "2, 6, fill, default");
        textField.setColumns(10);

        textField_1 = new JTextField();
        panel_1.add(textField_1, "2, 10, fill, default");
        textField_1.setColumns(10);

        textField_2 = new JTextField();
        panel_1.add(textField_2, "2, 14, fill, default");
        textField_2.setColumns(10);

        textField_3 = new JTextField();
        panel_1.add(textField_3, "2, 18, fill, default");
        textField_3.setColumns(10);

        comboBox = new JComboBox();
        comboBox.addItemListener(new ItemListener()
        {
            public void itemStateChanged(ItemEvent arg0)
            {
                or.changeTicketCost((StudentType) comboBox.getSelectedItem());
            }
        });
        comboBox.setModel(new DefaultComboBoxModel(StudentType.values()));
        panel_1.add(comboBox, "2, 22, fill, default");

        comboBox_1 = new JComboBox();
        comboBox_1.setModel(new DefaultComboBoxModel(DancerType.values()));
        panel_1.add(comboBox_1, "2, 26, fill, default");

        comboBox_2 = new JComboBox();
        comboBox_2.setModel(new DefaultComboBoxModel(ExperienceLevel.values()));
        panel_1.add(comboBox_2, "2, 30, fill, default");

        comboBox_3 = new JComboBox();
        comboBox_3.setModel(new DefaultComboBoxModel(YesNo.values()));
        panel_1.add(comboBox_3, "2, 34, fill, default");

        comboBox_4 = new JComboBox();
        comboBox_4.setModel(new DefaultComboBoxModel(YesNo.values()));
        panel_1.add(comboBox_4, "2, 38, fill, default");

        textField_4 = new JTextField();
        textField_4.setHorizontalAlignment(SwingConstants.RIGHT);
        textField_4.setEditable(false);
        panel_1.add(textField_4, "2, 42, fill, default");
        textField_4.setColumns(10);

        textField_5 = new JTextField();
        textField_5.setHorizontalAlignment(SwingConstants.RIGHT);
        textField_5.setEditable(false);
        panel_1.add(textField_5, "2, 46, fill, default");
        textField_5.setColumns(10);

        textField_6 = new JTextField();
        textField_6.setHorizontalAlignment(SwingConstants.RIGHT);
        textField_6.setEditable(false);
        panel_1.add(textField_6, "2, 50, fill, default");
        textField_6.setColumns(10);

        cTree = new FestivalClassesTree(or.getTreeModel((StudentType) comboBox.getSelectedItem()));
        frame.getContentPane().add(cTree.getScrollingPaneTree(), "6, 2, fill, fill");
        resetFields();
    }

    /**
     * Since the labels and the text boxes should be arranged the same way with respect
     * to the rows, this function returns the appropriate RowSpec array for their
     * arrangement
     *
     * @return a RowSpec array for the arrangement of the labels and text boxes in the GUI
     */
    private RowSpec[] getInformationRowSpec()
    {
        return new RowSpec[]{
                                    FormFactory.RELATED_GAP_ROWSPEC,
                                    RowSpec.decode("30px"),
                                    FormFactory.RELATED_GAP_ROWSPEC,
                                    FormFactory.DEFAULT_ROWSPEC,
                                    FormFactory.RELATED_GAP_ROWSPEC,
                                    RowSpec.decode("19dlu"),
                                    FormFactory.RELATED_GAP_ROWSPEC,
                                    FormFactory.DEFAULT_ROWSPEC,
                                    FormFactory.RELATED_GAP_ROWSPEC,
                                    RowSpec.decode("19dlu"),
                                    FormFactory.RELATED_GAP_ROWSPEC,
                                    FormFactory.DEFAULT_ROWSPEC,
                                    FormFactory.RELATED_GAP_ROWSPEC,
                                    RowSpec.decode("19dlu"),
                                    FormFactory.RELATED_GAP_ROWSPEC,
                                    FormFactory.DEFAULT_ROWSPEC,
                                    FormFactory.RELATED_GAP_ROWSPEC,
                                    RowSpec.decode("19dlu"),
                                    FormFactory.RELATED_GAP_ROWSPEC,
                                    FormFactory.DEFAULT_ROWSPEC,
                                    FormFactory.RELATED_GAP_ROWSPEC,
                                    RowSpec.decode("19dlu"),
                                    FormFactory.RELATED_GAP_ROWSPEC,
                                    FormFactory.DEFAULT_ROWSPEC,
                                    FormFactory.RELATED_GAP_ROWSPEC,
                                    RowSpec.decode("19dlu"),
                                    FormFactory.RELATED_GAP_ROWSPEC,
                                    FormFactory.DEFAULT_ROWSPEC,
                                    FormFactory.RELATED_GAP_ROWSPEC,
                                    RowSpec.decode("19dlu"),
                                    FormFactory.RELATED_GAP_ROWSPEC,
                                    FormFactory.DEFAULT_ROWSPEC,
                                    FormFactory.RELATED_GAP_ROWSPEC,
                                    RowSpec.decode("19dlu"),
                                    FormFactory.RELATED_GAP_ROWSPEC,
                                    FormFactory.DEFAULT_ROWSPEC,
                                    FormFactory.RELATED_GAP_ROWSPEC,
                                    RowSpec.decode("19dlu"),
                                    FormFactory.RELATED_GAP_ROWSPEC,
                                    FormFactory.DEFAULT_ROWSPEC,
                                    FormFactory.RELATED_GAP_ROWSPEC,
                                    RowSpec.decode("19dlu"),
                                    FormFactory.RELATED_GAP_ROWSPEC,
                                    FormFactory.DEFAULT_ROWSPEC,
                                    FormFactory.RELATED_GAP_ROWSPEC,
                                    RowSpec.decode("19dlu"),
                                    FormFactory.RELATED_GAP_ROWSPEC,
                                    FormFactory.DEFAULT_ROWSPEC,
                                    FormFactory.RELATED_GAP_ROWSPEC,
                                    RowSpec.decode("19dlu"),
                                    FormFactory.RELATED_GAP_ROWSPEC,
                                    FormFactory.DEFAULT_ROWSPEC,
                                    FormFactory.RELATED_GAP_ROWSPEC,
                                    RowSpec.decode("19dlu"),
        };
    }

    /**
     * Resets the fields and combo boxes in the GUI
     */
    public void resetFields()
    {
        textField.setText("");
        textField_1.setText("");
        textField_2.setText("");
        textField_3.setText("");
        textField_4.setText("$0.00");
        textField_5.setText("$0.00");
        textField_6.setText("$0.00");

        comboBox.setSelectedIndex(0);
        comboBox_1.setSelectedIndex(0);
        comboBox_2.setSelectedIndex(0);
        comboBox_3.setSelectedIndex(0);
        comboBox_4.setSelectedIndex(0);

        cTree.clearChecking();
    }

    /**
     * Changes the TreeModel used in the tree that contains all
     * the pass and class information
     *
     * @param tm TreeModel to use for the tree
     */
    public void setTreeModel(DefaultTreeModel tm)
    {
        cTree.setTreeModel(tm);
    }

    /**
     * Updates the subtotal, tax, and total cost found in the GUI based
     * on the input list of nodes that have been checked
     *
     * @param checkedLeaves list of leaf nodes that are checked
     */
    public void updateTotalCost(List<DefaultMutableTreeNode> checkedLeaves)
    {
        or.updateTotalCost(checkedLeaves, (StudentType) comboBox.getSelectedItem());
    }

    /**
     * Updates the subtotal, tax, and total cost found in the GUI based
     * on the given values
     *
     * @param subtotal subtotal cost
     * @param tax      cost in taxes alone
     */
    public void updateTotalCost(double subtotal, double tax)
    {
        textField_4.setText(String.format("$%02.2f", subtotal));
        textField_5.setText(String.format("$%02.2f", tax));
        textField_6.setText(String.format("$%02.2f", subtotal + tax));
    }

	/* ***** A BUNCH OF METHODS USED TO GET THE NEW REGISTRANT INFORMATION ****/

    public String getFirstName()
    {
        return textField.getText();
    }

    public String getLastName()
    {
        return textField_1.getText();
    }

    public String getEmail()
    {
        return textField_2.getText();
    }

    public String getPhoneNumber()
    {
        return textField_3.getText();
    }

    public StudentType getStudentStatus()
    {
        return (StudentType) comboBox.getSelectedItem();
    }

    public DancerType getDancerType()
    {
        return (DancerType) comboBox_1.getSelectedItem();
    }

    public ExperienceLevel getExperienceLevel()
    {
        return (ExperienceLevel) comboBox_2.getSelectedItem();
    }

    public YesNo getPaymentReceived()
    {
        return (YesNo) comboBox_3.getSelectedItem();
    }

    public YesNo getSendETicket()
    {
        return (YesNo) comboBox_4.getSelectedItem();
    }

    public double getTotal()
    {
        return Double.parseDouble(textField_6.getText().replace("$", ""));
    }

    public String getNumRegistrants()
    {
        return "FOOBAR";
    }

    public List<String> getListOfClasses()
    {
        List<DefaultMutableTreeNode> checkedLeaves = cTree.getCheckedLeafNodes();

        ArrayList<String> classList = new ArrayList<String>(checkedLeaves.size());

        return classList;
    }

    public List<DefaultMutableTreeNode> getCheckedList()
    {
        return cTree.getCheckedLeafNodes();
    }
}
