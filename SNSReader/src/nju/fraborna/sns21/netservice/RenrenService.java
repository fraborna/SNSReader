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

import nju.fraborna.sns21.adapter.RenrenTweetAdapter;
import nju.fraborna.sns21.model.Tweet;

public class RenrenService implements NetService {

	private String accessToken;
	private RenrenTweetAdapter renrenTweetAdapter = new RenrenTweetAdapter();
	private ArrayList<Tweet> tweets = new ArrayList<Tweet>();

	public RenrenService(String accessToken) {
		super();
		this.accessToken = accessToken;
	}

	public ArrayList<Tweet> getTweets(int num) {
		tweets.removeAll(tweets);
		for (int i = 1; i <= num; i++) {
			Tweet tweet = null;
			try {
				String response = "";
				String temp = "";
				String url = "https://api.renren.com/v2/feed/list?access_token="
						+ accessToken
						+ "&feedType=UPDATE_STATUS&pageSize=1&pageNumber=" + i;

				URL url2 = new URL(url);
				URLConnection urlConnection = url2.openConnection();
				urlConnection.setDoInput(true);

				InputStream inputStream = urlConnection.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(
						inputStream));
				while ((temp = br.readLine()) != null) {
					response += temp;
				}

				if (response.length() < 30) {
					num++;
					continue;
				} else {
					tweet = renrenTweetAdapter.convertToTweet(response);
					tweets.add(tweet);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return tweets;
	}

	public ArrayList<Tweet> getMoreTweets() {
		tweets.removeAll(tweets);

		return tweets;
	}

	@Override
	public boolean publish(String content) {
		// TODO Auto-generated method stub
		String destUrl = "https://api.renren.com/v2/status/put";

		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("access_token", accessToken));
		params.add(new BasicNameValuePair("content", content));

		String response = new PostUtil().publish(destUrl, params);

		if (response.contains("response")) {
			return true;
		} else {
			return false;
		}

	}

}
