cics-java-jcics-samples
================
Sample CICS Java programs demonstrating how to use the JCICS API in an OSGi JVM server environment

## Samples overview

* [`com.ibm.cicsdev.link`](projects/com.ibm.cicsdev.link) - Performing CICS LINK operations using both commareas, and channels and containers.
* `com.ibm.cicsdev.serialize` - Serializing access to a common resource using the CICS ENQ and DEQ mechanism.
* `com.ibm.cicsdev.tdq` - Accessing transient data queues.
* `com.ibm.cicsdev.terminal` - Reading in data from a terminal for an OSGi application.
* `com.ibm.cicsdev.tsq` - Accessing temporary storage queues.
* `com.ibm.cicsdev.vsam` - Accessing KSDS, ESDS, and RRDS VSAM files.

## Repository structure

* `etc/` - Supporting materials used to define CICS and z/OS resources needed for the samples.
* `projects/` - Complete Eclipse projects suitable for importing into a CICS Explorer environment.
* `src/` - Supporting source code for non-Java programs.

## Eclipse projects
Java source code for the following classes is provided in the corresponding Eclipse plugin projects in the projects directory  

### Project com.ibm.cicsdev.link

* LinkProg1 - A class that demonstrates linking to the COBOL application EC01 using a COMMAREA
* LinkProg2 - A class that demonstrates linking to the COBOL application EDUPGM using a COMMAREA mapped using a JZOS generated record
* LinkProg3 - A class that demonstrates linking to COBOL application EDUCHAN using channels and containers  
* LinkProgCommon - Super class used to provide common service for the LinkProg samples  
* LinkServerEduchan - A Java version of the EDUCHAN COBOL program used as the back-end to LinkProg3     

### Project com.ibm.cicsdev.serialize

* SerializeExample1 - A simple class to demonstrate using the CICS ENQ and DEQ mechanism from a JCICS environment.

### Project com.ibm.cicsdev.tdq

* TDQCommon - Super class used to provide common services for the TDQ samples 
* TDQExample1 - A simple class that writes Java strings to a TDQ and then reads them back.
* TDQExample2 - An alternative implementation of TDQExample1 that writes and reads Java strings, but
performing all of the codepage conversion manually. This avoids the pre-req of CICS TS V5.1, but is slightly more work to code in Java.
* TDQExample3 - A more complex example that writes structured records to a TDQ and then reads them back.

### Project com.ibm.cicsdev.terminal

* TerminalExample1 - A simple class to demonstrate receiving arguments from a terminal in a JCICS environment.


### Project com.ibm.cicsdev.tsq

* TSQExample1 - A simple class that writes Java strings to a TSQ and then reads them back.
* TSQExample2 - An alternative implementation of TSQExample1 that writes and reads Java strings, but performs all of the byte array to string conversion manually. This avoids the pre-req of CICS TS V5.1, but is slightly more
work to code in Java.
* TSQExample3 - A more complex example that uses a JZOS generated record to write a structured record to a TSQ and then read it back.
* TSQExample4 - An extension of the TSQExample3 sample program, which populates a TSQ, updates items within the queue, then reads them back to confirm.
* TSQCommon - Super class used to provide common services for the TSQ samples 

### Project com.ibm.cicsdev.vsam

Provides examples for using VSAM files from a JCICS environment. All examples have a ESDS, KSDS, and RRDS variant. The numbered example classes are driving classes, with the ExampleCommon classes performing the JCICS calls.

* Example1 - Writes a single record to a file.
* Example2 - Reads a single record from a file.
* Example3 - Updates a single record in a file.
* Example4 - Deletes a single record in a file (not ESDS).
* Example5 - Browses a VSAM file.
* ExampleCommon - Various routines that perform the various JCICS calls.

## CICS bundle projects
The following Eclipse CICS bundle projects are provided to support deployment of the Java projects and the associated CICS resource definitions

* Project com.ibm.cicsdev.link.cicsbundle - CICS bundle project for deploying the com.ibm.cicsdev.link Java samples
* Project com.ibm.cicsdev.link.resources.cicsbundle - CICS bundle project containing transaction and program definitions for the com.ibm.cicsdev.link sample
* Project com.ibm.cicsdev.serialize.cicsbundle - CICS bundle project for deploying the com.ibm.cicsdev.serialize samples
* Project com.ibm.cicsdev.serialize.resources.cicsbundle - CICS bundle project containing transaction and program definitions for the com.ibm.cicsdev.serialize sample
* Project com.ibm.cicsdev.tdq.cicsbundle - CICS bundle project for deploying the com.ibm.cicsdev.tdq samples
* Project com.ibm.cicsdev.tdq.resources.cicsbundle - CICS bundle project containing transaction and program definitions for the com.ibm.cicsdev.tdq sample
* Project com.ibm.cicsdev.terminal.cicsbundle - CICS bundle project for deploying the com.ibm.cicsdev.terminal sample
* Project com.ibm.cicsdev.terminal.resources.cicsbundle - CICS bundle project containing transaction and program definitions for the com.ibm.cicsdev.terminal sample
* Project com.ibm.cicsdev.tsq.cicsbundle - CICS bundle project for deploying the com.ibm.cicsdev.tsq samples
* Project com.ibm.cicsdev.tsq.resources.cicsbundle - CICS bundle project containing transaction and program definitions for the com.ibm.cicsdev.tsq sample
* Project com.ibm.cicsdev.vsam.cicsbundle - CICS bundle project for deploying the com.ibm.cicsdev.vsam samples
* Project com.ibm.cicsdev.vsam.resources.cicsbundle - CICS bundle project containing transaction and program definitions for the com.ibm.cicsdev.vsam sample


