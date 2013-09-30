package com.envived.android.api.user.researchprofile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.MenuItem;
import com.envived.android.R;
import com.envived.android.api.user.UserProfileSettings;
import com.envived.android.utils.Utils;

public class ResearchSubProfileFragment extends SherlockFragment implements OnClickListener {

    public static final String ARG_SECTION_NUMBER = "1";
    private static final int RESULT_SETTINGS = 1;
    private static final String TAG = "ResearchSubProfileFragment";
    
    private EditText mAffiliation;
    private MultiAutoCompleteTextView mInterests;
    private Button mBtnSubmit;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        
        View view = inflater.inflate(R.layout.subprofile_research, container, false);
        
        mAffiliation = (EditText) view.findViewById(R.id.txt_affiliation_subprofile_research);
        mInterests = (MultiAutoCompleteTextView) view.findViewById(R.id.txt_interests_subprofile_research);
        mBtnSubmit = (Button) view.findViewById(R.id.btn_submit_subprofile_research);

        /*
         * SUGGESTIONS
         */
        ArrayAdapter<String> arrayTokens = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, Utils.suggestedWords);
        mInterests.setAdapter(arrayTokens);
        mInterests.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        Log.d(TAG, "CHOOSE INTERESTS REGISTER: " + mInterests.getText().toString());
        
        mBtnSubmit.setOnClickListener(this);
        
        return view;
    }


    @Override
    public void onClick(View v) {
        if (v == mBtnSubmit) {
            //new ProfileSaveTask().execute();
        }
    }


}
