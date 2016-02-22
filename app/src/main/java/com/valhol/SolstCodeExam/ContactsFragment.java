package com.valhol.SolstCodeExam;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import com.valhol.SolstCodeExam.dao.Contact;
import com.valhol.SolstCodeExam.dao.ContactDao;
import com.valhol.SolstCodeExam.events.FinishedEvent;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

import javax.inject.Inject;
import java.util.ArrayList;


public class ContactsFragment extends Fragment {

    @Bind(R.id.contacts_recycler)
    RecyclerView mContactsRecyclerView;

    @Inject
    ContactDao mContactDao;

    ArrayList<Contact> mContactsList;

    ContactsAdapter mContactsAdapter;

    public static ContactsFragment newInstance() {
        return new ContactsFragment();
    }

    public ContactsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        mContactsRecyclerView = (RecyclerView) view.findViewById(R.id.contacts_recycler);

        ((ContactsApplication) inflater.getContext().getApplicationContext()).getNetworkComponent().inject(this);

        mContactsRecyclerView.setLayoutManager(
                new LinearLayoutManager(
                        inflater.getContext(),
                        LinearLayoutManager.VERTICAL,
                        false
                ));

        setupList();

        return view;
    }

    private void setupList() {
        try {
            mContactsList = (ArrayList<Contact>) mContactDao.loadAll();
            mContactsAdapter = new ContactsAdapter(mContactsList);
            mContactsRecyclerView.setAdapter(mContactsAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void onFinishedEvent(FinishedEvent event) {
        setupList();
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
}
