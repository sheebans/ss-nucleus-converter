
Nucleus converter
==================

 Nucleus converter component should have all the nucleus related conversion implementation. It contain one HTTP server and that way they qualify as another cluster in nucleus infrastructure.
 
Supported features
----------------

  - Convert HTML to PDF
  - Convert HTML to EXCEL
 
##Configuration setup

 - Configuration file name ```nucleus-converter.json```
 
 - Update the conversion file path settings, json key is  ```converter.file.location```. it should be NFS storage  file system mount path.

 
 - Update the s3 configuration settings by  replacing the placeholder values  with the actual configuration values.
 

To understand build related stuff, take a look at **BUILD_README.md**.

