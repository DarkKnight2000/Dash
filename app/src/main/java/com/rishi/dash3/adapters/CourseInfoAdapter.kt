package com.rishi.dash3.adapters

//import com.rishi.dash3.services.NotifService
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.rishi.dash3.R
import com.rishi.dash3.activties.CourseInfo
import com.rishi.dash3.models.EachCourse
import com.rishi.dash3.models.Settings
import com.rishi.dash3.utils.restartNotifService
import io.realm.Realm
import kotlinx.android.synthetic.main.course_list_card.view.*


class ClassesAdapter(val context: Context, val clsses:MutableList<EachCourse>, val realm: Realm) : RecyclerView.Adapter<ClassesAdapter.EachViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): EachViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.course_list_card,parent,false)
        return  EachViewHolder(view)
    }

    override fun getItemCount(): Int {
        return clsses.size //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(p0: EachViewHolder, pos: Int) {
        val cls = clsses[pos]
        p0.setData(cls, pos)
    }


    inner class EachViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){

        private var crseClss: EachCourse? = null
        private var crsePos: Int = 0

        init {
            itemView.setOnClickListener{
                if(clsses.size >= crsePos){
                    val intent = Intent(context, CourseInfo::class.java)

                    intent.putExtra("name",clsses[crsePos].crsename)
                    intent.putExtra("code",clsses[crsePos].crsecode)
                    intent.putExtra("slot",clsses[crsePos].defSlot)
                    context.startActivity(intent)

                }
                else
                    Toast.makeText(context,"Cant open Info screen", Toast.LENGTH_SHORT).show()

            }
            itemView.btnBin.setOnClickListener{
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Warning!!")
                //set message for alert dialog
                builder.setMessage("Are you sure you want to delete \"${crseClss?.crsename}\" course? This cannot be undone!")

                //performing positive action
                builder.setPositiveButton("Delete"){_, _ ->
                    if(clsses.contains(crseClss)) {
                        realm.beginTransaction()
                        val crse = realm.where(EachCourse::class.java).equalTo("crsecode", crseClss?.crsecode).findFirst()
                        crse?.crseClsses?.deleteAllFromRealm()
                        crse?.deleteFromRealm()
                        realm.commitTransaction()
                        notifyItemRemoved(crsePos)
                        notifyItemRangeChanged(crsePos,clsses.size)
                        //context.startService(Intent(context, NotifService::class.java))
                        //Toast.makeText(context, "Deleted at, Size " + clsses.size, Toast.LENGTH_SHORT).show()
                        if(realm.where(Settings::class.java).findFirst()!!.sendNotif) restartNotifService(
                            context
                        )
                    }
                    else
                        Toast.makeText(context,"Cant delete course", Toast.LENGTH_SHORT).show()
                }
                //performing cancel action
                builder.setNegativeButton("Cancel"){_ , _ ->
                    Toast.makeText(context,"Delete aborted",Toast.LENGTH_LONG).show()
                }
                // Create the AlertDialog
                val alertDialog: AlertDialog = builder.create()
                // Set other dialog properties
                alertDialog.setCanceledOnTouchOutside(true)
                alertDialog.show()
            }
        }

        fun setData(cls: EachCourse?, pos:Int){
            itemView.cardTitle.text = cls!!.crsename

            this.crseClss = cls
            this.crsePos = pos

        }
    }
}
