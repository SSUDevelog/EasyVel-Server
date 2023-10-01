package com.easyvel.server.subscribe.service;

import com.easyvel.server.global.dto.PostDto;
import com.easyvel.server.global.dto.VelogUserInfoDto;
import com.easyvel.server.global.entity.bridge.UserVelogUser;
import com.easyvel.server.global.entity.VelogUser;
import com.easyvel.server.global.entity.User;
import com.easyvel.server.subscribe.dto.*;
import com.easyvel.server.global.repository.UserVelogUserRepository;
import com.easyvel.server.global.repository.VelogUserRepository;
import com.easyvel.server.global.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SubscribeService {

    private final UserVelogUserRepository userVelogUserRepository;
    private final UserRepository userRepository;
    private final VelogUserRepository velogUserRepository;
    // private final TagRepository tagRepository;


    public UserMainDto getUserMain(String name) {
        // Velog 유저의 프로필 url을 담은 dto를 리턴합니다.
        return new UserMainDto(name);
    }

    public SubscriberPostsDto getSubscribersPost(String uid) throws IOException {
        // 유저가 구독하고 있는 velog 유저들의 게시물들을 리턴합니다.
        // throws에 SubscribeException 추가해야 합니다.

        Optional<User> user = userRepository.getByUid(uid);
        User resultUser = user.orElseThrow(() -> new IOException()); // 커스텀 에러로 변경 고려

        List<VelogUserInfoDto> subscribers = getSubscribers(resultUser); //subscriber 이름 다시 생각
        List<String> names = new ArrayList<>();

        for (VelogUserInfoDto velogUserInfoDto : subscribers) {
            names.add(velogUserInfoDto.getName());
        }
        SubscriberPostsDto subscriberPostsDto = getSubscribersPost(names);

        // 구독 여부를 true로 표시합니다.
        for (PostDto postDto : subscriberPostsDto.getSubscribePostDtoList()) {
            postDto.setSubscribed(true);
        }

        return subscriberPostsDto;
    }

    public SubscriberPostsDto getSubscribersPost(List<String> subscribers) throws IOException {
        // velog 유저들의 post를 스크래핑합니다.
        // throws에 SubscribeException 추가해야 합니다.

        SubscriberPostsDto subscriberPostsDto = new SubscriberPostsDto();

        for (String sub : subscribers) {
            List<PostDto> subscribePostDtos = getSubscriberPosts(sub);
            if (!subscribePostDtos.isEmpty()) {
                subscriberPostsDto.getSubscribePostDtoList().addAll(subscribePostDtos);
            }
        }
        Collections.sort(subscriberPostsDto.getSubscribePostDtoList(), PostDto.compareByDate);

        // SubscribeException 임시로 주석처리합니다.
//        if(subscriberPostsDto.getSubscribePostDtoList().isEmpty()){
//            throw new SubscribeException(HttpStatus.ACCEPTED, "불러올 포스트가 없습니다.");
//        }

        return subscriberPostsDto;
    }

    //반환형 나중에 성공여부 DTO로 바꾸기
    public void addSubscribe(String uid, String subscriber) throws IOException {
        // 구독관계 추가를 위해 user와 target의 이름을 맵핑하는 객체를 생성합니다.
        // throws에 SubscribeException 추가해야 합니다.

        Optional<User> user = userRepository.getByUid(uid);
        User resultUser = user.orElseThrow(() -> new IOException()); // 추후 커스텀 에러로 변경 고려

        List<VelogUserInfoDto> velogUserInfoDtos = getSubscribers(resultUser);
        List<String> names = new ArrayList<>();

        for (VelogUserInfoDto subs : velogUserInfoDtos) {
            names.add(subs.getName());
        }

        // SubscribeException 코드 주석처리 합니다.
//        if(names.contains(subscriber)){
//            throw new SubscribeException(HttpStatus.BAD_REQUEST, "이미 추가한 구독대상입니다.");
//        }

        makeSubscribe(resultUser, subscriber);
    }

    //
    public List<VelogUserInfoDto> getSubscribers(String userName) throws IOException {
        // 유저 네임을 통해 User 객체를 얻고, 구독자 조회 메소드를 호출합니다.
        Optional<User> user = userRepository.getByUid(userName);
        User resultUser = user.orElseThrow(() -> new IOException()); // 커스텀 에러로 변경 고려

        return getSubscribers(resultUser);
    }

    public List<PostDto> getSubscriberPosts(String subscriber) throws IOException {
        // Velog 유저의 아이디를 통해 해당 유저가 작성한 포스트를 스크래핑 합니다.

        String userProfileURL = "https://velog.io/@" + subscriber;
        Document doc = Jsoup.connect(userProfileURL).get();

        Elements posts = doc.select("#root > div > div > div > div > div").get(6).select("> div");

        return doSubscribeScrapping(posts, subscriber);
    }

    public ValidateVelogUserDto getVelogUserProfile(Boolean isPresent, ValidateVelogUserDto validateVelogUserDto) throws IOException {
        // 실제 존재하는 velog 유저라면 프로필 사진 url을 추가하는 함수를 호출하고, 아니라면 바로 리턴합니다.
        if (isPresent == Boolean.FALSE) {
            validateVelogUserDto.setValidate(Boolean.FALSE);
            return validateVelogUserDto;
        }
        validateVelogUserDto.setValidate(Boolean.TRUE);
        getVelogUserProfilePicture(validateVelogUserDto);
        return validateVelogUserDto;
    }

    public UnsubscribeDto deleteSubscribe(String userName, String targetName) {
        // EasyVel 유저(User)와 Velog 유저(Target)간의 구독 관계를 삭제합니다.
        UnsubscribeDto unsubscribeDto = new UnsubscribeDto();
        Optional<User> user = userRepository.getByUid(userName);//밖으로 빼야함.
        User resultUser = user.orElseThrow(() -> new RuntimeException()); // 커스텀 에러로 변경 고려

        Optional<VelogUser> target= velogUserRepository.getByName(targetName);
        VelogUser resultVelogUser = target.orElseThrow(() -> new RuntimeException()); // 추후 custom exception 처리

        Optional<UserVelogUser> subscribe = userVelogUserRepository.getByUserAndVelogUser(resultUser, resultVelogUser);
        UserVelogUser resultUserVelogUser = subscribe.orElseThrow(() -> new RuntimeException()); // 추후 custom exception 처리

        userVelogUserRepository.delete(resultUserVelogUser);
        unsubscribeDto.setSuccess(Boolean.TRUE);
        unsubscribeDto.setMsg("구독을 취소했습니다.");
        return unsubscribeDto;
    }

    public void validateTarget(String targetName) {
        // 구독 삭제가 일어남으로써, 아무도 해당 Velog 유저를 구독하지 않는다면 그 Velog 유저의 정보를 삭제합니다.
        Optional<VelogUser> target= velogUserRepository.getByName(targetName);
        VelogUser result = target.orElseThrow(() -> new RuntimeException());

        if (result.getUserVelogUsers().isEmpty()) {
            velogUserRepository.delete(result);
        }
    }


    public TrendResultDto collectTrendPost(String uid) throws IOException {
        // 트렌드 포스트의 정보들을 스크래핑하여 저장합니다.
        // throws에 SubscribeException 추가해야 합니다.

        String url = "https://velog.io";

        Document doc = Jsoup.connect(url).get();

        Elements posts = doc.select("#root > div > div > div > main > div > div");

        List<PostDto> trendposts = doTrendPostScrapping(posts, url);

        return checkSubscribeOfTrend(trendposts, uid);

    }

    private List<PostDto> doTrendPostScrapping(Elements posts, String url) {

        List<PostDto> trendposts = new ArrayList<>();

        for (Element post : posts) {
            PostDto postDto = new PostDto();
            postDto.setTitle(post.select("a h4").text());
            postDto.setImg(post.select("img").attr("src"));
            postDto.setUrl(url + post.select("a").attr("href"));
            postDto.setSummary(post.select("p").text());
            postDto.setName(post.select("a.userinfo").attr("href").substring(2));
            postDto.setDate(post.select("div.sub-info > span").first().text());
            postDto.setLike(Integer.parseInt(post.select("div.likes").text()));
            postDto.setComment(Integer.parseInt(post.select("div.sub-info > span").last().text().replace("개의 댓글", "")));

            trendposts.add(postDto);
        }

        return trendposts;
    }

    private TrendResultDto checkSubscribeOfTrend(List<PostDto> trendposts, String uid) throws IOException {

        TrendResultDto trendResultDto = new TrendResultDto();

        List<VelogUserInfoDto> subscribers = getSubscribers(uid);
        List<String> subNames = new ArrayList<>();

        if (subscribers == null) {
            return trendResultDto;
        }

        for (VelogUserInfoDto velogUserInfoDto : subscribers) {
            subNames.add(velogUserInfoDto.getName());
        }

        for (PostDto postDto : trendposts) {
            postDto.setSubscribed(subNames.contains(postDto.getName()));
        }

        return trendResultDto;
    }

    private List<PostDto> doSubscribeScrapping(Elements posts, String subscriber) {
        List<PostDto> subscribePostDtos = new ArrayList<>();
        for (Element post : posts) {
            try {
                PostDto postDto = new PostDto();

                postDto.setName(subscriber);
                postDto.setTitle(post.select("a h2").text());
                postDto.setSummary(post.select("p").text());
                postDto.setDate(post.select(".subinfo span").get(0).text());
                postDto.setComment(Integer.parseInt(post.select(".subinfo span").get(1).text().replace("개의 댓글", "")));
                postDto.setLike(Integer.parseInt(post.select(".subinfo span").get(2).text()));
                postDto.setImg(post.select("a div img").attr("src"));
                postDto.setUrl("https://velog.io" + post.select("> a").attr("href"));

                Elements tags = post.select(".tags-wrapper a");
                for (Element tag : tags) {
                    postDto.getTag().add(tag.text());
                }

                subscribePostDtos.add(postDto);
            } catch (RuntimeException e) {
                return subscribePostDtos;
            }
        }
        return subscribePostDtos;
    }

    public Boolean isValidateUser(ValidateVelogUserDto validateVelogUserDto) throws IOException {
        // 구독하려는 velog 유저의 프로필 URL이 존재하는지 여부를 리턴하는 함수입니다.

        int responseCode = openURL(validateVelogUserDto.getProfileURL());

        if (responseCode == 404) return Boolean.FALSE;
        return Boolean.TRUE;
    }

    private List<VelogUserInfoDto> getSubscribers(User user) throws IOException {
        // 유저가 구독중인 velog 유저들의 이름과 프로필 url을 리턴합니다.

        List<UserVelogUser> userVelogUsers = user.getUserVelogUsers();
        List<VelogUserInfoDto> velogUserInfoDtos = new ArrayList<>();

        for (UserVelogUser userVelogUser : userVelogUsers) {
            VelogUserInfoDto velogUserInfoDto = new VelogUserInfoDto();
            velogUserInfoDto.setName(userVelogUser.getVelogUser().getName());
            String url = "https://velog.io/@" + velogUserInfoDto.getName();
            Document document = Jsoup.connect(url).get();
            Elements profileImageURL = document.selectXpath("//*[@id=\"root\"]/div[2]/div[3]/div[1]/div[1]/a/img");
            String imgUrl = profileImageURL.attr("src");

            if (!imgUrl.contains("https://")) {
                imgUrl = "";
            }
            velogUserInfoDto.setImg(imgUrl);

            velogUserInfoDtos.add(velogUserInfoDto);
        }

        return velogUserInfoDtos;
    }

    private void getVelogUserProfilePicture(ValidateVelogUserDto validateVelogUserDto) throws IOException {
        // velog 유저의 프로필 사진 url을 스크래핑합니다.
        Document document = Jsoup.connect(validateVelogUserDto.getProfileURL()).get();

        Elements profileImageURL = document.selectXpath("//*[@id=\"root\"]/div[2]/div[3]/div[1]/div[1]/a/img");
        String imgUrl = profileImageURL.attr("src");

        if (!imgUrl.contains("https://")) {
            imgUrl = "";
        }

        validateVelogUserDto.setProfilePictureURL(imgUrl);
    }

    private void makeSubscribe(User user, String subscriber) {
        // velog 유저 이름으로 target을 찾고, DB에 subscribe를 기록하는 함수를 호출합니다.
        Optional<VelogUser> target = velogUserRepository.getByName(subscriber);
        VelogUser result = target.orElseGet(() -> makeNewTarget(subscriber));

        writeSubscribeTable(user, result);
    }

    private VelogUser makeNewTarget(String subscriber) {
        VelogUser velogUser = new VelogUser();
        velogUser.setName(subscriber);
        velogUserRepository.save(velogUser);
        return velogUser;
    }

    private void writeSubscribeTable(User user, VelogUser velogUser) {
        // DB에 구독 관계를 기록합니다.
        UserVelogUser userVelogUser = new UserVelogUser();

        userVelogUser.setUser(user);
        userVelogUser.setVelogUser(velogUser);
        userVelogUserRepository.save(userVelogUser);
    }

    private int openURL(String profileURL) throws IOException {
        // URL에서 돌아오는 응답 코드를 체크합니다.
        HttpURLConnection connection = (HttpURLConnection) new URL(profileURL).openConnection();
        connection.setRequestMethod("HEAD");
        return connection.getResponseCode();
    }

}
