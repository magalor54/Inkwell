package galondo.uv.inkwell;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
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


public class BusquedasAdapter extends RecyclerView.Adapter<BusquedasAdapter.ViewHolder> implements Filterable {

    private LayoutInflater inflater;
    private ArrayList<Libro> libros;
    private ArrayList<Libro> libros_filtrados;
    View.OnClickListener mOnItemClickListener;

    public BusquedasAdapter(Context ctx, ArrayList<Libro> libros){

        inflater = LayoutInflater.from(ctx);
        libros_filtrados = libros;
        this.libros = libros;
    }

    @Override
    public BusquedasAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.busquedas_recycleview, parent, false);

        ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(mOnItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(BusquedasAdapter.ViewHolder holder, int position) {


        // below line is use to set image from URL in our image view.
        if(!libros_filtrados.get(position).getImage_drawable().isEmpty()) {
            Picasso.get().load(libros_filtrados.get(position).getImage_drawable()).resize(200, 200).into(holder.iv);
        }else
            Picasso.get().load("https://via.placeholder.com/300x400").into(holder.iv);
        //holder.iv.setImageResource(imageModelArrayList.get(position).getImage_drawable());
        holder.time.setText(libros_filtrados.get(position).getName());
        holder.autor.setText(libros_filtrados.get(position).getAutor());
    }

    @Override
    public int getItemCount() {
        return libros_filtrados.size();
    }

    public void setOnItemClickListener(View.OnClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView time;
        TextView autor;
        ImageView iv;

        public ViewHolder(View itemView) {
            super(itemView);

            time = (TextView) itemView.findViewById(R.id.titulo);
            iv = (ImageView) itemView.findViewById(R.id.iv);
            autor = (TextView) itemView.findViewById(R.id.autor);
            // Put this line in the code of the ViewHolder constructor
            itemView.setTag(this);
        }

    }

    public ArrayList getInfoPosition(int pos){

        ArrayList info = new ArrayList<>();

        info.add(libros_filtrados.get(pos).getName());
        info.add(libros_filtrados.get(pos).getImage_drawable());
        info.add(libros_filtrados.get(pos).getISBN());
        info.add(libros_filtrados.get(pos).getAutor());
        info.add(libros_filtrados.get(pos).getGenero());
        info.add(libros_filtrados.get(pos).getBookInfo());

        return info;
    }

    @Override
    public Filter getFilter() {
        return new Filter(){
            @Override
            protected FilterResults performFiltering(CharSequence charSequence){
                String chartString = charSequence.toString();
                if(chartString.isEmpty()){
                    libros_filtrados = libros;
                } else{
                    ArrayList<Libro> filteredList = new ArrayList<>();
                    for(Libro libr: libros_filtrados){
                        if(libr.getName().toLowerCase().contains(chartString.toLowerCase())){
                            filteredList.add(libr);
                        }
                    }
                    libros_filtrados = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = libros_filtrados;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                libros_filtrados = (ArrayList<Libro>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}
