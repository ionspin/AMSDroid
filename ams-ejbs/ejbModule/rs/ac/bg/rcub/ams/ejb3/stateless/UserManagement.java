package rs.ac.bg.rcub.ams.ejb3.stateless;

import java.util.List;

import javax.ejb.Local;

import rs.ac.bg.rcub.ams.ejb3.entity.Alert;
import rs.ac.bg.rcub.ams.ejb3.entity.Category;
import rs.ac.bg.rcub.ams.ejb3.entity.Message;
import rs.ac.bg.rcub.ams.ejb3.entity.Subscription;
import rs.ac.bg.rcub.ams.ejb3.entity.UserInfo;
import rs.ac.bg.rcub.ams.ejb3.entity.Target;
import rs.ac.bg.rcub.ams.util.enums.MessageType;

@Local
public interface UserManagement {

	public void addSub(String target, String category, String contact, long dispatchTimeout, int countThreshold,
			boolean ignoreIfLastOK, MessageType type);

	public void removeSub(int subID);

	public List<Subscription> listSubs();

	public List<Target> listAllTargets();

	public List<Category> listAllCategories();

	public List<UserInfo> listAllSenders();

	public List<Alert> listAllAlerts();

	public long totalAlerts();

	public long totalAlerts(String sender, String target, String category, long timeOffset);

	public List<Alert> listAlerts(int start, int offset);

	public List<Alert> listAlerts(int start, int offset, String sender, String target, String category, long timeOffset);

	public List<Message> listMessages(Alert a);

}
