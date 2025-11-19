<p align="center">
  <img src="../shared/resources/src/commonMain/composeResources/drawable/app_icon.png"
       alt="Gondi Banner"
       width="180" />
</p>

<h1 align="center">Supabase Queries</h1>

Welcome back, traveller. Having conquered authentication, you now tread into the lands of **Supabase Tables** — where schemas are forged and triggers breathe life into your backend.

The profile table and its guardians (a.k.a. triggers and policies) form the backbone of your player data. Below are the exact incantations I used — add them to your **SQL Editor** in Supabase to bring your backend to life.

---

### 🧱 `Avatar` Enum

The avatars in Gondi hail from the good folks at [Dicebear.com](https://dicebear.com).

```sql
-- Create enum type for avatars
create type avatar as enum (
  'aidan',
  'adrian',
  'amaya',
  'christian',
  'george',
  'jocelyn',
  'katherine',
  'leo',
  'nolan',
  'ryker',
  'sawyer'
);
```

---

### 🎨 `Avatar Background` Enum

Because even heroes deserve a good backdrop.

```sql
-- Create enum type for avatar backgrounds
create type avatar_background as enum (
  'green_emerald',
  'green_minty',
  'green_neon',
  'green_leafy',
  'yellow_bright',
  'yellow_sunny',
  'yellow_banana',
  'yellow_golden',
  'purple_electric',
  'purple_orchid',
  'purple_lilac',
  'purple_amethyst',
  'cyan_bright',
  'pink_hot',
  'orange_coral'
);
```

---

### 👤 `Profile` Table

This is where player identities dwell — each one bound to its auth user.

```sql
-- Create profiles table
create table public.profiles (
  id uuid not null,
  username text null,
  avatar public.avatar null,
  avatar_background public.avatar_background null default 'green_emerald'::avatar_background,
  created_at timestamp with time zone null default now(),
  updated_at timestamp with time zone null default now(),
  constraint profiles_pkey primary key (id),
  constraint profiles_username_key unique (username),
  constraint profiles_id_fkey foreign KEY (id) references auth.users (id) on delete CASCADE
);

-- Enable RLS
alter table public.profiles enable row level security;

-- Allow anyone to read profiles
create policy "Anyone can read profiles"
  on public.profiles
  for select
  using (true);

-- Allow users to update their own profile
create policy "Users can update their own profile"
  on public.profiles
  for update
  using (auth.uid() = id);

-- Allow users to insert their own profile
create policy "Users can insert their own profile"
  on public.profiles
  for insert
  with check (auth.uid() = id);

-- Ensure updated_at updates automatically
create extension if not exists moddatetime schema extensions;

create trigger update_profiles_updated_at
before update on public.profiles
for each row
execute procedure moddatetime(updated_at);

-- Auto-create profile when new user signs up
create or replace function public.handle_new_user()
returns trigger as $$
begin
  insert into public.profiles (id, username)
  values (
    new.id,
    split_part(new.email, '@', 1) -- take part before '@'
  )
  on conflict (id) do nothing;

  return new;
end;
$$ language plpgsql security definer;

create trigger on_auth_user_created
after insert on auth.users
for each row
execute procedure public.handle_new_user();
```

---

And there you have it — your **Supabase backend** now hums quietly, keeping watch over every player who enters the realm.

If you’ve followed these steps, your fork now stands tall with a living, breathing data layer. ⚔️
