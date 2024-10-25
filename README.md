# cics-java-jcics-samples

Sample CICS Java programs demonstrating how to use the JCICS API in an OSGi JVM server environment.

## Samples overview

* [`com.ibm.cicsdev.link`](projects/com.ibm.cicsdev.link) - Performing CICS LINK operations using both commareas, and channels and containers.
* [`com.ibm.cicsdev.serialize`](projects/com.ibm.cicsdev.serialize) - Serializing access to a common resource using the CICS ENQ and DEQ mechanism.
* [`com.ibm.cicsdev.tdq`](projects/com.ibm.cicsdev.tdq) - Accessing transient data queues.
* [`com.ibm.cicsdev.terminal`](projects/com.ibm.cicsdev.terminal) - Reading in data from a terminal for an OSGi application.
* [`com.ibm.cicsdev.tsq`](projects/com.ibm.cicsdev.tsq) - Accessing temporary storage queues.
* [`com.ibm.cicsdev.vsam`](projects/com.ibm.cicsdev.vsam) - Accessing KSDS, ESDS, and RRDS VSAM files.

## Repository structure

* [`etc/`](etc) - Supporting materials used to define CICS and z/OS resources needed for the samples.
* [`projects/`](projects) - Complete Eclipse projects suitable for importing into a CICS Explorer environment.
* [`src/`](src) - Supporting source code for non-Java programs.

## Pre-requisites

* Java SE 1.8 or later on the workstation
* Eclipse with the IBM CICS SDK for Java or any IDE that supports usage of the Maven Central artifact [com.ibm.cics:com.ibm.cics.server.](https://search.maven.org/artifact/com.ibm.cics/com.ibm.cics.server) 
 
## Configuration

The sample Java classes are designed to be added to an OSGi bundle and deployed into a CICS OSGi JVM server, but can also be used as the basis for extending Web applications deployed into a Liberty JVM server. 

### Starting a JVM server in CICS

1. Enable Java support in the CICS region by adding the `SDFJAUTH` library to the `STEPLIB` concatenation and setting `USSHOME` and the `JVMPROFILE` SIT parameters.
1. Define an OSGi JVM server called `DFHJVMS` using the CICS-supplied sample definition in the CSD group `DFH$OSGI`.
1. Copy the CICS supplied `DFHOSGI.jvmprofile` zFS file to the zFS directory specified by the `JVMPROFILE` SIT parameter, and ensure the `JAVA_HOME` variable is set correctly.
1. Install the `DFHJVMS` resource defined in step 2 and ensure it becomes enabled.


### Import the resources into your IDE

1. Ensure you have an Eclipse-based IDE with the CICS Explorer SDK installed.
1. Import the relevant projects into your Eclipse environment as described in [Adding the resources to Eclipse](/projects#adding-the-resources-to-eclipse).
1. Follow the instructions in the [Generated resources](/projects#generated-resources) section to add any required binary libraries to the build path.


### To deploy the samples into a CICS region 

1. Using the CICS Explorer export the `com.ibm.cicsdev.*.cicsbundle` projects to a zFS directory. The sample definitions use the following style of zFS location `/u/cics1/com.ibm.cicsdev.link.cicsbundle_1.0.0`.
1. Define and install CICS `BUNDLE` resource defintions referring to the deployed bundle directory in step 1, and ensure all resources are enabled. 
1. Create the required transaction and program definitions using either the supplied `DFHCSD.txt` files as input to a CSD define job, or using the supplied CICS bundle projects.
1. See the individual project directories for any additional supporting resources required.
 
## License

This project is licensed under [Apache License Version 2.0](LICENSE).  

## Reference

* CICSDev tutorial archive [Getting to Grips with JCICS](blog/blog.md)
* For further details on using the IBM Record Generator for Java see this [tutorial](https://developer.ibm.com/tutorials/build-java-records-from-cobol-with-ibm-record-generator/)
* For details on how to define a CICS OSGi JVM server refer to the Knowledge Center topic [Configuring an OSGi JVM server](http://www.ibm.com/support/knowledgecenter/SSGMCP_5.3.0/com.ibm.cics.ts.java.doc/JVMserver/config_jvmserver_app.html)
