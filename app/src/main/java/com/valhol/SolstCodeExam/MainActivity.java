package com.valhol.SolstCodeExam;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import com.valhol.SolstCodeExam.dao.*;
import com.valhol.SolstCodeExam.events.BackToContactsEvent;
import com.valhol.SolstCodeExam.events.ContactSelectedEvent;
import com.valhol.SolstCodeExam.events.FinishedEvent;
import com.valhol.SolstCodeExam.interfaces.ContactsDetailsService;
import com.valhol.SolstCodeExam.interfaces.ContactsService;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import javax.inject.Inject;
import java.util.List;

public class MainActivity extends FragmentActivity {

    @Inject
    SharedPreferences mSharedPreferences;
    @Inject
    Retrofit mRetrofit;
    @Inject
    ContactsService mContactsService;
    @Inject
    ContactsDetailsService mContactsDetailsService;
    @Inject
    DaoSession mDaoSession;
    @Inject
    ContactDao mContactDao;
    @Inject
    PhoneDao mPhoneDao;
    @Inject
    ContactDetailsDao mContactDetailsDao;
    @Inject
    AddressDao mAddressDao;

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((ContactsApplication) getApplication()).getNetworkComponent().inject(this);
        setContentView(R.layout.activity_contact_list);

        downloadContacts();

        if (findViewById(R.id.contact_detail_container) != null) {
            mTwoPane = true;
        } else {
            loadContentFragment(ContactsFragment.newInstance());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe
    public void onFinishedEvent(FinishedEvent event) {

    }

    private void downloadContacts() {
        mContactsService.contactsList().enqueue(new Callback<List<Contact>>() {
            @Override
            public void onResponse(Call<List<Contact>> call, Response<List<Contact>> response) {
                updateLocalDatabase(response.body());
            }

            @Override
            public void onFailure(Call<List<Contact>> call, Throwable t) {
                Log.d("onFailure", t.getMessage());
            }
        });
    }

    private void updateLocalDatabase(List<Contact> contactList) {
        for (Contact contact : contactList) {
            Log.d("Contact", contact.getCompany());
            mContactDao.insertOrReplaceInTx(contact);
            if (contact.getPhone() != null)
                mPhoneDao.insertOrReplaceInTx(contact.getPhone());

            mContactsDetailsService.contactsDetails(contact.getEmployeeId()).enqueue(new Callback<ContactDetails>() {
                @Override
                public void onResponse(Call<ContactDetails> call, Response<ContactDetails> response) {
                    ContactDetails contactDetails = response.body();
                    mContactDetailsDao.insertOrReplace(contactDetails);

                    Address address = contactDetails.getAddress();
                    if(address == null) return;
                    mAddressDao.insertOrReplace(address);
                    address.setContactDetails(contactDetails);
                    mAddressDao.update(address);
                }

                @Override
                public void onFailure(Call<ContactDetails> call, Throwable t) {

                }
            });
        }

        EventBus.getDefault().post(new FinishedEvent());
    }

    @Subscribe
    public void onContactSelected(ContactSelectedEvent contactSelectedEvent) {
        long employeeId = contactSelectedEvent.employeeId;
        ContactDetailFragment fragment = getContactDetailFragment(employeeId);

        if (mTwoPane) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.contact_detail_container, fragment)
                    .commit();
        } else {
            loadContentFragment(fragment);
        }
    }

    @Subscribe
    public void onBackToContactsEvent(BackToContactsEvent backToContactsEvent) {
        loadContentFragment(ContactsFragment.newInstance());
    }

    private ContactDetailFragment getContactDetailFragment(long employeeId) {
        Bundle arguments = new Bundle();
        arguments.putLong(ContactDetailFragment.ARG_EMPLOYEE_ID, employeeId);
        ContactDetailFragment fragment = new ContactDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    public void loadContentFragment(Fragment fragment) {
        getFragmentManager().beginTransaction()
                .replace(R.id.contact_list, fragment)
                .addToBackStack("BACKSTACK")
                .commit();
    }
}
