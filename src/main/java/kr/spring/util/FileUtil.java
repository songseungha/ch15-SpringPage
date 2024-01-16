package kr.spring.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileUtil {
	//업로드 상대 경로
	private static final String UPLOAD_PATH = "/upload";
	
	//파일 생성 시 호출하는 메서드
	public static String createFile(HttpServletRequest request, MultipartFile file) throws IllegalStateException, IOException {
		//절대 경로 구하기
		String absolutePath = request.getServletContext().getRealPath(UPLOAD_PATH);
		//파일명 생성 (난수 발생)
		String filename = null;
		if(!file.isEmpty()) { //파일이 있을 경우
			filename = UUID.randomUUID()+"_"+file.getOriginalFilename();
			//원하는 경로에 파일 저장
			file.transferTo(new File(absolutePath+"/"+filename));
		}
		return filename; //전송된 파일이 없다면 null처리
	}
	
	//파일 삭제 시 호출하는 메서드
	public static void removeFile(HttpServletRequest request, String filename) {
		if(filename!=null) {
			//업로드 절대 경로
			String absolutePath = request.getServletContext().getRealPath(UPLOAD_PATH);
			//파일 객체 생성
			File file = new File(absolutePath+"/"+filename);
			//파일이 존재한다면 삭제, 없으면 무반응
			if(file.exists()) file.delete();
		}
	}
	
	
	public static byte[] getBytes(String path) {
		FileInputStream fis = null;
		byte[] readbyte = null;
		try {
			fis = new FileInputStream(path);
			readbyte = new byte[fis.available()];
			fis.read(readbyte);
		}catch(Exception e) {
			log.error(e.toString());
		}finally {
			if(fis!=null)try {fis.close();}catch(IOException e) {}
		}
		return readbyte;
	}
}
