/* Licensed Materials - Property of IBM                                   */
/*                                                                        */
/* SAMPLE                                                                 */
/*                                                                        */
/* (c) Copyright IBM Corp. 2017 All Rights Reserved                       */
/*                                                                        */
/* US Government Users Restricted Rights - Use, duplication or disclosure */
/* restricted by GSA ADP Schedule Contract with IBM Corp                  */
/*                                                                        */

package com.ibm.cicsdev.terminal;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.ibm.cics.server.CicsConditionException;
import com.ibm.cics.server.DataHolder;
import com.ibm.cics.server.EndOfChainIndicatorException;
import com.ibm.cics.server.Task;
import com.ibm.cics.server.TerminalPrincipalFacility;

/**
 * Provides an example of receiving arguments to a transaction at a 3270 terminal.
 */
public class TerminalExample1
{
    /**
     * Main entry point to a CICS OSGi program.
     */
    public static void main(String[] args) throws CicsConditionException
    {
        // Get details about our current CICS task
        Task task = Task.getTask();

        // Retrieve the raw string from the terminal
        // This will need to be issued before we write anything out
        String strTerm = getTerminalString();

        // Now we can report status to the terminal
        task.out.println(" - Starting TerminalExample1");

        // Do we have terminal arguments?
        if (strTerm != null) {

            // Parse to individual elements
            String[] termArgs = parseTerminalString(strTerm);

            // Display arguments
            for (int i = 0; i < termArgs.length; i++) {
                task.out.println( String.format("Arg %d : %s", i, termArgs[i]) );
            }
        }
        
        // Completion message
        task.out.println("Completed TerminalExample1");        
    }

    /**
     * Verifies the current task is associated with a terminal and then
     * receives the input data. The received data is converted to a Java
     * string and returned.
     * 
     * @return a String containing the data input at the terminal. If the
     * transaction's principal facility is not a terminal, then <code>null</code>
     * will be returned.
     */
    private static String getTerminalString() throws CicsConditionException
    {
        // Get the current task's principal facility
        Object pf = Task.getTask().getPrincipalFacility();
        
        // Are we of a suitable type?
        if ( pf instanceof TerminalPrincipalFacility ) {

            // Cast to correct type
            TerminalPrincipalFacility tpf = (TerminalPrincipalFacility) pf;
            
            // Create a holder object to store the data
            DataHolder holder = new DataHolder();
            
            try {
                // Perform the receive from the terminal
                tpf.receive(holder);               
            }
            catch (EndOfChainIndicatorException e) {
                // Normal operation - ignore this one
            }
            catch (CicsConditionException cce) {
                // Propagate all other problems
                throw cce;
            }
            
            // Convert the received data into a valid String
            // Assume this is a valid character string in the CICS local CCSID 
            // Requires CICS TS V5.1 and later (JCICS 1.401.0)            
            return holder.getStringValue();
        }
        else {
            // Not a terminal principal facility
            return null;
        }
    }

    /**
     * Breaks down the input string into 
     * @param strTerm
     * @return
     */
    private static String[] parseTerminalString(String strTerm)
    {
        // A place to store the output collection
        List<String> args = new ArrayList<>();
        
        // Tokenize the input string using standard whitespace characters
        StringTokenizer tok = new StringTokenizer(strTerm);
        
        // Add each of the tokens to the output collection
        while ( tok.hasMoreTokens() ) {
            args.add(tok.nextToken());
        }
        
        // Convert the collection to an array
        return args.toArray( new String[args.size()] );
    }
}
