insert into public.person (id, created_at, entity_status, updated_at, birth_date, first_name, home_address, language, last_name, mail, patronymic, state, telephone, telegram_id)
values  (44, '2021-08-03 17:52:20.386375', 'ACTIVE', '2021-08-05 16:57:27.585185', '2000-11-30', 'viktor', 'Mogilev, kutepova 46 20', 'RUSSIAN', '3', 'fpmi.graskovviktor@gmail.com', 'ivanovich', 'class com.sbo.bot.state.impl.management.ManagementState', 375297473331, 5362802084),
        (8, '2021-08-03 17:52:20.386375', 'ACTIVE', '2021-08-05 16:57:27.585185', '2000-11-30', 'viktor', 'Mogilev, kutepova 46 20', 'RUSSIAN', '7', 'fpmi.graskovviktor@gmail.com', 'ivanovich', 'class com.sbo.bot.state.impl.management.ManagementState', 375297473331, 5362802088),
        (5, '2021-08-03 17:52:20.386375', 'ACTIVE', '2021-08-05 16:57:27.585185', '2000-11-30', 'viktor', 'Mogilev, kutepova 46 20', 'RUSSIAN', '4', 'fpmi.graskovviktor@gmail.com', 'ivanovich', 'class com.sbo.bot.state.impl.management.ManagementState', 375297473331, 5362802085),
        (6, '2021-08-03 17:52:20.386375', 'ACTIVE', '2021-08-05 16:57:27.585185', '2000-11-30', 'viktor', 'Mogilev, kutepova 46 20', 'RUSSIAN', '5', 'fpmi.graskovviktor@gmail.com', 'ivanovich', 'class com.sbo.bot.state.impl.management.ManagementState', 375297473331, 5362802086),
        (7, '2021-08-03 17:52:20.386375', 'ACTIVE', '2021-08-05 16:57:27.585185', '2000-11-30', 'viktor', 'Mogilev, kutepova 46 20', 'RUSSIAN', '6', 'fpmi.graskovviktor@gmail.com', 'ivanovich', 'class com.sbo.bot.state.impl.management.ManagementState', 375297473331, 5362802087),
        (4, '2021-08-05 18:45:37.097493', 'ACTIVE', '2021-08-05 18:45:37.104170', '2000-11-30', 'viktor', 'Mogilev, kutepova 46 20', 'ENGLISH', '12', 'fpmi.graskovviktor@gmail.com', 'ivanovich', 'class com.sbo.bot.state.impl.StartState', 375297473331, 461395480),
        (10, '2021-08-05 20:37:42.204728', 'ACTIVE', '2021-08-05 20:37:42.206434', '2000-11-30', 'viktor', 'Mogilev, kutepova 46 20', 'ENGLISH', '13', 'fpmi.graskovviktor@gmail.com', 'ivanovich', 'class com.sbo.bot.state.impl.StartState', 375297473331, 803100319),
        (1, '2021-08-03 17:52:20.386375', 'ACTIVE', '2021-08-12 16:09:32.863017', '2000-11-30', 'viktor', 'Mogilev, kutepova 46 20', 'ENGLISH', 'Graskov', 'fpmi.graskovviktor@gmail.com', 'ivanovich', 'class com.sbo.bot.state.impl.timetable.TimetableState', 375297473331, 536280208),
        (3, '2021-08-05 17:51:19.932911', 'ACTIVE', '2021-08-11 14:36:49.601014', '2000-11-30', 'viktor', 'Mogilev, kutepova 46 20', 'ENGLISH', '14', 'fpmi.graskovviktor@gmail.com', 'ivanovich', 'class com.sbo.bot.state.impl.StartState', 375297473331, 869071391);

insert into public.roles (person_id, roles)
values  (1, 'ADMIN'),
        (4, 'USER'),
        (10, 'USER'),
        (3, 'UNAUTHORIZED');

