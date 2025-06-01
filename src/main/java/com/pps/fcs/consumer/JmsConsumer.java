package com.pps.fcs.consumer;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.pps.common.enums.PaymentStatus;
import com.pps.common.model.PaymentAudit;
import com.pps.common.model.PaymentCanonical;
import com.pps.common.utils.MessageConvertor;
import com.pps.common.utils.PaymentConstants;
import com.pps.fcs.producer.JmsProducer;
import com.pps.fcs.service.FCSService;
import com.pps.fcs.utils.FCSConstants;

import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import lombok.extern.slf4j.Slf4j;

/**
 * A JMS Consumer to receive payment for FCS processing.
 * 
 * @author shashank
 *
 */
@Component
@Slf4j
public class JmsConsumer implements MessageListener {

	@Autowired
	private FCSService fcsService;

	@Autowired
	private JmsProducer jmsProducer;

	@Override
	@JmsListener(destination = "${active-mq.req-topic}")
	public void onMessage(Message message) {
		try {
			ActiveMQTextMessage objectMessage = (ActiveMQTextMessage) message;

			log.info(FCSConstants.RECEIVED_MESSAGE + objectMessage.getText());

			PaymentCanonical paymentCanonical = MessageConvertor.covertXmlToPojo(objectMessage.getText());

			boolean isLegitimatePayment = fcsService.processFCS(paymentCanonical);

			PaymentAudit paymentAudit = PaymentAudit.builder().statusDateTime(LocalDateTime.now()).build();

			if (paymentCanonical.getPaymentAudits() == null) {
				paymentCanonical.setPaymentAudits(new ArrayList<>());
			}

			paymentCanonical.getPaymentAudits().add(paymentAudit);

			paymentAudit.setPaymentStatus(PaymentStatus.SUCESSFUL);
			paymentAudit.setPaymentOperation(FCSConstants.FCS);

			if (isLegitimatePayment) {
				paymentAudit.setStatusCode(PaymentConstants.FCS001);
				paymentAudit.setStatusDesc(PaymentConstants.APPROVED);
			} else {
				paymentAudit.setStatusCode(PaymentConstants.FCS002);
				paymentAudit.setStatusDesc(PaymentConstants.REJECTED);
			}

			String paymentCanonicalXml =  MessageConvertor.covertPojoToXml(paymentCanonical);
			
			jmsProducer.sendMessage(paymentCanonicalXml);
		} catch (Exception e) {
			log.error(FCSConstants.RECEIVED_EXCEPTION + e);
		}
	}
}