package top.ashman.wx.domain.model.message;

import java.util.Random;

/**
 * @author singoasher
 * @date 2018/2/8
 */
public enum ReplyMessage {
    /**
     * Hello
     */
    HELLO("你好/HELLO") {
        @Override
        public String normalReply() {
            String[] replyArray = {"你好", "[Smirk]", "Hi"};
            return replyArray[new Random().nextInt(replyArray.length)] +
                    "\n\n【回复 >>> " + HELP.keyword() + " <<< 获取帮助】";
        }

        @Override
        public String abnormalReply() {
            String[] replyArray = {"不好不好", "是么", "一点都不好"};
            return replyArray[new Random().nextInt(replyArray.length)];
        }
    }, HELP("帮助") {
        @Override
        public String normalReply() {
            return "[Smirk]\n" +
                    "【关键字不区分大小写】\n" +
                    "【关键字与参数间的空格并非必须】\n" +
                    "\n" +
                    "【" + CREATE_TASK.keyword() + " 任务内容】创建任务\n" +
                    "【" + LIST_TASK.keyword() + "】任务列表\n" +
                    "【" + LIST_EXPIRED_TASK.keyword() + "】过期任务列表\n" +
                    "【" + REMOVE_ALL_TASK.keyword() + "】清空任务\n" +
                    "【" + REMOVE_EXPIRED_TASK.keyword() + "】删除所有过期任务\n" +
                    "【" + ACTIVE_TASK.keyword() + "】激活所有过期任务\n" +
                    "\n" +
                    "【回复 >>> " + HELP.keyword() + " <<< 获取帮助】";
        }

        @Override
        public String abnormalReply() {
            return null;
        }
    }, CREATE_TASK("创建/C") {
        @Override
        public String normalReply() {
            return "任务正在后台创建 ... ...";
        }

        @Override
        public String abnormalReply() {
            return "请输入任务内容";
        }
    }, LIST_TASK("L") {
        @Override
        public String normalReply() {
            return null;
        }

        @Override
        public String abnormalReply() {
            return "未发现任务";
        }
    }, LIST_EXPIRED_TASK("LE") {
        @Override
        public String normalReply() {
            return null;
        }

        @Override
        public String abnormalReply() {
            return "功能构建中 ... ...";
        }
    }, REMOVE_ALL_TASK("R") {
        @Override
        public String normalReply() {
            return "任务删除中 ... ...";
        }

        @Override
        public String abnormalReply() {
            return "功能构建中 ... ...";
        }
    }, REMOVE_EXPIRED_TASK("RE") {
        @Override
        public String normalReply() {
            return "过期任务删除中 ... ...";
        }

        @Override
        public String abnormalReply() {
            return null;
        }
    }, ACTIVE_TASK("A") {
        @Override
        public String normalReply() {
            return null;
        }

        @Override
        public String abnormalReply() {
            return null;
        }
    };

    private String keyword;

    ReplyMessage(String keyword) {
        this.keyword = keyword;
    }

    public abstract String normalReply();
    public abstract String abnormalReply();

    public String keyword() {
        return keyword;
    }

    public String[] keywords() {
        return keyword.split("/");
    }
}
