package object;

import java.io.Serializable;
/**
 * 
 * <pre>
 * Class Name : Header
 * Description : 데이터를 전송하기 전 서버에 보내는 헤더
 * Supplements : Created in 2020. 8. 21
 *
 * Modification Information
 *
 * Date          By               Description
 * ------------- -----------      ----------------------------------------------
 * 2020. 8. 21  Yeongho        First Commit.
 *
 * @since 2020
 * @version v1.0
 * @author Yeongho
 *
 * Copyright (c) ABrain.  All rights reserved.
 * </pre>
 */
public class Header implements Serializable{
	private long dataSize;
	private DataType dataType;
	
	private String filePath;
	private String fileName;
	private String fileType;
	
	public Header(long dataSize, DataType dataType) {
		super();
		this.dataSize = dataSize;
		this.dataType = dataType;
	}
	

	public Header(long dataSize, DataType dataType, String filePath, String fileName, String fileType) {
		super();
		this.dataSize = dataSize;
		this.dataType = dataType;
		this.filePath = filePath;
		this.fileName = fileName;
		this.fileType = fileType;
	}

	public String getFilePath() {
		return filePath;
	}

	public long getDataSize() {
		return dataSize;
	}

	public DataType getDataType() {
		return dataType;
	}
	public String getFileName() {
		return fileName;
	}
	public String getFileType() {
		return fileType;
	}

	@Override
	public String toString() {
		return "Header [dataSize=" + dataSize + ", dataType=" + dataType + ", filePath=" + filePath + ", fileName="
				+ fileName + ", fileType=" + fileType + "]";
	}


}
