package usp.ime.movel.brickbreaker.model;

import java.lang.reflect.Method;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {

	// Database Version
	private static final int DATABASE_VERSION = 6;
	// Database Name
	private static final String DATABASE_NAME = "OuvidoriaDB";
	// Table Name
	private static final String SCORE_TABLE = "scores";

	public SQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// SQL statement to create book table
		String middleQueryCode = "";

		ScoreKey[] keys = ScoreKey.values();
		for (int i = 0; i < keys.length; i++) {
			middleQueryCode += keys[i].getColumnName() + " "
					+ keys[i].getType();
			if (i + 1 < keys.length)
				middleQueryCode += ", ";
			else
				middleQueryCode += " )";
		}
		String createTable = "CREATE TABLE " + SCORE_TABLE + " ( " + middleQueryCode;
		db.execSQL(createTable);
	}

	public void resetTables(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + SCORE_TABLE);
		// create fresh books table
		this.onCreate(db);
	}

	public void resetTables() {
		resetTables(getWritableDatabase());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older tables if existed
		if (oldVersion <= 4)
			db.execSQL("DROP TABLE IF EXISTS score");
		resetTables(db);
	}

	// --------------------------------------------------------------------------

	enum ScoreKey {
		ID("INTEGER PRIMARY KEY AUTOINCREMENT"), SCORE("LONG");
		private String columnName;
		private String type;
		private Method getter;
		private Method setter;

		ScoreKey(String type) {
			this.columnName = this.name().toLowerCase();
			this.type = type;
			for (Method method : Score.class.getMethods()) {
				if (("get" + this.columnName).equals(method.getName()
						.toLowerCase()))
					getter = method;
				if (("set" + this.columnName).equals(method.getName()
						.toLowerCase()))
					setter = method;
			}
			if (getter == null)
				Log.e("IncidentKey", "Not found: " + "get" + this.columnName);
			if (setter == null)
				Log.e("IncidentKey", "Not found: " + "set" + this.columnName);
		}

		String getColumnName() {
			return this.columnName;
		}

		String getType() {
			return this.type;
		}

		Object get(Score score) {
			try {
				return getter.invoke(score);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		void set(Score score, Object value) {
			try {
				setter.invoke(score, value);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// Get All Books
	public Score getHigherScore() {

		// 1. build the query
		String query = "SELECT MAX(score) FROM " + SCORE_TABLE;

		// 2. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		// 3. go over each row, build book and add it to list
		Score score = null;
		if (cursor.moveToFirst()) {
			score = new Score();
			ScoreKey[] keys = ScoreKey.values();
			for (int i = 0; i < keys.length; i++) {
				if (cursor.isNull(i))
					continue;
				else if (keys[i].getType() == "LONG")
					keys[i].set(score, cursor.getString(i));
			}
		}

		Log.d("getHigherScore()", score.toString());

		return score;
	}

	private ContentValues makeValues(Score score) {
		ContentValues values = new ContentValues();
		ScoreKey[] keys = ScoreKey.values();
		for (int i = 1; i < keys.length; i++) {
			if (keys[i].getType() == "LONG")
				values.put(keys[i].getColumnName(),
						(Long) keys[i].get(score));
		}
		return values;
	}

	public Long addScore(Score score) {
		long id = -1;
		// 1. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();
		// 2. insert
		id = db.insert(SCORE_TABLE, // table
				null, // nullColumnHack
				makeValues(score)); // key/value -> keys = column names/
										// values = column values
		// 3. close
		db.close();
		Log.d("addIncident()", Long.toString(id));
		return Long.valueOf(id);
	}

	public void updateScore(Score score) {
		// 1. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();
		// 3. updating row
		db.update(SCORE_TABLE, // table
				makeValues(score), // column/value
				ScoreKey.ID.getColumnName() + " = ?", // selections
				new String[] { String.valueOf(score.getId()) }); // selection
																	// args
		// 4. close
		db.close();
		Log.d("updateIncident()", score.getId().toString());
	}

	public void removeScore(Score score) {
		// 1. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();
		// 2. delete
		db.delete(SCORE_TABLE, ScoreKey.ID.getColumnName()
				+ " = ?", new String[] { String.valueOf(score.getId()) });
		// 3. close
		db.close();
	}

}