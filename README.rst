JyConsole: Eclipse JDT Exposed via Jython
=========================================

Features
--------

- Eclipse + jython = win


Installation
------------

- Import as an existing project in a workspace
- Open plugin.xml, which should open the Plugin Plug-in Manifest Editor
- In the Plugin Plug-in Manifest Editor, select the Overview tab
- Bottom right, select the Export Wizard, point to a destination directory, and Finish
- This creates a plugin jar. Copy / move this jar to your Eclipse's installation, "dropins" directory
- Restart Eclipse

Usage
-----

- On a jython / python file that's written to use Eclipse's JDT, right click, Open With, JyConsole Editor
- Right click on the script in the JyConsole Editor, Run Script
