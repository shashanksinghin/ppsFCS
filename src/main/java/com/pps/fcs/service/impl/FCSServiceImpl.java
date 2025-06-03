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

	@Value("${blacklisted.payer.bank}")
	String[] blacklistedPayerBank;

	@Value("${blacklisted.payee.bank}")
	String[] blacklistedPayeeBank;

	@Value("${blacklisted.paymentInstruction}")
	String[] blacklistedPaymentInstruction;

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

		if (isLegitimatePayment) {
			isLegitimatePayment = validatePayerBank(paymentCanonical);
		}

		if (isLegitimatePayment) {
			isLegitimatePayment = validatePayeeBank(paymentCanonical);
		}

		if (isLegitimatePayment) {
			isLegitimatePayment = validatePaymentInstruction(paymentCanonical);
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

	private boolean validatePayerBank(PaymentCanonical paymentCanonical) {
		boolean isLegitimatePayment = true;
		for (int i = 0; i < blacklistedPayerBank.length; i++) {

			if (paymentCanonical.getPaymentInfo().getPayerBank().equalsIgnoreCase(blacklistedPayerBank[i])) {
				isLegitimatePayment = false;
				break;
			}
		}
		return isLegitimatePayment;
	}

	private boolean validatePayeeBank(PaymentCanonical paymentCanonical) {
		boolean isLegitimatePayment = true;
		for (int i = 0; i < blacklistedPayeeBank.length; i++) {

			if (paymentCanonical.getPaymentInfo().getPayeeBank().equalsIgnoreCase(blacklistedPayeeBank[i])) {
				isLegitimatePayment = false;
				break;
			}
		}
		return isLegitimatePayment;
	}

	private boolean validatePaymentInstruction(PaymentCanonical paymentCanonical) {
		boolean isLegitimatePayment = true;
		for (int i = 0; i < blacklistedPaymentInstruction.length; i++) {

			if (paymentCanonical.getPaymentInfo().getPaymentInstruction().equalsIgnoreCase(blacklistedPaymentInstruction[i])) {
				isLegitimatePayment = false;
				break;
			}
		}
		return isLegitimatePayment;
	}

}
