package com.srnpr.xmassystem.enumer;


public enum EKvSchema {
	/*
	 * 商品库存信息 该库存信息存放各种SKU或者活动明细的库存数量
	 */
	Stock,

	/**
	 * 商品价格
	 */
	Price,

	/**
	 * SKU的信息 该SKU信息中包含着实时的价格等可购买信息
	 */
	Sku,

	/**
	 * 活动
	 */
	Event,
	/**
	 * 活动排除商品
	 */
	EventExclude,

	/**
	 * 根据活动明细编号缓存的SKU信息
	 */
	IcSku,

	/**
	 * 活动明细的基本信息
	 */
	Item,

	/**
	 * 活动明细上同一商品编号关联出的明细，逗号分隔，主要用于同一活动下多个SKU的信息的分解
	 */
	Iconst,

	/**
	 * 库存变为0的时间
	 */
	EmptyStock,

	/**
	 * 活动明细的日志，用于核算活动库存限制
	 */
	LogItem,

	/**
	 * 订单占用活动库存日志，用户取消订单时返回促销库存
	 */
	LogOrderStock,

	/**
	 * 订单
	 */
	Order,

	/**
	 * 在线支付的方式
	 */
	PayFrom,

	/**
	 * 锁定库存 一般是根据SKU的活动来锁定的库存
	 */
	LockStock,

	/**
	 * 活动锁定库存
	 */
	EventLock,

	/**
	 * 预计订单取消时间
	 */
	TimeCancelOrder,

	/**
	 * 记录活动下所有进入过缓存系统的子编号
	 */
	EventChildren,

	/*
	 * 记录下商品下关联的IC编号信息 以备清除缓存用
	 */
	ProductIcChildren,

	/**
	 * 特殊子活动下的子活动类型对应的活动的编号
	 */
	SubEventCode,

	/**
	 * 明细编号
	 */
	SubIcCode,

	/**
	 * 二维码链接
	 */
	Qrcode,

	/**
	 * 密码校验
	 */
	PasswordCheck,
	/**
	 * 扫码购允许商品
	 */
	ScannerAllow,
	/**
	 * 用于新用户首次下单上级返券总金额
	 */
	SuperiorTotalCoupon,
	/**
	 * 用于新用户首次下单上级返券总卷数
	 */
	SuperiorSumCoupon,

	/*
	 * SKU分库库存数据
	 */
	SkuStoreStock,

	/**
	 * 基本哈希数据
	 */
	DefineHashKey,

	/**
	 * 图片缩放
	 */
	ImageZoom,

	/**
	 * 创建订单
	 */
	CreateOrder,
	/**
	 * sku扩展信息
	 */
	SkuInfoSpread,

	/**
	 * 订单地址
	 */
	Address,

	/**
	 * 赠品信息
	 */
	Gift,

	/**
	 * 商品扩展
	 */
	Product,
	
	/**
	 * 退款单处理标识
	 */
	RefundTime,

	/**
	 * 运费模板
	 */
	Freight,
	/*
	 * 关联SKU编号，用于复制商品之类的判断和校验 存放的是sku关联的复制或者被复制的sku的编号 双向关联关系
	 */
	ConcatSku,

	/**
	 * 商品下面的所有的SKU编号，用逗号分隔
	 */
	ProductSku,

	/**
	 * 全场类活动
	 */
	EventSale,

	/**
	 * 购物车信息
	 */
	ShopCart,
	/**
	 * 第三级行政编号
	 */
	AreaCode,
	/**
	 * 区域模板对应的城市编号
	 */
	templateAreaCode,
	/**
	 * 用户相关信息
	 */
	Member,
	/**
	 *黑名单 
	 */
	Black,
	/**
	 * 运费减免
	 */
	FreeShipping,
	/**
	 * 商品近30天销量
	 */
	ProductSales,
	/**
	 * 商品标签
	 */
	ProductLabels,
	/**
	 * 拼好货
	 */
	GoodsProduct,
	/**
	 * 内购
	 */
	EvenetPurchase,
	
	/**
	 * 订单活动信息
	 */
	ActivityInfo,
	/***
	 * 是否内购
	 */
	UserMemberCode,
	/**
	 * 拼好货
	 */
	PinHaoHuo,
	/**
	 * 惠家有后台冻结用户累计触发值
	 */
	FreezeOperator,
	
	/**
	 * 商户信息
	 */
	Seller,
	
	/**
	 * 权威标志
	 */
	ProductAuthorityLogo,

