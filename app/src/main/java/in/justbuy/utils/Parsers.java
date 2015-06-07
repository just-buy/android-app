package in.justbuy.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

import in.justbuy.model.Ad;
import in.justbuy.model.Category;
import in.justbuy.model.User;

/**
 * @author aritra
 */
public class Parsers {

    public static List<Ad> parseAds(String json) {
        List<Ad> ads = new ArrayList<>();
        JsonArray jsonAds = new JsonParser().parse(json).getAsJsonArray();
        for (JsonElement jsonPatientElement : jsonAds) {
            Ad ad = parseAd(jsonPatientElement.toString());
            ads.add(ad);
        }
        return ads;
    }

    public static Ad parseAd(String json) {
        Ad ad = new Ad();
        JsonObject jsonAd = new JsonParser().parse(json).getAsJsonObject();

        ad.id = jsonAd.get("id").getAsInt();
        ad.title = jsonAd.get("title").getAsString();
        ad.description = jsonAd.get("description").getAsString();
        ad.value = jsonAd.get("value").getAsInt();
        ad.soldOut = jsonAd.get("soldOut").getAsBoolean();

//      String createdBy = jsonAd.get("createdBy").getAsString();
//      ad.user = parseUser(createdBy);
        return ad;
    }

    public static List<Category> parseCategories(String json) {
        List<Category> categories = new ArrayList<>();
        JsonArray jsonCategories = new JsonParser().parse(json).getAsJsonArray();
        for (JsonElement jsonPatientElement : jsonCategories) {
            Category category = parseCategory(jsonPatientElement.toString());
            categories.add(category);
        }
        return categories;
    }

    public static Category parseCategory(String json) {
        Category category = new Category();
        JsonObject jsonCategory = new JsonParser().parse(json).getAsJsonObject();

        category.id = jsonCategory.get("id").getAsInt();
        category.name = jsonCategory.get("name").getAsString();
        return category;
    }

    public static User parseUser(String json) {
        User user = new User();
        JsonObject jsonUser = new JsonParser().parse(json).getAsJsonObject();

        user.id = jsonUser.get("id").getAsInt();
        user.name = jsonUser.get("name").getAsString();
        user.mobile = jsonUser.get("mobile").getAsString();
        user.email = jsonUser.get("email").getAsString();
        return user;
    }

}
