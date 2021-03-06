package com.example.countdowndays;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;


import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.countdowndays.db.EventDB;
import com.example.countdowndays.model.Event;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import github.chenupt.dragtoplayout.DragTopLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventListFragment extends androidx.fragment.app.Fragment {
    private ListView listView;
    private List<Event> list;
    private EditText title_ET;
    private EditText note_ET;
    private RadioGroup color_group;
    private Button date_btn,ok_btn;
    private Calendar c;
    private Calendar c_notice;
    private EventDB eventDB;
    private DragTopLayout dtl;
    private Adapter adapter;
    private Spinner song_spinner;
    private TextView detail_song;
    private ImageButton notice_btn;
    private EditText edit_search_input;
    private TextView text_search_cancel;
    private View view_holder_for_focus;
    private NotificationSender notificationSender;

    public EventListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode){
            case 1:{
                Event e = (Event)data.getSerializableExtra("EVENT");
                try {
                    list.set(requestCode,e);
                    notificationSender.SendEventNotification(e,getContext());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                adapter.notifyDataSetChanged();
                break;
            }
            case 2:{
                Event e = (Event)data.getSerializableExtra("DeleteEVENT");
                try {
                    list.remove(requestCode);
                    NotificationSender.CancelNotification(e);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                adapter.notifyDataSetChanged();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_event_list, container, false);
        dtl = (DragTopLayout)view.findViewById(R.id.dtl);

        notificationSender = new NotificationSender(getContext());
        listView = (ListView)view.findViewById(R.id.listview);
        edit_search_input = (EditText)view.findViewById(R.id.edit_search_input);
        text_search_cancel = (TextView)view.findViewById(R.id.text_search_cancel);
        view_holder_for_focus = (View)view.findViewById(R.id.view_holder_for_focus);
        text_search_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_search_input.setText("");
                hideKeyboard(getContext(), edit_search_input);
                view_holder_for_focus.requestFocus();
            }
        });



        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (view != null && view.getChildCount() > 0) {
                    if (view.getChildAt(0).getTop() < 0 || view.getFirstVisiblePosition()!=0) {
                        dtl.setTouchMode(false);
                    }else{
                        dtl.setTouchMode(true);
                    }
                }

            }
        });


        eventDB = EventDB.getInstance(getActivity());
        list = eventDB.loadEvent();

        adapter = new Adapter(getActivity(),R.layout.card_item,list);

        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event e = (Event)(parent.getItemAtPosition(position));
                Intent i = new Intent(getActivity(),DetailActivity.class);     //跳转到detail activity
                i.putExtra("EVENT",e);
                startActivityForResult(i, position);
            }
        });

        title_ET = (EditText)view.findViewById(R.id.title_et);
        note_ET = (EditText)view.findViewById(R.id.note_et);
        color_group = (RadioGroup)view.findViewById(R.id.color_group);
        date_btn = (Button)view.findViewById(R.id.date_btn);
        ok_btn = (Button)view.findViewById(R.id.ok_btn);
        song_spinner = (Spinner)view.findViewById(R.id.song_spinner);
        detail_song = (TextView)view.findViewById(R.id.show_song);
        notice_btn = (ImageButton)view.findViewById(R.id.imagebutton_notice);

        notice_btn.setOnClickListener(new View.OnClickListener() {  //设置提醒日期按钮
            @Override
            public void onClick(View v) {
                Calendar calendar_notice = Calendar.getInstance();
                new DatePickerDialog(getActivity(),DatePickerDialog.THEME_HOLO_LIGHT,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        c_notice= Calendar.getInstance();
                        c_notice.set(year,monthOfYear,dayOfMonth);
                    }
                },calendar_notice.get(Calendar.YEAR),
                        calendar_notice.get(Calendar.MONTH),
                        calendar_notice.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });

        date_btn.setOnClickListener(new View.OnClickListener() {      //设置日期按钮
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();

                new DatePickerDialog(getActivity(),DatePickerDialog.THEME_HOLO_LIGHT,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        c= Calendar.getInstance();
                        c.set(year,monthOfYear,dayOfMonth);
                    }
                },calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });
        ok_btn.setOnClickListener(new View.OnClickListener() {       //ok按钮
            @Override
            public void onClick(View v) {
                String title = title_ET.getText().toString();
                String note = note_ET.getText().toString();
                if (c == null){
                    new AlertDialog.Builder(getActivity(),AlertDialog.THEME_HOLO_LIGHT)
                            .setTitle("Sorry")
                            .setMessage("has not set date")
                            .show();
                    return;
                }
                if(c_notice == null){
                    new AlertDialog.Builder(getActivity(),AlertDialog.THEME_HOLO_LIGHT)
                            .setTitle("Sorry")
                            .setMessage("has not set notice date")
                            .show();
                    return;
                }
                if(TextUtils.isEmpty(title)){
                    new AlertDialog.Builder(getActivity(),AlertDialog.THEME_HOLO_LIGHT)
                            .setTitle("Sorry")
                            .setMessage("has not set title")
                            .show();
                    return;
                }
                title_ET.setText("");
                note_ET.setText("");


                Event e = new Event();
                e.setNotidate(c_notice.getTimeInMillis());
                e.setDate(c.getTimeInMillis());
                e.setTitle(title);
                e.setNote(note);
                e.setBgm(song_spinner.getSelectedItemPosition());
                switch (color_group.getCheckedRadioButtonId()){
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
                eventDB.saveEvent(e);
                list.add(0,e);
                adapter.notifyDataSetChanged();
                NotificationSender.SendEventNotification(e,getContext());
                dtl.closeTopView(true);
                c = null;
                c_notice = null;
            }
        });


        edit_search_input.addTextChangedListener(new TextWatcher() {                //搜索框处理
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<Event> list_temp = EventDB.getInstance(getContext()).FuzzySearchEvent(s.toString());
                list.clear();
                list.addAll(list_temp);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("hihihi", "resume");
    }

    private void printlist(List<Event> list){
        for(Event event:list){
            Log.d("1",event.getTitle());
        }
    }

    private void hideKeyboard(Context context, View view) {
        InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (im != null) {
            im.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    public class Adapter extends ArrayAdapter<Event> {
        private int resId;

        public Adapter(Context context, int resource, List<Event> list) {
            super(context, resource, list);
            resId = resource;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            Event event = getItem(position);
            View view;
            ViewHolder viewHolder;

            if(convertView == null){
                view = LayoutInflater.from(getContext()).inflate(resId,null);
                viewHolder = new ViewHolder();
                viewHolder.title = (TextView)view.findViewById(R.id.card_title);
                viewHolder.date = (TextView)view.findViewById(R.id.card_time);
                viewHolder.note = (TextView)view.findViewById(R.id.card_note);
                viewHolder.days = (TextView)view.findViewById(R.id.card_days);
                viewHolder.since_or_left = (TextView)view.findViewById(R.id.since_or_left);
                viewHolder.color = (RelativeLayout)view.findViewById(R.id.card_color);
                viewHolder.show_song = (TextView)view.findViewById(R.id.show_song);
                viewHolder.detail_music = (TextView)view.findViewById(R.id.detail_music_text);
                view.setTag(viewHolder);
            }else{
                view = convertView;
                viewHolder = (ViewHolder)view.getTag();
            }
           // printlist(list);
            viewHolder.title.setText(list.get(position).getTitle());
            viewHolder.date.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date(list.get(position).getDate())));
            Date now = new Date();
            double days = (list.get(position).getDate() - now.getTime())/(1000 * 60 * 60 * 24.0);
            int i = (int)Math.ceil(days);
            if(i>0){
                viewHolder.since_or_left.setText("Days left");
                viewHolder.days.setText(String.valueOf(i));
            }else{
                viewHolder.since_or_left.setText("Days since");
                viewHolder.days.setText(String.valueOf(-1 *i));
            }
            switch(event.getColor()){
                case 1:{
                    viewHolder.color.setBackgroundColor(Color.parseColor("#F06292"));
                    break;
                }
                case 2:{
                    viewHolder.color.setBackgroundColor(Color.parseColor("#26A69A"));
                    break;
                }
                case 3:{
                    viewHolder.color.setBackgroundColor(Color.parseColor("#FFCA28"));
                    break;
                }
                case 4:{
                    viewHolder.color.setBackgroundColor(Color.parseColor("#FF7043"));
                    break;
                }
            }
            viewHolder.note.setText(list.get(position).getNote());
            //viewHolder.show_song.setText(list.get(position).getBgm());
            return view;
        }

        public class ViewHolder{
            public TextView title;
            public TextView date;
            public TextView note;
            public RelativeLayout color;
            public TextView days;
            public TextView since_or_left;
            public TextView show_song;
            public TextView detail_music;
        }
    }

}