package com.pps.fcs.service;

import com.pps.common.model.PaymentCanonical;

/**
 * A service interface to perform business operation to detect a fraud.
 * 
 * @author shashank
 *
 */
public interface FCSService {
	public boolean processFCS(PaymentCanonical paymentCanonical);

}
