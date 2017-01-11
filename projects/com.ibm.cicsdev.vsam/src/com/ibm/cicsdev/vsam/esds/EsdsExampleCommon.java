/* Licensed Materials - Property of IBM                                   */
/*                                                                        */
/* SAMPLE                                                                 */
/*                                                                        */
/* (c) Copyright IBM Corp. 2017 All Rights Reserved                       */
/*                                                                        */
/* US Government Users Restricted Rights - Use, duplication or disclosure */
/* restricted by GSA ADP Schedule Contract with IBM Corp                  */
/*                                                                        */

package com.ibm.cicsdev.vsam.esds;

import java.util.ArrayList;
import java.util.List;

import com.ibm.cics.server.CicsConditionException;
import com.ibm.cics.server.DuplicateRecordException;
import com.ibm.cics.server.ESDS;
import com.ibm.cics.server.ESDS_Browse;
import com.ibm.cics.server.EndOfFileException;
import com.ibm.cics.server.InvalidRequestException;
import com.ibm.cics.server.RecordHolder;
import com.ibm.cics.server.RecordNotFoundException;
import com.ibm.cics.server.Task;
import com.ibm.cicsdev.bean.StockPart;
import com.ibm.cicsdev.vsam.VsamExampleCommon;

/**
 * Example class to demonstrate various operations which may be
 * useful when manipulating VSAM ESDS files.
 * 
 * Records in an ESDS file are located using their Relative Byte
 * Address (RBA).
 */
public class EsdsExampleCommon extends VsamExampleCommon
{
    /**
     * Name of the file resource to use.
     */
    private static final String FILE_NAME = "XMPLESDS";

    /**
     * A field to hold a reference to the VSAM ESDS file this
     * instance will access. 
     */
    private final ESDS esds;
    
    /**
     * Constructor to initialise the reference to the sample file.
     */    
    public EsdsExampleCommon()
    {
        // Create a new ESDS instance and initialise
        this.esds = new ESDS();
        this.esds.setName(FILE_NAME);        
    }

    /**
     * Provides a simple example of adding a record to a VSAM ESDS file.
     * 
     * @param sp the {@link StockPart} instance to write to the file.
     * 
     * @return the RBA for the new record.
     */
    public long addRecord(StockPart sp)
    {
        // Get the flat byte structure from the JZOS object
        byte[] record = sp.getByteBuffer();
        
        try {
            // Write the record into the file
            long rba = this.esds.write(record);
            
            // Return the new RBA for this record
            return rba;
        }
        catch (DuplicateRecordException dre) {
            
            // Collision on the generated key
            String strMsg = "Tried to insert duplicate record 0x%016X"; 
            Task.getTask().out.println( String.format(strMsg, sp.getPartId()) );
            throw new RuntimeException(dre);
        }
        catch (InvalidRequestException ire) {
            
            // Invalid request may occur for several reasons - find out the root cause
            // See the CICS API documentation for WRITE to see the full list
            if ( ire.getRESP2() == 20 ) {
                // File not addable
                String strMsg = "Add operations not permitted for file %s";
                Task.getTask().out.println( String.format(strMsg, this.esds.getName()) );
            }
            
            // Throw an exception to rollback the current UoW
            throw new RuntimeException(ire);
        }
        catch (CicsConditionException cce) {
            // Crude error handling - propagate an exception back to caller
            throw new RuntimeException(cce);
        }
    }

