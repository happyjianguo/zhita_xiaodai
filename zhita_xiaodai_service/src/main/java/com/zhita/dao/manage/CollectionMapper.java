package com.zhita.dao.manage;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zhita.model.manage.Collection;
import com.zhita.model.manage.Collection_member;
import com.zhita.model.manage.Collectiondetails;
import com.zhita.model.manage.Deferred;
import com.zhita.model.manage.Orderdetails;

public interface CollectionMapper {
	
	
	List<Integer> OrderIdMa(Integer companyId);
	
	
	Integer SelectTotalCount(Collection coll);
	
	
	List<Orderdetails> Allorderdetails(Collection coll);
	
	
	List<Collection_member> CollectionAll(Integer companyId);
	
	
	Integer addCollection(List<Collection> list);
	
	
	List<Orderdetails> SelectOrdersdetails(Orderdetails order);
	
	
	Orderdetails OneOrdersdetails(String orderNumber);
	
	
	Integer CollectionTotalCount();
	
	
	List<Collection> Collectionmemberdetilas(Collection coll);
	
	
	Integer CollectionNum(Collection coll);
	
	
	List<Orderdetails> FenpeiCollection(Collection col);
	
	
	Integer CollectionTotalcount(Collection col);
	
	
	Integer CollectionWeiTotalcount(Collection col);
	
	
	List<Integer> SelectCollectionId(Integer companyId);
	
	
	List<Integer> SelectCollectionMemberIds(@Param("ids")List<Integer> ids);
	
	
	List<Integer> SelectId(Integer id);
	
	
	List<Collection> OneCollection(Orderdetails order);
	
	
	
	
	List<Orderdetails> WeiControllerOrdetialis(Collection coll);
	
	
	Integer AddCollectionAs(Collection col);
	
	
	List<Collection> SelectSumOrderNum(Collection coll);
	
	
	List<Collection> SelectSumOrder(Collection coll);
	
	
	List<Collection> SelectUserNum(Collection col);
	
	
	Integer SelectOrderNum(Collection coll);
	
	
	Integer SelectCollectionNum(Collection col);
	
	
	Integer SelectcollectionStatus(Collection col);
	
	
	Integer SelectUserCollectionNum(Collection col);
	
	
	Integer SelectUsercollectionStatus(Collection col);
	
	
	Deferred DefeSet(Orderdetails order);
	
	
	Collection CollMen(Orderdetails order);
	
	
	Integer CollNum(Integer orderId);
	
	
	Deferred DefNum(Integer orderId);
	
	
	Integer UpdateCollectionDelete(Integer orderId);
	
	
	Integer UpdateOrderStatus(Integer orderId);
}
