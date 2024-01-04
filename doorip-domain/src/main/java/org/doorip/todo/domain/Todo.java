package org.doorip.todo.domain;

import jakarta.persistence.*;
import lombok.*;
import org.doorip.common.BaseTimeEntity;
import org.doorip.trip.domain.Trip;

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
    @OneToMany(mappedBy = "todo", cascade = CascadeType.REMOVE)
    private List<Allocator> allocators = new ArrayList<>();
}
