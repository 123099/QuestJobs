package com.khazovgames.questjobs.gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.bukkit.event.Listener;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.impl.PojoClassFactory;

public class TypeChooserFrame extends JFrame implements ActionListener{

	private HashMap<String, Class<?>> subclassTypes;
	
	private FieldData fieldData;
	private Class<?> type;
	private Listener typeEditorEventListener;
	
	private JComboBox<String> comboBox;
	
	public TypeChooserFrame(Class<?> type, Listener typeEditorEventListener) {
		findSubclassTypesOf(type);
		
		this.type = type;
		this.typeEditorEventListener = typeEditorEventListener;
		
		initWindow();
		initComponents();
		
		pack();
	}
	
	public TypeChooserFrame(FieldData fieldData, Listener typeEditorEventListener) {
		findSubclassTypesOf(fieldData.GetType());
		
		this.fieldData = fieldData;
		this.type = fieldData.GetType();
		this.typeEditorEventListener = typeEditorEventListener;
		
		initWindow();
		initComponents();
		
		pack();
	}
	
	private void initWindow() {
		setTitle("Chose " + type.getSimpleName() + " type...");
		setLocationRelativeTo(null);
		setResizable(false);
	}
	
	private void initComponents() {
		JPanel panel = new JPanel();
		panel.setBackground(Color.green);
		panel.setLayout(new GridLayout(1, 2, 10,2));
		
		comboBox = new JComboBox<String>(subclassTypes.keySet().toArray(new String[] {}));
		
		JButton button = new JButton("Select");
		button.addActionListener(this);

		panel.add(comboBox);
		panel.add(button);
		add(panel);
	}
	
	@SuppressWarnings("rawtypes")
	private void findSubclassTypesOf(Class<?> clazz) {
		subclassTypes = new HashMap<>();
		
		List<PojoClass> pClasses = PojoClassFactory.enumerateClassesByExtendingType("com.khazovgames.questjobs", clazz, null);
		
		for(PojoClass pClass : pClasses)
			subclassTypes.put(formatClassName(pClass.getClazz().getSimpleName()), pClass.getClazz());
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		Class<?> selectedType = subclassTypes.get(comboBox.getSelectedItem());
		TypeEditorFrame typeEditor = new TypeEditorFrame(fieldData, selectedType);
		typeEditor.registerListener(typeEditorEventListener);
		typeEditor.setVisible(true);
		setVisible(false);
	}
	
	private String formatClassName(String className) {
		return className.replaceAll("(.)([A-Z])", "$1 $2");
	}
}
