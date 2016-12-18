package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Activities.MainActivity;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;

/**
 * Created by Eric on 25.10.2016.
 */

public class MonthFragment extends Fragment {

    // Variables
    Calendar mCalendarReference;
    int mStartPosition;
    int mEndPosition;
    int mSelectedPosition = 0;
    int mTodayPosition = -1;
    DatabaseHelper mDatabaseHelper;
    CalendarFragment mCalendarFragment;
    int mCalendarPage;

    // Array Lists
    ArrayList<Calendar> mCalendarObjectList;
    ArrayList<RelativeLayout> rlDayList;
    ArrayList<TextView> tvDayList;
    ArrayList<View> vCircleSmallList;
    ArrayList<View> vCircleBigList;


    // RelativeLayout - Days

    // RelativeLayout - Days
    RelativeLayout rlDay_1;RelativeLayout rlDay_2;RelativeLayout rlDay_3;RelativeLayout rlDay_4;RelativeLayout rlDay_5;RelativeLayout rlDay_6;RelativeLayout rlDay_7;RelativeLayout rlDay_8;RelativeLayout rlDay_9;RelativeLayout rlDay_10;
    RelativeLayout rlDay_11;RelativeLayout rlDay_12;RelativeLayout rlDay_13;RelativeLayout rlDay_14;RelativeLayout rlDay_15;RelativeLayout rlDay_16;RelativeLayout rlDay_17;RelativeLayout rlDay_18;RelativeLayout rlDay_19;RelativeLayout rlDay_20;
    RelativeLayout rlDay_21;RelativeLayout rlDay_22;RelativeLayout rlDay_23;RelativeLayout rlDay_24;RelativeLayout rlDay_25;RelativeLayout rlDay_26;RelativeLayout rlDay_27;RelativeLayout rlDay_28;RelativeLayout rlDay_29;RelativeLayout rlDay_30;
    RelativeLayout rlDay_31;RelativeLayout rlDay_32;RelativeLayout rlDay_33;RelativeLayout rlDay_34;RelativeLayout rlDay_35;RelativeLayout rlDay_36;RelativeLayout rlDay_37;RelativeLayout rlDay_38;RelativeLayout rlDay_39;RelativeLayout rlDay_40;
    RelativeLayout rlDay_41;RelativeLayout rlDay_42;

    // TextView - Days
    TextView tvDay_1;TextView tvDay_2;TextView tvDay_3;TextView tvDay_4;TextView tvDay_5;TextView tvDay_6;TextView tvDay_7;TextView tvDay_8;TextView tvDay_9;TextView tvDay_10;
    TextView tvDay_11;TextView tvDay_12;TextView tvDay_13;TextView tvDay_14;TextView tvDay_15;TextView tvDay_16;TextView tvDay_17;TextView tvDay_18;TextView tvDay_19;TextView tvDay_20;
    TextView tvDay_21;TextView tvDay_22;TextView tvDay_23;TextView tvDay_24;TextView tvDay_25;TextView tvDay_26;TextView tvDay_27;TextView tvDay_28;TextView tvDay_29;TextView tvDay_30;
    TextView tvDay_31;TextView tvDay_32;TextView tvDay_33;TextView tvDay_34;TextView tvDay_35;TextView tvDay_36;TextView tvDay_37;TextView tvDay_38;TextView tvDay_39;TextView tvDay_40;
    TextView tvDay_41;TextView tvDay_42;

    // View - CircleBig
    View vCircleBig_1;View vCircleBig_2;View vCircleBig_3;View vCircleBig_4;View vCircleBig_5;View vCircleBig_6;View vCircleBig_7;View vCircleBig_8;View vCircleBig_9;View vCircleBig_10;
    View vCircleBig_11;View vCircleBig_12;View vCircleBig_13;View vCircleBig_14;View vCircleBig_15;View vCircleBig_16;View vCircleBig_17;View vCircleBig_18;View vCircleBig_19;View vCircleBig_20;
    View vCircleBig_21;View vCircleBig_22;View vCircleBig_23;View vCircleBig_24;View vCircleBig_25;View vCircleBig_26;View vCircleBig_27;View vCircleBig_28;View vCircleBig_29;View vCircleBig_30;
    View vCircleBig_31;View vCircleBig_32;View vCircleBig_33;View vCircleBig_34;View vCircleBig_35;View vCircleBig_36;View vCircleBig_37;View vCircleBig_38;View vCircleBig_39;View vCircleBig_40;
    View vCircleBig_41;View vCircleBig_42;

