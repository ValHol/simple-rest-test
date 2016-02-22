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
import com.valhol.SolstCodeExam.model.ContactDetailModel;
import com.valhol.SolstCodeExam.model.ContactModel;
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
        mContactsService.contactsList().enqueue(new Callback<List<ContactModel>>() {
            @Override
            public void onResponse(Call<List<ContactModel>> call, Response<List<ContactModel>> response) {
                updateLocalDatabase(response.body());
            }

            @Override
            public void onFailure(Call<List<ContactModel>> call, Throwable t) {
                Log.d("onFailure", t.getMessage());
            }
        });
    }

    private void updateLocalDatabase(List<ContactModel> contactModelList) {
        for (ContactModel contactModel : contactModelList) {
            Contact contact = getContactFromModel(contactModel);
            Phone phone = getPhoneFromModel(contactModel);

            mContactsDetailsService.contactsDetails(contact.getEmployeeId()).enqueue(new Callback<ContactDetailModel>() {
                @Override
                public void onResponse(Call<ContactDetailModel> call, Response<ContactDetailModel> response) {
                    ContactDetailModel contactDetailModel = response.body();
                    ContactDetails contactDetails = getContactDetailsFromModel(contactDetailModel);
                    Address address = getAddressFromModel(contactDetailModel);
                    mAddressDao.insertOrReplace(address);
                    contactDetails.setAddress(address);
                    mContactDetailsDao.insertOrReplace(contactDetails);
                    address.setContactDetails(contactDetails);
                    mAddressDao.update(address);
                }

                @Override
                public void onFailure(Call<ContactDetailModel> call, Throwable t) {

                }
            });

            mPhoneDao.insertOrReplace(phone);
            contact.setPhone(phone);
            mContactDao.insertOrReplace(contact);
            phone.setContact(contact);
            mPhoneDao.update(phone);
        }

        EventBus.getDefault().post(new FinishedEvent());
    }

    private Address getAddressFromModel(ContactDetailModel contactDetailModel) {
        return new Address(
                null,
                contactDetailModel.getAddress().getStreet(),
                contactDetailModel.getAddress().getCity(),
                contactDetailModel.getAddress().getState(),
                contactDetailModel.getAddress().getCountry(),
                contactDetailModel.getAddress().getZip(),
                contactDetailModel.getAddress().getLatitude(),
                contactDetailModel.getAddress().getLongitude(),
                null
        );
    }

    private ContactDetails getContactDetailsFromModel(ContactDetailModel contactDetailModel) {
        return new ContactDetails(
                null,
                contactDetailModel.getEmployeeId(),
                contactDetailModel.getFavorite(),
                contactDetailModel.getLargeImageURL(),
                contactDetailModel.getEmail(),
                contactDetailModel.getWebsite(),
                null
        );
    }

    private Contact getContactFromModel(ContactModel contactModel) {
        return new Contact(
                null,
                contactModel.getName(),
                contactModel.getEmployeeId(),
                contactModel.getCompany(),
                contactModel.getDetailsURL(),
                contactModel.getSmallImageURL(),
                contactModel.getBirthdate(),
                null,
                null
        );
    }

    private Phone getPhoneFromModel(ContactModel contactModel) {
        return new Phone(
                null,
                contactModel.getPhone().getWork(),
                contactModel.getPhone().getHome(),
                contactModel.getPhone().getMobile(),
                null
        );
    }

    @Subscribe
    public void onContactSelected(ContactSelectedEvent contactSelectedEvent) {

        long employeeId = contactSelectedEvent.employeeId;

        if (mTwoPane) {
            ContactDetailFragment fragment = getContactDetailFragment(employeeId);
            getFragmentManager().beginTransaction()
                    .replace(R.id.contact_detail_container, fragment)
                    .commit();

        } else {
            ContactDetailFragment fragment = getContactDetailFragment(employeeId);
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
