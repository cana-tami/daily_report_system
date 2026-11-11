package models.views;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import models.Schedule;

/**
 * 月間予定表（カレンダー）の1マス分の情報を保持するクラス。
 * データベースには保存しないため、エンティティではない。
 */
public class CalendarDay {
	/** そのマスが表す日付 */
	private Date date;

	/** 日（1〜31） */
	private int day;

	/** 曜日（1:日曜 〜 7:土曜） */
	private int day_of_week;

	/** 表示中の月の日であればtrue（前後の月の日であればfalse） */
	private boolean current_month;

	/** 今日であればtrue */
	private boolean today;

	/** その日に登録されている予定の一覧 */
	private List<Schedule> schedules = new ArrayList<Schedule>();

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getDay_of_week() {
		return day_of_week;
	}

	public void setDay_of_week(int day_of_week) {
		this.day_of_week = day_of_week;
	}

	public boolean isCurrent_month() {
		return current_month;
	}

	public void setCurrent_month(boolean current_month) {
		this.current_month = current_month;
	}

	public boolean isToday() {
		return today;
	}

	public void setToday(boolean today) {
		this.today = today;
	}

	public List<Schedule> getSchedules() {
		return schedules;
	}

	public void setSchedules(List<Schedule> schedules) {
		this.schedules = schedules;
	}
}
