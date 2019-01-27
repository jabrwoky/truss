@Grab('com.xlson.groovycsv:groovycsv:1.3')
import com.xlson.groovycsv.CsvParser
@Grab('com.xlson.groovycsv:groovycsv:1.3')
import com.xlson.groovycsv.CsvParser

import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

String filePath = "/home/ed/projects/truss/doc/sample-with-broken-utf8.csv"
File file = new File(filePath)
BufferedReader br = null
br = new BufferedReader(new FileReader(file))
def csvData = CsvParser.parseCsv(br)
println "Timestamp\t" +
        "Address\t" +
        "ZIP\t" +
        "FullName\t" +
        "FooDuration\t" +
        "BarDuration\t" +
        "TotalDuration\t" +
        "Notes"
def outputMap = [:]
Instant instant = Instant.now()
//println "NOW: " + instant.toString()

csvData.each {
    outputMap['Address'] = it.Address
    outputMap['ZIP'] = it.ZIP.padLeft(5, '0')
    outputMap['FullName'] = it.FullName.toUpperCase()
    outputMap['FooDuration'] = convertDuration(it.FooDuration)
    //println "fooduration     :" + formatDuration(it.FooDuration)
    outputMap['BarDuration'] = convertDuration(it.BarDuration)
    outputMap['TotalDuration'] = sumDurations(it.FooDuration, it.BarDuration)
    outputMap['Notes'] = it.Notes

    outputMap['Timestamp'] = it.Timestamp
    SimpleDateFormat inputFormat = new SimpleDateFormat("MM/dd/yy HH:mm:ss a")
    ZonedDateTime zonedDateTime = null
    try {
        zonedDateTime = inputFormat.parse(it.Timestamp).toInstant()
                .atZone(ZoneId.of("America/Los_Angeles"))
    }
    catch (ParseException e) {
        println "Invalid date format ${it.Timestamp}"
        //e.printStackTrace()
    }
    /*   println "it.Timestamp           :" + it.Timestamp
       println "zonedDateTime          :" + zonedDateTime
       println "zonedDateTime New York :" + zonedDateTime.withZoneSameLocal(ZoneId.of("America/New_York"))
       DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
       println "dateTimeFormatted      :" + dateTimeFormatter.format(zonedDateTime.withZoneSameLocal(ZoneId.of("America/New_York")))
   *//*
       TimeZone tz = TimeZone.getTimeZone("America/Los_Angeles");
       DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'")
       df.setTimeZone(tz);
       String nowAsISO = df.format(zonedDateTime);

   println "Iso date :" + nowAsISO

       SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a")
       println outputFormat(zonedDateTime)
   */

    /*outputMap['Timestamp'] = it.Timestamp
    outputMap['Address'] = it.Address
    outputMap['ZIP'] = it.ZIP.padLeft(5,'0')
    outputMap['FullName'] = it.FullName.toUpperCase()
    outputMap['FooDuration'] = it.FooDuration
    outputMap['BarDuration'] = it.BarDuration
    outputMap['TotalDuration'] = it.TotalDuration
    outputMap['Notes'] = it.Notes
    */
    println "FooDuration" + outputMap.FooDuration
    println "BarDuration" + outputMap.BarDuration
    println "TotalDuration" + outputMap.TotalDuration
}


private String sumDurations(String foo, String bar) {
    convertMillisecondsToHourString( convertToMilliseconds(foo) + convertToMilliseconds(bar))

}

private String convertDuration(String durationString) {
    convertMillisecondsToHourString(convertToMilliseconds(durationString))
}

private int convertToMilliseconds(String durationString) {
    //println "Durationstring : " + durationString
    def parts = durationString.split(":") as List
    Duration d = Duration.ZERO
    int hours = Integer.parseInt(parts[0])
    int minutes = Integer.parseInt(parts[1])
    int hoursToMs = hours * 3600000;
    int minutesToMs = minutes * 60000;


    String secondsString = parts[2]
    //println "secondsString:  " + secondsString
    def secondsParts = secondsString.split("\\.")

    int seconds = Integer.parseInt(secondsParts[0])
    int milliseconds = Integer.parseInt(secondsParts[1])
    //println "secondsParts:  " + secondsParts
    int secondsToMs = seconds * 1000;
    /*printf( sprintf (" hours converted %09d\t%09d\n",[hours, hoursToMs]))
    printf( sprintf (" minutes converted %09d\t%09d\n",[minutes, minutesToMs]))
    printf( sprintf (" seconds converted %09d\t%09d\n",[seconds, secondsToMs]))
    printf( sprintf (" milliseconds converted %09d\t%09d\n",[milliseconds, milliseconds]))
*/
    int totalMilliseconds = hoursToMs + minutesToMs + secondsToMs + milliseconds
    //println "totalMilliseconds  :" + totalMilliseconds
    //float totalHours = totalMilliseconds / 3600000
    /*println "totalMilliseconds: " +totalMilliseconds
    println (sprintf("%.2f",totalMilliseconds/ 3600000 as float))
    */
    totalMilliseconds
}

private String convertMillisecondsToHourString(int milliseconds) {
    float totalHours = milliseconds / 3600000 as float
    sprintf("%.2f", totalHours)
}
