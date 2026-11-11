package controllers.schedules;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Schedule;
import models.views.CalendarDay;
import utils.CalendarUtil;
import utils.DBUtil;

/**
 * Servlet implementation class SchedulesIndexServlet
 */
@WebServlet("/schedules/index")
public class SchedulesIndexServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public SchedulesIndexServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 表示する年月を決める（不正な値や未指定の場合は今月）
		LocalDate today = LocalDate.now();
		LocalDate target_month;
		try {
			int year = Integer.parseInt(request.getParameter("year"));
			int month = Integer.parseInt(request.getParameter("month"));
			target_month = LocalDate.of(year, month, 1);
		} catch(Exception e) {
			target_month = LocalDate.of(today.getYear(), today.getMonthValue(), 1);
		}

		int year = target_month.getYear();
		int month = target_month.getMonthValue();

		// カレンダーに表示される範囲（前後の月にはみ出す分を含む）の予定を取得する
		Date from = Date.valueOf(CalendarUtil.getFirstDayOfCalendar(year, month));
		Date to = Date.valueOf(CalendarUtil.getLastDayOfCalendar(year, month));

		EntityManager em = DBUtil.createEntityManager();

		Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");

		List<Schedule> schedules = em.createNamedQuery("getMySchedulesBetween", Schedule.class)
									 .setParameter("employee", login_employee)
									 .setParameter("from", from)
									 .setParameter("to", to)
									 .getResultList();

		long schedules_count = (long)em.createNamedQuery("getMySchedulesCountBetween", Long.class)
									   .setParameter("employee", login_employee)
									   .setParameter("from", Date.valueOf(target_month))
									   .setParameter("to", Date.valueOf(target_month.withDayOfMonth(target_month.lengthOfMonth())))
									   .getSingleResult();

		em.close();

		List<List<CalendarDay>> calendar = CalendarUtil.getCalendar(year, month, schedules);

		LocalDate prev_month = target_month.minusMonths(1);
		LocalDate next_month = target_month.plusMonths(1);

		request.setAttribute("calendar", calendar);
		request.setAttribute("year", year);
		request.setAttribute("month", month);
		request.setAttribute("schedules_count", schedules_count);
		request.setAttribute("prev_year", prev_month.getYear());
		request.setAttribute("prev_month", prev_month.getMonthValue());
		request.setAttribute("next_year", next_month.getYear());
		request.setAttribute("next_month", next_month.getMonthValue());

		if(request.getSession().getAttribute("flush") != null) {
			request.setAttribute("flush", request.getSession().getAttribute("flush"));
			request.getSession().removeAttribute("flush");
		}

		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/schedules/index.jsp");
		rd.forward(request, response);
	}

}
