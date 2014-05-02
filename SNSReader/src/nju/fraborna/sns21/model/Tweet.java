package nju.fraborna.sns21.model;

public class Tweet {

	public static final String ID = "_id";
	public static final String USERID = "userId";
	public static final String HEADIMAGE = "headimage";
	public static final String USERNBME = "username";
	public static final String TIMESTAMP = "timestamp";
	public static final String FROM = "source";
	public static final String CONTENT = "content";
	public static final String ZANNUM = "zanNum";
	public static final String COMMENTNUM = "commentNum";
	public static final String FORWARDNUM = "forwardNum";

	private String id;
	private String userId;
	private String headImage;
	private String userName;
	private String timaStamp;
	private String from;
	private String content;
	private int zanNum;
	private int commentNum;
	private int forwardNum;

	public Tweet(String id, String userId, String headImage, String userName,
			String timaStamp, String from, String content, int zanNum,
			int commentNum, int forwardNum) {
		super();
		this.id = id;
		this.userId = userId;
		this.headImage = headImage;
		this.userName = userName;
		this.timaStamp = timaStamp;
		this.from = from;
		this.content = content;
		this.zanNum = zanNum;
		this.commentNum = commentNum;
		this.forwardNum = forwardNum;
	}

	public String getId() {
		return id;
	}

	public String getUserId() {
		return userId;
	}

	public String getHeadImage() {
		return headImage;
	}

	public String getUserName() {
		return userName;
	}

	public String getTimaStamp() {
		return timaStamp;
	}

	public String getFrom() {
		return from;
	}

	public String getContent() {
		return content;
	}

	public int getZanNum() {
		return zanNum;
	}

	public int getCommentNum() {
		return commentNum;
	}

	public int getForwardNum() {
		return forwardNum;
	}

}
