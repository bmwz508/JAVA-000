import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

/**
 * @author linmf
 * @Description
 * @date 2020/12/1 14:21
 */
public class Insert {

    public static void main(String[] args) {
        //时间-182秒
        insertRelease();
        //时间-29秒
//        insert();
    }

    
    public static Connection getConnection() {
        String url = "jdbc:mysql://localhost:3306/mall";
        String user = "root";
        String password = "123456";
        Connection connection = null;
        try {
            Class<?> aClass = Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
    public static void insert() {
        Connection conn = getConnection();
        // 开时时间
        Long begin = System.currentTimeMillis();
        // sql前缀
        String prefix = "INSERT INTO `mall_order` VALUES ";
        try {
            // 保存sql后缀
            StringBuffer suffix = new StringBuffer();
            // 设置事务为非自动提交
            conn.setAutoCommit(false);
            // Statement st = conn.createStatement();
            // 比起st，pst会更好些
            PreparedStatement pst = conn.prepareStatement("");
            // 外层循环，总提交事务次数
            for (int i = 1; i <= 100; i++) {
                int offSet = (i - 1) * 10000;
                // 第次提交步长
                for (int j = 1; j <= 10000; j++) {
                    // 构建sql后缀
                    suffix.append("(" + (offSet + j) + ",'" + UUID.randomUUID().toString() + "', 'ba7d2452-64ee-4586-b716-800ad61d1d6a', 10.00, 10.00, 1.00, 1.00, 1.00, 1.00, NOW(), NOW()),");
                }
                // 构建完整sql
                String sql = prefix + suffix.substring(0, suffix.length() - 1);
                // 添加执行sql
                pst.addBatch(sql);
                // 执行操作
                pst.executeBatch();
                // 提交事务
                conn.commit();
                // 清空上一次添加的数据
                suffix = new StringBuffer();
            }
            // 头等连接
            pst.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // 结束时间
        Long end = System.currentTimeMillis();
        // 耗时
        System.out.println("cast : " + (end - begin) / 1000 + " ms");
    }


    public static void insertRelease() {
        Connection conn = getConnection();
        // 开时时间
        Long begin = System.currentTimeMillis();
        String sql = "INSERT INTO `mall_order`(`id`, `order_id`, `user_id`, `order_status`, `goods_amount`, `express_amount`, `total_amount`, `actual_amount`, `discount_amount`, `update_time`, `create_time`) VALUES (?, ?, 'ba7d2452-64ee-4586-b716-800ad61d1d6a', 10, 10.00, 1.00, 1.00, 1.00, 1.00, NOW(), NOW())";
        try {
            conn.setAutoCommit(false);
            PreparedStatement pst = conn.prepareStatement(sql);
            for (int i = 1; i <= 100; i++) {
                int offSet = (i - 1) * 10000;
                for (int k = 1; k <= 10000; k++) {
                    pst.setLong(1, offSet + k);
                    pst.setString(2, UUID.randomUUID().toString());
                    pst.addBatch();
                }
                pst.executeBatch();
                conn.commit();
            }
            pst.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Long end = System.currentTimeMillis();
        System.out.println("cast : " + (end - begin) / 1000 + " ms");
    }
}
