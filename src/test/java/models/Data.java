package models;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;

@lombok.Data
@Builder
public class Data {
    int id;
    String email;
    @SerializedName("first_name")
    String firstName;
    @SerializedName("last_name")
    String lastName;
    String avatar;
}
