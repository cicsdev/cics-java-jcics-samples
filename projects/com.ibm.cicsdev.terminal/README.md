com.ibm.cicsdev.terminal
===

Provides some sample Java code to show how to parse arguments when a Java program is used as an
initial program on a transaction, and the transaction is started from a terminal.

The code first checks we are associated with a terminal, and then performs `receive()` call
to obtain the data supplied at the terminal.

This data is then broken down using the `java.util.StringTokenizer` class to split the
supplied arguments on word boundaries.  

* `TerminalExample1` - a simple class to demonstrate receiving arguments from a terminal in a JCICS environment.

## Supporting files

* [`/etc/Terminal`](../../etc/Terminal) - contains the output of a DFHCSDUP EXTRACT operation needed to define the required programs and transactions.


## Running the Example

At a 3270 terminal screen, enter the transaction you wish to run, followed by some optional arguments. 

    JTM1 Hello from CICS!

and the following output will be returned (assuming upper-casing is enabled for the terminal): 

    JTM1 Hello from CICS! - Starting TerminalExample1
    Arg 0 : JTM1                                     
    Arg 1 : HELLO                                    
    Arg 2 : FROM                                     
    Arg 3 : CICS!                                    
    Completed TerminalExample1