    /**
     * Provides a simple example of updating a single record in a VSAM ESDS file.
     * 
     * This method uses the supplied RBA to locate and lock a record using
     * the readForUpdate() method, uses the supplied description to update the
     * record that has been read, and then writes the new record back to the file
     * using the rewrite() method.
     * 
     * @param rba the RBA of the record to update.
     * @param strDescription the new description to store in the VSAM file.
     * 
     * @return the updated {@link StockPart} instance.
     */
    public StockPart updateRecord(long rba, String strDescription)
    {
        try {            
            // Holder object to receive the data
            RecordHolder rh = new RecordHolder();

            // Read the record at the specified RBA and lock
            this.esds.readForUpdate(rba, rh);

            // Create a StockPart instance from the record
            byte[] readBytes = rh.getValue();
            StockPart sp = new StockPart(readBytes);

            // Update the description
            sp.setDescription(strDescription);
            
            // Rewrite the record with the updated data
            this.esds.rewrite( sp.getByteBuffer() );

            // Return the updated StockPart instance
            return sp;
        }
        catch (RecordNotFoundException rnfe) {
            // Initial read failed - key not found in file
            String strMsg = "Record with RBA 0x%016X not found in file";
            Task.getTask().out.println( String.format(strMsg, rba) );            
            throw new RuntimeException(rnfe);
        }
        catch (InvalidRequestException ire) {
            
            // Invalid request may occur for several reasons - find out the root cause
            // See the CICS API documentation for READ UPDATE to see the full list
            if ( ire.getRESP2() == 20 ) {
                // File not readable or updateable
                String strMsg = "Read or update operations not permitted for file %s";
                Task.getTask().out.println( String.format(strMsg, this.esds.getName()) );
            }
            
            // Throw an exception to rollback the current UoW
            throw new RuntimeException(ire);
        }
        catch (CicsConditionException cce) {
            // Some other CICS failure
            throw new RuntimeException(cce);
        }
    }
    
    /**
     * Provides a simple example of reading a single record from a VSAM ESDS file.
     * 
     * @param rba the RBA of the record to locate in the VSAM file.
     * 
     * @return a {@link StockPart} instance representing the record in the VSAM file
     * identified by the specified key.
     */
    public StockPart readRecord(long rba)
    {        
        try {            
            // Holder object to receive the data
            RecordHolder rh = new RecordHolder();

            // Read the record identified by the supplied RBA 
            this.esds.read(rba, rh);

            // Create a StockPart instance from the record
            return new StockPart( rh.getValue() );
        }
        catch (InvalidRequestException ire) {
            
            // Invalid request may occur for several reasons - find out the root cause
            // See the CICS API documentation for READ to see the full list
            if ( ire.getRESP2() == 20 ) {
                // File not readable or updateable
                String strMsg = "Read operation not permitted for file %s";
                Task.getTask().out.println( String.format(strMsg, this.esds.getName()) );
            }
            
            // Throw an exception to rollback the current UoW
            throw new RuntimeException(ire);
        }
        catch (RecordNotFoundException rnfe) {
            // The requested record was not found
            String strMsg = "Record with RBA 0x%016X was not found";
            Task.getTask().out.println( String.format(strMsg, rba) );
            throw new RuntimeException(rnfe);
        }
        catch (CicsConditionException cce) {
            // Some other CICS failure
            throw new RuntimeException(cce);
        }
    }

    /**
     * Provides an example of browsing a VSAM ESDS dataset.
     * 
     * @param rbaStart the RBA from which the browse should begin.
     * @param count the maximum number of records to return.
     * 
     * @return a list of StockPart objects.
     */
    public List<StockPart> browse(long rbaStart, int count)
    {
        // The list instance to return
        List<StockPart> list = new ArrayList<>(count);
        
        // Holder object to receive the data
        RecordHolder rh = new RecordHolder();
        
        try {            
            // Start the browse of the file
            ESDS_Browse esdsBrowse = this.esds.startBrowse(rbaStart);
            
            // Loop until we reach maximum count
            for ( int i = 0; i < count; i++ ) {
                
                // Read a record from the file (discard the returned RBA)
                esdsBrowse.next(rh);
                
                // Get the record and convert to a StockPart
                StockPart sp = new StockPart( rh.getValue() );
                
                // Add this object to our return array
                list.add(sp);
            }
        }
        catch (RecordNotFoundException rnfe) {
            // Initial browse failed - no records matching the supplied RBA
        }
        catch (EndOfFileException eof) {
            // Normal termination of loop - no further records
        }
        catch (InvalidRequestException ire) {
            
            // Invalid request may occur for several reasons - find out the root cause
            // See the CICS API documentation for STARTBR to see the full list
            if ( ire.getRESP2() == 20 ) {
                // File not readable or updateable
                String strMsg = "Browse operations not permitted for file %s";
                Task.getTask().out.println( String.format(strMsg, this.esds.getName()) );
            }
            
            // Throw an exception to rollback the current UoW
            throw new RuntimeException(ire);
        }
        catch (CicsConditionException cce) {
            // Some other CICS failure
            throw new RuntimeException(cce);
        }
        
        // Return the list
        return list;
    }
}
