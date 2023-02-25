RtlViewPager changelog
======================

2.0.0
-----
* Migrate to AndroidX
* Add @NonNull annotations
* Update target sdk version to 29.
* Update gradle, plugins, libraries, and build tools.

1.0.3
-----
* Fix bugs in `removeOnPageChangeListener` and `clearOnPageChangeListeners`.

1.0.2
-----
* Fix a bug in onRestoreInstanceState where page change listeners would be called with the wrong position when in RTL mode.
* Update target sdk version to 25.
* Update gradle, plugins, libraries, and build tools.

1.0.1
-----
* Fix a bug in propagation of data set changed notifications.

1.0.0
-----
* First release!
