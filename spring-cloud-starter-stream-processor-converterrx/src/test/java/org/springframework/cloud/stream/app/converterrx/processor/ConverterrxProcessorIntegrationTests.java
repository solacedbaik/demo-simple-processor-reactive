/*
 * 
 * Copyright (c) 2018 Solace Corp.
 * 
 */

package org.springframework.cloud.stream.app.converterrx.processor;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.annotation.Output;

import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.cloud.stream.test.matcher.MessageQueueMatcher.receivesPayloadThat;

/**
 * Integration Tests for the Converterrx Processor.
 *
 * @author Solace Corp.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
@SpringBootTest
public abstract class ConverterrxProcessorIntegrationTests {

	@Autowired
	protected Processor channels;

	@Autowired
	protected MessageCollector collector;

	/**
	 * Validates that the module loads with default properties.
	 */
	public static class UsingNothingIntegrationTests extends ConverterrxProcessorIntegrationTests {

		@Test
	    @Output(Processor.OUTPUT)
		public void test() {
			channels.input().send(new GenericMessage<Double>((double) 123));
			assertThat(collector.forChannel(channels.output()), receivesPayloadThat(is("50.55555555555556")));
		}
	}

	@SpringBootTest("converterrx.convertToCelsius=false")
	public static class UsingPropsIntegrationTests extends ConverterrxProcessorIntegrationTests {
		@Test
		public void test() {
			this.channels.input().send(new GenericMessage<Double>((double)123));
			assertThat(this.collector.forChannel(this.channels.output()), receivesPayloadThat(is("253.4")));
		}
	}

	@SpringBootApplication
	public static class ConverterrxProcessorApplication {
		public static void main(String[] args) {
			SpringApplication.run(ConverterrxProcessorApplication.class, args);
		}
	}

}
