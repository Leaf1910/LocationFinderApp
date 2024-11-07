package com.example.locationfinder

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.locationfinder.database.LocationDatabaseHelper

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: LocationDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = LocationDatabaseHelper(this)

        // UI elements
        val addressInput = findViewById<EditText>(R.id.addressInput)
        val latitudeInput = findViewById<EditText>(R.id.latitudeInput)
        val longitudeInput = findViewById<EditText>(R.id.longitudeInput)
        val resultTextView = findViewById<TextView>(R.id.resultTextView)

        val addButton = findViewById<Button>(R.id.addButton)
        val searchButton = findViewById<Button>(R.id.searchButton)
        val updateButton = findViewById<Button>(R.id.updateButton)
        val deleteButton = findViewById<Button>(R.id.deleteButton)

        // Add location to database
        addButton.setOnClickListener {
            val address = addressInput.text.toString()
            val latitude = latitudeInput.text.toString().toDoubleOrNull()
            val longitude = longitudeInput.text.toString().toDoubleOrNull()

            if (address.isNotEmpty() && latitude != null && longitude != null) {
                dbHelper.addLocation(address, latitude, longitude)
                resultTextView.text = "Location added: $address"
            } else {
                resultTextView.text = "Please enter valid data"
            }
        }

        // Search location by address
        searchButton.setOnClickListener {
            val address = addressInput.text.toString()

            if (address.isNotEmpty()) {
                val location = dbHelper.getLocationByAddress(address)
                if (location != null) {
                    resultTextView.text = "Found: ${location.address} - Lat: ${location.latitude}, Long: ${location.longitude}"
                    latitudeInput.setText(location.latitude.toString())
                    longitudeInput.setText(location.longitude.toString())
                } else {
                    resultTextView.text = "Location not found"
                }
            } else {
                resultTextView.text = "Please enter an address to search"
            }
        }

        // Update location in database
        updateButton.setOnClickListener {
            val address = addressInput.text.toString()
            val latitude = latitudeInput.text.toString().toDoubleOrNull()
            val longitude = longitudeInput.text.toString().toDoubleOrNull()

            if (address.isNotEmpty() && latitude != null && longitude != null) {
                val location = dbHelper.getLocationByAddress(address)
                if (location != null) {
                    dbHelper.updateLocation(location.id, address, latitude, longitude)
                    resultTextView.text = "Location updated: $address"
                } else {
                    resultTextView.text = "Location not found for update"
                }
            } else {
                resultTextView.text = "Please enter valid data for update"
            }
        }

        // Delete location from database
        deleteButton.setOnClickListener {
            val address = addressInput.text.toString()

            if (address.isNotEmpty()) {
                val location = dbHelper.getLocationByAddress(address)
                if (location != null) {
                    dbHelper.deleteLocation(location.id)
                    resultTextView.text = "Location deleted: $address"
                    latitudeInput.text.clear()
                    longitudeInput.text.clear()
                } else {
                    resultTextView.text = "Location not found for deletion"
                }
            } else {
                resultTextView.text = "Please enter an address to delete"
            }
        }
    }
}