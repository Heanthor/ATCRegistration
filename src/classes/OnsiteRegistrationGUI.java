package classes;

import javax.swing.*;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import datastructures.Enums.StudentType;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import datastructures.Enums.DancerType;
import datastructures.Enums.ExperienceLevel;
import datastructures.Enums.YesNo;
import drivers.OnsiteRegisterer;

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
    private OnsiteRegisterer    m_registerer;
    private FestivalClassesTree m_festivalClassesTree;

    private JFrame                     m_frame;
    private JTextField                 m_firstNameField;
    private JTextField                 m_lastNameField;
    private JTextField                 m_emailField;
    private JTextField                 m_phoneNumField;
    private JComboBox<StudentType>     m_studentTypeBox;
    private JComboBox<DancerType>      m_dancerTypeBox;
    private JComboBox<ExperienceLevel> m_experienceLevelBoxBox;
    private JComboBox<YesNo>           m_shouldSendETicketBox;
    private JComboBox<Integer>         m_numRegistrantsBox;
    private JTextField                 m_secondRegFirstNameField;
    private JTextField                 m_secondRegLastNameField;
    private JTextField                 m_subtotalField;
    private JTextField                 m_taxField;
    private JTextField                 m_totalField;

    /**
     * Create the application.
     */
    public OnsiteRegistrationGUI(OnsiteRegisterer or)
    {
        m_registerer = or;
        initialize();
        m_frame.setVisible(true);
        m_festivalClassesTree.addCheckingListener(this);
    }

    /**
     * Initialize the contents of the m_frame.
     */
    private void initialize()
    {
        m_frame = new JFrame();
        m_frame.setBounds(100, 100, 1400, 780);
        m_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        m_frame.getContentPane().setLayout(new FormLayout(new ColumnSpec[]
                                                                  {
                                                                          FormFactory.RELATED_GAP_COLSPEC,
                                                                          ColumnSpec.decode("225px"),
                                                                          FormFactory.RELATED_GAP_COLSPEC,
                                                                          ColumnSpec.decode("225px"),
                                                                          FormFactory.RELATED_GAP_COLSPEC,
                                                                          ColumnSpec.decode("300px:grow"),
                                                                          FormFactory.RELATED_GAP_COLSPEC,
                                                                  },
                                                          new RowSpec[]
                                                                  {
                                                                          FormFactory.RELATED_GAP_ROWSPEC,
                                                                          RowSpec.decode("default:grow"),
                                                                          FormFactory.RELATED_GAP_ROWSPEC,
                                                                  }
        ));

        JPanel labelPanel = new JPanel();
        m_frame.getContentPane().add(labelPanel, "2, 2, fill, fill");
        labelPanel.setLayout(new FormLayout(new ColumnSpec[]
                                                    {
                                                            FormFactory.RELATED_GAP_COLSPEC,
                                                            ColumnSpec.decode("80dlu:grow"),
                                                            FormFactory.RELATED_GAP_COLSPEC,
                                                    },
                                            getInformationRowSpec()
        ));

        JButton btnNewRegistrant = new JButton("New Registrant");
        btnNewRegistrant.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                m_registerer.resetRegistrationGui();
            }
        });
        labelPanel.add(btnNewRegistrant, "2, 2");

        JLabel lblFirstName = new JLabel("First Name");
        labelPanel.add(lblFirstName, "2, 6");

        JLabel lblLastName = new JLabel("Last Name");
        labelPanel.add(lblLastName, "2, 10");

        JLabel lblEmail = new JLabel("Email");
        labelPanel.add(lblEmail, "2, 14");

        JLabel lblPhoneNumber = new JLabel("Phone Number");
        labelPanel.add(lblPhoneNumber, "2, 18");

        JLabel lblStudentStatus = new JLabel("Student Status");
        labelPanel.add(lblStudentStatus, "2, 22");

        JLabel lblDancerType = new JLabel("Dancer Type");
        labelPanel.add(lblDancerType, "2, 26");

        JLabel lblExperienceLevel = new JLabel("Experience Level");
        labelPanel.add(lblExperienceLevel, "2, 30");

        JLabel lblSendEticket = new JLabel("Send E-Ticket?");
        labelPanel.add(lblSendEticket, "2, 34");

        JLabel lblNumRegistrants = new JLabel("Number Registrants?");
        labelPanel.add(lblNumRegistrants, "2, 38");

        JLabel secondRegFirstNameLabel = new JLabel("Second Registrant First Name");
        labelPanel.add(secondRegFirstNameLabel, "2, 42");

        JLabel secondRegLastNameLabel = new JLabel("Second Registrant Last Name");
        labelPanel.add(secondRegLastNameLabel, "2, 46");

        JLabel lblTotal = new JLabel("Subtotal");
        labelPanel.add(lblTotal, "2, 50");

        JLabel lblTax = new JLabel("Tax");
        labelPanel.add(lblTax, "2, 54");

        JLabel lblTotal_1 = new JLabel("Total");
        labelPanel.add(lblTotal_1, "2, 58");

        JButton btnSubmit = new JButton("Submit");
        btnSubmit.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                m_registerer.submit();
            }
        });
        labelPanel.add(btnSubmit, "2, 62");

        JPanel dataInputPanel= new JPanel();
        m_frame.getContentPane().add(dataInputPanel, "4, 2, fill, fill");
        dataInputPanel.setLayout(new FormLayout(new ColumnSpec[]{
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

        m_firstNameField = new JTextField();
        dataInputPanel.add(m_firstNameField, "2, 6, fill, default");
        m_firstNameField.setColumns(10);

        m_lastNameField = new JTextField();
        dataInputPanel.add(m_lastNameField, "2, 10, fill, default");
        m_lastNameField.setColumns(10);

        m_emailField = new JTextField();
        dataInputPanel.add(m_emailField, "2, 14, fill, default");
        m_emailField.setColumns(10);

        m_phoneNumField = new JTextField();
        dataInputPanel.add(m_phoneNumField, "2, 18, fill, default");
        m_phoneNumField.setColumns(10);

        m_studentTypeBox = new JComboBox<>();
        m_studentTypeBox.addItemListener(new ItemListener()
        {
            public void itemStateChanged(ItemEvent arg0)
            {
                m_registerer.changeTicketCost((StudentType) m_studentTypeBox.getSelectedItem());
            }
        });
        m_studentTypeBox.setModel(new DefaultComboBoxModel<>(StudentType.values()));
        dataInputPanel.add(m_studentTypeBox, "2, 22, fill, default");

        m_dancerTypeBox = new JComboBox<>();
        m_dancerTypeBox.setModel(new DefaultComboBoxModel<>(DancerType.values()));
        dataInputPanel.add(m_dancerTypeBox, "2, 26, fill, default");

        m_experienceLevelBoxBox = new JComboBox<>();
        m_experienceLevelBoxBox.setModel(new DefaultComboBoxModel<>(ExperienceLevel.values()));
        dataInputPanel.add(m_experienceLevelBoxBox, "2, 30, fill, default");

        m_shouldSendETicketBox = new JComboBox<>();
        m_shouldSendETicketBox.setModel(new DefaultComboBoxModel<>(YesNo.values()));
        dataInputPanel.add(m_shouldSendETicketBox, "2, 34, fill, default");

        m_numRegistrantsBox = new JComboBox<>();
        m_numRegistrantsBox.setModel(new DefaultComboBoxModel<>(new Integer[]{1, 2}));
        dataInputPanel.add(m_numRegistrantsBox, "2, 38, fill, default");
        m_numRegistrantsBox.addItemListener(new ItemListener()
        {
            @Override
            public void itemStateChanged(ItemEvent e)
            {
                if (e.getStateChange() == ItemEvent.SELECTED)
                {
                    m_registerer.updateTotalCost(m_festivalClassesTree.getCheckedLeafNodes());
                }
            }
        });

        m_secondRegFirstNameField = new JTextField();
        dataInputPanel.add(m_secondRegFirstNameField, "2, 42, fill, default");
        m_secondRegFirstNameField.setColumns(10);

        m_secondRegLastNameField = new JTextField();
        dataInputPanel.add(m_secondRegLastNameField, "2, 46, fill, default");
        m_secondRegLastNameField.setColumns(10);

        m_subtotalField = new JTextField();
        m_subtotalField.setHorizontalAlignment(SwingConstants.RIGHT);
        m_subtotalField.setEditable(false);
        dataInputPanel.add(m_subtotalField, "2, 50, fill, default");
        m_subtotalField.setColumns(10);

        m_taxField = new JTextField();
        m_taxField.setHorizontalAlignment(SwingConstants.RIGHT);
        m_taxField.setEditable(false);
        dataInputPanel.add(m_taxField, "2, 54, fill, default");
        m_taxField.setColumns(10);

        m_totalField = new JTextField();
        m_totalField.setHorizontalAlignment(SwingConstants.RIGHT);
        m_totalField.setEditable(false);
        dataInputPanel.add(m_totalField, "2, 58, fill, default");
        m_totalField.setColumns(10);

        m_festivalClassesTree = new FestivalClassesTree(m_registerer.getTreeModel((StudentType) m_studentTypeBox.getSelectedItem()));
        m_frame.getContentPane().add(m_festivalClassesTree.getScrollingPaneTree(), "6, 2, fill, fill");
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
        m_firstNameField.setText("");
        m_lastNameField.setText("");
        m_emailField.setText("");
        m_phoneNumField.setText("");
        m_subtotalField.setText("$0.00");
        m_taxField.setText("$0.00");
        m_totalField.setText("$0.00");

        m_studentTypeBox.setSelectedIndex(0);
        m_dancerTypeBox.setSelectedIndex(0);
        m_experienceLevelBoxBox.setSelectedIndex(0);
        m_shouldSendETicketBox.setSelectedIndex(0);
        m_numRegistrantsBox.setSelectedIndex(0);
        m_secondRegFirstNameField.setText("");
        m_secondRegLastNameField.setText("");

        m_festivalClassesTree.clearChecking();
    }

    /**
     * Changes the TreeModel used in the tree that contains all
     * the pass and class information
     *
     * @param tm TreeModel to use for the tree
     */
    public void setTreeModel(DefaultTreeModel tm)
    {
        m_festivalClassesTree.setTreeModel(tm);
    }

    /**
     * Updates the subtotal, tax, and total cost found in the GUI based
     * on the input list of nodes that have been checked
     *
     * @param checkedLeaves list of leaf nodes that are checked
     */
    public void updateTotalCost(List<DefaultMutableTreeNode> checkedLeaves)
    {
        m_registerer.updateTotalCost(checkedLeaves);
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
        m_subtotalField.setText(String.format("$%02.2f", subtotal));
        m_taxField.setText(String.format("$%02.2f", tax));
        m_totalField.setText(String.format("$%02.2f", subtotal + tax));
    }

	/* ***** A BUNCH OF METHODS USED TO GET THE NEW REGISTRANT INFORMATION ****/

    public String getFirstName()
    {
        return m_firstNameField.getText();
    }

    public String getLastName()
    {
        return m_lastNameField.getText();
    }

    public String getEmail()
    {
        return m_emailField.getText();
    }

    public String getPhoneNumber()
    {
        return m_phoneNumField.getText();
    }

    public StudentType getStudentStatus()
    {
        return (StudentType) m_studentTypeBox.getSelectedItem();
    }

    public DancerType getDancerType()
    {
        return (DancerType) m_dancerTypeBox.getSelectedItem();
    }

    public ExperienceLevel getExperienceLevel()
    {
        return (ExperienceLevel) m_experienceLevelBoxBox.getSelectedItem();
    }

    public YesNo getSendETicket()
    {
        return (YesNo) m_shouldSendETicketBox.getSelectedItem();
    }

    public double getTotal()
    {
        return Double.parseDouble(m_totalField.getText().replace("$", ""));
    }

    public int getNumRegistrants()
    {
        return (Integer) m_numRegistrantsBox.getSelectedItem();
    }

    public String getSecondRegFirstName()
    {
        return m_secondRegFirstNameField.getText();
    }

    public String getSecondRegLastName()
    {
        return m_secondRegLastNameField.getText();
    }

    public List<String> getListOfClasses()
    {
        List<DefaultMutableTreeNode> checkedLeaves = m_festivalClassesTree.getCheckedLeafNodes();

        ArrayList<String> classList = new ArrayList<String>(checkedLeaves.size());

        return classList;
    }

    public List<DefaultMutableTreeNode> getCheckedList()
    {
        return m_festivalClassesTree.getCheckedLeafNodes();
    }
}
