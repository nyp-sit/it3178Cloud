package demo;

import java.util.*;

import javax.persistence.*;

public class QuoteDAO {
	@SuppressWarnings("unchecked")
	public List<Quote> listQuotes() {
		List<Quote> list = new ArrayList<Quote>();
		EntityManager mgr = EMF.get().createEntityManager();
		Query query = mgr.createQuery("select x from Quote x");
		list = (List<Quote>)query.getResultList();
		return list;
	}

}
