package com.flat.backend.friends.service;

import com.amazonaws.Response;
import com.flat.backend.friends.dto.FriendDeleteDto;
import com.flat.backend.friends.dto.RequestDto;
import com.flat.backend.friends.dto.ResponseDto;
import com.flat.backend.friends.repository.FriendsRepository;
import com.flat.backend.friends.repository.ReqFriendRepository;
import com.flat.backend.friends.repository.entity.Friends;
import com.flat.backend.friends.repository.entity.ReqFriendDto;
import com.flat.backend.user.repository.UserRepository;
import com.flat.backend.user.repository.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FriendService{

    private final UserRepository userRepository;
    private final ReqFriendRepository reqFriendRepository;
    private final FriendsRepository friendsRepository;

    public boolean findPrevRequest(RequestDto requestDto) {
        if(reqFriendRepository.findBySenderId_IdAndReceiverId_Id(requestDto.getSenderId(), requestDto.getReceiverId()).isEmpty()) {
            return reqFriendRepository.findBySenderId_IdAndReceiverId_Id(requestDto.getReceiverId(), requestDto.getSenderId()).isEmpty();
        }
        return false;
    }

    public ResponseEntity<?> sendRequest(RequestDto requestDto) {
        if(!findPrevRequest(requestDto)) {
            return ResponseEntity.badRequest()
                    .body("이미 존재하는 친구요청 입니다.");
        }

        // Todo 이미 친구인 경우를 프론트에서 처리? 백에서 처리?


        ReqFriendDto reqFriendDto = ReqFriendDto.builder()
                        .senderId(userRepository.findById(requestDto.getSenderId()).orElseThrow())
                                .receiverId(userRepository.findById(requestDto.getReceiverId()).orElseThrow())
                                        .build();

        reqFriendRepository.save(reqFriendDto);
        reqFriendDto.getSenderId().getSentFriendRequests().add(reqFriendDto);
        reqFriendDto.getReceiverId().getReceivedFriendRequests().add(reqFriendDto);

        return ResponseEntity.ok()
                .body(reqFriendDto);
    }

    public ResponseEntity<?> getSendRequests(UUID senderId) {
        List<ReqFriendDto> reqFriendDtos = reqFriendRepository.findBySenderId_Id(senderId).orElseThrow();
        List<ResponseDto> responseDtos = new ArrayList<>();
        for (ReqFriendDto reqFriendDto : reqFriendDtos) {
            responseDtos.add(ResponseDto.builder()
                    .senderId(reqFriendDto.getSenderId().getId())
                    .receiverId(reqFriendDto.getReceiverId().getId())
                    .build());
        }
        return ResponseEntity.ok()
                .body(responseDtos);
    }

    public ResponseEntity<?> getRecvRequests(UUID receiverId) {
        List<ReqFriendDto> reqFriendDtos = reqFriendRepository.findByReceiverId_Id(receiverId).orElseThrow();
        List<ResponseDto> responseDtos = new ArrayList<>();
        for (ReqFriendDto reqFriendDto : reqFriendDtos) {
            responseDtos.add(ResponseDto.builder()
                    .senderId(reqFriendDto.getSenderId().getId())
                    .receiverId(reqFriendDto.getReceiverId().getId())
                    .build());
        }
        return ResponseEntity.ok()
                .body(responseDtos);
    }

    public ResponseEntity<?> connectFriend(RequestDto requestDto) {

        if(reqFriendRepository.findBySenderId_IdAndReceiverId_Id(requestDto.getSenderId(), requestDto.getReceiverId()).isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        User sendUser = userRepository.findById(requestDto.getSenderId()).orElseThrow();
        User recvUser = userRepository.findById(requestDto.getReceiverId()).orElseThrow();

        // 친구 요청 보낸 쪽 처리
        Friends friendsSend = Friends.builder()
                .userId(sendUser)
                .friendId(recvUser)
                .build();
        friendsRepository.save(friendsSend);
        sendUser.getFriends().add(friendsSend);

        // 친구 요청 받은 쪽 처리
        Friends friendsRecv = Friends.builder()
                .userId(recvUser)
                .friendId(sendUser)
                .build();
        friendsRepository.save(friendsRecv);
        recvUser.getFriends().add(friendsRecv);

        ReqFriendDto reqFriendDto = reqFriendRepository.findBySenderId_IdAndReceiverId_Id(requestDto.getSenderId(), requestDto.getReceiverId()).orElseThrow();
        reqFriendRepository.delete(reqFriendDto);

        log.info("SendUser= {}", sendUser.getFriends().size());
        log.info("RecvUser= {}", recvUser.getFriends().size());

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> rejectRequest(RequestDto requestDto) {
        ReqFriendDto reqFriendDto = reqFriendRepository.findBySenderId_IdAndReceiverId_Id(requestDto.getSenderId(), requestDto.getReceiverId()).orElseThrow();
        reqFriendRepository.delete(reqFriendDto);

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> removeRequest(RequestDto requestDto) {
        ReqFriendDto reqFriendDto = reqFriendRepository.findBySenderId_IdAndReceiverId_Id(requestDto.getSenderId(), requestDto.getReceiverId()).orElseThrow();
        reqFriendRepository.delete(reqFriendDto);

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> getFriends(UUID userId) {
        List<User> list = new ArrayList<User>();
        for(Friends friends : userRepository.findById(userId).orElseThrow().getFriends()) {
            list.add(userRepository.findById(friends.getFriendId().getId()).orElseThrow());
        }
        return ResponseEntity.ok()
                .body(list);
    }

    public ResponseEntity<?> removeFriend(FriendDeleteDto friendDeleteDto) {
        User user = userRepository.findById(friendDeleteDto.getUserId()).orElseThrow();
        User friend = userRepository.findById(friendDeleteDto.getFriendId()).orElseThrow();

        log.info("userId = {}", user.getId());
        log.info("friendId = {}", friend.getId());

        log.info("userId의 friend = {}", user.getFriends().size());
        log.info("friendId의 friend = {}", friend.getFriends().size());

        Friends userIdFriends = friendsRepository.findByUserId_IdAndFriendId_Id(user.getId(), friend.getId()).orElseThrow();
        Friends friendIdFriends = friendsRepository.findByUserId_IdAndFriendId_Id(friend.getId(), user.getId()).orElseThrow();

        user.getFriends().remove(userIdFriends);
        friend.getFriends().remove(friendIdFriends);

        friendsRepository.delete(userIdFriends);
        friendsRepository.delete(friendIdFriends);

        log.info("userId의 friend = {}", user.getFriends().size());
        log.info("friendId의 friend = {}", friend.getFriends().size());

        return ResponseEntity.ok().build();
    }

}
