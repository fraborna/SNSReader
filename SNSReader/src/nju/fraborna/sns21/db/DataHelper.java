package nju.fraborna.sns21.db;

import java.util.ArrayList;
import java.util.List;

import nju.fraborna.sns21.model.Tweet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DataHelper {
	// 数据库名称
	private static String DB_NAME = "snsreader.db";
	// 数据库版本
	private static int DB_VERSION = 1;
	private SQLiteDatabase db;
	private SqliteHelper dbHelper;

	public DataHelper(Context context) {
		// 定义一个SQLite数据库
		dbHelper = new SqliteHelper(context, DB_NAME, null, DB_VERSION);
		db = dbHelper.getWritableDatabase();
	}

	public void Close() {
		db.close();
		dbHelper.close();
	}

	public List<Tweet> getTweetsList(String type) {
		List<Tweet> tweetList = new ArrayList<Tweet>();

		Cursor cursor = db.query(SqliteHelper.TB_NAME, null, "type=?",
				new String[] { type }, null, null, Tweet.ID + " DESC");

		cursor.moveToFirst();

		while (!cursor.isAfterLast() && (cursor.getString(1) != null)) {
			Tweet tweet = new Tweet(cursor.getString(0), cursor.getString(1),
					cursor.getString(2), cursor.getString(3),
					cursor.getString(4), cursor.getString(5),
					cursor.getString(6), cursor.getInt(7), cursor.getInt(8),
					cursor.getInt(9));
			tweetList.add(tweet);
			cursor.moveToNext();
		}

		cursor.close();

		return tweetList;
	}

	public boolean addTweet(ArrayList<Tweet> tweetsList, String type) {
		//delTweet(type);
		
		ContentValues values = new ContentValues();

		for (Tweet tweet : tweetsList) {
			values.put(Tweet.ID, tweet.getId());
			values.put(Tweet.USERID, tweet.getUserId());
			values.put(Tweet.HEADIMAGE, tweet.getHeadImage());
			values.put(Tweet.USERNBME, tweet.getUserName());
			values.put(Tweet.TIMESTAMP, tweet.getTimaStamp());
			values.put(Tweet.FROM, tweet.getFrom());
			values.put(Tweet.CONTENT, tweet.getContent());
			values.put(Tweet.ZANNUM, tweet.getZanNum());
			values.put(Tweet.COMMENTNUM, tweet.getCommentNum());
			values.put(Tweet.FORWARDNUM, tweet.getForwardNum());
			values.put("type", type);

			Long tid = db.insert(SqliteHelper.TB_NAME, Tweet.ID, values);

			Log.e("Save tweet from " + type, tid + "");
			System.out.println("Save tweet from " + type+"  "+tid + "");
		}

		return true;
	}

//	private void delTweet(String type) {
//		int id = db.delete(SqliteHelper.TB_NAME, "type =?",
//				new String[] { type });
//		Log.e("Delete tweets of " + type, id + "");
//	}

}