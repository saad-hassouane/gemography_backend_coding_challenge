package gemography.backend.codingchallenge.outputClass;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import gemography.backend.codingchallenge.inputClass.Repo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@JsonSerialize
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProgrammingLanguage implements Comparable<ProgrammingLanguage> {

    private String name;
    @JsonProperty
    private int count;
    @JsonProperty
    private List<String> repos;

    public ProgrammingLanguage(String language, int countt,String repo) {
        //this.name=language;

        repos=new ArrayList<String>();
        repos.add(repo);
        count=countt;

    }
public void addRepo(Repo rep){
    this.repos.add("https://api.github.com/repositories/"+String.valueOf(rep.getId()));
    this.setCount( this.getRepos().size());
}

    @Override
    public int compareTo(ProgrammingLanguage o) {
        return this.count-o.getCount();
    }
}
