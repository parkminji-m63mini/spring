package com.kh.spring.common.scheduling;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.kh.spring.board.model.service.BoardService;

@Component
public class ImageDeleteScheduling {
	
	private final Logger logger = LoggerFactory.getLogger(ImageDeleteScheduling.class);
	
	@Autowired
	private BoardService boardService;
	
	@Autowired 
	private ServletContext servletContext;


	// * Summernote를 이용한 글 작성 중 이미지가 업로드 될 때 마다 서버에 이미지가 저장되게 코드가 구현되어 있음.
	//   -> Summernote에 업로드한 이미지를 지우는 경우 DB에 저장되는 파일 정보에서는 없어지지만
	//      서버측에는 이미지 파일이 남아있는 문제가 발생함.
	//      이를 해결하기 위한 매 시간 정각 마다 
	//      서버와 DB에서 최근 3일내에 저장된 파일 정보를 읽어와 비교하여
	//      서버 정보 중 DB정보와 다른 내용을 삭제함.
	
	//               초 분
	@Scheduled(cron="0 0 * * * *") // 초와 분이 0인 경우 == 매 시간 정각 (정시마다)
	public void deleteImageFile() {
		String savePath = servletContext.getRealPath("/resources/infoImages");
		File path = new File(savePath);
		
		// Server path 경로에 저장된 모든 파일을 얻어옴.
		File[] fileList = path.listFiles();
		
		// DB에서 3일이내 저장된 파일 정보를 조회해옴.
		List<String> dbFileList = boardService.selectDbFileList();

		
		// Server에 저장된 파일 중 생성된지 3일 이내 파일만을 읽어와 파일명을 serverFileList에 저장할 예정
		// -> 글 작성 중 미리 업로드된 이미지가 삭제되지 않게 하기 위하여 3일이라는 충분한 시간 간격을 줌.
		List<String> serverFileList = new ArrayList<String>();
		if(fileList.length > 0) {
			
			
			// 3일 전 날짜
			Date agoDate = new Date(System.currentTimeMillis() - (1000*60*60*24*3));
			
			// 날짜 데이터 형태 지정
			SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHH");
			
			// Server 경로에 저장된 파일 목록에 반복 접근
			for(File f : fileList) {
				
				String tmp = f.toString();
				
				// 파일명 앞 8글자를 잘라서 비교함.(년월일시)
				// 파일이 생성된 날짜가 3일 전 날짜 보다 크거나 같은 경우  == 파일 생성 날짜가 3일 이내인 경우 serverFileList에 추가
				if((tmp.substring(0,8)).compareTo(sdf.format(agoDate)) >= 0){
				// ex) "20091609".compareTo("20091410") >= 0  --> true
					serverFileList.add(tmp.substring(tmp.lastIndexOf('\\')+1));
				}
			}
			
			// serverFileList에서 dbFileList와 일치하는 값을 제거
			// -> 삭제 후 남아있는 값   == DB에서 조회되지 않은 값
			//    == 3일 내에 글 작성 시 실제 업로드된 파일이 아님.
			serverFileList.removeAll(dbFileList);
			
			// 삭제해야될 파일이 존재할 경우
			if(!serverFileList.isEmpty()) {
				// 서버에서 파일 삭제
				for(String fileName : serverFileList) {
					File tmp = new File(savePath + "/" + fileName);
					tmp.delete();
				}
				
				logger.info("******************************************************************************************************************************************" );
				logger.info("Server에 저장된 이미지 중 DB 저장 정보와 일치하지 않는 이미지 파일 삭제 완료" );
				logger.info("******************************************************************************************************************************************" );
			}
		}
		
		
		
		
	}
}
