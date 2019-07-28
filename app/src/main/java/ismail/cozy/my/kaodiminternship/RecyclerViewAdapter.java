package ismail.cozy.my.kaodiminternship;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mContext;
    ArrayList<ListingItem> mData;

    private static final int TYPE_SERVICE = 0;
    private static final int TYPE_SERVICE_HEADER = 1;
    private static final int TYPE_SERVICE_FOOTER = 2;


    public RecyclerViewAdapter(Context mContext, ArrayList<ListingItem> mData){

        this.mContext = mContext;
        this.mData = mData;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view;
        Log.d("Type", String.valueOf(viewType));

        if (viewType == TYPE_SERVICE) {
            view = LayoutInflater.from(mContext).inflate(R.layout.service_list_row, viewGroup, false);
            ServiceViewHolder serviceViewHolder = new ServiceViewHolder(view);
            return serviceViewHolder;

        } else if (viewType == TYPE_SERVICE_HEADER) {
            view = LayoutInflater.from(mContext).inflate(R.layout.service_type_row, viewGroup, false);
            ServiceTypeViewHolder serviceTypeViewHolder = new ServiceTypeViewHolder(view);
            return serviceTypeViewHolder;
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.service_footer_row, viewGroup, false);
            ServiceFooterViewHolder serviceFooterViewHolder = new ServiceFooterViewHolder(view);
            return serviceFooterViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        Log.d("View Type", String.valueOf(viewHolder.getItemViewType()));
        ArrayList<ListingItem> itemList = mData;

        if(viewHolder.getItemViewType() == 0){
            ServiceViewHolder myViewHolder = (ServiceViewHolder)viewHolder;

            myViewHolder.tv_title.setText(itemList.get(i).getName());

            String url;

            try{
                url = itemList.get(i).getImgUrl();
            }catch (NullPointerException e){
                url = "";
            }

            myViewHolder.displayImage(mContext,url);

        }else{

            if(itemList.get(i).getHeader()){

                ServiceTypeViewHolder serviceTypeViewHolder = (ServiceTypeViewHolder)viewHolder;

                serviceTypeViewHolder.tv_type.setText(itemList.get(i).getName());

            }
        }
    }

    @Override
    public int getItemViewType(int position) {

        if(mData.get(position).getHeader()){
            return TYPE_SERVICE_HEADER;
        }
        else if(mData.get(position).getFooter()){
            return TYPE_SERVICE_FOOTER;
        }
        else{
            return TYPE_SERVICE;
        }

    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ServiceViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_title;
        private ImageView tv_img;

        public ServiceViewHolder(View itemView){

         super(itemView);

         tv_title = itemView.findViewById(R.id.service_title);
         tv_img = itemView.findViewById(R.id.img);

        }

        public void displayImage(Context context, String url){

            Log.d("URL", url);

            if(url == ""){
                tv_img.setImageResource(R.drawable.no_image);
            }else{
                Picasso.with(context).load(url).placeholder(R.drawable.no_image).error(R.drawable.no_image).into(tv_img);
            }

        }
    }

    public static class ServiceTypeViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_type;

        public ServiceTypeViewHolder(View itemView){
            super(itemView);

            tv_type = itemView.findViewById(R.id.service_type_title);

        }

    }

    public static class ServiceFooterViewHolder extends RecyclerView.ViewHolder{

        public ServiceFooterViewHolder(View itemView){
            super(itemView);
        }

    }
}
