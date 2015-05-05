package demo;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class QuoteServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		QuoteDAO dao = new QuoteDAO();
		List<Quote> list = dao.listQuotes();
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		out.print("<html><body>");
		out.print("Number of Quotes: " + list.size()+ "<br>");
		for (Quote quote: list) {
			out.print(quote.getAuthor() + ":" + quote.getMessage() + "<br>");
		}
		out.println("</body></html>");
	}
}
