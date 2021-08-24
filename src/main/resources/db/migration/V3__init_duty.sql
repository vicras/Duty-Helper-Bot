insert into public.duties (id, created_at, entity_status, updated_at, duty_from, duty_to, is_people_could_change,
                           max_people_on_duty, description)
values (1, '2021-08-03 17:52:20.386375', 'ACTIVE', '2021-08-05 17:00:00', '2021-08-18 17:00:00',
        '2021-08-18 19:00:00', true, 2, 'simple duty 17.00-19.00'),
       (2, '2021-08-03 17:52:20.386375', 'ACTIVE', '2021-08-05 16:57:27.585185', '2021-08-18 19:00:00',
        '2021-08-18 23:59:00', true, 1, 'simple duty 19.00-23.59'),
       (3, '2021-08-03 17:52:20.386375', 'ACTIVE', '2021-08-05 16:57:27.585185', '2021-08-19 19:00:00',
        '2021-08-18 23:59:00', true, 1, 'duty 19.00-23.59');

insert into duty_type (duty_id, duty_types)
values (1, 'EVENING'),
       (2, 'EVENING'),
       (3, 'EVENING');


insert into public.people_on_duty (id, created_at, updated_at, entity_status, person_id, duty_id, range, status,
                                   is_people_could_change)
values (1, '2021-08-13T00:43:44.000000', '2021-08-13T00:43:49.000000', 'ACTIVE', 1, 1,
        '2021-08-18T17:00:00 2021-08-18T20:00:00', 'ACTIVE', true),
       (2, '2021-08-13T00:43:44', '2021-08-13T00:43:49', 'ACTIVE', 2, 1,
        '2021-08-18T17:52:20 2021-08-18T20:18:54', 'ACTIVE', true),
       (3, '2021-08-13T00:43:44.000000', '2021-08-13T00:43:49.000000', 'ACTIVE', 3, 2,
        '2021-08-18T17:52:20 2021-08-18T20:18:54', 'ACTIVE', true),

       (4, '2021-08-13 00:43:44.000000', '2021-08-13T00:43:49.000000', 'ACTIVE', 1, 3,
        '2021-08-18T17:52:20 2021-08-18T20:18:54', 'ACTIVE', true);