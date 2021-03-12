package controllers.reports;

import java.io.IOException;
import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Like;
import models.Report;
import utils.DBUtil;


/**
 * Servlet implementation class ReportsCreateLikeServlet
 */
@WebServlet("/reports/create/like")
public class ReportsCreateLikeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsCreateLikeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }





protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    // 日報データを取得
    EntityManager em = DBUtil.createEntityManager();
    Report r = em.find(Report.class , Integer.parseInt(request.getParameter("id")));


    // いいね数に1を加算する
    int like_count = r.getLike_count();
            like_count++;
    r.setLike_count(like_count);


    //Likeの更新
    Like l = new Like();


   Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");



    //日付の取得
    Timestamp currentTime = new Timestamp(System.currentTimeMillis());
    l.setCreated_at(currentTime);
    l.setUpdated_at(currentTime);

    l.setEmployee(login_employee);
    l.setReport(r);

    long check_like =(long)em.createNamedQuery("checkLike_count", Long.class)
            .setParameter("report",r)
            .setParameter("employee", login_employee)
            .getSingleResult();

        request.setAttribute("check_like" , check_like);


    // 日報テーブル更新
    em.getTransaction().begin();
    em.persist(l);
    em.getTransaction().commit();
    em.close();

    // 日報一覧ページへ遷移
    request.getSession().setAttribute("flush", "いいねしました。");
    response.sendRedirect(request.getContextPath() + "/reports/index");



}
}





