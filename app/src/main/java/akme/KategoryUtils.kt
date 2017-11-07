package akme

import kategory.*

fun <A> A?.toOption(): Option<A> = if (this != null) Option.Some(this) else Option.None

fun <A> ListKW<A>.headOption(): Option<A> = this.firstOrNull().toOption()

fun <A> ListKW<A>.lastOption(): Option<A> = this.lastOrNull().toOption()