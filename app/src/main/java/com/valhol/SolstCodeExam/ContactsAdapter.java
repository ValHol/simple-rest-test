package com.valhol.SolstCodeExam;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.valhol.SolstCodeExam.dao.Contact;
import com.valhol.SolstCodeExam.events.ContactSelectedEvent;
import com.valhol.SolstCodeExam.views.ContactItem;
import de.greenrobot.event.EventBus;

import java.util.ArrayList;

/**
 * Created by Valentín on 21-Feb-16.
 */
public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {
    ArrayList<Contact> mContactsList;

    public ContactsAdapter(ArrayList<Contact> contactList) {
        mContactsList = contactList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contactviewholder, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Contact contact = mContactsList.get(position);
        holder.bindContact(contact);
    }

    @Override
    public int getItemCount() {
        return mContactsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ContactItem mContactItem;

        public ViewHolder(View itemView) {
            super(itemView);
            mContactItem = (ContactItem) itemView;
        }

        public void bindContact(final Contact contact) {
            mContactItem.setContactName(contact.getName());
            mContactItem.setPhoneNumber(contact.getPhone().getMobile());
            mContactItem.setThumbnailUri(contact.getSmallImageURL());
            mContactItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventBus.getDefault().post(new ContactSelectedEvent(contact.getEmployeeId()));
                }
            });
        }
    }

}
