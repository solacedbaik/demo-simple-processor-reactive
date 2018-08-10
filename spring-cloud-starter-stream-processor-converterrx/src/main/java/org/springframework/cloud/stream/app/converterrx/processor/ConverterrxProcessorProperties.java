/*
 * 
 * Copyright (c) 2018 Solace Corp.
 * 
 */
package org.springframework.cloud.stream.app.converterrx.processor;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for the Converterrx Processor module.
 * Determines whether conversion should be to Celsius or Fahrenheit.
 *
 * @author Solace Corp.
 */
@ConfigurationProperties("converterrx")
public class ConverterrxProcessorProperties {
	private boolean convertToCelsius = true;

	public void setConvertToCelsius(boolean convertToCelsius) {
		this.convertToCelsius = convertToCelsius;
	}

	public boolean getConvertToCelsius() {
		return convertToCelsius;
	}
}
