package controllers.schedules;

import java.io.IOException;
import java.sql.Date;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Schedule;
import utils.DBUtil;

/**
 * Servlet implementation class SchedulesDestroyServlet
 */
@WebServlet("/schedules/destroy")
public class SchedulesDestroyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public SchedulesDestroyServlet() {
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

			Date deleted_date = s.getSchedule_date();

			em.getTransaction().begin();
			em.remove(s);
			em.getTransaction().commit();
			em.close();

			request.getSession().setAttribute("flush", "予定の削除が完了しました。");
			request.getSession().removeAttribute("schedule_id");

			response.sendRedirect(request.getContextPath() + "/schedules/index"
					+ "?year=" + deleted_date.toLocalDate().getYear()
					+ "&month=" + deleted_date.toLocalDate().getMonthValue());
		}
	}

}
