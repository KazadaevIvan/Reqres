package models;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Resource {
    int id;
    String name;
    int year;
    String color;
    @SerializedName("pantone_value")
    String pantoneValue;
}
