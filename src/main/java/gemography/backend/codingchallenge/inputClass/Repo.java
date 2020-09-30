package gemography.backend.codingchallenge.inputClass;

import com.fasterxml.jackson.annotation.JsonTypeId;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonSerialize
@Data

public class Repo {

    @SerializedName(value = "id")
    private long id;
    @SerializedName(value = "language")
    private String language;
    public  Repo(long id,String name){
        if(name!="null"){
            language=name;
            id=id;
        }
    }
}
