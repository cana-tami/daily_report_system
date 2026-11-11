package utils;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Schedule;
import models.views.CalendarDay;

/**
 * 月間予定表（カレンダー）の表示用データを組み立てるユーティリティ。
 */
public class CalendarUtil {

	/** 1週間の日数 */
	public static final int DAYS_OF_WEEK = 7;

	/**
	 * 指定した年月のカレンダーの最初のマスの日付（その月の1日を含む週の日曜日）を返す。
	 */
	public static LocalDate getFirstDayOfCalendar(int year, int month) {
		LocalDate first_day = LocalDate.of(year, month, 1);

		// DayOfWeek は 月曜=1 〜 日曜=7 のため、7で割った余りが日曜からの日数になる
		int offset = first_day.getDayOfWeek().getValue() % DAYS_OF_WEEK;

		return first_day.minusDays(offset);
	}

	/**
	 * 指定した年月のカレンダーに必要な週数を返す。
	 */
	public static int getWeekCount(int year, int month) {
		LocalDate first_day = LocalDate.of(year, month, 1);
		int offset = first_day.getDayOfWeek().getValue() % DAYS_OF_WEEK;

		return (offset + first_day.lengthOfMonth() + DAYS_OF_WEEK - 1) / DAYS_OF_WEEK;
	}

	/**
	 * 指定した年月のカレンダーの最後のマスの日付を返す。
	 */
	public static LocalDate getLastDayOfCalendar(int year, int month) {
		return getFirstDayOfCalendar(year, month)
				.plusDays((long)getWeekCount(year, month) * DAYS_OF_WEEK - 1);
	}

	/**
	 * 指定した年月のカレンダーを、週ごとの行（7マス）のリストとして組み立てる。
	 *
	 * @param year      表示する年
	 * @param month     表示する月（1〜12）
	 * @param schedules カレンダーに表示する予定の一覧
	 * @return 週ごとに分割されたカレンダーのマスのリスト
	 */
	public static List<List<CalendarDay>> getCalendar(int year, int month, List<Schedule> schedules) {
		// 日付ごとに予定を振り分けておく
		Map<String, List<Schedule>> schedules_by_date = new HashMap<String, List<Schedule>>();
		if(schedules != null) {
			for(Schedule s : schedules) {
				if(s.getSchedule_date() == null) {
					continue;
				}

				String key = s.getSchedule_date().toString();
				List<Schedule> list = schedules_by_date.get(key);
				if(list == null) {
					list = new ArrayList<Schedule>();
					schedules_by_date.put(key, list);
				}
				list.add(s);
			}
		}

		LocalDate first_day = getFirstDayOfCalendar(year, month);
		LocalDate today = LocalDate.now();
		int week_count = getWeekCount(year, month);

		List<List<CalendarDay>> calendar = new ArrayList<List<CalendarDay>>();
		for(int w = 0; w < week_count; w++) {
			List<CalendarDay> week = new ArrayList<CalendarDay>();

			for(int d = 0; d < DAYS_OF_WEEK; d++) {
				LocalDate current = first_day.plusDays((long)w * DAYS_OF_WEEK + d);

				CalendarDay calendar_day = new CalendarDay();
				calendar_day.setDate(Date.valueOf(current));
				calendar_day.setDay(current.getDayOfMonth());
				calendar_day.setDay_of_week(current.getDayOfWeek().getValue() % DAYS_OF_WEEK + 1);
				calendar_day.setCurrent_month(current.getYear() == year && current.getMonthValue() == month);
				calendar_day.setToday(current.equals(today));

				List<Schedule> day_schedules = schedules_by_date.get(current.toString());
				if(day_schedules != null) {
					calendar_day.setSchedules(day_schedules);
				}

				week.add(calendar_day);
			}

			calendar.add(week);
		}

		return calendar;
	}
}
