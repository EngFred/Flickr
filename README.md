<h1>FlickR - Daily Interesting Images Viewer</h1>
    <p>FlickR is an Android application designed to fetch and display daily interesting images from the Flickr API. It provides users with a seamless experience to discover captivating images, featuring pagination for browsing convenience, robust search functionality, and an intuitive image viewer with pinch-to-zoom capability. Additionally, FlickR employs background processing to notify users of newly available interesting images, ensuring they never miss out on fresh content.</p>

    <h2>Features</h2>
    <ul>
        <li><strong>Daily Interesting Images:</strong> Fetches and displays daily interesting images from the Flickr API.</li>
        <li><strong>Pagination:</strong> Allows users to navigate through multiple pages of search results effortlessly.</li>
        <li><strong>Search Functionality:</strong> Enables users to search for specific images based on keywords.</li>
        <li><strong>Image Viewer:</strong> Provides a user-friendly interface for viewing images with pinch-to-zoom functionality.</li>
        <li><strong>Background Work:</strong> Utilizes background processing to notify users of new interesting images available for viewing.</li>
    </ul>

    <h2>Architecture</h2>
    <p>FlickR is built using the MVVM (Model-View-ViewModel) clean architecture pattern, ensuring a maintainable and scalable codebase. This architecture separates the application into three layers:</p>
    <ul>
        <li><strong>Model:</strong> Represents the data and business logic of the application.</li>
        <li><strong>View:</strong> Displays the UI elements and interacts with the user.</li>
        <li><strong>ViewModel:</strong> Acts as a mediator between the Model and the View, handling the presentation logic and maintaining the UI-related data.</li>
    </ul>

    <h2>Technologies Used</h2>
    <ul>
        <li><strong>Kotlin:</strong> FlickR is developed using the Kotlin programming language, leveraging its concise syntax and powerful features for Android app development.</li>
        <li><strong>Android Architecture Components:</strong> Utilizes LiveData and ViewModel from the Android Architecture Components to build a robust MVVM architecture.</li>
        <li><strong>Retrofit:</strong> Integrates Retrofit for efficient networking and API communication.</li>
        <li><strong>Glide:</strong> Incorporates Glide for seamless image loading and caching.</li>
        <li><strong>Coroutines:</strong> Implements Coroutines for asynchronous and non-blocking programming, enhancing app responsiveness.</li>
        <li><strong>Flickr API:</strong> Interacts with the Flickr API to fetch daily interesting images and perform search operations.</li>
    </ul>

    <h2>License</h2>
    <p>This project is licensed under the MIT License - see the <a href="LICENSE">LICENSE</a> file for details.</p>

    <h2>Acknowledgments</h2>
    <p>Special thanks to the creators and maintainers of the Flickr API for providing access to a vast collection of interesting images.</p>
    <p>Inspired by the clean architecture principles advocated by Robert C. Martin (Uncle Bob) and the MVVM architecture pattern for Android development.</p>

App screenshots

![flikcr1](https://github.com/EngFred/flickr-app/assets/136785545/7fdaab32-b39c-4b90-b536-4da7e2a0560a)
![flikcr2](https://github.com/EngFred/flickr-app/assets/136785545/d6afb285-77ef-4c94-9367-575a08145b54)
![flikcr3](https://github.com/EngFred/flickr-app/assets/136785545/2fc9957d-b2f6-4598-8810-ffe73acad207)
