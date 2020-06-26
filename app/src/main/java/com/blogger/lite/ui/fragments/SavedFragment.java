package com.blogger.lite.ui.fragments;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import com.blogger.lite.R;
import com.blogger.lite.ui.adapters.PostAdapter;
import com.blogger.lite.data.models.Post;
import com.blogger.lite.ui.viewmodels.SavedViewModel;


public class SavedFragment extends Fragment {
    List<Post> postList;
    PostAdapter pa;
    Context mContext;
    SavedViewModel viewModel;

    public SavedFragment() {
        // Required empty public constructor
    }

    public static SavedFragment newInstance() {
        SavedFragment fragment = new SavedFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_saved, container, false);
        mContext = container.getContext();

        ListView savedPostLists = (ListView) v.findViewById(R.id.savedPostslist);
        savedPostLists.setDivider(null);
        postList = new ArrayList<Post>();

        pa = new PostAdapter(container.getContext(),postList,false);

        savedPostLists.setAdapter(pa);

        //ViewModel
        viewModel = ViewModelProviders.of(this).get(SavedViewModel.class);
        viewModel.init(mContext);
        viewModel.getFavorites().observe(this, new Observer<List<Post>>() {
            @Override
            public void onChanged(@Nullable List<Post> posts) {
                postList.clear();
                postList.addAll(posts);
                pa.notifyDataSetChanged();
            }
        });

        return v;
    }
}
