package com.clevergrant.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.clevergrant.fammap.AppCompatPreferenceActivity;
import com.clevergrant.fammap.R;
import com.clevergrant.fammap.SettingsActivity;
import com.clevergrant.model.Event;
import com.clevergrant.model.Model;
import com.clevergrant.model.Person;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class GoogleMapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

	AppCompatActivity mainActivity;

	private OnActionListener listener;

	private GoogleMap map;

	private ImageView displayedImage;
	private TextView displayedText;

	private ArrayList<Polyline> lines = new ArrayList<>();

	private Model m = Model.getInstance();

	public static final String SETTINGS = "com.example.clevergrant.fammap.SETTINGS";

	// Attach/Detach/Create Handlers

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		mainActivity = (AppCompatActivity) context;
		if (context instanceof OnActionListener) {
			listener = (OnActionListener) context;
		} else {
			throw new RuntimeException(context.toString()
					+ " must implement OnActionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		listener = null;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	// Initializers

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_google_map, container, false);

		displayedImage = view.findViewById(R.id.imageView);
		displayedText = view.findViewById(R.id.textView);

		SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

		setHasOptionsMenu(true);

		if (mapFragment != null) mapFragment.getMapAsync(this);
		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.map_menu, menu);
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {

		map = googleMap;

		for (Event event : m.getEvents().values()) {
			LatLng ll = new LatLng(event.getLatitude(), event.getLongitude());

			BitmapDescriptor color = defineColor(event.getEventType());

			Person person = m.getPersons().get(event.getPersonID());
			if (person != null)
				map.addMarker(
						new MarkerOptions()
								.position(ll)
								.title(person.getFirstName() + " " + person.getLastName())
								.icon(color)
				).setTag(event);
		}

		map.setOnMarkerClickListener(this);
	}

	// Map Action Handlers

	@SuppressLint("DefaultLocale")
	@Override
	public boolean onMarkerClick(Marker marker) {
		Event e = (Event) marker.getTag();
		if (e != null) {
			Person p = m.getPersons().get(e.getPersonID());
			if (p != null) {

				String type = defineType(e.getEventType());
				int genderIcon = defineGenderIcon(p.getGender());

				for (Polyline l : lines) l.remove();

				Person spouse = m.getPersons().get(p.getSpouse());

				if (spouse != null) addRelativeLine(spouse, marker.getPosition(), 0xFFB388FF, 12);
				addFamilyLines(p, marker.getPosition(), 0xFFB2FF59, 15);
				addLifeStoryLines(p);

				displayedImage.setBackgroundResource(genderIcon);
				displayedText.setText(String.format("%s %s\n%s in %d\n%s, %s", p.getFirstName(), p.getLastName(), type, e.getYear(), e.getCity(), e.getCountry()));
			}
		}
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

			case R.id.action_search:
				return true;

			case R.id.action_filter:
				return true;

			case R.id.action_settings:
				Intent intent = new Intent(getActivity(), SettingsActivity.class);
				startActivity(intent);
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}

	// Callback Interface

	public interface OnActionListener {
//		void act();
	}

	// Line Drawers

	private void addRelativeLine(Person relative, LatLng thisLoc, int color, int width) {
		Event relativeEvent = (relative == null) ? null : m.getEvent(relative.getEvents().firstEntry().getValue());
		if (relativeEvent != null) {
			LatLng EventLoc = new LatLng(relativeEvent.getLatitude(), relativeEvent.getLongitude());
			PolylineOptions lineOpt = new PolylineOptions()
					.add(thisLoc, EventLoc)
					.width(width).color(color);
			lines.add(map.addPolyline(lineOpt));
		}
	}

	private void addFamilyLines(Person p, LatLng thisLoc, int color, int width) {
		Person dad = m.getPersons().get(p.getFather());
		Person mom = m.getPersons().get(p.getMother());

		if (dad != null) {
			addRelativeLine(dad, thisLoc, color, width);
			Event relativeEvent = m.getEvent(dad.getEvents().firstEntry().getValue());
			if (relativeEvent != null) {
				LatLng EventLoc = new LatLng(relativeEvent.getLatitude(), relativeEvent.getLongitude());
				addFamilyLines(dad, EventLoc, color, width - 3);
			}
		}
		if (mom != null) {
			addRelativeLine(mom, thisLoc, color, width);
			Event relativeEvent = m.getEvent(mom.getEvents().firstEntry().getValue());
			if (relativeEvent != null) {
				LatLng EventLoc = new LatLng(relativeEvent.getLatitude(), relativeEvent.getLongitude());
				addFamilyLines(mom, EventLoc, color, width - 3);
			}
		}
	}

	private void addLifeStoryLines(Person p) {
		ArrayList<LatLng> coordinates = new ArrayList<>();

		for (String eventId : p.getEvents().values()) {
			Event e = m.getEvent(eventId);
			coordinates.add(new LatLng(e.getLatitude(), e.getLongitude()));
		}

		LatLng[] coordinatesArr = coordinates.toArray(new LatLng[0]);

		if (coordinatesArr != null) {
			LatLng last = coordinatesArr[0];
			for (LatLng coordinate : coordinatesArr) {
				if (coordinate != last) {
					PolylineOptions lineOpt = new PolylineOptions()
							.add(last, coordinate)
							.width(12).color(0xFF40C4FF);
					lines.add(map.addPolyline(lineOpt));
				}
				last = coordinate;
			}
		}
	}

	// Switch Functions

	private BitmapDescriptor defineColor(String eventType) {
		switch (eventType.toLowerCase()) {
			case "birth":
				return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
			case "death":
				return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
			case "marriage":
				return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET);
			default:
				return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);
		}
	}

	private String defineType(String t) {
		switch (t) {
			case "birth":
				return "Born";
			case "death":
				return "Died";
			case "marriage":
				return "Married";
			default:
				return "Event";
		}
	}

	private int defineGenderIcon(String g) {
		switch (g) {
			case "m":
				return R.drawable.ic_human_male;
			case "f":
				return R.drawable.ic_human_female;
			default:
				return R.drawable.ic_android_green_72dp;
		}
	}
}
