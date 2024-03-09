package com.bivizul.phonecam

import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.p2p.WifiP2pManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.bivizul.phonecam.ui.MainScreen
import com.bivizul.phonecam.ui.theme.PhoneCamTheme
import java.io.IOException
import java.net.ServerSocket

class MainActivity : ComponentActivity() {

    private lateinit var wifiP2pManager: WifiP2pManager
    private lateinit var channel: WifiP2pManager.Channel
    private val permissionCode = 1001
    private val permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.CAMERA)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PhoneCamTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(
                        onClick = {
                            if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                wifiP2pManager.discoverPeers(channel, object : WifiP2pManager.ActionListener {
                                    override fun onSuccess() {
                                        Toast.makeText(this@MainActivity, "Discovery started", Toast.LENGTH_SHORT).show()
                                    }

                                    override fun onFailure(reason: Int) {
                                        Toast.makeText(this@MainActivity, "Discovery failed: $reason", Toast.LENGTH_SHORT).show()
                                    }
                                })
                            } else {
                                Toast.makeText(this@MainActivity, "Location permission required", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }
            }
        }

        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
            checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(permissions, permissionCode)
        }

        wifiP2pManager = getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager
        channel = wifiP2pManager.initialize(this, mainLooper, null)

        // Определение потока сервера
        Thread(ServerThread()).start()
    }

    inner class ServerThread : Runnable {
        override fun run() {
            try {
                val serverSocket = ServerSocket(8888)
                val client = serverSocket.accept()
                val outputStream = client.getOutputStream()

                // Здесь должна быть логика захвата видео и передачи через outputStream

                serverSocket.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permissions granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permissions denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}