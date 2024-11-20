package com.telemedicine.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.VideoView;
import androidx.fragment.app.Fragment;

public class LifestyleFragment extends Fragment {
    private View next; // Button to navigate to Heart activity

    // Declare VideoView, ImageView, and Button elements for each video set
    private VideoView videoView0, videoView1, videoView2, videoView3, videoView4, videoView5;
    private ImageView videoCover0, videoCover1, videoCover2, videoCover3, videoCover4, videoCover5;
    private Button playButton0, playButton1, playButton2, playButton3, playButton4, playButton5;

    public LifestyleFragment() {
        // Required empty public constructor
    }

    public static LifestyleFragment newInstance(String param1, String param2) {
        LifestyleFragment fragment = new LifestyleFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lifestyle, container, false);

        // Initialize each VideoView, ImageView, and Button separately
        videoView0 = view.findViewById(R.id.videoView0);
        videoCover0 = view.findViewById(R.id.videoCover0);
        playButton0 = view.findViewById(R.id.playButton0);

        videoView1 = view.findViewById(R.id.videoView1);
        videoCover1 = view.findViewById(R.id.videoCover1);
        playButton1 = view.findViewById(R.id.playButton1);

        videoView2 = view.findViewById(R.id.videoView2);
        videoCover2 = view.findViewById(R.id.videoCover2);
        playButton2 = view.findViewById(R.id.playButton2);

        videoView3 = view.findViewById(R.id.videoView3);
        videoCover3 = view.findViewById(R.id.videoCover3);
        playButton3 = view.findViewById(R.id.playButton3);

        videoView4 = view.findViewById(R.id.videoView4);
        videoCover4 = view.findViewById(R.id.videoCover4);
        playButton4 = view.findViewById(R.id.playButton4);

        videoView5 = view.findViewById(R.id.videoView5);
        videoCover5 = view.findViewById(R.id.videoCover5);
        playButton5 = view.findViewById(R.id.playButton5);

        // Set up each video individually
        setupVideoPlayer(videoView0, videoCover0, playButton0, "video0");
        setupVideoPlayer(videoView1, videoCover1, playButton1, "video1");
        setupVideoPlayer(videoView2, videoCover2, playButton2, "video2");
        setupVideoPlayer(videoView3, videoCover3, playButton3, "video3");
        setupVideoPlayer(videoView4, videoCover4, playButton4, "video4");
        setupVideoPlayer(videoView5, videoCover5, playButton5, "video5");

        // Initialize the 'next' button and set up the click listener
        next = view.findViewById(R.id.heart);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent to navigate to the Heart activity
                Intent intent = new Intent(getActivity(), Heart.class);
                startActivity(intent);
            }
        });
        next = view.findViewById(R.id.blpressure);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent to navigate to the Heart activity
                Intent intent = new Intent(getActivity(), hearthealth.class);
                startActivity(intent);
            }
        });

        return view;
    }

    // Helper method to set up video URI and button click listener for a video set
    private void setupVideoPlayer(VideoView videoView, ImageView videoCover, Button playButton, String videoName) {
        int videoResource = getResources().getIdentifier(videoName, "raw", requireActivity().getPackageName());

        if (videoResource == 0) {
            Log.e("LifestyleFragment", "Video resource not found for: " + videoName);
            return;
        }

        Uri videoUri = Uri.parse("android.resource://" + requireActivity().getPackageName() + "/" + videoResource);
        videoView.setVideoURI(videoUri);

        playButton.setOnClickListener(v -> {
            videoCover.setVisibility(View.GONE);
            videoView.setVisibility(View.VISIBLE);

            if (!videoView.isPlaying()) {
                videoView.start();
            }
        });
    }
}

