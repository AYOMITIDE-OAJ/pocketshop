<h1 align="center"> PocketShop </h1>

An E-commerce application store front for users to have a unique shopping experience

<P align="center">
  <img src="/app/screenshot/pocketshop_home.png"/>
</p>


Features Include

* Product Catalogue
* Powered Search Feature
* Filter options
* Account Creation
* Profile Customization
* Payment Gateway Methods
* Shipment Tracking 

## Download
Go to the [Releases](https://github.com/AYOMITIDE-OAJ/pocketshop/releases) to download the latest APK

## Tech stack & Open-source libraries
- Minimum SDK level 21
- [Kotlin](https://kotlinlang.org/) based, [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) + [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/) for asynchronous.
- Jetpack
  - Lifecycle: Observe Android lifecycles and handle UI states upon the lifecycle changes.
  - ViewModel: Manages UI-related data holder and lifecycle aware. Allows data to survive configuration changes such as screen rotations.
  - DataBinding: Binds UI components in your layouts to data sources in your app using a declarative format rather than programmatically.
  - Room: Constructs Database by providing an abstraction layer over SQLite to allow fluent database access.
  - [Hilt](https://dagger.dev/hilt/): for dependency injection.
- Architecture
  - MVVM Architecture (View - DataBinding - ViewModel - Model)
  - [Bindables](https://github.com/skydoves/bindables): Android DataBinding kit for notifying data changes to UI layers.
  - Repository Pattern
- [Retrofit2 & OkHttp3](https://github.com/square/retrofit): Construct the REST APIs and paging network data.
- [Moshi](https://github.com/square/moshi/): A modern JSON library for Kotlin and Java.
- [Bundler](https://github.com/skydoves/bundler): Android Intent & Bundle extensions, which insert and retrieve values elegantly.
- [ksp](https://github.com/google/ksp): Kotlin Symbol Processing API.
- [Turbine](https://github.com/cashapp/turbine): A small testing library for kotlinx.coroutines Flow.
- [Material-Components](https://github.com/material-components/material-components-android): Material design components for building ripple animation, and CardView.
- [Glide](https://github.com/bumptech/glide), [GlidePalette](https://github.com/florent37/GlidePalette): Loading images from network.
- [Timber](https://github.com/JakeWharton/timber): A logger with a small, extensible API.

## Screenshot

<table>
  <tr>
    <td>
      <img src="https://github.com/AYOMITIDE-OAJ/pocketshop/blob/main/app/screenshot/Screenshot_20230321_143228.png" width="250" height="500"/>  
    </td>
    <td>
      <img src="https://github.com/AYOMITIDE-OAJ/pocketshop/blob/main/app/screenshot/Screenshot_20230321_143316.png" width="250" height="500"/>  
    </td>
    <td>
      <img src="https://github.com/AYOMITIDE-OAJ/pocketshop/blob/main/app/screenshot/Screenshot_20230321_143335.png" width="250" height="500"/>  
    </td>
  </tr>
  <tr>
    <td>
      <img src="https://github.com/AYOMITIDE-OAJ/pocketshop/blob/main/app/screenshot/7.png" width="250" height="500"/>  
    </td>
     <td>
      <img src="https://github.com/AYOMITIDE-OAJ/pocketshop/blob/main/app/screenshot/8.png" width="250" height="500"/>  
    </td>
    <td>
      <img src="https://github.com/AYOMITIDE-OAJ/pocketshop/blob/main/app/screenshot/9.png" width="250" height="500"/>  
     </td> 
   </tr>
   <tr>
     <td>
      <img src="https://github.com/AYOMITIDE-OAJ/pocketshop/blob/main/app/screenshot/Screenshot_1679413151.png" width="250" height="500"/>
    </td>
    <td>
      <img src="https://github.com/AYOMITIDE-OAJ/pocketshop/blob/main/app/screenshot/Screenshot_1679413162.png" width="250" height="500"/>  
     </td> 
      <td>
      <img src="https://github.com/AYOMITIDE-OAJ/pocketshop/blob/main/app/screenshot/Screenshot_1679413199.png" width="250" height="500"/>  
     </td> 
   </tr>
  
</table>

## MAD Score
![summary](https://user-images.githubusercontent.com/24237865/102366914-84f6b000-3ffc-11eb-8d49-b20694239782.png)
![kotlin](https://user-images.githubusercontent.com/24237865/102366932-8a53fa80-3ffc-11eb-8131-fd6745a6f079.png)

# License
```xml
Designed and developed by 2023 OAJSTUDIOS (Ayomitide OAJ)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.




