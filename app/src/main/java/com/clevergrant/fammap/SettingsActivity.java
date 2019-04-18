package com.clevergrant.fammap;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

import com.clevergrant.client.ServerProxy;
import com.clevergrant.model.Model;
import com.google.android.gms.maps.GoogleMap;

public class SettingsActivity extends AppCompatActivity {

	Model m = Model.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		Switch switchLifeStory = findViewById(R.id.switch_life_story);
		switchLifeStory.setChecked(m.store.getLifeStoryLine(this));
		switchLifeStory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				m.store.setLifeStoryLine(buttonView.getContext(), isChecked);
			}
		});

		Switch switchFamilyTree = findViewById(R.id.switch_family_tree);
		switchFamilyTree.setChecked(m.store.getFamilyTreeLine(this));
		switchFamilyTree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				m.store.setFamilyTreeLine(buttonView.getContext(), isChecked);
			}
		});

		Switch switchSpouse = findViewById(R.id.switch_spouse);
		switchSpouse.setChecked(m.store.getSpouseLine(this));
		switchSpouse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				m.store.setSpouseLine(buttonView.getContext(), isChecked);
			}
		});

		ArrayAdapter<String> lifeStoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, m.colorResource.markerSpinnerColors);
		lifeStoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner spinnerLifeStory = findViewById(R.id.spinner_life_story);
		spinnerLifeStory.setAdapter(lifeStoryAdapter);
		spinnerLifeStory.setSelection(m.store.getLifeStoryColorPosition(this));
		spinnerLifeStory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				m.store.setLifeStoryColor(parent.getContext(), m.colorResource.lineColors.get(parent.getItemAtPosition(position).toString()));
				m.store.setLifeStoryColorPosition(parent.getContext(), position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		ArrayAdapter<String> familyTreeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, m.colorResource.markerSpinnerColors);
		familyTreeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner spinnerFamilyTree = findViewById(R.id.spinner_family_tree);
		spinnerFamilyTree.setAdapter(familyTreeAdapter);
		spinnerFamilyTree.setSelection(m.store.getFamilyTreeColorPosition(this));
		spinnerFamilyTree.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				m.store.setFamilyTreeColor(parent.getContext(), m.colorResource.lineColors.get(parent.getItemAtPosition(position).toString()));
				m.store.setFamilyTreeColorPosition(parent.getContext(), position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		ArrayAdapter<String> spouseAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, m.colorResource.markerSpinnerColors);
		spouseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner spinnerSpouse = findViewById(R.id.spinner_spouse);
		spinnerSpouse.setAdapter(spouseAdapter);
		spinnerSpouse.setSelection(m.store.getSpouseColorPosition(this));
		spinnerSpouse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				m.store.setSpouseColor(parent.getContext(), m.colorResource.lineColors.get(parent.getItemAtPosition(position).toString()));
				m.store.setSpouseColorPosition(parent.getContext(), position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		ArrayAdapter<String> mapAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, m.mapResource.types);
		mapAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner spinnerMap = findViewById(R.id.spinner_map_type);
		spinnerMap.setAdapter(mapAdapter);
		spinnerMap.setSelection(m.store.getMapTypePosition(this));
		spinnerMap.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				switch (parent.getItemAtPosition(position).toString()) {
					case "Satellite":
						m.store.setMapType(parent.getContext(), GoogleMap.MAP_TYPE_SATELLITE);
						break;
					case "Terrain":
						m.store.setMapType(parent.getContext(), GoogleMap.MAP_TYPE_TERRAIN);
						break;
					case "Hybrid":
						m.store.setMapType(parent.getContext(), GoogleMap.MAP_TYPE_HYBRID);
						break;
					default:
						m.store.setMapType(parent.getContext(), GoogleMap.MAP_TYPE_NORMAL);
						break;
				}
				m.store.setMapTypePosition(parent.getContext(), position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		Button buttonSync = findViewById(R.id.button_sync);
		buttonSync.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(Model.ResultCodes.SYNCHRONIZE, new Intent());
				finish();
			}
		});

		Button buttonLogout = findViewById(R.id.button_logout);
		buttonLogout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(Model.ResultCodes.LOGOUT, new Intent());
				finish();
			}
		});

		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		ActionBar ab = getSupportActionBar();
		if (ab != null) ab.setDisplayHomeAsUpEnabled(true);
	}
}
