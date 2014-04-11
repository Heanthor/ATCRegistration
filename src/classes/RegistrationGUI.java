package classes;

import javax.swing.JFrame;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import datastructures.Registrant;
import drivers.PaymentValidator;

import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.awt.Color;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JScrollBar;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.SwingConstants;
import javax.swing.JList;

import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

import datastructures.Enums.SheetClientMode;
import datastructures.Enums.RegistrationMode;

/**
 * A GUI that uses the SheetClient class as a back-end to perform
 * operations upon the registration data.
 *
 * @author benjamyn
 */
public class RegistrationGUI
{
    /**
     * This class is provided as a wrapper for a {@link Registrant}
     * object so that the {@link #toString} method does not dump
     * all the information, but only the information we want to
     * show in the JList.
     *
     * @author benjamyn
     */
    private static class RegistrantWrapper implements Comparable<RegistrantWrapper>
    {
        public final Registrant reg;

        public RegistrantWrapper(Registrant reg)
        {
            this.reg = reg;
        }

        public String toString()
        {
            return reg.row + ": " + reg.name;
        }

        /**
         * Comparison based on last name
         *
         * @param rw
         * @return
         */
        public int compareTo(RegistrantWrapper rw)
        {
            return this.reg.compareTo(rw.reg);
        }
    }

    private static class JTextWrapPane extends JTextPane
    {
        private boolean wrapState = true;

        /*
         * Constructor
         */
        JTextWrapPane()
        {
            super();
        }

        public JTextWrapPane(StyledDocument p_oSdLog)
        {
            super(p_oSdLog);
        }

        public boolean getScrollableTracksViewportWidth()
        {
            return wrapState;
        }

        public void setLineWrap(boolean wrap)
        {
            wrapState = wrap;
        }

        public boolean getLineWrap(boolean wrap)
        {
            return wrapState;
        }
    }

    private final String spacing = "  "; // spacing used in formatting register info output

    private JFrame     frame;
    private JTextField textField;
    private JTextField textField_1;
    private JComboBox  comboBox;
    private JComboBox  comboBox_1;

    private JList<RegistrantWrapper>            list;
    private DefaultListModel<RegistrantWrapper> dlm;
    private ArrayList<Registrant>               allRegistrants;

    private JScrollPane      scrollPane;
    private StyledDocument   textPaneDoc;
    private PaymentValidator pv;

