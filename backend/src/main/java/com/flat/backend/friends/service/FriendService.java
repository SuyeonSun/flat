package com.flat.backend.friends.service;

import com.flat.backend.friends.dto.RequestDto;
import com.flat.backend.friends.dto.ResponseDto;
import com.flat.backend.friends.repository.FriendsRepository;
import com.flat.backend.friends.repository.ReqFriendRepository;
import com.flat.backend.friends.repository.entity.ReqFriendDto;
import com.flat.backend.user.repository.UserRepository;
import com.flat.backend.user.repository.entity.User;
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
        ReqFriendDto reqFriendDto = ReqFriendDto.builder()
                        .senderId(userRepository.findById(requestDto.getSenderId()).orElseThrow())
                                .receiverId(userRepository.findById(requestDto.getReceiverId()).orElseThrow())
                                        .build();

        reqFriendRepository.save(reqFriendDto);
        User sendUser = userRepository.findById(requestDto.getSenderId()).orElseThrow();
        User recvUser = userRepository.findById(requestDto.getReceiverId()).orElseThrow();
        sendUser.getSentFriendRequests().add(reqFriendDto);
        log.info("sentFriendRequests.length: {}", sendUser.getSentFriendRequests().size());
        recvUser.getReceivedFriendRequests().add(reqFriendDto);
        log.info("receivedFriendRequests.length: {}", recvUser.getReceivedFriendRequests().size());

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


}
