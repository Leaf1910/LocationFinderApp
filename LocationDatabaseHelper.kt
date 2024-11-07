import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// Database Helper Class
class LocationDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    // Create the database table
    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE $TABLE_LOCATIONS (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_ADDRESS TEXT, " +
                "$COLUMN_LATITUDE REAL, " +
                "$COLUMN_LONGITUDE REAL)"
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Handle database upgrade logic (this could be more complex in real apps)
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_LOCATIONS")
        onCreate(db)
    }

    // Add a location to the database
    fun addLocation(address: String, latitude: Double, longitude: Double) {
        val values = ContentValues()
        values.put(COLUMN_ADDRESS, address)
        values.put(COLUMN_LATITUDE, latitude)
        values.put(COLUMN_LONGITUDE, longitude)

        val db = this.writableDatabase
        db.insert(TABLE_LOCATIONS, null, values)
        db.close()
    }

    // Get location by address
    fun getLocationByAddress(address: String): Location? {
        val db = this.readableDatabase
        val cursor: Cursor = db.query(
            TABLE_LOCATIONS, null,
            "$COLUMN_ADDRESS = ?", arrayOf(address),
            null, null, null
        )

        var location: Location? = null
        if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
            val lat = cursor.getDouble(cursor.getColumnIndex(COLUMN_LATITUDE))
            val lon = cursor.getDouble(cursor.getColumnIndex(COLUMN_LONGITUDE))
            location = Location(id, address, lat, lon)
        }
        cursor.close()
        db.close()
        return location
    }

    // Update an existing location
    fun updateLocation(id: Int, address: String, latitude: Double, longitude: Double) {
        val values = ContentValues()
        values.put(COLUMN_ADDRESS, address)
        values.put(COLUMN_LATITUDE, latitude)
        values.put(COLUMN_LONGITUDE, longitude)

        val db = this.writableDatabase
        db.update(TABLE_LOCATIONS, values, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
    }

    // Delete a location by ID
    fun deleteLocation(id: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_LOCATIONS, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
    }

    companion object {
        const val DATABASE_NAME = "locationFinder.db"
        const val DATABASE_VERSION = 1

        const val TABLE_LOCATIONS = "locations"
        const val COLUMN_ID = "id"
        const val COLUMN_ADDRESS = "address"
        const val COLUMN_LATITUDE = "latitude"
        const val COLUMN_LONGITUDE = "longitude"
    }
}

// Location data model
data class Location(val id: Int, val address: String, val latitude: Double, val longitude: Double)
