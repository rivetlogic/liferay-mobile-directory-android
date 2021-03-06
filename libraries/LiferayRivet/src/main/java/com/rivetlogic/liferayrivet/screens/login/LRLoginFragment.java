package com.rivetlogic.liferayrivet.screens.login;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.rivetlogic.liferayrivet.R;
import com.rivetlogic.liferayrivet.component.ShapeRoundRectStroke;
import com.rivetlogic.liferayrivet.component.StateListLoginOptionsBackground;
import com.rivetlogic.liferayrivet.component.StateListRect;
import com.rivetlogic.liferayrivet.util.SettingsUtil;

import org.json.JSONObject;

/**
 * Created by lorenz on 1/13/15.
 */
public class LRLoginFragment extends Fragment {

    private static final String KEY_STYLE_ID = "com.rivetlogic.liferay.screens.login.LRLoginFragment_styleResId";

    private LRLoginFragmentCallback listener;

    private int styleResId;

    private int backgroundRes;
    private int topImageRes;
    private int bottomImageRes;

    private EditText user;
    private EditText password;
    private TextView submitButton;

    private TextView option1;
    private TextView option2;
    private View optionDiv;
    private String option1Text;
    private String option2Text;

    private String buttonText;
    private int buttonTextColor;
    private int buttonBackgroundColor;

    private int editTextColor;
    private int editTextHintColor;

    private String userTextHint;
    private int userDrawable;

    private String passwordTextHint;
    private int passwordDrawable;

    private int optionsColor;

    public interface LRLoginFragmentCallback {

        public void onLoginPreExecute();

        public void onLoginSuccess(JSONObject obj);

        public void onLoginCancel(String error);

        public void onLoginOption1Clicked();

        public void onLoginOption2Clicked();
    }

    public static LRLoginFragment newInstance() {
        return new LRLoginFragment();
    }

    public static LRLoginFragment newInstance(int styleResId) {
        LRLoginFragment fragment = new LRLoginFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_STYLE_ID, styleResId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.listener = (LRLoginFragmentCallback) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        styleResId = R.style.LRThemeLoginViewDefault;
        setStyledAttributes();
        Bundle args = getArguments();
        if (args != null && args.containsKey(KEY_STYLE_ID)) {
            styleResId = args.getInt(KEY_STYLE_ID);
            if (styleResId > 0)
                setStyledAttributes();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.lr_fragment_login, null);

        submitButton = (TextView) v.findViewById(R.id.lr_login_submit);
        user = (EditText) v.findViewById(R.id.lr_login_user);
        password = (EditText) v.findViewById(R.id.lr_login_password);

        optionDiv = v.findViewById(R.id.lr_login_options_div);
        option1 = (TextView) v.findViewById(R.id.lr_login_option_1);
        option2 = (TextView) v.findViewById(R.id.lr_login_option_2);

        float radius = getResources().getDimension(R.dimen.corner_radius);
        float strokeWidth = getResources().getDimension(R.dimen.stroke_width);
        submitButton.setBackground(new StateListRect(buttonBackgroundColor, .08f, radius, 255));
        submitButton.setTextColor(buttonTextColor);
        submitButton.setText(buttonText);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        ImageView background = (ImageView) v.findViewById(R.id.lr_login_background);
        background.setImageResource(backgroundRes);

        ImageView topImage = (ImageView) v.findViewById(R.id.lr_login_image_top);
        topImage.setImageResource(topImageRes);

        ImageView bottomImage = (ImageView) v.findViewById(R.id.lr_login_image_bottom);
        bottomImage.setImageResource(bottomImageRes);

        Drawable textDrawable = new ShapeRoundRectStroke(buttonBackgroundColor, 0f, radius, radius, radius, radius, 255, strokeWidth);
        user.setBackground(textDrawable);
        password.setBackground(textDrawable);

        user.setHint(userTextHint);
        user.setTextColor(editTextColor);
        user.setHintTextColor(editTextHintColor);
        if(userDrawable > 0)
        user.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(userDrawable), null, null, null);

