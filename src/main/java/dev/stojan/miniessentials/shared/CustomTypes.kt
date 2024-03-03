package dev.stojan.miniessentials.shared

class Coordinates(val x: Double, val y: Double, val z: Double) {
    companion object {
        fun parse(coordsAsString: String): Coordinates {
            val (strX, strY, strZ) = coordsAsString.split("/");
            val x = strX.toDouble();
            val y = strY.toDouble();
            val z = strZ.toDouble();

            return Coordinates(x, y, z)
        }
    }

    override fun toString() = "${String.format("%.3f", this.x)}/${String.format("%.3f", this.y)}/${String.format("%.3f", this.z)}"
}