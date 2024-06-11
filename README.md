FlickR - Daily Interesting Images Viewer

FlickR is an Android application designed to fetch and display daily interesting images from the Flickr API.
It provides users with a seamless experience to discover captivating images, 
featuring pagination for browsing convenience, robust search functionality, and an intuitive image viewer with pinch-to-zoom capability.
Additionally, FlickR employs background processing to notify users of newly available interesting images,
ensuring they never miss out on fresh content.

Features

Daily Interesting Images: Fetches and displays daily interesting images from the Flickr API.
Pagination: Allows users to navigate through multiple pages of search results effortlessly.
Search Functionality: Enables users to search for specific images based on keywords.
Image Viewer: Provides a user-friendly interface for viewing images with pinch-to-zoom functionality.
Background Work: Utilizes background processing to notify users of new interesting images available for viewing.

Architecture

FlickR is built using the MVVM (Model-View-ViewModel) clean architecture pattern, ensuring a maintainable and scalable codebase.
This architecture separates the application into three layers:

Model: Represents the data and business logic of the application.
View: Displays the UI elements and interacts with the user.
ViewModel: Acts as a mediator between the Model and the View, handling the presentation logic and maintaining the UI-related data.

Technologies Used

Kotlin: FlickR is developed using the Kotlin programming language, leveraging its concise syntax and powerful features for Android app development.
Android Architecture Components: Utilizes LiveData and ViewModel from the Android Architecture Components to build a robust MVVM architecture.
Retrofit: Integrates Retrofit for efficient networking and API communication.
Glide: Incorporates Glide for seamless image loading and caching.
Coroutines: Implements Coroutines for asynchronous and non-blocking programming, enhancing app responsiveness.
Flickr API: Interacts with the Flickr API to fetch daily interesting images and perform search operations.

License

This project is licensed under the MIT License - see the LICENSE file for details.

Acknowledgments

Special thanks to the creators and maintainers of the Flickr API for providing access to a vast collection of interesting images.
Inspired by the clean architecture principles advocated by Robert C. Martin (Uncle Bob) and the MVVM architecture pattern for Android development.

App screenshots

![flikcr1](https://github.com/EngFred/flickr-app/assets/136785545/7fdaab32-b39c-4b90-b536-4da7e2a0560a)
![flikcr2](https://github.com/EngFred/flickr-app/assets/136785545/d6afb285-77ef-4c94-9367-575a08145b54)
![flikcr3](https://github.com/EngFred/flickr-app/assets/136785545/2fc9957d-b2f6-4598-8810-ffe73acad207)
