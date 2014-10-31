package it3178.quotable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.mycompany.services.quoteendpoint.Quoteendpoint;
import com.mycompany.services.quoteendpoint.model.CollectionResponseQuote;
import com.mycompany.services.quoteendpoint.model.Quote;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MainActivity extends Activity {
	SimpleAdapter adapter = null;
	String[] from = {"author", "message"};
	int[] to = {android.R.id.text1, android.R.id.text2 };
	ArrayList<Map<String,String>> list = new ArrayList<Map<String,String>>();
	ListView listView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		listView = (ListView)findViewById(R.id.listView1);
		// insert 2 test list items, for testing purpose
		// comment out the testAdapter() line to remove the testing code
		testAdapter();
		// invoke the AsyncTask to consume the endpoints API from the server
		new QuotesAsyncTask(this).execute();
	}

	private void testAdapter() {
		// Test code to display 2 list items in your list view
		Map<String, String> map = new HashMap<String,String>();
		map.put("author", "foo");
		map.put("message", "happy fool day");
		list.add(map);
		map.put("message", "happy fool week");
		list.add(map);
		adapter = new SimpleAdapter(this, list, android.R.layout.simple_list_item_2, from, to);
		listView.setAdapter(adapter);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	private class QuotesAsyncTask extends AsyncTask<Void, Void, CollectionResponseQuote> {
		final JsonFactory JSON_FACTORY = new AndroidJsonFactory();
		final HttpTransport HTTP_TRANSPORT = AndroidHttp.newCompatibleTransport();
		Context context;
		ProgressDialog pd;
		
		public QuotesAsyncTask(Context context) {
			this.context = context;
		}
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(context);
			pd.setMessage("Retrieving...");
			pd.show();
		}
		@Override
		protected CollectionResponseQuote doInBackground(Void... params) {
			CollectionResponseQuote quotes = null;
			try {
				Quoteendpoint.Builder builder = new Quoteendpoint.Builder(HTTP_TRANSPORT,  JSON_FACTORY, null);
				Quoteendpoint service = builder.build();
				quotes = service.listQuote().execute();
			} catch (Exception e) {
				Log.d("Error in retrieving quotes", e.getMessage(), e);
			}
			return quotes;
		}
			
		protected void onPostExecute(CollectionResponseQuote quotes) {
			pd.dismiss();
			List<Quote> _list = quotes.getItems();
			for (Quote quote: _list) {
				HashMap<String, String> item = new HashMap<String, String>();
				item.put("author", quote.getAuthor());
				item.put("message", quote.getMessage());
				list.add(item);
			}
			adapter = new SimpleAdapter(MainActivity.this, list, android.R.layout.simple_list_item_2, from, to);
			listView.setAdapter(adapter);
		}
	
	}
}
