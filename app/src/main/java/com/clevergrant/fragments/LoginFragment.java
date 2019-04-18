package com.clevergrant.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.clevergrant.fammap.R;
import com.clevergrant.model.Model;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnActionListener} interface
 * to handle interaction events.
 */
public class LoginFragment extends Fragment {

	private OnActionListener actionListener;

	public void setOnActionListener(AppCompatActivity activity) {
		actionListener = (OnActionListener) activity;
	}

	public LoginFragment() {
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof OnActionListener) {
			actionListener = (OnActionListener) context;
		} else {
			throw new RuntimeException(context.toString()
					+ " must implement OnActionListener");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	private TextWatcher watcher(final View v, final Model m, final String modelVariableName) {
		return new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				switch (modelVariableName) {

					case "serverHost":
						Model.setHostIp(s.toString());
						break;

					case "serverPort":
						Model.setHostPort(s.toString());
						break;

					case "username":
						m.setUserName(s.toString());
						break;

					case "password":
						m.setPassword(s.toString());
						break;

					case "firstName":
						m.setFirstName(s.toString());
						break;

					case "lastName":
						m.setLastName(s.toString());
						break;

					case "email":
						m.setEmail(s.toString());
						break;

					default:
						break;
				}

				checkValues(v, m);
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		};
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		final View v = inflater.inflate(R.layout.fragment_login, container, false);

		final Model m = Model.getInstance();
		Model.clear();

		m.fromSharedPref(getActivity());

		checkValues(v, m);

		EditText serverHostField = v.findViewById(R.id.login_domain);
		serverHostField.addTextChangedListener(watcher(v,m,"serverHost"));
		serverHostField.setText(Model.getHostIp());

		EditText serverPortField = v.findViewById(R.id.login_port);
		serverPortField.addTextChangedListener(watcher(v,m,"serverPort"));
		serverPortField.setText(Model.getHostPort());

		EditText usernameField = v.findViewById(R.id.login_user);
		usernameField.addTextChangedListener(watcher(v,m,"username"));
		usernameField.setText(Model.getUser().getUserName());

		EditText passwordField = v.findViewById(R.id.login_pass);
		passwordField.addTextChangedListener(watcher(v,m,"password"));

		EditText firstNameField = v.findViewById(R.id.login_first);
		firstNameField.addTextChangedListener(watcher(v,m,"firstName"));

		EditText lastNameField = v.findViewById(R.id.login_last);
		lastNameField.addTextChangedListener(watcher(v,m,"lastName"));

		EditText emailField = v.findViewById(R.id.login_email);
		emailField.addTextChangedListener(watcher(v,m,"email"));

		RadioGroup genderGroup = v.findViewById(R.id.login_gender);
		genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				RadioButton checkedButton = group.findViewById(checkedId);
				m.setGender(String.valueOf(checkedButton.getText().charAt(0)).toLowerCase());
				checkValues(v, m);
			}
		});

		Button registerButton = v.findViewById(R.id.login_button_register);
		registerButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (actionListener != null) {
					actionListener.OnRegister();
				}
			}
		});

		Button loginButton = v.findViewById(R.id.login_button_login);
		loginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (actionListener != null) {
					actionListener.OnLogin();
				}
			}
		});

		return v;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		actionListener = null;
	}

	public interface OnActionListener {
		void OnRegister();
		void OnLogin();
	}

	public void checkValues(View v, Model m) {

		boolean enabled =
				!Model.getHostIp().isEmpty() &&
						!Model.getHostPort().isEmpty() &&
						!m.getUserName().isEmpty() &&
						!m.getPassword().isEmpty();

		Button loginButton = v.findViewById(R.id.login_button_login);
		loginButton.setEnabled(enabled);

		Button registerButton = v.findViewById(R.id.login_button_register);
		registerButton.setEnabled(
				enabled &&
				!m.getFirstName().isEmpty() &&
				!m.getLastName().isEmpty() &&
				!m.getEmail().isEmpty() &&
				!m.getGender().isEmpty());
	}

}
