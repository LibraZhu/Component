package com.libra.app;

import java.util.ArrayList;

/**
 * Created by huyg on 2018/10/21.
 */
public class NewsResponse {

    /**
     * total : 1
     * rows : [{"id":1,"title":"超职教育是由超职时代（北京）教育科技有限公司所打造的综合性在线教育平台","img":"//test-aci-api.chaozhiedu.com/api/file/10710","news_category_id":1,"category_id":2,"content":"<p>超职教育是由超职时代（北京）教育科技有限公司所打造的综合性在线教育平台，多年来主要来以提供快乐、高效、超值的课程体验为服务宗旨，课程推出至今，高质量与好口碑是我们的核心发展前提，在课程建设方面，我们不断完善，加强环节把控，严密针对学员自身条件推出不同课型，有效提升学员短期内的成长进度，加强与学员的情感联系，努力为学员提供售前售后的极致体验。<\/p><p><br><\/p><p><br><\/p>","ct":"2018-08-23 00:42:39","ut":"2018-08-23 01:00:06","status":1,"subtitle":"hello"}]
     */

    private int total;
    private ArrayList<NewBean> rows;

    public ArrayList<NewBean> getRows() {
        return rows;
    }

    public void setRows(ArrayList<NewBean> rows) {
        this.rows = rows;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

}
