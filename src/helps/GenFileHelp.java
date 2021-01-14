package helps;

import lombok.Data;

/**
 * @author 程刘德
 * @version 1.0
 * @Description TODO
 * @date 2021/1/14
 */
@Data
public class GenFileHelp {

    private String SQL;
    private int limit;
    private  String split;
    private  String dirLocation;
    private  String finaDir;
//    dirLocation = "/home/bgusr01/vip_backend/2.1new/";
//    finaDir = "/home/bgusr01/vip_backend/files/";

    private static GenFileHelp createFileHelp = null;
    public static GenFileHelp init() {
        createFileHelp = new GenFileHelp();
        return createFileHelp;
    }

    public GenFileHelp sql(String sql) {
        createFileHelp.setSQL(sql);
        return createFileHelp;
    }
    public GenFileHelp limit(int lim) {
        createFileHelp.setLimit(lim);
        return createFileHelp;
    }
    public GenFileHelp split(String spli) {
        createFileHelp.split(spli);
        return createFileHelp;
    }
//    private  String dirLocation;
//    private  String finaDir;
    public GenFileHelp dirLocation(String dirLocation) {
        createFileHelp.setDirLocation(dirLocation);
        return createFileHelp;
    }
    public GenFileHelp finaDir(String finaDir) {
        createFileHelp.setFinaDir(finaDir);
        return createFileHelp;
    }

}
