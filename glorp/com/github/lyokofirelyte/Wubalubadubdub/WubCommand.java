package com.github.lyokofirelyte.Wubalubadubdub;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
public @interface WubCommand {
	String[] commands();
	String perm() default "wub.lub";
	String help() default "M-m-morty! Look at, at what you did! Yo-oo-u screw *burp* screwed it all up!";
	int maxArgs() default 99999;
	int minArgs() default 0;
}