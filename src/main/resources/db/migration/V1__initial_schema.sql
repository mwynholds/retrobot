create table users (
  id uuid primary key,
  email varchar(255) not null,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

create table cards (
  id uuid primary key,
  body text,
  creator uuid not null references users(id),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

create table authors (
  card_id uuid not null references cards(id),
  user_id uuid not null references users(id),
  primary key (card_id, user_id)
);