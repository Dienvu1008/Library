package com.dienvu.commons.views.bottomactionmenu

interface BottomActionMenuCallback {
    fun onItemClicked(item: BottomActionMenuItem) {}
    fun onViewCreated(view: BottomActionMenuView) {}
    fun onViewDestroyed() {}
}