    /**
     * Create the application.
     */
    public RegistrationGUI(PaymentValidator pv)
    {
        this.pv = pv;
        initialize();
        frame.setVisible(true);
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize()
    {
        frame = new JFrame();
        frame.setBounds(100, 100, 1400, 790);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new FormLayout(new ColumnSpec[]{
                                                                                FormFactory.RELATED_GAP_COLSPEC,
                                                                                ColumnSpec.decode("506px:grow"),
                                                                                FormFactory.RELATED_GAP_COLSPEC,
                                                                                ColumnSpec.decode("250px"),
                                                                                FormFactory.RELATED_GAP_COLSPEC,},
                                                        new RowSpec[]{
                                                                             FormFactory.RELATED_GAP_ROWSPEC,
                                                                             RowSpec.decode("354px:grow"),}
        ));

        JPanel panel = new JPanel();
        frame.getContentPane().add(panel, "2, 2, fill, fill");
        panel.setLayout(new FormLayout(new ColumnSpec[]{
                                                               FormFactory.RELATED_GAP_COLSPEC,
                                                               ColumnSpec.decode("250px"),
                                                               FormFactory.RELATED_GAP_COLSPEC,
                                                               ColumnSpec.decode("150px:grow"),
                                                               FormFactory.RELATED_GAP_COLSPEC,},
                                       new RowSpec[]{
                                                            FormFactory.RELATED_GAP_ROWSPEC,
                                                            RowSpec.decode("30px"),
                                                            FormFactory.RELATED_GAP_ROWSPEC,
                                                            RowSpec.decode("300px:grow"),
                                                            FormFactory.RELATED_GAP_ROWSPEC,}
        ));

        JPanel panel_2 = new JPanel();
        panel.add(panel_2, "2, 2, fill, fill");
        panel_2.setLayout(new FormLayout(new ColumnSpec[]{
                                                                 FormFactory.RELATED_GAP_COLSPEC,
                                                                 ColumnSpec.decode("30px:grow"),
                                                                 FormFactory.RELATED_GAP_COLSPEC,
                                                                 ColumnSpec.decode("50px:grow"),
                                                                 FormFactory.RELATED_GAP_COLSPEC,},
                                         new RowSpec[]{
                                                              FormFactory.RELATED_GAP_ROWSPEC,
                                                              RowSpec.decode("25px:grow"),
                                                              FormFactory.RELATED_GAP_ROWSPEC,}
        ));

        JLabel lblFirstName = new JLabel("First Name");
        panel_2.add(lblFirstName, "2, 2, right, default");

        textField = new JTextField();
        panel_2.add(textField, "4, 2, fill, default");
        textField.setColumns(10);

        JPanel panel_3 = new JPanel();
        panel.add(panel_3, "4, 2, fill, fill");
        panel_3.setLayout(new FormLayout(new ColumnSpec[]{
                                                                 FormFactory.RELATED_GAP_COLSPEC,
                                                                 ColumnSpec.decode("30px:grow"),
                                                                 FormFactory.RELATED_GAP_COLSPEC,
                                                                 ColumnSpec.decode("50px:grow"),
                                                                 FormFactory.RELATED_GAP_COLSPEC,},
                                         new RowSpec[]{
                                                              FormFactory.RELATED_GAP_ROWSPEC,
                                                              RowSpec.decode("30px:grow"),
                                                              FormFactory.RELATED_GAP_ROWSPEC,}
        ));

        JLabel lblLastName = new JLabel("Last Name");
        panel_3.add(lblLastName, "2, 2, right, default");

        textField_1 = new JTextField();
        panel_3.add(textField_1, "4, 2, fill, default");
        textField_1.setColumns(10);

        JScrollPane scrollPane_1 = new JScrollPane();
        scrollPane_1.setViewportBorder(new LineBorder(new Color(0, 0, 0)));
        panel.add(scrollPane_1, "2, 4, fill, fill");

        JLabel lblRegistrants = new JLabel("Registrants");
        lblRegistrants.setHorizontalAlignment(SwingConstants.CENTER);
        scrollPane_1.setColumnHeaderView(lblRegistrants);

        dlm = new DefaultListModel<RegistrantWrapper>();
        list = new JList<RegistrantWrapper>(dlm);
        scrollPane_1.setViewportView(list);

        scrollPane = new JScrollPane();
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setViewportBorder(new LineBorder(new Color(0, 0, 0)));
        panel.add(scrollPane, "4, 4, fill, fill");

        JLabel lblClasses = new JLabel("Registrant Information");
        lblClasses.setHorizontalAlignment(SwingConstants.CENTER);
        scrollPane.setColumnHeaderView(lblClasses);

        JPanel panel_4 = new JPanel();
        FlowLayout flowLayout = (FlowLayout) panel_4.getLayout();
        flowLayout.setVgap(0);
        flowLayout.setHgap(0);
        flowLayout.setAlignment(FlowLayout.LEFT);
        panel_4.setBackground(Color.WHITE);
        panel_4.setAutoscrolls(true);
        scrollPane.setViewportView(panel_4);

        JTextWrapPane textPane = new JTextWrapPane();
        panel_4.add(textPane);
        textPane.setEditable(false);
        textPane.setLineWrap(false);
        textPaneDoc = textPane.getStyledDocument();

        JPanel panel_1 = new JPanel();
        frame.getContentPane().add(panel_1, "4, 2, fill, fill");
        panel_1.setLayout(new FormLayout(new ColumnSpec[]{
                                                                 FormFactory.RELATED_GAP_COLSPEC,
                                                                 ColumnSpec.decode("default:grow"),
                                                                 FormFactory.RELATED_GAP_COLSPEC,},
                                         new RowSpec[]{
                                                              FormFactory.RELATED_GAP_ROWSPEC,
                                                              FormFactory.DEFAULT_ROWSPEC,
                                                              FormFactory.RELATED_GAP_ROWSPEC,
                                                              FormFactory.DEFAULT_ROWSPEC,
                                                              FormFactory.RELATED_GAP_ROWSPEC,
                                                              FormFactory.DEFAULT_ROWSPEC,
                                                              FormFactory.RELATED_GAP_ROWSPEC,
                                                              FormFactory.DEFAULT_ROWSPEC,
                                                              FormFactory.RELATED_GAP_ROWSPEC,
                                                              FormFactory.DEFAULT_ROWSPEC,
                                                              FormFactory.RELATED_GAP_ROWSPEC,
                                                              FormFactory.DEFAULT_ROWSPEC,
                                                              FormFactory.RELATED_GAP_ROWSPEC,
                                                              FormFactory.DEFAULT_ROWSPEC,
                                                              FormFactory.RELATED_GAP_ROWSPEC,
                                                              FormFactory.DEFAULT_ROWSPEC,
                                                              FormFactory.RELATED_GAP_ROWSPEC,
                                                              FormFactory.DEFAULT_ROWSPEC,
                                                              FormFactory.RELATED_GAP_ROWSPEC,
                                                              FormFactory.DEFAULT_ROWSPEC,
                                                              FormFactory.RELATED_GAP_ROWSPEC,
                                                              FormFactory.DEFAULT_ROWSPEC,
                                                              FormFactory.RELATED_GAP_ROWSPEC,
                                                              FormFactory.DEFAULT_ROWSPEC,
                                                              FormFactory.RELATED_GAP_ROWSPEC,
                                                              FormFactory.DEFAULT_ROWSPEC,
                                                              FormFactory.RELATED_GAP_ROWSPEC,
                                                              FormFactory.DEFAULT_ROWSPEC,
                                                              FormFactory.RELATED_GAP_ROWSPEC,
                                                              FormFactory.DEFAULT_ROWSPEC,
                                                              FormFactory.RELATED_GAP_ROWSPEC,
                                                              FormFactory.DEFAULT_ROWSPEC,
                                                              FormFactory.RELATED_GAP_ROWSPEC,
                                                              FormFactory.DEFAULT_ROWSPEC,
                                                              FormFactory.RELATED_GAP_ROWSPEC,
                                                              FormFactory.DEFAULT_ROWSPEC,
                                                              FormFactory.RELATED_GAP_ROWSPEC,
                                                              FormFactory.DEFAULT_ROWSPEC,
                                                              FormFactory.RELATED_GAP_ROWSPEC,
                                                              FormFactory.DEFAULT_ROWSPEC,
                                                              FormFactory.RELATED_GAP_ROWSPEC,
                                                              FormFactory.DEFAULT_ROWSPEC,
                                                              FormFactory.RELATED_GAP_ROWSPEC,
                                                              FormFactory.DEFAULT_ROWSPEC,
                                                              FormFactory.RELATED_GAP_ROWSPEC,
                                                              FormFactory.DEFAULT_ROWSPEC,
                                                              FormFactory.RELATED_GAP_ROWSPEC,
                                                              FormFactory.DEFAULT_ROWSPEC,
                                                              FormFactory.RELATED_GAP_ROWSPEC,
                                                              FormFactory.DEFAULT_ROWSPEC,
                                                              FormFactory.RELATED_GAP_ROWSPEC,
                                                              FormFactory.DEFAULT_ROWSPEC,
                                                              FormFactory.RELATED_GAP_ROWSPEC,
                                                              FormFactory.DEFAULT_ROWSPEC,}
        ));

        JButton btnDelete = new JButton("Delete");
        btnDelete.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                pv.deleteRegistrants(getSelectedRegistrants());
            }
        });

        JButton btnListClasses = new JButton("Show Information");
        btnListClasses.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                pv.listRegistrantClasses(getSelectedRegistrants());

                // if you don't invoke a new thread later, the scroll bars may be updated
                // before the text in the JTextPane is updated
                SwingUtilities.invokeLater(new Runnable()
                {
                    public void run()
                    {
                        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
                        JScrollBar horizontalScrollBar = scrollPane.getHorizontalScrollBar();
                        verticalScrollBar.setValue(verticalScrollBar.getMinimum());
                        horizontalScrollBar.setValue(horizontalScrollBar.getMinimum());
                    }
                });
            }
        });

        JButton btnGetUnpaid = new JButton("Get Unpaid");
        btnGetUnpaid.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                pv.showUnpaidRegistrants();
            }
        });

        JButton btnGetAllRegistrants = new JButton("Get All Registrants");
        btnGetAllRegistrants.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                pv.showAllRegistrants();
            }
        });

        JButton btnFilter = new JButton("Filter");
        btnFilter.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                pv.filterRegistrantList(textField.getText(), textField_1.getText(), getAllRegistrantsList());
            }
        });
        panel_1.add(btnFilter, "2, 2");
        panel_1.add(btnGetAllRegistrants, "2, 6");

        JButton btnGetPaid = new JButton("Get Paid");
        btnGetPaid.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                pv.showPaidRegistrants();
            }
        });
        panel_1.add(btnGetPaid, "2, 10");
        panel_1.add(btnGetUnpaid, "2, 14");
        panel_1.add(btnListClasses, "2, 18");
        panel_1.add(btnDelete, "2, 22");

        JButton btnSendEticket = new JButton("Send E-Ticket");
        btnSendEticket.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                pv.sendETickets(getSelectedRegistrants());
            }
        });

        panel_1.add(btnSendEticket, "2, 34");

        JButton btnGetSummary = new JButton("Get Summary");
        btnGetSummary.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                pv.showSummary();
            }
        });

        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                pv.refresh();
            }
        });
        panel_1.add(btnRefresh, "2, 46");

        comboBox = new JComboBox();
        comboBox.setModel(new DefaultComboBoxModel(SheetClientMode.values()));
        comboBox.addItemListener(new ItemListener()
        {
            public void itemStateChanged(ItemEvent arg0)
            {
                pv.changeSheetClientMode((SheetClientMode) comboBox.getSelectedItem());
            }
        });
        panel_1.add(comboBox, "2, 50, fill, default");

        comboBox_1 = new JComboBox();
        comboBox_1.setModel(new DefaultComboBoxModel(RegistrationMode.values()));
        comboBox_1.addItemListener(new ItemListener()
        {
            public void itemStateChanged(ItemEvent arg0)
            {
                pv.changeRegistrationMode((RegistrationMode) comboBox_1.getSelectedItem());
            }
        });
        panel_1.add(comboBox_1, "2, 54, fill, default");
    }

    /**
     * Returns all the registrants currently known by the GUI
     *
     * @return all registrants listed in the GUI
     */
    private List<Registrant> getAllRegistrantsList()
    {
        return (List<Registrant>) allRegistrants.clone();
    }

    /**
     * Return all the registrants in the JList panel of the GUI.
     * This might be equal to the list returned by
     * {@see #getAllRegistrantsList()} due to filtering that
     * has been applied to the list.
     *
     * @return all registrants in the listed after the GUI filter is applied
     */
    private List<Registrant> getCurrentRegistrantList()
    {
        ArrayList<Registrant> regList = new ArrayList<Registrant>(dlm.size());

        for (int i = 0; i < dlm.size(); ++i)
            regList.add(dlm.get(i).reg);

        return regList;
    }

    /**
     * Get a list of registrants that have been selected by the user.
     *
     * @return a list of selected registrants
     */
    private List<Registrant> getSelectedRegistrants()
    {
        List<RegistrantWrapper> selectedReg = list.getSelectedValuesList();

        ArrayList<Registrant> regList = new ArrayList<Registrant>(selectedReg.size());
        for (RegistrantWrapper regWrap : selectedReg)
            regList.add(regWrap.reg);

        return regList;
    }

    /**
     * Clear the list of registrants in the gui
     */
    private void clearRegistrantList()
    {
        dlm.clear();
    }

    /**
     * This method differs from {@link #setShownRegistrantList(List)} because it changes
     * the list of known registrants internal to the object. The {@link #setShownRegistrantList(List)}
     * does not change the internal list, but only which registrants are show, which
     * is primarily used when we want to filter a list of registrants, but not loose
     * the information in the entire list.
     *
     * @param registrants list of registrants to be able to interact with in the GUI
     */
    public void setRegistrantList(List<Registrant> registrants)
    {
        // copy and sort registrant list
        allRegistrants = new ArrayList<Registrant>(registrants.size());
        for (Registrant reg : registrants)
            allRegistrants.add(reg);

        Collections.sort(allRegistrants);

        clearRegistrantList();
        for (Registrant reg : allRegistrants)
            dlm.addElement(new RegistrantWrapper(reg));
    }

    /**
     * @param registrants specific list of registrants to show in the GUI
     */
    public void setShownRegistrantList(List<Registrant> registrants)
    {
        // copy and sort registrant list
        ArrayList<Registrant> regsSorted = new ArrayList<Registrant>(registrants.size());
        for (Registrant reg : registrants)
            regsSorted.add(reg);

        Collections.sort(registrants);

        clearRegistrantList();

        for (Registrant reg : regsSorted)
            dlm.addElement(new RegistrantWrapper(reg));
    }

    /**
     * This method is used to connect the RegistrationGUI to a PaymentValidator
     * object so that when buttons (or other components that have listeners)
     * are clicked, the PaymentValidator can be notified to perform the
     * necessary processing
     *
     * @param pv
     */
    public void setPaymentValidator(PaymentValidator pv)
    {
        this.pv = pv;
    }

    private void clearClassesText()
    {
        try
        {
            textPaneDoc.remove(0, textPaneDoc.getLength());
        }
        catch (BadLocationException e)
        {
            new AtcErr("Error trying to clear out the list of classes.");
        }
    }

    /**
     * @param regs list of registrants to show information for
     */
    public void showRegistrantInformation(List<Registrant> regs)
    {
        // clear current classes
        clearClassesText();

        //  Define a registrant name attribute
        SimpleAttributeSet regNameAttrib = new SimpleAttributeSet();
        StyleConstants.setForeground(regNameAttrib, Color.RED);
        StyleConstants.setBackground(regNameAttrib, Color.YELLOW);
        StyleConstants.setBold(regNameAttrib, true);

        //  Define a registrant info attribute
        SimpleAttributeSet regInfoAttrib = new SimpleAttributeSet();
        StyleConstants.setForeground(regInfoAttrib, Color.BLUE);
        StyleConstants.setBackground(regInfoAttrib, Color.GREEN);

        //  Define a registrant class attribute
        SimpleAttributeSet regClassAttrib = new SimpleAttributeSet();
        StyleConstants.setForeground(regClassAttrib, Color.BLACK);
        StyleConstants.setBackground(regClassAttrib, Color.WHITE);

        //  Add text
        try
        {
            for (Registrant reg : regs)
            {
                textPaneDoc.insertString(textPaneDoc.getLength(), reg.name + "\n", regNameAttrib);
                textPaneDoc.insertString(textPaneDoc.getLength(), formatRegistrantInfo(reg), regInfoAttrib);

                for (String regClass : reg.getFilteredClasses())
                    textPaneDoc.insertString(textPaneDoc.getLength(), spacing + spacing + regClass + "\n", regClassAttrib);
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    private String formatRegistrantInfo(Registrant reg)
    {
        StringBuilder builder = new StringBuilder();
        addLineToBuilder(builder, 1, "Email: ", reg.email);
        addLineToBuilder(builder, 1, "Phone: ", reg.phone);
        addLineToBuilder(builder, 1, "Student Type: ", reg.studentType.toString());
        addLineToBuilder(builder, 1, "Dancer Type: ", reg.dancerType.toString());
        addLineToBuilder(builder, 1, "Experience Level: ", reg.expLvl.toString());
        addLineToBuilder(builder, 1, "Amount: ", String.valueOf(reg.amount));
        addLineToBuilder(builder, 1, "Num Registrants: ", String.valueOf(reg.numRegistrants));

        if (reg.hasSecondRegistrant())
        {
            addLineToBuilder(builder, 2, "First Name: ", reg.secondRegName.first);
            addLineToBuilder(builder, 2, "Last Name: ", reg.secondRegName.last);
        }

        addLineToBuilder(builder, 1, "ETicket Sent: ", String.valueOf(reg.hasEticketSent()));
        builder.append(spacing).append("Classes: \n");
        return builder.toString();
    }

    private void addLineToBuilder(StringBuilder builder, int numSpaces, String... args)
    {
        for (int i = 0; i < numSpaces; ++i) { builder.append(spacing); }
        for (String arg : args)             { builder.append(arg); }
        builder.append("\n");
    }

    /**
     * @param summary the summary to be shown in the GUI
     */
    public void showSummary(String summary)
    {
        clearClassesText();

        //  Define a registrant class attribute
        SimpleAttributeSet regSummaryAttrib = new SimpleAttributeSet();
        StyleConstants.setFontFamily(regSummaryAttrib, "Monospaced"); // use this font so spacing is pretty
        StyleConstants.setForeground(regSummaryAttrib, Color.BLACK);
        StyleConstants.setBackground(regSummaryAttrib, Color.WHITE);

        // add summary
        try
        {
            textPaneDoc.insertString(textPaneDoc.getLength(), summary, regSummaryAttrib);
        }
        catch (BadLocationException e)
        {
            new AtcErr("Error trying to update the registration summary");
        }
    }

    /**
     * Clear the panels in the GUI containing the list of registrants and class/summary information
     */
    public void clearPanels()
    {
        clearRegistrantList();
        clearClassesText();
    }
}
