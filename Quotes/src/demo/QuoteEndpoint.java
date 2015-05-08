package demo;

import demo.EMF;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.datanucleus.query.JPACursorHelper;

import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@Api(name = "quoteendpoint", namespace = @ApiNamespace(ownerDomain = "mycompany.com", ownerName = "mycompany.com", packagePath = "services"))
public class QuoteEndpoint {

	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method and paging support.
	 *
	 * @return A CollectionResponse class containing the list of all entities
	 * persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listQuote")
	public CollectionResponse<Quote> listQuote(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		EntityManager mgr = null;
		Cursor cursor = null;
		List<Quote> execute = null;

		try {
			mgr = getEntityManager();
			Query query = mgr.createQuery("select from Quote as Quote");
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				query.setHint(JPACursorHelper.CURSOR_HINT, cursor);
			}

			if (limit != null) {
				query.setFirstResult(0);
				query.setMaxResults(limit);
			}

			execute = (List<Quote>) query.getResultList();
			cursor = JPACursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (Quote obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<Quote> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}

	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listQuoteByAuthor", path = "quoteByAuthor")
	public CollectionResponse<Quote> listQuoteByAuthor(
			@Nullable @Named("author") String author,			
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		EntityManager mgr = null;
		Cursor cursor = null;
		List<Quote> execute = null;

		try {
			mgr = getEntityManager();
			Query query = mgr.createQuery("select x from Quote x where x.author = '" + author +"'");
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				query.setHint(JPACursorHelper.CURSOR_HINT, cursor);
			}

			if (limit != null) {
				query.setFirstResult(0);
				query.setMaxResults(limit);
			}

			execute = (List<Quote>) query.getResultList();
			cursor = JPACursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (Quote obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<Quote> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}
	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getQuote")
	public Quote getQuote(@Named("id") Long id) {
		EntityManager mgr = getEntityManager();
		Quote quote = null;
		try {
			quote = mgr.find(Quote.class, id);
		} finally {
			mgr.close();
		}
		return quote;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param quote the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertQuote")
	public Quote insertQuote(Quote quote) {
		EntityManager mgr = getEntityManager();
		try {
			if (quote.getId() != null) {
				if (containsQuote(quote)) {
					throw new EntityExistsException("Object already exists");
				}
			}
			mgr.persist(quote);
		} finally {
			mgr.close();
		}
		return quote;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param quote the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateQuote")
	public Quote updateQuote(Quote quote) {
		EntityManager mgr = getEntityManager();
		try {
			if (!containsQuote(quote)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.persist(quote);
		} finally {
			mgr.close();
		}
		return quote;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 */
	@ApiMethod(name = "removeQuote")
	public void removeQuote(@Named("id") Long id) {
		EntityManager mgr = getEntityManager();
		try {
			Quote quote = mgr.find(Quote.class, id);
			mgr.remove(quote);
		} finally {
			mgr.close();
		}
	}

	private boolean containsQuote(Quote quote) {
		EntityManager mgr = getEntityManager();
		boolean contains = true;
		try {
			Quote item = mgr.find(Quote.class, quote.getId());
			if (item == null) {
				contains = false;
			}
		} finally {
			mgr.close();
		}
		return contains;
	}

	private static EntityManager getEntityManager() {
		return EMF.get().createEntityManager();
	}

}
