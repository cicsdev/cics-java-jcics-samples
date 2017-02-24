/* Licensed Materials - Property of IBM                                   */
/*                                                                        */
/* SAMPLE                                                                 */
/*                                                                        */
/* (c) Copyright IBM Corp. 2016 All Rights Reserved                       */
/*                                                                        */
/* US Government Users Restricted Rights - Use, duplication or disclosure */
/* restricted by GSA ADP Schedule Contract with IBM Corp                  */
/*                                                                        */

package com.ibm.cicsdev.link;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ibm.cics.server.CommAreaHolder;
import com.ibm.cics.server.Task;

/**
 * Provides a Java implementation that is functionally equivalent to the COBOL
 * EC01 sample program.
 */
public class LinkServEC01 
{
	/**
	 * Length of the COMMAREA returned by this program.
	 */
	private static final int CA_LEN = 18;

	/**
	 * Abend code used if the COMMAREA is too short.
	 */
	private static final String CA_LEN_ABCODE = "LEN";

	/**
	 * CCSID of the CICS region.
	 */
	private static final String LOCALCCSID = System.getProperty("com.ibm.cics.jvmserver.local.ccsid");

	/**
	 * Main entry point to a CICS OSGi program.
	 * 
	 * The fully qualified name of this class should be added to the
	 * CICS-MainClass entry in the parent OSGi bundle's manifest.
	 * 
	 * This program is a Java version of the COBOL EC01 sample. It expects to be
	 * invoked with a COMMAREA of 18 bytes and returns the date.
	 */
	public static void main(CommAreaHolder cah) {

		// Get a reference to the current CICS task
		Task task = Task.getTask();

		// Check input area is long enough, else abend task
		if (cah.getValue().length < CA_LEN) {
			task.abend(CA_LEN_ABCODE);
		}

		// Get time for return to caller
		Date timestamp = new Date();
		SimpleDateFormat dfTime = new SimpleDateFormat("dd/MM/yy HH:mm:ss");

		try {
			// Create byte array in local encoding
			byte ba[] = dfTime.format(timestamp).getBytes(LOCALCCSID);

			// Copy into the byte array provided by the CommAreaHolder object
			System.arraycopy(ba, 0, cah.getValue(), 0, ba.length);
		} catch (UnsupportedEncodingException uee) {
			// Crude error handling for simple example
			throw new RuntimeException(uee);
		}
	}
}
