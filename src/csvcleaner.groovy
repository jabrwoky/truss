@Grab('com.xlson.groovycsv:groovycsv:1.3')
import com.xlson.groovycsv.CsvParser
@Grab('com.xlson.groovycsv:groovycsv:1.3')
import com.xlson.groovycsv.CsvParser

import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

String filePath = "/home/ed/projects/truss/doc/sample-with-broken-utf8.csv"
File file = new File(filePath)
BufferedReader br = null
br = new BufferedReader(new FileReader(file))
def csvData = CsvParser.parseCsv(br)
/*
println "Timestamp\t" +
        "Address\t" +
        "ZIP\t" +
        "FullName\t" +
        "FooDuration\t" +
        "BarDuration\t" +
        "TotalDuration\t" +
        "Notes"
*/
def outputMap = [:]
Instant instant = Instant.now()
//println "NOW: " + instant.toString()

csvData.each {
    outputMap['Timestamp'] = timeStringToIso(it.Timestamp)
    outputMap['Address'] = it.Address
    outputMap['ZIP'] = it.ZIP.padLeft(5, '0')
    outputMap['FullName'] = it.FullName.toUpperCase()
    outputMap['FooDuration'] = convertDuration(it.FooDuration)
    //println "fooduration     :" + formatDuration(it.FooDuration)
    outputMap['BarDuration'] = convertDuration(it.BarDuration)
    outputMap['TotalDuration'] = sumDurations(it.FooDuration, it.BarDuration)
    //outputMap['Notes'] = checkUTF8(it.Notes)


    /*outputMap['Timestamp'] = it.Timestamp
    outputMap['Address'] = it.Address
    outputMap['ZIP'] = it.ZIP.padLeft(5,'0')
    outputMap['FullName'] = it.FullName.toUpperCase()
    outputMap['FooDuration'] = it.FooDuration
    outputMap['BarDuration'] = it.BarDuration
    outputMap['TotalDuration'] = it.TotalDuration
    outputMap['Notes'] = it.Notes
    */
    /*println "FooDuration" + outputMap.FooDuration
    println "BarDuration" + outputMap.BarDuration
    println "TotalDuration" + outputMap.TotalDuration
*/
}


private String sumDurations(String foo, String bar) {
    convertMillisecondsToHourString(convertToMilliseconds(foo) + convertToMilliseconds(bar))

}

private String convertDuration(String durationString) {
    convertMillisecondsToHourString(convertToMilliseconds(durationString))
}

private int convertToMilliseconds(String durationString) {
    def parts = durationString.split(":") as List
    Duration d = Duration.ZERO
    int hours = Integer.parseInt(parts[0])
    int minutes = Integer.parseInt(parts[1])
    int hoursToMs = hours * 3600000;
    int minutesToMs = minutes * 60000;


    String secondsString = parts[2]
    def secondsParts = secondsString.split("\\.")
    int seconds = Integer.parseInt(secondsParts[0])
    int milliseconds = Integer.parseInt(secondsParts[1])
    int secondsToMs = seconds * 1000;
    int totalMilliseconds = hoursToMs + minutesToMs + secondsToMs + milliseconds
    totalMilliseconds
}

private String convertMillisecondsToHourString(int milliseconds) {
    float totalHours = milliseconds / 3600000 as float
    sprintf("%.2f", totalHours)
}

private String timeStringToIso(String timeString) {
    println "timeString             :" + timeString
    ZonedDateTime zonedDateTime = null
    // 4/1/11 11:00:00 AM
    String inputPattern = "M/d/yy h:mm:ss a";
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(inputPattern).withZone(ZoneId.of("America/Los_Angeles"))
    zonedDateTime = ZonedDateTime.parse(timeString, dateTimeFormatter);
    println "zonedDateTime          :" + zonedDateTime

    ZonedDateTime centralEastern = zonedDateTime.withZoneSameInstant(ZoneId.of("America/New_York"));
    println "centralEastern         :" + centralEastern


    println "zonedDateTime ISO-8601 format: " + zonedDateTime.toOffsetDateTime().toString()
    zonedDateTime.toOffsetDateTime().toString()
}

//checkUTF8()

private void checkUTF8(String myString) {
    System.out.println(myString);
    byte[] myBytes = null;

    try {
        myBytes = myString.getBytes("UTF-8");
    }
    catch (UnsupportedEncodingException e) {
        println "String is not utf8: " + myString
    }

    for (int i = 0; i < myBytes.length; i++) {
        System.out.println(myBytes[i]);
    }
    myString
}

