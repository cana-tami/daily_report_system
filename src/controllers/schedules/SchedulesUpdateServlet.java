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
 * Servlet implementation class SchedulesUpdateServlet
 */
@WebServlet("/schedules/update")
public class SchedulesUpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public SchedulesUpdateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String _token = (String)request.getParameter("_token");
		if(_token != null && _token.equals(request.getSession().getId())) {
			Integer schedule_id = (Integer)(request.getSession().getAttribute("schedule_id"));
			if(schedule_id == null) {
				response.sendRedirect(request.getContextPath() + "/schedules/index");
				return;
			}

			EntityManager em = DBUtil.createEntityManager();

			Schedule s = em.find(Schedule.class, schedule_id);

			Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");
			if(s == null || !login_employee.getId().equals(s.getEmployee().getId())) {
				em.close();
				request.getSession().removeAttribute("schedule_id");

				response.sendRedirect(request.getContextPath() + "/schedules/index");
				return;
			}

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
			s.setUpdated_at(new Timestamp(System.currentTimeMillis()));

			List<String> errors = ScheduleValidator.validate(s);
			if(errors.size() > 0) {
				em.close();

				request.setAttribute("_token", request.getSession().getId());
				request.setAttribute("schedule", s);
				request.setAttribute("errors", errors);

				RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/schedules/edit.jsp");
				rd.forward(request, response);
			} else {
				em.getTransaction().begin();
				em.getTransaction().commit();

				Date updated_date = s.getSchedule_date();
				em.close();
				request.getSession().setAttribute("flush", "予定の更新が完了しました。");

				request.getSession().removeAttribute("schedule_id");

				response.sendRedirect(request.getContextPath() + "/schedules/index"
						+ "?year=" + updated_date.toLocalDate().getYear()
						+ "&month=" + updated_date.toLocalDate().getMonthValue());
			}
		}
	}

}
