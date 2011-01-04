package rs.ac.bg.rcub.ams.config.hgsm;

public class HGSMSite {

	private String name;
	private String goc;
	private String giis;
	private String ce;
	private String country;
	private String affiliation;
	private String email;
	private String comments;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGoc() {
		return goc;
	}

	public void setGoc(String goc) {
		this.goc = goc;
	}

	public String getGiis() {
		return giis;
	}

	public void setGiis(String giis) {
		this.giis = giis;
	}

	public String getCe() {
		return ce;
	}

	public void setCe(String ce) {
		this.ce = ce;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getAffiliation() {
		return affiliation;
	}

	public void setAffiliation(String affiliation) {
		this.affiliation = affiliation;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(name: ");
		sb.append(name);
		sb.append(", aff: ");
		sb.append(affiliation);
		sb.append(")");
		return sb.toString();
	}

}
