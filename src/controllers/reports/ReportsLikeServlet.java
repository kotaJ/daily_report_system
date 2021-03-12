package controllers.reports;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Like;
import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class ReportsLikeServlet
 */
@WebServlet("/reports/like")
public class ReportsLikeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsLikeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


         EntityManager em = DBUtil.createEntityManager();

         int page;
         try{
             page = Integer.parseInt(request.getParameter("page"));
         } catch(Exception e) {
             page = 1;
         }

         try{

        Report l = em.find(Report.class , Integer.parseInt(request.getParameter("id")));



         List <Like> likes = em.createNamedQuery("getMyAllLikes" , Like.class)
                                 .setParameter("report",l)
                                 .setFirstResult(15 * (page - 1))
                                 .setMaxResults(15)
                                 .getResultList();


         long likes_count = (long)em.createNamedQuery("getMyLikesCount", Long.class)
                 .setParameter("report", l)
                 .getSingleResult();

         em.close();


         request.setAttribute("page", page);
         request.setAttribute("likes_count", likes_count);
         request.setAttribute("likes", likes);


         }catch(NumberFormatException ex){}

         RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/like.jsp");
         rd.forward(request, response);

}
}
