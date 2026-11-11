package controllers.schedules;

import java.io.IOException;
import java.sql.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Schedule;

/**
 * Servlet implementation class SchedulesNewServlet
 */
@WebServlet("/schedules/new")
public class SchedulesNewServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public SchedulesNewServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("_token", request.getSession().getId());

		Schedule s = new Schedule();

		// カレンダーの日付から遷移してきた場合は、その日付を初期値にする
		Date schedule_date = new Date(System.currentTimeMillis());
		String date_str = request.getParameter("date");
		if(date_str != null && !date_str.equals("")) {
			try {
				schedule_date = Date.valueOf(date_str);
			} catch(IllegalArgumentException e) {
				schedule_date = new Date(System.currentTimeMillis());
			}
		}
		s.setSchedule_date(schedule_date);

		request.setAttribute("schedule", s);

		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/schedules/new.jsp");
		rd.forward(request, response);
	}

}
