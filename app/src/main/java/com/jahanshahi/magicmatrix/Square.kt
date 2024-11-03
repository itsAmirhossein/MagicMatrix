package com.jahanshahi.magicmatrix

data class Square(
    val initX: Double,
    val initY: Double,
    var x: Double,
    var y: Double,
    var vX: Double = 0.0,
    var vY: Double = 0.0
) {
    override fun equals(other: Any?): Boolean {
        return this.initX == (other as Square).initX && this.initY == other.initY && this.x == other.x && this.y == other.y && this.vX == other.vX && this.vY == other.vY
    }

    override fun hashCode(): Int {
        var result = initX.hashCode()
        result = 31 * result + initY.hashCode()
        result = 31 * result + x.hashCode()
        result = 31 * result + y.hashCode()
        result = 31 * result + vX.hashCode()
        result = 31 * result + vY.hashCode()
        return result
    }

}

