package com.idevicesinc.sweetblue.annotations;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Classes marked with this {@link Annotation} have immutable internal state, or the apparent behavior of so.
 * Most of the time this means all private final members.
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.CLASS)
public @interface Immutable
{

}
