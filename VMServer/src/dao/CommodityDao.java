package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import util.DBUtil;
import vo.ItemVO;

public class CommodityDao {
	
	public ItemVO findById(int id) {
		Connection conn = DBUtil.getConn();
		String sql = "select * from commodity where id = ?";
		PreparedStatement pstmt = null;
		ItemVO item = null;
		ResultSet rSet = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			rSet = pstmt.executeQuery();
			while (rSet.next()) {
				item = new ItemVO(rSet.getInt(1), rSet.getString(2), rSet.getDouble(3), 1, rSet.getString(4));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeRst(rSet);
			DBUtil.closePstmt(pstmt);
			DBUtil.closeConn(conn);	
		}
		return item;
	}
}
