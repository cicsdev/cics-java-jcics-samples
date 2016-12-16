package com.ibm.cicsdev.vsam.ksds;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import com.ibm.cics.server.CicsConditionException;
import com.ibm.cics.server.DuplicateRecordException;
import com.ibm.cics.server.EndOfFileException;
import com.ibm.cics.server.InvalidRequestException;
import com.ibm.cics.server.KSDS;
import com.ibm.cics.server.KeyHolder;
import com.ibm.cics.server.KeyedFileBrowse;
import com.ibm.cics.server.RecordHolder;
import com.ibm.cics.server.RecordNotFoundException;
import com.ibm.cics.server.SearchType;
import com.ibm.cics.server.Task;
import com.ibm.cicsdev.bean.StockPart;
import com.ibm.cicsdev.vsam.StockPartHelper;
import com.ibm.cicsdev.vsam.VsamExampleCommon;


public class KsdsExampleCommon extends VsamExampleCommon
{
    /**
     * Name of the file resource to use.
     */
    private static final String FILE_NAME = "XMPLKSDS";

    /**
     * A field to hold a reference to the VSAM KSDS file this
     * instance will access. 
     */
    protected final KSDS ksds;
    
    /**
     * Constructor to initialise the sample class.
     */    
    public KsdsExampleCommon()
    {
        this.ksds = new KSDS();
        this.ksds.setName(FILE_NAME);        
    }

