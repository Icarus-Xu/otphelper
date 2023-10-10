# COPY SMS CODE | OTP Helper | کپی رمز پیامک

An open-source application that allows you to copy OTP and codes from SMS and notifications automatically by reading all of your notifications.

The application works completely offline and without internet permission. So you can be rest assured that your data does not leave your device.

[<img src="https://fdroid.gitlab.io/artwork/badge/get-it-on.png"
     alt="Get it on F-Droid"
     height="80">](https://f-droid.org/packages/io.github.jd1378.otphelper/)
[<img src="https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png"
     alt='Get it on Google Play'
     height="80">](https://play.google.com/store/apps/details?id=io.github.jd1378.otphelper)
[<img src="https://raw.githubusercontent.com/jd1378/otphelper/main/cafebazaar.png"
     alt='Get it on Bazaar'
     height="80">](https://cafebazaar.ir/app/io.github.jd1378.otphelper)

or get the APK from [Latest Release](https://github.com/jd1378/otphelper/releases/latest).

## How it works

The app setups a [notification listener](https://github.com/jd1378/otphelper/blob/main/app/src/main/java/io/github/jd1378/otphelper/NotificationListener.kt) and reads all notifications that is sent by any apps. So the name of the app is a bit inaccurate. This allows the app to detect codes also from emails and possibly any other app that sends notification.
when a notification is sent, the app creates a single string from all text in the notification, then [checks if it should be ignored](https://github.com/jd1378/otphelper/blob/main/app/src/main/java/io/github/jd1378/otphelper/utils/CodeIgnore.kt). If It's not ignored, then It's matched against the [code detection regex](https://github.com/jd1378/otphelper/blob/main/app/src/main/java/io/github/jd1378/otphelper/utils/CodeExtractor.kt). Then app will take the extracted code and behave according to settings.

## How to build

Simply run:

```bash
./gradlew :app:assembleRelease
```

The apk should be available in `apps/build/outputs/apk/release/` directory.

## Credits

feature graphic image generated by [hotpot.ai](https://hotpot.ai/templates/google-play-feature-graphic)

### Translators

German:

- [@Dacid99](https://github.com/Dacid99)

Spanish:

- [@nilp0inter](https://github.com/nilp0inter)

Turksih:

- [@SirCrownguard](https://github.com/SirCrownguard)
