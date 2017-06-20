package com.seaninboulder;

import com.google.common.collect.Sets;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class PuppyParser implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(PuppyParser.class);

    private final Set<Puppy> puppyCache = new HashSet<>();
    private final PuppyNotifier puppyNotifier;

    @Inject
    public PuppyParser(PuppyNotifier puppyNotifier) {
        this.puppyNotifier = puppyNotifier;
    }

    @Override
    public void run() {
        try {
            Document doc = Jsoup.connect("https://www.boulderhumane.org/animals/adoption/dogs").get();

            Elements puppiesForAdoption = doc.getElementsByClass("adopt-me");

            Set<Puppy> puppies = puppiesForAdoption.stream().filter(puppy -> {
                String age = puppy.getElementsByClass("views-field-field-pp-age").first().child(1).text();
                int ageInMonths = parseAgeInMonths(age);
                return ageInMonths <= 3;
            }).map(puppy -> {
                try {
                    String puppyMetaURL = "https://www.boulderhumane.org" + puppy.getElementsByClass("animal-photo").first().child(0).attr("href");
                    Document puppyMetaDoc = Jsoup.connect(puppyMetaURL).get();
                    String puppyPhotoURL = puppyMetaDoc.getElementById("petpics").child(0).attr("src");
                    Element puppyMetaData = puppyMetaDoc.getElementById("petstats");
                    String puppyName = puppyMetaData.getElementsByClass("petname").first().text();
                    String primaryBreed = puppyMetaData.getElementsByClass("breed").first().text();
                    String puppyBasicsRaw = puppyMetaData.getElementById("basics")
                            .child(0)
                            .html()
                            .replace("<strong>", "")
                            .replace("</strong>", "")
                            .replace("<br>", "\n");

                    int endOfPuppyBasics = puppyBasicsRaw.indexOf("\n\n");
                    String puppyBasics = puppyBasicsRaw.substring(0, endOfPuppyBasics);

                    return new Puppy(puppyPhotoURL, puppyName, primaryBreed, puppyBasics, puppyMetaURL);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }).collect(Collectors.toSet());


            Set<Puppy> newPuppies = Sets.difference(puppies, puppyCache);

            if (!newPuppies.isEmpty()) {
                puppyNotifier.execute(newPuppies);
            }

            Set<Puppy> puppiesToRemove = Sets.difference(puppyCache, puppies);

            puppyCache.addAll(newPuppies);
            puppyCache.removeAll(puppiesToRemove);
        } catch (Exception ex) {
            logger.warn("Caught an Exception while checking for new pups!", ex);
        }
    }

    static int parseAgeInMonths(String age) {
        String[] split = age.split(" ");
        int years = Integer.parseInt(split[0]);
        int months = Integer.parseInt(split[2]);
        return (years * 12) + months;
    }

}
