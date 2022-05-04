package galondo.uv.inkwell;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<Libro> libros;
    View.OnClickListener mOnItemClickListener;

    public HorizontalAdapter(Context ctx, ArrayList<Libro> libros){

        inflater = LayoutInflater.from(ctx);
        this.libros = libros;
    }

    @Override
    public HorizontalAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.book_recycleview, parent, false);

        ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(mOnItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(HorizontalAdapter.ViewHolder holder, int position) {

        // below line is use to set image from URL in our image view.
        Picasso.get().load(libros.get(position).getImage_drawable()).resize(100,100).into(holder.iv);
        Log.d(null, "------------------>" + libros.get(position).getImage_drawable());
        //holder.iv.setImageResource(imageModelArrayList.get(position).getImage_drawable());
        holder.time.setText(libros.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return libros.size();
    }

    public void setOnItemClickListener(View.OnClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView time;
        ImageView iv;

        public ViewHolder(View itemView) {
            super(itemView);

            time = (TextView) itemView.findViewById(R.id.tv);
            iv = (ImageView) itemView.findViewById(R.id.iv);
            // Put this line in the code of the ViewHolder constructor
            itemView.setTag(this);
        }

    }

    public ArrayList getInfoPosition(int pos){

        ArrayList info = new ArrayList<>();

        info.add(libros.get(pos).getName());
        info.add(libros.get(pos).getImage_drawable());
        info.add(libros.get(pos).getISBN());
        info.add(libros.get(pos).getAutor());
        info.add(libros.get(pos).getGenero());
        info.add(libros.get(pos).getBookInfo());

        return info;
    }

}
