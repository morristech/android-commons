package org.mcxiaoke.commons.ui.fragment;

import org.mcxiaoke.commons.ui.R;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public abstract class ProgressFragment extends Fragment {
	static final int MESSAGE_ID_PROGRESS = 0;

	private final Handler mHandler = new ProgressHandler();
	private boolean mRestoredFragment;
	private boolean mPaused;

	protected abstract boolean isEmpty();

	protected boolean isPaused() {
		return mPaused;
	}

	protected void setEmptyText(View root, int resId) {
		TextView tv = (TextView) root.findViewById(R.id.empty_text);
		tv.setText(resId);
	}

	protected void setEmptyText(View root, CharSequence text) {
		TextView tv = (TextView) root.findViewById(R.id.empty_text);
		tv.setText(text);
	}

	protected void doShowEmptyView(View root, CharSequence text) {
		if (isEmpty()) {
			View emptyView = root.findViewById(R.id.empty_view);
			emptyView.setVisibility(View.VISIBLE);
			TextView emptyTextView = (TextView) emptyView
					.findViewById(R.id.empty_text);
			emptyTextView.setText(text);
			emptyTextView.setVisibility(View.VISIBLE);
			View emptyProgressView = emptyView
					.findViewById(R.id.empty_progress);
			emptyProgressView.setVisibility(View.GONE);

		}
	}

	protected void doShowEmptyViewProgress(View root) {
		if (isEmpty()) {
			View emptyView = root.findViewById(R.id.empty_view);
			emptyView.setVisibility(View.VISIBLE);
			TextView emptyTextView = (TextView) emptyView
					.findViewById(R.id.empty_text);
			emptyTextView.setVisibility(View.GONE);
			View emptyProgressView = emptyView
					.findViewById(R.id.empty_progress);
			emptyProgressView.setVisibility(View.VISIBLE);
		}
	}

	protected final void doShowEmptyViewProgressDelayed() {
		if ((isAdded()) && (!isPaused())) {
			View root = getView();
			if (root != null) {
				doShowEmptyViewProgress(root);
			}
		}
	}

	protected final void removeProgressViewMessages() {
		this.mHandler.removeMessages(0);
	}

	protected void showContent(View root) {
		removeProgressViewMessages();
		View emptyView = root.findViewById(R.id.empty_view);
		emptyView.setVisibility(View.GONE);
	}

	protected void showEmptyView(View root, CharSequence text) {
		removeProgressViewMessages();
		doShowEmptyView(root, text);
	}

	protected void showEmptyViewProgress(View root) {
		doShowEmptyViewProgress(root);
	}

	protected void showEmptyViewProgress(View root, CharSequence text) {
		if (isEmpty()) {
			TextView emptyProgressTextView = (TextView) root
					.findViewById(R.id.empty_progress_text);
			emptyProgressTextView.setText(text);
			showEmptyViewProgress(root);
		}
	}

	protected void showEmptyViewProgressDelayed() {
		Message msg = mHandler.obtainMessage(MESSAGE_ID_PROGRESS);
		mHandler.sendMessageDelayed(msg, 800L);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			this.mRestoredFragment = true;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onPause() {
		super.onPause();
		mPaused = true;
	}

	@Override
	public void onResume() {
		super.onResume();
		mPaused = false;
		if(mRestoredFragment){
			//TODO
		}
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@SuppressLint("HandlerLeak")
	class ProgressHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			doShowEmptyViewProgressDelayed();
		}

	}

}
