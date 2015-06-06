package in.justbuy.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

import in.justbuy.model.Ad;

/**
 * @author aritra
 */
public class Parsers {

    public static List<Ad> parseAds(String json) {
        List<Ad> ads = new ArrayList<>();
        JsonArray jsonPatients = new JsonParser().parse(json).getAsJsonArray();
        for (JsonElement jsonPatientElement : jsonPatients) {
            Ad ad = parseAd(jsonPatientElement.toString());
            ads.add(ad);
        }
        return ads;
    }

    public static Ad parseAd(String json) {
        Ad ad = new Ad();
        JsonObject jsonMobilePatient = new JsonParser().parse(json).getAsJsonObject();

        ad.id = jsonMobilePatient.get("id").getAsInt();
        ad.title = jsonMobilePatient.get("title").getAsString();
        ad.description = jsonMobilePatient.get("description").getAsString();
        ad.value = jsonMobilePatient.get("value").getAsInt();
        ad.soldOut = jsonMobilePatient.get("soldOut").getAsBoolean();
        return ad;
    }

}
