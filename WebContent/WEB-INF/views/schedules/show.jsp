<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:import url="/WEB-INF/views/layout/app.jsp">
	<c:param name="content">
		<c:choose>
			<c:when test="${schedule != null}">
				<h2>予定　詳細ページ</h2>

				<table>
					<tbody>
						<tr>
							<th>氏名</th>
							<td><c:out value="${schedule.employee.name}" /></td>
						</tr>
						<tr>
							<th>日付</th>
							<td><fmt:formatDate value="${schedule.schedule_date}" pattern="yyyy-MM-dd" /></td>
						</tr>
						<tr>
							<th>予定</th>
							<td><c:out value="${schedule.title}" /></td>
						</tr>
						<tr>
							<th>メモ</th>
							<td>
								<pre><c:out value="${schedule.content}" /></pre>
							</td>
						</tr>
						<tr>
							<th>登録日時</th>
							<td>
								<fmt:formatDate value="${schedule.created_at}" pattern="yyyy-MM-dd HH:mm:ss" />
							</td>
						</tr>
						<tr>
							<th>更新日時</th>
							<td>
								<fmt:formatDate value="${schedule.updated_at}" pattern="yyyy-MM-dd HH:mm:ss" />
							</td>
						</tr>
					</tbody>
				</table>

				<p><a href="<c:url value='/schedules/edit?id=${schedule.id}' />">この予定を編集する</a></p>
			</c:when>
			<c:otherwise>
				<h2>お探しのデータは見つかりませんでした。</h2>
			</c:otherwise>
		</c:choose>

		<p><a href="<c:url value='/schedules/index' />">月間予定表に戻る</a></p>
	</c:param>
</c:import>
