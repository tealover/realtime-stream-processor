/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.inchtek.realtime.stream.processor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.stream.input.InputHandler;
import org.wso2.siddhi.core.stream.output.StreamCallback;
import org.wso2.siddhi.core.util.EventPrinter;

import com.alibaba.fastjson.JSON;

/**
 * Sample demonstrating a simple filtering use-case
 */
public class SimpleFilterSample {

	public static void main(String[] args) throws InterruptedException {

		Properties props = new Properties();
		props.put("bootstrap.servers",
				"172.17.80.13:9092,172.17.80.14:9092, 172.17.80.15:9092");
		props.put("group.id", "c2monit2");
		props.put("enable.auto.commit", "true");
		props.put("auto.commit.interval.ms", "1000");
		props.put("session.timeout.ms", "30000");
		props.put("key.deserializer",
				"org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer",
				"org.apache.kafka.common.serialization.StringDeserializer");
		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
		consumer.subscribe(Arrays.asList("tcollector_metric"));

	
		// Create Siddhi Manager
		SiddhiManager siddhiManager = new SiddhiManager();

		siddhiManager
				.defineStream("define stream MetricStream ( metric string, value double, timestamp long , tags string)");
		siddhiManager.addQuery("from  MetricStream select metric,  str:strcmp(metric, 'proc.uptime.now') as compareTo "
				+ "insert into ServerUptimeEvent ;");

		siddhiManager.addCallback("ServerUptimeEvent", new StreamCallback() {
			@Override
			public void receive(Event[] events) {
				EventPrinter.print(events);
			}
		});
		InputHandler metricStreamHandler = siddhiManager
				.getInputHandler("MetricStream");
		//inputHandler.send(new Object[] { "IBM", 75.6f, 100 });
		//inputHandler.send(new Object[] { "GOOG", 50f, 100 });
		//inputHandler.send(new Object[] { "IBM", 76.6f, 100 });
		//inputHandler.send(new Object[] { "WSO2", 45.6f, 100 });
		//Thread.sleep(500);
		
		while (true) {
			ConsumerRecords<String, String> records = consumer.poll(100);
			for (ConsumerRecord<String, String> record : records) {
				String msg = record.value();
				System.out.println(msg);
				List<MetricStream> mArrays = JSON.parseArray(msg, MetricStream.class);
				Object[] metrics = mArrays.toArray();
				metricStreamHandler.send(metrics);
			}
		}
		//siddhiManager.shutdown();
	}
}
