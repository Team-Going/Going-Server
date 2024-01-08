package org.doorip.trip.domain;

import jakarta.persistence.*;
import lombok.*;
import org.doorip.common.BaseTimeEntity;
import org.doorip.todo.domain.Todo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
@Entity
public class Trip extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trip_id")
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private LocalDate startDate;
    @Column(nullable = false)
    private LocalDate endDate;
    @Column(nullable = false)
    private String code;
    @Builder.Default
    @OneToMany(mappedBy = "trip", cascade = CascadeType.REMOVE)
    private List<Participant> participants = new ArrayList<>();
    @Builder.Default
    @OneToMany(mappedBy = "trip", cascade = CascadeType.REMOVE)
    private List<Todo> todos = new ArrayList<>();

    public static Trip createTrip(String title, LocalDate startDate, LocalDate endDate, String code) {
        return Trip.builder()
                .title(title)
                .startDate(startDate)
                .endDate(endDate)
                .code(code)
                .build();
    }

    public void addParticipant(Participant participant) {
        participants.add(participant);
    }

    public void removeParticipant(Participant participant) {
        participants.remove(participant);
    }
}
