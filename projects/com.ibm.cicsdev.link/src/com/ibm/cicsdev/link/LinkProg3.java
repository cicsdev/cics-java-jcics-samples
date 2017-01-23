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
import java.text.MessageFormat;

import com.ibm.cics.server.CCSIDErrorException;
import com.ibm.cics.server.Channel;
import com.ibm.cics.server.ChannelErrorException;
import com.ibm.cics.server.CicsConditionException;
import com.ibm.cics.server.CodePageErrorException;
import com.ibm.cics.server.Container;
import com.ibm.cics.server.ContainerErrorException;
import com.ibm.cics.server.InvalidRequestException;
import com.ibm.cics.server.Program;
import com.ibm.cics.server.Task;

public class LinkProg3 extends LinkProgCommon {

    private static final String PROG_NAME = "EDUCHAN";
    private static final String CHANNEL = "EDUCHAN";
    private static final String INPUT_CONTAINER = "INPUTDATA";
    private static final String DATE_CONTAINER = "CICSTIME";
    private static final String CICSRC_CONTAINER = "CICSRC";

    private static final String INPUTSTRING = "Hello from Java";

    /**
     * Constructor used to pass data to superclass constructor.
     * 
     * @param task - the current CICS task executing this example.
     * @param prog - the program reference we will
     * be manipulating in this example.
     */
    private LinkProg3(Task task, Program prog)
    {
        super(task, prog);
    }

    /**
     * Main entry point to a CICS OSGi program.
     * This can be called via a LINK or a 3270 attach
     * 
     * The fully qualified name of this class should be added to the CICS-MainClass 
     * entry in the parent OSGi bundle's manifest.
     */
    public static void main(String[] args)
    {
        // Get details about our current CICS task
        Task task = Task.getTask();
        task.out.println(" - Starting LinkProg3");
        String msg = "";

        // Create a reference to the Program we will invoke
        Program prog = new Program();

        // Specify the properties on the program    
        prog.setName(PROG_NAME);
        
        // Don't syncpoint between remote links, this is the default 
        // Setting true ensures each linked program runs in its own UOW and
        // allows the a remote server program to use a syncpoint command
        prog.setSyncOnReturn(false); 

        // Create a new instance of the class        
        LinkProg3 lp = new LinkProg3(task, prog);

        // Build up the Channel and containers
        Channel chan = lp.buildChannel();

        // link to CICS program
        lp.linkProg(chan);        

        // Get output data from output container
        String resultStr = "";
        int cicsrc = 0 ;

        try {

            // Read CHAR container from channel container data as formatted string
            // CICS returns this in a UTF16 format and JCICS reads this into a String
            // Container object will be null if container not present
            Container charContainer = chan.getContainer(DATE_CONTAINER);
            if (charContainer != null) {
                resultStr = charContainer.getString();
            }

            // Get CICS return code from binary container and convert to 32-bit int/fullword
            // CICS returns this unconverted so wrapper in a byte buffer and convert to an int    
            Container bitContainer = chan.getContainer(CICSRC_CONTAINER);
            if (bitContainer != null) {
                byte[] ba = bitContainer.get();
                ByteBuffer bb = ByteBuffer.wrap(ba);                     
                cicsrc = bb.getInt(); 
                msg = MessageFormat.format("Returned from link to {0} with rc({1}) date {2}",
                        prog.getName(),cicsrc,resultStr);
            }

        } catch (ContainerErrorException | ChannelErrorException | CCSIDErrorException | CodePageErrorException cce) {
            msg = MessageFormat.format("ERROR from link to {0} with message({1}) ",
                    prog.getName(), cce.getMessage());
        }

        // Return output to caller and exit        
        task.out.println(msg);
    }

    // LINK to the CICS program
    private void linkProg(Channel chan)
    {
        try {
             prog.link(chan);
        } catch (CicsConditionException cce) {
            System.out.println("ERROR from LinkServerEduchan " + cce.getMessage());       
        }
    }

    // Build channel and populate with simple char container with string data
    private Channel buildChannel()
    {
        Channel testChannel = null;

        try {

            // Create channel
            testChannel = task.createChannel(CHANNEL);

            // Create a CHAR container populated with a simple String
            // CHAR containers will be created in UTF16 when created iin JCICS
            Container inputContainer = testChannel.createContainer(INPUT_CONTAINER);
            inputContainer.putString(INPUTSTRING);

        } catch (ChannelErrorException | ContainerErrorException | InvalidRequestException | CCSIDErrorException | CodePageErrorException cce) {
            throw new RuntimeException(cce);
        }
        
        return testChannel;
    }
}
