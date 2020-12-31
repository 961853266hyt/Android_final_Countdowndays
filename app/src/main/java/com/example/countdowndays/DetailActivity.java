package com.example.countdowndays;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.example.countdowndays.db.EventDB;
import com.example.countdowndays.model.Event;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class DetailActivity extends AppCompatActivity {
    private TextView detail_title,detail_note,detail_days,detail_sol,detail_show_song,detail_show_time;

    private Button detail_edit_delete,detail_edit_ok,edit_date,detail_ok,detail_edit,edit_noti_date;
    private RadioGroup edit_color_group;
    private EditText edit_title,edit_note;
    private RelativeLayout detail_show_layout;
    private LinearLayout detail_edit_layout;
    private Calendar calendar;
    private Calendar calendar_noti;  //提醒日期
    private Event e;
    private Spinner detail_spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        detail_show_layout = (RelativeLayout)findViewById(R.id.detail_show_layout);
        detail_edit_layout = (LinearLayout)findViewById(R.id.detail_edit_layout);
        detail_title = (TextView)findViewById(R.id.detail_title);
        detail_note = (TextView)findViewById(R.id.detail_note);
        detail_days = (TextView)findViewById(R.id.detail_days);
        detail_sol = (TextView)findViewById(R.id.detail_since_or_left);
        detail_ok = (Button)findViewById(R.id.detail_ok);
        detail_edit = (Button)findViewById(R.id.detail_edit);
        detail_spinner = (Spinner)findViewById(R.id.detail_spinner);
        detail_show_song = (TextView)findViewById(R.id.show_song);
        detail_show_time = (TextView)findViewById(R.id.detail_show_time);

        detail_edit_delete = (Button)findViewById(R.id.detail_edit_delete);
        detail_edit_ok = (Button)findViewById(R.id.detail_edit_ok);
        edit_date = (Button)findViewById(R.id.edit_date);
        edit_noti_date = (Button)findViewById(R.id.edit_noti_date);

        edit_color_group = (RadioGroup)findViewById(R.id.edit_color_group);
        edit_title = (EditText)findViewById(R.id.edit_title);
        edit_note = (EditText)findViewById(R.id.edit_note);


        Intent i = getIntent();
        e = (Event)i.getSerializableExtra("EVENT");
        Date now = new Date();
        calendar = Calendar.getInstance();
        calendar_noti = Calendar.getInstance();
        calendar.setTimeInMillis(e.getDate());
        calendar_noti.setTimeInMillis(e.getNotidate());

        double days_double = (e.getDate() - now.getTime())/(1000 * 60 * 60 * 24.0);
        double days_double_noti = (e.getNotidate()-now.getTime())/(1000 * 60 * 60 * 24.0);

        int days = (int)Math.ceil(days_double);
        int days_noti = (int)Math.ceil(days_double_noti);
        if(days>0){
            detail_sol.setText("还有");
            detail_days.setText(String.valueOf(days));
        }else{
            detail_sol.setText("已经过了");
            detail_days.setText(String.valueOf(-1 *days));
        }
        detail_title.setText(e.getTitle());
        detail_note.setText(e.getNote());
        //detail_spinner.setEnabled(false);

        String[] music_arr = getResources().getStringArray(R.array.music);
        detail_show_song.setText(music_arr[e.getBgm()]);
        detail_show_time.setText("距离提醒日期还有:"+days_noti+"天");
        switch(e.getColor()){
            case 1:{
                detail_title.setTextColor(Color.parseColor("#F06292"));
                detail_days.setTextColor(Color.parseColor("#F06292"));
                break;
            }
            case 2:{
                detail_title.setTextColor(Color.parseColor("#26A69A"));
                detail_days.setTextColor(Color.parseColor("#26A69A"));

                break;
            }
            case 3:{
                detail_title.setTextColor(Color.parseColor("#FFCA28"));
                detail_days.setTextColor(Color.parseColor("#FFCA28"));

                break;
            }
            case 4:{
                detail_title.setTextColor(Color.parseColor("#FF7043"));
                detail_days.setTextColor(Color.parseColor("#FF7043"));
                break;
            }
        }

        detail_ok .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        detail_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                detail_show_layout.setVisibility(View.GONE);
                detail_edit_layout.setVisibility(View.VISIBLE);
                detail_spinner.setEnabled(true);
                detail_spinner.setAdapter(new ArrayAdapter<String>(DetailActivity.this, android.R.layout.simple_spinner_dropdown_item,music_arr));
                detail_spinner.setSelection(e.getBgm(),true);
                edit_title.setText(e.getTitle());
                edit_note.setText(e.getNote());

            }
        });
        edit_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(DetailActivity.this,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(year,monthOfYear,dayOfMonth);
                    }
                },calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });
        edit_noti_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(DetailActivity.this,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar_noti.set(year,monthOfYear,dayOfMonth);
                    }
                },calendar_noti.get(Calendar.YEAR),
                        calendar_noti.get(Calendar.MONTH),
                        calendar_noti.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });

        detail_edit_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = edit_title.getText().toString();
                String note = edit_note.getText().toString();
                if(TextUtils.isEmpty(title)){
                    new AlertDialog.Builder(DetailActivity.this)
                            .setTitle("Wrong")
                            .setMessage("has not set title")
                            .show();
                    return;
                }
                edit_title.setText("");
                edit_note.setText("");
                e.setDate(calendar.getTimeInMillis());
                e.setNotidate(calendar_noti.getTimeInMillis());
                e.setTitle(title);
                e.setNote(note);
                e.setBgm(detail_spinner.getSelectedItemPosition());
                switch (edit_color_group.getCheckedRadioButtonId()){
                    case R.id.color1:{
                        e.setColor(1);
                        break;
                    }
                    case R.id.color2:{
                        e.setColor(2);
                        break;
                    }
                    case R.id.color3:{
                        e.setColor(3);
                        break;
                    }
                    case R.id.color4:{
                        e.setColor(4);
                        break;
                    }
                }
                EventDB.getInstance(DetailActivity.this).updateEvent(e);
                Intent i = new Intent();
                i.putExtra("EVENT",e);
                setResult(1, i);
                finish();
            }
        });

        detail_edit_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra("DeleteEVENT",e);   //这里传入id用来删除对应的notification
                setResult(2, i);
                //setResult(2);
                EventDB.getInstance(DetailActivity.this).deleteEvent(e);
                finish();
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}