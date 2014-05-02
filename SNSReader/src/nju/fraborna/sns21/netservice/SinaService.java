package nju.fraborna.sns21.netservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import nju.fraborna.sns21.adapter.SinaTweetAdapter;
import nju.fraborna.sns21.model.Tweet;

public class SinaService implements NetService {

	private String accessToken;
	private SinaTweetAdapter sinaTweetAdapter = new SinaTweetAdapter();
	private ArrayList<Tweet> tweets = new ArrayList<Tweet>();

	public SinaService(String accessToken) {
		super();
		this.accessToken = accessToken;
	}

	@Override
	public ArrayList<Tweet> getTweets(int num) {
		// TODO Auto-generated method stub
		tweets.removeAll(tweets);
		for (int i = 1; i <= num; i++) {
			Tweet tweet = null;
			try {
				String response = "";
				String temp = "";
				String url = "https://api.weibo.com/2/statuses/friends_timeline.json?access_token="
						+ accessToken + "&feature=1&count=1&page=" + i;

				URL url2 = new URL(url);
				URLConnection urlConnection = url2.openConnection();
				urlConnection.setDoInput(true);
				
				InputStream inputStream = urlConnection.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(
						inputStream));
				while ((temp = br.readLine()) != null) {
					response += temp;
				}

				tweet = sinaTweetAdapter.convertToTweet(response);
				tweets.add(tweet);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return tweets;
	}

	@Override
	public boolean publish(String content) {
		// TODO Auto-generated method stub
		String destUrl = "https://api.weibo.com/2/statuses/update.json";

		// the post name and value must be used as NameValuePair
		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("access_token", accessToken));
		params.add(new BasicNameValuePair("status", content));

		String response = new PostUtil().publish(destUrl, params);

		if (response.contains("\"created_at\"")) {
			return true;
		} else {
			return false;
		}

	}

}
