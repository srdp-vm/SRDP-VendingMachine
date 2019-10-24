package service;

import dao.CommodityDao;
import vo.ItemVO;

public class CommodityService {
	
	private CommodityDao dao = new CommodityDao();
	
	public ItemVO findById(int id) {
		return dao.findById(id);
	}
}
