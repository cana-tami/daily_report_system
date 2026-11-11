<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/WEB-INF/views/layout/app.jsp">
	<c:param name="content">
		<c:if test="${flush != null}">
			<div id="flush_success">
				<c:out value="${flush}"></c:out>
			</div>
		</c:if>

		<h2>月間予定表</h2>

		<c:url var="prev_url" value="/schedules/index">
			<c:param name="year" value="${prev_year}" />
			<c:param name="month" value="${prev_month}" />
		</c:url>
		<c:url var="next_url" value="/schedules/index">
			<c:param name="year" value="${next_year}" />
			<c:param name="month" value="${next_month}" />
		</c:url>

		<div id="calendar_nav">
			<a href="${prev_url}">&lt;&nbsp;前の月</a>
			<span id="calendar_title"><c:out value="${year}" />年&nbsp;<c:out value="${month}" />月</span>
			<a href="${next_url}">次の月&nbsp;&gt;</a>
			&nbsp;&nbsp;&nbsp;
			<a href="<c:url value='/schedules/index' />">今月</a>
		</div>

		<table id="calendar">
			<tbody>
				<tr>
					<th class="sunday">日</th>
					<th>月</th>
					<th>火</th>
					<th>水</th>
					<th>木</th>
					<th>金</th>
					<th class="saturday">土</th>
				</tr>
				<c:forEach var="week" items="${calendar}">
					<tr>
						<c:forEach var="day" items="${week}">
							<td class="calendar_cell<c:if test='${!day.current_month}'> other_month</c:if><c:if test='${day.today}'> today</c:if><c:if test='${day.day_of_week == 1}'> sunday</c:if><c:if test='${day.day_of_week == 7}'> saturday</c:if>">
								<div class="calendar_date">
									<c:url var="new_url" value="/schedules/new">
										<c:param name="date" value="${day.date}" />
									</c:url>
									<a href="${new_url}" title="この日に予定を登録する"><c:out value="${day.day}" /></a>
								</div>
								<ul class="calendar_schedules">
									<c:forEach var="schedule" items="${day.schedules}">
										<li>
											<a href="<c:url value='/schedules/show?id=${schedule.id}' />"><c:out value="${schedule.title}" /></a>
										</li>
									</c:forEach>
								</ul>
							</td>
						</c:forEach>
					</tr>
				</c:forEach>
			</tbody>
		</table>

		<p>（<c:out value="${month}" />月の予定&nbsp;全&nbsp;<c:out value="${schedules_count}" />&nbsp;件）</p>
		<p><a href="<c:url value='/schedules/new' />">新規予定の登録</a></p>
	</c:param>
</c:import>
