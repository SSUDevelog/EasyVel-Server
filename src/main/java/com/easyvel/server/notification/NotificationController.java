package com.easyvel.server.notification;

import com.easyvel.server.annotation.EasyvelTokenApiImplicitParams;
import com.easyvel.server.config.security.SecurityConfiguration;
import com.easyvel.server.jwt.JwtTokenProvider;
import com.easyvel.server.notification.fcm.FcmService;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.messaging.FirebaseMessagingException;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/notice")
public class NotificationController {
    //Todo: 나중에 제한 잘 걸어둬야합니다!!

    private final JwtTokenProvider jwtTokenProvider;
    private final FcmService fcmService;

    @EasyvelTokenApiImplicitParams
    @ApiOperation(value = "알림 토큰 등록", notes = "fcmToken을 body noticeToken으로 전송")
    @PostMapping("/token")
    public void enableToken(@RequestHeader(SecurityConfiguration.TOKEN_HEADER) String token, @RequestBody String noticeToken) throws FirebaseAuthException {
        String uid = jwtTokenProvider.getUid(token);

        fcmService.enableFcmToken(uid, noticeToken);
    }

    @EasyvelTokenApiImplicitParams
    @ApiOperation(value = "알림 토큰 삭제")
    @DeleteMapping ("/token")
    public void disableToken(@RequestHeader(SecurityConfiguration.TOKEN_HEADER) String token) {
        String uid = jwtTokenProvider.getUid(token);

        fcmService.disableFcmToken(uid);
    }

    //Todo: 관리자 권한 설정 필요

    @EasyvelTokenApiImplicitParams
    @ApiOperation(value = "알림 그룹 생성", notes = "생성할 그룹을 입력")
    @PostMapping("/group")
    public void createNoticeGroup(@RequestBody String groupName) {
        fcmService.createNoticeGroup(groupName);
    }
    
    @EasyvelTokenApiImplicitParams
    @ApiOperation(value = "알림 그룹 삭제", notes = "삭제할 그룹을 입력")
    @DeleteMapping("/group")
    public void deleteNoticeGroup(@RequestBody String groupName) {
        fcmService.deleteNoticeGroup(groupName);
    }
    
    @EasyvelTokenApiImplicitParams
    @ApiOperation(value = "알림 그룹 참가", notes = "참가할 그룹을 groupName에 입력")
    @PostMapping("/group/{groupName}")
    public void joinNoticeGroup(@RequestHeader(SecurityConfiguration.TOKEN_HEADER) String token, @PathVariable String groupName) {
        String uid = jwtTokenProvider.getUid(token);

        fcmService.joinNoticeGroup(uid, groupName);
    }
    
    @EasyvelTokenApiImplicitParams
    @ApiOperation(value = "알림 그룹 탈퇴", notes = "탈퇴할 그룹을 groupName에 입력")
    @DeleteMapping("/group/{groupName}")
    public void outNoticeGroup(@RequestHeader(SecurityConfiguration.TOKEN_HEADER) String token, @PathVariable String groupName) {
        String uid = jwtTokenProvider.getUid(token);

        fcmService.outNoticeGroup(uid, groupName);
    }

    // Todo: 테스트 구현
    @EasyvelTokenApiImplicitParams
    @ApiOperation(value = "그룹 알림 메시지 전송", notes = "단체 전송할 메시지 데이터를 맵 형태로 입력")
    @PostMapping("/group/{groupName}/send")
    public void sendMessageToGroup(@PathVariable String groupName, Map<String, String> messageData) throws FirebaseMessagingException {
        fcmService.sendMessageToGroup(groupName, messageData);
    }
}