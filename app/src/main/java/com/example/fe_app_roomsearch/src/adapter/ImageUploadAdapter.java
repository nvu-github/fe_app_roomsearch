package com.example.fe_app_roomsearch.src.adapter;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fe_app_roomsearch.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ImageUploadAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<String> mUri;

    private static final int VIEW_TYPE_IMAGE = 0;
    private static final int VIEW_TYPE_VIDEO = 1;

    public ImageUploadAdapter(Context context, List<String> mUri) {
        this.context = context;
        this.mUri = mUri;
    }

    @Override
    public int getItemViewType(int position) {
        // Kiểm tra định dạng của file, nếu là video thì trả về VIEW_TYPE_VIDEO,
        // còn nếu là ảnh thì trả về VIEW_TYPE_IMAGE
        String mimeType = context.getContentResolver().getType(Uri.parse(mUri.get(position)));
        if (mimeType != null && mimeType.startsWith("video/")) {
            return VIEW_TYPE_VIDEO;
        } else {
            return VIEW_TYPE_IMAGE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_VIDEO) {
            // Nếu là video thì sử dụng layout chứa VideoView
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_host_fragment_room, parent, false);
            return new VideoViewHolder(view);
        } else {
            // Nếu là ảnh thì sử dụng layout chứa ImageView
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_host_fragment_room, parent, false);
            return new ImageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        Gson gson = new Gson();

        String getListUri = sharedPreferences.getString("listUri", null);
        Type type = new TypeToken<List<String>>() {}.getType();
        List<String> listUri = gson.fromJson(getListUri, type);

        if (getItemViewType(position) == VIEW_TYPE_VIDEO) {
            // Nếu là video thì gán Uri cho VideoView
            Uri uri = Uri.parse(mUri.get(position));
            VideoViewHolder videoViewHolder = (VideoViewHolder) holder;
            videoViewHolder.videoView.setVideoURI(uri);
            videoViewHolder.videoView.start();

            videoViewHolder.btnDeleteVideoFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mUri.remove(videoViewHolder.getAdapterPosition());
                    notifyDataSetChanged();

                    listUri.remove(position);
                    String setListUri = gson.toJson(listUri);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("listUri", setListUri);
                    editor.apply();
                }
            });
        } else {
            // Nếu là ảnh thì gán Uri cho ImageView
            Uri uri = Uri.parse(mUri.get(position));
            ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
            Glide.with(imageViewHolder.imageView.getContext())
                    .load(uri)
                    .into(imageViewHolder.imageView);

            imageViewHolder.btnDeleteImageFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mUri.remove(imageViewHolder.getAdapterPosition());
                    notifyDataSetChanged();

                    listUri.remove(position);

                    String setListUri = gson.toJson(listUri);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("listUri", setListUri);
                    editor.apply();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mUri.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        ImageButton btnDeleteImageFile;

        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.reviewImageUpload);
            btnDeleteImageFile = itemView.findViewById(R.id.btnDeleteImageFile);
        }
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        public VideoView videoView;
        ImageButton btnDeleteVideoFile;

        public VideoViewHolder(View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.reviewVideoUpload);
            btnDeleteVideoFile = itemView.findViewById(R.id.btnDeleteVideoFile);
        }
    }
}
