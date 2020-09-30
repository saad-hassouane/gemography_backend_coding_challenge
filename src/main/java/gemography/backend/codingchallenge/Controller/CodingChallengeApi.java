package gemography.backend.codingchallenge.Controller;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import gemography.backend.codingchallenge.inputClass.Repo;
import gemography.backend.codingchallenge.outputClass.ProgrammingLanguage;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.util.stream.Collectors.toMap;

@RestController
@JsonSerialize
public class CodingChallengeApi {
    @Async
    @RequestMapping("/codingchallenge")
    public String codingChallengeApi() throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        // // getting the date of a month before
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        Date date = cal.getTime();
        String dateString = (df.format(date)).toString();
        // //
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String server = "https://api.github.com/search/repositories?q=created:>" + dateString + "&sort=stars&order=desc&pagesize=100";
        RestTemplate rest = new RestTemplate();

        // 1- recuperer les resultats de l'api de repo github

        String result = rest.getForObject(server, String.class);
        JSONObject parsedJson = new JSONObject(result.toString());
        JSONArray items = parsedJson.getJSONArray("items");

        // 2- deserializer(JSON) les repos (dont le language de prog different de "null") et les stocker dans une liste
        List<Repo> repos = new ArrayList<Repo>();
        for (int i = 0; i < items.length(); i++) {
            Repo rep=gson.fromJson(String.valueOf(items.getJSONObject(i)), Repo.class);
            if(rep.getLanguage()!="null" && rep.getLanguage()!=null)
            {
            repos.add(rep);
            }
        }
        // 3- proceder au traitement de la liste pour constituer le resultat final qui est une liste de language avec le nbre des repos et leur liste dont il est utilise
        Map<String, ProgrammingLanguage> set = new HashMap<String, ProgrammingLanguage>();
        for (Repo rep : repos) {
            if (set.containsKey(rep.getLanguage())) {
                ProgrammingLanguage pl = set.get(rep.getLanguage());
                pl.addRepo(rep);
            }
            else{
                set.put(rep.getLanguage(), new ProgrammingLanguage(rep.getLanguage(), 1, "https://api.github.com/repositories/"+String.valueOf(rep.getId())));
            }
        }
        // 4- serializer(JSON) la liste (resultat final) et les ordonner puis, les retourner en format JSON
        Map<String,ProgrammingLanguage> sortedResults=set.entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(
                        toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                LinkedHashMap::new));
        String results=gson.toJson(sortedResults);/*set*/
        JsonParser jp=new JsonParser();
        JsonElement je=jp.parse(results);
        String finalRes=gson.toJson(je);
        return finalRes;
    }
}
