package nju.fraborna.sns21.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import nju.fraborna.sns21.R;
import nju.fraborna.sns21.model.Tweet;
import nju.fraborna.sns21.netservice.NetService;
import nju.fraborna.sns21.netservice.RenrenService;
import nju.fraborna.sns21.netservice.SinaService;
import nju.fraborna.sns21.netservice.TencentService;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private static final String SNS_RENREN = "人人";
	private static final String SNS_SINA = "新浪微博";
	private static final String SNS_TENCENT = "腾讯微博";

	private static final String ACTION_TWEETS = "新鲜事";
	private static final String ACTION_PUBLISH = "发布";

	private static String CURRENT_SNS = SNS_RENREN;
	private static String CURRENT_ACTION = ACTION_TWEETS;

	private static String ACCESS_TOKEN_RENREN;
	private static String ACCESS_TOKEN_SINA;
	private static String ACCESS_TOKEN_TENCENT;
	private static String OPENID_TENCENT;

	public static RenrenService renrenService;
	public static SinaService sinaService;
	public static TencentService tencentService;

	private TextView renren, sina, tencent;
	private TextView tweets, notices;
	private ListView listView;
	private TextView setting, username;

	private SimpleAdapter simpleAdapter;
	private Handler handler;

	private SharedPreferences preference;

	private ProgressDialog progressDialog;

	private static Boolean isExit = false;

	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);

		setView();
		setListener();
		setBottomColor();

		init();

		if (ACCESS_TOKEN_RENREN.length() == 0) {
			Toast.makeText(MainActivity.this, "请绑定人人账号！", Toast.LENGTH_LONG)
					.show();
		} else {
			// downTweets(renrenService, "renren");
		}

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if (msg.what == 1) {
					progressDialog.dismiss();
					listView.setAdapter(simpleAdapter);
					listView.invalidate();
				}
			}
		};
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		init();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exitBy2Click(); // 调用双击退出函数
		}
		return false;
	}

	/**
	 * 双击退出函数
	 */
	private void exitBy2Click() {
		Timer tExit = null;
		if (isExit == false) {
			isExit = true; // 准备退出
			Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false; // 取消退出
				}
			}, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

		} else {
			finish();
		}
	}

	private void init() {
		preference = getSharedPreferences("activity.OauthRenrenActivity",
				MODE_PRIVATE);
		ACCESS_TOKEN_RENREN = preference.getString("人人令牌", "");
		preference = getSharedPreferences("activity.OauthSinaActivity",
				MODE_PRIVATE);
		ACCESS_TOKEN_SINA = preference.getString("新浪令牌", "");
		preference = getSharedPreferences("activity.OauthTencentActivity",
				MODE_PRIVATE);
		ACCESS_TOKEN_TENCENT = preference.getString("腾讯令牌", "");
		OPENID_TENCENT = preference.getString("腾讯用户标识", "");

		renrenService = new RenrenService(ACCESS_TOKEN_RENREN);
		sinaService = new SinaService(ACCESS_TOKEN_SINA);
		tencentService = new TencentService(ACCESS_TOKEN_TENCENT,
				OPENID_TENCENT);
	}

	private void setView() {
		renren = (TextView) findViewById(R.id.sns_renren);
		sina = (TextView) findViewById(R.id.sns_sinaweibo);
		tencent = (TextView) findViewById(R.id.sns_tencentweibo);

		tweets = (TextView) findViewById(R.id.bottom_tweets);
		notices = (TextView) findViewById(R.id.bottom_notices);

		listView = (ListView) findViewById(R.id.main_list);

		setting = (TextView) findViewById(R.id.top_panel_setting);
		username = (TextView) findViewById(R.id.top_panel_username);
		username.setText(CURRENT_SNS);
	}

	private void setListener() {
		tweets.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				CURRENT_ACTION = ACTION_TWEETS;
				listView.setVisibility(View.VISIBLE);
				tweets.setClickable(true);
				tweets.setFocusable(true);
				setBottomColor();
				if (CURRENT_SNS.equals(SNS_RENREN)) {
					downTweets(renrenService, "renren");
				} else if (CURRENT_SNS.equals(SNS_SINA)) {
					downTweets(sinaService, "sina");
				} else if (CURRENT_SNS.equals(SNS_TENCENT)) {
					downTweets(tencentService, "tencent");
				}
			}
		});

		renren.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				CURRENT_SNS = SNS_RENREN;
				renren.setClickable(true);
				renren.setFocusable(true);
				username.setText("人人");
				setBottomColor();
				if (ACCESS_TOKEN_RENREN.length() == 0) {
					Toast.makeText(MainActivity.this, "请绑定人人账号！",
							Toast.LENGTH_LONG).show();
				} else {
					downTweets(renrenService, "renren");
				}
			}
		});

		sina.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				CURRENT_SNS = SNS_SINA;
				sina.setClickable(true);
				sina.setFocusable(true);
				username.setText("新浪微博");
				setBottomColor();
				if (ACCESS_TOKEN_SINA.length() == 0) {
					Toast.makeText(MainActivity.this, "请绑定新浪微博账号！",
							Toast.LENGTH_LONG).show();
				} else {
					downTweets(sinaService, "sina");
				}
			}
		});

		tencent.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				CURRENT_SNS = SNS_TENCENT;
				tencent.setClickable(true);
				tencent.setFocusable(true);
				username.setText("腾讯微博");
				setBottomColor();
				if (ACCESS_TOKEN_TENCENT.length() == 0
						|| OPENID_TENCENT.length() == 0) {
					Toast.makeText(MainActivity.this, "请绑定腾讯微博账号！",
							Toast.LENGTH_LONG).show();
				} else {
					downTweets(tencentService, "tencent");
				}
			}
		});

		setting.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				setting.setClickable(true);
				setting.setFocusable(true);
				Intent intent = new Intent(MainActivity.this,
						SettingActivity.class);
				startActivity(intent);
			}
		});

		// listView.setOnItemClickListener(new OnItemClickListener() {
		// @Override
		// public void onItemClick(AdapterView<?> parent, View arg1,
		// int position, long id) {
		// // TODO Auto-generated method stub
		// ListView lView = (ListView) parent;
		// @SuppressWarnings("unchecked")
		// Map<String, Object> map = (Map<String, Object>) lView
		// .getItemAtPosition(position);
		// Bundle bundle = new Bundle();
		// Intent intent = new Intent(MainActivity.this,
		// TweetActivity.class);
		// bundle.putString("username", (String) map.get("username"));
		// bundle.putString("timestamp", (String) map.get("timestamp"));
		// bundle.putString("content", (String) map.get("content"));
		// bundle.putString("sns", CURRENT_SNS);
		//
		// intent.putExtras(bundle);
		// startActivity(intent);
		// }
		// });

	}

	private void setBottomColor() {
		renren.setTextColor(getResources().getColor(R.color.bottom_text_normal));
		sina.setTextColor(getResources().getColor(R.color.bottom_text_normal));
		tencent.setTextColor(getResources()
				.getColor(R.color.bottom_text_normal));
		tweets.setTextColor(getResources().getColor(R.color.bottom_text_normal));
		notices.setTextColor(getResources()
				.getColor(R.color.bottom_text_normal));

		renren.setBackgroundResource(R.color.bottom_back_normal);
		sina.setBackgroundResource(R.color.bottom_back_normal);
		tencent.setBackgroundResource(R.color.bottom_back_normal);
		tweets.setBackgroundResource(R.color.bottom_back_normal);
		notices.setBackgroundResource(R.color.bottom_back_normal);

		if (CURRENT_ACTION.equals(ACTION_TWEETS)) {
			tweets.setTextColor(getResources().getColor(
					R.color.bottom_text_pressed));
			tweets.setBackgroundResource(R.color.bottom_back_pressed);
		} else if (CURRENT_ACTION.equals(ACTION_PUBLISH)) {
			notices.setTextColor(getResources().getColor(
					R.color.bottom_text_pressed));
			notices.setBackgroundResource(R.color.bottom_back_pressed);
		}

		if (CURRENT_SNS.equals(SNS_RENREN)) {
			renren.setTextColor(getResources().getColor(
					R.color.bottom_text_pressed));
			renren.setBackgroundResource(R.color.bottom_back_pressed);
		} else if (CURRENT_SNS.equals(SNS_SINA)) {
			sina.setTextColor(getResources().getColor(
					R.color.bottom_text_pressed));
			sina.setBackgroundResource(R.color.bottom_back_pressed);
		} else if (CURRENT_SNS.equals(SNS_TENCENT)) {
			tencent.setTextColor(getResources().getColor(
					R.color.bottom_text_pressed));
			tencent.setBackgroundResource(R.color.bottom_back_pressed);
		}
	}

	private void downTweets(final NetService netService, final String type) {
		progressDialog = ProgressDialog.show(this, "请稍后", "正在死命加载>-<...");
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				final ArrayList<Tweet> tweets = netService.getTweets(20);
				List<Map<String, Object>> tweetsList = new ArrayList<Map<String, Object>>();
				for (int i = 0; i < tweets.size(); i++) {
					Tweet t = tweets.get(i);
					Map<String, Object> listItem = new HashMap<String, Object>();
					listItem.put("username", t.getUserName());
					listItem.put("timestamp", t.getTimaStamp());
					listItem.put("content", t.getContent());
					tweetsList.add(listItem);
				}
				simpleAdapter = new SimpleAdapter(MainActivity.this,
						tweetsList, R.layout.tweets_list_item, new String[] {
								"username", "timestamp", "content" },
						new int[] { R.id.tweets_item_username,
								R.id.tweets_item_timestamp,
								R.id.tweets_item_content });

				Message msg = new Message();
				msg.what = 1;
				handler.sendMessage(msg);
			}
		}).start();
	}

	// private void initTweets(String type) {
	// DataHelper dataHelper = DataBaseContext.getInstance(this);
	// List<Tweet> tweetsList = dataHelper.getTweetsList(type);
	// List<Map<String, Object>> tweets = new ArrayList<Map<String, Object>>();
	//
	// for (int i = 0; i < tweetsList.size(); i++) {
	// Tweet t = tweetsList.get(i);
	// Map<String, Object> listItem = new HashMap<String, Object>();
	// listItem.put("headimage", t.getHeadImage());
	// listItem.put("username", t.getUserName());
	// listItem.put("timestamp", t.getTimaStamp());
	// listItem.put("content", t.getContent());
	// tweets.add(listItem);
	// }
	//
	// simpleAdapter = new SimpleAdapter(MainActivity.this, tweets,
	// R.layout.tweets_list_item, new String[] { "headimage",
	// "username", "timestamp", "content", "commentsNum" },
	// new int[] { R.id.tweets_item_headimage,
	// R.id.tweets_item_username, R.id.tweets_item_timestamp,
	// R.id.tweets_item_content });
	//
	// listView.setAdapter(simpleAdapter);
	// listView.invalidate();
	// }
}
