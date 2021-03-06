package com.lerex.tr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class addTickets extends AppCompatActivity {


    //To Add FireSotre

    //Auth
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private TinyDB tinydb;

    //RealTime Database Connection
    private DatabaseReference mydb = FirebaseDatabase.getInstance().getReference("Tickets");
    //private DatabaseReference availabledb = FirebaseDatabase.getInstance().getReference("Available");
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private TimePickerDialog.OnTimeSetListener timesetlistener;
    private String date=null,time = null;
    private TextView eDate,eTime;
    private int num;

    protected void addTicket(){
        String key = mydb.push().getKey();
        String curuser = Objects.requireNonNull(mAuth.getCurrentUser()).getPhoneNumber();
        AutoCompleteTextView eName = findViewById(R.id.etRef1);
        EditText eCost = findViewById(R.id.etCost);
        String city=tinydb.getString("CurCity");//eCity.getText().toString();
        EditText eTheatre = findViewById(R.id.etTheatre);

        TicketDetails newEvent = new TicketDetails(eName.getText().toString(),eCost.getText().toString(),date,num,curuser,city,eTheatre.getText().toString(),key,time);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String cost=eCost.getText().toString();
        String theatre=eTheatre.getText().toString();
        String mov=eName.getText().toString();

        if((date == null) || (theatre.trim().equals("") || theatre.isEmpty()) || (cost.trim().equals("") || cost.isEmpty()) || (mov.isEmpty()) || (city.isEmpty() || city.trim().equals("")))
        {
            Toast.makeText(this, "Fill all the fields.",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            try {
                Date strDate = sdf.parse(date);
                Date curDate = sdf.parse(sdf.format(new Date()));
                if ((Objects.requireNonNull(curDate).before(strDate) || curDate.equals(strDate))) {
                    DatabaseReference moviedb = FirebaseDatabase.getInstance().getReference(eName.getText().toString() + "/" + "Tickets");
                    DatabaseReference userdb = FirebaseDatabase.getInstance().getReference(mAuth.getCurrentUser().getUid());
                    DatabaseReference citydb = FirebaseDatabase.getInstance().getReference(eName.getText().toString() + "/" + city + "/" + eTheatre.getText().toString());

                    mydb.child(Objects.requireNonNull(key)).setValue(newEvent);
                    moviedb.child(key).setValue("");
                    userdb.child(key).setValue("");
                    citydb.child(key).setValue("");
                    finish();
                } else {
                    Toast.makeText(this, "Date Invalid",
                            Toast.LENGTH_SHORT).show();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

       // availabledb.child(key).setValue("");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//FullScreening The Application

        setContentView(R.layout.activity_add_tickets);

        tinydb = new TinyDB(this);//Shared Preference To Get Localized Data
        ArrayList<String> tickets = tinydb.getListString("Movies");

        eDate = findViewById(R.id.etDate);
        eTime= findViewById(R.id.eventtime);

        eTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                // Create a new instance of TimePickerDialog and return it
                TimePickerDialog timePickerDialog = new TimePickerDialog(addTickets.this, timesetlistener, hour, minute,false);
                timePickerDialog.show();
            }
        });

        timesetlistener = new TimePickerDialog.OnTimeSetListener(){
            @Override
            public void onTimeSet(TimePicker view,int hour,int minute){
                String hours,minutes,session=" AM";
                hours=Integer.toString(hour);
                minutes=Integer.toString(minute);
                if(hour>12){
                    hour=hour-11;
                    session=" PM";
                }
                if(hour<10){
                    hours="0"+hours;
                }
                if(minute<10){
                    minutes="0"+minutes;
                }
                time=hours+":"+minutes+session;
                eTime.setText(time);
            }

        };
        eDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();
                int day=calendar.get(Calendar.DAY_OF_MONTH);
                int month=calendar.get(Calendar.MONTH);
                int year=calendar.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog=new DatePickerDialog(
                        addTickets.this,R.style.DialogTheme,dateSetListener,year,month,day);
                datePickerDialog.show();
            }
        });


        dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month=month+1;
                date=day+"/"+month+"/"+year;
                eDate.setText(date);

            }
        };

        Spinner eNo = findViewById(R.id.etNumber);

        final List<Integer> noOfTickets=new ArrayList<>();

        noOfTickets.add(1);
        noOfTickets.add(2);
        noOfTickets.add(3);
        noOfTickets.add(4);
        noOfTickets.add(5);

        ArrayAdapter<Integer> NumAdapter=new ArrayAdapter<>(this,R.layout.spinner_view,noOfTickets);
        NumAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        eNo.setAdapter(NumAdapter);

        eNo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                num=Integer.valueOf(noOfTickets.get(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Button add1 = findViewById(R.id.btnAdd1);
        add1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addTicket();
            }
        });

        final AutoCompleteTextView act =findViewById(R.id.etRef1);
        final AutoCompList adapter=new AutoCompList(addTickets.this,android.R.layout.simple_dropdown_item_1line, tickets);
        act.setAdapter(adapter);

    }

}
