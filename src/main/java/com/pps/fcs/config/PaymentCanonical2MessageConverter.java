package com.pps.fcs.config;

import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pps.common.model.PaymentCanonical;
import com.pps.fcs.utils.FCSConstants;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;

/**
 * This is Message Converter class to convert canonical pojo to message and
 * vice-versa.
 * 
 * @author shashank
 *
 */
public class PaymentCanonical2MessageConverter implements MessageConverter {

	private ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public Message toMessage(Object object, Session session) throws JMSException, MessageConversionException {

		PaymentCanonical paymentCanonical = (PaymentCanonical) object;

		try {
			String json = objectMapper.writeValueAsString(paymentCanonical);
			TextMessage textMessage = session.createTextMessage(json);
			return textMessage;
		} catch (Exception e) {
			throw new MessageConversionException(FCSConstants.FAILED_TO_CONVERT_MSG_TO_POJO, e);
		}
	}

	@Override
	public Object fromMessage(Message message) throws JMSException, MessageConversionException {
		try {
			TextMessage textMessage = (TextMessage) message;
			String json = textMessage.getText();
			return objectMapper.readValue(json, PaymentCanonical.class);
		} catch (Exception e) {
			throw new MessageConversionException(FCSConstants.FAILED_TO_CONVERT_POJO_TO_MSG, e);
		}
	}

}
