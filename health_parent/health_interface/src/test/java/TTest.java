public class TTest {
    public static void main(String[] args) {
        System.out.println("<script>" +
                "select * from t_checkitem where 1=1 " +
                "<if test='condition != null and condition.length &gt; 0 and condition.equals('null')'>" +
                " and code like #{condition} or name like #{condition}" +
                "</if>" +
                "</script>");
    }
}
