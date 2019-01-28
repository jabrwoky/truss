@Grab('com.opencsv:opencsv:4.0')
@Grab('com.xlson.groovycsv:groovycsv:1.3')
import com.opencsv.CSVWriter
import com.xlson.groovycsv.CsvParser


import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.nio.charset.CharsetDecoder
import java.nio.charset.CharsetEncoder
import java.nio.ByteBuffer
import java.nio.charset.CharacterCodingException
import java.time.format.DateTimeParseException

// Read in the csv data
BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))
def Iterator csvData = CsvParser.parseCsv(bufferedReader)

// transform the data

def outputSet = []  // this will hold the final processed dataset

try {

    csvData.findAll { it ->
        def outputMap = [:]  // I like maps, it's easier to debug
        outputMap['Timestamp'] = timeStringToIso(it.Timestamp)
        if (!outputMap['Timestamp']) { // something is wrong with the input, skip this line
            System.err.println "timeStamp can't be formatted, skipping..."
            return true // go to next line
        }
        outputMap['Address'] = it.Address
        outputMap['ZIP'] = it.ZIP.padLeft(5, '0')
        outputMap['FullName'] = it.FullName.toUpperCase()
        outputMap['FooDuration'] = convertDuration(it.FooDuration)
        outputMap['BarDuration'] = convertDuration(it.BarDuration)
        if (!outputMap['FooDuration'] || !outputMap['BarDuration']) {
            System.err.println "duration can't be formatted, skipping..."
            return true // go to next line
        }
        outputMap['TotalDuration'] = sumDurations(it.FooDuration, it.BarDuration)
        outputMap['Notes'] = it.Notes
        //checkUTF8( it.Notes )
        outputSet.add(outputMap.collect { it.value } as String[])
    }
} catch (MissingPropertyException e) {
    System.err.println "Fatal error processing input file:" + e.getMessage()
    System.exit(1)
}

// write the output
writeCsvFromMap(outputSet)

private writeCsvFromMap(stuffToPrint) {
    String headerLine = "Timestamp,Address,ZIP,FullName,FooDuration,BarDuration,TotalDuration,Notes\n"
    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(System.out))
    bufferedWriter.write(headerLine)
    bufferedWriter.flush()
    CSVWriter writer = new CSVWriter(bufferedWriter,
            ',' as char,
            '"' as char,
            CSVWriter.DEFAULT_ESCAPE_CHARACTER,
            CSVWriter.DEFAULT_LINE_END)
    stuffToPrint.each {
        //println "Writing " + it
        writer.writeNext(it)
        writer.flush();
    }

}

private String sumDurations(String foo, String bar) {
    try {
        convertMillisecondsToSecondsString(convertToMilliseconds(foo) + convertToMilliseconds(bar))
    } catch (java.lang.NumberFormatException e) {
        return null
    }
}

private String convertDuration(String durationString) {
    try {
        convertMillisecondsToSecondsString(convertToMilliseconds(durationString))
    } catch (java.lang.NumberFormatException e) {
        System.err.println "Cannot parse duration:" + durationString
        return null
    }
}

private int convertToMilliseconds(String durationString) {
    def parts = durationString.split(":") as List

    int hours = Integer.parseInt(parts[0])
    int minutes = Integer.parseInt(parts[1])

    int hoursToMs = hours * 3600000;
    int minutesToMs = minutes * 60000;

    // parse seconds and milliseconds on '.' 999.999
    String secondsString = parts[2]
    def secondsParts = secondsString.split("\\.")
    int seconds = Integer.parseInt(secondsParts[0])
    int milliseconds = Integer.parseInt(secondsParts[1])
    int secondsToMs = seconds * 1000;
    int totalMilliseconds = hoursToMs + minutesToMs + secondsToMs + milliseconds
    totalMilliseconds
}

private String convertMillisecondsToSecondsString(int milliseconds) {
    sprintf("%.4f", milliseconds / 1000 as float)
}

private String timeStringToIso(String timeString) {
    if (!timeString) {
        System.err.println "timeString cannot be null"
        return null
    }
    ZonedDateTime zonedDateTime = null
    // 4/1/11 11:00:00 AM
    String inputPattern = "M/d/yy h:mm:ss a";
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(inputPattern).withZone(ZoneId.of("America/Los_Angeles"))
    try {
        zonedDateTime = ZonedDateTime.parse(timeString, dateTimeFormatter)
    } catch (DateTimeParseException e) {
        System.err.println "Cannot parse date: " + timeString
        return null
    }

    // So at that instant in LA, what time is it in NY?
    ZonedDateTime easternDateTime = zonedDateTime.withZoneSameInstant(ZoneId.of("America/New_York"));
    easternDateTime.toOffsetDateTime().toString()
}