	/**
	 * 跨境通商品常见问题
	 */
	ProductCommonProblem,

	/**
	 * 控制微公社余额是否显示与是否可以使用
	 * xs-CgroupMoney-Config (hash)
	 * [key]view-商品返利 			[value]1：显示，其他：不显示
	 * [key]use-下单是否可用微公社余额  	[value]1：能，其他：不能
	 * [key]withdraw-取现开关                 [value]1：能，其他：不能
	 */
	CgroupMoney,
	
	/**
	 * 优惠券活动
	 */
	CouponActivity,
	
	/**
	 * 缤纷扫码购明细
	 */
	ErweiCodeBinfen,
	/**
	 * 分销优惠券活动
	 */
	ActivityAgent,
	
	/**
	 * web专题模板
	 */
	WebTemplateCode,
	
	/**
	 * 惠豆设置包括是否可用(switch),消费设置(consume)和生产设置(produce)
	 * hash结构.
	 * xs-HomehasBeanConfig-Consume
	 * xs-HomehasBeanConfig-Produce
	 * xs-HomehasBeanConfig-switch
	 */
	HomehasBeanConfig,
	
	/**
	 * 存放图片的宽高
	 * key :图片的链接
	 * value:width-height ;  eg:640-640;
	 */
	ImageWidthHeight,
	
	/**
	 * 购物车猜你喜欢是否启用 4497480100020001 是 |4497480100020002 否
	 */
	ShoppingCartMaybeLove,
	
	/**
	 * 客户端消息是否启用 4497480100020001 是 |4497480100020002 否
	 */
	MessageUseable,
	
	/**
	 * 会员日折扣
	 */
	VipDiscount,
	/**
	 * 家有用户等级
	 */
	MemberLevel,
	
	/**
	 * 微信小程序支付订单的prepareid
	 */
	XcxOrderPrepareId,
	
	/**
	 * 在线支付立减活动
	 */
	EventOnlinePay,
	/**
	 * 当日播出的商品
	 */
	PcTvGoods,
	/**
	 * 考拉订单确认接口缓存
	 */
	KaoLaOrderConfirm,
	/**
	 * 是否同步LD订单
	 */
	IsSyncLdOrder,
	/**
	 * 是否需要更新分类商品表
	 */
	IsUpdateCategoryProductCount,
	/**
	 * 是否同步LD订单
	 */
	SignCycle,
	/**
	* 是否需要更新分类商品表
	 */
	IsUpdateBrandProductCount,
	/**
	 * 全场权威标识
	 */
	SellerAuthorityLogo,
	/**
	 * 优惠券类型信息
	 */
	CouponType,
	/**
	 * 详情页优惠券类型
	 */
	ShowCouponType,
	/**
	 * 商品SKU临时调价记录
	 */
	SkuPriceChange,
	/**
	 * 优惠券类型信息
	 */
	CouponTypeInfo,
	/**
	 * 优惠券是否存在记录
	 */
	CouponExist,
	/**
	 * 优惠券列表信息
	 */
	CouponListForProduct,
	/**
	 * 是否领取优惠券信息
	 */
	CouponGetUser,
	/**
	 * 获取京东token
	 */
	JingDongToken,
	/**
	 * 互动活动商品信息
	 */
	HuDongEvent,
	/**
	 * 微信小程序音乐相册token
	 */
	wxMusicAlbumToken,
	/**
	 * 秒杀活动时间列表
	 */
	FlashSaleList,
	/**
	 * 秒杀活动商品列表
	 */
	FlashSaleProductList,
	/**
	 * CCVID对应视频播放链接地址
	 */
	CcVideoLink,
	/**
	 * 商品实际订单量
	 */
	ProductOrderNum,
	/**
	 * 用于签到、打卡、农场判断是否当日访问
	 */
	PageActiveFlag,
	/**
	 * 支付类型信息
	 */
	payTypeInfo,
	/**
	 * 短信支付标识
	 */
	SmsPayFlag,
	/**
	 * 首页滚动消息
	 */
	ScrollMessage,
	/**
	 * 橙意卡专享活动
	 */
	EventPlusList,
	/**
	 * 优惠券使用记录
	 */
	CouponUse,
	/**
	 * 特殊橙意卡活动
	 */
	PlusSaleProductList,
	/**
	 * 微信小程序分享图片
	 */
	WxSharePic,
	/**
	 * 专题送积分
	 */
	Ztsjf,
	/**
	 * 
	 */
	ShortLinkStart
}
