CREATE TABLE duties
(
    id                     BIGSERIAL NOT NULL,
    created_at             TIMESTAMP WITHOUT TIME ZONE,
    updated_at             TIMESTAMP WITHOUT TIME ZONE,
    entity_status          VARCHAR(255),
    duty_from              TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    duty_to                TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    max_people_on_duty     BIGINT    NOT NULL,
    is_people_could_change BOOLEAN   NOT NULL,
    description            VARCHAR(255),
    CONSTRAINT pk_duties PRIMARY KEY (id)
);

CREATE TABLE duty_type
(
    duty_id    BIGINT NOT NULL,
    duty_types VARCHAR
);

CREATE TABLE exchange
(
    id                     BIGSERIAL NOT NULL,
    created_at             TIMESTAMP WITHOUT TIME ZONE,
    updated_at             TIMESTAMP WITHOUT TIME ZONE,
    entity_status          VARCHAR(255),
    exchange_request_state VARCHAR(255),
    from_time              TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    to_time                TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    from_recipient         TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    to_recipient           TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_exchange PRIMARY KEY (id)
);

CREATE TABLE notifications
(
    id                                 BIGSERIAL NOT NULL,
    created_at                         TIMESTAMP WITHOUT TIME ZONE,
    updated_at                         TIMESTAMP WITHOUT TIME ZONE,
    entity_status                      VARCHAR(255),
    notifications_to_notified_users_id BIGINT,
    text                               VARCHAR(255),
    author_id                          BIGINT,
    expiration_date                    TIMESTAMP WITHOUT TIME ZONE,
    notification_type                  INTEGER   NOT NULL,
    CONSTRAINT pk_notifications PRIMARY KEY (id)
);

CREATE TABLE notifications_to_notified
(
    id            BIGSERIAL NOT NULL,
    created_at    TIMESTAMP WITHOUT TIME ZONE,
    updated_at    TIMESTAMP WITHOUT TIME ZONE,
    entity_status VARCHAR(255),
    CONSTRAINT pk_notificationstonotified PRIMARY KEY (id)
);

CREATE TABLE notifications_to_notified_notifications
(
    notifications_to_notified_users_id BIGINT NOT NULL,
    notifications_id                   BIGINT NOT NULL
);

CREATE TABLE notifications_to_notified_notified_users
(
    notifications_to_notified_users_id BIGINT NOT NULL,
    notified_users_id                  BIGINT NOT NULL
);

CREATE TABLE people_on_duty
(
    id                     BIGSERIAL    NOT NULL,
    created_at             TIMESTAMP WITHOUT TIME ZONE,
    updated_at             TIMESTAMP WITHOUT TIME ZONE,
    entity_status          VARCHAR(255),
    person_id              BIGINT       NOT NULL,
    duty_id                BIGINT       NOT NULL,
    on_duty_from           TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    on_duty_to             TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    status                 VARCHAR(255) NOT NULL,
    is_people_could_change BOOLEAN      NOT NULL,
    CONSTRAINT pk_people_on_duty PRIMARY KEY (id)
);

CREATE TABLE person
(
    id            BIGSERIAL    NOT NULL,
    created_at    TIMESTAMP WITHOUT TIME ZONE,
    updated_at    TIMESTAMP WITHOUT TIME ZONE,
    entity_status VARCHAR(255),
    first_name    VARCHAR(255),
    last_name     VARCHAR(255),
    patronymic    VARCHAR(255),
    telegram_id   BIGINT       NOT NULL,
    birth_date    date,
    telephone     BIGINT,
    mail          VARCHAR(255),
    home_address  VARCHAR(255),
    language      VARCHAR(255) NOT NULL,
    state         VARCHAR(255),
    CONSTRAINT pk_person PRIMARY KEY (id)
);

CREATE TABLE roles
(
    person_id BIGINT NOT NULL,
    roles     VARCHAR
);

ALTER TABLE notifications_to_notified_notifications
    ADD CONSTRAINT uc_notifications_to_notified_notifications_notifications UNIQUE (notifications_id);

ALTER TABLE notifications_to_notified_notified_users
    ADD CONSTRAINT uc_notifications_to_notified_notified_users_notifiedusers UNIQUE (notified_users_id);

ALTER TABLE person
    ADD CONSTRAINT uc_person_telegram UNIQUE (telegram_id);

ALTER TABLE notifications
    ADD CONSTRAINT FK_NOTIFICATIONS_ON_AUTHOR FOREIGN KEY (author_id) REFERENCES person (id);

ALTER TABLE notifications
    ADD CONSTRAINT FK_NOTIFICATIONS_ON_NOTIFICATIONSTONOTIFIEDUSERS FOREIGN KEY (notifications_to_notified_users_id) REFERENCES notifications_to_notified (id);

ALTER TABLE people_on_duty
    ADD CONSTRAINT FK_PEOPLE_ON_DUTY_ON_DUTY FOREIGN KEY (duty_id) REFERENCES duties (id);

ALTER TABLE people_on_duty
    ADD CONSTRAINT FK_PEOPLE_ON_DUTY_ON_PERSON FOREIGN KEY (person_id) REFERENCES person (id);

ALTER TABLE duty_type
    ADD CONSTRAINT fk_duty_type_on_duty FOREIGN KEY (duty_id) REFERENCES duties (id);

ALTER TABLE notifications_to_notified_notifications
    ADD CONSTRAINT fk_notnot_on_notification FOREIGN KEY (notifications_id) REFERENCES notifications (id);

ALTER TABLE notifications_to_notified_notifications
    ADD CONSTRAINT fk_notnot_on_notifications_to_notified_users FOREIGN KEY (notifications_to_notified_users_id) REFERENCES notifications_to_notified (id);

ALTER TABLE notifications_to_notified_notified_users
    ADD CONSTRAINT fk_notnot_on_person FOREIGN KEY (notified_users_id) REFERENCES person (id);

ALTER TABLE roles
    ADD CONSTRAINT fk_roles_on_person FOREIGN KEY (person_id) REFERENCES person (id);