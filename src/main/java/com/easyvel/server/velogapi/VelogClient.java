package com.easyvel.server.velogapi;

import com.easyvel.server.global.dto.PostDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class VelogClient {

    //Todo: pageable 적용하기
    public List<PostDto> getTrendData(int limit, int offset) throws JsonMappingException, JsonProcessingException {
        String query = "{\"query\":\"\\n    query trendingPosts($input: TrendingPostsInput!) {\\n  trendingPosts(input: $input) {\\n    id\\n    title\\n    short_description\\n    thumbnail\\n    likes\\n    user {\\n      id\\n      username\\n      profile {\\n        id\\n        thumbnail\\n      }\\n    }\\n    url_slug\\n    released_at\\n    updated_at\\n    is_private\\n    comments_count\\n  }\\n}\\n    \",\"variables\":{\"input\":{\"limit\":"+limit+",\"offset\":"+offset+",\"timeframe\":\"week\"}}}";
        WebClient webClient = WebClient.builder()
                .baseUrl("https://v3.velog.io")
                .build();

        ResponseEntity<TrendData> response = webClient.post()
                .uri("/graphql")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(query)
                .retrieve()
                .toEntity(TrendData.class)
                .block();

        List<TrendData.Data.PostData> trendingPosts = response.getBody().getData().getTrendingPosts();
        List<PostDto> trendPostList = new ArrayList<>();

        for (TrendData.Data.PostData p : trendingPosts) {
            log.info("loop");
            PostDto postDto = PostDto.builder()
                    .name(p.getUser().getUsername())
                    .title(p.getTitle())
                    .summary(p.getShort_description())
                    .date(p.getReleased_at())
                    .comment(p.getComments_count())
                    .like(p.getLikes())
                    .img(p.getThumbnail())
                    .tag(null) // Todo: 채워야 하는 데이터
                    .url(null) // Todo: 채워야 하는 데이터
                    .build();

            trendPostList.add(postDto);
        }

        return trendPostList;
    }
}
