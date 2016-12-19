package com.ibm.cicsdev.vsam.rrds;

import java.util.ArrayList;
import java.util.List;

import com.ibm.cics.server.CicsConditionException;
import com.ibm.cics.server.DuplicateRecordException;
import com.ibm.cics.server.EndOfFileException;
import com.ibm.cics.server.InvalidRequestException;
import com.ibm.cics.server.RRDS;
import com.ibm.cics.server.RRDS_Browse;
import com.ibm.cics.server.RecordHolder;
import com.ibm.cics.server.RecordNotFoundException;
import com.ibm.cics.server.Task;
import com.ibm.cicsdev.bean.StockPart;
import com.ibm.cicsdev.vsam.VsamExampleCommon;

/**
 * Example class to demonstrate various operations which may be
 * useful when manipulating VSAM RRDS files.
 * 
 * In a VSAM RRDS, records are addressed using their Relative Record
 * Number (RRN).
 */
public class RrdsExampleCommon extends VsamExampleCommon
{
    /**
     * Name of the file resource to use.
     */
    private static final String FILE_NAME = "XMPLRRDS";

    /**
     * A field to hold a reference to the VSAM RRDS file this
     * instance will access. 
     */
    private final RRDS rrds;
    
    /**
     * Constructor to initialise the reference to the sample file.
     */    
    public RrdsExampleCommon()
    {
        // Create a new RRDS instance and initialise
        this.rrds = new RRDS();
        this.rrds.setName(FILE_NAME);        
    }
    
