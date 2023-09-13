package com.acorn.acornstore.domain;

import lombok.*;

import javax.persistence.*;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserProfileImage extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    private String profileImg;

    public UserProfileImage(String profileImg, User user) {
        this();
        this.user = user;
        this.profileImg = profileImg;
    }

    public void setUser(User user) {
        this.user = user;
    }
}