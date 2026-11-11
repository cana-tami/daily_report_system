package models.validators;

import java.util.ArrayList;
import java.util.List;

import models.Schedule;

public class ScheduleValidator {
	public static List<String> validate(Schedule s) {
		List<String> errors = new ArrayList<String>();

		String date_error = _validateScheduleDate(s.getSchedule_date());
		if(!date_error.equals("")) {
			errors.add(date_error);
		}

		String title_error = _validateTitle(s.getTitle());
		if(!title_error.equals("")) {
			errors.add(title_error);
		}

		return errors;
	}

	private static String _validateScheduleDate(java.sql.Date schedule_date) {
		if(schedule_date == null) {
			return "日付を入力してください。";
		}

		return "";
	}

	private static String _validateTitle(String title) {
		if(title == null || title.equals("")) {
			return "予定のタイトルを入力してください。";
		}

		return "";
	}
}
