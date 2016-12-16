package com.ibm.cicsdev.vsam.helper;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.concurrent.ThreadLocalRandom;

import com.ibm.cicsdev.bean.StockPart;

/**
 * Generates a random record for testing purposes.
 * 
 * This class serves no purpose in the application interface to CICS. It is merely provided
 * to generate random data, without any requirements to pre-load datasets with sample data.
 */
public class StockPartHelper
{
    /**
     * Constant used to represent the milliseconds time value as of Jan 1st 2010 00:00:00.000.
     */
    private static final long JANUARY_1ST_2010 = 1262304000000l;
    
    /**
     * Constant used to represent the number of milliseconds between Jan 1st 2010 and Jan 1st 2020.
     */
    private static final long TEN_YEARS = 315532800000l;
    
    /**
     * Constant used to represent the number of milliseconds in eight weeks.
     */
    private static final long EIGHT_WEEKS = 4838400000l;
    
    /**
     * A selection of possible sizes for the description. Maximum length should be 11 characters.
     */
    public static final String[] SIZE =
        { "Tiny", "Small", "Medium", "Large", "Extra large", "Huge" };
    
    /**
     * A selection of possible colours for the description. Maximum length should be 6 characters.
     */
    public static final String[] COLOUR =
        { "blue", "red", "orange", "green", "yellow", "white", "black", "purple" };
    
    /**
     * A selection of possible shapes for the description. Maximum length should be 6 characters.
     */
    public static final String[] SHAPE =
        { "round", "square", "oval", "flat" };
    
    /**
     * A selection of possible materials for the description. Maximum length should be 7 characters.
     */
    public static final String[] MATERIAL =
        { "plastic", "metal", "card" };
    
    /**
     * A selection of possible object types for the description. Maximum length should be 6 characters.
     */
    public static final String[] NOUN =
        { "widget", "thing", "plug", "nut", "bit", "part", "panel" };            
    
    
    /**
     * Generates a random key.
     * 
     * @return a byte array representing a random key value
     */
    public static byte[] generateKey() {
       
        // Source of random numbers
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        
        // Create a new StockPart instance
        StockPart sp = new StockPart();
        
        // Generate a random part number as an integer (range is 0 <= n < 100,000,000)
        sp.setPartId(rand.nextInt(100_000_000));

        // Convert to a byte array using helper method
        return getKey(sp);
    }

    
    public static byte[] getKeyZero() {
        
        // Create a new StockPart instance
        StockPart sp = new StockPart();
        
        // Key zero is for stock part with ID of zero
        sp.setPartId(0);

        // Convert to a byte array using helper method
        return getKey(sp);
    }
    
    /**
     * Helper method.
     * 
     * @param sp
     * 
     * @return a byte array which can be used as a key to identify the
     * supplied StockPart record.
     */
    public static byte[] getKey(StockPart sp) {
        
        // Get the flat byte structure from the StockPart instance
        byte[] record = sp.getByteBuffer();
        
        // Get a byte array containing the record key
        byte[] key = new byte[8];
        System.arraycopy(record, 0, key, 0, key.length);

        // Return the key as a byte array
        return key;
    }
    
    public static byte[] getKey(int key)
    {
        // Create a new StockPart instance and delegate
        StockPart sp = new StockPart();
        sp.setPartId(key);
        return getKey(sp);
    }
    
    /**
     * Generate a StockPart object which contains random, but valid data.
     * 
     * @return A newly-created StockPart object.
     */
    public static StockPart generate() {

        // Source of random numbers
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        
        // New record to create
        StockPart sp = new StockPart();

        // Generate a random part number as an integer (range is 1 <= n < 100,000,000)
        sp.setPartId(rand.nextInt(1, 100_000_00));
        
        // Generate a random supplier number as an integer (range is 1 <= n < 100,000,000)
        sp.setSupplier(rand.nextInt(1, 100_000_000));

        // Last order date is some time in the ten years after January 1st 2010
        long lLastOrderDate = JANUARY_1ST_2010 + rand.nextLong(TEN_YEARS);
        Calendar cLastOrderDate = Calendar.getInstance();
        cLastOrderDate.setTimeInMillis(lLastOrderDate);
        
        // Set each of the fields
        sp.setLastOrderDateYy(String.format("%1$ty", cLastOrderDate));
        sp.setLastOrderDateMm(String.format("%1$tm", cLastOrderDate));
        sp.setLastOrderDateDd(String.format("%1$td", cLastOrderDate));
        
        // Next order date is eight weeks after last order date
        long lNextOrderDate = lLastOrderDate + EIGHT_WEEKS;
        Calendar cNextOrderDate = Calendar.getInstance();
        cNextOrderDate.setTimeInMillis(lNextOrderDate);

        // Set each of the fields
        sp.setNextOrderDateYy(String.format("%1$ty", cNextOrderDate));
        sp.setNextOrderDateMm(String.format("%1$tm", cNextOrderDate));
        sp.setNextOrderDateDd(String.format("%1$td", cNextOrderDate));

        // Generate a random description
        sp.setDescription(generateDescription());
        
        // Generate a random stock quantity (range of 0 <= n < 10,000)
        int iStock = rand.nextInt(0, 10_000);
        sp.setStockQuantity(iStock);
        
        // Generate a random price (range is 100 <= n < 10,000)
        double dPrice = rand.nextInt(100, 10_000) / 100.0d;
        sp.setUnitPrice(new BigDecimal(dPrice));
        
        // Return to the caller
        return sp;
    }
    
    /**
     * Generates a description for a random part using the arrays of constants
     * {@link #SIZE}, {@link #COLOUR}, {@link #SHAPE}, {@link #MATERIAL}, and
     * {@link #NOUN}. The description will be a string of maximum length 40 characters.
     * 
     * @return a human-readable description of an object
     */
    public static String generateDescription() {
        
        // Source of random numbers
        ThreadLocalRandom r = ThreadLocalRandom.current();
        
        // Buffer to hold the generated description - max possible length of 40 characters
        StringBuilder sb = new StringBuilder(40);
                
        // Generate a random sequence of adjectives and finish with a noun
        sb.append(SIZE[r.nextInt(SIZE.length)]);
        sb.append(' ');
        sb.append(COLOUR[r.nextInt(COLOUR.length)]);
        sb.append(' ');
        sb.append(SHAPE[r.nextInt(SHAPE.length)]);
        sb.append(' ');
        sb.append(MATERIAL[r.nextInt(MATERIAL.length)]);
        sb.append(' ');
        sb.append(NOUN[r.nextInt(NOUN.length)]);
        
        // Return the created description
        return sb.toString();
    }
}
