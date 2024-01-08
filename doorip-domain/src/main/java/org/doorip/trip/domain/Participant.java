package org.doorip.trip.domain;

import jakarta.persistence.*;
import lombok.*;
import org.doorip.common.BaseTimeEntity;
import org.doorip.todo.domain.Allocator;
import org.doorip.user.domain.User;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
@Entity
public class Participant extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "participant_id")
    private Long id;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(nullable = false, name = "style_a")
    private Integer styleA;
    @Column(nullable = false, name = "style_b")
    private Integer styleB;
    @Column(nullable = false, name = "style_c")
    private Integer styleC;
    @Column(nullable = false, name = "style_d")
    private Integer styleD;
    @Column(nullable = false, name = "style_e")
    private Integer styleE;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    private Trip trip;
    @OneToOne(mappedBy = "participant", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Allocator allocator;

    public static Participant createParticipant(Role role, int styleA, int styleB, int styleC,
                                                int styleD, int styleE, User user, Trip trip){
        Participant participant = Participant.builder()
                .role(role)
                .styleA(styleA)
                .styleB(styleB)
                .styleC(styleC)
                .styleD(styleD)
                .styleE(styleE)
                .user(user)
                .trip(trip)
                .build();
        participant.changeTrip(trip);

        return participant;
    }

    public void changeTrip(Trip trip) {
        if (this.trip != null) {
            this.trip.removeParticipant(this);
        }
        this.trip = trip;
        trip.addParticipant(this);
    }
}
