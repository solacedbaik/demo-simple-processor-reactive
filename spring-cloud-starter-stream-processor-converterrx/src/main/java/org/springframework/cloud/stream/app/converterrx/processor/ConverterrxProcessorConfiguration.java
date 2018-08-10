/*
 * 
 * Copyright (c) 2018 Solace Corp.
 * 
 */
package org.springframework.cloud.stream.app.converterrx.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import reactor.core.publisher.Flux;


/**
 * A temperature units conversion processor (Metric / Imperial)
 *
 * @author Solace Corp
 */
@EnableBinding(Processor.class)
@EnableConfigurationProperties(ConverterrxProcessorProperties.class)
public class ConverterrxProcessorConfiguration {
	@Autowired
	private ConverterrxProcessorProperties properties;

	// Functional (reactive) style
	@StreamListener
	@Output(Processor.OUTPUT)
    public Flux<Double> doConversion(@Input(Processor.INPUT) Flux<Double> temperature) {
		if (properties.getConvertToCelsius())
			return temperature.map(val -> (val - 32) * 5 / 9);
		else
			return temperature.map(val -> val * 9 / 5 + 32);
    }
}
