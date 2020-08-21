package object;

import java.io.Serializable;

public class FileObj implements Serializable {

	private String fileName;
	private long size;
	private String uuid;
	
	public FileObj(String fileName, long size, String uuid) {
		super();
		this.fileName = fileName;
		this.size = size;
		this.uuid = uuid;
	}

	@Override
	public String toString() {
		return "File [fileName=" + fileName + ", size=" + size + ", uuid=" + uuid + "]";
	}
	
	
}
