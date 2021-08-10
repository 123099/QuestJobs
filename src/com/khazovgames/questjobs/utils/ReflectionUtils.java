package com.khazovgames.questjobs.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.khazovgames.questjobs.gui.FieldData;

public final class ReflectionUtils {

	public static Field[] GetAllFields(Class<?> clazz) {
		List<Field> fields = new ArrayList<Field>();
		Class<?> currentClass = clazz;
		while(currentClass != null) {
			fields.addAll(Arrays.asList(currentClass.getDeclaredFields()));
			currentClass = currentClass.getSuperclass();
		}
		
		return fields.toArray(new Field[] {});
	}
	
	public static Field[] GetAllFieldsAnnotatedWith(Class<?> clazz, Class<? extends Annotation> annotationClass) {
		List<Field> annotatedFields = new ArrayList<Field>();
		
		Field[] allFields = ReflectionUtils.GetAllFields(clazz);
        for(Field field : allFields) {
        	Annotation[] annotations = field.getDeclaredAnnotations();
        	for(Annotation annotation : annotations) {
        		if(annotation.annotationType() == annotationClass) {
        			annotatedFields.add(field);
        			break;
        		}
        	}
        }
        
        return annotatedFields.toArray(new Field[] {});
	}
	
	public static FieldData GetMatchingFieldDataToParameter(Parameter parameter, FieldData[] fieldsToTest) {
		String parameterName = parameter.getName();
		for(FieldData fieldData : fieldsToTest) {
			if(fieldData.GetName().equals(parameterName))
				return fieldData;
		}
		
		return null;
	}
}