    // View - CircleSmall
    View vCircleSmall_1;View vCircleSmall_2;View vCircleSmall_3;View vCircleSmall_4;View vCircleSmall_5;View vCircleSmall_6;View vCircleSmall_7;View vCircleSmall_8;View vCircleSmall_9;View vCircleSmall_10;
    View vCircleSmall_11;View vCircleSmall_12;View vCircleSmall_13;View vCircleSmall_14;View vCircleSmall_15;View vCircleSmall_16;View vCircleSmall_17;View vCircleSmall_18;View vCircleSmall_19;View vCircleSmall_20;
    View vCircleSmall_21;View vCircleSmall_22;View vCircleSmall_23;View vCircleSmall_24;View vCircleSmall_25;View vCircleSmall_26;View vCircleSmall_27;View vCircleSmall_28;View vCircleSmall_29;View vCircleSmall_30;
    View vCircleSmall_31;View vCircleSmall_32;View vCircleSmall_33;View vCircleSmall_34;View vCircleSmall_35;View vCircleSmall_36;View vCircleSmall_37;View vCircleSmall_38;View vCircleSmall_39;View vCircleSmall_40;
    View vCircleSmall_41;View vCircleSmall_42;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_month, container, false);

        // Set up layout
        initiateLayout(layout);

        // Set up variables
        mDatabaseHelper = new DatabaseHelper(getActivity());

        // Handle arguments
        Bundle bundle = getArguments();
        Calendar cal = (Calendar)bundle.getSerializable(MyConstants.CALENDAR_OBJECT);
        mCalendarReference = Calendar.getInstance();
        mCalendarReference.set(Calendar.YEAR, cal.get(Calendar.YEAR));
        mCalendarReference.set(Calendar.MONTH, cal.get(Calendar.MONTH));
        mCalendarPage = bundle.getInt(MyConstants.CALENDAR_PAGE);

        // Set up ArrayList<Calendar>
        mCalendarObjectList = new ArrayList<>(42);
        mCalendarReference.set(Calendar.DAY_OF_MONTH, 1);
        int firstWeekDay  = mCalendarReference.get(Calendar.DAY_OF_WEEK);
        if (firstWeekDay == 1) {
            firstWeekDay = 7;
        } else {
            firstWeekDay = firstWeekDay-1;
        }
        mStartPosition = firstWeekDay-1;
        Log.i("CALENDAR INFORMATION", "Year: "+ mCalendarReference.get(Calendar.YEAR)+", Month: "+ mCalendarReference.get(Calendar.MONTH)+", DAY OF WEEK: "+firstWeekDay+", Day of Week in Month: "+ mCalendarReference.get(Calendar.DAY_OF_WEEK_IN_MONTH));

        mCalendarReference.add(Calendar.MONTH, -1);
        mCalendarReference.set(Calendar.DAY_OF_MONTH, mCalendarReference.getActualMaximum(Calendar.DAY_OF_MONTH));
        if (firstWeekDay > 1) {
            mCalendarReference.add(Calendar.DAY_OF_MONTH, -(firstWeekDay-2));
        } else {
            mCalendarReference.add(Calendar.DAY_OF_MONTH, 1);
        }

        // last month
        for (int i = 1; i < firstWeekDay; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, mCalendarReference.get(Calendar.YEAR));
            calendar.set(Calendar.MONTH, mCalendarReference.get(Calendar.MONTH));
            calendar.set(Calendar.DAY_OF_MONTH, mCalendarReference.get(Calendar.DAY_OF_MONTH));
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            mCalendarObjectList.add(calendar);
            mCalendarReference.add(Calendar.DAY_OF_MONTH, 1);
        }
        int numberOfDaysNextMonth = mCalendarReference.getActualMaximum(Calendar.DAY_OF_MONTH);
        // this month
        for (int i = 0; i < numberOfDaysNextMonth; i++) {
            Calendar calendar = Calendar.getInstance();
            if (calendar.get(Calendar.YEAR) == mCalendarReference.get(Calendar.YEAR) && calendar.get(Calendar.MONTH) == mCalendarReference.get(Calendar.MONTH)&& calendar.get(Calendar.DAY_OF_MONTH) == mCalendarReference.get(Calendar.DAY_OF_MONTH)) mTodayPosition = mCalendarObjectList.size();
            calendar.set(Calendar.YEAR, mCalendarReference.get(Calendar.YEAR));
            calendar.set(Calendar.MONTH, mCalendarReference.get(Calendar.MONTH));
            calendar.set(Calendar.DAY_OF_MONTH, mCalendarReference.get(Calendar.DAY_OF_MONTH));
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            mCalendarObjectList.add(calendar);
            mCalendarReference.add(Calendar.DAY_OF_MONTH, 1);
        }
        // next month
        int size = mCalendarObjectList.size();
        mEndPosition = mCalendarObjectList.size()-1;
        for (int i = 0; i < 42-size; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, mCalendarReference.get(Calendar.YEAR));
            calendar.set(Calendar.MONTH, mCalendarReference.get(Calendar.MONTH));
            calendar.set(Calendar.DAY_OF_MONTH, mCalendarReference.get(Calendar.DAY_OF_MONTH));
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            mCalendarObjectList.add(calendar);
            mCalendarReference.add(Calendar.DAY_OF_MONTH, 1);
        }

        // Create layout ArrayLists
        createArrayLists();

        // Set up calendar content
        setUpCalendarContent();


        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDay(rlDayList.indexOf((RelativeLayout)view));
            }
        };
        // Set up onClickListener
        for (int i = 0; i < 42; i++) {
            RelativeLayout rl = rlDayList.get(i);
            rl.setOnClickListener(onClickListener);
            /*
            final int finalI = i;
            rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectDay(finalI);
                }
            });
            */
        }

        // Select Position
        if (mTodayPosition >= 0) {
            selectDay(mTodayPosition);
        } else {
            selectDay(mStartPosition);
        }

        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    // Class methods

    private void initiateLayout(View layout) {
        rlDay_1 = (RelativeLayout)layout.findViewById(R.id.rlDay_1);rlDay_2 = (RelativeLayout)layout.findViewById(R.id.rlDay_2);rlDay_3 = (RelativeLayout)layout.findViewById(R.id.rlDay_3);rlDay_4 = (RelativeLayout)layout.findViewById(R.id.rlDay_4);rlDay_5 = (RelativeLayout)layout.findViewById(R.id.rlDay_5);rlDay_6 = (RelativeLayout)layout.findViewById(R.id.rlDay_6);rlDay_7 = (RelativeLayout)layout.findViewById(R.id.rlDay_7);rlDay_8 = (RelativeLayout)layout.findViewById(R.id.rlDay_8);rlDay_9 = (RelativeLayout)layout.findViewById(R.id.rlDay_9);rlDay_10 = (RelativeLayout)layout.findViewById(R.id.rlDay_10);
        rlDay_11 = (RelativeLayout)layout.findViewById(R.id.rlDay_11);rlDay_12 = (RelativeLayout)layout.findViewById(R.id.rlDay_12);rlDay_13 = (RelativeLayout)layout.findViewById(R.id.rlDay_13);rlDay_14 = (RelativeLayout)layout.findViewById(R.id.rlDay_14);rlDay_15 = (RelativeLayout)layout.findViewById(R.id.rlDay_15);rlDay_16 = (RelativeLayout)layout.findViewById(R.id.rlDay_16);rlDay_17 = (RelativeLayout)layout.findViewById(R.id.rlDay_17);rlDay_18 = (RelativeLayout)layout.findViewById(R.id.rlDay_18);rlDay_19 = (RelativeLayout)layout.findViewById(R.id.rlDay_19);rlDay_20 = (RelativeLayout)layout.findViewById(R.id.rlDay_20);
        rlDay_21 = (RelativeLayout)layout.findViewById(R.id.rlDay_21);rlDay_22 = (RelativeLayout)layout.findViewById(R.id.rlDay_22);rlDay_23 = (RelativeLayout)layout.findViewById(R.id.rlDay_23);rlDay_24 = (RelativeLayout)layout.findViewById(R.id.rlDay_24);rlDay_25 = (RelativeLayout)layout.findViewById(R.id.rlDay_25);rlDay_26 = (RelativeLayout)layout.findViewById(R.id.rlDay_26);rlDay_27 = (RelativeLayout)layout.findViewById(R.id.rlDay_27);rlDay_28 = (RelativeLayout)layout.findViewById(R.id.rlDay_28);rlDay_29 = (RelativeLayout)layout.findViewById(R.id.rlDay_29);rlDay_30 = (RelativeLayout)layout.findViewById(R.id.rlDay_30);
        rlDay_31 = (RelativeLayout)layout.findViewById(R.id.rlDay_31);rlDay_32 = (RelativeLayout)layout.findViewById(R.id.rlDay_32);rlDay_33 = (RelativeLayout)layout.findViewById(R.id.rlDay_33);rlDay_34 = (RelativeLayout)layout.findViewById(R.id.rlDay_34);rlDay_35 = (RelativeLayout)layout.findViewById(R.id.rlDay_35);rlDay_36 = (RelativeLayout)layout.findViewById(R.id.rlDay_36);rlDay_37 = (RelativeLayout)layout.findViewById(R.id.rlDay_37);rlDay_38 = (RelativeLayout)layout.findViewById(R.id.rlDay_38);rlDay_39 = (RelativeLayout)layout.findViewById(R.id.rlDay_39);rlDay_40 = (RelativeLayout)layout.findViewById(R.id.rlDay_40);
        rlDay_41 = (RelativeLayout)layout.findViewById(R.id.rlDay_41);rlDay_42 = (RelativeLayout)layout.findViewById(R.id.rlDay_42);

        tvDay_1 = (TextView)layout.findViewById(R.id.tvCalendarDay_1);tvDay_2 = (TextView)layout.findViewById(R.id.tvCalendarDay_2);tvDay_3 = (TextView)layout.findViewById(R.id.tvCalendarDay_3);tvDay_4 = (TextView)layout.findViewById(R.id.tvCalendarDay_4);tvDay_5 = (TextView)layout.findViewById(R.id.tvCalendarDay_5);tvDay_6 = (TextView)layout.findViewById(R.id.tvCalendarDay_6);tvDay_7 = (TextView)layout.findViewById(R.id.tvCalendarDay_7);tvDay_8 = (TextView)layout.findViewById(R.id.tvCalendarDay_8);tvDay_9 = (TextView)layout.findViewById(R.id.tvCalendarDay_9);tvDay_10 = (TextView)layout.findViewById(R.id.tvCalendarDay_10);
        tvDay_11 = (TextView)layout.findViewById(R.id.tvCalendarDay_11);tvDay_12 = (TextView)layout.findViewById(R.id.tvCalendarDay_12);tvDay_13 = (TextView)layout.findViewById(R.id.tvCalendarDay_13);tvDay_14 = (TextView)layout.findViewById(R.id.tvCalendarDay_14);tvDay_15 = (TextView)layout.findViewById(R.id.tvCalendarDay_15);tvDay_16 = (TextView)layout.findViewById(R.id.tvCalendarDay_16);tvDay_17 = (TextView)layout.findViewById(R.id.tvCalendarDay_17);tvDay_18 = (TextView)layout.findViewById(R.id.tvCalendarDay_18);tvDay_19 = (TextView)layout.findViewById(R.id.tvCalendarDay_19);tvDay_20 = (TextView)layout.findViewById(R.id.tvCalendarDay_20);
        tvDay_21 = (TextView)layout.findViewById(R.id.tvCalendarDay_21);tvDay_22 = (TextView)layout.findViewById(R.id.tvCalendarDay_22);tvDay_23 = (TextView)layout.findViewById(R.id.tvCalendarDay_23);tvDay_24 = (TextView)layout.findViewById(R.id.tvCalendarDay_24);tvDay_25 = (TextView)layout.findViewById(R.id.tvCalendarDay_25);tvDay_26 = (TextView)layout.findViewById(R.id.tvCalendarDay_26);tvDay_27 = (TextView)layout.findViewById(R.id.tvCalendarDay_27);tvDay_28 = (TextView)layout.findViewById(R.id.tvCalendarDay_28);tvDay_29 = (TextView)layout.findViewById(R.id.tvCalendarDay_29);tvDay_30 = (TextView)layout.findViewById(R.id.tvCalendarDay_30);
        tvDay_31 = (TextView)layout.findViewById(R.id.tvCalendarDay_31);tvDay_32 = (TextView)layout.findViewById(R.id.tvCalendarDay_32);tvDay_33 = (TextView)layout.findViewById(R.id.tvCalendarDay_33);tvDay_34 = (TextView)layout.findViewById(R.id.tvCalendarDay_34);tvDay_35 = (TextView)layout.findViewById(R.id.tvCalendarDay_35);tvDay_36 = (TextView)layout.findViewById(R.id.tvCalendarDay_36);tvDay_37 = (TextView)layout.findViewById(R.id.tvCalendarDay_37);tvDay_38 = (TextView)layout.findViewById(R.id.tvCalendarDay_38);tvDay_39 = (TextView)layout.findViewById(R.id.tvCalendarDay_39);tvDay_40 = (TextView)layout.findViewById(R.id.tvCalendarDay_40);
        tvDay_41 = (TextView)layout.findViewById(R.id.tvCalendarDay_41);tvDay_42 = (TextView)layout.findViewById(R.id.tvCalendarDay_42);

        vCircleBig_1 = layout.findViewById(R.id.vCircleBig_1);vCircleBig_2 = layout.findViewById(R.id.vCircleBig_2);vCircleBig_3 = layout.findViewById(R.id.vCircleBig_3);vCircleBig_4 = layout.findViewById(R.id.vCircleBig_4);vCircleBig_5 = layout.findViewById(R.id.vCircleBig_5);vCircleBig_6 = layout.findViewById(R.id.vCircleBig_6);vCircleBig_7 = layout.findViewById(R.id.vCircleBig_7);vCircleBig_8 = layout.findViewById(R.id.vCircleBig_8);vCircleBig_9 = layout.findViewById(R.id.vCircleBig_9);vCircleBig_10 = layout.findViewById(R.id.vCircleBig_10);
        vCircleBig_11 = layout.findViewById(R.id.vCircleBig_11);vCircleBig_12 = layout.findViewById(R.id.vCircleBig_12);vCircleBig_13 = layout.findViewById(R.id.vCircleBig_13);vCircleBig_14 = layout.findViewById(R.id.vCircleBig_14);vCircleBig_15 = layout.findViewById(R.id.vCircleBig_15);vCircleBig_16 = layout.findViewById(R.id.vCircleBig_16);vCircleBig_17 = layout.findViewById(R.id.vCircleBig_17);vCircleBig_18 = layout.findViewById(R.id.vCircleBig_18);vCircleBig_19 = layout.findViewById(R.id.vCircleBig_19);vCircleBig_20 = layout.findViewById(R.id.vCircleBig_20);
        vCircleBig_21 = layout.findViewById(R.id.vCircleBig_21);vCircleBig_22 = layout.findViewById(R.id.vCircleBig_22);vCircleBig_23 = layout.findViewById(R.id.vCircleBig_23);vCircleBig_24 = layout.findViewById(R.id.vCircleBig_24);vCircleBig_25 = layout.findViewById(R.id.vCircleBig_25);vCircleBig_26 = layout.findViewById(R.id.vCircleBig_26);vCircleBig_27 = layout.findViewById(R.id.vCircleBig_27);vCircleBig_28 = layout.findViewById(R.id.vCircleBig_28);vCircleBig_29 = layout.findViewById(R.id.vCircleBig_29);vCircleBig_30 = layout.findViewById(R.id.vCircleBig_30);
        vCircleBig_31 = layout.findViewById(R.id.vCircleBig_31);vCircleBig_32 = layout.findViewById(R.id.vCircleBig_32);vCircleBig_33 = layout.findViewById(R.id.vCircleBig_33);vCircleBig_34 = layout.findViewById(R.id.vCircleBig_34);vCircleBig_35 = layout.findViewById(R.id.vCircleBig_35);vCircleBig_36 = layout.findViewById(R.id.vCircleBig_36);vCircleBig_37 = layout.findViewById(R.id.vCircleBig_37);vCircleBig_38 = layout.findViewById(R.id.vCircleBig_38);vCircleBig_39 = layout.findViewById(R.id.vCircleBig_39);vCircleBig_40 = layout.findViewById(R.id.vCircleBig_40);
        vCircleBig_41 = layout.findViewById(R.id.vCircleBig_41);vCircleBig_42 = layout.findViewById(R.id.vCircleBig_42);

        vCircleSmall_1 = layout.findViewById(R.id.vCircleSmall_1);vCircleSmall_2 = layout.findViewById(R.id.vCircleSmall_2);vCircleSmall_3 = layout.findViewById(R.id.vCircleSmall_3);vCircleSmall_4 = layout.findViewById(R.id.vCircleSmall_4);vCircleSmall_5 = layout.findViewById(R.id.vCircleSmall_5);vCircleSmall_6 = layout.findViewById(R.id.vCircleSmall_6);vCircleSmall_7 = layout.findViewById(R.id.vCircleSmall_7);vCircleSmall_8 = layout.findViewById(R.id.vCircleSmall_8);vCircleSmall_9 = layout.findViewById(R.id.vCircleSmall_9);vCircleSmall_10 = layout.findViewById(R.id.vCircleSmall_10);
        vCircleSmall_11 = layout.findViewById(R.id.vCircleSmall_11);vCircleSmall_12 = layout.findViewById(R.id.vCircleSmall_12);vCircleSmall_13 = layout.findViewById(R.id.vCircleSmall_13);vCircleSmall_14 = layout.findViewById(R.id.vCircleSmall_14);vCircleSmall_15 = layout.findViewById(R.id.vCircleSmall_15);vCircleSmall_16 = layout.findViewById(R.id.vCircleSmall_16);vCircleSmall_17 = layout.findViewById(R.id.vCircleSmall_17);vCircleSmall_18 = layout.findViewById(R.id.vCircleSmall_18);vCircleSmall_19 = layout.findViewById(R.id.vCircleSmall_19);vCircleSmall_20 = layout.findViewById(R.id.vCircleSmall_20);
        vCircleSmall_21 = layout.findViewById(R.id.vCircleSmall_21);vCircleSmall_22 = layout.findViewById(R.id.vCircleSmall_22);vCircleSmall_23 = layout.findViewById(R.id.vCircleSmall_23);vCircleSmall_24 = layout.findViewById(R.id.vCircleSmall_24);vCircleSmall_25 = layout.findViewById(R.id.vCircleSmall_25);vCircleSmall_26 = layout.findViewById(R.id.vCircleSmall_26);vCircleSmall_27 = layout.findViewById(R.id.vCircleSmall_27);vCircleSmall_28 = layout.findViewById(R.id.vCircleSmall_28);vCircleSmall_29 = layout.findViewById(R.id.vCircleSmall_29);vCircleSmall_30 = layout.findViewById(R.id.vCircleSmall_30);
        vCircleSmall_31 = layout.findViewById(R.id.vCircleSmall_31);vCircleSmall_32 = layout.findViewById(R.id.vCircleSmall_32);vCircleSmall_33 = layout.findViewById(R.id.vCircleSmall_33);vCircleSmall_34 = layout.findViewById(R.id.vCircleSmall_34);vCircleSmall_35 = layout.findViewById(R.id.vCircleSmall_35);vCircleSmall_36 = layout.findViewById(R.id.vCircleSmall_36);vCircleSmall_37 = layout.findViewById(R.id.vCircleSmall_37);vCircleSmall_38 = layout.findViewById(R.id.vCircleSmall_38);vCircleSmall_39 = layout.findViewById(R.id.vCircleSmall_39);vCircleSmall_40 = layout.findViewById(R.id.vCircleSmall_40);
        vCircleSmall_41 = layout.findViewById(R.id.vCircleSmall_41);vCircleSmall_42 = layout.findViewById(R.id.vCircleSmall_42);
    }

    private void createArrayLists() {
        rlDayList = new ArrayList<>(42);
        rlDayList.add(rlDay_1);rlDayList.add(rlDay_2);rlDayList.add(rlDay_3);rlDayList.add(rlDay_4);rlDayList.add(rlDay_5);rlDayList.add(rlDay_6);rlDayList.add(rlDay_7);rlDayList.add(rlDay_8);rlDayList.add(rlDay_9);rlDayList.add(rlDay_10);
        rlDayList.add(rlDay_11);rlDayList.add(rlDay_12);rlDayList.add(rlDay_13);rlDayList.add(rlDay_14);rlDayList.add(rlDay_15);rlDayList.add(rlDay_16);rlDayList.add(rlDay_17);rlDayList.add(rlDay_18);rlDayList.add(rlDay_19);rlDayList.add(rlDay_20);
        rlDayList.add(rlDay_21);rlDayList.add(rlDay_22);rlDayList.add(rlDay_23);rlDayList.add(rlDay_24);rlDayList.add(rlDay_25);rlDayList.add(rlDay_26);rlDayList.add(rlDay_27);rlDayList.add(rlDay_28);rlDayList.add(rlDay_29);rlDayList.add(rlDay_30);
        rlDayList.add(rlDay_31);rlDayList.add(rlDay_32);rlDayList.add(rlDay_33);rlDayList.add(rlDay_34);rlDayList.add(rlDay_35);rlDayList.add(rlDay_36);rlDayList.add(rlDay_37);rlDayList.add(rlDay_38);rlDayList.add(rlDay_39);rlDayList.add(rlDay_40);
        rlDayList.add(rlDay_41);rlDayList.add(rlDay_42);

        tvDayList = new ArrayList<>(42);
        tvDayList.add(tvDay_1);tvDayList.add(tvDay_2);tvDayList.add(tvDay_3);tvDayList.add(tvDay_4);tvDayList.add(tvDay_5);tvDayList.add(tvDay_6);tvDayList.add(tvDay_7);tvDayList.add(tvDay_8);tvDayList.add(tvDay_9);tvDayList.add(tvDay_10);
        tvDayList.add(tvDay_11);tvDayList.add(tvDay_12);tvDayList.add(tvDay_13);tvDayList.add(tvDay_14);tvDayList.add(tvDay_15);tvDayList.add(tvDay_16);tvDayList.add(tvDay_17);tvDayList.add(tvDay_18);tvDayList.add(tvDay_19);tvDayList.add(tvDay_20);
        tvDayList.add(tvDay_21);tvDayList.add(tvDay_22);tvDayList.add(tvDay_23);tvDayList.add(tvDay_24);tvDayList.add(tvDay_25);tvDayList.add(tvDay_26);tvDayList.add(tvDay_27);tvDayList.add(tvDay_28);tvDayList.add(tvDay_29);tvDayList.add(tvDay_30);
        tvDayList.add(tvDay_31);tvDayList.add(tvDay_32);tvDayList.add(tvDay_33);tvDayList.add(tvDay_34);tvDayList.add(tvDay_35);tvDayList.add(tvDay_36);tvDayList.add(tvDay_37);tvDayList.add(tvDay_38);tvDayList.add(tvDay_39);tvDayList.add(tvDay_40);
        tvDayList.add(tvDay_41);tvDayList.add(tvDay_42);

        vCircleBigList = new ArrayList<>(42);
        vCircleBigList.add(vCircleBig_1);vCircleBigList.add(vCircleBig_2);vCircleBigList.add(vCircleBig_3);vCircleBigList.add(vCircleBig_4);vCircleBigList.add(vCircleBig_5);vCircleBigList.add(vCircleBig_6);vCircleBigList.add(vCircleBig_7);vCircleBigList.add(vCircleBig_8);vCircleBigList.add(vCircleBig_9);vCircleBigList.add(vCircleBig_10);
        vCircleBigList.add(vCircleBig_11);vCircleBigList.add(vCircleBig_12);vCircleBigList.add(vCircleBig_13);vCircleBigList.add(vCircleBig_14);vCircleBigList.add(vCircleBig_15);vCircleBigList.add(vCircleBig_16);vCircleBigList.add(vCircleBig_17);vCircleBigList.add(vCircleBig_18);vCircleBigList.add(vCircleBig_19);vCircleBigList.add(vCircleBig_20);
        vCircleBigList.add(vCircleBig_21);vCircleBigList.add(vCircleBig_22);vCircleBigList.add(vCircleBig_23);vCircleBigList.add(vCircleBig_24);vCircleBigList.add(vCircleBig_25);vCircleBigList.add(vCircleBig_26);vCircleBigList.add(vCircleBig_27);vCircleBigList.add(vCircleBig_28);vCircleBigList.add(vCircleBig_29);vCircleBigList.add(vCircleBig_30);
        vCircleBigList.add(vCircleBig_31);vCircleBigList.add(vCircleBig_32);vCircleBigList.add(vCircleBig_33);vCircleBigList.add(vCircleBig_34);vCircleBigList.add(vCircleBig_35);vCircleBigList.add(vCircleBig_36);vCircleBigList.add(vCircleBig_37);vCircleBigList.add(vCircleBig_38);vCircleBigList.add(vCircleBig_39);vCircleBigList.add(vCircleBig_40);
        vCircleBigList.add(vCircleBig_41);vCircleBigList.add(vCircleBig_42);

        vCircleSmallList = new ArrayList<>(42);
        vCircleSmallList.add(vCircleSmall_1);vCircleSmallList.add(vCircleSmall_2);vCircleSmallList.add(vCircleSmall_3);vCircleSmallList.add(vCircleSmall_4);vCircleSmallList.add(vCircleSmall_5);vCircleSmallList.add(vCircleSmall_6);vCircleSmallList.add(vCircleSmall_7);vCircleSmallList.add(vCircleSmall_8);vCircleSmallList.add(vCircleSmall_9);vCircleSmallList.add(vCircleSmall_10);
        vCircleSmallList.add(vCircleSmall_11);vCircleSmallList.add(vCircleSmall_12);vCircleSmallList.add(vCircleSmall_13);vCircleSmallList.add(vCircleSmall_14);vCircleSmallList.add(vCircleSmall_15);vCircleSmallList.add(vCircleSmall_16);vCircleSmallList.add(vCircleSmall_17);vCircleSmallList.add(vCircleSmall_18);vCircleSmallList.add(vCircleSmall_19);vCircleSmallList.add(vCircleSmall_20);
        vCircleSmallList.add(vCircleSmall_21);vCircleSmallList.add(vCircleSmall_22);vCircleSmallList.add(vCircleSmall_23);vCircleSmallList.add(vCircleSmall_24);vCircleSmallList.add(vCircleSmall_25);vCircleSmallList.add(vCircleSmall_26);vCircleSmallList.add(vCircleSmall_27);vCircleSmallList.add(vCircleSmall_28);vCircleSmallList.add(vCircleSmall_29);vCircleSmallList.add(vCircleSmall_30);
        vCircleSmallList.add(vCircleSmall_31);vCircleSmallList.add(vCircleSmall_32);vCircleSmallList.add(vCircleSmall_33);vCircleSmallList.add(vCircleSmall_34);vCircleSmallList.add(vCircleSmall_35);vCircleSmallList.add(vCircleSmall_36);vCircleSmallList.add(vCircleSmall_37);vCircleSmallList.add(vCircleSmall_38);vCircleSmallList.add(vCircleSmall_39);vCircleSmallList.add(vCircleSmall_40);
        vCircleSmallList.add(vCircleSmall_41);vCircleSmallList.add(vCircleSmall_42);
    }

    public void selectDay(int selectedPosition) {
        View oldCircleBig = vCircleBigList.get(mSelectedPosition);
        oldCircleBig.setVisibility(View.GONE);
        TextView oldTextView = tvDayList.get(mSelectedPosition);
        if (mSelectedPosition >= mStartPosition && mSelectedPosition <= mEndPosition) {
            if (mSelectedPosition == mTodayPosition) {
                oldTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
            } else {
                oldTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryText));
            }

        } else {
            oldTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorDivider));
        }

        mSelectedPosition = selectedPosition;
        View circleBig = vCircleBigList.get(selectedPosition);
        circleBig.setVisibility(View.VISIBLE);
        if (selectedPosition >= mStartPosition && selectedPosition <= mEndPosition) {
            circleBig.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.circle_solid_primary_color));
        } else {
            circleBig.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.circle_solid_divider_color));
        }
        TextView textView = tvDayList.get(selectedPosition);
        textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));

        //update RecyclerView
        try {
            ((MainActivity)getActivity()).updateCalendarFragmentList(mCalendarObjectList.get(selectedPosition), mCalendarPage);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
        }
    }

    public void setUpCalendarContent() {
        // Text of TextView and Small Circle
        for (int i = 0; i<42; i++) {
            TextView tvDay = tvDayList.get(i);
            Calendar calendar = mCalendarObjectList.get(i);
            tvDay.setText(Integer.toString(calendar.get(Calendar.DAY_OF_MONTH)));
            if (isThisMonth(i)) {
                tvDay.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryText));
            } else {
                tvDay.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorDivider));
            }

            Log.i("Checking by Content", "Year: "+ calendar.get(Calendar.YEAR)+", Month: "+ calendar.get(Calendar.MONTH)+", Day of Month: "+ calendar.get(Calendar.DAY_OF_MONTH));
            View circleSmall = vCircleSmallList.get(i);
            if (mDatabaseHelper.checkIfDayHasAnyContent(calendar)) {
                circleSmall.setVisibility(View.VISIBLE);
                if (isThisMonth(i)) {
                    circleSmall.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.circle_solid_primary_color));
                } else {
                    circleSmall.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.circle_solid_divider_color));
                }
            } else {
                circleSmall.setVisibility(View.GONE);
            }
        }
        // Select Day
    }

    private boolean isThisMonth(int position) {
        if (position >= mStartPosition && position <= mEndPosition) {
            return true;
        } else {
            return false;
        }
    }

    public int getSelectedPosition() {
        return mSelectedPosition;
    }

    public Calendar getInformationForCalendar() {
        return mCalendarObjectList.get(mSelectedPosition);
    }
}
