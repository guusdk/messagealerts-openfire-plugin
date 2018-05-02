[![Build Status](https://travis-ci.org/guusdk/messagealerts-openfire-plugin.svg?branch=master)](https://travis-ci.org/guusdk/messagealerts-openfire-plugin)

The MessageAlerts Plugin is a plugin for the [Openfire XMPP server](https://www.igniterealtime.org/openfire), which periodically reads from a database table, and sends alerts to JIDs found in that table.

Building
--------

This project is using the Maven-based Openfire build process, as introduced in Openfire 4.2.0. To build this plugin locally, ensure that the following are available on your local host:

* A Java Development Kit, version 7 or (preferably) 8
* Apache Maven 3

To build this project, invoke on a command shell:

    $ mvn clean package

Upon completion, the openfire plugin will be available in `target/messagealerts.jar`

Installation
------------
Copy `messagealerts.jar` into the plugins directory of your Openfire server, or use the Openfire Admin Console to upload the plugin. The plugin will then be automatically deployed.

To upgrade to a new version, copy the new `messagealerts.jar` file over the existing file.

Configuration
------------
To configure this plugin, the following properties are used:

* `messagealerts.from` - The that is the sender of the messages. Defaults to the server address.</li>
* `messagealerts.frequency` - The amount of seconds between each iteration. Defaults to 60.</li>

