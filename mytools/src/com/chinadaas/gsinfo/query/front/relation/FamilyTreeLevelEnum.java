package com.chinadaas.gsinfo.query.front.relation;

public enum FamilyTreeLevelEnum {
	levelNagativeOne("-1"), levelZero("0"), levelOne("1"), levelTwo("2");
	private String level;

	FamilyTreeLevelEnum(String leveLValue) {
		this.level = leveLValue;
	}

	public int getLevel() {
		return Integer.parseInt(this.level);
	}
}
