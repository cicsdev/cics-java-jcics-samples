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

/**
 * Provides a simple example of LINKing to a CICS program using JCICS,
 * using channels and containers.
 * 
 * This class executes in an OSGi JVM server environment.
 */
public class LinkProg3 extends LinkProgCommon
{
    /**
     * Name of the program to invoke.
     */
    private static final String PROG_NAME = "EDUCHAN";
    
    /**
     * Name of the channel to use.
     */
    private static final String CHANNEL = "EDUCHAN";
    
    /**
     * Name of the container used to send data to the target program.
     */
    private static final String INPUT_CONTAINER = "INPUTDATA";
    
    /**
     * Name of the container which will contain the date as a response
     * from the target program.
     */
    private static final String DATE_CONTAINER = "CICSTIME";
    
    /**
     * Name of the container which will contain the CICS return code
     * as a response from the target program.
     */
    private static final String CICSRC_CONTAINER = "CICSRC";

    /**
     * Data to place in the container to be sent to the target program.
     */
    private static final String INPUTSTRING = "Hello from Java";

    /**
     * Constructor used to pass data to superclass constructor.
     * 
     * @param prog - the program reference we will
     * be manipulating in this example.
     */
    private LinkProg3(Program prog)
    {
        super(prog);
    }

    /**
     * Main entry point to a CICS OSGi program.
     * This can be called via a LINK or a 3270 attach.
     * 
     * The fully qualified name of this class should be added to the CICS-MainClass 
     * entry in the parent OSGi bundle's manifest.
     */
    public static void main(String[] args)
    {
        // Final message to emit to the terminal
        String msg;

        // Get details about our current CICS task
        Task task = Task.getTask();
        task.out.println(" - Starting LinkProg3");
        
        // Create a reference to the Program we will invoke        
        Program prog = new Program();

        // Specify the properties on the program    
        prog.setName(PROG_NAME);
        
        // Don't syncpoint between remote links, this is the default 
        // Setting true ensures each linked program runs in its own UOW and
        // allows the a remote server program to use a syncpoint command
        prog.setSyncOnReturn(false); 

        // Create a new instance of the class        
        LinkProg3 lp = new LinkProg3(prog);

        // Build up the Channel and containers
        Channel chan = lp.buildChannel();

        // link to CICS program
        lp.linkProg(chan);        

        try {
            // Get output data from output container
            String resultStr = null;
            int cicsrc;

            // Read CHAR container from channel container data as formatted string
            // CICS returns this in a UTF16 format and JCICS reads this into a String
            // Container object will be null if container not present
            Container charContainer = chan.getContainer(DATE_CONTAINER);
            if (charContainer != null) {
                try {
					resultStr = charContainer.getString();
				} catch (CicsConditionException e) {
					task.abend("LPGC");
				}
            }
            else {
                // Missing response container
                resultStr = "<missing>";
            }

            // Get CICS return code from binary container and convert to 32-bit int/fullword
            // CICS returns this unconverted so wrapper in a byte buffer and convert to an int    
            Container bitContainer = chan.getContainer(CICSRC_CONTAINER);
            if (bitContainer != null) {
                
                // Obtain the RC as an int
                byte[] ba = null;
				try {
					ba = bitContainer.get();
				} catch (CicsConditionException e) {
					task.abend("LPGC");
				}
                ByteBuffer bb = ByteBuffer.wrap(ba);                     
                cicsrc = bb.getInt(); 

                // Format the final message
                msg = MessageFormat.format("Returned from link to {0} with rc({1}) date {2}",
                        prog.getName(), cicsrc, resultStr);
            }
            else {
                // Missing response container
                msg = MessageFormat.format("Returned from link to {0} with no CICS RC", prog.getName());
            }
        }
        catch (CicsConditionException exc) {
            
            // Log message and continue
            msg = MessageFormat.format("ERROR from link to {0} with message({1}) ",
                    prog.getName(), exc.getMessage());
        }

        // Return output to caller and exit        
        task.out.println(msg);
    }
    

    /**
     * LINK to the CICS program.
     * 
     * @param chan - the channel to send to the target CICS program.
     */ 
    private void linkProg(Channel chan) {

        try {
            // Perform the LINK operation, passing the channel
            this.prog.link(chan);
        }
        catch (CicsConditionException cce) {
            System.out.println("ERROR from LinkServerEduchan " + cce.getMessage());
        }
    }

    /**
     * Build channel and populate with simple char container with string data.
     * 
     * @return a Channel object containing the data to send to the target program.
     */ 
    private Channel buildChannel() {

        // Obtain a reference to the current task
        Task task = Task.getTask();
         
        try {
            // Create channel
            Channel testChannel = task.createChannel(CHANNEL);

            // Create a CHAR container populated with a simple String
            // CHAR containers will be created in UTF16 when created iin JCICS
            Container inputContainer = testChannel.createContainer(INPUT_CONTAINER);
            inputContainer.putString(INPUTSTRING);
            
            // Return the constructed channel to the caller
            return testChannel;
        }
        catch (ChannelErrorException | ContainerErrorException | InvalidRequestException |
                CCSIDErrorException | CodePageErrorException exc) {
            // Rethrow as a RuntimeException to avoid cluttering this example with error-handling logic
            throw new RuntimeException(exc);
        }
    }
}
