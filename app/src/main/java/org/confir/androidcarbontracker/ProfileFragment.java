package org.confir.androidcarbontracker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Justin on 2/21/18.
 */

//Every fragment needs a layout file
public class ProfileFragment extends Fragment{
    //To set the layout for the fragment, we must override 2 methods
    //onCreateView() and onViewCreated()

    //onCreateView() returns the view for the fragment.
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, null);
    }

    //onViewCreated() allows for activity methods to be created
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
