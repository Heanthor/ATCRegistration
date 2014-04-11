package classes;

import java.awt.Component;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import it.cnr.imaa.essi.lablib.gui.checkboxtree.CheckboxTree;
import it.cnr.imaa.essi.lablib.gui.checkboxtree.CheckboxTreeCellRenderer;
import it.cnr.imaa.essi.lablib.gui.checkboxtree.TreeCheckingEvent;
import it.cnr.imaa.essi.lablib.gui.checkboxtree.TreeCheckingListener;
import it.cnr.imaa.essi.lablib.gui.checkboxtree.TreeCheckingModel;
import it.cnr.imaa.essi.lablib.gui.checkboxtree.TreeCheckingModel.CheckingMode;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.TreePath;

/**
 * The FestivalClassesTree configures the underlying CheckBoxTree for
 * use in the GUI.
 *
 * @author benjamyn
 */
public class FestivalClassesTree
{
    private class FestivalTree extends CheckboxTree
    {
        // don't want the paths to be allowed to collapse
        @Override
        public void collapsePath(TreePath path)
        {
        }
    }

    private class CheckBoxTreeCellRenderer implements CheckboxTreeCellRenderer
    {
        // a bunch of objects used for rendering
        private JCheckBox checkBox = new JCheckBox();
        private JPanel    panel    = new JPanel();
        private JLabel    label    = new JLabel();

        public CheckBoxTreeCellRenderer()
        {
            label.setOpaque(false);
            checkBox.setEnabled(false);

            panel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
            panel.add(checkBox);
            panel.add(label);

            checkBox.setBackground(UIManager.getColor("Tree.textBackground"));
            panel.setBackground(UIManager.getColor("Tree.textBackground"));
        }

        @Override
        public boolean isOnHotspot(int x, int y)
        {
            return (checkBox.getBounds().contains(x, y));
        }

        @Override
        public Component getTreeCellRendererComponent(JTree tree,
                                                      Object value,
                                                      boolean selected,
                                                      boolean expanded,
                                                      boolean leaf,
                                                      int row,
                                                      boolean hasFocus)
        {
            //
            label.setText(value.toString());

            if (selected)
                label.setBackground(UIManager.getColor("Tree.selectionBackground"));
            else
                label.setBackground(UIManager.getColor("Tree.textBackground"));

            TreeCheckingModel checkingModel = ((CheckboxTree) tree).getCheckingModel();
            TreePath path = tree.getPathForRow(row);
            boolean enabled, checked;

            if (leaf)
            {
                enabled = checkingModel.isPathEnabled(path);
                checked = checkingModel.isPathChecked(path);
            }
            else
            {
                enabled = false;
                checked = false;
            }

            checkBox.setEnabled(enabled);
            checkBox.setSelected(checked);

            return panel;
        }
    }

    private class UpdateCostListener implements TreeCheckingListener
    {
        private OnsiteRegistrationGUI m_onsiteRegistrationGUI;

        public UpdateCostListener(OnsiteRegistrationGUI orGui)
        {
            m_onsiteRegistrationGUI = orGui;
        }

        public void valueChanged(TreeCheckingEvent e)
        {
            ArrayList<DefaultMutableTreeNode> checkedLeaves = new ArrayList<>();

            for (TreePath tp : m_festivalTree.getCheckingPaths())
            {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) tp.getLastPathComponent();

                // only add leaves
                if (node.isLeaf())
                {
                    checkedLeaves.add(node);
                }
            }

            m_onsiteRegistrationGUI.updateTotalCost(checkedLeaves);
        }
    }

    private FestivalTree m_festivalTree;
    private JScrollPane  m_scrollPane;

    /**
     * @param dtm TreeModel to use in creating the tree
     */
    public FestivalClassesTree(TreeModel dtm)
    {
        // set up tree
        m_festivalTree = new FestivalTree();
        m_festivalTree.setModel(dtm);
        m_festivalTree.getCheckingModel().setCheckingMode(CheckingMode.SIMPLE);
        m_festivalTree.setCellRenderer(new CheckBoxTreeCellRenderer());

        m_festivalTree.expandAll();
        m_festivalTree.setRootVisible(false);
        m_festivalTree.setToggleClickCount(0);

        // create JScrollPanel and add tree
        m_scrollPane = new JScrollPane();
        m_scrollPane.setViewportView(m_festivalTree);
    }

    /**
     * Checking listeners are used to call methods when items in the
     * tree have been checked or unchecked.
     *
     * @param orGui
     */
    public void addCheckingListener(OnsiteRegistrationGUI orGui)
    {
        m_festivalTree.addTreeCheckingListener(new UpdateCostListener(orGui));
    }

    /**
     * A helper method for GUIs in getting a JScrollPane object that
     * contains a configured CheckBoxTree
     *
     * @return a scrolling pane containing the tree
     */
    public JScrollPane getScrollingPaneTree()
    {
        return m_scrollPane;
    }

    /**
     * Returns a list of leaf nodes that have been checked
     *
     * @return a list of leaf nodes that have been checked
     */
    public List<DefaultMutableTreeNode> getCheckedLeafNodes()
    {
        ArrayList<DefaultMutableTreeNode> checkedLeaves = new ArrayList<DefaultMutableTreeNode>();

        for (TreePath tp : m_festivalTree.getCheckingPaths())
        {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tp.getLastPathComponent();
            // only add leaves
            if (node.isLeaf())
                checkedLeaves.add(node);
        }

        return checkedLeaves;
    }

    /**
     * Sets the underlying CheckBoxTree's TreeModel
     *
     * @param tm TreeModel to set the underlying CheckBoxTree to have
     */
    public void setTreeModel(TreeModel tm)
    {
        m_festivalTree.setModel(tm);
        // models start in a collapsed state, thus we need to re-expand
        m_festivalTree.expandAll();
    }

    /**
     * Clear all the special passes/milonagas/classes that have been selected
     */
    public void clearChecking()
    {
        m_festivalTree.clearChecking();
    }
}
