insert into public.person (id, created_at, entity_status, updated_at, birth_date, first_name, home_address, language, last_name, mail, patronymic, state, telephone, telegram_id)
values  (3, '2021-08-03 17:52:20', 'ACTIVE', '2021-08-05 16:57:27', '2000-02-07', 'Viktor', 'Vileyka, Cvetochnaya', 'RUSSIAN', 'Plesko', 'viktor.plesko@gmail.com', 'Anatolevich', 'class com.sbo.bot.state.impl.StartState', 375297473331, 426970446),
        (2, '2021-08-05 20:37:42', 'ACTIVE', '2021-08-05 20:37:42', '2000-10-20', 'Dmitriy', 'Mogilev, Dzimitrava Ave', 'ENGLISH', 'Tarasenko', 'dmitriy.tarasenko@gmail.com', 'Alexseivich', 'class com.sbo.bot.state.impl.StartState', 375297473331, 869071391),
        (1, '2021-08-03 17:52:20', 'ACTIVE', '2021-08-12 16:09:32', '2000-11-30', 'Viktor', 'Mogilev, Kutepova', 'ENGLISH', 'Graskov', 'fpmi.graskovviktor@gmail.com', 'Ivanovich', 'class com.sbo.bot.state.impl.StartState', 375297473331, 536280208);

insert into public.roles (person_id, roles)
values  (1, 'ADMIN'),
        (2, 'USER'),
        (3, 'USER'),
        (3, 'UNAUTHORIZED');

