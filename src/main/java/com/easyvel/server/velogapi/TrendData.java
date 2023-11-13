package com.easyvel.server.velogapi;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@ToString
@Getter
@Setter
public class TrendData {
    Data data;
    @ToString
    @Getter
    @Setter
    public static class Data {
        List<PostData> trendingPosts;
        @ToString
        @Getter
        @Setter
        public static class PostData {
            String id;
            String title;
            String short_description;
            String thumbnail;
            int likes;
            UserData user;
            String url_slug;
            Date released_at;
            Date updated_at;
            boolean is_private;
            int comments_count;

            @ToString
            @Getter
            @Setter
            public static class UserData {
                String id;
                String username;
                Profile profile;

                @ToString
                @Getter
                @Setter
                public static class Profile {
                    String id;
                    String thumbnail;

                }
            }
        }
    }
}
