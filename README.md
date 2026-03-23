# cics-java-jcics-samples

Sample CICS Java programs demonstrating how to use the JCICS API in an OSGi JVM server environment.

## Sample projects overview

* [`cics-java-jcics-link-app`](cics-java-jcics-link-app) - Java project covering LINK commands using both COMMAREAs and channels and containers
* [`cics-java-jcics-link-bundle`](cics-java-jcics-link-bundle) - CICS bundle plug-in project for cics-java-jcics-link-app
* [`cics-java-jcics-serialize-app`](cics-java-jcics-serialize-app) - Java project covering serializing access to a common resource using the CICS ENQ and DEQ commands.
* [`cics-java-jcics-serialize-bundle`](cics-java-jcics-serialize-bundle) - CICS bundle plug-in project for cics-java-jcics-serialize-app
* [`cics-java-jcics-tdq-app`](cics-java-jcics-tdq-app) - Java project covering access to transient data queues using the READQ and WRITEQ commands.
* [`cics-java-jcics-tdq-bundle`](cics-java-jcics-tdq-bundle) - CICS bundle plug-in project for cics-java-jcics-tdq-app


## Repository structure


* [`etc/`](etc) - Supporting materials including DFHCSDUP definition files.
* [`etc/cics_bundle_projects/`](etc/cics_bundle_projects) - Eclipse CICS Bundle projects for importing into an Eclipse environment.
* [`etc/src/`](etc/src) - Supporting source code for COBOL programs.
* [`local-repo/`](/local-repo) - Maven repository for COMMAREA wrapper JAR generated from EDUPGM COBOL copybook.
* [`gradle/`](gradle) - Gradle wrapper 
* [`.mvn/`](.mvn/wrapper) - Maven wrapper 
* [`blog/`](https://github.com/cicsdev/cics-java-jcics-samples/tree/main/blog/blog.md) - Archive of developer works JCICS tutorial.

## Pre-requisites

* IBM CICS TS V5.5 or later
* IBM Semeru Runtime Certified Edition Version 17.0 or later on the workstation
* IBM Semeru Runtime Certified Edition Version 17.0 or later on z/OS
* Either Gradle or Apache Maven on the workstation (optional if using Wrappers)
* An Eclipse development environment on the workstation (optional)


## Downloading

- Download the sample as a [ZIP](https://github.com/cicsdev/cics-java-jcics-samples/archive/refs/heads/cicsts/v5.5.zip) and unzip onto the workstation **or** clone the repository using a git client.



## Building
The sample includes an Eclipse project configuration, a Gradle build, a Maven POM, and Gradle/Maven Wrappers offering a wide range of build options with the tooling and IDE of your choice.

Choose from the following approach:

1. Use the command line to drive the supplied Gradle or Apache Maven Wrappers, this means there is no requirement for Gradle, Maven, Eclipse, or CICS Explorer SDK to be installed.
2. Use the built-in Eclipse and CICS Explorer SDK capability
3. Use Eclipse with Buildship (Gradle), or m2e (Maven) to drive Gradle, or Maven.


### Option 1a - Building with Gradle

The sample comes pre-configured with a Gradle wrapper and build files to facilitate automated builds.

The CICS JVM server name should be modified in the  `cics.jvmserver` property in the gradle build file or alternatively can be set on the command line (see below).

If you have the Gradle buildship plug-in available, use the right-click **Run As...** menu on the cics-java-osgi-link project to configure and run the `clean` and `build` tasks. Otherwise choose from the command-line approaches.

**Gradle Wrapper (Linux/Mac):**
```shell
./gradlew clean build
```

**Gradle Wrapper (Windows):**
```shell
gradlew.bat clean build
```

**Gradle Wrapper setting jvmserver:**
```shell
gradlew clean build -Pcics.jvmserver=MYJVM
```

A JAR file for each CICS bundle project is created inside the application project `build/libs/` sub-directory and a CICS bundle ZIP file inside the CICS bundle project `build/distributions` directory.


### Option 1b - Building with Apache Maven

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

**Maven Wrapper setting jvmserver:**
```shell
mvnw clean verify -Dcics.jvmserver=MYJVM
```

A JAR file for each CICS bundle project is created inside the application project  `target` sub-directory and a CICS bundle ZIP file inside the CICS bundle project `target` directory.


### Option 2 - Building with Eclipse and the CICS Explorer SDK for Java

To import the sample into Eclipse either
1. Clone the repository using your IDEs support, such as the Eclipse Git plugin,**or**
2. Use the **File > Import > Existing Projects into Workspace** wizard and select the expanded zip archive directory as the root directory. 
Ensure you check "Search for nested projects", and do not select **Copy projects into workspace**

The sample comes pre-configured for use with Java 17 and the CICS TS V5.5 Target Platform. When you initially import the project to your IDE, if your IDE is not configured for Java 17, or does not have CICS Explorer SDK installed with the correct *Target Platform* set, you might experience local project compile errors. 

To resolve issues:
* Ensure you have the latest CICS Explorer SDK plug-in installed
* Set the CICS TS Target Platform to your intended CICS target (Hint: **Window > Preferences > Plug-in Development > Target Platform > Add > Template > Other...**) 
* Configure the Project's build-path, and Application Project settings to use your preferred JDK and Java compiler settings

### Option 3 - Use Eclipse with Buildship (Gradle), or m2e (Maven) to drive Gradle, or Maven

1. Import the projects into Eclipse as detailed in Option 2. 
2. Use the Eclipse support for Maven or Gradle to build the project using the supplied build files.

---


## Configuration

The sample Java classes are designed to be added to an OSGi bundle and deployed into a CICS OSGi JVM server, but can also be used as the basis for extending Web applications deployed into a Liberty JVM server. 



### Deploying to zFS


#### Option 1 - Deploying CICS Bundle Maven/Gradle plugin builds
1. Upload the built CICS bundle ZIP file from your *target* or */build/distributions* directory to zFS in binary.
2. Connect to USS on the host system (e.g. SSH).
3. Create the bundle directory in zFS for the project
4. Copy the CICS bundle ZIP file into the bundle directory.
5. Extract the CICS bundle ZIP file. This can be done using the `jar` command. For example:
   ```shell
   jar -xvf bundle.zip
   ```

#### Option 2 - Deploying using CICS Explorer SDK and the CICS bundle projects
1. Deploy each CICS bundle project from CICS Explorer to zFS using the **Export Bundle Project to z/OS UNIX File System** wizard. This CICS bundle includes the osgi bundlepart, and optionally the transaction and bundle resources to run the sample.


#### Option 3 - Deploying using Eclipse and z/OS Explorer 
1. Connect to the host system using the Remote Systems view in z/OS Explorer 
2. Create the bundle directory in zFS
3. Copy & paste the built CICS bundle ZIP file from your *target* or *build/distributions* directory in the local workstation to the bundle directory on zFS. 
4. Extract the ZIP by right-clicking on the ZIP file > User Action > unjar...



### Deploying to CICS

1. Define an OSGi JVM server resource called `DFHJVMS` based on the CICS-supplied sample definition in the CSD group `DFH$OSGI`.

1. CICS resource definitions for the bundle, programs, transactions are supplied as a DFHCSDUP sample input for each sub-project in the `etc` directory, for instance [`etc/Link/DFHCSD.txt`](etc/Link/DFHCSD.txt). Alternatively they can be installed using the bundle parts supplied with the Eclipse CICS bundle projects in the [`cics_bundle_projects`](etc/cics_bundle_projects) directory. 
See the individual project directories for any additional supporting resources required.

1. Install the groups containing the JVMSERVER, TRANSACTION and PROGRAM resources and ensure the JVM server becomes enabled along with all associated bundles.

 
## License

This project is licensed under [Apache License Version 2.0](LICENSE).  

## Reference


* For further details on using the IBM Record Generator for Java see this [tutorial](https://developer.ibm.com/tutorials/build-java-records-from-cobol-with-ibm-record-generator/)
* For details on how to define a CICS OSGi JVM server refer to the Knowledge Center topic [Configuring an OSGi JVM server](https://www.ibm.com/docs/en/cics-ts/5.5.0?topic=server-configuring-osgi-jvm)
* Developer works tutorial archive [Getting to Grips with JCICS](blog/blog.md)
