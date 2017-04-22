package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments.Navigation_Drawer;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.LayoutColor;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Constants.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.DonationInterface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;

/**
 * Created by Eric on 03.04.2017.
 */

public class DonateFragment extends Fragment implements View.OnClickListener {

    // Layout
    Button bDonateOne;
    Button bDonateTwo;
    Button bDonateThree;

    // Variables
    DatabaseHelper mDatabaseHelper;

    // Interfaces
    DonationInterface mDonationInterface;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_donate, container, false);

        bDonateOne = (Button)layout.findViewById(R.id.bDonateOne);
        bDonateTwo = (Button)layout.findViewById(R.id.bDonateTwo);
        bDonateThree = (Button)layout.findViewById(R.id.bDonateThree);

        bDonateOne.setOnClickListener(this);
        bDonateTwo.setOnClickListener(this);
        bDonateThree.setOnClickListener(this);

        // Variables
        mDatabaseHelper = new DatabaseHelper(getActivity());
        LayoutColor layoutColor = new LayoutColor(getActivity(), mDatabaseHelper.getLayoutColorValue());

        // Color
        bDonateOne.setBackgroundColor(layoutColor.getLayoutColor());
        bDonateTwo.setBackgroundColor(layoutColor.getLayoutColor());
        bDonateThree.setBackgroundColor(layoutColor.getLayoutColor());

        // Text
        bDonateOne.setText(mDonationInterface.getDonationPrice(MyConstants.DONATION_TYPE_ONE)+" - "+(getActivity().getString(R.string.fragment_donate_one)));
        bDonateTwo.setText(mDonationInterface.getDonationPrice(MyConstants.DONATION_TYPE_TWO)+" - "+(getActivity().getString(R.string.fragment_donate_two)));
        bDonateThree.setText(mDonationInterface.getDonationPrice(MyConstants.DONATION_TYPE_THREE)+" - "+(getActivity().getString(R.string.fragment_donate_three)));

        return layout;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bDonateOne:
                mDonationInterface.startDonation("donate_one");
                break;
            case R.id.bDonateTwo:
                mDonationInterface.startDonation("donate_two");
                break;
            case R.id.bDonateThree:
                mDonationInterface.startDonation("donate_three");
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mDonationInterface = (DonationInterface) context;
    }
}
