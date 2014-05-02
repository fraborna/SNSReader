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

public class OauthTencentActivity extends Activity {
	private WebView webView;
	private SharedPreferences preferences;

	private static final String TAG = "腾讯微博认证";
	private static final String APP_SECRET = "c38170a77887ae70c04a1f14554d3683";
	private static final String APP_KEY = "801499919";

	private static final String ACCESS_TOKEN = "腾讯令牌";
	private static final String EXPIRES_IN = "腾讯令牌有效期";
	private static final String OPENID = "腾讯用户标识";

	private String access_token = "";
	private String expires_in = "";
	private String openid = "";

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
		webView.loadUrl("https://open.t.qq.com/cgi-bin/oauth2/authorize?"
				+ "client_id="
				+ APP_KEY
				+ "&response_type=token"
				+ "&redirect_uri=http://wiki.open.t.qq.com/index.php/&forcelogin=true");

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
								url0.indexOf("openid") - 1);
						Log.i(TAG, "expires_in = " + expires_in);
					}
					if (url0.contains("openid=")) { // 从URL中解析得到 openid
						openid = url0.substring(url0.indexOf("openid=") + 7,
								url0.indexOf("openkey=") - 1);
						Log.i(TAG, "openid = " + openid);
					}

					preferences = getPreferences(MODE_APPEND);
					Editor editor = preferences.edit();
					editor.clear();
					editor.putString(ACCESS_TOKEN, access_token);// 将解析得到的access_token
																	// 保存起来
					editor.putString(EXPIRES_IN, expires_in);// 将解析得到的expires_in保存起来
					editor.putString(OPENID, openid);
					editor.commit();

					// 输入用户名、密码登陆成功，进行页面跳转
					if (access_token.length() != 0) {
						Log.i(TAG, "Binding Renren Successed!");
						Toast.makeText(OauthTencentActivity.this,
								"绑定腾讯微博账号成功!", Toast.LENGTH_SHORT).show();
						OauthTencentActivity.this.finish();
					}
				}
				super.onPageFinished(view, url);
			}
		});
	}
}
