# cics-java-jcics-samples

Sample CICS Java programs demonstrating how to use the JCICS API in an OSGi JVM server environment.

## Sample projects overview

* [`cics-java-jcics-link-app`](cics-java-jcics-link-app) - Java project covering LINK commands using both COMMAREAs and channels and containers
* [`cics-java-jcics-link-bundle`](cics-java-jcics-link-bundle) - CICS bundle plug-in project for cics-java-jcics-link-app
* [`cics-java-jcics-serialize-app`](cics-java-jcics-serialize-app) - Java project covering serializing access to a common resource using the CICS ENQ and DEQ commands.
* [`cics-java-jcics-serialize-bundle`](cics-java-jcics-serialize-bundle) - CICS bundle plug-in project for cics-java-jcics-serialize-app


## Repository structure

* [`gradle/`](gradle) - Gradle wrapper 
* [`.mvn/`](.mvn/wrapper) - Maven wrapper 
* [`local-repo/`](/local-repo) - Maven repository for COMMAREA wrapper JAR generated from EDUPGM COBOL copybook.
* [`etc/`](etc) - Supporting materials used to define CICS and z/OS resources needed for the samples.
* [`etc/eclipse_projects/`](etc/eclipse_projects) - Eclipse CICS Bundle projects for importing into an Eclipse environment.
* [`etc/src/`](etc/src) - Supporting source code for COBOL programs.
* [`blog/`](blog/blog.md) - Archive of developer works JCICS tutorial.

## Pre-requisites

* IBM CICS TS V5.5 or later
* IBM Semeru Runtime Certified Edition Version 17.0 or later on the workstation
* Either Gradle or Apache Maven on the workstation (optional if using Wrappers)
* An Eclipse development environment on the workstation (optional)


## Downloading

