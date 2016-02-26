contactlab-hybris-connect
=========================

The hybris-contactlab connector covers main functionalities of email marketing that might be of interest when implementing a new omni-channel commerce solution.

The hybris-ContactLab connector covers the following cases:
- Subscriber management across hybris & ContactLab
- Unsubscribing
- Transactional emails management
- Abandoned cart automated marketing email delivery


Installation / Usage
--------------------

Installation will require a working installation of the hybris commerce suite. Copy the connector code into the bin/custom directory and add the following lines to ${HYBRIS_HOME_DIR}\hybris\config\localextensions.xml
```
<extension name="contactlab" />
<extension name="contactlabwebservices" />
<extension name="contactlabaddon" />
```

Go to ${HYBRIS_HOME_DIR}\hybris\bin\platform and set up Apache Ant by entering the following command:

- For MS Windows operating systems: setantenv.bat 
- For Unix operating systems: . ./setantenv.sh

Execute the following command:
```
ant addoninstall -Daddonnames="contactlabaddon" -DaddonStorefront.yacceleratorstorefront="yacceleratorstorefront"
```

Perform the build procedure by entering the command
```
ant clean all
```

Start the platform by entering hybrisserver.sh (or hybrisserver.sh for Windows)

Perform a system initialization

Perform a catalog synchronization on hmc.

