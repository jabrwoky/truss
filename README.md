# Project Title

This project contains my solution to the Truss exercise  

## Getting Started



### Prerequisites

You will need Java 8 and Groovy
I use sdkman to install and manage versions. If you don't have sdkman, see https://sdkman.io/install to install  
sdk install java 8u111  
sdk install groovy 2.5.5  


### Installing

If you have issues with downloading grapes for commons-logging, clear your groovy grapes
rm -rf  ~/.groovy/grapes/commons-logging/
You may have to repeat this before all the dependencies get resolved
You can get more debugging information about dependency resolution by turning up the logging

export JAVA_OPTS="-Dgroovy.grape.report.downloads=true -Divy.message.logger.level=4"

```
until finished
```

End with an example of getting some data out of the system or using it for a little demo

## Running the tests

Explain how to run the automated tests for this system


## Authors

* **Ed Delaney** 


