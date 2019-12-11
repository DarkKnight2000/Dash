package com.rishi.dash3.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.rishi.dash3.Models.EachClass
import com.rishi.dash3.Models.EachCourse
import com.rishi.dash3.R
import io.realm.Realm
import kotlinx.android.synthetic.main.edit_info_card.view.*

class InfoAdapter(val context: Context, val clsses:MutableList<EachClass>, val canEdit:Boolean, val realm:Realm, var crse:EachCourse?) : RecyclerView.Adapter<InfoAdapter.EachViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): EachViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.edit_info_card,parent,false)
        return  EachViewHolder(view)
    }

    override fun getItemCount(): Int {
        return clsses.size
    }

    override fun onBindViewHolder(p0: EachViewHolder, pos: Int) {
        val cls = clsses[pos]
        //Log.i("All crses codes",cls.toString())
        p0.setData(cls, pos, canEdit)
    }

    inner class EachViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        private var crseClss: EachClass =
            EachClass()
        private var crsePos: Int = 0

        init {
            itemView.btnBin.setOnClickListener {
                if(clsses.map { it.id }.contains(crseClss.id)) {
                    clsses.removeAt(clsses.map { it.id }.indexOf(crseClss.id))
                    realm.beginTransaction()
                    crse?.crseClsses?.remove(crseClss)
                    realm.commitTransaction()
                    notifyItemRemoved(crsePos)
                    notifyItemRangeChanged(crsePos, clsses.size)
                    Toast.makeText(context,"delete  id ",Toast.LENGTH_SHORT).show()
                }
                else
                    Toast.makeText(context,"Cant delete",Toast.LENGTH_SHORT).show()
            }
        }

        fun setData(cls: EachClass?, pos:Int, canEdit: Boolean){
            itemView.weekdayView.text = cls!!.date
            itemView.startTime.text = cls.startTime
            itemView.endTime.text = cls.endTime
            itemView.room.text = cls.room
            itemView.btnBin.visibility = if (canEdit) View.VISIBLE else View.GONE


            this.crseClss = cls
            this.crsePos = pos

        }
    }
}