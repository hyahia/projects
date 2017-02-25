package com.hyahya.attribute.manager;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.InputStream;

import javax.swing.AbstractAction;
import javax.swing.AbstractCellEditor;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

public class AttributeManager {

	public static final String LABEL_COLOR = "#3c8dbc";
	public static final String BORDER_TITLE_COLOR = "#ff8c26";
	
	public static final String ACTION_QUERY = "INSERT INTO CIM_SRVC_ACTIONS (ACTION_ID, ACTION_TYPE, ACTION_CODE,ACTION_DESC,ACTION_STATUS,JNDI_LOOKUP,ADAPTER_PROVIDER_URL,ACTION_QUERY,ACTION_PARAMETERS) VALUES((SELECT MAX(ACTION_ID)+1 FROM CIM_SRVC_ACTIONS),'GET','%s','%s','ENABLED','%s','%s','%s','%s')";
	
	public static void main(String[] args) {
//		JFrame.setDefaultLookAndFeelDecorated(true);

		try {
		     GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		     InputStream in = AttributeManager.class.getClass().getResourceAsStream("/res/neo-tech-alt-medium.ttf");
		     Font font = Font.createFont(Font.TRUETYPE_FONT, in);
		     font = font.deriveFont(Font.PLAIN,12);
		     ge.registerFont(font);
		     setUIFont (new javax.swing.plaf.FontUIResource(font));
		} catch (Exception e) {
		    e.printStackTrace();
		}
		new ActionFrame().setVisible(true);
	}
	private static void setUIFont(javax.swing.plaf.FontUIResource f)
	{
	    java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
	    while (keys.hasMoreElements())
	    {
	        Object key = keys.nextElement();
	        Object value = UIManager.get(key);
	        if (value instanceof javax.swing.plaf.FontUIResource)
	        {
	            UIManager.put(key, f);
	        }
	    }
	}
}

class ActionFrame extends javax.swing.JFrame{
	Action validateForm;
	InputVerifier iv;
	
	final JPanel actionPanel = new JPanel(new GridBagLayout()), tabPanel = new JPanel(new GridBagLayout()), groupPanel = new JPanel(new GridBagLayout()), 
			attributesPanel = new JPanel(new GridBagLayout()), permissionsPanel = new JPanel(new GridBagLayout()), buttonsPanel = new JPanel(new GridBagLayout());
	final JTextField actionCode = new JTextField(30), actionDesc = new JTextField(30), actionURL = new JTextField(30), actionJNDI = new JTextField(30), actionQuery = new JTextField(30), actionParameters = new JTextField(30);
	final JTextField tabName = new JTextField(30), tabDesc = new JTextField(30), tabUIId = new JTextField(30), tabParentId = new JTextField(30);
	final JCheckBox isNewTab = new JCheckBox("Is New Tab?");
	final JCheckBox createPermissions = new JCheckBox("Create Permissions ?");
	final JCheckBox createRolePermissions = new JCheckBox("Add Permissions to existing roles ?");
	final JTextField groupName = new JTextField(30), groupDesc = new JTextField(30), groupUIId = new JTextField(30);
	final JTable attributesTable = new JTable();
	
	final JLabel tabNameLabel = new JLabel("<html><font color='"+AttributeManager.LABEL_COLOR+"'>Name</font></html>");
	final JLabel tabDescLabel = new JLabel("<html><font color='"+AttributeManager.LABEL_COLOR+"'>Description</font></html>");
	final JLabel tabUIIDLabel = new JLabel("<html><font color='"+AttributeManager.LABEL_COLOR+"'>UI_Id</font></html>");
	final JLabel tabParentLabel = new JLabel("<html><font color='"+AttributeManager.LABEL_COLOR+"'>Parent_Tab_Name</font></html>");
	
