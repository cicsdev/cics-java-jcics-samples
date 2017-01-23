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


public class LinkServEC01   {
	
	private static final int CA_LEN = 18 ; // Length of commarea returned by EC01	
	private static final String abcode = "LEN" ; // Abend code if commarea too short	
	private static final String LOCALCCSID = System.getProperty("com.ibm.cics.jvmserver.local.ccsid");

	/**
	 * Main entry point to a CICS OSGi program.
	 * 
	 * The fully qualified name of this class should be added to the CICS-MainClass entry in
	 * the parent OSGi bundle's manifest.
	 * 
	 * This program expects to be invoked with a COMMAREA of 18 bytes and
	 * returns the date
	 */
	public static void main(CommAreaHolder cah)  {
		
		Task task = Task.getTask();

        // Check input area is long enough, else abend task
        if (cah.getValue().length < CA_LEN ){
			task.abend(abcode);			
		}

		// Get time for return to caller
		Date timestamp = new Date();
		SimpleDateFormat dfTime = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
		
		// Create byte array in local encoding and copy into CA holder		
		try {
			byte ba[] = dfTime.format(timestamp).getBytes(LOCALCCSID);
			System.arraycopy (ba, 0, cah.getValue(), 0, ba.length);
		} catch (UnsupportedEncodingException uee) {
			throw new RuntimeException(uee);
		}				
	
	}
}
