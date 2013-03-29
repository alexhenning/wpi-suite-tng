package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.jdesktop.swingx.treetable.AbstractTreeTableModel;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.DB;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.RequirementsCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.SingleIterationCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.SingleRequirementCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.ListRequirementsPanel.UpdateTableCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;


@SuppressWarnings("serial")
public class ViewReqTreeTable extends AbstractTreeTableModel {

	private RequirementTreeNode myroot;
    private String[] columnNames = { "Name", "ID", "Status", "Priority", "Iteration", "Estimate", "Subrequirement count"};
    //private 
    //first list is row, second list is column data
    
    private Object[][] data = {};
    
	public ViewReqTreeTable(List<RequirementModel> reqs)
	{
		myroot = new RequirementTreeNode();
		if (reqs.size() > 0) {
			//TODO find a better way to do this
			//TODO figure out how to not display a requirement if it is a subrequirement.......
			myroot.clearChildren();
			for(RequirementModel req : reqs) {
				myroot.getChildren().add(new RequirementTreeNode(req));
			}
		}
		else {
			// do nothing, there are no requirements
		}
		
	}

//	public void updateAllRequirementList() {
//		DB.getAllRequirements(new UpdateTreeTableCallback());
//	}

	@Override
	public int getColumnCount() 
	{
		return columnNames.length;
	}

	public int getRowCount() {
        return data.length;
    }

	@Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Class<?> getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    public boolean isCellEditable(int row, int col) {
       return false; //Cells should not be editable in table, should be able to double click and open edit tab
    }

    public void setValueAt(Object value, int row, int col) {
//        data[row][col] = value;
//        fireTableCellUpdated(row, col);
    }
    
    public void setData(Object[][] data){
    	this.data = data;
    }
    
	@Override
	public Object getValueAt( Object node, int column ) 
	{
		System.out.println( "getValueAt: " + node + ", " + column );
		RequirementTreeNode treenode = ( RequirementTreeNode )node;
		switch( column )
		{
		case 0: return treenode.getName();
		case 1: return treenode.getId();
		case 2: return treenode.getStatus();
		case 3: return treenode.getPriority();
		case 4: return treenode.getIterationNumber();
		case 5: return treenode.getEstimate();
		case 6: return treenode.getChildren().size();
		default: return "Unknown";
		}
	}

	@Override
	public Object getChild( Object node, int index ) 
	{
		RequirementTreeNode treenode = ( RequirementTreeNode )node;
		return treenode.getChildren().get( index );
	}

	@Override
	public int getChildCount( Object parent ) 
	{
		RequirementTreeNode treenode = ( RequirementTreeNode )parent;
		return treenode.getChildren().size();
	}

	@Override
	public int getIndexOfChild( Object parent, Object child ) 
	{
		RequirementTreeNode treenode = ( RequirementTreeNode )parent;
		for( int i=0; i>treenode.getChildren().size(); i++ )
		{
			if( treenode.getChildren().get( i ) == child )
			{
				return i;
			}
		}

		// TODO Auto-generated method stub
		return 0;
	}
	
	 public boolean isLeaf( Object node )
	 {
		 RequirementTreeNode treenode = ( RequirementTreeNode )node;
		 if( treenode.getChildren().size() > 0 )
		 {
			 return false;
		 }
		 return true;
	 }
	 
	 @Override
	 public Object getRoot()
	 {
		 return myroot;
	 }
}

class RequirementTreeNode
{
	private RequirementModel requirement;
	private List<RequirementTreeNode> children = new ArrayList<RequirementTreeNode>();
	
	public RequirementTreeNode() 
	{
	}
	
	public RequirementTreeNode(RequirementModel requiement) {
		this.requirement = requiement;
		updateChildren();
	}
	
	public RequirementTreeNode(int requiementId) {
		this.requirement = null;
		DB.getSingleRequirement(""+requiementId, new UpdateRequirementCallback());
		while(this.requirement == null) {}
		updateChildren();
	}
	
	public void clearChildren() {
		children.clear();
	}
	
	public void updateChildren() {
		//TODO figure this part out
	}
	
	public String getName() {
		return requirement.getName();
	}
	
//	public void setName(String name) {
//		requirement.setName(name);
//	}
	
	public String getDescription() {
		return requirement.getDescription();
	}
	
//	public void setDescription(String description) {
//		requirement.setDescription(description);
//	}
	
	public int getId() {
		return requirement.getId();
	}
	
	public int getIterationNumber() {
		return requirement.getIteration().getIterationNumber();
	}
	
	public String getStatus() {
		return requirement.getStatus().name();
	}
	
	public String getPriority() {
		return requirement.getPriority().name();
	}
	
	public String getEstimate() {
		return requirement.getEstimate();
	}
	
	public List<RequirementTreeNode> getChildren() {
		return children;
	}
	
	public String toString(){
		return "RequirementTreeNode: " + requirement.toString();
	}
	
	class UpdateRequirementCallback implements SingleRequirementCallback {

		@Override
		public void callback(RequirementModel req) {
			requirement = req;
			// TODO Auto-generated method stub
			
		}
		
	}
}



