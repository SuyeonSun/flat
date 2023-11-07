package com.flat.backend.friends;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FriendGrade {
    BEST(0),
    WORST(1);
    final int value;
}
