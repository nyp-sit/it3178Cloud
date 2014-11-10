package demo;

import java.io.IOException;
import java.util.*;

import javax.servlet.http.*;

@SuppressWarnings("serial")
public class QuoteSQLServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		QuoteDAO dao = new QuoteDAO();
		List<Quote> list = dao.listQuotes();
			
		resp.getWriter().println("Number of Quotes: " + list.size());
	}
}
