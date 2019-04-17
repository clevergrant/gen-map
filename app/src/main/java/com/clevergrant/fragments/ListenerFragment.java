package com.clevergrant.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.clevergrant.fammap.R;

public class ListenerFragment extends Fragment {

	private OnActionListener listener;

	public ListenerFragment() {
	}

	public static ListenerFragment newInstance() {
		ListenerFragment fragment = new ListenerFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	public interface OnActionListener {
		// TODO: Update argument type and name
		void onAction(Uri uri);
	}
}