	final JLabel actionCodeLabel = new JLabel("<html><font color='"+AttributeManager.LABEL_COLOR+"'>Code </font></html>");
	final JLabel actionDescLabel = new JLabel("<html><font color='"+AttributeManager.LABEL_COLOR+"'>Description </font></html>");
	final JLabel actionURLLabel = new JLabel("<html><font color='"+AttributeManager.LABEL_COLOR+"'>URL </font></html>");
	final JLabel actionJNDILabel = new JLabel("<html><font color='"+AttributeManager.LABEL_COLOR+"'>JNDI </font></html>");
	final JLabel actionQueryLabel = new JLabel("<html><font color='"+AttributeManager.LABEL_COLOR+"'>Query </font></html>");
	final JLabel actionParametersLabel = new JLabel("<html><font color='"+AttributeManager.LABEL_COLOR+"'>Parameters </font></html>");
	
	final JLabel groupNameLabel = new JLabel("<html><font color='"+AttributeManager.LABEL_COLOR+"'>Name</font></html>");
	final JLabel groupUIIdLabel = new JLabel("<html><font color='"+AttributeManager.LABEL_COLOR+"'>UI_Id</font></html>");
	final JLabel groupDescLabel = new JLabel("<html><font color='"+AttributeManager.LABEL_COLOR+"'>Description</font></html>");
	
	final JButton showSql;
	final JButton exit;
	
