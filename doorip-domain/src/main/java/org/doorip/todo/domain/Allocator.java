package org.doorip.todo.domain;

import jakarta.persistence.*;
import lombok.*;
import org.doorip.common.BaseTimeEntity;
import org.doorip.trip.domain.Participant;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
@Entity
public class Allocator extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "allocator_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id")
    private Todo todo;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_id")
    private Participant participant;
}
