//package org.corefine.common.jdbc;
//
//import com.github.pagehelper.Page;
//import com.github.pagehelper.PageHelper;
//
//public class PageUtils {
//
//    /**
//     * 仅查询list不查询count
//     */
//    public static <T> Page<T> list(int pageNum, int pageSize) {
//        return PageHelper.startPage(pageNum, pageSize, false);
//    }
//
//    /**
//     * 仅查询count不查询list
//     */
//    public static <T> Page<T> count() {
//        return PageHelper.startPage(0, -1, true);
//    }
//
//    /**
//     * 查询list和count
//     */
//    public static <T> Page<T> countList(int pageNum, int pageSize) {
//        return PageHelper.startPage(pageNum, pageSize, true);
//    }
//
//    public static void clear() {
//        PageHelper.clearPage();
//    }
//}
