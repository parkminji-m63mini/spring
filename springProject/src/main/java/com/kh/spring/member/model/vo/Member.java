package com.kh.spring.member.model.vo;

import java.sql.Date;

public class Member {
	
	private int memberNo;
	private String memberId;
	private String memberPwd;
	private String memberName;
	private String memberPhone;
	private String memberEmail;
	private String memberAddr;
	private String memberInterest;
	private Date memberEnrollDate;
	private String memberStatus;
	private String memberGrade;
	
	public Member() {
		// TODO Auto-generated constructor stub
	}

	public Member(int memberNo, String memberId, String memberPwd, String memberName, String memberPhone,
			String memberEmail, String memberAddr, String memberInterest, Date memberEnrollDate, String memberStatus,
			String memberGrade) {
		super();
		this.memberNo = memberNo;
		this.memberId = memberId;
		this.memberPwd = memberPwd;
		this.memberName = memberName;
		this.memberPhone = memberPhone;
		this.memberEmail = memberEmail;
		this.memberAddr = memberAddr;
		this.memberInterest = memberInterest;
		this.memberEnrollDate = memberEnrollDate;
		this.memberStatus = memberStatus;
		this.memberGrade = memberGrade;
	}

	public int getMemberNo() {
		return memberNo;
	}

	public void setMemberNo(int memberNo) {
		this.memberNo = memberNo;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getMemberPwd() {
		return memberPwd;
	}

	public void setMemberPwd(String memberPwd) {
		this.memberPwd = memberPwd;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getMemberPhone() {
		return memberPhone;
	}

	public void setMemberPhone(String memberPhone) {
		this.memberPhone = memberPhone;
	}

	public String getMemberEmail() {
		return memberEmail;
	}

	public void setMemberEmail(String memberEmail) {
		this.memberEmail = memberEmail;
	}

	public String getMemberAddr() {
		return memberAddr;
	}

	public void setMemberAddr(String memberAddr) {
		this.memberAddr = memberAddr;
	}

	public String getMemberInterest() {
		return memberInterest;
	}

	public void setMemberInterest(String memberInterest) {
		this.memberInterest = memberInterest;
	}

	public Date getMemberEnrollDate() {
		return memberEnrollDate;
	}

	public void setMemberEnrollDate(Date memberEnrollDate) {
		this.memberEnrollDate = memberEnrollDate;
	}

	public String getMemberStatus() {
		return memberStatus;
	}

	public void setMemberStatus(String memberStatus) {
		this.memberStatus = memberStatus;
	}

	public String getMemberGrade() {
		return memberGrade;
	}

	public void setMemberGrade(String memberGrade) {
		this.memberGrade = memberGrade;
	}

	@Override
	public String toString() {
		return "Member [memberNo=" + memberNo + ", memberId=" + memberId + ", memberPwd=" + memberPwd + ", memberName="
				+ memberName + ", memberPhone=" + memberPhone + ", memberEmail=" + memberEmail + ", memberAddr="
				+ memberAddr + ", memberInterest=" + memberInterest + ", memberEnrollDate=" + memberEnrollDate
				+ ", memberStatus=" + memberStatus + ", memberGrade=" + memberGrade + "]";
	}
	
	

}
