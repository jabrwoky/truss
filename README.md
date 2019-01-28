# csvcleaner

This project contains my solution to the Truss exercise  
ork zone  
This is a standalone groovy script that reads csv formatted data from stdin and writes reformatted csv data to stdout  

* The TimeStamp field on the input is assumed to be Pacific time, and the output is the ISO-8601 representation of that same time in America/New Y
* Zip codes are formatted as 5 digits with leading zeros
* Name columns are converted to upper case
* FooDuration and BarDuration are output as floating point seconds
* TotalDuration is ignored on input, and is the sum of FooDuration and BarDurtion on output
* Duration and TimeStamp errors cause the record to be skipped
* Notes column is currently passed through with no editing

I took the liberty to treat the problem as a utility script, so testing different imput files is manual.  


## Getting Started



### Prerequisites

You will need Java 8 and Groovy
I use sdkman to install and manage versions.  
If you don't have sdkman, see https://sdkman.io/install to install  
Then:  
```
sdk install java 8u111  
sdk install groovy 2.5.5  
```
### Troubleshooting
If you have issues with downloading grapes for commons-logging, clear your groovy grapes directory  
`rm -rf  ~/.groovy/grapes/commons-logging/  `  
You may have to repeat this before all the dependencies get resolved  
You can get more debugging information about dependency resolution by turning up the logging

`export JAVA_OPTS="-Dgroovy.grape.report.downloads=true -Divy.message.logger.level=4"
`


End with an example of getting some data out of the system or using it for a little demo

## Running
```
cd truss/src
groovy csvcleaner < <path to input file>  
# for example:
groovy csvcleaner.groovy < ../doc/sample.csv
```

Errors are directed to stderr, you can use standard bash redirecton of stdout and stderr  

``groovy csvcleaner.groovy < ../doc/sample.csv 2>error.log``

## Authors

* **Ed Delaney** 


