//package vip.wlwbad;
//
//import helps.GenFileHelp;
//import vip.shishou.CreateFileHelp;
//
///**
// * @author 程刘德
// * @version 1.0
// * @Description TODO
// * @date 2021/1/13
// */
//public class wlwbad {
//    public static void main(String[] args) {
////  select * from coll_invoices@cq where revoke_flag=2 and closed_date is null
////  --296664
////  select * from coll_revoke_invoices@cq  where revoke_flag=2  and to_char(closed_date,'yyyymm') = to_char(add_months(sysdate,-1),'yyyymm' )
///
//        GenFileHelp.init().sql("select * from jt_bill_data_invoice where rownum <100000")
//                .limit(1000)
//                .split("\\|")
//
//    }
//}
