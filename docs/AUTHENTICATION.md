<p align="center">
  <img src="../shared/resources/src/commonMain/composeResources/drawable/app_icon.png"
       alt="Gondi Banner"
       width="180" />
</p>

<h1 align="center">Set up authentication</h1>

The game uses Supabase for profile management and high score tracking (at least at the time of writing this, it intends to). 

## 1. Set up supabase
The first step is creating your Supabase project, copying your SUPABASE_URL and ANON_KEY and addinging them to your `local.properties` in your forked project

```
SUPABASE_KEY=ey*******
SUPABASE_URL=https://*****.supabase.co
```

## 2. Google auth
Then you would want to go to create a `web client` for Google Sign-in

Copy your Web ID and client secret to your supabase provider

<p align="center">
  <img src="assets/supabase_provider.png"
       alt="Client secrets"/>
</p>

## 3. Copy the callback url
Copy the callback url in the screenshot above and add it to your Google cloud web client here. See the first field

<p align="center">
  <img src="assets/gcp_redirect.png"
       alt="Callback url"/>
</p>

## 4. Copy the redirect url
On successful authentication, add the app's intent urls i.e `localhost` for PC and `gondi auth` for android 

