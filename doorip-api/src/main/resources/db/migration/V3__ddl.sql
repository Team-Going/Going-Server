alter table allocator drop foreign key allocator_ibfk_1;
drop index participant_id on allocator;
alter table allocator add constraint allocator_ibfk_1 foreign key (participant_id) references participant (participant_id);