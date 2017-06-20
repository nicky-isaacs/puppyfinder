package com.seaninboulder;

public class Puppy {
    private final String photoURL;
    private final String name;
    private final String breed;
    private final String meta;
    private final String url;

    public Puppy(String photoURL, String name, String breed, String meta, String url) {
        this.photoURL = photoURL;
        this.name = name;
        this.breed = breed;
        this.meta = meta;
        this.url = url;
    }

    public String getPhotoURL() {
        return photoURL;
    }


    @Override
    public String toString() {
        return name + "\n" +
               "Breed: " + breed + "\n" +
               meta + "\n" +
               "Call: (303)442-4030\n" +
               url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Puppy puppy = (Puppy) o;

        if (photoURL != null ? !photoURL.equals(puppy.photoURL) : puppy.photoURL != null) return false;
        if (name != null ? !name.equals(puppy.name) : puppy.name != null) return false;
        if (breed != null ? !breed.equals(puppy.breed) : puppy.breed != null) return false;
        if (meta != null ? !meta.equals(puppy.meta) : puppy.meta != null) return false;
        return url != null ? url.equals(puppy.url) : puppy.url == null;
    }

    @Override
    public int hashCode() {
        int result = photoURL != null ? photoURL.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (breed != null ? breed.hashCode() : 0);
        result = 31 * result + (meta != null ? meta.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }
}