## Third Party Libraries:
- com.squareup.retrofit2:retrofit
- com.squareup.retrofit2:converter-gson

## AndroidX Libraries:
- androidx.navigation:navigation-fragment-ktx
- androidx.navigation:navigation-ui-ktx
- androidx.activity:activity-ktx
- androidx.lifecycle:lifecycle-viewmodel-ktx
- androidx.lifecycle:lifecycle-runtime-ktx
- androidx.swiperefreshlayout:swiperefreshlayout
- androidx.fragment:fragment-ktx
- androidx.constraintlayout:constraintlayout
- androidx.core:core-ktx
- androidx.lifecycle:lifecycle-runtime-ktx

## Google Library:
- com.google.android.gms:play-services-maps

## Notes:
When separating the trips by day I noticed some trips were started the day prior and then
completed the following day. For these trips I opted to key the day as the day that the ride
was completed, but can easily switch it to the prior day in the mapper class.

## Improvements:
Improve repository caching mechanisms to save to disk, and be smarter about retrieving from 
disk vs keep in memory, and when to retrieve from network vs from cache. 

Add Hilt to the project for dependency injection.
It isn't necessary  for a small project like this, but would be useful as the project grows.

Add tests, since this isn't added to CI I didn't think it would be very beneficial to add unit tests,
but as the project grows it will be beneficial.

## APK: 
https://drive.google.com/file/d/1Ug6txdta8tsVDW9ufkvwDh6HEGhJeV0A/view?usp=sharing
