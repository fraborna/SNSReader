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

import nju.fraborna.sns21.adapter.TencentTweetAdapter;
import nju.fraborna.sns21.model.Tweet;

public class TencentService implements NetService {

	private static final String APP_KEY = "801499919";

	private String accessToken, openid;
	private TencentTweetAdapter tencentTweetAdapter = new TencentTweetAdapter();
	private ArrayList<Tweet> tweets = new ArrayList<Tweet>();

	public TencentService(String accessToken, String openid) {
		super();
		this.accessToken = accessToken;
		this.openid = openid;
	}

	@Override
	public ArrayList<Tweet> getTweets(int num) {
		// TODO Auto-generated method stub
		String tempTime = "0";
		tweets.removeAll(tweets);
		for (int i = 1; i <= num; i++) {
			Tweet tweet = null;
			try {
				String response = "";
				String temp = "";
				String url = "https://open.t.qq.com/api/statuses/home_timeline?format=json&pageflag=1&pagetime="
						+ tempTime
						+ "&reqnum=1&type=1&contenttype=1&oauth_consumer_key="
						+ APP_KEY
						+ "&access_token="
						+ accessToken
						+ "&openid="
						+ openid + "&oauth_version=2.a&scope=all";

				URL url2 = new URL(url);
				URLConnection urlConnection = url2.openConnection();
				urlConnection.setDoInput(true);
				
				InputStream inputStream = urlConnection.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(
						inputStream));
				while ((temp = br.readLine()) != null) {
					response += temp;
				}
				if (response.length() > 200) {
					tweet = tencentTweetAdapter.convertToTweet(response);
					tempTime = tweet.getTimaStamp();
					tweets.add(tweet);
				}
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
		String destUrl = "https://open.t.qq.com/api/t/add";

		// the post name and value must be used as NameValuePair

		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("oauth_consumer_key", APP_KEY));
		params.add(new BasicNameValuePair("access_token", accessToken));
		params.add(new BasicNameValuePair("openid", openid));
		params.add(new BasicNameValuePair("oauth_version", "2.a"));
		params.add(new BasicNameValuePair("scope", "all"));
		params.add(new BasicNameValuePair("format", "json"));
		params.add(new BasicNameValuePair("content", content));

		String response = new PostUtil().publish(destUrl, params);

		if (response.contains("\"msg\":\"ok\"")) {
			return true;
		} else {
			return false;
		}

	}
}