# Libra Java Library
---

This is the library that provides Common graphics, OpenGL and utility stuff used in the EtCeterum [Photo Album Live Wallpaper](https://play.google.com/store/apps/details?id=etceterum.android.lwp.photoalbum),
which I created in 2011 with 3 goals in mind:

1. Learn about Android development
2. Learn OpenGL ES
3. Study various image filtering algorithms

The library deliberately ignores all Android-provided raster image-related stuff (the Bitmap class and such) because:

* I am a portability freak (The wallpaper actually exists in 2 flavors: Android and desktop), and 
* I wanted to do all the low-level stuff myself, in order for me to do [2] and [3] above. 

The most interesting parts probably are:
* Pure java 9-patch compiler
* Image class as a wrapper around an RGBA buffer
* Collection of image operations (filters) (under the image.op namespace)
* K-means clusterer for advanced filtering ~~(only mentioning this to show off)~~
