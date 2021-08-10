package com.khazovgames.questjobs.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.lang.reflect.Array;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ArrayPanel extends JPanel implements Listener{

	private FieldData arrayFieldData;
	private HashMap<JComponent, Object> componentValues;
	
	public ArrayPanel(FieldData arrayFieldData) {		
		setLayout(new GridLayout(0, 1));
		
		update(arrayFieldData);
	}
	
	public void update(FieldData newArrayFieldData) {
		arrayFieldData = newArrayFieldData;
		this.componentValues = new HashMap<>();
		
		removeAll();
		
		JButton button = new JButton("Add New Item");
		button.addActionListener(e -> doAddNewItemAction());
		
		add(button);
		
		if(newArrayFieldData.IsCurrentValueSet())
		{
			Object array = newArrayFieldData.GetCurrentValue();
			int length = Array.getLength(array);
			
			for(int i = 0; i < length; ++i)
			{
				JComponent component = null;
				
				if(arrayFieldData.GetArrayType().isPrimitive() || arrayFieldData.GetArrayType() == String.class) 
				{
					component = new JTextField();
				}
				else if(arrayFieldData.GetArrayType() == Enum.class) 
				{
					component = new JComboBox<>(arrayFieldData.GetArrayType().getEnumConstants());
				}
				else 
				{
					JButton editButton = new JButton();
					editButton.setText("Edit " + arrayFieldData.GetArrayType().getSimpleName());
					editButton.addActionListener(event -> doObjectEditorButtonClick(event));
					component = editButton;
				}
				
				componentValues.put(component, Array.get(array, i));
				add(component);
			}
			
			revalidate();
			repaint();
		}
		
		revalidate();
		repaint();
	}
	
	private void doAddNewItemAction() {		
		JComponent component = null;
		
		if(arrayFieldData.GetArrayType().isPrimitive() || arrayFieldData.GetArrayType() == String.class) 
		{
			component = new JTextField();
		}
		else if(arrayFieldData.GetArrayType() == Enum.class) 
		{
			component = new JComboBox<>(arrayFieldData.GetArrayType().getEnumConstants());
		}
		else 
		{
			new TypeChooserFrame(arrayFieldData.GetArrayType(), this).setVisible(true);
		}
		
		if(component != null)
		{
			componentValues.put(component, null);
			add(component);
			revalidate();
			repaint();
		}
	}
	
	private HashMap<TypeEditorFrame, JButton> editorComponents = new HashMap<>();
	private void doObjectEditorButtonClick(ActionEvent event) {
		Object value = componentValues.get(event.getSource());
		TypeEditorFrame typeEditor = new TypeEditorFrame(new ObjectEditor(value, value.getClass()));
		typeEditor.registerListener(this);
		typeEditor.setVisible(true);
		
		editorComponents.put(typeEditor, (JButton) event.getSource());
	}
	
	@EventHandler
	private void onTypeEditorSaved(TypeEditorFrame frame, FieldData fieldData, Object newValue) {		
		JButton clickedButton = editorComponents.get(frame);
		if(clickedButton == null) 
		{
			JButton button = new JButton();
			button.setText("Edit " + arrayFieldData.GetArrayType().getSimpleName());
			button.addActionListener(event -> doObjectEditorButtonClick(event));
			add(button);
			
			revalidate();
			repaint();
			
			componentValues.put(button, newValue);
		}
		else if(newValue == null)
		{
			componentValues.remove(clickedButton);
			remove(clickedButton);
			repaint();
			revalidate();
		}
		else
		{
			componentValues.put(clickedButton, newValue);
		}
		
		Object arrayInstance = Array.newInstance(arrayFieldData.GetArrayType(), componentValues.size());

		int index = 0;
		for(JComponent component : componentValues.keySet()) 
		{
			Array.set(arrayInstance, index, componentValues.get(component));
			++index;
		}
		
		if(Array.getLength(arrayInstance) == 0)
			arrayFieldData.SetIntendedValue(null);
		else
			arrayFieldData.SetIntendedValue(arrayInstance);
	}
}
