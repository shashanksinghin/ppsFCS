package com.pps.fcs.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.pps.common.model.PaymentCanonical;
import com.pps.fcs.service.FCSService;

/**
 * A service class to perform business operation to detect a fraud.
 * 
 * @author shashank
 *
 */
@Service
public class FCSServiceImpl implements FCSService {

	@Value("${blacklisted.payer.names}")
	String[] blacklistedPayerNames;

	@Value("${blacklisted.payee.names}")
	String[] blacklistedPayeeNames;

	@Value("${blacklisted.payer.country}")
	String[] blacklistedPayerCountry;

	@Value("${blacklisted.payee.country}")
	String[] blacklistedPayeeCountry;

	@Override
	public boolean processFCS(PaymentCanonical paymentCanonical) {

		boolean isLegitimatePayment = true;

		isLegitimatePayment = validatePayeeName(paymentCanonical);

		if (isLegitimatePayment) {
			isLegitimatePayment = validatePayeeCountry(paymentCanonical);
		}

		if (isLegitimatePayment) {
			isLegitimatePayment = validatePayerName(paymentCanonical);
		}

		if (isLegitimatePayment) {
			isLegitimatePayment = validatePayerCountry(paymentCanonical);
		}

		return isLegitimatePayment;
	}

	private boolean validatePayeeCountry(PaymentCanonical paymentCanonical) {
		boolean isLegitimatePayment = true;
		for (int i = 0; i < blacklistedPayeeCountry.length; i++) {
			if (paymentCanonical.getPaymentInfo().getPayeeCountryCode() != null && blacklistedPayeeCountry[i]
					.equalsIgnoreCase(paymentCanonical.getPaymentInfo().getPayeeCountryCode())) {
				isLegitimatePayment = false;
				break;
			}
		}
		return isLegitimatePayment;
	}

	private boolean validatePayerCountry(PaymentCanonical paymentCanonical) {
		boolean isLegitimatePayment = true;
		for (int i = 0; i < blacklistedPayerCountry.length; i++) {

			if (paymentCanonical.getPaymentInfo().getPayerCountryCode() != null && blacklistedPayerCountry[i]
					.equalsIgnoreCase(paymentCanonical.getPaymentInfo().getPayeeCountryCode())) {
				isLegitimatePayment = false;
				break;
			}
		}
		return isLegitimatePayment;
	}

	private boolean validatePayerName(PaymentCanonical paymentCanonical) {
		boolean isLegitimatePayment = true;
		for (int i = 0; i < blacklistedPayerNames.length; i++) {

			if (paymentCanonical.getPaymentInfo().getPayerName().equalsIgnoreCase(blacklistedPayerNames[i])) {
				isLegitimatePayment = false;
				break;
			}
		}
		return isLegitimatePayment;
	}

	private boolean validatePayeeName(PaymentCanonical paymentCanonical) {
		boolean isLegitimatePayment = true;
		for (int i = 0; i < blacklistedPayeeNames.length; i++) {

			if (paymentCanonical.getPaymentInfo().getPayeeName().equalsIgnoreCase(blacklistedPayeeNames[i])) {
				isLegitimatePayment = false;
				break;
			}
		}
		return isLegitimatePayment;
	}

}
