package nju.fraborna.sns21.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nju.fraborna.sns21.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class TweetActivity extends Activity {

	private TextView username, timestamp, content, zanList;
	private ListView commentsList;

	public List<Map<String, Object>> comments = new ArrayList<Map<String, Object>>();

	private String sns;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tweets_detail);

		Bundle bundle = getIntent().getExtras();
		String usernameString = bundle.getString("username");
		String timeString = bundle.getString("timestamp");
		String contentString = bundle.getString("content");
		sns = bundle.getString("sns");

		setView();

		username.setText(usernameString);
		timestamp.setText(timeString);
		content.setText(contentString);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void setView() {
		username = (TextView) findViewById(R.id.tweets_body_username);
		timestamp = (TextView) findViewById(R.id.tweets_body_timestamp);
		content = (TextView) findViewById(R.id.tweets_body_content);
		zanList = (TextView) findViewById(R.id.zan_list);

		commentsList = (ListView) findViewById(R.id.comments_list);

		List<Map<String, Object>> list = getComments(sns);

		SimpleAdapter simpleAdapter = new SimpleAdapter(TweetActivity.this,
				list, R.layout.comment_body, new String[] { "username",
						"timestamp", "content" }, new int[] {
						R.id.comment_username, R.id.comment_timestamp,
						R.id.comment_content });

		commentsList.setAdapter(simpleAdapter);
		commentsList.invalidate();
	}

	public List<Map<String, Object>> getComments(String sns) {
		return comments;
	}
}
