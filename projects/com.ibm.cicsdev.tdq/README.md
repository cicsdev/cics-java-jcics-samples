com.ibm.cicsdev.tdq
===

This project contains various sample programs to demonstrate use of the JCICS API for manipulating a transient
data queue (TDQ). A TDQ is a queue of data items that is stored in sequential queues. Transient queues are
resources whose name may contain up to 4 characters and must be defined before use, unlike temporary storage
queues that can be dynamically defined. 

* **Intrapartition** queues are accessible only by CICS transactions within the CICS region
* **Extrapartition** queues are sequential data sets on tape or disk.
* Intrapartition and extrapartition queues can be referenced through **indirect** destinations to provide
flexibility in program maintenance. Queue definitions can be changed without having to recompile existing
programs.

All of the examples in this repository use an intrapartition queue named `MYQ1`.

* `TDQExample1` - a simple class that writes Java strings to a TDQ and then reads them back.
* `TDQExample2` - an alternative implementation of `TDQExample1` that writes and reads Java strings, but performing all of the codepage conversion manually. This avoids the pre-requisite of CICS TS V5.1, but is slightly more work to code in Java.
* `TDQExample3` - a more complex example that writes structured records to a TDQ and then reads them back.
* `TDQCommon` - Superclass used to provide common services for the TDQ samples. 

For the sake of brevity, all of the examples lack comprehensive error-handling logic.
To demonstrate exactly where an exception may be thrown when using the JCICS API, one `try { ... } catch { ... }`
block has been included per API call that may thrown an exception.


## Supporting files

* `lib/TdqRecord.jar` - a pre-built JAR containing the JZOS generated record that maps the copybook structure used in `TDQExample3` (includes source). 
* [`/etc/TDQ`](../../etc/TDQ) - contains the output of a DFHCSDUP EXTRACT operation needed to define the required programs and transactions.


## Structured record

The class `TdqRecord` is produced by the JZOS record generator utility from a COBOL copybook. This class
is used in example 3 to write a structured record to the TDQ. The `TdqRecord` class is used to
demonstrate how a Java application would interact with non-Java implementations when communicating using a TDQ.

The COBOL structure that is mapped by the `TdqRecord` class is as follows:

        01 TDQ-RECORD.                           
           03 RECORD-ID        PIC 9(8)   COMP.  
           03 BINARY-DIGIT     PIC 9(4)   COMP.  
           03 CHARACTER-STRING PIC X(30).        
           03 NUMERIC-VALUE    PIC 9(18).        
           03 PACKED-DEC       PIC 9(15)  COMP-3.
           03 SIGNED-PACKED    PIC S9(12) COMP-3.


## Configuration

Note you must define an intrapartition transient data queue named `MYQ1` before using these samples.
The simplest way to define this TDQ is using the `CECI` transaction:

    CECI CREATE TDQUEUE(MYQ1) ATTRIBUTES('TYPE(INTRA)')

Press send twice - firstly to confirm the command, and secondly to receive the message:

    STATUS:  COMMAND EXECUTION COMPLETE


## Running the Example

At a 3270 terminal screen, enter the transaction you wish to run, for example JTD1 will run TDQ example 1. 

    JTD1

and the following output will be returned 

    JTD1 - Starting TDQExample1                       
    Read data from queue "TDQ write from JCICS item 1"
    Read data from queue "TDQ write from JCICS item 2"
    Read data from queue "TDQ write from JCICS item 3"
    Read data from queue "TDQ write from JCICS item 4"
    Read data from queue "TDQ write from JCICS item 5"
    Completed TDQExample1                                 

