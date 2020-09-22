package helps;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;

public class SFTPHelp {
	private static Session sshSession;
	private static Channel channel;
	private static ChannelSftp sftp = null;
	/**
	  * 连接sftp服务器
	  * @param host 主机
	  * @param port 端口
	  * @param username 用户名
	  * @param password 密码
	  * @return
	  */
	public static ChannelSftp connect(String host, int port, String username, String password) {
	  try {
	   JSch jsch = new JSch();
	   jsch.getSession(username, host, port);
	   sshSession = jsch.getSession(username, host, port);
	   sshSession.setPassword(password);
	   Properties sshConfig = new Properties();
	   sshConfig.put("StrictHostKeyChecking", "no");
	   sshSession.setConfig(sshConfig);
	   sshSession.connect();
	   channel = sshSession.openChannel("sftp");
	   channel.connect();
	   sftp = (ChannelSftp) channel;
	   System.out.println("登录成功");
	  } catch (Exception e) {
           System.out.println("链接出问题了==============================》》》》》》》》》》》》"+e.getMessage());
	  }
	  return sftp;
	 }
	 /**
	  * 下载文件
	  * @param directory 服务器文件所在的目录
	  * @param downloadFile 需要下载的文件名
	  * @param saveFile 存在本地位置（文件夹）
	  */
	 public void download (String directory, String downloadFile, String saveFile) {
		    try {    
	            sftp.cd(directory);
	            System.out.println("服务器文件所在的目录  =========="+directory);
				System.out.println("需要下载的文件名      =========="+downloadFile);
				System.out.println("存在本地位置（文件夹）=========="+saveFile);

	            File file = new File(saveFile);
	            FileOutputStream fileOutputStream =new FileOutputStream(file);
	            sftp.get(downloadFile, fileOutputStream );
	            fileOutputStream.close();    
	        } catch (Exception e) {
	           e.printStackTrace();
	        }finally{
	        	if(sftp != null){
		    		sftp.disconnect();
		    	}
	        	if(channel != null){
	        		channel.disconnect();
	        	}
	        	if(sshSession != null){
	        		sshSession.disconnect();
	        	}
	        }
		    
		 }

	public static void main(String[] args) {
//		ChannelSftp connect = SFTPHelp.connect();
//		connect.d

	}
	  
	     
	    
}
