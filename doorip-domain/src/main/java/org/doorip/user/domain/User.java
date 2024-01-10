package org.doorip.user.domain;

import jakarta.persistence.*;
import lombok.*;
import org.doorip.common.BaseTimeEntity;
import org.doorip.trip.domain.Participant;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
@Table(name = "users")
@Entity
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String intro;
    private String result;
    @Column(nullable = false)
    private String platformId;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Platform platform;
    private String refreshToken;
    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Participant> participants = new ArrayList<>();

    public static User createUser(String name, String intro, String platformId, Platform platform) {

        return User.builder()
                .name(name)
                .intro(intro)
                .platformId(platformId)
                .platform(platform)
                .build();
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateResult(String result) {
        this.result = result;
    }
}
