# Sprint 1

### **Technical Update for the Fitness App**

### **a. Final List of APIs**

1. **Authentication & User Management:**
    - **Firebase Authentication**: For user sign-in via email/password, [Google](https://developers.google.com/identity/android-credential-manager), or other OAuth providers.
    - **Credential Manager:** https://developer.android.com/identity/sign-in/credential-manager
2. **Fitness Tracking:**
    - [**Health Connect API**:](https://developer.android.com/health-and-fitness/guides/health-connect/plan/data-types) Fetch data for steps, calories burned, distance traveled, and workout activities.
    - **Device Sensors API**: Access data directly for fitness tracking if Google Fit is unavailable.
3. **Real-Time Data Sync & Cloud Storage:**
    - **Firebase Firestore**: For syncing and storing user stats, leaderboard data, challenges, and activity logs.
4. **Push Notifications:**
    - **Firebase Cloud Messaging (FCM)**: To send real-time notifications for challenges, milestones, and updates.
5. **Location-Based Features:**
    - **Google Maps API**: For geolocation services to support nearby leaderboards and location-specific challenges.
    - **Geofencing API**: To define specific zones for group competitions.
6. **Analytics:**
    - **Firebase Analytics**: For tracking user interactions, feature usage, and app performance.
7. **Background Tasks:**
    - **WorkManager**: To sync offline data and schedule recurring tasks (e.g., leaderboard refresh, notifications).

---

### **b. On-Device Sensors**

1. **Accelerometer:**
    - To detect movement and estimate steps if Google Fit data is unavailable.
2. **GPS:**
    - For tracking distance covered during outdoor activities like walking, running, or cycling.
    - Used for geolocation-based leaderboard and challenges.
3. **Gyroscope:**
    - To enhance motion detection and provide a richer activity-tracking experience.

---

### **c. Database Schema**

### **Firestore Collections and Documents**

1. **Users:**
    - **Document ID:** `userId`
    - **Fields:**
        - `name`: String
        - `email`: String
        - `profilePicture`: String (URL)
        - `totalPoints`: Integer
        - `level`: Integer
        - `achievements`: Array (List of badge IDs)
        - `lastSyncDate`: Timestamp
2. **Activities:**
    - **Document ID:** Auto-generated
    - **Fields:**
        - `userId`: String (Reference to Users collection)
        - `activityType`: String (e.g., steps, running, cycling)
        - `duration`: Integer (in seconds)
        - `caloriesBurned`: Float
        - `distance`: Float (in kilometers)
        - `date`: Timestamp
3. **Leaderboards:**
    - **Document ID:** `leaderboardId`
    - **Fields:**
        - `name`: String (e.g., "Weekly Leaderboard")
        - `type`: String (e.g., global, friends, nearby)
        - `startDate`: Timestamp
        - `endDate`: Timestamp
        - `entries`: Array (List of objects containing `userId`, `points`, and `rank`)
4. **Challenges:**
    - **Document ID:** `challengeId`
    - **Fields:**
        - `name`: String
        - `description`: String
        - `startDate`: Timestamp
        - `endDate`: Timestamp
        - `creatorId`: String (userId of creator)
        - `participants`: Array (List of user IDs)
        - `type`: String (e.g., steps, calories burned)
        - `target`: Integer (e.g., steps to complete)
        - `status`: String (ongoing/completed)
5. **Badges:**
    - **Document ID:** `badgeId`
    - **Fields:**
        - `name`: String
        - `description`: String
        - `image`: String (URL for badge icon)
        - `criteria`: Object (e.g., `{type: "steps", target: 10000}`)

---

### **d. External Libraries**

1. **UI and UX:**
    - **Jetpack Compose:** For building a modern, reactive UI.
    - **Accompanist:** For animations, insets, and UI utilities in Jetpack Compose.
2. **Networking:**
    - **Retrofit:** For API calls (if integrating third-party APIs or custom backend).
    - **OkHttp:** For efficient HTTP requests and connection management.
3. **Local Storage:**
    - **Room Database:** For offline caching of activity logs and user data.
4. **Fitness and Location:**
    - **Health Connect SDK:** To integrate fitness data.
    - **Google Play Services Location:** For geolocation and geofencing features.
5. **Background Processing:**
    - **WorkManager:** For syncing offline data and scheduling background tasks.
6. **Push Notifications:**
    - **Firebase Messaging:** For sending alerts and reminders.
7. **Image Handling:**
    - **Glide or Coil:** For efficient image loading and caching (e.g., profile pictures, badge icons).

### Next Steps

- API calling
- UI