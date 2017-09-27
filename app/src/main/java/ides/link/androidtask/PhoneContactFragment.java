package ides.link.androidtask;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.provider.ContactsContract.Contacts;

public class PhoneContactFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>{

    private static final int PHONE_LOADER_ID = 0;
    private static final String TAG = PhoneContactFragment.class.getSimpleName();

    private PhoneContactAdapter mAdapter;
    RecyclerView mRecyclerView;

    private final static String[] FROM_COLUMNS = {
                    Contacts.DISPLAY_NAME,
                    Contacts._ID,
                    Contacts.HAS_PHONE_NUMBER

    };


    public PhoneContactFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getSupportLoaderManager().initLoader(PHONE_LOADER_ID, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_phone_contact, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.number_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new PhoneContactAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getActivity(),
                Contacts.CONTENT_URI,
                FROM_COLUMNS,
                null,
                null,
                null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);

    }
}
