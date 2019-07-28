package ismail.cozy.my.kaodiminternship;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ServiceGroup {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("service_types")
    @Expose
    private ArrayList<Service> services = null;

    public ServiceGroup(String name, ArrayList<Service> services){
        this.name = name;
        this.services = services;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setServices(ArrayList<Service> services) {
        this.services = services;
    }

    public ArrayList<Service> getServices() {
        return services;
    }
}



