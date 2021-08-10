package com.khazovgames.questjobs.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TypeEditorFrame extends JFrame implements ActionListener{

	private List<Listener> listeners;
	
	private ObjectEditor objectEditor; //The editor for the instance of the field in fieldData
	private FieldData fieldData; //The fieldData we are editing
	
	private EditorPanel editorPanel;
	
	private JButton saveButton;
	private JButton deleteButton;
	
	public TypeEditorFrame(FieldData fieldData) {
		this(fieldData, fieldData.GetCurrentValue().getClass());
	}
	
	public TypeEditorFrame(FieldData fieldData, Class<?> fieldDataCurrentValueType) {
		this(new ObjectEditor(fieldData == null ? null : fieldData.GetCurrentValue(), fieldDataCurrentValueType));
		this.fieldData = fieldData;
	}
	
	public TypeEditorFrame(ObjectEditor objectEditor) {
		this.listeners = new ArrayList<Listener>();
		this.objectEditor = objectEditor;
		
		initWindow();
		initComponents();
		
		pack();
		setSize(400, getHeight());
	}
	
	private void initWindow() {
		setTitle("Edit " + objectEditor.GetObjectType().getSimpleName());
		setLocationRelativeTo(null);
		setResizable(false);
	}
	
	private void initComponents() {
		editorPanel = new EditorPanel(objectEditor);
		editorPanel.setBackground(Color.green);
		
		saveButton = new JButton("Save");
		saveButton.addActionListener(this);
		
		deleteButton = new JButton("Delete");
		deleteButton.addActionListener(this);
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.PAGE_END;
		
		constraints.gridx = 0;
		editorPanel.add(deleteButton, constraints);
		
		constraints.gridx = 1;
		editorPanel.add(saveButton, constraints);
		add(editorPanel);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object newFieldDataValue = null;
		
		if(e.getSource() == saveButton) {
			boolean success = editorPanel.applyComponentDataToFieldData();
			if(!success) return;
			
			newFieldDataValue = objectEditor.ConstructObjectFromFieldData();
		}

		for(Listener listener : listeners) {
			Method[] methods = listener.getClass().getDeclaredMethods();
			for(Method m : methods)
				if(m.isAnnotationPresent(EventHandler.class))
				{
					m.setAccessible(true);
					try { m.invoke(listener, this, fieldData, newFieldDataValue); }
					catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) { e1.printStackTrace(); }
					break;
				}
		}
		
		setVisible(false);
		dispose();
	}
	
	public void registerListener(Listener listener) {
		listeners.add(listener);
	}
}
