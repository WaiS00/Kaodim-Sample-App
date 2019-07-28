package ismail.cozy.my.kaodiminternship;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class EventsFragment extends Fragment {

    View v;
    private RecyclerView myRecyclerView;
    private ArrayList<ListingItem> itemList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_two, container, false);
        myRecyclerView = v.findViewById(R.id.recycler_view_events);

        return  v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        makeAPICall();

        super.onCreate(savedInstanceState);
    }

    public void makeAPICall(){

        itemList = new ArrayList<>();

        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<List<ServiceGroup>> call = service.getEventsData();

        call.enqueue(new Callback<List<ServiceGroup>>() {
            @Override
            public void onResponse(Call<List<ServiceGroup>> call, Response<List<ServiceGroup>> response) {

                for(int x=0; x < response.body().size(); x++){
                    String serviceTypeName = response.body().get(x).getName();

                    ListingItem serviceType = new ListingItem(serviceTypeName, true);
                    itemList.add(serviceType);

                    for(int y = 0; y < response.body().get(x).getServices().size(); y++){
                        String serviceName = response.body().get(x).getServices().get(y).getName();
                        String imgUrl;
                        try{
                            imgUrl = response.body().get(x).getServices().get(y).getImageListUrls().getUrl();
                        }catch (NullPointerException e){
                            imgUrl = "";
                        }

                        ListingItem service = new ListingItem(serviceName, imgUrl);
                        itemList.add(service);
                    }

                    if(!(x == (response.body().size() - 1))){
                        ListingItem serviceFooter = new ListingItem(true);
                        itemList.add(serviceFooter);
                    }
                }

                RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(getContext(), itemList);
                myRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
                myRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                myRecyclerView.setAdapter(recyclerViewAdapter);
            }

            @Override
            public void onFailure(Call<List<ServiceGroup>> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}