        password.setHint(passwordTextHint);
        password.setTextColor(editTextColor);
        password.setHintTextColor(editTextHintColor);
        if(passwordDrawable > 0)
        password.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(passwordDrawable), null, null, null);

        optionDiv.setBackgroundColor(optionsColor);
        option1.setTextColor(optionsColor);
        option2.setTextColor(optionsColor);

        option1.setText(option1Text);
        option1.setBackground(new StateListLoginOptionsBackground(new ColorDrawable(optionsColor)));
        option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onLoginOption1Clicked();
            }
        });
        option2.setText(option2Text);
        option2.setBackground(new StateListLoginOptionsBackground(new ColorDrawable(optionsColor)));
        option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onLoginOption2Clicked();
            }
        });
        if(option1Text == null || option2Text == null)
            optionDiv.setVisibility(View.GONE);

        user.setText(SettingsUtil.getLogin());
        password.setText(SettingsUtil.getPassword());

        return v;
    }

    private void setStyledAttributes() {
        TypedArray a = getActivity().getApplicationContext().obtainStyledAttributes(styleResId, R.styleable.LRLoginView);
        if(a != null) {
            try {

                backgroundRes = a.getResourceId(R.styleable.LRLoginView_lrScreenLoginImageBackground, backgroundRes);
                topImageRes = a.getResourceId(R.styleable.LRLoginView_lrScreenLoginImageTop, topImageRes);
                bottomImageRes = a.getResourceId(R.styleable.LRLoginView_lrScreenLoginImageBottom, bottomImageRes);

                buttonTextColor = a.getColor(R.styleable.LRLoginView_lrScreenLoginButtonTextColor, buttonTextColor);
                String src = a.getString(R.styleable.LRLoginView_lrScreenLoginButtonText);
                if (src != null)
                    buttonText = src;
                buttonBackgroundColor = a.getColor(R.styleable.LRLoginView_lrScreenLoginButtonBackgroundColor, buttonBackgroundColor);

                editTextColor = a.getColor(R.styleable.LRLoginView_lrScreenLoginEditTextColor, editTextColor);
                editTextHintColor = a.getColor(R.styleable.LRLoginView_lrScreenLoginEditTextHintColor, editTextHintColor);

                src = a.getString(R.styleable.LRLoginView_lrScreenLoginUserTextHint);
                if (src != null)
                    userTextHint = src;
                userDrawable = a.getResourceId(R.styleable.LRLoginView_lrScreenLoginUserDrawable, 0);

                src = a.getString(R.styleable.LRLoginView_lrScreenLoginPasswordTextHint);
                if (src != null)
                    passwordTextHint = src;
                passwordDrawable = a.getResourceId(R.styleable.LRLoginView_lrScreenLoginPasswordDrawable, 0);

                optionsColor = a.getColor(R.styleable.LRLoginView_lrScreenLoginOptionsTextColor, optionsColor);
                option1Text = a.getString(R.styleable.LRLoginView_lrScreenLoginOption1Text);
                option2Text = a.getString(R.styleable.LRLoginView_lrScreenLoginOption2Text);

            } finally {
                a.recycle();
            }
        }
    }

    public void autoLogin(String e, String p) {
        user.setText(e);
        password.setText(p);
        login();
    }

    private void login() {
        SettingsUtil.setLogin(user.getText().toString());
        SettingsUtil.setPassword(password.getText().toString());
        UserTask userTask = new UserTask(callback);
        userTask.execute();
    }

    private UserTask.UserTaskCallback callback = new UserTask.UserTaskCallback() {
        @Override
        public void onPreExecute() {
            listener.onLoginPreExecute();
        }

        @Override
        public void onSuccess(JSONObject obj) {
            listener.onLoginSuccess(obj);
        }

        @Override
        public void onCancel(String error) {
            listener.onLoginCancel(error);
        }
    };

}