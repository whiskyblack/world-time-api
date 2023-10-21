package alphalabs.base

import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<VH : BaseViewHolder<*, *>, M> :
    RecyclerView.Adapter<VH>() {
    val data: MutableList<M> = mutableListOf()

    abstract fun addData(data: MutableList<M>)
    abstract fun clearData()
}