    /**
     * Provides a simple example of adding a record to a VSAM KSDS file.
     */
    public void addRecord(StockPart sp)
    {
        try {
            // Get the flat byte structure from the JZOS object
            byte[] record = sp.getByteBuffer();
            
            // Get a byte array containing the key for this record
            byte[] key = StockPartHelper.getKey(sp);
            
            // Write the record into the file at the specified key
            this.ksds.write(key, record);
        }
        catch (DuplicateRecordException dre) {
            
            // Collision on the generated key
            String strMsg = "Tried to insert duplicate key {0}"; 
            Task.getTask().out.println( MessageFormat.format(strMsg, sp.getPartId()) );
            throw new RuntimeException(dre);
        }
        catch (InvalidRequestException ire) {
            
            // Invalid request may occur for several reasons - find out the root cause
            // See the CICS API documentation for WRITE to see the full list
            if ( ire.getRESP2() == 20 ) {
                // File not addable
                String strMsg = "Add operations not permitted for file {0}";
                Task.getTask().out.println( MessageFormat.format(strMsg, this.ksds.getName()) );
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
     * Provides a simple example of updating a single record in a VSAM KSDS file.
     * 
     * This method uses the supplied part ID to locate and lock a record using
     * the readForUpdate() method, generates a new description for the part, and
     * then writes the new record back to the file using the rewrite() method.
     * 
     * @param key
     */
    public StockPart updateRecord(int key, String strDescription)
    {
        try {            
            // Use the StockPartHelper class to get a byte[] from this key 
            byte[] keyBytes = StockPartHelper.getKey(key);

            // Holder object to receive the data
            RecordHolder rh = new RecordHolder();

            // Read the record at the specified key and lock
            this.ksds.readForUpdate(keyBytes, SearchType.EQUAL, rh);

            // Create a StockPart instance from the record
            byte[] readBytes = rh.getValue();
            StockPart sp = new StockPart(readBytes);

            // Update the description
            sp.setDescription(strDescription);
            
            // Rewrite the record with the updated data
            this.ksds.rewrite( sp.getByteBuffer() );

            // Return the updated StockPart instance
            return sp;
        }
        catch (RecordNotFoundException rnfe) {
            // Initial read failed - key not found in file
            String strMsg = "Record with key {0} not found in file";
            Task.getTask().out.println( MessageFormat.format(strMsg, key) );            
            throw new RuntimeException(rnfe);
        }
        catch (InvalidRequestException ire) {
            
            // Invalid request may occur for several reasons - find out the root cause
            // See the CICS API documentation for READ UPDATE to see the full list
            if ( ire.getRESP2() == 20 ) {
                // File not readable or updateable
                String strMsg = "Read or update operations not permitted for file {0}";
                Task.getTask().out.println( MessageFormat.format(strMsg, this.ksds.getName()) );
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
     * Provides a simple example of reading a single record from a VSAM KSDS file.
     * 
     * @param key the key of the record to locate in the VSAM file.
     * 
     * @return a {@link StockPart} instance representing the record in the VSAM file
     * identified by the specified key.
     */
    public StockPart readRecord(int key)
    {        
        // Use the StockPartHelper class to get a byte[] from this key 
        byte[] keyBytes = StockPartHelper.getKey(key);

        try {            
            // Holder object to receive the data
            RecordHolder rh = new RecordHolder();

            // Read the record identified by the supplied key 
            this.ksds.read(keyBytes, SearchType.EQUAL, rh);

            // Create a StockPart instance from the record
            return new StockPart( rh.getValue() );
        }
        catch (InvalidRequestException ire) {
            
            // Invalid request may occur for several reasons - find out the root cause
            // See the CICS API documentation for READ to see the full list
            if ( ire.getRESP2() == 20 ) {
                // File not readable or updateable
                String strMsg = "Read operation not permitted for file {0}";
                Task.getTask().out.println( MessageFormat.format(strMsg, this.ksds.getName()) );
            }
            
            // Throw an exception to rollback the current UoW
            throw new RuntimeException(ire);
        }
        catch (RecordNotFoundException rnfe) {
            // The supplied record was not found
            String strMsg = "Record with key {0} was not found";
            Task.getTask().out.println( MessageFormat.format(strMsg, key) );
            throw new RuntimeException(rnfe);
        }
        catch (CicsConditionException cce) {
            // Some other CICS failure
            throw new RuntimeException(cce);
        }
    }
    
    public StockPart deleteRecord(int key)
    {
        // The record as it stood before deletion
        StockPart sp;
        
        /*
         * Lock the record for an update.
         */
        
        try {            
            // Use the StockPartHelper class to get a byte[] from this key 
            byte[] keyBytes = StockPartHelper.getKey(key);

            // Holder object to receive the data
            RecordHolder rh = new RecordHolder();

            // Read the record at the specified key and lock
            this.ksds.readForUpdate(keyBytes, SearchType.EQUAL, rh);
            
            // Convert to a StockPart object
            sp = new StockPart( rh.getValue() );
        }
        catch (RecordNotFoundException rnfe) {
            // Initial read failed - key not found in file
            String strMsg = "Record with key {0} not found in file";
            Task.getTask().out.println( MessageFormat.format(strMsg, key) );            
            throw new RuntimeException(rnfe);
        }
        catch (InvalidRequestException ire) {
            
            // Invalid request may occur for several reasons - find out the root cause
            // See the CICS API documentation for READ UPDATE to see the full list
            if ( ire.getRESP2() == 20 ) {
                // File not readable or updateable
                String strMsg = "Read or update operations not permitted for file {0}";
                Task.getTask().out.println( MessageFormat.format(strMsg, this.ksds.getName()) );
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
            this.ksds.delete();
            
            // Return the record as it stood before deletion
            return sp;
        }
        catch (InvalidRequestException ire) {
            
            // Invalid request may occur for several reasons - find out the root cause
            // See the CICS API documentation for DELETE to see the full list
            if ( ire.getRESP2() == 20 ) {
                // File not readable or updateable
                String strMsg = "Delete operations not permitted for file {0}";
                Task.getTask().out.println( MessageFormat.format(strMsg, this.ksds.getName()) );
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
     * Provides an example of browsing a VSAM dataset.
     * 
     * @param startKey
     * @param count
     * 
     * @return a list of StockPart objects
     */
    public List<StockPart> browse(int startKey, int count)
    {
        // The list instance to return
        List<StockPart> list = new ArrayList<>(count);
        
        // Holder object to receive the data
        RecordHolder rh = new RecordHolder();
        KeyHolder kh = new KeyHolder();
        
        // Start a browse of the file at the supplied key
        byte[] key = StockPartHelper.getKey(startKey);
        
        try {            
            // Start the browse of the file
            KeyedFileBrowse kfb = this.ksds.startBrowse(key, SearchType.GTEQ);
            
            // Loop until we reach maximum count
            for ( int i = 0; i < count; i++ ) {
                
                // Read a record from the file
                kfb.next(rh, kh);
                
                // Get the record and convert to a StockPart
                StockPart sp = new StockPart( rh.getValue() );
                
                // Add this object to our return array
                list.add(sp);
            }
        }
        catch (RecordNotFoundException rnfe) {
            // Initial browse failed - no records matching GTEQ condition
        }
        catch (EndOfFileException eof) {
            // Normal termination of loop - no further records
        }
        catch (InvalidRequestException ire) {
            
            // Invalid request may occur for several reasons - find out the root cause
            // See the CICS API documentation for STARTBR to see the full list
            if ( ire.getRESP2() == 20 ) {
                // File not readable or updateable
                String strMsg = "Browse operations not permitted for file {0}";
                Task.getTask().out.println( MessageFormat.format(strMsg, this.ksds.getName()) );
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
