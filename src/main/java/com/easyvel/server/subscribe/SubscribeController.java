package com.easyvel.server.subscribe;

import com.easyvel.server.global.dto.VelogUserInfoDto;
import com.easyvel.server.subscribe.dto.*;
import com.easyvel.server.subscribe.service.SubscribeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("subscribe")
public class SubscribeController {

//     현재 구현체가 없어서 주석처리 해두었습니다.
//    private final JwtTokenProvider jwtTokenProvider;

    private final SubscribeService subscribeService;

//    알림은 현재 사용하지 않으며, 구현체가 없어서 주석처리합니다.
//    private final NotificationService notificationService;

    private final Logger LOGGER = LoggerFactory.getLogger(SubscribeController.class.getSimpleName());

//    상기 구현체 이유로 주석처리 합니다.
//    public SubscribeController(JwtTokenProvider jwtTokenProvider, SubscribeService subscribeService, NotificationService notificationService) {
//        this.jwtTokenProvider = jwtTokenProvider;
//        this.subscribeService = subscribeService;
//        this.notificationService = notificationService;
//    }

    // 리팩토링 진행중에만 임시로 사용하는 생성자입니다.
    public SubscribeController(SubscribeService subscribeService) {
        this.subscribeService = subscribeService;
    }

//    스웨거 설정이 없어서 관련 annotation은 임시로 주석처리 합니다.
//    @ApiImplicitParams({
//          @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")})
    @GetMapping(value = "/getsubscriber")
    public ResponseEntity<List<VelogUserInfoDto>> getSubscriber(@RequestHeader("X-AUTH-TOKEN") String token) throws IOException {//제네릭 안의 제네릭 -> 별도응답 객체를 만들어야함!
        // 해당 유저가 구독한 대상 리스트를 불러오는 GET api 입니다.
        // throw에 SubscribeException 추가해야 합니다.

        //String userName = jwtTokenProvider.getUsername(token);
        String userName = "test"; // JWT 구현체가 없어서 임시로 사용합니다.

        //Todo: 반환 타입을 바꿔서 주기. 구독자 리스트 + 이미지 url 추가해서

        List<VelogUserInfoDto> subscribers = subscribeService.getSubscribers(userName);

        return ResponseEntity.status(HttpStatus.OK).body(subscribers);
    }

//    스웨거 설정이 없어서 관련 annotation은 임시로 주석처리 합니다.
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")})
    @PostMapping(value = "/addsubscriber")
    public ResponseEntity<String> addSubscriber(@RequestHeader("X-AUTH-TOKEN") String token, @RequestParam String name, @RequestParam String fcmToken) throws IOException{
        // 유저의 구독자를 추가하는 POST api 입니다.
        // throw에 SubscribeException 추가해야 합니다.
        // String userName = jwtTokenProvider.getUsername(token);
        String userName = "test"; // JWT 구현체가 없어서 임시로 사용합니다.

        //JoinGroupDto joinGroupDto = new JoinGroupDto(userName, name); 알림 관련 구현체 없어서 임시 주석 처리
        subscribeService.addSubscribe(userName, name);
        //notificationService.joinGroup(joinGroupDto); 알림 관련 구현체 없어서 임시 주석 처리

        return ResponseEntity.status(HttpStatus.OK).body("Success");//임시
    }

//    스웨거 설정이 없어서 관련 annotation은 임시로 주석처리 합니다.
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")})
    @GetMapping(value = "/subscriberpost")//responseentity가 없어도 되는지 테스트 되는지 확인해보기//403 402같은 코드를 던질경우 쓰기
    public ResponseEntity<SubscriberPostsDto> getSubscriberPost(@RequestHeader("X-AUTH-TOKEN") String token) throws IOException{
        // 유저가 구독하고 있는 velog 유저들의 post를 리턴하는 GET api입니다.
        // throw에 SubscribeException 추가해야 합니다.

        //String userName = jwtTokenProvider.getUsername(token);
        String userName = "test"; // JWT 구현체가 없어서 임시로 사용합니다.

        SubscriberPostsDto subscriberPostsDto = subscribeService.getSubscribersPost(userName);

        return ResponseEntity.status(HttpStatus.OK).body(subscriberPostsDto);
    }

//    스웨거 설정이 없어서 관련 annotation은 임시로 주석처리 합니다.
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")})
    @GetMapping("/validate/{name}")
    @ResponseBody
    public ResponseEntity<ValidateVelogUserDto> validateUser(@PathVariable String name) throws IOException {
        // 입력 된 id가 실제 velog에 존재하는 유저인지 확인하는 GET api입니다.

        ValidateVelogUserDto validateVelogUserDto = new ValidateVelogUserDto(name);
        Boolean isPresent = subscribeService.isValidateUser(validateVelogUserDto);
        subscribeService.getVelogUserProfile(isPresent, validateVelogUserDto);

        return ResponseEntity.status(HttpStatus.OK).body(validateVelogUserDto);
    }

    @DeleteMapping("/unsubscribe/{targetName}")
    @ResponseBody
    public ResponseEntity<UnsubscribeDto> unSubscribe(@RequestHeader("X-AUTH-TOKEN") String token, @PathVariable String targetName) {
        // 구독을 취소하는 DELETE api입니다.

        //String userName = jwtTokenProvider.getUsername(token);
        String userName = "test"; // JWT 구현체가 없어서 임시로 사용합니다.
        UnsubscribeDto unsubscribeDto = subscribeService.deleteSubscribe(userName, targetName); // 이거 common dto 어떤지?
        subscribeService.validateTarget(targetName);
        return ResponseEntity.status(HttpStatus.OK).body(unsubscribeDto);
    }

    @GetMapping("/trendposts")
    @ResponseBody
    public ResponseEntity<TrendResultDto> trendPosts(@RequestHeader("X-AUTH-TOKEN") String token){
        // Velog 메인에서 제공하는 실시간 트렌드 포스트들을 리턴합니다.
        // throw에 SubscribeException 추가해야 합니다.

        //String userName = jwtTokenProvider.getUsername(token);
        String userName = "test"; // JWT 구현체가 없어서 임시로 사용합니다.
        try {
            TrendResultDto trendResultDto = subscribeService.collectTrendPost(userName);
            return ResponseEntity.status(HttpStatus.OK).body(trendResultDto);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

//    스웨거 설정이 없어서 관련 annotation은 임시로 주석처리 합니다.
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")})
    @GetMapping("/usermain/{name}")
    @ResponseBody
    public UserMainDto userMain(@PathVariable String name) {
        // Velog 유저의 프로필 url을 리턴하는 GET api 입니다.
        return subscribeService.getUserMain(name);
    }

}