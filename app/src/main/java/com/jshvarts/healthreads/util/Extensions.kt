package com.jshvarts.healthreads.util

/**
 * Helper to force a when statement to assert all options are matched in a when statement.
 *
 * By default, Kotlin doesn't care if all branches are handled in a when statement. However, if you
 * use the when statement as an expression (with a value) it will force all cases to be handled.
 *
 * This extension will force all cases to be handled in a when statement as well.
 *
 * Usage:
 *
 * when(sealedObject) {
 *     is OneType -> ...
 *     is AnotherType -> ...
 * }.exhaustive
 */
val <T> T.exhaustive: T
  get() = this