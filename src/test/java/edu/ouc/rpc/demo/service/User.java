package edu.ouc.rpc.demo.service;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试用的自定义业务类型
 * 
 * @author wqx
 *
 */
public class User implements java.io.Serializable{
	
	private static final long serialVersionUID = 493399440916323966L;

	private Integer id;
	
	private String name;
	
	private List<User> childs;

	
	public void addChild(User child){
		if(childs == null){
			childs = new ArrayList<User>();
		}
		childs.add(child);
	}
	public User(int id, String name){
		this.id = id;
		this.name = name;
	}
	public User(int id, String name, List<User> childs){
		this.id = id;
		this.name = name;
		this.childs = childs;
	}
	
	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<User> getChilds() {
		return childs;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setChilds(List<User> childs) {
		this.childs = childs;
	}
	
	
}
