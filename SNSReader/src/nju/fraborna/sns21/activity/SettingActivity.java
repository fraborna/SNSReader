package nju.fraborna.sns21.activity;

import nju.fraborna.sns21.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends Activity {
	private static final String SNS_RENREN = "人人";
	private static final String SNS_SINA = "新浪微博";
	private static final String SNS_TENCENT = "腾讯微博";

	private Button renrenBind, sinaBind, tencentBind;
	private Button renrenUnbind, sinaUnbind, tencentUnbind;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.accounts_binding);

		setView();
		setListener();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void setView() {
		renrenBind = (Button) findViewById(R.id.bind_account_renren_bind);
		renrenUnbind = (Button) findViewById(R.id.bind_account_renren_unbind);
		sinaBind = (Button) findViewById(R.id.bind_account_sina_bind);
		sinaUnbind = (Button) findViewById(R.id.bind_account_sina_unbind);
		tencentBind = (Button) findViewById(R.id.bind_account_tencent_bind);
		tencentUnbind = (Button) findViewById(R.id.bind_account_tencent_unbind);
	}

	private void setListener() {

		renrenBind.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SettingActivity.this,
						OauthRenrenActivity.class);
				startActivity(intent);
			}
		});

		sinaBind.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SettingActivity.this,
						OauthSinaActivity.class);
				startActivity(intent);
			}
		});

		tencentBind.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SettingActivity.this,
						OauthTencentActivity.class);
				startActivity(intent);
			}
		});

		renrenUnbind.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SharedPreferences preference = getSharedPreferences(
						"activity.OauthRenrenActivity", MODE_PRIVATE);
				Editor editor = preference.edit();
				editor.clear();
				editor.commit();
				Toast.makeText(SettingActivity.this, "解除绑定成功！",
						Toast.LENGTH_SHORT).show();
			}
		});

		sinaUnbind.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SharedPreferences preference = getSharedPreferences(
						"activity.OauthSinaActivity", MODE_PRIVATE);
				Editor editor = preference.edit();
				editor.clear();
				editor.commit();
				Toast.makeText(SettingActivity.this, "解除绑定成功！",
						Toast.LENGTH_SHORT).show();
			}
		});

		tencentUnbind.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SharedPreferences preference = getSharedPreferences(
						"activity.OauthTencentActivity", MODE_PRIVATE);
				Editor editor = preference.edit();
				editor.clear();
				editor.commit();
				Toast.makeText(SettingActivity.this, "解除绑定成功！",
						Toast.LENGTH_SHORT).show();
			}
		});
	}
}
