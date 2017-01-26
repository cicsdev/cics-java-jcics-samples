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

import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ibm.cics.server.CCSIDErrorException;
import com.ibm.cics.server.ChannelErrorException;
import com.ibm.cics.server.CicsConditionException;
import com.ibm.cics.server.CodePageErrorException;
import com.ibm.cics.server.Container;
import com.ibm.cics.server.ContainerErrorException;
import com.ibm.cics.server.InvalidRequestException;
import com.ibm.cics.server.Task;
import com.ibm.cics.server.Channel;

public class LinkServEduchan   {

    // Static fields for Channel and Container names 
    private static final String INPUT_CONTAINER="INPUTDATA";
    private static final String OUTPUT_CONTAINER="OUTPUTDATA";
    private static final String DATE_CONTAINER="CICSTIME";
    private static final String CICSRC_CONTAINER="CICSRC";  
    
    // Static fields for integer return codes
    private static final int CONTAINERERR = 100; // INPUT_CONTAINER was not found 
    private static final int CCSIDERR = 101;
    private static final int CODEPAGEERR = 102;
    private static final int CHANNELERR = 103;
    private static final int INVREQERR = 104;
    private static final int OTHERERROR = 105;


    /**
     * Main entry point to a CICS OSGi program.
     * 
     * The fully qualified name of this class should be added to the CICS-MainClass entry in
     * the parent OSGi bundle's manifest.
     * 
     * This program expects to be invoked with a CHAR container INPUTDATA and
     * returns the date in a CHAR container, the reversed input container
     * and an integer rc in a BIT container
     */
    public static void main(String[] args)  {

        int returncode = 0;
        // Get details of the CICS task
        Task task = Task.getTask();

        // Create a new instance of the class        
        LinkServEduchan linkserver = new LinkServEduchan();        

        // Get the current channel and abend if none present
        Channel chan = task.getCurrentChannel();
        if (chan == null) {
            task.abend("NOCH");
        }

        // Get time for return to caller
        Date timestamp = new Date();
        SimpleDateFormat dfTime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");    

        try {        

            // Get input container and reverse the sequence and return in output container
            // Container object will be null if container not present
            Container input = chan.getContainer(INPUT_CONTAINER);
            if (input!=null) {    
                StringBuilder str = new StringBuilder(input.getString());
                Container output = chan.createContainer(OUTPUT_CONTAINER);
                output.putString(str.reverse().toString());
            }
            else {
                returncode = CONTAINERERR;                
            }

            // Build  output containers
            Container outputCont = chan.createContainer(DATE_CONTAINER);
            outputCont.putString(dfTime.format(timestamp));        

        } catch ( CicsConditionException cce) {
        	// Log a generic error for the CICS condition
            System.out.println ("CICS ERROR from LinkServerEduchan " + cce.getMessage());
            
            // Return an integer rc, this is just to show how to handle binary data in containers
            if (cce instanceof CCSIDErrorException) returncode = CCSIDERR; 
            else if (cce instanceof ChannelErrorException) returncode = CHANNELERR;
            else if (cce instanceof CodePageErrorException) returncode = CODEPAGEERR;
            else if (cce instanceof InvalidRequestException) returncode = INVREQERR;
            else returncode = OTHERERROR;                 
        } 
        // Lastly build BIT container with rc
        linkserver.buildrccont(chan, returncode);
    }


    /**
     * build a BIT container from an int and put into a CICS container in the channel
     *  
     * @param rc - integer value for return code to be converted to a byte array and added as a BIT container
     * @param chan - a reference to the channel into which the container will be put
     */
    public void buildrccont (Channel chan, int rc) {

        // Now write the rc to the container
        try {                    
            byte[] ba;
            ByteBuffer bb = ByteBuffer.allocate(4);
            bb.putInt(rc);
            ba = bb.array();
            Container cicsrc = chan.createContainer(CICSRC_CONTAINER);
            cicsrc.put(ba);                

        } catch ( InvalidRequestException | ChannelErrorException | CCSIDErrorException | CodePageErrorException | ContainerErrorException cce) {
            System.out.println ("CICS ERROR from LinkServerEduchan " + cce.getMessage());
            throw new RuntimeException(cce);        
        }

    }

}
