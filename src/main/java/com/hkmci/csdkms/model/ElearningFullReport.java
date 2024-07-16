package com.hkmci.csdkms.model;

import java.util.List;

import com.hkmci.csdkms.entity.ElearningReport;
import com.hkmci.csdkms.entity.ElearningReportQuizRecord;

public class ElearningFullReport {
    private List<ElearningReportQuizRecord> reportQuizRecord;
    private List<ElearningReport> report;
    private Integer score;
    private Integer totalScore;
    private Integer percentage;

    public List<ElearningReportQuizRecord> getReportQuizRecord() {
        return reportQuizRecord;
    }

    public void setReportQuizRecord(List<ElearningReportQuizRecord> reportQuizRecord) {
        this.reportQuizRecord = reportQuizRecord;
    }

    public List<ElearningReport> getReport() {
        return report;
    }

    public void setReport(List<ElearningReport> report) {
        this.report = report;
    }
    
    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }

    public Integer getPercentage() {
        return percentage;
    }

    public void setPercentage(Integer percentage) {
        this.percentage = percentage;
    }
}