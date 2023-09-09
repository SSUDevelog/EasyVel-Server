package com.easyvel.server.tag;

import com.easyvel.server.annotation.EasyvelTokenApiImplicitParams;
import com.easyvel.server.config.security.SecurityConfiguration;
import com.easyvel.server.global.dto.PostDto;
import com.easyvel.server.jwt.JwtTokenProvider;
import com.easyvel.server.tag.dto.TagList;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequestMapping("/tags")
@RequiredArgsConstructor
@RestController
public class TagController {

    private final JwtTokenProvider jwtTokenProvider;
    private final TagService tagService;

    @ApiOperation("유저 태그 목록")
    @EasyvelTokenApiImplicitParams
    @GetMapping("/gettag")
    public TagList getUserTagList(@RequestHeader(SecurityConfiguration.TOKEN_HEADER) String token) {
        String uid = jwtTokenProvider.getUid(token);

        return tagService.getUserTagList(uid);
    }

    @ApiOperation("유저 태그 추가")
    @EasyvelTokenApiImplicitParams
    @PostMapping("addtag")
    public void addUserTag(@RequestBody String tag,
            @RequestHeader(SecurityConfiguration.TOKEN_HEADER) String token) {
        String uid = jwtTokenProvider.getUid(token);

        tagService.addTag(uid, tag);
    }

    @ApiOperation("유저 태그 제거")
    @EasyvelTokenApiImplicitParams
    @DeleteMapping("deletetag")
    public void deleteUserTag(@RequestBody String tag,
                              @RequestHeader(SecurityConfiguration.TOKEN_HEADER) String token) {
        String uid = jwtTokenProvider.getUid(token);

        tagService.deleteTag(uid, tag);
    }

    //Todo: 페이징 필요
    @ApiOperation("유저 태그 목록 연관 포스트")
    @EasyvelTokenApiImplicitParams
    @GetMapping("/posts-1")
    public List<PostDto> getUserTagPostList(@RequestHeader(SecurityConfiguration.TOKEN_HEADER) String token) throws IOException {
        String uid = jwtTokenProvider.getUid(token);

        return tagService.getUserTagPostList(uid);
    }

    //Todo: 페이징 필요
    @ApiOperation(value = "태그 연관 포스트", notes = "search 파라미터에 검색할 태그를 입력")
    @EasyvelTokenApiImplicitParams
    @GetMapping("/posts-2")
    public List<PostDto> getUserTagPostList(@RequestParam String search,
                                            @RequestHeader(SecurityConfiguration.TOKEN_HEADER) String token) throws IOException {
        String uid = jwtTokenProvider.getUid(token);

        return tagService.getPostDtoListByTag(uid, search);
    }

    @ApiOperation(value = "현재 인기있는 태그 목록", notes = "vol 파라미터에 받을 태그 수량을 입력")
    @GetMapping("/hot")
    public TagList getHotTagList(@RequestParam(defaultValue = "10") int vol) throws IOException {
        return tagService.getHotTagList(vol);
    }
}
