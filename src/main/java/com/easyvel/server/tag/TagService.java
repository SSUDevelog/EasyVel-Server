package com.easyvel.server.tag;

import com.easyvel.server.global.dto.PostDto;
import com.easyvel.server.global.dto.VelogUserInfoDto;
import com.easyvel.server.global.entity.User;
import com.easyvel.server.global.repository.TagRepository;
import com.easyvel.server.global.repository.UserRepository;
import com.easyvel.server.subscribe.service.SubscribeService;
import com.easyvel.server.tag.bridge.UserTag;
import com.easyvel.server.tag.bridge.UserTagRepository;
import com.easyvel.server.tag.dto.TagList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final UserTagRepository userTagRepository;
    private final SubscribeService subscribeService;

    public TagList getUserTagList(String uid) {
        User user = getUserByUid(uid);

        return TagList.make(user);
    }

    public void addTag(String uid, String tagName) {
        User user = getUserByUid(uid);
        Tag tag = getElseMakeTag(tagName);

        checkBlankAndSpecial(tagName);

        if (containsTag(user, tag))
            throw new IllegalArgumentException("이미 추가한 태그입니다.");

        UserTag userTag = new UserTag(user, tag);
        userTagRepository.save(userTag);
    }

    //Todo: 다른 곳에서도 필요할지도..? 이후 추가 적용하기
    private void checkBlankAndSpecial(String tagName) {
        String pattern = "^[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힣]*$";
        if (!Pattern.matches(pattern, tagName))
            throw new IllegalArgumentException("공백, 특수문자는 허용되지 않습니다.");
    }

    public void deleteTag(String uid, String tagName) {
        User user = getUserByUid(uid);
        Tag tag = getElseMakeTag(tagName);

        UserTag userTag = userTagRepository.findByUserAndTag(user, tag)
                .orElseThrow(() -> new IllegalArgumentException("목록에 " + tagName + "가 없습니다."));

        userTagRepository.delete(userTag);
    }

    // Todo: 이부분 로직의 변경과 사용하는 부분 수정이 필요합니다.
    /**
     * 해당 태그와 관련된 포스트를 크롤링해 PostDto를 만듭니다.
     *
     * @param uid
     * @param tag
     * @return
     * @throws IOException
     */
    public List<PostDto> getPostDtoListByTag(String uid, String tag) throws IOException {
        //Todo: 없는 태그를 긁어오려할 경우 체크하는 로직 만들기
        List<String> userSubscribeList = getSubscribeNameList(uid);
        Elements postsElements = getTagPostsElements(tag);

        return createPostDtoList(userSubscribeList, postsElements);
    }

    public List<PostDto> getUserTagPostList(String uid) throws IOException {
        TagList tagList = getUserTagList(uid);

        List<PostDto> postDtoList = new ArrayList<>();
        for (String tag : tagList.getTags()) {
            List<PostDto> postDtoListByTag = getPostDtoListByTag(uid, tag);
            postDtoList.addAll(postDtoListByTag);
        }
        
        // TODO: 날짜 DATE로 바꾸는 알고리즘 만들기
//        postDtoList.sort(PostDto.compareByDate);

        return postDtoList;
    }

    /**
     * 현재 가장 인기있는 태그 목록을 가져옵니다.
     *
     * @param vol 가져올 개수
     * @return
     */
    public TagList getHotTagList(int vol) throws IOException {
        String tagUrl = "https://velog.io/tags";
        Document doc = Jsoup.connect(tagUrl).get();

        return getTagList(vol, doc);
    }

    private TagList getTagList(int vol, Document doc) throws IOException {
        List<String> tags = new ArrayList<>();

        Elements elementTags = doc.selectXpath("//*[@id=\"root\"]/div[2]/main/section").select("a");
        int cnt = 0;
        for (Element elementTag : elementTags) {
            if (cnt >= vol)
                break;

            try {
                tags.add(elementTag.text());
            } catch (RuntimeException e) {
                throw new IOException("문서 구조가 변경되었습니다.");
            }
            cnt++;
        }

        return new TagList(tags);
    }

    private List<PostDto> createPostDtoList(List<String> userSubscribeList, Elements postsElements) throws IOException {
        List<PostDto> postDtoList = new ArrayList<>();

        for (Element postElement : postsElements) {
            try {
                PostDto postDto = createPostDto(postElement, userSubscribeList);
                postDtoList.add(postDto);
            } catch (Exception e) {
                throw new IOException("문서 구조가 변경되었습니다.");
            }
        }
        postDtoList.sort(PostDto.compareByDate);
        return postDtoList;
    }

    //Todo: 이거 subscribe 옮기는게 맞을지..?
    private List<String> getSubscribeNameList(String uid) throws IOException {
        List<String> userSubscribeList = new ArrayList<>();

        List<VelogUserInfoDto> subscribers = subscribeService.getSubscribers(uid);
        for (VelogUserInfoDto subscriber : subscribers) {
            userSubscribeList.add(subscriber.getName());
        }

        return userSubscribeList;
    }

    private Elements getTagPostsElements(String tag) throws IOException {
        String tagUrl = "https://velog.io/tags/" + tag;
        Document doc = Jsoup.connect(tagUrl).get();

        return doc.selectXpath("//*[@id=\"root\"]/div[2]/main/div/div[2]");
    }

    //Todo: 전체 적용이 가능한지 알아보기
    private PostDto createPostDto(Element post, List<String> subscribeList) {
        PostDto postDto = PostDto.builder()
                .name(post.select(".user-info .username a").text())
                .title(post.select("a h2").text())
                .summary(post.select("p").text())
//                .date(post.select(".subinfo span").get(0).text())
                .comment(Integer.parseInt(post.select(".subinfo span").get(1).text().replace("개의 댓글", "")))
                .like(Integer.parseInt(post.select(".subinfo span").get(2).text()))
                .img(post.select("a div img").attr("src"))
                .url("https://velog.io" + post.select("> a").attr("href"))
                .build();

        Elements tags = post.selectXpath("div[1]/div[2]/a");
        for (Element _tag : tags) {
            postDto.getTag().add(_tag.text());
        }

        postDto.setSubscribed(subscribeList.contains(postDto.getName()));

        return postDto;
    }

    private boolean containsTag(User user, Tag tag) {
        return userTagRepository.findByUserAndTag(user, tag).isPresent();
    }

    private Tag getElseMakeTag(String tagValue) {
        Tag tag = tagRepository.findByValue(tagValue)
                .orElseGet(() -> makeTag(tagValue));

        return tag;
    }

    private Tag makeTag(String value) {
        tagRepository.findByValue(value)
                .ifPresent(m -> new IllegalArgumentException("이미 존재하는 tag 입니다."));

        Tag tag = new Tag(value);
        return tagRepository.save(tag);
    }

    private User getUserByUid(String uid) {
        return userRepository.getByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 uid 입니다."));
    }
}
