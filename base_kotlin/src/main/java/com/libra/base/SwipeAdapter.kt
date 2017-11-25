package com.libra.base

import com.libra.superrecyclerview.swipe.BaseSwipeAdapter
import java.util.*

/**
 * Created by libra on 2017/6/15
 */

public abstract class SwipeAdapter : BaseSwipeAdapter<SwipeViewHolder>() {

    private var data: ArrayList<Any>? = null

    init {
        data = ArrayList()
    }

    override fun onBindViewHolder(viewHolder: SwipeViewHolder, position: Int) {
        viewHolder.bindViewHolder(getItem(position))

    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    /**
     * 设置数据源
     *
     * @param objects 数据源
     */
    fun setData(objects: ArrayList<*>) {
        data!!.clear()
        append(objects)
    }

    /**
     * 添加数据
     *
     * @param objects 数据数组
     */
    fun append(objects: ArrayList<*>) {
        val positionStart = data!!.size
        val itemCount = objects.size
        data!!.addAll(objects)
        if (positionStart > 0 && itemCount > 0) {
            notifyItemRangeInserted(positionStart, itemCount)
        } else {
            notifyDataSetChanged()
        }
    }

    fun add(any: Any) {
        data!!.add(any)
        notifyDataSetChanged()
    }

    /**
     * 插入数据
     *
     * @param index 位置
     * @param object 数据
     */
    fun insert(index: Int, any: Any) {
        data!!.add(index, any)
        notifyDataSetChanged()
    }

    /**
     * 重置数据
     *
     * @param index 位置
     * @param object 数据
     */
    operator fun set(index: Int, any: Any) {
        if (index != -1) {
            data!![index] = any
            notifyDataSetChanged()
        }
    }

    /**
     * 删除数据
     *
     * @param object 数据
     */
    fun remove(any: Any) {
        data!!.remove(any)
        notifyDataSetChanged()
    }

    /**
     * 删除数据
     *
     * @param position 数据位置
     */
    fun remove(position: Int) {
        data!!.removeAt(position)
        notifyDataSetChanged()
    }

    /**
     * 数据在数据源中的位置
     *
     * @param o 数据
     * @return 位置
     */
    fun index(o: Any): Int {
        return data!!.indexOf(o)
    }

    /**
     * 清除数据源
     */
    fun clear() {
        data!!.clear()
        notifyDataSetChanged()
    }

    /**
     * 判断是否包含
     *
     * @param o 对象
     * @return 是否
     */
    operator fun contains(o: Any): Boolean {
        return data?.contains(o) ?: false
    }

    /**
     * 获取position位置的数据
     *
     * @param position 位置
     */
    fun getItem(position: Int): Any? {
        return if (data == null) null else data!![position]
    }

    fun getData(): ArrayList<Any>? {
        return data
    }
}