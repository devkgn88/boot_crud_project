package com.gn.crudproject.service;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.gn.crudproject.dto.UploadFileDto;
import com.gn.crudproject.entity.UploadFile;
import com.gn.crudproject.repository.UploadFileRepository;

@Service
public class UploadFileService {
	
	@Autowired
	private UploadFileRepository fileRepository;
	
	@Value("${ffupload.location}")
	private String fileDir;
	
	
	public List<UploadFile> list(Long article_id){
		return fileRepository.findAllByArticleId(article_id);
	}
	
	public void delete(Long file_id) {
		
	}
	
	public UploadFileDto uploadFile(MultipartFile file) {
		UploadFileDto dto = new UploadFileDto();
		try {
			// 1. 정상 파일 여부 확인
			if(file == null || file.isEmpty()) {
				throw new Exception("존재하지 않는 파일입니다.");
			}
			// 2. 파일 최대 용량 체크
			long fileSize = file.getSize();
			if( fileSize <= 0 || 5242880 < fileSize) {
				throw new Exception("파일 크기 : "+fileSize+"(파일 사이즈가 5MB를 넘습니다.)");
			}
			// 3. 파일 최초 이름
			String oriName = file.getOriginalFilename();
			dto.setOri_name(oriName);
			// 4. 파일 확장자 자르기
			String fileExt
				= oriName.substring(oriName.lastIndexOf("."),oriName.length());
			// 5. 파일 명칭 바꾸기
			UUID uuid = UUID.randomUUID();
			// 6. 8자리마다 포함되는 하이픈 제거
			String uniqueName = uuid.toString().replaceAll("-", "");
			// 7. 새로운 파일명
			String newName = uniqueName+fileExt;
			dto.setNew_name(newName);
			// 8. 파일 저장 경로 설정(article외에 다른 파일 전달할 경우 다른 경로 쓸 수있음)
			// 9. 파일 껌데기 생성
			String downDir = fileDir+"article/"+newName;
			dto.setFile_dir(downDir);
			File saveFile = new File(downDir);
			// 10. 경로 존재 여부 확인
			if(!saveFile.exists()) {
				saveFile.mkdirs();
			}
			// 11. 껍데기에 파일 정보 복제
			file.transferTo(saveFile);
			
		}catch(Exception e) {
			dto = null; 
			e.printStackTrace();
		}
		return dto;
	}

	
}
