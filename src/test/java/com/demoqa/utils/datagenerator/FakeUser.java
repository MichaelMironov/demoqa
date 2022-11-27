package com.demoqa.utils.datagenerator;

import com.github.javafaker.Faker;

import java.util.*;

import static java.util.Arrays.asList;

public class FakeUser {

    private static final Faker faker = new Faker();
    public static final String NAME = faker.name().firstName();
    public static final String SURNAME = faker.name().lastName();
    public static final String MAIL = faker.internet().safeEmailAddress();
    public static final String GENDER = new String[]{"Male", "Female", "Other"}[new Random().nextInt(2)];
    public static final String PHONE_NUMBER = String.valueOf(faker.numerify("##########"));
    public static final String HOBBIES = getHobbies();
    public static final String IMG_JPEG = "test.jpeg";
    public static final String DATE = String.format(Locale.ENGLISH, "%td %<tB %<tY", faker.date().birthday());
    public static final String ADDRESS = faker.address().fullAddress();
    public static final String STATE = new String[]{"NCR", "Uttar Pradesh", "Haryana", "Rajasthan"}[new Random().nextInt(4)];
    public static final String CITY = rndValidCity();
    public static final String[] SUBJECTS = getSubjects();

    private FakeUser() {
    }

    /**
     *
     * @return random array of hobbies
     */
    private static String getHobbies() {
        List<String> hobbies = asList("Sports", "Reading", "Music");
        Collections.shuffle(hobbies);
        int rndLength = (int) (Math.random() * hobbies.size()+1);
        String[] arr = new String[rndLength];
        for (int i = 0; i < rndLength; i++) {
            arr[i] = hobbies.get(i);
        }
        return Arrays.toString(arr).replaceAll("^\\[|\\]$", "");
    }

    /**
     *
     * @return random valid city in dependency of chosen state
     */
    private static String rndValidCity() {
        switch (STATE) {
            case "NCR":
                return new String[]{"Delhi", "Gurgaon", "Noida"}[new Random().nextInt(3)];
            case "Uttar Pradesh":
                return new String[]{"Agra", "Lucknow", "Merrut"}[new Random().nextInt(3)];
            case "Haryana":
                return new String[]{"Karnal", "Panipat"}[new Random().nextInt(2)];
            case "Rajasthan":
                return new String[]{"Jaipur", "Jaiselmer"}[new Random().nextInt(2)];
            default:
                return null;
        }

    }
    /**
     *
     * @return random array of subjects
     */
    private static String[] getSubjects(){

        List<String> list = asList("Maths", "English", "Computer Science", "Chemistry", "Physics", "Social Studies", "Biology", "Hindi", "Economics", "Accounting", "Arts", "Commerce");
        Collections.shuffle(list);
        int rndSize = (int) (Math.random() * list.size()+1);
        String[] temp = new String[rndSize];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = list.get(i);
        }
        return temp;
    }

}
