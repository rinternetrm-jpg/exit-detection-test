package com.exitreminder.exitdetection.domain.model

/**
 * Complete profile of a location for exit detection
 * Created once when user adds a new reminder at that location
 */
data class LocationProfile(
    val id: Long = 0,
    val createdAt: Long = System.currentTimeMillis(),

    // === WLAN ===
    val wifiSsid: String,
    val wifiBssid: String,
    val wifiSignalAtStart: Int,  // dBm

    // === GPS ===
    val latitude: Double,
    val longitude: Double,
    val gpsAccuracyAtStart: Float,  // meters
    val altitude: Double,  // meters above sea level

    // === MAP ANALYSIS (from ChatGPT) ===
    val buildingType: BuildingType = BuildingType.UNKNOWN,
    val estimatedFloor: Int = 0,  // 0 = ground floor
    val totalFloors: Int? = null,
    val hasGarden: Boolean = false,
    val gardenDirection: Direction? = null,

    // === STREET INFO ===
    val nearestStreetName: String = "",
    val nearestStreetDistance: Float = 0f,  // meters
    val nearestStreetDirection: Direction = Direction.NORTH,
    val streetType: StreetType = StreetType.RESIDENTIAL,

    // === EXITS ===
    val possibleExits: List<ExitPoint> = emptyList(),

    // === SURROUNDINGS ===
    val surroundingBuildings: Int = 0,
    val isUrbanArea: Boolean = true,
    val nearestPOIs: List<String> = emptyList(),

    // === HEIGHT PROFILE ===
    val baseAltitude: Double = 0.0,  // ground floor altitude
    val floorHeight: Float = 3.0f,  // ~3m per floor

    // === TIMING (learned) ===
    val typicalExitDuration: Int? = null  // seconds to exit
)

enum class BuildingType(val emoji: String, val displayName: String) {
    HOUSE("🏠", "Einfamilienhaus"),
    APARTMENT("🏢", "Mehrfamilienhaus"),
    OFFICE("🏛️", "Bürogebäude"),
    OFFICE_COMPLEX("🏙️", "Bürokomplex"),
    HIGHRISE("🏬", "Hochhaus"),
    SHOPPING("🛒", "Einkaufszentrum"),
    HOSPITAL("🏥", "Krankenhaus"),
    SCHOOL("🏫", "Schule"),
    OTHER("❓", "Sonstiges"),
    UNKNOWN("❓", "Unbekannt")
}

enum class Direction(val symbol: String, val angle: Float) {
    NORTH("↑", 0f),
    NORTH_EAST("↗", 45f),
    EAST("→", 90f),
    SOUTH_EAST("↘", 135f),
    SOUTH("↓", 180f),
    SOUTH_WEST("↙", 225f),
    WEST("←", 270f),
    NORTH_WEST("↖", 315f);

    companion object {
        fun fromBearing(bearing: Float): Direction {
            val normalized = (bearing + 360) % 360
            return when {
                normalized < 22.5 || normalized >= 337.5 -> NORTH
                normalized < 67.5 -> NORTH_EAST
                normalized < 112.5 -> EAST
                normalized < 157.5 -> SOUTH_EAST
                normalized < 202.5 -> SOUTH
                normalized < 247.5 -> SOUTH_WEST
                normalized < 292.5 -> WEST
                else -> NORTH_WEST
            }
        }
    }
}

enum class StreetType(val displayName: String) {
    FOOTPATH("Fußweg"),
    RESIDENTIAL("Wohnstraße"),
    MAIN("Hauptstraße"),
    HIGHWAY("Schnellstraße")
}

data class ExitPoint(
    val direction: Direction,
    val distance: Float,  // meters
    val exitType: ExitType,
    val leadsTo: String  // "Hauptstraße", "Garten", etc.
)

enum class ExitType(val emoji: String, val displayName: String) {
    MAIN_ENTRANCE("🚪", "Haupteingang"),
    MAIN_DOOR("🚪", "Haupteingang"),
    SIDE_DOOR("🚪", "Nebeneingang"),
    GARAGE("🚗", "Garage"),
    GARDEN_GATE("🌳", "Gartentor"),
    BACK_DOOR("🚪", "Hintertür"),
    EMERGENCY_EXIT("🚨", "Notausgang"),
    EMERGENCY("🚨", "Notausgang"),
    ELEVATOR("🛗", "Aufzug"),
    STAIRS("🪜", "Treppe")
}
