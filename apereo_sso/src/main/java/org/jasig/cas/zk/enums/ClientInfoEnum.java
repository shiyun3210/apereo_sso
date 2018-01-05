package org.jasig.cas.zk.enums;

public enum ClientInfoEnum {
	
	gyluc(1001,"gyl",1),
	gportal(1002,"gp",1),
	kjuc(1003,"kj",2),
	kportal(1004,"kp",2),
	gylwx(1005,"gylwx",1);
	
	
	private int id;
	private String name;
	private int type;
	
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public int getType() {
		return type;
	}

	private ClientInfoEnum(int id,String name,int type) {
		this.id = id;
		this.name = name;
		this.type = type;
	}
	
	public static ClientInfoEnum getInstance(int id){
		for(ClientInfoEnum client:ClientInfoEnum.values()){
			if(client.getId()==id){
				return client;
			}
		}
		return null;
	}
	
}
