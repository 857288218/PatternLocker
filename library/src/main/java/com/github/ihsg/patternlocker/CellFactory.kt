package com.github.ihsg.patternlocker

internal class CellFactory(private val width: Int, private val height: Int) {
    val cellBeanList: List<CellBean> by lazy {
        val result = ArrayList<CellBean>()
        result.clear()

        //cell半径和cell之间的距离相等
        val pWidth = this.width / 8f
        val pHeight = this.height / 8f

        for (i: Int in 0..2) {
            for (j: Int in 0..2) {
                val id = (i * 3 + j) % 9
                val x = (j * 3 + 1) * pWidth
                val y = (i * 3 + 1) * pHeight
                result.add(CellBean(id, x, y, pWidth))
            }
        }
        Logger.d("CellFactory", "result = $result")
        return@lazy result
    }
}