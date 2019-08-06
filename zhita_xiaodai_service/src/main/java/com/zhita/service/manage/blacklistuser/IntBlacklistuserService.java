package com.zhita.service.manage.blacklistuser;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.zhita.model.manage.BlacklistUser;
import com.zhita.model.manage.Company;

public interface IntBlacklistuserService {
	 //后台管理---查询列表
     public Map<String, Object> queryAll(Integer page,Integer companyId,String name,String phone,String idcard,String blackType);
     
     //后台管理——添加功能（先查询出所有公司）
     public List<Company> queryAllCompany();
     
     //后台管理---添加操作
     public int insert(BlacklistUser record);
     
     //后台管理---根据id查询当前对象信息
     public BlacklistUser selectByPrimaryKey(Integer id);
     
     //后台管理---更新保存功能
     public int updateByPrimaryKey(BlacklistUser record);
     
     //后台管理---更新假删除状态
     public int upaFalseDel(Integer id,Integer userid);
     
     /**
      * 批量导入Excel
      * @param excelFile
      * @return
      */
     String ajaxUploadExcel(MultipartFile excelFile,Integer companyId,Integer operator);

	public int getid(String phone, int companyId);

	public int getid1(String idCard, int companyId);
}
