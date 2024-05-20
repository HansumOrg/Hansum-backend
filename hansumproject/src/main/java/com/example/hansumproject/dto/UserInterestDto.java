package com.example.hansumproject.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class UserInterestDto {
    private List<String> interestedLocation;
    private List<String> interestedHobby;
    private List<String> interestedFood;
}
