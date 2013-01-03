# IconButton for Android

IconButton is an Android button widget that allows you to center both the button's text and an icon.

Android's stock `Button` class allows you to assign a `Drawable` to the left, right, top, or bottom of a button, but it looks bad if you need to have the button fill the screen:

![Default Android Button Behavior](https://github.com/pnc/IconButton/raw/master/IconButtonDemo/default_behavior.png "Default Android Button Behavior")

The component is a [Library Project](http://developer.android.com/guide/developing/eclipse-adt.html#libraryProject). This means that there's no need to copy-paste resources into your own project, simply add the cloned repository as a library reference.

## Installation

If you're using ADT, clone this repository and import it into your workspace using File - Import. Then add it to your project by right-clicking your project, selecting Properties, then Android, and adding IconButton in the references list at the bottom of the Properties window.

If you're not using ADT, clone this repository and add the following to your own `project.properties`:

    android.library.reference.1=../IconButton

where `../IconButton` is the relative path to the cloned repository. If you already have library references, change the `1` to the appropriate number.

## When to use
Use the IconButton class if you want finer control over a button that has both a drawable and text.

## Usage

### In your layout

    <com.phillipcalvin.iconbutton.IconButton
            android:id="@+id/search"
            android:drawableLeft="@drawable/action_search"
            android:text="@string/search"
            app:iconPadding="10dp" />

The use of `app:iconPadding` is optional. It allows you to add padding between the drawable and your text.

You can assign either a `drawableLeft` or a `drawableRight` to the IconButton.

<img src="https://github.com/pnc/IconButton/raw/master/IconButtonDemo/demo.png" width="360" alt="Preview of icon button in several different configurations">

### Caveats

IconButton only supports one drawable on the left or right. I'll absolutely accept patches that improve its handling of multiple drawables.

## License
Copyright (c) 2012 [Phil Calvin](http://philcalvin.com)

Licensed under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)

Contains modified source from [ParcelHelper](https://github.com/commonsguy/cwac-parcel), also under the Apache License.

