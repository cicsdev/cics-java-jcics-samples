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

/**
 * Provides a Java implementation that is functionally equivalent to the COBOL
 * EDUCHAN sample program.
 */
public class LinkServEduchan
{   
    /**
     * Name of the container used to send data into this program.
     */
    private static final String INPUT_CONTAINER = "INPUTDATA";
    
    /**
     * Name of the container which will contain the date as a response
     * from this program.
     */
    private static final String DATE_CONTAINER = "CICSTIME";
    
    /**
     * Name of the container which will contain the CICS return code
     * as a response from this program.
     */
    private static final String CICSRC_CONTAINER = "CICSRC";

    /**
     * Name of the container used to return data from this program.
     */
    private static final String OUTPUT_CONTAINER = "OUTPUTDATA";  

    /**
     * Main entry point to a CICS OSGi program.
     * 
     * The fully qualified name of this class should be added to the
     * CICS-MainClass entry in the parent OSGi bundle's manifest.
     * 
     * This program expects to be invoked with a CHAR container INPUTDATA.
     * The date and the reversed input are returned in a CHAR container.
     * An integer return code is returned in a BIT container.
     */
    public static void main(String[] args)  {

        // Return code from this method
        ReturnCode rc = ReturnCode.OK;
        
        // Get details of the CICS task
        Task task = Task.getTask();

        // Create a new instance of the class
        LinkServEduchan linkserver = new LinkServEduchan();        

        // Get the current channel and abend if none present
        Channel chan = task.getCurrentChannel();
        if (chan == null) {
            task.abend("NOCH");
        }

        try {
            // Get input container and reverse the sequence and return in output container
            // Container object will be null if container not present
            Container input = chan.getContainer(INPUT_CONTAINER);
            if (input != null) {
                
                // Get the input
                StringBuilder str = new StringBuilder(input.getString());
                
                // Reverse and store in the output container
                Container output = chan.createContainer(OUTPUT_CONTAINER);
                output.putString(str.reverse().toString());
            }
            else {
                // Input container not found 
                rc = ReturnCode.CONTAINERERR;                
            }

            // Get time for return to caller
            SimpleDateFormat dfTime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");    

            // Build date output container
            Container outputCont = chan.createContainer(DATE_CONTAINER);
            outputCont.putString( dfTime.format(new Date()) );        
        }
        catch ( CicsConditionException cce) {
            
            // Log a generic error for the CICS condition
            System.out.println("CICS ERROR from LinkServerEduchan " + cce.getMessage());

            // Decide on the error type and set RC accordingly
            if (cce instanceof CCSIDErrorException) rc = ReturnCode.CCSIDERR;
            else if (cce instanceof ChannelErrorException) rc = ReturnCode.CHANNELERR;
            else if (cce instanceof CodePageErrorException) rc = ReturnCode.CODEPAGEERR;
            else if (cce instanceof InvalidRequestException) rc = ReturnCode.INVREQERR;
            else rc = ReturnCode.OTHERERROR;
        }
        
        // Lastly build BIT container with rc. Return an integer rc, this
        // is just to show how to handle binary data in containers
        linkserver.buildRcContainer(chan, rc.getNumVal());
    }


    /**
     * Build a BIT container from an int and put into a CICS container in the channel.
     *  
     * @param rc - integer value for return code to be converted to a byte array and added as a BIT container
     * @param chan - a reference to the channel into which the container will be put
     */
    public void buildRcContainer(Channel chan, int rc) {

        // Write the rc to the container
        try {
            // Write an int to a byte[]
            ByteBuffer bb = ByteBuffer.allocate(4);
            bb.putInt(rc);
            
            // Obtain the BIT data and store in the container
            byte[] ba = bb.array();
            Container cicsrc = chan.createContainer(CICSRC_CONTAINER);
            cicsrc.put(ba);
        }
        catch ( InvalidRequestException | ChannelErrorException | CCSIDErrorException |
                CodePageErrorException | ContainerErrorException exc) {
            
            // Log the message and throw wrappered to reduce error-handling in this example
            System.out.println("CICS error from LinkServerEduchan " + exc.getMessage());
            throw new RuntimeException(exc);
        }
    }


    /**
     * Internal enum used to store integer values for the possible return codes from
     * this program.
     */
    private static enum ReturnCode {
        
        OK(0),
        CONTAINERERR(100),
        CCSIDERR(101),
        CODEPAGEERR(102),
        CHANNELERR(103),
        INVREQERR(104),
        OTHERERROR(105);
        
        private int iErrorVal;

        private ReturnCode(int i) {
            this.iErrorVal = i;
        }

        public int getNumVal() {
            return this.iErrorVal;
        }
    }
}
