package org.doorip.trip.domain;

import jakarta.persistence.*;
import lombok.*;
import org.doorip.common.BaseTimeEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
@Entity
public class Todo extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "todo_id")
    private Long id;
    @Column(nullable = false)
    private String title;
    private LocalDate endDate;
    private String memo;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Secret secret;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Progress progress;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    private Trip trip;
    @Builder.Default
    @OneToMany(mappedBy = "todo", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private List<Allocator> allocators = new ArrayList<>();

    public static Todo createTodo(String title, LocalDate endDate, String memo, Secret secret, Trip trip) {
        Todo todo = Todo.builder()
                .title(title)
                .endDate(endDate)
                .memo(memo)
                .secret(secret)
                .progress(Progress.INCOMPLETE)
                .build();
        todo.changeTrip(trip);
        return todo;
    }

    private void changeTrip(Trip trip) {
        if (this.trip != null) {
            this.trip.removeTodo(this);
        }
        this.trip = trip;
        trip.addTodo(this);
    }

    public void addAllocator(Allocator allocator) {
        allocators.add(allocator);
    }

    public void removeAllocator(Allocator allocator) {
        allocators.remove(allocator);
    }

    public void complete() {
        this.progress = Progress.COMPLETE;
    }

    public void incomplete() {
        this.progress = Progress.INCOMPLETE;
    }
}
