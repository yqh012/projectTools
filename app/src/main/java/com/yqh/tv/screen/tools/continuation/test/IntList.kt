package com.yqh.tv.screen.tools.continuation.test

sealed class IntList {
    object Nil : IntList() {
        override fun toString(): String {
            return "Nil"
        }
    }

    data class Con(val head: Int, val tail: IntList) : IntList() {
        override fun toString(): String {
            return "$head,${tail}"
        }
    }
}

fun arrayIntList(vararg args: Int): IntList {
    return when (args.size) {
        0 -> IntList.Nil
        else -> IntList.Con(args[0], arrayIntList(*(args.slice(1 until args.size).toIntArray())))
    }
}

fun IntList.add(value: Int): IntList {
    return IntList.Con(value, this)
}

fun IntList.forEach(block: (Int) -> Unit) {
    when (this) {
        IntList.Nil -> Unit
        is IntList.Con -> {
            block(head)
            tail.forEach(block)
        }
    }
}

fun IntList.remove(value: Int): IntList {
    return when (this) {
        IntList.Nil -> this
        is IntList.Con -> {
            if (head == value) {
                tail
            } else {
                tail.remove(value)
            }
        }
    }
}

fun main() {
    val list = arrayIntList(0, 1, 2, 3, 4)
    println(list.toString())
//    val addList = list.add(5)
//    println(addList.toString())
//
//    addList.forEach {
//        println(it)
//    }

    val removelist = list.remove(2)
    removelist.forEach {
        println(it)
    }

}