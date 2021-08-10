package com.khazovgames.questjobs.gui;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class FieldData {

	private Field field; //The field represented by this data
	private Object value; //The value that the user intends to have for this field
	private String name; //The name of the field
	private Class<?> type; //The type of the field
	
	private Object instance; //The instance of an object containing this field
	
	public FieldData(Field field, Object instance) {
		this(field, instance, null);
		SetIntendedValueAsInstanceValue();
	}
	
	public FieldData(Field field, Object instance, Object initialValue) {
		this.field = field;
		this.instance = instance;
		this.value = initialValue;
		
		name = field.getName();
		type = field.getType();
		
		field.setAccessible(true);
	}
	
	public Field GetField() {
		return field;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T GetCurrentValue() {
		return (T) value;
	}
	
	public Object GetActualValueFromInstance() {
		if(!IsInstanceSet()) return null;
		
		try { return field.get(instance); }
		catch (IllegalArgumentException | IllegalAccessException e) { e.printStackTrace(); return null; }
	}
	
	public String GetName() {
		return name;
	}
	
	public Class<?> GetType(){
		return type;
	}
	
	public Class<?> GetArrayType(){
		return type.getComponentType();
	}
	
	@SuppressWarnings("rawtypes")
	public Enum[] GetEnumConstants() {
		return (Enum[]) type.getEnumConstants();
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Annotation> T GetAnnotation(Class<? extends T> annotationClass) {
		Annotation[] annotations = field.getDeclaredAnnotations();
		for(Annotation annotation : annotations)
			if(annotation.annotationType() == annotationClass)
				return (T)annotation;
		
		return null;
	}
	
	public boolean IsPrimitive() {
		return type.isPrimitive();
	}
	
	public boolean IsString() {
		return type == String.class;
	}
	
	public boolean IsPrimitiveOrString() {
		return IsPrimitive() || IsString();
	}
	
	public boolean IsEnum() {
		return type.isEnum();
	}
	
	public boolean IsBoolean() {
		return type == Boolean.TYPE;
	}
	
	public boolean IsArray() {
		return type.isArray();
	}
	
	public boolean IsInstanceSet() {
		return instance != null;
	}
	
	public boolean IsCurrentValueSet() {
		return value != null;
	}
	
	public void SetIntendedValue(Object value) {
		this.value = value;
	}
	
	public void SetFieldValueInInstance() {
		if(!IsInstanceSet()) return;
		
		try { field.set(instance, value); } 
		catch (IllegalArgumentException | IllegalAccessException e) { e.printStackTrace(); }
	}
	
	public void SetIntendedValueAsInstanceValue() {
		if(IsInstanceSet()) {
			SetIntendedValue(GetActualValueFromInstance());
			SetFieldValueInInstance();
		}
		else
			SetIntendedValue(null);
	}
	
	public void SetInstance(Object instance) {
		this.instance = instance;
	}
	
	public Object Cast(Object objectToCast) {
		if(!IsPrimitive() && !(objectToCast instanceof String))
			return type.cast(objectToCast);
		
		String stringToCast = (String)objectToCast;
		if(type == Integer.TYPE)
			return Integer.parseInt(stringToCast);
		else if(type == Boolean.TYPE)
			return Boolean.parseBoolean(stringToCast);
		else if(type == Double.TYPE)
			return Double.parseDouble(stringToCast);
		else
			return stringToCast;
	}
}
