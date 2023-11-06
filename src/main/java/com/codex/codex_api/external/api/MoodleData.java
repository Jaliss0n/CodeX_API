package com.codex.codex_api.external.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoodleData {

    @JsonProperty("users")
    private List<MoodleUser> users;

    @JsonProperty("warnings")
    private List<String> warnings;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MoodleUser {

        @JsonProperty("id")
        private int id;

        @JsonProperty("username")
        private String username;

        @JsonProperty("firstname")
        private String firstname;

        @JsonProperty("lastname")
        private String lastname;

        @JsonProperty("fullname")
        private String fullname;

        @JsonProperty("email")
        private String email;

        @JsonProperty("department")
        private String department;

        @JsonProperty("firstaccess")
        private long firstaccess;

        @JsonProperty("lastaccess")
        private long lastaccess;

        @JsonProperty("auth")
        private String auth;

        @JsonProperty("suspended")
        private boolean suspended;

        @JsonProperty("confirmed")
        private boolean confirmed;

        @JsonProperty("lang")
        private String lang;

        @JsonProperty("theme")
        private String theme;

        @JsonProperty("timezone")
        private String timezone;

        @JsonProperty("mailformat")
        private int mailformat;

        @JsonProperty("description")
        private String description;

        @JsonProperty("descriptionformat")
        private int descriptionformat;

        @JsonProperty("city")
        private String city;

        @JsonProperty("country")
        private String country;

        @JsonProperty("profileimageurlsmall")
        private String profileimageurlsmall;

        @JsonProperty("profileimageurl")
        private String profileimageurl;

        @JsonProperty("customfields")
        private List<CustomField> customfields;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CustomField {

        @JsonProperty("type")
        private String type;

        @JsonProperty("value")
        private String value;

        @JsonProperty("name")
        private String name;

        @JsonProperty("shortname")
        private String shortname;
    }
}
