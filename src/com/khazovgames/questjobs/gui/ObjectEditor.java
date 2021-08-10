package com.khazovgames.questjobs.gui;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;

import com.khazovgames.questjobs.utils.ReflectionUtils;
import com.khazovgames.questjobs.utils.SerializeField;

public class ObjectEditor {

	private Object objectInstance; //The instance of the object we are editting. Can be null
	private Class<?> objectType; //The type of the object we are editting
	
	private FieldData[] fieldData;
	
	public ObjectEditor(Object objectInstance, Class<?> objectType) {
		this.objectInstance = objectInstance;
		this.objectType = objectType;
		
		initFieldData();
	}
	
	private void initFieldData() {
		Field[] objectFields = ReflectionUtils.GetAllFieldsAnnotatedWith(objectType, SerializeField.class);
		fieldData = new FieldData[objectFields.length];
		
		for(int i = 0; i < objectFields.length; ++i) {
			fieldData[i] = new FieldData(objectFields[i], objectInstance);
		}
	}
	
	public int GetIndexOf(Field field) {
		for(int i = 0; i < fieldData.length; ++i)
			if(fieldData[i].GetField() == field)
				return i;
		return -1;
	}
	
	public FieldData GetFieldData(int index) {
		assert(index >= 0 && index < fieldData.length);
		return fieldData[index];
	}
	
	public Object GetObjectInstance() {
		return objectInstance;
	}
	
	public Class<?> GetObjectType() {
		return objectType;
	}
	
	public Field[] GetFieldArray() {
		Field[] fields = new Field[fieldData.length];
		for(int i = 0; i < fields.length; ++i)
			fields[i] = fieldData[i].GetField();
		
		return fields;
	}
	
	public int GetFieldDataCount() {
		return fieldData.length;
	}
	
	public void SetObjectInstance(Object instance) {
		this.objectInstance = instance;
		
		for(FieldData data : fieldData) {
			data.SetInstance(objectInstance);
			data.SetIntendedValueAsInstanceValue();
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> T ConstructObjectFromFieldData() {
		Constructor<?> mainConstructor = objectType.getConstructors()[0];
		Parameter[] constructorParams = mainConstructor.getParameters();
		Object[] constructorArguments = new Object[constructorParams.length];
			
		for(int i = 0; i < constructorParams.length; ++i) 
		{
			FieldData matchingField = ReflectionUtils.GetMatchingFieldDataToParameter(constructorParams[i], fieldData);
			
			if(matchingField == null)
				constructorArguments[i] = null;
			else 
				constructorArguments[i] = matchingField.GetCurrentValue();
		}
		
		try 
		{
			Object newInstance = mainConstructor.newInstance(constructorArguments); 
			return (T) objectType.cast(newInstance);
		} 
		catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) 
		{ 
			e.printStackTrace(); 
			return null;
		}
	}
}
