package com.pps.fcs.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.pps.common.model.PaymentCanonical;
import com.pps.fcs.utils.FCSConstants;

import lombok.extern.slf4j.Slf4j;

/**
 * A JMS producer to send processed payment to BS.
 * 
 * @author shashank
 *
 */
@Component
@Slf4j
public class JmsProducer {

	@Autowired
	private JmsTemplate jmsTemplate;

	@Value("${active-mq.res-topic}")
	private String resTopic;

	@Value("${active-mq.req-topic}")
	private String reqTopic;

	public void sendMessage(String message) {
		try {
			log.info(FCSConstants.ATTEMPTING_SEND_MSG_TO_BS + resTopic);
			jmsTemplate.convertAndSend(resTopic, message);
		} catch (Exception e) {
			log.error(FCSConstants.RECEIVED_EXCEPTION_WHILE_MSG_SEND, e);
		}
	}

	public void sendMessageTest(PaymentCanonical message) {
		try {
			log.info(FCSConstants.ATTEMPTING_SEND_MSG_TO_FCS + reqTopic);
			jmsTemplate.convertAndSend(reqTopic, message);
		} catch (Exception e) {
			log.error(FCSConstants.RECEIVED_EXCEPTION_WHILE_MSG_SEND, e);
		}
	}
}