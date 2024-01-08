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
    @JoinColumn(name = "todo_id")
    private Todo todo;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id")
    private Participant participant;

    public static Allocator createAllocator(Todo todo, Participant participant) {
        Allocator allocator = Allocator.builder()
                .build();
        allocator.changeTodo(todo);
        allocator.changeParticipant(participant);
        return allocator;
    }

    private void changeTodo(Todo todo) {
        if (this.todo != null) {
            this.todo.removeAllocator(this);
        }
        this.todo = todo;
        todo.addAllocator(this);
    }

    private void changeParticipant(Participant participant) {
        if (this.participant != null) {
            this.participant.removeAllocator(null);
        }
        this.participant = participant;
        participant.addAllocator(this);
    }
}
