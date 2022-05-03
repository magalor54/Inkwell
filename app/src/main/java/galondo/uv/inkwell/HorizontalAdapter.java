package galondo.uv.inkwell;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import com.squareup.picasso.Picasso;


public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<Libro> libros;
    View.OnClickListener mOnItemClickListener;

    public HorizontalAdapter(Context ctx, ArrayList<Libro> libros){

        inflater = LayoutInflater.from(ctx);
        this.libros = libros;
    }

    @Override
    public HorizontalAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.book_recycleview, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        view.setOnClickListener(mOnItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(HorizontalAdapter.MyViewHolder holder, int position) {

        // below line is use to set image from URL in our image view.
        Picasso.get().load(libros.get(position).getImage_drawable()).into(holder.iv);

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

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView time;
        ImageView iv;

        public MyViewHolder(View itemView) {
            super(itemView);

            time = (TextView) itemView.findViewById(R.id.tv);
            iv = (ImageView) itemView.findViewById(R.id.iv);
        }

    }

    public ArrayList getInfoPosition(int pos){

        ArrayList info = new ArrayList<>();

        info.add(libros.get(pos).getName());
        info.add(libros.get(pos).getImage_drawable());
        info.add(libros.get(pos).getAutor());
        info.add(libros.get(pos).getGenero());

        return info;
    }

}