package com.pps.fcs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.pps.common.model.PaymentCanonical;
import com.pps.fcs.producer.JmsProducer;

/**
 * A rest controller to facilitate the unit testing.
 * 
 * @author shashank
 *
 */
@RestController
public class FCSController {

	@Autowired
	JmsProducer jmsProducer;

	@PostMapping("/sendPayment")
	public void sendToTopic(@RequestBody PaymentCanonical paymentCanonical) {
		jmsProducer.sendMessageTest(paymentCanonical);
	}
}
