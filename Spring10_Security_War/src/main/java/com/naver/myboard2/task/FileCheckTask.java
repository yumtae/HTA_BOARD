package com.naver.myboard2.task;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.naver.myboard2.domain.MySaveFolder;
import com.naver.myboard2.service.BoardService;

@Service
@EnableScheduling
@Configuration 
public class FileCheckTask {

	
	private static final Logger logger = LoggerFactory.getLogger(FileCheckTask.class);
	
	@Autowired
	private MySaveFolder mysavefolder;
	
	
	@Autowired
	private BoardService boardservice;
	public FileCheckTask(BoardService boardService, MySaveFolder mysavefolder) {
		this.boardservice=boardService;
		this.mysavefolder=mysavefolder;
		
	}
	

	public void test() throws Exception{
		logger.info("test");	
	}
	
	

	@Scheduled(cron = " 0 16 * * * *")
	public void checkFiles() throws Exception{
		String saveFolder = mysavefolder.getSavefolder();
		logger.info("checkFiles");
		
		List<String> deleteFileList = boardservice.getDeleteFileList();
		
		// for(String filename : deleteFileList){
		for (int i = 0; i< deleteFileList.size() ; i++) {
			String filename = deleteFileList.get(i);
			
			File file = new File(saveFolder+filename);
			if(file.exists()) {
				if(file.delete()) {
					logger.info(file.getPath() + "삭제되었습니다");
					boardservice.deleteFileList(filename);
				}
				
			}else {
				logger.info(file.getPath() + "삭제실패.");
				
			}
			
			
		}
		
		
		
	}
	
	
	
	
	
	
	
}
