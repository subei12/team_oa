package top.jsls9.oajsfx.hlxPojo;

/** 用户获取key
 * @author bSu
 * @date 2021/5/6 - 21:47
 */
public class GetKeyRootBean {

        private String msg;
        private String _key;
        private User user;
        private String session_key;
        private int checkstatus;
        private int qqinfostatus;
        private int status;
        public void setMsg(String msg) {
            this.msg = msg;
        }
        public String getMsg() {
            return msg;
        }

        public void set_key(String _key) {
            this._key = _key;
        }
        public String get_key() {
            return _key;
        }

        public void setUser(User user) {
            this.user = user;
        }
        public User getUser() {
            return user;
        }

        public void setSession_key(String session_key) {
            this.session_key = session_key;
        }
        public String getSession_key() {
            return session_key;
        }

        public void setCheckstatus(int checkstatus) {
            this.checkstatus = checkstatus;
        }
        public int getCheckstatus() {
            return checkstatus;
        }

        public void setQqinfostatus(int qqinfostatus) {
            this.qqinfostatus = qqinfostatus;
        }
        public int getQqinfostatus() {
            return qqinfostatus;
        }

        public void setStatus(int status) {
            this.status = status;
        }
        public int getStatus() {
            return status;
        }


    }
