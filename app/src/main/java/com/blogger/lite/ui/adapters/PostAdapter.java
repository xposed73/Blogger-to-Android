package com.blogger.lite.ui.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.blogger.lite.data.AppDatabase;
import com.blogger.lite.data.dao.FavoriteDao;
import com.blogger.lite.data.models.Favorite;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.blogger.lite.data.models.Post;
import com.blogger.lite.ui.activities.PostDetail;
import com.blogger.lite.R;

public class PostAdapter extends ArrayAdapter<Post> {
    private Context mContext;
    private LayoutInflater mInflater;
    private Boolean isHome;
    private FavoriteDao dao;
    public PostAdapter(@NonNull Context context, List<Post> posts, Boolean isHome) {
        super(context, R.layout.row_news, posts);
        mContext = context;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.isHome = isHome;
        this.dao = AppDatabase.getDatabase(mContext).favoriteDao();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final ViewHolder holder;

        final Post post = getItem(position);

        convertView = mInflater.inflate(R.layout.row_news, parent, false);

        holder = new ViewHolder();

        holder.title = convertView.findViewById(R.id.NoticiaTitle);

        holder.title.setText(post.getTitle());

        holder.image = convertView.findViewById(R.id.postimage);

        holder.content = convertView.findViewById(R.id.card_view);

        //Favorite Button
        holder.favoriteButton = convertView.findViewById(R.id.favorite);

        String postImage = extractUrls(post.getContent());

        if (postImage != null){
            //Picasso.get(mContext).load(postImage).fit().into(holder.image);
            Picasso.get()
                    .load(postImage)
                    .into(holder.image);
        }
        else{
            holder.image.setVisibility(View.GONE);
            holder.favoriteButton.setVisibility(View.GONE);
        }

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext.getApplicationContext(), PostDetail.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Gson gson = new Gson();
                i.putExtra("post", gson.toJson(post));
                mContext.startActivity(i);
            }
        };

        holder.content.setOnClickListener(listener);

        //Favorite Button

        View.OnClickListener fl = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dao.existInFavorite(post.getId()) > 0){
                    holder.favoriteButton.setImageResource(R.drawable.ic_favorite_empty);
                    Favorite fav = new Favorite();
                    fav.setId(post.getId());
                    dao.delete(fav);

                    if (!isHome){
                        remove(post);
                        notifyDataSetChanged();
                    }
                }
                else{
                    holder.favoriteButton.setImageResource(R.drawable.ic_favorite);
                    Favorite fav = new Favorite();
                    fav.setId(post.getId());
                    dao.save(fav);
                }

            }
        };
        if (dao.existInFavorite(post.getId()) > 0){
            holder.favoriteButton.setImageResource(R.drawable.ic_favorite);

        }
        else{
            holder.favoriteButton.setImageResource(R.drawable.ic_favorite_empty);
        }

        holder.favoriteButton.setOnClickListener(fl);

        return convertView;
    }

    public static class ViewHolder{
        TextView title;
        ImageView image;
        ImageView favoriteButton;
        CardView content;
    }

    public String extractUrls(String input) {

        String result = null;
        Pattern pattern = Pattern.compile(
                "<img[^>]*src=[\\\\\\\"']([^\\\\\\\"^']*)");

        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            String src = matcher.group();
            int startIndex = src.indexOf("src=") + 5;
            if (result == null){
                result = src.substring(startIndex, src.length());
            }
        }
        return result;
    }
}
