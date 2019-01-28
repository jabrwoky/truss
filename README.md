# csvcleaner

This project contains my solution to the Truss exercise  
 
This is a standalone groovy script that reads csv formatted data from stdin and writes reformatted csv data to stdout  

* The TimeStamp field on the input is assumed to be Pacific time, and the output is the ISO-8601 representation of that same time in America/New York
* Zip codes are formatted as 5 digits with leading zeros
* Name columns are converted to upper case
* FooDuration and BarDuration are output as floating point seconds
* TotalDuration is ignored on input, and is the sum of FooDuration and BarDurtion on output
* Duration and TimeStamp errors cause the record to be skipped
* Notes column is currently passed through with no editing

I took the liberty to treat the problem as a utility script, so testing different input files is manual.  
Clearly for production code this would have become an actual service with unit tests.  
I did not do the character validity feature because the requirement was unclear to me, and I did not have to opportunitiy to talk to team mates or a product owner to clarify the requirement.  


## Getting Started

### Prerequisites

You will need Java 8, Git, and Groovy  
I use sdkman to install and manage versions, you can use whatever package manager or installer you want. If you need sdkman, see `https://sdkman.io/install` to install  
Then:  
```
sdk install java 8u111  
sdk install groovy 2.5.5  
```

 
### Troubleshooting
If you have issues with downloading @Grapes for commons-logging, clear your groovy grapes directory  
`rm -rf  ~/.groovy/grapes/commons-logging/  `  
You may have to repeat this before all the dependencies get resolved  
You can get more debugging information about dependency resolution by turning up the logging

`export JAVA_OPTS="-Dgroovy.grape.report.downloads=true -Divy.message.logger.level=4"
`

### Installation
Clone the repo from github
`git clone https://github.com/jabrwoky/truss.git`




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


