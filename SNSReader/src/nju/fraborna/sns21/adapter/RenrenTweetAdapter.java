package nju.fraborna.sns21.adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import nju.fraborna.sns21.model.Tweet;

public class RenrenTweetAdapter implements TweetAdapter {

	private Tweet tweet = null;

	@Override
	public Tweet convertToTweet(String response) {
		// TODO Auto-generated method stub
		String userId = null, userName = null, avatarUrl = null, id = null, time = null, content = null;
		int commentsNum = 0;
		JSONObject responseJsonObject;
		
		try {
			responseJsonObject = new JSONObject(response);

			JSONArray responseJsonArray = responseJsonObject
					.getJSONArray("response");
			JSONObject resource = responseJsonArray.getJSONObject(0);
			JSONObject sourceUser = resource.getJSONObject("sourceUser");
			JSONArray avatarJsonArray = sourceUser.getJSONArray("avatar");
			JSONObject avatarJsonObject = avatarJsonArray.getJSONObject(0);
			JSONObject resourceJSON = resource.getJSONObject("resource");

			userId = sourceUser.getString("id");
			userName = sourceUser.getString("name");
			avatarUrl = avatarJsonObject.getString("url");
			id = resource.getString("id");
			time = resource.getString("time");
			content = resourceJSON.getString("content");

			String cString = resource.getString("comments");
			if (cString.length() > 10) {
				JSONArray comments = resource.getJSONArray("comments");
				commentsNum = comments.length();
			}

			tweet = new Tweet(id, userId, avatarUrl, userName, time, "",
					content, 0, commentsNum, 0);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tweet;
	}

}
