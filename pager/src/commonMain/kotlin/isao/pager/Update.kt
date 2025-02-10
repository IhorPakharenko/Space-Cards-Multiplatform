package isao.pager

internal inline fun <reified T, reified R : T> List<T>.updateOrNull(
  old: R,
  new: (R) -> T,
): List<T>? = update(indexOf(old).also { if (it == -1) return null }, new)

internal inline fun <reified T, reified R : T> List<T>.update(
  old: R,
  new: (R) -> T,
): List<T> = update(indexOf(old), new)

internal inline fun <reified T, reified R : T> List<T>.updateFirstOrNull(
  predicate: (T) -> Boolean,
  new: (R) -> T,
): List<T>? = update(indexOfFirst(predicate).also { if (it == -1) return null }, new)

internal inline fun <reified T, reified R : T> List<T>.update(
  index: Int,
  new: (R) -> T,
): List<T> = toMutableList().apply {
  set(index, new(get(index) as R))
}
