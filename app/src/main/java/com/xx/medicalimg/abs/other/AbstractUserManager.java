package com.xx.medicalimg.abs.other;


import com.xx.medicalimg.bean.BasicInfo;

/**
 *自定义本地用户信息管理抽象类
 */
public abstract class AbstractUserManager {
	/**
	 * 获取登陆用户，需要强制类型转换
	 * @return 确认详细信息需要类型转换
	 */
	public abstract BasicInfo getUser ();

	/**
	 * 设置用户
	 * 调用此方法默认登陆了，不用重新设置登录状态
	 * @param user 获取详细信息需要强制类型转换
	 */
	public abstract void setUser(BasicInfo user);
	/**
	 * 获取用户类型,主要用于在getUser后进行强制类型转换
	 * @return normal普通信息，employee医务人员信息
	 */
	public abstract String getUserType();

	/**
	 * Manger结束后调用，用于自动保存数据
	 */
	public abstract void onDepose();
	/**
	 *从磁盘中恢复数据，
	 * 定义为protected,因为恢复是主动的，外界不可调用
	 */
	protected abstract void restoreUserInfo();

	/**
	 * 保存数据到磁盘中
	 * 定义为protected,因为保存是主动的，外界不可调用
	 */
	protected abstract void saveUserInfo();
}
