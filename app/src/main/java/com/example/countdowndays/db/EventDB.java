package com.example.countdowndays.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.countdowndays.model.Event;

import java.util.ArrayList;
import java.util.List;

public class EventDB {
    public static  final String DB_NAME = "event_db2";
    public static final int VERSION = 1;
    private static EventDB eventDB;
    private SQLiteDatabase db;
    private EventDB(Context context){
        EventOpenHelper eventOpenHelper = new EventOpenHelper(context,DB_NAME,null,VERSION);
        db = eventOpenHelper.getWritableDatabase();
    }

    public synchronized static EventDB getInstance(Context context){
        if(eventDB == null){
            eventDB  = new EventDB(context);
        }
        return eventDB;
    }

    public void saveEvent(Event event){
        if(event != null){
            ContentValues values = new ContentValues();
            values.put("title",event.getTitle());
            values.put("color",event.getColor());
            values.put("note",event.getNote());
            values.put("date",event.getDate());
            values.put("bgm",event.getBgm());
            values.put("notidate",event.getNotidate());
            db.insert("event",null,values);
        }
    }
    public void updateEvent(Event event){

        ContentValues values = new ContentValues();
        values.put("color", event.getColor());
        values.put("note", event.getNote());
        values.put("date", event.getDate());
        values.put("title", event.getTitle());
        values.put("bgm",event.getBgm());
        values.put("notidate",event.getNotidate());
        db.update("event", values, "id=?", new String[]{String.valueOf(event.getId())});

    }
    public void deleteEvent(Event e){
        db.delete("event","id=?", new String[]{String.valueOf(e.getId())});
    }

    public List<Event> loadEvent(){
        List<Event> list = new ArrayList<Event>();
        Cursor cursor = db.query("event",null,null,null,null,null,"id",null);
        Log.d("hihihi","b");
        if(cursor.moveToLast()){
            do{
                Log.d("hihihi","a");
                Event event = new Event();
                event.setId(cursor.getInt(cursor.getColumnIndex("id")));
                event.setColor(cursor.getInt(cursor.getColumnIndex("color")));
                event.setNote(cursor.getString(cursor.getColumnIndex("note")));
                event.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                event.setDate(cursor.getLong(cursor.getColumnIndex("date")));
                event.setBgm(cursor.getInt(cursor.getColumnIndex("bgm")));
                event.setNotidate(cursor.getLong(cursor.getColumnIndex("notidate")));
                list.add(event);
            }while(cursor.moveToPrevious());
            if(cursor!=null){
                cursor.close();
            }
        }
        return list;
    }

}