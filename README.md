# apple-maps-server-kotlin

Lightweight SDK written in Kotlin for the Apple Maps Server API to use with Kotlin or Java.

## Features

We support the following APIs by Apple Maps SDK. Each API supports the full set of parameters.

- ✅ Geocoding - [see Apple Maps API Description](https://developer.apple.com/documentation/applemapsserverapi/geocode_an_address)
- ✅ Reverse Geocoding - [see Apple Maps API Description](https://developer.apple.com/documentation/applemapsserverapi/reverse_geocode_a_location)

## Installation

### Gradle

```groovy
dependencies {
    implementation("com.doorbit:apple-maps-server-kotlin:0.2.0")
}
```

### Maven

```xml
<dependency>
  <groupId>com.doorbit</groupId>
  <artifactId>apple-maps-server-kotlin</artifactId>
  <version>0.2.0</version>
</dependency>
```

## Usage

**Simple example**

```kotlin
import com.doorbit.applemaps.AppleMaps
import com.doorbit.applemaps.Place

val api = AppleMaps(authToken = "your-apple-maps-auth-token")

// Geocode an address to get a coordinate and a fully structured address
val places : List<Place> = api.geocode("Jungfernstieg 1, 20354 Hamburg")
println(places)
```

The above example's result is equivalent to this JSON:

```json
{
  "results": [
    {
      "coordinate": {
        "latitude": 53.551666,
        "longitude": 9.9942163
      },
      "displayMapRegion": {
        "southLatitude": 53.5471744235794,
        "westLongitude": 9.986655966708607,
        "northLatitude": 53.556157576420595,
        "eastLongitude": 10.001776633291392
      },
      "name": "Jungfernstieg 1",
      "formattedAddressLines": [
        "Jungfernstieg 1",
        "20095 Hamburg",
        "Germany"
      ],
      "structuredAddress": {
        "locality": "Hamburg",
        "postCode": "20095",
        "subLocality": "Hamburg-Mitte",
        "thoroughfare": "Jungfernstieg",
        "subThoroughfare": "1",
        "fullThoroughfare": "Jungfernstieg 1",
        "dependentLocalities": [
          "City Centre",
          "Hamburg-Altstadt"
        ]
      },
      "country": "Germany",
      "countryCode": "DE"
    }
  ]
}    
```

**Advanced example**

```kotlin
val api = AppleMaps(authToken = "your-apple-maps-auth-token")

/**
 * In this example we are searching only for the street address "Jungfernstieg 1".
 * Apple provides a parameter called "searchLocation" which acts as a hint for the geocoder where to search.
 * In this case, we are providing coordinates of somewhere in Hamburg, Germany.
 * This is useful if your users only type in a street address without a city or country and you will be able to 
 * already autocomplete the address based on the users location.
 */
val input = GeocodeInput(
    address = "Jungfernstieg 1",
    userLocation = LatLon(latitude = 53.57, longitude = 10.00)
)

api.geocode(input).let(::println)
```

### Authorization

You need to provide an authorization token to use the Apple Maps Server API.
Your authorization token is then exchanged for a short-lived access token. This SDK handles this for you and also caches and refreshes it automatically.

To obtain the authorization token, first you have to create a Maps ID and private key through your Apple Developer Account.
Click [here](https://developer.apple.com/documentation/mapkitjs/creating_a_maps_identifier_and_a_private_key) for a tutorial by Apple.
Once you got your Maps ID and downloaded your private key, you can use the Token Generator provided by Apple to generate your authorization token: https://maps.developer.apple.com/token-maker

### Request quota to Apple Maps Geocoding API

Apple provides a free daily limit of 250,000 map views and 25,000 service calls per Apple Developer Program membership.
You can review your current spend on the Apple Maps dashboard: https://maps.developer.apple.com/
