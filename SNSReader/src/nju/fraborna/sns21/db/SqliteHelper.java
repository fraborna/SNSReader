package nju.fraborna.sns21.db;

import nju.fraborna.sns21.model.Tweet;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SqliteHelper extends SQLiteOpenHelper {

	public static final String TB_NAME = "tweets";

	public SqliteHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE IF NOT EXISTS " + TB_NAME + "(" + Tweet.ID
				+ " integer primary key," + Tweet.USERID + " varchar,"
				+ Tweet.HEADIMAGE + " varchar," + Tweet.USERNBME + " varchar,"
				+ Tweet.TIMESTAMP + " varchar," + Tweet.FROM + " varchar,"
				+ Tweet.CONTENT + " varchar," + Tweet.ZANNUM + " varchar,"
				+ Tweet.COMMENTNUM + " varchar," + Tweet.FORWARDNUM
				+ " varchar," + "type varchar" + ")");
	
		Log.e("Database", "onCreate");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TB_NAME);
		onCreate(db);
	
		Log.e("Database", "onUpgrade");
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		super.onOpen(db);
	}

	// ¸üÐÂÁÐ
	public void updateColumn(SQLiteDatabase db, String oldColumn,
			String newColumn, String typeColumn) {
		try {
			db.execSQL("ALTER TABLE " + TB_NAME + " CHANGE " + oldColumn + " "
					+ newColumn + " " + typeColumn);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
