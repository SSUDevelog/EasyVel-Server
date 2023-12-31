package com.easyvel.server.subscribe.dto;

import com.easyvel.server.global.dto.PostDto;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
public class TrendResultDto {
    List<PostDto> trendPostDtos = new ArrayList<>();
}
