package onenet.DevOperation.constant;


public enum OperateModule {
    DevparaSetting(0), AppUpdating(1), OrgSetting(2), SysSettings(3);
    private int text;
 
    OperateModule(int text) {
        this.text = text;
    }
 
    public int getText() {
        return text;
    }
}
