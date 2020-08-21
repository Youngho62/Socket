package object;
import java.io.Serializable;

public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	private String userName;
	private int age;
	private String password;
	
	public User(String userName, int age, String password) {
		this.userName = userName;
		this.age = age;
		this.password = password;
	}

	@Override
	public String toString() {
		return "User [userName=" + userName + ", age=" + age + ", password=" + password + "]";
	}
	
}
