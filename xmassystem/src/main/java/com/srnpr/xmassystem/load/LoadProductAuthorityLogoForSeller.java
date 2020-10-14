package com.srnpr.xmassystem.load;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.face.IPlusConfig;
import com.srnpr.xmassystem.modelproduct.PlusModelAuthorityLogo;
import com.srnpr.xmassystem.modelproduct.PlusModelAuthorityLogos;
import com.srnpr.xmassystem.modelproduct.PlusModelProductAuthorityLogoForSellerQuery;
import com.srnpr.xmassystem.plusconfig.PlusConfigProductAuthorityLogo;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.xmassystem.top.LoadTop;
import com.srnpr.xmassystem.up.XmasKv;

/**
 * @description: 加载权威标识信息|与LoadProductAuthorityLogo相似，这里获取的是商户自己定义的标识。
 * 	其reids key为：xs-ProductAuthorityLogo-productCode 格式
 * 	注意：不是每个商品都会有这个标识。 
 *
 * @author Yangcl
 * @date 2017年3月3日 下午2:14:28 
 * @version 1.0.0
 */
public class LoadProductAuthorityLogoForSeller extends LoadTop<PlusModelAuthorityLogos, PlusModelProductAuthorityLogoForSellerQuery> {

	public PlusModelAuthorityLogos topInitInfo(PlusModelProductAuthorityLogoForSellerQuery query) {
		return new PlusSupportProduct().initProductAuthorityLogoForSellerFromDb(query.getCode());
	}

	private final static PlusConfigProductAuthorityLogo PLUS_CONFIG = new PlusConfigProductAuthorityLogo();

	public IPlusConfig topConfig() {
		return PLUS_CONFIG;
	}
	
	@Override
	public PlusModelAuthorityLogos upInfoByCode(PlusModelProductAuthorityLogoForSellerQuery tQuery) {
		// 商品关联的权威标识
		PlusModelAuthorityLogos pmal = super.upInfoByCode(tQuery);
		
		// 有效的商品权威标识（商品标签类型）
		List<PlusModelAuthorityLogo> logoList = new PlusSupportProduct().initProductAuthorityLogoForSellerFromDb();
		Map<String,PlusModelAuthorityLogo> map = new HashMap<String, PlusModelAuthorityLogo>();
		for(PlusModelAuthorityLogo item : logoList){
			map.put(item.getUid(), item);
		}
		
		// 剔除已经删除的标签、更新缓存内容
		Iterator<PlusModelAuthorityLogo> iterator = pmal.getAuthorityLogos().iterator();
		PlusModelAuthorityLogo logo,temp;
		while(iterator.hasNext()){
			logo = iterator.next();
			temp = map.get(logo.getUid());
			// 剔除失效的标签
			if(temp == null) {
				iterator.remove();
				XmasKv.upFactory(EKvSchema.ProductAuthorityLogo).del(tQuery.getCode());
				continue;
			}
			
			logo.setAllFlag(temp.getAllFlag());
			logo.setLogoContent(temp.getLogoContent());
			logo.setLogoLocation(temp.getLogoLocation());
			logo.setLogoPic(temp.getLogoPic());
			logo.setManageCode(temp.getManageCode());
			logo.setShowProductSource(temp.getShowProductSource());
		}
		
		return pmal;
	}

}
