package nju.fraborna.sns21.db;

import android.content.Context;

public class DataBaseContext {

	private static DataHelper dataHelper;

	private static Object INSTANCE_LOCK = new Object();

	public static DataHelper getInstance(Context context) {
		synchronized (INSTANCE_LOCK) {
			if (dataHelper == null) {
				dataHelper = new DataHelper(context);
			}
			return dataHelper;
		}
	}
}
