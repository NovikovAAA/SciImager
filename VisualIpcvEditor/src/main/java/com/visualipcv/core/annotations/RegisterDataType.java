package com.visualipcv.core.annotations;

import com.visualipcv.core.DataType;

import javax.annotation.processing.SupportedAnnotationTypes;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RegisterDataType {
}
