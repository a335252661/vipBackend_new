/*
 * 创建日期 2011-10-28
 *
 * 更改所生成文件模板为
 * 窗口 > 首选项 > Java > 代码生成 > 代码和注释
 */
package utils;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author
 *
 */
public class FtpUtil {

	private FTPClient ftp;
	
	private String path = null;
	
	//上传文件时tempPath不为空，则先将文件上传到临时目录，之后再rename到path目录
	private String tempPath = null;
	
	//远程备份路径
	private String remoteBakDir = null;
	
	
	public String getRemoteBakDir() {
		return remoteBakDir;
	}

	public void setRemoteBakDir(String remoteBakDir) {
		this.remoteBakDir = remoteBakDir;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the tempPath
	 */
	public String getTempPath() {
		return tempPath;
	}

	/**
	 * @param tempPath the tempPath to set
	 */
	public void setTempPath(String tempPath) {
		this.tempPath = tempPath;
	}

	public static FtpUtil connect(String host, String user, String pwd, String path) throws SocketException, IOException {
		FtpUtil ftpUtil = new FtpUtil();
		ftpUtil.ftp = new FTPClient();
		ftpUtil.ftp.connect(host);
		ftpUtil.ftp.login(user, pwd);
		ftpUtil.ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
		ftpUtil.ftp.changeWorkingDirectory(path);
		return ftpUtil;
	}
	
	/**
	 * 设置路径
	 * @throws IOException
	 *
	 */
	public void changeDir(String dir) throws IOException {
		this.ftp.changeWorkingDirectory(dir);
	}
	/**
	 * 得到当前路径下全部文件名
	 * @throws IOException
	 *
	 */
	public String[] getFileNames() throws IOException {
		return this.ftp.listNames();
	}


	/**
	 * 得到文件扩展名
	 * @throws Exception
	 *
	 */
	public static String getFileExtName(String fileName){
		String fileExtName = "";
		if(fileName != null && fileName.lastIndexOf(".") != -1){
			fileExtName = fileName.substring(fileName.lastIndexOf("."),fileName.length());
		}
		return fileExtName;
	}

	/**
	 * 得到指定扩展名的文件名
	 * @throws IOException
	 * @extName 扩展名,如为NULL则取全部文件
	 *
	 */
	public String[] getFileNames(String extName) throws IOException {
		List fileNameList = new ArrayList();
		String[] arr = this.ftp.listNames();
		if(arr == null || arr.length < 1){
			return null;
		}
		for(String fileName:arr){
			if(StringUtils.isNotEmpty(extName) && !getFileExtName(fileName).equals(extName)){
				continue;
			}
			fileNameList.add(fileName);
		}

		if(fileNameList.size() > 0 ){
			return (String[])fileNameList.toArray(new String[fileNameList.size()]);
		}

		return null;

	}
	

	
	/**
	 * 得到指定扩展名的文件
	 * @extName 扩展名
	 * @localDir 本地存放文件路径
	 * @isDel  是否删除FTP上的文件
	 * @throws IOException
	 * @extName 扩展名,如为NULL则取全部文件 
	 *
	 */
	public List<File> downloadFiles(String extName, String localDir, boolean isDel) throws IOException {
		List<File> fileList = new ArrayList<File>();
		String[] fileNames = this.getFileNames(extName);
		if(fileNames != null){
			for (String fileName:fileNames) {
				fileList.add(this.downloadFile(fileName, localDir));
				if(isDel){
					//删除FTP上的文件
					this.deleteFile(fileName);
				}
			}
		}
		 
		return fileList;
		
	}
	
	/**
	 * 根据正则表达式得到文件
	 * @regExp    正则表达式
	 * @localDir 本地存放文件路径
	 * @isDel  是否删除FTP上的文件
	 * @throws IOException
	 * @extName 扩展名,如为NULL则取全部文件 
	 *
	 */
	public List<File> downloadFilesForRegExp(String regExp, String localDir, boolean isDel) throws IOException {
		List<File> fileList = new ArrayList<File>();
		String[] fileNames = this.getRegFileNames(regExp);
		if(fileNames != null && fileNames.length > 0){
			for (String fileName:fileNames) {
				fileList.add(this.downloadFile(fileName, localDir));
				if(isDel){
					//删除FTP上的文件
					this.deleteFile(fileName);
				}
			}
		}
		 
		return fileList;
		
	}
	
	/**
	 * 根据正则表达式得到文件
	 * @regExp    正则表达式
	 * @localDir 本地存放文件路径
	 * @flag  1:删除远程文件 2:备份远程文件
	 * @throws IOException
	 * @extName 扩展名,如为NULL则取全部文件 
	 *
	 */
	public List<File> downloadFilesForRegExp(String regExp, String localDir, String flag) throws IOException {
		List<File> fileList = new ArrayList<File>();
		String[] fileNames = this.getRegFileNames(regExp);
		if(fileNames != null && fileNames.length > 0){
			for (String fileName:fileNames) {
				fileList.add(this.downloadFile(fileName, localDir));
				if("1".equals(flag)){
					//删除FTP上的文件
					this.deleteFile(fileName);
				} else if("2".equals(flag)) {
					//备份FTP上的文件
					if(StringUtils.isNotEmpty(this.getRemoteBakDir())) {
						this.rename(fileName, this.getRemoteBakDir() + fileName);
					} else {
						this.deleteFile(fileName);
					}
				}
			}
		}
		 
		return fileList;
		
	}
	
	/**
	 * 根据正则表达式的文件名
	 * @throws IOException
	 * @extName 扩展名,如为NULL则取全部文件
	 *
	 */
	public String[] getRegFileNames(String regExp) throws IOException {
		List fileNameList = new ArrayList();
		String[] arr = this.ftp.listNames();
		if(arr == null || arr.length < 1){
			return null;
		}
		for(String fileName:arr){
			if(StringUtils.isNotEmpty(regExp) && !fileName.matches(regExp)){
				continue;
			}
			fileNameList.add(fileName);
		}
		
		if(fileNameList.size() > 0 ){
			return (String[])fileNameList.toArray(new String[fileNameList.size()]);
		}
		
		return null;
		
	}
	/**
	 * 下载文件到指定路径下
	 * @localDir 本地路径
	 * @throws IOException
	 */
	public File downloadFile(String fileName, String localDir) throws IOException {
		File tmpfile = new File(localDir+fileName);
		FileOutputStream fos = new FileOutputStream(tmpfile);
		this.ftp.retrieveFile(fileName, fos);
		fos.close();
		return tmpfile;
	}
	
	/**
	 * 删除文件
	 * @throws IOException
	 * @fileName 要删除的文件名
	 *
	 */
	public void deleteFile(String fileName) throws IOException {
		this.ftp.deleteFile(fileName);
	}
	
	/**
	 * 断开链接
	 * @throws Exception
	 *
	 */
	public void disconnect(){
		try {
			if(null != this.ftp){
				this.ftp.logout();
				this.ftp.disconnect();
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	public boolean uploadFile(File file, String fileName){
		FileInputStream fis = null;
		boolean flag = false;
		try{
			fis = new FileInputStream(file);
			this.ftp.storeFile(fileName,fis);
			
			if(StringUtils.isNotEmpty(this.getTempPath())) {
				this.rename(fileName, this.path + fileName);
			}
			flag = true;
		}catch(Exception ex){
			ex.printStackTrace();
		} finally {
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return flag;
	}

	public boolean uploadFile2(File file){
		String fileName = file.getName();
		FileInputStream fis = null;
		boolean flag = false;
		try{
			fis = new FileInputStream(file);
			this.ftp.storeFile(fileName,fis);

			if(StringUtils.isNotEmpty(this.getTempPath())) {
				this.rename(fileName, this.path + fileName);
			}
			flag = true;
		}catch(Exception ex){
			ex.printStackTrace();
		} finally {
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return flag;
	}
	
	public boolean rename(String from, String to) {
		try {
			return this.ftp.rename(from, to);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void uploadFile(List<File> files){
		for(File file:files){
			this.uploadFile(file, file.getName());
		}
	}
	
	public static void main(String[] args) {
//		try {
//			FtpUtil ftp = FtpUtil.connect("10.7.95.70","","","/home/bgusr01/payment/ocs2billing/bak/");
//			ftp.setRemoteBakDir("/home/bgusr01/payment/ocs2billing/config/");
//			ftp.downloadFilesForRegExp(".*", "D:/test/", "2");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

		try {
			FtpUtil ftp = FtpUtil.connect("10.7.95.70","bgusr01","lc#v58iHH",
					"/home/bgusr01/vip_backend/2.1new");
//			ftp.downloadFile("vip.sh","C:\\Users\\Admin\\Desktop\\常用\\logs\\");
//			String[] fileNames = ftp.getFileNames();
//			System.out.println(fileNames.length);
			File file = new File("C:\\Users\\Admin\\Desktop\\ppp\\2.4\\MBI_AMOUNT_202003.021.001.001.001.62.txt");
			ftp.uploadFile(file,"MBI_AMOUNT_202003.021.001.001.001.62.txt");

			//腾讯云服务器 ftp
//            FtpUtil ftp = FtpUtil.connect("106.54.46.37","ftpuser","cld7758258",
//                    "/home/ftpuser/ll");
//			ftp.downloadFile("22.txt","C:\\Users\\Admin\\Desktop\\常用\\logs\\");

//			File file = new File("D:\\7.txt");
//			ftp.uploadFile(file,"7.txt");

//			            FtpUtil ftp2 = FtpUtil.connect("10.145.195.75","acct_pay","Pay!2#we",
//                    "/acct_ftp/payment/yzf");
//			ftp2.downloadFile("abc.txt","C:\\Users\\Admin\\Desktop\\常用\\logs\\");

		} catch (Exception e) {
			e.printStackTrace();
		}



	}
}
