package com.valhol.SolstCodeExam;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.squareup.picasso.Picasso;
import com.valhol.SolstCodeExam.dao.*;
import com.valhol.SolstCodeExam.events.BackToContactsEvent;
import de.greenrobot.event.EventBus;

import javax.inject.Inject;
import java.util.ArrayList;

public class ContactDetailFragment extends Fragment {

    public static final String ARG_EMPLOYEE_ID = "employeeid";

    @Inject
    ContactDetailsDao mContactDetailsDao;

    @Inject
    Context mContext;

    @Inject
    ContactDao mContactDao;

    @Bind(R.id.contactName)
    TextView mContactName;

    @Bind(R.id.contactCompany)
    TextView mContactCompany;

    @Bind(R.id.homePhone)
    TextView mHomePhone;

    @Bind(R.id.workPhone)
    TextView mWorkPhone;

    @Bind(R.id.mobilePhone)
    TextView mMobilePhone;

    @Bind(R.id.contactStreet)
    TextView mContactStreet;

    @Bind(R.id.fullSizePicture)
    ImageView mPicture;

    @Bind(R.id.contactCity)
    TextView mContactCity;

    @Bind(R.id.email)
    TextView mEmail;

    @Bind(R.id.website)
    TextView mWebsite;

    @Bind(R.id.birthday)
    TextView mBirthday;

    @Bind(R.id.favButton)
    ImageView mFavButton;

    public ContactDetailFragment() {
    }

    public static ContactDetailFragment newInstance(long employeeId) {
        ContactDetailFragment fragment = new ContactDetailFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_EMPLOYEE_ID, employeeId);
        fragment.setArguments(args);
        return fragment;
    }

    long mEmployeeId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(ARG_EMPLOYEE_ID)) {
            mEmployeeId = getArguments().getLong(ARG_EMPLOYEE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contact_detail, container, false);
        /**
         * Dependency injection
         */
        ((ContactsApplication) inflater.getContext().getApplicationContext()).getNetworkComponent().inject(this);

        /**
         * Bind the views
         */
        ButterKnife.bind(this, rootView);

        ArrayList<Contact> contacts =
                (ArrayList<Contact>) mContactDao.queryDeep("WHERE T.'EMPLOYEE_ID' = " + mEmployeeId, null);

        ArrayList<ContactDetails> contactsDetailsList =
                (ArrayList<ContactDetails>) mContactDetailsDao.queryDeep("WHERE T.'EMPLOYEE_ID' = " + mEmployeeId, null);

        /**
         * Get the contact data
         */
        Contact contact = contacts.get(0);
        Phone phones = contact.getPhone();
        final ContactDetails contactDetails = contactsDetailsList.get(0);
        Address address = contactDetails.getAddress();

        /**
         * Assign this data to the corresponding views
         */
        mContactName.setText(contact.getName());
        mContactCompany.setText(contact.getCompany());
        if (phones != null) {
            if (mHomePhone != null) mHomePhone.setText(phones.getHome());
            if (mWorkPhone != null) mWorkPhone.setText(phones.getWork());
            if (mMobilePhone != null) mMobilePhone.setText(phones.getMobile());
        }
        mBirthday.setText(contact.getBirthdate());
        mWebsite.setText(contactDetails.getWebsite());
        mEmail.setText(contactDetails.getEmail());

        if(address != null) {
            mContactStreet.setText(address.getStreet());
            mContactCity.setText(address.getCity() +
                    ", " +
                    address.getState() +
                    " " +
                    address.getZip());
        }

        /**
         * Load the picture with Picasso
         */
        Picasso.with(mContext).load(contactDetails.getLargeImageURL()).into(mPicture);

        /**
         * Set the click listener for the favorite button
         */
        mFavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contactDetails.setFavorite(!contactDetails.getFavorite());
                setFavButton(contactDetails);
            }
        });

        /**
         * Update the favorite button
         */
        setFavButton(contactDetails);

        ImageView backButton = (ImageView) rootView.findViewById(R.id.backButton);
        if (backButton != null) {
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventBus.getDefault().post(new BackToContactsEvent());
                }
            });
        }

        return rootView;
    }

    private void setFavButton(ContactDetails contactDetails) {
        int favButtonResource = contactDetails.getFavorite() ? R.drawable.ic_star_black_48dp : R.drawable.ic_star_border_black_48dp;
        mFavButton.setImageResource(favButtonResource);
    }
}