## Supporting files
* etc/Link/DFHCSD.txt - Output from a DFHCSDUP EXTRACT for the Link sample.
* etc/TDQ/DFHCSD.txt - Output from a DFHCSDUP EXTRACT for the TSQ sample.
* etc/Terminal/DFHCSD.txt - Output from a DFHCSDUP EXTRACT for the terminal sample.
* etc/TSQ/DFHCSD.txt - Output from a DFHCSDUP EXTRACT for the TDQ sample.
* etc/VSAM/DFHCSD.txt - Output from a DFHCSDUP EXTRACT for the VSAM sample.
* src/Cobol/EC01.cbl - Sample CICS COBOL application that returns the date and time in a COMMAREA.
* src/Cobol/EDUCHAN.cbl - A sample CICS COBOL that returns the date and time and reversed input using channels and containers
* EDUPGM.jar - A pre-built JAR containing the JZOS generated record that maps the copybook structure used in the EDUPGM COBOL sample. The generated class JZOSCommareaWrapperclass is used in the Link example 2 to map the fields in the COMMAREA used by the COBOL program EDUPGM.
* TdqRecord.jar - A pre-built JAR containing the JZOS generated record that maps the copybook structure used in the TDQExample3.
* TsqRecord.jar - A pre-built JAR containing the JZOS generated record that maps the copybook structure used in the TSQExample3.
* vsam.jar - A pre-built JAR containing the JZOS generated record that maps the copybook structure used in all the VSAM samples.


## Pre-reqs

* CICS TS V5.1 or later, due to the usage of the getString() methods.
* Java SE 1.7 or later on the z/OS system
* Java SE 1.7 or later on the workstation
* Eclipse with CICS Explorer SDK installed

    

## Configuration

The sample Java classes are designed to be added to an OSGi bundle and deployed into a CICS OSGi JVM server, but can also be used as the basis for extending Web applications deployed into a Liberty JVM server. 

### Adding the resources to Eclipse

1. Using an Eclipse development environment import the project from the project folder using the menu File -> Import -> Existing Projects into Workspace
1. Define and set a CICS Target Platform for the workspace using the menu Windows -> Preferences -> Target Platform 


### Starting a JVM server in CICS

1. Enable Java support in the CICS region by adding the SDFJAUTH library to the STEPLIB concatenation and setting USSHOME and the JVMPROFILE SIT parameters.
1. Define an OSGi JVM server called DFHJVMS using the CICS supplied sample definition in the CSD group DFH$OSGI
1. Copy the CICS supplied DFHOSGI.jvmprofile zFS file to the zFS directory specified above and ensure the JAVA_HOME variable is set correctly.
1. Install the DFHJVMS resource defined in step 2 and ensure it becomes enabled.

### To deploy the samples into a CICS region 
1. Using the CICS Explorer export the Java projects com.ibm.cicsdev.link.cicsbundle, com.ibm.cicsdev.tsq.cicsbundle and com.ibm.cicsdev.tdq.cicsbundle to a zFS directory. The sample definitions use the following style of zFS location /u/cics1/com.ibm.cicsdev.link.cicsbundle_1.0.0
1. Define and install CICS BUNDLE resource defintions referring to the deployed bundle directory in step 1. and ensure all resources are enabled. 
1. Create the require transaction and program definitions using either the supplied DFHCSD.txt files as input to a CSD define job or using the CICS bundle projects 
com.ibm.cicsdev.link.resources.cicsbundle, com.ibm.cicsdev.tsq.resources.cicsbundle, com.ibm.cicsdev.tdq.resources.cicsbuncdle
1. If using the TDQ sample define an intra-partition TDQ called MYQ1
1. If using the LINK sample compile and deploy the COBOL programs EC01 and EDUCHAN, and either define PROGRAM definitions or enable program autoinstall.  
1. Optionally add a CICS Java program definiton for LinkServerEduchan called EDUCHAN if you wish to replace the EDUCHAN COBOL sample with the Java implemenation. 
  

## License
This project is licensed under [Apache License Version 2.0](LICENSE).  


## Reference

* For further details on using the IBM JZOS record generator see this [developer center article]  (https://developer.ibm.com/cics/2016/05/12/java-cics-using-ibmjzos/)
* For details on how to define a CICS OSGi JVM server refer to the Knowledge Center topic [Configuring an OSGi JVM server] (http://www.ibm.com/support/knowledgecenter/SSGMCP_5.3.0/com.ibm.cics.ts.java.doc/JVMserver/config_jvmserver_app.html)

