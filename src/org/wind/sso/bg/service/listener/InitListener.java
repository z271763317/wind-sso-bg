package org.wind.sso.bg.service.listener;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;

/**
 * @描述：web项目监听器（启动前、后的处理）
 * @版权：胡璐璐
 * @时间：2020年10月31日 13:49:41
 */
@WebListener
public class InitListener implements ServletContextListener {

	
	/**启动前：InitServlet首先执行它，启动前的处理*/
	public void contextInitialized(ServletContextEvent arg0) {
		
	}
	
	/**关闭前：关闭前的处理**/
	public void contextDestroyed(ServletContextEvent arg0) {
		Enumeration<Driver> drivers = DriverManager.getDrivers();
		Driver d = null;
		while (drivers.hasMoreElements()) {
			try {
				d = drivers.nextElement();
				DriverManager.deregisterDriver(d);
			} catch (SQLException ex) {

			}
		}
		AbandonedConnectionCleanupThread.checkedShutdown();
	}
}