package controllers.schedules;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
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
import models.validators.ScheduleValidator;
import utils.DBUtil;

/**
 * Servlet implementation class SchedulesCreateServlet
 */
@WebServlet("/schedules/create")
public class SchedulesCreateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public SchedulesCreateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String _token = (String)request.getParameter("_token");
		if(_token != null && _token.equals(request.getSession().getId())) {
			EntityManager em = DBUtil.createEntityManager();

			Schedule s = new Schedule();

			s.setEmployee((Employee)request.getSession().getAttribute("login_employee"));

			Date schedule_date = null;
			String sd_str = request.getParameter("schedule_date");
			if(sd_str != null && !sd_str.equals("")) {
				try {
					schedule_date = Date.valueOf(sd_str);
				} catch(IllegalArgumentException e) {
					schedule_date = null;
				}
			}
			s.setSchedule_date(schedule_date);

			s.setTitle(request.getParameter("title"));
			s.setContent(request.getParameter("content"));

			Timestamp currentTime = new Timestamp(System.currentTimeMillis());
			s.setCreated_at(currentTime);
			s.setUpdated_at(currentTime);

			List<String> errors = ScheduleValidator.validate(s);
			if(errors.size() > 0) {
				em.close();

				request.setAttribute("_token", request.getSession().getId());
				request.setAttribute("schedule", s);
				request.setAttribute("errors", errors);

				RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/schedules/new.jsp");
				rd.forward(request, response);
			} else {
				em.getTransaction().begin();
				em.persist(s);
				em.getTransaction().commit();
				em.close();
				request.getSession().setAttribute("flush", "予定の登録が完了しました。");

				response.sendRedirect(request.getContextPath() + "/schedules/index"
						+ "?year=" + (s.getSchedule_date().toLocalDate().getYear())
						+ "&month=" + (s.getSchedule_date().toLocalDate().getMonthValue()));
			}
		}
	}

}