	public ActionFrame() {
		try {
		    for (LookAndFeelInfo info  : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
		setTitle("CIM Action & Attribute Manager 0.1");
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(6*dim.width/7, 6*dim.height/7);
		setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		
		addActionAndValidation();
		
		exit = new JButton("Exit");
		showSql = new JButton(validateForm);
		showSql.setText("Show SQL");
		actionCode.setInputVerifier(iv);
		actionDesc.setInputVerifier(iv);
		actionURL.setInputVerifier(iv);
		actionJNDI.setInputVerifier(iv);
		actionQuery.setInputVerifier(iv);
		actionParameters.setInputVerifier(iv);
		tabName.setInputVerifier(iv);
		tabDesc.setInputVerifier(iv);
		tabUIId.setInputVerifier(iv);
		tabParentId.setInputVerifier(iv);
		groupName.setInputVerifier(iv);
		groupDesc.setInputVerifier(iv);
		groupUIId.setInputVerifier(iv);
		
		new JLabel("Action Code").setLabelFor(actionCode);
		new JLabel("Action Description").setLabelFor(actionDesc);
		new JLabel("Action URL").setLabelFor(actionURL);
		new JLabel("Action JNDI").setLabelFor(actionJNDI);
		new JLabel("Action Query").setLabelFor(actionQuery);
		new JLabel("Action Query Parameters").setLabelFor(actionParameters);
		new JLabel("Tab Name").setLabelFor(tabName);
		new JLabel("Tab Description").setLabelFor(tabDesc);
		new JLabel("Tab UUID").setLabelFor(tabUIId);
		new JLabel("Tab Parent Id").setLabelFor(tabParentId);
		new JLabel("Group Name").setLabelFor(groupName);
		new JLabel("Group Description").setLabelFor(groupDesc);
		new JLabel("Group UIID").setLabelFor(groupUIId);
		
		decorate();
	}
	
	private void addActionAndValidation() {
		iv = new InputVerifier() {

		    @Override
		    public boolean verify(JComponent input) {
		        if (!(input instanceof JTextField)) return true;
		        return isValidText((JTextField) input);
		    }

		    protected boolean isValidText(JTextField field) {
		    	boolean valid = field.getText() != null &&  !field.getText().trim().isEmpty();
		    	if (!valid) {
		    		field.setBorder(BorderFactory.createLineBorder(Color.RED));
				}else {
			    	field.setBorder(BorderFactory.createLineBorder(Color.GRAY));
				}
		    	
		    	return valid;
		    }

		    /**
		     * Implemented to unconditionally return true: focus traversal
		     * should never be restricted.
		     */
		    @Override
		    public boolean shouldYieldFocus(JComponent input) {
		        return true;
		    }

		};

		validateForm = new AbstractAction("Commit") {

		    @Override
		    public void actionPerformed(ActionEvent e) {
		        if (!validateInputs(actionPanel) || (isNewTab.isSelected() ? !validateInputs(tabPanel) : !validateInputs(tabName))   || !validateInputs(groupPanel) || !validateInputs(attributesPanel) ) {
		            return;
		        }
		        System.out.println("All valid - generate SQL");
		        generateSQL();
		    }

		    protected boolean validateInputs(Container form) {
		    	if (form instanceof JTextField) {
		    		JComponent child = (JComponent) form;
		        	System.out.println(child);

		            if (!isValid(child)) {
		                String text = getLabelText(child);
		                JOptionPane.showMessageDialog(form.getParent(), "\"" + text + "\" is empty.", "Missing Parameter Error", JOptionPane.ERROR_MESSAGE);
		                child.requestFocusInWindow();
		                return false;
		            }
				}
		        for (int i = 0; i < form.getComponentCount(); i++) {
		            JComponent child = (JComponent) form.getComponent(i);
		        	System.out.println(child);

		            if (!isValid(child)) {
		                String text = getLabelText(child);
		                JOptionPane.showMessageDialog(form.getParent(), "\"" + text + "\" is empty.", "Missing Parameter Error", JOptionPane.ERROR_MESSAGE);
		                child.requestFocusInWindow();
		                return false;
		            }
		        }
		        return true;
		    }
		    /**
		     * Returns the text of the label which is associated with
		     * child. 
		     */
		    protected String getLabelText(JComponent child) {
		        JLabel labelFor = (JLabel) child.getClientProperty("labeledBy");
		        return labelFor != null ? labelFor.getText() : "";
		    }

		    private boolean isValid(JComponent child) {
		        if (child.getInputVerifier() != null) {
		            return child.getInputVerifier().verify(child);
				}
		        return true;
		    }
		};
		
	}

	private void generateSQL(){
		String actionQueryString = String.format(AttributeManager.ACTION_QUERY, actionCode.getText(),actionDesc.getText(),actionJNDI.getText(),
				actionURL.getText(),actionQuery.getText(),actionParameters.getText());
		System.out.println(actionQueryString);
	}
	private void decorate(){
		
		try {
		    setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/res/logo.png")));
		}
		catch (Exception exc) {
		    exc.printStackTrace();
		}
		setLayout(new GridBagLayout());

		String p = getClass().getResource("/res/actions.png" ).toString();
		String ap = getClass().getResource("/res/attribute.png" ).toString();
		String gp = getClass().getResource("/res/group.png" ).toString();
		String tp = getClass().getResource("/res/tabs.png" ).toString();
		String pp = getClass().getResource("/res/perm.png" ).toString();
		String vp = getClass().getResource("/res/view.png" ).toString();
		actionPanel.setBorder(BorderFactory.createTitledBorder("<html><img src='"+p+"' height='16' width='16'><font color='"+AttributeManager.BORDER_TITLE_COLOR+"'>&nbsp;ACTION</span></html>"));
		tabPanel.setBorder(BorderFactory.createTitledBorder("<html><img src='"+tp+"' height='16' width='16'><font color='"+AttributeManager.BORDER_TITLE_COLOR+"'>&nbsp;TAB</font></html>"));
		groupPanel.setBorder(BorderFactory.createTitledBorder("<html><img src='"+gp+"' height='16' width='16'><font color='"+AttributeManager.BORDER_TITLE_COLOR+"'>&nbsp;GROUP</font></html>"));
		attributesPanel.setBorder(BorderFactory.createTitledBorder("<html><img src='"+ap+"' height='16' width='16'><font color='"+AttributeManager.BORDER_TITLE_COLOR+"'>&nbsp;ATTRIBUTES</font></html>"));
		permissionsPanel.setBorder(BorderFactory.createTitledBorder("<html><img src='"+pp+"' height='16' width='16'><font color='"+AttributeManager.BORDER_TITLE_COLOR+"'>&nbsp;PERMISSIONS</font></html>"));
		buttonsPanel.setBorder(BorderFactory.createTitledBorder("<html><img src='"+vp+"' height='16' width='16'><font color='"+AttributeManager.BORDER_TITLE_COLOR+"'>&nbsp;SHOW SQL</font></html>"));

		actionPanel.add(actionCodeLabel, new GridBagConstraints(0, 0, 1, 1, 0.2, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		actionPanel.add(actionCode, new GridBagConstraints(1, 0, 1, 1, 1.8, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		actionPanel.add(actionDescLabel, new GridBagConstraints(2, 0, 1, 1, 0.2, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
		actionPanel.add(actionDesc, new GridBagConstraints(3, 0, 1, 1, 1.8, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		actionPanel.add(actionURLLabel, new GridBagConstraints(4, 0, 1, 1, 0.2, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
		actionPanel.add(actionURL, new GridBagConstraints(5, 0, 1, 1, 1.8, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		actionPanel.add(actionJNDILabel, new GridBagConstraints(0, 1, 1, 1, 0.2, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		actionPanel.add(actionJNDI, new GridBagConstraints(1, 1, 1, 1, 1.8, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		actionPanel.add(actionQueryLabel, new GridBagConstraints(2, 1, 1, 1, 0.2, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
		actionPanel.add(actionQuery, new GridBagConstraints(3, 1, 1, 1, 1.8, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		actionPanel.add(actionParametersLabel, new GridBagConstraints(4, 1, 1, 1, 0.2, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
		actionPanel.add(actionParameters, new GridBagConstraints(5, 1, 1, 1, 1.8, 1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		
		/*showSql.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String actionQueryString = String.format(AttributeManager.ACTION_QUERY, actionCode.getText(),actionDesc.getText(),actionJNDI.getText(),
						actionURL.getText(),actionQuery.getText(),actionParameters.getText());
				System.out.println(actionQueryString);
			}
		});*/
		buttonsPanel.add(showSql,new GridBagConstraints(0, 0, 1, 1, 1.95, 1, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		buttonsPanel.add(exit,new GridBagConstraints(1, 0, 1, 1, 0.05, 1, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		
		isNewTab.setSelected(true);
		isNewTab.setBackground(new Color(238, 238, 238));
		isNewTab.setForeground(new Color((int)Long.parseLong(AttributeManager.LABEL_COLOR.replace("#", ""),16)));
		isNewTab.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tabDesc.setVisible(isNewTab.isSelected());
				tabUIId.setVisible(isNewTab.isSelected());
				tabDescLabel.setVisible(isNewTab.isSelected());
				tabUIIDLabel.setVisible(isNewTab.isSelected());
				tabParentId.setVisible(isNewTab.isSelected());
				tabParentLabel.setVisible(isNewTab.isSelected());
			}
		});
		
		String header[] = new String[] { "Code", "Description", "Label","Formatter", "Mask", "Delete" };
		DefaultTableModel dtm = new DefaultTableModel(0,0);
		dtm.setColumnIdentifiers(header);
		for (int count = 1; count <= 25; count++) {
	        dtm.addRow(new Object[] { "", "", "", "Formatter", "Mask", "Delete" });
		}
		attributesTable.setRowHeight(20);
		attributesTable.setModel(dtm);
		attributesTable.getColumnModel().getColumn(0).setPreferredWidth(150);
		attributesTable.getColumnModel().getColumn(1).setPreferredWidth(150);
		attributesTable.getColumnModel().getColumn(2).setPreferredWidth(150);
		attributesTable.getColumnModel().getColumn(3).setPreferredWidth(50);
		attributesTable.getColumnModel().getColumn(4).setPreferredWidth(50);
		attributesTable.getColumnModel().getColumn(5).setPreferredWidth(50);

		Action delete = new AbstractAction()
		{
		    public void actionPerformed(ActionEvent e)
		    {
		        JTable table = (JTable)e.getSource();
		        int modelRow = Integer.valueOf( e.getActionCommand() );
		        ((DefaultTableModel)table.getModel()).removeRow(modelRow);
		    }
		};

		ButtonColumn deleteColumn = new ButtonColumn(attributesTable, delete, 5);
		
		Action formatter = new AbstractAction()
		{
		    public void actionPerformed(ActionEvent e)
		    {
		        JTable table = (JTable)e.getSource();
		        int modelRow = Integer.valueOf( e.getActionCommand() );

				final JDialog frame = new JDialog(ActionFrame.this, "Attribute Formatter Details", true);
				frame.getContentPane().add(new FormatterPanel(frame));
				Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
				frame.setPreferredSize(new Dimension(550,150));
				frame.setResizable(false);
				frame.setLocation(dim.width/2-275, dim.height/2-75);
				frame.pack();
				frame.setVisible(true);
		    }
		};

		ButtonColumn formatterColumn = new ButtonColumn(attributesTable, formatter, 3);
		
		Action mask = new AbstractAction()
		{
		    public void actionPerformed(ActionEvent e)
		    {
		        JTable table = (JTable)e.getSource();
		        int modelRow = Integer.valueOf( e.getActionCommand() );

		        final JDialog frame = new JDialog(ActionFrame.this, "Attribute Mask Details", true);
				frame.getContentPane().add(new MaskPanel(frame));
				Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
				frame.setPreferredSize(new Dimension(450,90));
				frame.setResizable(false);
				frame.setLocation(dim.width/2-225, dim.height/2-45);
				frame.pack();
				frame.setVisible(true);
		    }
		};

		ButtonColumn maskColumn = new ButtonColumn(attributesTable, mask, 4);
		
		JScrollPane scrollPane = new JScrollPane(attributesTable);
		scrollPane.setVisible(true);
		
		tabPanel.add(isNewTab, new GridBagConstraints(0, 0, 1, 1, 0.2, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		tabPanel.add(tabNameLabel, new GridBagConstraints(1, 0, 1, 1, 0.2, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		tabPanel.add(tabName, new GridBagConstraints(2, 0, 1, 1, 1.8, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		tabPanel.add(tabDescLabel, new GridBagConstraints(3, 0, 1, 1, 0.2, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
		tabPanel.add(tabDesc, new GridBagConstraints(4, 0, 1, 1, 1.8, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		tabPanel.add(tabUIIDLabel, new GridBagConstraints(5, 0, 1, 1, 0.2, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
		tabPanel.add(tabUIId, new GridBagConstraints(6, 0, 1, 1, 1.8, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		tabPanel.add(tabParentLabel, new GridBagConstraints(7, 0, 1, 1, 0.2, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
		tabPanel.add(tabParentId, new GridBagConstraints(8, 0, 1, 1, 1.8, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		
		groupPanel.add(groupNameLabel, new GridBagConstraints(0, 0, 1, 1, 0.2, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		groupPanel.add(groupName, new GridBagConstraints(1, 0, 1, 1, 1.8, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		groupPanel.add(groupDescLabel, new GridBagConstraints(2, 0, 1, 1, 0.2, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
		groupPanel.add(groupDesc, new GridBagConstraints(3, 0, 1, 1, 1.8, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		groupPanel.add(groupUIIdLabel, new GridBagConstraints(4, 0, 1, 1, 0.2, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
		groupPanel.add(groupUIId, new GridBagConstraints(5, 0, 1, 1, 1.8, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		
		attributesPanel.add(scrollPane, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		
		createPermissions.setBackground(new Color(238, 238, 238));
		createRolePermissions.setBackground(new Color(238, 238, 238));
		createPermissions.setForeground(new Color((int)Long.parseLong(AttributeManager.LABEL_COLOR.replace("#", ""),16)));
		createRolePermissions.setForeground(new Color((int)Long.parseLong(AttributeManager.LABEL_COLOR.replace("#", ""),16)));
		permissionsPanel.add(createPermissions, new GridBagConstraints(0, 0, 1, 1, 0.5, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		permissionsPanel.add(createRolePermissions, new GridBagConstraints(1, 0, 1, 1, 0.5, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 700), 0, 0));
		
		getContentPane().add(actionPanel, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		getContentPane().add(tabPanel, new GridBagConstraints(0, 1, 1, 1, 1, 0.5, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		getContentPane().add(groupPanel, new GridBagConstraints(0, 2, 1, 1, 1, 0.5, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		getContentPane().add(attributesPanel, new GridBagConstraints(0, 3, 1, 1, 1, 3.5, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		getContentPane().add(permissionsPanel, new GridBagConstraints(0, 4, 1, 1, 1, 0.5, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		getContentPane().add(buttonsPanel, new GridBagConstraints(0, 6, 1, 1, 1, 0.5, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		
		
	}
}

class ButtonColumn extends AbstractCellEditor
implements TableCellRenderer, TableCellEditor, ActionListener, MouseListener
{
private JTable table;
private Action action;
private int mnemonic;
private Border originalBorder;
private Border focusBorder;

private JButton renderButton;
private JButton editButton;
private Object editorValue;
private boolean isButtonColumnEditor;

public ButtonColumn(JTable table, Action action, int column)
{
	this.table = table;
	this.action = action;

	renderButton = new JButton();
	editButton = new JButton();
	editButton.setFocusPainted( false );
	editButton.addActionListener( this );
	originalBorder = null;

	TableColumnModel columnModel = table.getColumnModel();
	columnModel.getColumn(column).setCellRenderer( this );
	columnModel.getColumn(column).setCellEditor( this );
	table.addMouseListener( this );
}

public Border getFocusBorder()
{
	return focusBorder;
}

public void setFocusBorder(Border focusBorder)
{
	this.focusBorder = focusBorder;
	editButton.setBorder( focusBorder );
}

public int getMnemonic()
{
	return mnemonic;
}

public void setMnemonic(int mnemonic)
{
	this.mnemonic = mnemonic;
	renderButton.setMnemonic(mnemonic);
	editButton.setMnemonic(mnemonic);
}

@Override
public Component getTableCellEditorComponent(
	JTable table, Object value, boolean isSelected, int row, int column)
{
	if (value == null)
	{
		editButton.setText( "" );
		editButton.setIcon( null );
	}
	else if (value instanceof Icon)
	{
		editButton.setText( "" );
		editButton.setIcon( (Icon)value );
	}
	else
	{
		editButton.setText( value.toString() );
		editButton.setIcon( null );
	}

	this.editorValue = value;
	return editButton;
}

@Override
public Object getCellEditorValue()
{
	return editorValue;
}

public Component getTableCellRendererComponent(
	JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
{
	if (isSelected)
	{
		renderButton.setForeground(table.getSelectionForeground());
 		renderButton.setBackground(table.getSelectionBackground());
	}
	else
	{
		renderButton.setForeground(table.getForeground());
		renderButton.setBackground(UIManager.getColor("Button.background"));
	}

	if (hasFocus)
	{
		renderButton.setBorder( focusBorder );
	}
	else
	{
		renderButton.setBorder( originalBorder );
	}

	if (value == null)
	{
		renderButton.setText( "" );
		renderButton.setIcon( null );
	}
	else if (value instanceof Icon)
	{
		renderButton.setText( "" );
		renderButton.setIcon( (Icon)value );
	}
	else
	{
		renderButton.setText( value.toString() );
		renderButton.setIcon( null );
	}

	return renderButton;
}

public void actionPerformed(ActionEvent e)
{
	int row = table.convertRowIndexToModel( table.getEditingRow() );
	fireEditingStopped();

	//  Invoke the Action

	ActionEvent event = new ActionEvent(
		table,
		ActionEvent.ACTION_PERFORMED,
		"" + row);
	action.actionPerformed(event);
}

public void mousePressed(MouseEvent e)
{
	if (table.isEditing()
	&&  table.getCellEditor() == this)
		isButtonColumnEditor = true;
}

public void mouseReleased(MouseEvent e)
{
	if (isButtonColumnEditor
	&&  table.isEditing())
		table.getCellEditor().stopCellEditing();

	isButtonColumnEditor = false;
}

public void mouseClicked(MouseEvent e) {}
public void mouseEntered(MouseEvent e) {}
public void mouseExited(MouseEvent e) {}
}

class FormatterPanel extends JPanel{
	JComboBox<String> type = new JComboBox<String>(new String[] {"com.etisalat.cim.view.format.TimeFormatter","com.etisalat.cim.view.format.DateFormatter","com.etisalat.cim.view.format.NumberFormatter"});
	JTextField src = new JTextField();
	JTextField dest = new JTextField();
	JTextField formula = new JTextField();
	JComboBox<String> unit = new JComboBox<String>(new String[] {"","KB","MB","GB","Fils","AED","Kbps","Mbps","Gbps","SEC","Second","MIN","Minute","Hr","Hour"});
	
	JButton ok = new JButton("OK");
	JButton cancel = new JButton("Cancel");
	
	final JLabel typeLabel = new JLabel("<html><font color='"+AttributeManager.LABEL_COLOR+"'>Type</font></html>");
	final JLabel unitLabel = new JLabel("<html><font color='"+AttributeManager.LABEL_COLOR+"'>Unit</font></html>");
	final JLabel srcLabel = new JLabel("<html><font color='"+AttributeManager.LABEL_COLOR+"'>Source</font></html>");
	final JLabel destLabel = new JLabel("<html><font color='"+AttributeManager.LABEL_COLOR+"'>Destination</font></html>");
	final JLabel formulaLabel = new JLabel("<html><font color='"+AttributeManager.LABEL_COLOR+"'>Formula</font></html>");
	
	
	final JDialog parent;
	
	public FormatterPanel(JDialog frame) {
		this.parent = frame;
		setLayout(new GridBagLayout());
		setSize(550,150);
		
		add(typeLabel, new GridBagConstraints(0, 0, 1, 1, 0.2, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
		add(type, new GridBagConstraints(1, 0, 1, 1, 5.8, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		add(unitLabel, new GridBagConstraints(2, 0, 1, 1, 0.2, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 20, 0, 0), 0, 0));
		add(unit, new GridBagConstraints(3, 0, 1, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 5), 0, 0));

		add(srcLabel, new GridBagConstraints(0, 1, 1, 1, 0.2, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
		add(src, new GridBagConstraints(1, 1, 1, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		add(destLabel, new GridBagConstraints(2, 1, 1, 1, 0.2, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 20, 0, 0), 0, 0));
		add(dest, new GridBagConstraints(3, 1, 1, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 5), 0, 0));
		
		add(formulaLabel, new GridBagConstraints(0, 2, 1, 1, 0.2, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
		add(formula, new GridBagConstraints(1, 2, 1, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		add(ok, new GridBagConstraints(2, 2, 1, 1, 0.5, 1, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 20, 0, 0), 0, 0));
		add(cancel, new GridBagConstraints(3, 2, 1, 1, 0.5, 1, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 20, 0, 5), 0, 0));
		
		ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				parent.dispose();
			}
		});
	}
}


class MaskPanel extends JPanel{
	JTextField regex = new JTextField();
	JButton ok = new JButton("OK");
	JButton cancel = new JButton("Cancel");
	
	final JDialog parent;
	
	public MaskPanel(JDialog frame) {
		this.parent = frame;
		setLayout(new GridBagLayout());
		setSize(550,150);
		
		add(new JLabel("<html><font color='"+AttributeManager.LABEL_COLOR+"'>Regex</font></html>"), new GridBagConstraints(0, 0, 1, 1, 0.2, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
		add(regex, new GridBagConstraints(1, 0, 1, 1, 1.8, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		
		add(ok, new GridBagConstraints(2, 0, 1, 1, 1, 0.8, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 0, 0));
		add(cancel, new GridBagConstraints(3, 0, 1, 1, 0.8, 1, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
		
		ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				parent.dispose();
			}
		});
	}
}
