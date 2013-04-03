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
 *
 */
public class FestivalClassesTree {
	private class FestivalTree extends CheckboxTree {
		// don't want the paths to be allowed to collapse
		@Override
		public void collapsePath (TreePath path) {}
	}
	
	private class CheckBoxTreeCellRenderer implements CheckboxTreeCellRenderer {
		// a bunch of objects used for rendering
		private JCheckBox checkBox = new JCheckBox ();
		private JPanel panel = new JPanel ();
		private JLabel label = new JLabel ();
		
		public CheckBoxTreeCellRenderer () {
			label.setOpaque (false);
			checkBox.setEnabled (false);
			
			panel.setLayout (new FlowLayout (FlowLayout.LEFT, 0, 0));
			panel.add (checkBox);
			panel.add (label);
			
			checkBox.setBackground (UIManager.getColor ("Tree.textBackground"));
			panel.setBackground (UIManager.getColor ("Tree.textBackground"));
		}

		@Override
		public boolean isOnHotspot (int x, int y) {
			return (checkBox.getBounds ().contains (x, y));
		}
		
		@Override
		public Component getTreeCellRendererComponent (JTree tree, Object value,
				boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
			//
			label.setText (value.toString ());

			if (selected)
				label.setBackground (UIManager.getColor ("Tree.selectionBackground"));
			else
				label.setBackground (UIManager.getColor ("Tree.textBackground"));
			
			TreeCheckingModel checkingModel = ((CheckboxTree) tree).getCheckingModel ();
			TreePath path = tree.getPathForRow (row);
			boolean enabled, checked;
			
			if (leaf) {
				enabled = checkingModel.isPathEnabled (path);
				checked = checkingModel.isPathChecked (path);
			}
			else {
				enabled = false;
				checked = false;
			}

			checkBox.setEnabled (enabled);
			checkBox.setSelected (checked);
			
			return panel;
		}
	}
	
	private class UpdateCostListener implements TreeCheckingListener {
		private OnsiteRegistrationGUI orGui;
		
		public UpdateCostListener (OnsiteRegistrationGUI orGui) {
			this.orGui = orGui;
		}
	      public void valueChanged(TreeCheckingEvent e) {
	    	  ArrayList <DefaultMutableTreeNode> checkedLeaves = new ArrayList<DefaultMutableTreeNode> ();

	          for (TreePath tp : tree.getCheckingPaths ()) {
	        	  DefaultMutableTreeNode node = (DefaultMutableTreeNode) tp.getLastPathComponent ();
	        	  // only add leaves
	        	  if (node.isLeaf ())
	        		  checkedLeaves.add (node);
	          }
	          
	          orGui.updateTotalCost (checkedLeaves);
	      }
	}
	
	private FestivalTree tree;
	private JScrollPane sPane;
	
	/**
	 * @param dtm TreeModel to use in creating the tree
	 */
	public FestivalClassesTree (TreeModel dtm) {
		// set up tree
		tree = new FestivalTree ();
		tree.setModel (dtm);
		tree.getCheckingModel ().setCheckingMode (CheckingMode.SIMPLE);
		tree.setCellRenderer (new CheckBoxTreeCellRenderer ());
		
		tree.expandAll ();
		tree.setRootVisible (false);
		tree.setToggleClickCount (0);
		
		// create JScrollPanel and add tree
		sPane = new JScrollPane();
		sPane.setViewportView(tree);
	}
	
	/**
	 * Checking listeners are used to call methods when items in the
	 * tree have been checked or unchecked.
	 * 
	 * @param orGui 
	 */
	public void addCheckingListener (OnsiteRegistrationGUI orGui) {
		tree.addTreeCheckingListener (new UpdateCostListener (orGui));
	}
	
	/**
	 * A helper method for GUIs in getting a JScrollPane object that
	 * contains a configured CheckBoxTree
	 * 
	 * @return a scrolling pane containing the tree
	 */
	public JScrollPane getScrollingPaneTree () {
		return sPane;
	}
	
	/**
	 * Returns a list of leaf nodes that have been checked
	 * 
	 * @return a list of leaf nodes that have been checked
	 */
	public List<DefaultMutableTreeNode> getCheckedLeafNodes () {
  	  ArrayList <DefaultMutableTreeNode> checkedLeaves = new ArrayList<DefaultMutableTreeNode> ();

        for (TreePath tp : tree.getCheckingPaths ()) {
      	  DefaultMutableTreeNode node = (DefaultMutableTreeNode) tp.getLastPathComponent ();
      	  // only add leaves
      	  if (node.isLeaf ())
      		  checkedLeaves.add (node);
        }
        
        return checkedLeaves;
	}
	
	/**
	 * Sets the underlying CheckBoxTree's TreeModel 
	 * 
	 * @param tm TreeModel to set the underlying CheckBoxTree to have
	 */
	public void setTreeModel (TreeModel tm) {
		tree.setModel (tm);
		// models start in a collapsed state, thus we need to re-expand
		tree.expandAll ();
	}
}