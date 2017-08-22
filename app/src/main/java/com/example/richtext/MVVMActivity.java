package com.example.richtext;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.example.richtext.databinding.ActivityMvvmBinding;
import com.example.richtext.model.MVViewModel;

public class MVVMActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMvvmBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_mvvm);
        binding.setViewmodel(new MVViewModel(this));
    }
}
