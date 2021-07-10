Hibernate:
create table account
(
    account_id bigint not null auto_increment,
    balance decimal(19,2),
    currency varchar(255),
    is_blocked bit,
    is_deleted bit,
    number varchar(255),
    user_user_id bigint,
    primary key (account_id)
) engine=InnoDB

Hibernate:
create table bank_card
(
    card_id bigint not null auto_increment,
     cvv integer,
     is_active bit,
     number varchar(16),
     validity varchar(255),
     account_account_id bigint,
     primary key (card_id)
) engine=InnoDB

Hibernate:
create table hibernate_sequence
(
    next_val bigint
) engine=InnoDB

Hibernate:
insert into hibernate_sequence values ( 1 )

Hibernate:
create table letter
(
    letter_id bigint not null auto_increment,
    date varchar(255),
    description varchar(2048),
    is_processed bit,
    type_question integer,
    user_user_id bigint,
    primary key (letter_id)
) engine=InnoDB

Hibernate:
create table log_entry
(
    log_entry_id bigint not null auto_increment,
    date varchar(255),
    description varchar(255),
    user_user_id bigint,
    primary key (log_entry_id)
) engine=InnoDB

Hibernate:
create table payment
(
    payment_id bigint not null auto_increment,
    appointment varchar(255),
    date varchar(255),
    exchange_rate decimal(19,2),
    is_outgoing bit,
    new_balance decimal(19,2),
    recipient_amount decimal(19,2),
    recipient_currency varchar(255),
    recipient_number varchar(255),
    sender_amount decimal(19,2),
    sender_currency varchar(255),
    sender_number varchar(255),
    status bit,
    user_id bigint,
    account_account_id bigint,
    primary key (payment_id)
) engine=InnoDB

Hibernate:
create table role
(
    id bigint not null,
    name varchar(255),
    primary key (id)
) engine=InnoDB

Hibernate:
create table user
(
    user_id bigint not null auto_increment,
    activation_code varchar(255),
    active bit, email varchar(255),
    email_verified bit,
    username varchar(30),
    name varchar(30),
    password varchar(100),
    phone varchar(30),
    registration_date varchar(255),
    surname varchar(30),
    role_id bigint,
    primary key (user_id)
) engine=InnoD

Hibernate:
alter table account
    add constraint FKjiqrvmmf18phbftjiwc1nsobl foreign key (user_user_id) references user (user_id)

Hibernate:
alter table bank_card
    add constraint FK6j7048q6auijf41vans0lj5qw foreign key (account_account_id) references account (account_id)

Hibernate:
alter table letter
    add constraint FK3dvaokahvyovfcfg1biemguwd foreign key (user_user_id) references user (user_id)

Hibernate:
alter table log_entry
    add constraint FKtf9sags1uipf70tqyq3knvh3a foreign key (user_user_id) references user (user_id)

Hibernate:
alter table payment
    add constraint FK9ntaamy420q595njxwhin8o70 foreign key (account_account_id) references account (account_id)

Hibernate:
alter table user
    add constraint FKn82ha3ccdebhokx3a8fgdqeyy foreign key (role_id) references role (id)