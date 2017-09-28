package ides.link.androidtask.adapter;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.provider.ContactsContract.CommonDataKinds.Phone;

import butterknife.BindView;
import butterknife.ButterKnife;
import ides.link.androidtask.R;

/**
 * Created by Eman on 9/26/2017.
 */

public class PhoneContactAdapter extends RecyclerView.Adapter<PhoneContactAdapter.PhoneViewHolder> {

    Context mContext;
    Cursor mCursor;

    public PhoneContactAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public PhoneViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.phone_list_item, parent, false);
        return new PhoneViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PhoneViewHolder holder, int position) {

        if (!mCursor.moveToPosition(position))
            return;


        String contactId = mCursor.getString(mCursor.getColumnIndex(ContactsContract.Contacts._ID));
        String name = mCursor.getString(mCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        String hasPhone = mCursor.getString(mCursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
        String numbers = "";

        if (hasPhone.equalsIgnoreCase("1")) {
            Cursor phones = mContext.getContentResolver().query(
                    Phone.CONTENT_URI, new String[]{Phone.NUMBER},
                    Phone.CONTACT_ID + " = " + contactId, null, null);
            while (phones.moveToNext()) {
            numbers =numbers +  phones.getString(phones.getColumnIndex(Phone.NUMBER)) +"\n";
            }

            phones.close();
        }
        holder.nameTV.setText(name);
        holder.numberTV.setText(numbers);
    }

    @Override
    public int getItemCount() {
        if (mCursor == null)
            return 0;
        return mCursor.getCount();
    }

    class PhoneViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name_text_view)TextView nameTV;
        @BindView(R.id.number_text_view)TextView numberTV;

        public PhoneViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void swapCursor(Cursor mCursor) {
        this.mCursor = mCursor;
        notifyDataSetChanged();
    }
}
