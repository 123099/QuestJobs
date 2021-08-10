package com.khazovgames.questjobs.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.khazovgames.questjobs.utils.SerializeField;

public class EditorPanel extends JPanel implements Listener{

	private ObjectEditor objectEditor;
	private Hashtable<FieldData, JComponent> fieldDataComponents;
	
	public EditorPanel(ObjectEditor objectEditor) {
		setPreferredSize(new Dimension(300,90));
		
		GridBagLayout gridBagLayout = new GridBagLayout(); 
		setLayout(gridBagLayout);
		
		this.objectEditor = objectEditor;
		fieldDataComponents = new Hashtable<>();
		
		for(int i = 0; i < objectEditor.GetFieldDataCount(); ++i)
			initFieldData(objectEditor.GetFieldData(i));
		
		update();
	}
	
	@SuppressWarnings("rawtypes")
	private void initFieldData(FieldData fieldData) {
		GridBagConstraints constraints = new GridBagConstraints();
		
		SerializeField annotation = fieldData.GetAnnotation(SerializeField.class);
		JLabel label = new JLabel("  " + (annotation != null ? annotation.name() : fieldData.GetName()));		
		JComponent component = null;
		
		if(fieldData.IsBoolean()) 
		{
			JCheckBox checkBox = new JCheckBox();
			checkBox.setSelected(fieldData.IsCurrentValueSet() ? fieldData.GetCurrentValue() : false);
			component = checkBox;
		}
		else if(fieldData.IsPrimitiveOrString()) 
		{
			JTextField textField = new JTextField();
			textField.setText(fieldData.IsCurrentValueSet() ? fieldData.GetCurrentValue().toString() : "");
			if(fieldData.IsString())
				textField.setMinimumSize(new Dimension(150,30));
			else
				textField.setMinimumSize(new Dimension(80, 30));
			component = textField;
		}
		else if(fieldData.IsEnum()) 
		{
			JComboBox<Enum> comboBox = new JComboBox<>(fieldData.GetEnumConstants());
			comboBox.setSelectedItem(fieldData.IsCurrentValueSet() ? fieldData.GetCurrentValue() : comboBox.getItemAt(0));
			comboBox.setMinimumSize(new Dimension(120,30));
			component = comboBox;
		}
		else if(fieldData.IsArray())
		{
			component = new ArrayPanel(fieldData);
			component.setBackground(Color.red);
			
			constraints.anchor = GridBagConstraints.NORTH;
		}
		else 
		{
			JButton button = new JButton();
			String buttonText = fieldData.IsCurrentValueSet() ? "Edit " : "Create new ";
			button.setText(buttonText + fieldData.GetName()); 
			button.addActionListener(event -> doButtonAction(fieldData));
			component = button;
		}
		
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(2,10,2,10);
		
		constraints.gridx = 0;
		add(label, constraints);
		
		constraints.gridx = 1;
		add(component, constraints);
		
		fieldDataComponents.put(fieldData, component);
	}
	
	public void setObjectEditor(ObjectEditor objectEditor) {
		this.objectEditor = objectEditor;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean applyComponentDataToFieldData() {
		for(FieldData fieldData : fieldDataComponents.keySet())
		{
			String fieldDisplayName = fieldData.GetAnnotation(SerializeField.class).name();
			JComponent component = fieldDataComponents.get(fieldData);
			
			if(component instanceof JCheckBox) 
			{
				JCheckBox checkBox = (JCheckBox)component;
				fieldData.SetIntendedValue(checkBox.isSelected());
			}
			else if(component instanceof JTextField)
			{
				JTextField textField = (JTextField)component;
				if(textField.getText().isEmpty()) {
					showMessage(fieldDisplayName + " cannot be empty!");
					return false;
				}
				else
					fieldData.SetIntendedValue(fieldData.Cast(textField.getText()));
			}
			else if(component instanceof JComboBox)
			{
				JComboBox<Enum> comboBox = (JComboBox<Enum>) component;
				fieldData.SetIntendedValue(comboBox.getSelectedItem());
			}
			else if(component instanceof JButton) 
			{
				if(!fieldData.IsCurrentValueSet()) {
					showMessage(fieldDisplayName + " must be created first!");
					return false;
				}
			}
			else if(component instanceof ArrayPanel)
			{
				if(!fieldData.IsCurrentValueSet()) {
					showMessage("At least 1 " + fieldDisplayName + " must be created first!");
					return false;
				}
			}
		}
		
		return true;
	}
	
	private void showMessage(String message) {
		JOptionPane.showMessageDialog(this, message);
	}
	
	public void update() {
		for(int i = 0; i < objectEditor.GetFieldDataCount(); ++i)
			updateFieldComponent(objectEditor.GetFieldData(i));
	}
	
	private void updateFieldComponent(FieldData fieldData) {
		JComponent component = fieldDataComponents.get(fieldData);
		
		if(component instanceof JCheckBox)
		{
			JCheckBox checkBox = (JCheckBox)component;
			checkBox.setSelected(fieldData.IsCurrentValueSet() ? fieldData.GetCurrentValue() : false);
		}
		else if(component instanceof JTextField)
		{
			JTextField textField = (JTextField)component;
			textField.setText(fieldData.IsCurrentValueSet() ? fieldData.GetCurrentValue().toString() : "");
		}
		else if(component instanceof JComboBox) 
		{
			JComboBox<Enum> comboBox = (JComboBox<Enum>)component;
			comboBox.setSelectedItem(fieldData.IsCurrentValueSet() ? fieldData.GetCurrentValue() : comboBox.getItemAt(0));
		}
		else if(component instanceof JButton)
		{
			JButton button = (JButton)component;
			button.setText((fieldData.IsCurrentValueSet() ? "Edit " : "Create new ") + fieldData.GetName());
		}
		else if(component instanceof ArrayPanel)
		{
			((ArrayPanel) component).update(fieldData);
		}
	}
	
	private void doButtonAction(FieldData fieldData) {
		if(!fieldData.IsCurrentValueSet())
			new TypeChooserFrame(fieldData, this).setVisible(true);
		else {
			TypeEditorFrame typeEditor = new TypeEditorFrame(fieldData);
			typeEditor.registerListener(this);
			typeEditor.setVisible(true);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@EventHandler
	private void onTypeEditorSaved(TypeEditorFrame frame, FieldData fieldData, Object newValue) {
		fieldData.SetIntendedValue(newValue);
		updateFieldComponent(fieldData);
	}
}
