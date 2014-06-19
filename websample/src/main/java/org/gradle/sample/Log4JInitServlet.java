package org.gradle.sample;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.PropertyConfigurator;

public class Log4JInitServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void init() throws ServletException {
		String prefix = getServletContext().getRealPath("/");
		String test = getServletContext().getRealPath("");
		System.out.println(prefix);
		System.out.println(test);
		//System.setProperty("webappHome", test);
		String file = getServletConfig().getInitParameter("log4jConfig");
		System.out.println(prefix + file);
        // 从Servlet参数读取log4j的配置文件
		if (file != null) {
			PropertyConfigurator.configure(prefix + file);
		}

	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		System.out.println(request.getAttribute("name"));
		System.out.println(request.getAttribute("second"));
		System.out.println(request.getAttribute("value"));
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		doGet(request, response);
	}

}
