package ides.link.androidtask;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
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


import butterknife.BindView;
import butterknife.ButterKnife;
import ides.link.androidtask.adapter.PhoneContactAdapter;
import ides.link.androidtask.utilities.CommonUtilities;

import static android.support.v4.content.PermissionChecker.checkSelfPermission;

public class PhoneContactFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>{

    private static final int PHONE_LOADER_ID = 0;
    private static final String TAG = PhoneContactFragment.class.getSimpleName();
    public static final int PERMISSIONS_REQUEST_ACCESS_READ_CONTACTS = 98;
    private PhoneContactAdapter mAdapter;
    @BindView(R.id.number_recycler_view)RecyclerView mRecyclerView;

    private final static String[] FROM_COLUMNS = {
                    Contacts.DISPLAY_NAME,
                    Contacts._ID,
                    Contacts.HAS_PHONE_NUMBER

    };


    public PhoneContactFragment() {
        // Required empty public constructor
        //todo fix runtime permission and ui
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.READ_CONTACTS},
                    PERMISSIONS_REQUEST_ACCESS_READ_CONTACTS);
        } else {
            getActivity().getSupportLoaderManager().initLoader(PHONE_LOADER_ID, null, this);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_phone_contact, container, false);
        ButterKnife.bind(this, view);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PERMISSIONS_REQUEST_ACCESS_READ_CONTACTS) {
            if (permissions[0].equals(Manifest.permission.READ_CONTACTS)
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getActivity().getSupportLoaderManager().initLoader(PHONE_LOADER_ID, null, this);
            } else {
                CommonUtilities.showPopupMessage(getActivity(),
                        getResources().getString(R.string.no_permission),
                        getResources().getString(R.string.no_permission_msg));
            }
        }
    }

}