-Download the sample as a [ZIP](https://github.com/cicsdev/cics-java-jcics-samples/archive/refs/heads/cicsts/v5.5.zip) and unzip onto the workstation **or**
- Clone the repository using your IDEs support, such as the Eclipse Git plugin


## Building
The sample includes an Eclipse project configuration, a Gradle build, a Maven POM, and Gradle/Maven Wrappers offering a wide range of build options with the tooling and IDE of your choice.

Choose from the following approach:
* Use the command line to drive Gradle or Apache Maven (if installed on your workstation)
* Use the command line or IDE support for Wrappers, to drive the supplied Gradle or Apache Maven Wrappers (with no requirement for Gradle, Maven, Eclipse, or CICS Explorer SDK to be installed)
* Use the built-in Eclipse and CICS Explorer SDK capability
* Use Eclipse with Buildship (Gradle), or m2e (Maven) to drive Gradle, or Maven.


### Option 1 - Building with Gradle

The sample comes pre-configured with a Gradle wrapper and build files to facilitate automated builds.

The CICS JVM server name should be modified in the  `cics.jvmserver` property in the gradle build file or alternatively can be set on the command line (see below).

If you have the Gradle buildship plug-in available, use the right-click **Run As...** menu on the cics-java-osgi-link project to configure and run the `clean` and `build` tasks. Otherwise choose from the command-line approaches.

**Gradle Wrapper (Linux/Mac):**
```shell
./gradlew clean build
```

**Gradle Wrapper (Windows):**
```shell
gradle.bat clean build
```

**Gradle (command-line & setting jvmserver):**
```shell
gradle clean build -Pcics.jvmserver=MYJVM
```

A JAR file for each CICS bundle project is created inside the application project `build/libs/` sub-directory and a CICS bundle ZIP file inside the CICS bundle project `build/distributions` directory.


### Option 2 - Building with Apache Maven

The sample comes pre-configured with a Maven wrapper and build files to facilitate automated builds.

The CICS JVM server name should be modified in the `<cics.jvmserver>` property in the build files to match the required CICS JVMSERVER resource name, or alternatively can be set on the command line (see below).


**Maven Wrapper (Linux/Mac):**
```shell
./mvnw clean verify
```

**Maven Wrapper (Windows):**
```shell
mvnw.cmd clean verify
```

**Maven (command-line & setting jvmserver):**
```shell
mvn clean verify -Dcics.jvmserver=MYJVM
```

A JAR file for each CICS bundle project is created inside the application project  `target` sub-directory and a CICS bundle ZIP file inside the CICS bundle project `target` directory.


### Option 3 - Building with Eclipse

The sample comes pre-configured for use with Java 17 and the CICS TS V5.5 Target Platform. When you initially import the project to your IDE, if your IDE is not configured for a Java 17, or does not have CICS Explorer SDK installed with the correct 'target platform' set, you might experience local project compile errors. 

To resolve issues:
* ensure you have the CICS Explorer SDK plug-in installed
* configure the Project's build-path, and Application Project settings to use your preferred JDK and Java compiler settings
* set the CICS TS Target Platform to your intended CICS target (Hint: Window | Preferences | Plug-in Development | Target Platform | Add | Template | Other...) 

---


## Configuration

The sample Java classes are designed to be added to an OSGi bundle and deployed into a CICS OSGi JVM server, but can also be used as the basis for extending Web applications deployed into a Liberty JVM server. 



### Deploying to zFS


#### Option 1 - Deploying using command line tools
1. Upload the built CICS bundle ZIP file from your *target* or */build/distributions* directory to zFS on the host system (e.g. FTP).
2. Connect to USS on the host system (e.g. SSH).
3. Create the bundle directory for the project.
4. Move the CICS bundle ZIP file into the bundle directory.
5. Extract the CICS bundle ZIP file. This can be done using the `jar` command. For example:
   ```shell
   jar -xvf bundle.zip
   ```

#### Option 2 - Deploying using CICS Explorer (Remote System Explorer) and CICS Bundle ZIP
1. Connect to USS on the host system
2. Create the bundle directory for the project.
3. Copy & paste the built CICS bundle ZIP file from your *target* or *build/distributions* directory to zFS on the host system into the bundle directory.
4. Extract the ZIP by right-clicking on the ZIP file > User Action > unjar...
5. Refresh the bundle directory
  
#### Option 3 - Deploying using CICS Explorer SDK and the provided CICS bundle project
1. Deploy the CICS bundle project from CICS Explorer using the **Export Bundle Project to z/OS UNIX File System** wizard. This CICS bundle includes the osgi bundlepart, and optionally the transaction and bundle resources to run the sample.



### Deploying to CICS

1. Define an OSGi JVM server resource called `DFHJVMS` based on the CICS-supplied sample definition in the CSD group `DFH$OSGI`.

1. CICS resource definitions for the bundle, programs, transactions are supplied as a DFHCSDUP sample input for each sub-project in the `etc` directory, for instance [`etc/Link/DFHCSD.txt`](etc/Link/DFHCSD.txt). Alternatively they can be installed using the bundle parts supplied with the Eclipse CICS bundle projects in the [`eclipse_projects`](etc/eclipse_projects) directory. 
See the individual project directories for any additional supporting resources required.

1. Install the groups containing the JVMSERVER, TRANSACTION and PROGRAM resourceS and ensure the JVM server becomes enabled along with all associated bundles.

 
## License

This project is licensed under [Apache License Version 2.0](LICENSE).  

## Reference


* For further details on using the IBM Record Generator for Java see this [tutorial](https://developer.ibm.com/tutorials/build-java-records-from-cobol-with-ibm-record-generator/)
* For details on how to define a CICS OSGi JVM server refer to the Knowledge Center topic [Configuring an OSGi JVM server](https://www.ibm.com/docs/en/cics-ts/5.5.0?topic=server-configuring-osgi-jvm)
* Developer works tutorial archive [Getting to Grips with JCICS](blog/blog.md)
