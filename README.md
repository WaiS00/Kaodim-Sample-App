# Kaodim List of Services App
This sample application lists the services available from Kaodim.

## Code Documentation

### Creating the Layout

#### MainActivity

The MainActivity creates the layout necessary to have two fragments in one screen.


```java

private TabAdapter adapter;
private TabLayout tabLayout;
private ViewPager viewPager;

viewPager = findViewById(R.id.viewPager);
tabLayout = findViewById(R.id.tabLayout);
adapter = new TabAdapter(getSupportFragmentManager(), this);
adapter.addFragment(new HomeFragment(), "Home", tabIcons[0]);
adapter.addFragment(new EventsFragment(), "Events", tabIcons[1]);
viewPager.setAdapter(adapter);
tabLayout.setupWithViewPager(viewPager);
```

#### TabAdapter

Creates the tab adapter that is the navigation UI

```java

 public View getTabView(int position) {

        View view = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        ImageView tabImageView = view.findViewById(R.id.tabImageView);
        tabImageView.setImageResource(mFragmentIconList.get(position));

        TextView tabTextView = view.findViewById(R.id.tabTextView);
        tabTextView.setText(mFragmentTitleList.get(position));

        tabImageView.setAlpha(0.7f);
        tabTextView.setAlpha(0.7f);

        return view;
  }
 ```
 
 ### Making the API request
  
The GetDataService interface houses the two kinds of GET request (with their respective paths) that will be made from this application. Data will be returned in the form of a list of a ServiceGroup class object.
 
 ```java
 
 public interface GetDataService {
    @GET("home")
    Call<List<ServiceGroup>> getHomeData();

    @GET("events")
    Call<List<ServiceGroup>> getEventsData();
}
```

A Retrofit client instance is created with the provided base URL.

```java

private static final String BASE_URL = "https://5d1dae5d3374890014f005d1.mockapi.io/api/v1/";

```

Because I'm using Retrofit, it returns the data into a list of objects that are to be created based on the data itself. Hence, I have to create 3 classes for every hierarchy of the JSON that I need to access data into.

```java

  public class ServiceGroup {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("service_types")
    @Expose
    private ArrayList<Service> services = null;
    
    ...
  }
  
  class Service {

    private String url;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("image_thumb_url")
    @Expose
    private ImageListUrl imageListUrls;
    
    ...

  }
  
  class ImageListUrl {
  
    @SerializedName("sm")
    @Expose
    private String url;
    
    ...
    
  }
  
  ```
  
An arraylist is instantiated before making the API call to store the desired data.

```java

itemList = new ArrayList<>();

```


A REST API call is made as following, with a callback for when the result is obtained from the server.

```java

call.enqueue(new Callback<List<ServiceGroup>>() {
            @Override
            public void onResponse(Call<List<ServiceGroup>> call, Response<List<ServiceGroup>> response){
            
            itemList = new ArrayList<>(getSortedData(response));
            
            RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(getContext(), itemList);
            myRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
            myRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            myRecyclerView.setAdapter(recyclerViewAdapter);

            }
});

```


The getSortedData serves as a parser function to get the exact sets of data needed for this application, which are the Service Type Name, Service Name, and the image URL (if available) for each of the service.

```java

public ArrayList<ListingItem> getSortedData(Response<List<ServiceGroup>> response){
        
        int serviceTypeSize = response.body().size();

        for(int x = 0; x < serviceTypeSize; x++){
            
            // getting the service type name
            String serviceTypeName = response.body().get(x).getName();

            ListingItem serviceType = new ListingItem(serviceTypeName, true);
            itemList.add(serviceType);

            for(int y = 0; y < response.body().get(x).getServices().size(); y++){
                
                // getting the service name
                String serviceName = response.body().get(x).getServices().get(y).getName();
                String imgUrl;

                try{
                    // getting the image url for the service
                    imgUrl = response.body().get(x).getServices().get(y).getImageListUrls().getUrl();
                }catch (NullPointerException e){
                    //sets the url as empty if it's unavailable
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

        return itemList;
    }
    
```


The ListingItem class is created to accomodate to the RecyclerViewAdapter (must be a list containing all objects).

```java

  public class ListingItem {
      private String name;
      private Boolean isHeader = false;
      private Boolean isFooter = false;
      private String imgUrl;

      ...   
  }
 ```
 
 
In the RecyclerViewAdapter class, there are 3 static viewholder classes which are header, service item, and footer. These 3 classes have different layouts attached to them.

```java

public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view;

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
    
```


This method binds the data to the view of the application, hence displaying the layouts and the data altogether.

```java
@Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        ArrayList<ListingItem> itemList = mData;

        //checks if it is a service
        if(viewHolder.getItemViewType() == 0){
        
            ServiceViewHolder myViewHolder = (ServiceViewHolder)viewHolder;
            // assigns the textView's text to the obtained service name
            myViewHolder.tv_title.setText(itemList.get(i).getName());

            String url;

            try{
                url = itemList.get(i).getImgUrl();
            }catch (NullPointerException e){
                url = "";
            }

            myViewHolder.displayImage(mContext,url);

        }else{
            // checks if it is a service type
            if(itemList.get(i).getHeader()){
                
                ServiceTypeViewHolder serviceTypeViewHolder = (ServiceTypeViewHolder)viewHolder;
                // assigns the textView's text to the obtained service type name
                serviceTypeViewHolder.tv_type.setText(itemList.get(i).getName());

            }
        }
    }

```
   
   
The URL passed to displayImage function. Picasso is used to display the image. If there isn't a URL provided, a default image will be set. If the image is loading, a placeholder image is also set.

```java
    
    public void displayImage(Context context, String url){
            
            if(url == ""){
                tv_img.setImageResource(R.drawable.no_image);
            }else{
                Picasso.with(context).load(url).placeholder(R.drawable.no_image).error(R.drawable.no_image).into(tv_img);
            }

        }
        
```
## Challenges Faced in Completing This Application

### Parsing the JSON

Because this was my first time using Retrofit, parsing and overall just organizing the structure of the classes
before even making the request was challenging. I had to spend a good while Googling to find the correct way to parse
the multi-tiered data in that way. I took the time to fully understand the concept behind it and thankfully I managed
get it to work, at least.

### Sorting the Data in RecyclerViewAdapter

The data comes in a list of multi-tiered list of objects. Hence, I spend most of the time developing this application stuck here,
to figure the best way to dynamically binding each different set of data to its corresponding layout. The solution that I came up
with was to create another class specifically for the layout objects which could be a header, a service name, or a footer. That way,
I could easily assign each and every type of data to its corresponding view.

## Final Product

![](App-Demo.gif)

