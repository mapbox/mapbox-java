package com.mapbox.api.directions.v5.utils;

import com.mapbox.core.TestUtils;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FormatUtilsTest extends TestUtils {

  @Test
  public void formatDateTimeToIso8601() throws ParseException {
    // parse string
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    Date d1 = df.parse("2010-11-02 16:18");
    // calendar
    Calendar calendar = Calendar.getInstance();
    calendar.set(2013, Calendar.APRIL, 3, 14, 32);
    Date d2 = calendar.getTime();
    calendar.set(2016, Calendar.MAY, 1, 8, 45, 44);
    Date d3 = calendar.getTime();
    calendar.set(2020, Calendar.DECEMBER, 16, 10, 9, 31);
    Date d4 = calendar.getTime();

    assertEquals("2010-11-02T16:18", FormatUtils.formatDateToIso8601(d1));
    assertEquals("2013-04-03T14:32", FormatUtils.formatDateToIso8601(d2));
    assertEquals("2016-05-01T08:45", FormatUtils.formatDateToIso8601(d3));
    assertEquals("2020-12-16T10:09", FormatUtils.formatDateToIso8601(d4));
  }
}
