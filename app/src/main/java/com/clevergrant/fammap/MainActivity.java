package com.clevergrant.fammap;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.clevergrant.client.ServerProxy;
import com.clevergrant.fragments.GoogleMapFragment;
import com.clevergrant.fragments.LoginFragment;
import com.clevergrant.model.Event;
import com.clevergrant.model.Model;
import com.clevergrant.model.Person;
import com.clevergrant.model.User;
import com.clevergrant.result.LoginResult;
import com.clevergrant.result.RegisterResult;
import com.clevergrant.tasks.SyncDataTask;

import androidx.navigation.Navigation;

public class MainActivity extends AppCompatActivity implements LoginFragment.OnActionListener, GoogleMapFragment.OnActionListener, ServerProxy.CallbackListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
			setContentView(R.layout.activity_main);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (findViewById(R.id.main_fragment) != null) {

			if (savedInstanceState != null) return;

			if (isLoggedIn()) setMapFragment(false);
			else setLoginFragment(false);
		}

		Toolbar myToolbar = findViewById(R.id.my_toolbar);
		setSupportActionBar(myToolbar);

		ActionBar bar = getSupportActionBar();
		if (bar != null) {
			bar.setTitle("Family Map");
		}
	}

	// Fragment Listener Methods

	@Override
	public void OnRegister() {
		ServerProxy server = new ServerProxy(this);
		server.register();
	}

	@Override
	public void OnLogin() {
		ServerProxy server = new ServerProxy(this);
		server.login();
	}

	// Server Callback Methods

	@Override
	public void registerComplete(RegisterResult result) {
		if (result == null) {
			Toast.makeText(this, "500: There was a problem getting your request.", Toast.LENGTH_SHORT).show();
			return;
		}

		if (result.getMessage() != null && !result.getMessage().isEmpty()) {
			Toast.makeText(this, result.getMessage(), Toast.LENGTH_LONG).show();
		} else {
			Model m = Model.getInstance();
			m.setPersonID(result.getPersonID());
			Model.setToken(result.getUserName(), result.getToken());

			SharedPreferences.Editor editor = getPreferences(this).edit();
			editor.putString("Authorization", result.getToken()).apply();
			editor.putString("ServerHost", Model.getHostIp()).apply();
			editor.putString("ServerPort", Model.getHostPort()).apply();
			editor.putString("Username", Model.getUser().getUserName()).apply();

			Toast.makeText(this, String.format("%s %s has been registered.", m.getFirstName(), m.getLastName()), Toast.LENGTH_SHORT).show();

			if (isLoggedIn()) setMapFragment(true);
		}
	}

	@Override
	public void loginComplete(LoginResult result) {
		if (result == null) {
			Toast.makeText(this, "500: There was a problem getting your request.", Toast.LENGTH_SHORT).show();
			return;
		}

		if (result.getMessage() != null && !result.getMessage().isEmpty()) {
			Toast.makeText(this, result.getMessage(), Toast.LENGTH_LONG).show();
		} else {
			Model m = Model.getInstance();

			m.setPersonID(result.getPersonID());

			Model.setToken(result.getUserName(), result.getToken());

			SharedPreferences.Editor editor = getPreferences(this).edit();
			editor.putString("Authorization", result.getToken()).apply();
			editor.putString("ServerHost", Model.getHostIp()).apply();
			editor.putString("ServerPort", Model.getHostPort()).apply();
			editor.putString("Username", Model.getUser().getUserName()).apply();

			if (this.isLoggedIn()) {
				ServerProxy server = new ServerProxy(this);
				server.syncData();
			}
		}
	}

	@Override
	public void dataFetched(SyncDataTask.SyncResult result) {
		if (result == null) {
			Toast.makeText(this, "500: There was a problem getting your request.", Toast.LENGTH_SHORT).show();
			return;
		}

		String message = result.getMessage();
		if (message != null && !message.isEmpty())
			Toast.makeText(this, message, Toast.LENGTH_LONG).show();
		else {

			Model m = Model.getInstance();

			for (Person p : result.getPersonsResult().getData()) m.addPerson(p);
			for (Event e : result.getEventsResult().getData()) {
				m.addEvent(e);
				Person p = m.getPersons().get(e.getPersonID());
				if (p != null) p.addEvent(e.getYear(), e.getEventID());
				else System.err.print("You ain't got a person to add the event to");
			}

			Model.setPerson(m.getPersons().get(m.getPersonID()));
			Person p = Model.getPerson();
			Model.setUser(new User(p.getDescendant(), null, null, p.getFirstName(), p.getLastName(), p.getGender(), p.getPersonID()));

			Toast t = Toast.makeText(this, String.format("Welcome back, %s %s!", m.getFirstName(), m.getLastName()), Toast.LENGTH_SHORT);
			t.setGravity(Gravity.TOP, 0, 15);
			t.show();

			setMapFragment(true);
		}
	}

	//Util

	public boolean isLoggedIn() {

		Model m = Model.getInstance();

		String userName = m.getUserName();

		if (userName != null && !userName.isEmpty()) {
			String token = getPreferences(this).getString("Authorization", "");
			if (token != null && !token.isEmpty()) {
				Model.setToken(userName, token);
				return true;
			}
		}

		return false;
	}

	public void setLoginFragment(boolean replace) {
		if (findViewById(R.id.main_fragment) != null) {
			LoginFragment lf = new LoginFragment();
			lf.setArguments(getIntent().getExtras());
			lf.setOnActionListener(this);

			FragmentTransaction t = getSupportFragmentManager().beginTransaction();

			if (replace) t.replace(R.id.main_fragment, lf).commit();
			else t.add(R.id.main_fragment, lf).commit();
		}
	}

	public void setMapFragment(boolean replace) {
		if (findViewById(R.id.main_fragment) != null) {
			GoogleMapFragment mf = new GoogleMapFragment();
			mf.setArguments(getIntent().getExtras());

			FragmentTransaction t = getSupportFragmentManager().beginTransaction();

			if (replace) t.replace(R.id.main_fragment, mf).commit();
			else t.add(R.id.main_fragment, mf).commit();
		}
	}

	private SharedPreferences getPreferences(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context);
	}

//	@Override
//	public void act() {
//
//	}
}
