package recoveryportal.adityagupta.recoveryportal.Data;

import java.util.Date;

/**
 * Created by adityagupta on 15/11/17.
 */

public class SearchResults {

    public String status, id, customerName, vehicleModel, loanNo, engineNo, regNo, chassisNo, bucket, principalOS, emiAmount, bounceChargeDue, lppDue, firstEmiueDate, loanEndDate, branch, client;
    public Date time;

    public SearchResults(Date time, String status, String id, String customerName, String vehicleModel, String loanNo, String engineNo, String regNo, String chassisNo, String bucket, String principalOS, String emiAmount, String bounceChargeDue, String lppDue, String firstEmiueDate, String loanEndDate, String branch, String client) {
        this.id = id;
        this.customerName = customerName;
        this.vehicleModel = vehicleModel;
        this.loanNo = loanNo;
        this.engineNo = engineNo;
        this.regNo = regNo;
        this.chassisNo = chassisNo;
        this.bucket = bucket;
        this.principalOS = principalOS;
        this.emiAmount = emiAmount;
        this.bounceChargeDue = bounceChargeDue;
        this.lppDue = lppDue;
        this.firstEmiueDate = firstEmiueDate;
        this.loanEndDate = loanEndDate;
        this.branch = branch;
        this.client = client;
        this.status = status;
        this.time = time;
    }
}
