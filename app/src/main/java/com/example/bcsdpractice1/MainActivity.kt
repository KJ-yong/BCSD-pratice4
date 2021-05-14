package com.example.bcsdpractice1

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.bcsdpractice1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createNotificationChannel()
        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.textView.text = "0"
        binding.buttonToast.setOnClickListener {
            //Toast.makeText(this, "TOAST", Toast.LENGTH_SHORT).show()
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Toast")
            builder.setMessage("Toast")
            var clickDialogButton = DialogInterface.OnClickListener{_, p1->
                when(p1){
                    DialogInterface.BUTTON_POSITIVE->
                        binding.textView.text = "0"
                    DialogInterface.BUTTON_NEUTRAL->
                        Toast.makeText(this, "TOAST", Toast.LENGTH_SHORT).show()
                }

            }
            builder.setPositiveButton("Positive", clickDialogButton)
            builder.setNeutralButton("Neutral",clickDialogButton)
            builder.setNegativeButton("Negative",clickDialogButton)
            builder.show()
        }
        binding.buttonCount.setOnClickListener {
            binding.textView.text = (binding.textView.text.toString().toInt()+1).toString()
        }
        binding.buttonRandom.setOnClickListener {
            val notificationBuilder = NotificationCompat.Builder(this, "progressBar")
            notificationBuilder.setSmallIcon(R.drawable.ic_launcher_foreground)
            notificationBuilder.setContentTitle("프로그래스 바")
            notificationBuilder.setContentText("")
            notificationBuilder.setProgress(100, 0 , false)
            NotificationManagerCompat.from(this).notify(100, notificationBuilder.build())
            setDataAtFragment(RandomFragment(),binding.textView.text.toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(100)
    }
    private fun setFragment(fragment: RandomFragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.frameLayout,fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
    private fun setDataAtFragment(fragment: RandomFragment, string: String){
        val bundle = Bundle()
        bundle.putString("maxnumber",string)
        fragment.arguments = bundle
        setFragment(fragment)
    }
    private fun removeFragment(){
        val transaction = supportFragmentManager.beginTransaction()
        val frameLayout = supportFragmentManager.findFragmentById(R.id.frameLayout)
        transaction.remove(frameLayout!!)
        transaction.commit()
    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "progressbar"
            val descriptionText = "description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("progressBar", name, importance)
            channel.description = descriptionText

            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}