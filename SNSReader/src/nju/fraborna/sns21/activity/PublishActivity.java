package nju.fraborna.sns21.activity;

import nju.fraborna.sns21.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class PublishActivity extends Activity {

	private EditText editText;
	private CheckBox checkBoxRenren, checkBoxSina, checkBoxTencent;
	private Button publishButton;

	private Handler handler;

	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.publish);

		setView();
		setListener();

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if (msg.what == 1) {
					Toast.makeText(PublishActivity.this, "发布成功！",
							Toast.LENGTH_LONG).show();
					PublishActivity.this.finish();
				} else {
					Toast.makeText(PublishActivity.this, msg.obj.toString(),
							Toast.LENGTH_LONG).show();
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

	private void setView() {
		editText = (EditText) findViewById(R.id.publish_text);
		checkBoxRenren = (CheckBox) findViewById(R.id.publish_renren);
		checkBoxSina = (CheckBox) findViewById(R.id.publish_sina);
		checkBoxTencent = (CheckBox) findViewById(R.id.publish_tencent);
		publishButton = (Button) findViewById(R.id.publish_button);
	}

	private void setListener() {
		publishButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				final boolean renrenChecked = checkBoxRenren.isChecked();
				final boolean sinaChecked = checkBoxSina.isChecked();
				final boolean tencentChecked = checkBoxTencent.isChecked();
				final String content = editText.getText().toString();

				if (!renrenChecked && !sinaChecked && !tencentChecked) {
					Toast.makeText(PublishActivity.this, "怎么着也得选一个吖亲╭(╯3╰╮",
							Toast.LENGTH_LONG).show();
				} else {
					new Thread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							boolean renrenOK = false;
							boolean sinaOK = false;
							boolean tencentOK = false;
							if (renrenChecked) {
								renrenOK = MainActivity.renrenService
										.publish(content);
							} else {
								renrenOK = true;
							}
							if (sinaChecked) {
								sinaOK = MainActivity.sinaService
										.publish(content);
							} else {
								sinaOK = true;
							}
							if (tencentChecked) {
								tencentOK = MainActivity.tencentService
										.publish(content);
							} else {
								tencentOK = true;
							}
							if (renrenOK && sinaOK && tencentOK) {
								Message msg = new Message();
								msg.what = 1;
								handler.sendMessage(msg);
							} else {
								String info = "";
								if (!renrenOK)
									info += "人人网、";
								if (!sinaOK)
									info += "新浪微博、";
								if (!tencentOK)
									info += "腾讯微博、";
								info = info.substring(0, info.length() - 1);
								info += "新鲜事发布失败!\n网络连接了没？账号绑定了没？表逗比哦~";
								Message msg = new Message();
								msg.obj = info;
								handler.sendMessage(msg);
							}
						}
					}).start();
				}
			}
		});
	}

}
