package com.example.richtext;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.method.MovementMethod;
import android.widget.TextView;

import com.example.richtext.databinding.ActivityMvvmBinding;
import com.example.richtext.model.MVViewModel;
import com.shuyu.textutillib.listener.ITextViewShow;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MVVMActivity extends Activity {


    @BindView(R.id.mvvm_rich_text_title)
    TextView mvvmRichTextTitle;
    @BindView(R.id.mvvm_rich_text)
    TextView mvvmRichText;

    MVViewModel mvViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMvvmBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_mvvm);

        mvViewModel = new MVViewModel(this);
        binding.setViewmodel(mvViewModel);
        mvViewModel.setiTextViewShow(iTextViewShow);

        ButterKnife.bind(this);

    }

    ITextViewShow iTextViewShow = new ITextViewShow() {
        @Override
        public void setText(CharSequence charSequence) {
            mvViewModel.setLocalCurrentTextString(charSequence);
        }

        @Override
        public CharSequence getText() {
            return mvViewModel.getLocalCurrentTextString();
        }

        @Override
        public void setMovementMethod(MovementMethod movementMethod) {
            mvvmRichText.setMovementMethod(movementMethod);
        }

        @Override
        public void setAutoLinkMask(int flag) {
            mvViewModel.setLinkFlag(flag);
        }
    };
}
