package nju.fraborna.sns21.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import nju.fraborna.sns21.model.Tweet;

public class SinaTweetAdapter implements TweetAdapter {

	private Tweet tweet = null;
	SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd kk:mm:ss zzz yyyy");
	SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");

	@Override
	public Tweet convertToTweet(String response) {
		// TODO Auto-generated method stub
		String userId = null, userName = null, avatarUrl = null, id = null, time = null, content = null;
		int commentsNum = 0, repostsNum = 0, attitudesNum = 0;
		JSONObject responseJsonObject;

		try {
			responseJsonObject = new JSONObject(response);

			JSONArray responseJsonArray = responseJsonObject
					.getJSONArray("statuses");
			JSONObject resource = responseJsonArray.getJSONObject(0);
			JSONObject sourceUser = resource.getJSONObject("user");

			time = resource.getString("created_at");
			id = resource.getString("id");
			content = resource.getString("text");
			userId = sourceUser.getString("id");
			userName = sourceUser.getString("name");
			avatarUrl = sourceUser.getString("profile_image_url");
			commentsNum = resource.getInt("comments_count");
			repostsNum = resource.getInt("reposts_count");
			attitudesNum = resource.getInt("attitudes_count");

			Date date = sdf.parse(time);
			date.setHours(date.getHours() + 8);
			time = sdf2.format(date);

			tweet = new Tweet(id, userId, avatarUrl, userName, time, "",
					content, attitudesNum, commentsNum, repostsNum);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tweet;
	}

}
