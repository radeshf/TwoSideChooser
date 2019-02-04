package radesh.twosidechooser

interface onSwipeEndListener {
    abstract fun onAccept() // when swipe to right
    abstract fun onIgnore() // when swipe to left
}