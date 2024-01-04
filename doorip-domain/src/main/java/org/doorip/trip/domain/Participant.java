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
    @Column(nullable = false)
    private Integer styleA;
    @Column(nullable = false)
    private Integer styleB;
    @Column(nullable = false)
    private Integer styleC;
    @Column(nullable = false)
    private Integer styleD;
    @Column(nullable = false)
    private Integer styleE;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    private Trip trip;
    @OneToOne(mappedBy = "participant", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Allocator allocator;
}
