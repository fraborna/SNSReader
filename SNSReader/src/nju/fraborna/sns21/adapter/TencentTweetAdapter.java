package nju.fraborna.sns21.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import nju.fraborna.sns21.model.Tweet;

public class TencentTweetAdapter implements TweetAdapter {

	private Tweet tweet = null;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");

	@Override
	public Tweet convertToTweet(String response) {
		// TODO Auto-generated method stub
		String userId = null, userName = null, avatarUrl = null, id = null, time = null, content = null;
		int commentsNum = 0, repostsNum = 0, attitudesNum = 0;
		JSONObject responseJsonObject;

		try {
			responseJsonObject = new JSONObject(response);

			JSONObject data = responseJsonObject.getJSONObject("data");
			JSONArray infoArray = data.getJSONArray("info");
			JSONObject infoObject = infoArray.getJSONObject(0);

			content = infoObject.getString("text");
			repostsNum = infoObject.getInt("count");
			commentsNum = infoObject.getInt("mcount");
			userName = infoObject.getString("nick");
			time = infoObject.getString("timestamp");
			avatarUrl = infoObject.getString("head");
			id = infoObject.getString("id");
			userId = infoObject.getString("openid");
			attitudesNum = 0;

			Date date = new Date(Long.valueOf(time) * 1000);
			date.setHours(date.getHours() + 8);
			time = sdf.format(date);

			tweet = new Tweet(id, userId, avatarUrl, userName, time, "",
					content, attitudesNum, commentsNum, repostsNum);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tweet;
	}

}
