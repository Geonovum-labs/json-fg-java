package nl.geonovum.jsonfg.model;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
public final class Time {

  private final LocalDate date;

  private final ZonedDateTime timestamp;

  private final Interval<?> interval;

  static final class DateInterval extends Interval<LocalDate> {
    DateInterval(LocalDate start, LocalDate end) {
      super(start, end);
    }
  }

  static final class TimestampInterval extends Interval<ZonedDateTime> {
    TimestampInterval(ZonedDateTime start, ZonedDateTime end) {
      super(start, end);
    }
  }

  @RequiredArgsConstructor
  static class Interval<T> {

    private final T start;

    private final T end;
  }
}
