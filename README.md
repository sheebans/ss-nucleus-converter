
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

 - Update the s3 config  file path settings, json key is  ```s3.config.file.location```. it should be s3 config file properties.
 
 - ```s3-converter-config.properties``` copy  s3 configuration template  file  from this repo  to  the configuration folder, this file should have s3 accesskey, secret and bucket name details. Replace the placeholder values in the copied property file with the actually values.
 

To understand build related stuff, take a look at **BUILD_README.md**.

