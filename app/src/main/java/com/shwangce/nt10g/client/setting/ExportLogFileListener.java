package com.shwangce.nt10g.client.setting;

public interface ExportLogFileListener {
    void onFileDataWriteComplete(String info);
    void onFileDataWriteError(String errorInfo);
}
