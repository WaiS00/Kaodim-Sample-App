package ismail.cozy.my.kaodiminternship;

import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

public interface GetDataService {
    @GET("home")
    Call<List<ServiceGroup>> getHomeData();

    @GET("events")
    Call<List<ServiceGroup>> getEventsData();
}