    /**
     * Provides a simple example of adding a record to a VSAM RRDS file.
     * 
     * @param rrn the RRN of the record to write to the file. 
     * @param sp the {@link StockPart} instance to write to the file.
     */
    public void addRecord(long rrn, StockPart sp)
    {
        // Get the flat byte structure from the JZOS object
        byte[] record = sp.getByteBuffer();
        
        try {
            // Write the record into the file at the specified RRN
            this.rrds.write(rrn, record);
        }
        catch (DuplicateRecordException dre) {
            
            // Collision on the record number
            String strMsg = "Tried to insert duplicate record at RRN 0x%016X"; 
            Task.getTask().out.println( String.format(strMsg, rrn) );
            throw new RuntimeException(dre);
        }
        catch (InvalidRequestException ire) {
            
            // Invalid request may occur for several reasons - find out the root cause
            // See the CICS API documentation for WRITE to see the full list
            if ( ire.getRESP2() == 20 ) {
                // File not addable
                String strMsg = "Add operations not permitted for file %s";
                Task.getTask().out.println( String.format(strMsg, this.rrds.getName()) );
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
     * Provides a simple example of updating a single record in a VSAM RRDS file.
     * 
     * This method uses the supplied RRN to locate and lock a record using
     * the readForUpdate() method, uses the supplied description to update the
     * record that has been read, and then writes the new record back to the file
     * using the rewrite() method.
     * 
     * @param rrn the RRN of the record to update.
     * @param strDescription the new description to store in the VSAM file.
     * 
     * @return the updated {@link StockPart} instance.
     */
    public StockPart updateRecord(long rrn, String strDescription)
    {
        try {            
            // Holder object to receive the data
            RecordHolder rh = new RecordHolder();

            // Read the record at the specified RRN and lock
            this.rrds.readForUpdate(rrn, rh);

            // Create a StockPart instance from the record
            byte[] readBytes = rh.getValue();
            StockPart sp = new StockPart(readBytes);

            // Update the description
            sp.setDescription(strDescription);
            
            // Rewrite the record with the updated data
            this.rrds.rewrite( sp.getByteBuffer() );

            // Return the updated StockPart instance
            return sp;
        }
        catch (RecordNotFoundException rnfe) {
            // Initial read failed - key not found in file
            String strMsg = "Record with RRN 0x%016X not found in file";
            Task.getTask().out.println( String.format(strMsg, rrn) );            
            throw new RuntimeException(rnfe);
        }
        catch (InvalidRequestException ire) {
            
            // Invalid request may occur for several reasons - find out the root cause
            // See the CICS API documentation for READ UPDATE to see the full list
            if ( ire.getRESP2() == 20 ) {
                // File not readable or updateable
                String strMsg = "Read or update operations not permitted for file %s";
                Task.getTask().out.println( String.format(strMsg, this.rrds.getName()) );
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
     * Provides a simple example of reading a single record from a VSAM RRDS file.
     * 
     * @param rrn the RRN of the record to locate in the VSAM file.
     * 
     * @return a {@link StockPart} instance representing the record in the VSAM file
     * identified by the specified key.
     */
    public StockPart readRecord(long rrn)
    {        
        try {            
            // Holder object to receive the data
            RecordHolder rh = new RecordHolder();

            // Read the record identified by the supplied RRN 
            this.rrds.read(rrn, rh);

            // Create a StockPart instance from the record
            return new StockPart( rh.getValue() );
        }
        catch (InvalidRequestException ire) {
            
            // Invalid request may occur for several reasons - find out the root cause
            // See the CICS API documentation for READ to see the full list
            if ( ire.getRESP2() == 20 ) {
                // File not readable or updateable
                String strMsg = "Read operation not permitted for file %s";
                Task.getTask().out.println( String.format(strMsg, this.rrds.getName()) );
            }
            
            // Throw an exception to rollback the current UoW
            throw new RuntimeException(ire);
        }
        catch (RecordNotFoundException rnfe) {
            // The requested record was not found
            String strMsg = "Record with RRN 0x%016X was not found";
            Task.getTask().out.println( String.format(strMsg, rrn) );
            throw new RuntimeException(rnfe);
        }
        catch (CicsConditionException cce) {
            // Some other CICS failure
            throw new RuntimeException(cce);
        }
    }

    /**
     * Provides an example of browsing a VSAM RRDS dataset.
     * 
     * @param rrnStart the RRN from which the browse should begin.
     * @param count the maximum number of records to return.
     * 
     * @return a list of StockPart objects.
     */
    public List<StockPart> browse(long rrnStart, int count)
    {
        // The list instance to return
        List<StockPart> list = new ArrayList<>(count);
        
        // Holder object to receive the data
        RecordHolder rh = new RecordHolder();
        
        try {            
            // Start the browse of the file
            RRDS_Browse rrdsBrowse = this.rrds.startBrowse(rrnStart);
            
            // Loop until we reach maximum count
            for ( int i = 0; i < count; i++ ) {
                
                // Read a record from the file (discard the returned RRN)
                rrdsBrowse.next(rh);
                
                // Get the record and convert to a StockPart
                StockPart sp = new StockPart( rh.getValue() );
                
                // Add this object to our return array
                list.add(sp);
            }
        }
        catch (RecordNotFoundException rnfe) {
            // No records matching the supplied RRN
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
                Task.getTask().out.println( String.format(strMsg, this.rrds.getName()) );
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
    
    /**
     * Provides a simple example of deleting a single record from a VSAM RRDS file.
     * 
     * @param rrn the RRN of the record to locate in the VSAM file.
     * 
     * @return a {@link StockPart} instance representing the record in the VSAM file
     * identified by the specified key before it was deleted.
     */
    public StockPart deleteRecord(long rrn)
    {
        // The record as it stood before deletion
        StockPart sp;
        
        /*
         * Lock the record for an update.
         */

        try {
            // Holder object to receive the data
            RecordHolder rh = new RecordHolder();

            // Read the record at the specified RRN and lock
            this.rrds.readForUpdate(rrn, rh);
            
            // Convert to a StockPart object
            sp = new StockPart( rh.getValue() );
        }
        catch (RecordNotFoundException rnfe) {
            // Initial read failed - RRN not found in file
            String strMsg = "Record with RRN 0x%016X not found in file";
            Task.getTask().out.println( String.format(strMsg, rrn) );            
            throw new RuntimeException(rnfe);
        }
        catch (InvalidRequestException ire) {
            
            // Invalid request may occur for several reasons - find out the root cause
            // See the CICS API documentation for READ UPDATE to see the full list
            if ( ire.getRESP2() == 20 ) {
                // File not readable or updateable
                String strMsg = "Read or update operations not permitted for file %s";
                Task.getTask().out.println( String.format(strMsg, this.rrds.getName()) );
            }
            
            // Throw an exception to rollback the current UoW
            throw new RuntimeException(ire);
        }
        catch (CicsConditionException cce) {
            // Some other CICS failure
            throw new RuntimeException(cce);
        }

        
        /*
         * Now perform the delete.
         */
        
        try {            
            // Delete the selected record
            this.rrds.delete();
            
            // Return the record as it stood before deletion
            return sp;
        }
        catch (InvalidRequestException ire) {
            
            // Invalid request may occur for several reasons - find out the root cause
            // See the CICS API documentation for DELETE to see the full list
            if ( ire.getRESP2() == 20 ) {
                // File not readable or updateable
                String strMsg = "Delete operations not permitted for file %s";
                Task.getTask().out.println( String.format(strMsg, this.rrds.getName()) );
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
     * Loops through all the records in the file, deleting them as we go.
     * Not expected to be very useful in a real-world scenario, but makes
     * coding an RRDS example much easier, as the file will be in a known
     * state before we begin the real update logic. 
     */
    public void emptyFile()
    {
        // Holder object to receive the data
        RecordHolder rh = new RecordHolder();
        
        try {
            // Read each record, deleting as we go
            for ( long rrn = 1; rrn < Integer.MAX_VALUE; rrn++ ) {
                
                // Lock this record and delete
                this.rrds.readForUpdate(rrn, rh);
                this.rrds.delete();
            }
        }
        catch (RecordNotFoundException rnfe) {
            // Normal exit from loop - record ID not found
        }
        catch (InvalidRequestException ire) {
            
            // Invalid request may occur for several reasons - find out the root cause
            // See the CICS API documentation for READ UPDATE to see the full list
            if ( ire.getRESP2() == 20 ) {
                // File not readable or updateable
                String strMsg = "Read, update, or delete operations not permitted for file %s";
                Task.getTask().out.println( String.format(strMsg, this.rrds.getName()) );
            }
            
            // Throw an exception to rollback the current UoW
            throw new RuntimeException(ire);
        }        
        catch (CicsConditionException cce) {
            // Crude error handling - propagate an exception back to caller
            throw new RuntimeException(cce);
        }
    }
}
