package radesh.twosidechooser

interface OnSwipeEndListener {
    abstract fun onAccept() // when swipe to right
    abstract fun onIgnore() // when swipe to left
}