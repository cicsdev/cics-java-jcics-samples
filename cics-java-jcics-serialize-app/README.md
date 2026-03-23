com.ibm.cicsdev.serialize
===

Provides an example for serializing access to a common resource using the CICS ENQ and DEQ mechanism.

* `SerializeExample1` - a simple class to demonstrate using the CICS ENQ and DEQ mechanism from a JCICS environment.


## Supporting files

* [`/etc/Serialize`](../../etc/Serialize) - contains the output of a DFHCSDUP EXTRACT operation needed to define the required programs and transactions.
    

## Running the Example

At a 3270 terminal screen, enter the transaction you wish to run, for example JSY1 will run example 1. 

    JSY1

and the following output will be returned 

    JSY1 - Starting SerializeExample1                  
    Attempting to acquire resource lock MYAPP.SYNC.RES1
    Resource lock has been acquired                    
    Resource lock has been released                    
    Completed SerializeExample1                        

