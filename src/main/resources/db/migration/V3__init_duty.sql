insert into public.duties (id, created_at, entity_status, updated_at, duty_from, duty_to, is_people_could_change,
                           max_people_on_duty, description)
values (1, '2021-08-03 17:52:20.386375', 'ACTIVE', '2021-08-05 16:57:27.585185', '2021-08-18 17:52:20',
        '2021-08-18 20:18:54', true, 2, 'simple evening duty'),
       (2, '2021-08-03 17:52:20.386375', 'ACTIVE', '2021-08-05 16:57:27.585185', '2021-08-18 19:52:20',
        '2021-08-18 23:18:54', true, 2, '');

insert into duty_type (duty_id, duty_types)
values (1, 'EVENING'),
       (2, 'EVENING');


insert into public.people_on_duty (id, created_at, updated_at, entity_status, person_id, duty_id, on_duty_from,
                                   on_duty_to, status, is_people_could_change)
values (1, '2021-08-13 00:43:44.000000', '2021-08-13 00:43:49.000000', 'ACTIVE', 1, 1, '2021-08-18 17:52:20.386000',
        '2021-08-18 20:18:54.000000', 'ACTIVE', true),
       (3, '2021-08-13 00:43:44.000000', '2021-08-13 00:43:49.000000', 'ACTIVE', 1, 2, '2021-08-18 17:52:20.386000',
        '2021-08-18 20:18:54.000000', 'ACTIVE', true),
       (4, '2021-08-13 00:43:44.000000', '2021-08-13 00:43:49.000000', 'ACTIVE', 3, 2, '2021-08-18 17:52:20.386000',
        '2021-08-18 20:18:54.000000', 'ACTIVE', true),
       (2, '2021-08-13 00:43:44.000000', '2021-08-13 00:43:49.000000', 'ACTIVE', 3, 1, '2021-08-18 17:52:20.386000',
        '2021-08-18 20:18:54.000000', 'ACTIVE', true);