package nju.fraborna.sns21.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import nju.fraborna.sns21.R;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class OauthRenrenActivity extends Activity {

	private WebView webView;
	private SharedPreferences preferences;

	private static final String TAG = "人人网认证";
	private static final String APP_ID = "267123";
	private static final String APP_KEY = "99bad0611e0242c6bb2ce014e5fe6a5c";
	private static final String SECRET_KEY = "5b696fb160e942548568d1c735793f61";

	private static final String ACCESS_TOKEN = "人人令牌";
	private static final String EXPIRES_IN = "人人令牌有效期";

	private String access_token = "";
	private String expires_in = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login_sina);

		webView = (WebView) findViewById(R.id.webview);

		// 对WebView进行设置（对JS的支持，对缩放的支持，对缓存模式的支持）
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setBuiltInZoomControls(true);
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

		// 根据client_id取得到人人服务器人人对你的应用授权，如果成功则返回人人网登陆页面的html文件，并在WebView控件上显示
		// 此时用户需要输入自己人人账号的用户名、密码并点击登陆
		webView.loadUrl("https://graph.renren.com/oauth/authorize?"
				+ "client_id="
				+ APP_KEY
				+ "&response_type=token"
				+ "&scope=read_user_feed,publish_feed&display=touch&redirect_uri=http://graph.renren.com/oauth/login_success.html&x_renew=true");

		webView.setWebViewClient(new WebViewClient() {

			// 点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onReceivedSslError(WebView view,
					SslErrorHandler handler, SslError error) {
				// TODO Auto-generated method stub
				handler.proceed();// 让webview处理https请求
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				String url0 = webView.getUrl();
				Log.i(TAG, "URL = " + url0);
				if (url0 != null) {
					if (url0.contains("access_token=")) { // 从URL中解析得到
															// access_token
						access_token = url0.substring(
								url0.indexOf("access_token=") + 13,
								url0.indexOf("expires_in") - 1);
						try {
							access_token = URLDecoder.decode(access_token,
									"utf-8"); // 制定为utf-8编码
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
						Log.i(TAG, "access_token = " + access_token);
					}
					if (url0.contains("expires_in=")) { // 从URL中解析得到 expires_in
						expires_in = url0.substring(
								url0.indexOf("expires_in=") + 11,
								url0.indexOf("scope") - 1);
						Log.i(TAG, "expires_in = " + expires_in);
					}

					preferences = getPreferences(MODE_APPEND);
					Editor editor = preferences.edit();
					editor.clear();
					editor.putString(ACCESS_TOKEN, access_token);// 将解析得到的access_token
																	// 保存起来
					editor.putString(EXPIRES_IN, expires_in);// 将解析得到的expires_in保存起来
					editor.commit();

					// 输入用户名、密码登陆成功，进行页面跳转
					if (access_token.length() != 0) {
						Log.i(TAG, "Binding Renren Successed!");
						Toast.makeText(OauthRenrenActivity.this, "绑定人人账号成功!",
								Toast.LENGTH_SHORT).show();
						OauthRenrenActivity.this.finish();
					}
				}
				super.onPageFinished(view, url);
			}
		});
	}
}
