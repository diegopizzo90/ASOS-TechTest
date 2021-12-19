# ASOS-TechTest

Android application that shows information about a SpaceX launches, rockets and company details

**Architecture:**
This project is based on MVVM pattern and written with Kotlin.

**Possible improvements:**
Add pagination logic for "/launches" endpoint

**This project requires Java 11**

# Third-party libraries

**Koin**  
Koin is a framework for the dependency injection.\
Link: https://github.com/InsertKoinIO/koin

**Retrofit**  
HTTP client used the make API Requests and retrieve the data from network.\
Link: https://square.github.io/retrofit/

**RxJava**  
Used to perform asynchronous and event-based tasks.\
Link: https://github.com/ReactiveX/RxJava

**Glide**
Image loader for Android.\
Link: https://github.com/bumptech/glide

**MockWebServer**\
Library useful to make it easy testing API calls and verify the expected results from requests.\
Link: https://github.com/square/okhttp/tree/master/mockwebserver

**Mockito**\
Mocking framework for Android\
Link: https://github.com/mockito/mockito

**dropbox/Store**\
Store is a Kotlin library for loading data from remote and local sources. It simplifies fetching and
storage data in android application. It implements a Repository pattern and expose an API built with
Coroutines but it also provides a converter for RxJava emitters.\
Link: https://github.com/dropbox/Store

**ThreeTenABP**\
Library for using java.time* package with sdk<26.\
Link: https://github.com/JakeWharton/ThreeTenABP

**MonthAndYearPicker**\
Library that allows users to open a date picker customizable with only month and year or only month
or only year as required. Used into filter dialog screen to pick the range of years desired. section
of year.\
Link: https://github.com/premkumarroyal/MonthAndYearPicker

**MultiStateToggleButton**\
Library useful to display a simple multi-state toggle button for Android. Used into filter dialog
screen to select filter related to the outcome of the launches or to handle the sorting.\
Link: https://github.com/jlhonora/multistatetogglebutton
