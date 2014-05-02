package nju.fraborna.sns21.activity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;

import org.json.JSONException;
import org.json.JSONObject;

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

public class OauthSinaActivity extends Activity {

	private WebView webView;
	private SharedPreferences preferences;

	private static final String TAG = "新浪微博认证";
	private static final String APP_KEY = "2587784359";
	private static final String APP_SECRET = "77e76caec6a5f25acd51ca022562a8a5";

	private static final String ACCESS_TOKEN = "新浪令牌";
	private static final String EXPIRES_IN = "新浪令牌有效期";

	private String code = "";
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
		webView.loadUrl("https://api.weibo.com/oauth2/authorize?"
				+ "client_id="
				+ APP_KEY
				+ "&response_type=code"
				+ "&scope=all&display=mobile&redirect_uri=http://open.weibo.com/&forcelogin=true");

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
					if (url0.contains("code=")) { // 从URL中解析得到code
						code = url0.substring(url0.indexOf("code=") + 5,
								url0.length());
						try {
							code = URLDecoder.decode(code, "utf-8"); // 制定为utf-8编码
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
						Log.i(TAG, "code = " + code);
						if (code.length() != 0) {
							new Thread(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									try {
										String parms = "client_id="
												+ APP_KEY
												+ "&client_secret="
												+ APP_SECRET
												+ "&grant_type=authorization_code"
												+ "&code="
												+ code
												+ "&redirect_uri=http://open.weibo.com/";
										byte[] data = parms.getBytes();
										URL url2 = new URL(
												"https://api.weibo.com/oauth2/access_token");
										HttpURLConnection httpURLConnection = (HttpURLConnection) url2
												.openConnection();
										httpURLConnection
												.setConnectTimeout(3000);
										httpURLConnection
												.setRequestMethod("POST"); // 以post请求方式提交
										httpURLConnection.setDoInput(true); // 读取数据
										httpURLConnection.setDoOutput(true); // 向服务器写数据
										// 设置post请求必要的请求头
										httpURLConnection
												.setRequestProperty(
														"Content-Type",
														"application/x-www-form-urlencoded");// 请求头,
																								// 必须设置
										httpURLConnection.setRequestProperty(
												"Content-Length",
												String.valueOf(data.length));// 注意是字节长度,
																				// 不是字符长度
										// 获得输出流，向服务器输出内容
										OutputStream outputStream = httpURLConnection
												.getOutputStream();
										// 写入数据
										outputStream
												.write(data, 0, data.length);
										outputStream.close();
										// 获得服务器响应结果和状态码
										int responseCode = httpURLConnection
												.getResponseCode();
										if (responseCode == 200) {
											// 取回响应的结果
											String result = changeInputStream(
													httpURLConnection
															.getInputStream(),
													"UTF-8");
											JSONObject jsonObject = new JSONObject(
													result);
											String access_token = jsonObject
													.getString("access_token");
											String expires_in = jsonObject
													.getString("expires_in");
											preferences = getPreferences(MODE_APPEND);
											Editor editor = preferences.edit();
											editor.clear();
											editor.putString(ACCESS_TOKEN,
													access_token);// 将解析得到的access_token
											// 保存起来
											editor.putString(EXPIRES_IN,
													expires_in);// 将解析得到的expires_in保存起来
											editor.commit();
											Log.i("sina",
													"accrss_token has got!");
										}
									} catch (MalformedURLException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}).start();
							OauthSinaActivity.this.finish();
						}
					}
					super.onPageFinished(view, url);
				}
			}
		});
	}

	/**
	 * 将一个输入流转换成指定编码的字符串
	 * 
	 * @param inputStream
	 * @param encode
	 * @return
	 */
	private String changeInputStream(InputStream inputStream, String encode) {

		// 内存流
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		byte[] data = new byte[1024];
		int len = 0;
		String result = null;
		if (inputStream != null) {
			try {
				while ((len = inputStream.read(data)) != -1) {
					byteArrayOutputStream.write(data, 0, len);
				}
				result = new String(byteArrayOutputStream.toByteArray(), encode);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}
