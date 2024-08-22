

package com.zoho.detective.agent;

import com.adventnet.ds.query.Column;
import com.adventnet.ds.query.SelectQuery;
import com.adventnet.ds.query.SelectQueryImpl;
import com.adventnet.ds.query.Table;
import com.adventnet.iam.AccountMember;
import com.adventnet.iam.AppAccount;
import com.adventnet.iam.Org;
import com.adventnet.iam.User;
import com.adventnet.persistence.DataObject;
import com.adventnet.persistence.Row;
import com.adventnet.sas.ds.SASThreadLocal;
import com.adventnet.sas.ds.SASUserScopingEngine;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.me.devicemanagement.cloud.common.authentication.DMCloudThreadLocal;
import com.me.devicemanagement.cloud.common.iam.IAMCommunicator;
import com.me.devicemanagement.cloud.common.threadlocal.DMCloudThreadLocalFlowSwitch;
import com.me.devicemanagement.cloud.common.util.SyMUtil;
import com.me.devicemanagement.cloud.server.factory.ApiCloudFactoryProvider;
import com.me.devicemanagement.cloud.server.iam.DMIAMUtil;
import com.me.devicemanagement.cloud.server.iam.DMIAMUtilService;
import com.me.devicemanagement.cloud.server.iam.DMMultiAppAccountUtil;
import com.me.devicemanagement.framework.server.customer.CustomerInfoThreadLocal;
import com.me.devicemanagement.framework.server.customer.CustomerInfoUtil;
import com.zoho.accounts.AccountsProto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;

public class ECDetectiveHandlerImpl extends DetectiveHandler {
    private static final String[] TABLES = new String[]{"DMCloudLicenseDetails", "DCCloudLicenseDetails", "SoMParams", "SystemParams", "DCServerInfo", "Resource", "ManagedDomain",
            "ClientMetaData", "MetaDataParams", "MasterRepository", "MasterMetaDataParams", "DSComponentDetails", "DFSFileDetails", "DCServerIPDetected", "MasterAgentVersionInfo",
            "AaaUser", "AaaLogin", "AaaContactInfo", "AaaUserContactInfo", "DCSuperAdmin", "OrgDetails", "CustomerInfo", "ReplicationPolicyDetails", "ComputerLimitToCustomerMapping",
            "AaaCloudUsers", "MacNotificationWindow", "MDMEnrollmentSettings", "CustomerParams", "NotificationTemplate", "AgentCommonSettings", "BranchOfficeDetails", "BranchOffProxyDetails",
            "AgentServerCommunicationDtl", "BranchAgentDetails", "RCPerformanceSettings", "BranchOfficeStatusForOSD", "PolicyToBranchOfficeRel", "LinuxAgentSettings", "AgentMstConfig"};
    private static final Map<String, String> STORE = new HashMap();
    private static final Map<Long, Integer> TEST_ORG = new HashMap();
    private static final Set<String> CREATED_ORG = new CopyOnWriteArraySet();

    public ECDetectiveHandlerImpl() {
    }

    public void delete(String s) {
        STORE.remove(s);
    }

    public String getOrgId(HttpServletRequest httpServletRequest, FilterConfig filterConfig) throws Exception {
        if (this.isGetTestUser()) {
            return String.valueOf(DMIAMUtil.getZaaid());
        } else {
            String requestUri = httpServletRequest.getRequestURI();
            if (requestUri.contains("/dcapi/org/createOrg")) {
                return null;
            } else if (!requestUri.contains("/dcapi/uac/userMeta") && !requestUri.contains("/dcapi/productMeta") && !requestUri.contains("/dcapi/org/productEditionDetails") && !requestUri.contains("/dcapi/org/orgDetail")) {
                String zaaid = DMIAMUtil.getZaaid();
                return zaaid != null && zaaid.matches("\\d*") ? zaaid : "-1";
            } else {
                return "-1";
            }
        }
    }

    public String getEmailId(HttpServletRequest httpServletRequest) throws Exception {
        User user = DMIAMUtil.getCurrentUser();
        return user != null ? user.getPrimaryEmail() : "";
    }

    public DataObject getDataObjectFromSpace(SelectQuery selectQuery, String schema, HttpServletRequest httpServletRequest) throws Exception {
        this.setThreadLocals();
        return SyMUtil.getPersistence().get(selectQuery);
    }

    public Map<String, List<JsonNode>> getTableRowsForOrgCreation(String s) throws Exception {
        Map<String, List<JsonNode>> data = new HashMap();
        this.setThreadLocals();
        String[] var3 = TABLES;
        int var4 = var3.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            String tableName = var3[var5];
            SelectQuery selectQuery = new SelectQueryImpl(Table.getTable(tableName));
            selectQuery.addSelectColumn(Column.getColumn(tableName, "*"));
            DataObject dataInTable = SyMUtil.getPersistence().get(selectQuery);
            List<JsonNode> jsonNodes = new ArrayList();
            Iterator it = dataInTable.getRows(tableName);

            while(it.hasNext()) {
                Row row = (Row)it.next();
                jsonNodes.add(toJsonNode(row));
            }

            if (!jsonNodes.isEmpty()) {
                data.put(tableName, jsonNodes);
            }
        }

        return data;
    }

    private static JsonNode toJsonNode(Row row) {
        ObjectNode objectNode = (new JsonNodeFactory(false)).objectNode();
        Iterator var2 = row.getColumns().iterator();

        while(var2.hasNext()) {
            Object col = var2.next();
            String colName = (String)col;
            objectNode.put(colName.toLowerCase(), String.valueOf(row.get(colName)));
        }

        return objectNode;
    }

    public Long retrieveOrgIdFromTheRestResponse(JsonNode jsonNode, byte[] bytes) throws Exception {
        if (this.isGetTestUser()) {
            return Long.valueOf(DMIAMUtil.getZaaid());
        } else {
            this.setThreadLocals();
            return Long.valueOf(DMIAMUtil.getZaaid());
        }
    }

    public String retrieveOrgNameFromTheRestResponse(JsonNode jsonNode, byte[] bytes) throws Exception {
        return DMIAMUtil.getCurrentUserOrg().getOrgName();
    }

    public int getSasRangeForOrg(String s) throws Exception {
        this.setThreadLocals();
        long startId = SASUserScopingEngine.getStartId(SASThreadLocal.getUserId());
        return (int)Math.floor((double)startId / Math.pow(10.0, 12.0));
    }

    public void markAsTestOrg(Long aLong, int i) throws Exception {
        TEST_ORG.put(aLong, i);
    }

    public boolean isDevelopmentEnvironment() {
        return true;
    }

    public boolean isLocalEnvironment() {
        return false;
    }

    public boolean isTestOrg(Long aLong, int i) {
        return TEST_ORG.getOrDefault(aLong, Integer.MIN_VALUE) == i;
//        return true;
        //   STORE.put("REC_25239865","true")
        //   TEST_ORG.put(25239865L,0)
    }

    public String getAppName(int appSource) {
        return "EndpointCentral";
    }

    public void canJobRecord(long jobId, String zsid) {
        if (zsid != null && zsid.matches("\\d*")) {
            this.setter("JOB_" + jobId + "_" + zsid, Boolean.TRUE.toString());
        } else {
            this.setter("JOB_" + jobId + "_" + zsid, Boolean.FALSE.toString());
        }

    }

    public void setter(String s, String o) {
        STORE.put(s, o);
    }

    public boolean isJobRecord(long jobId, String zsid) {
        String value = this.getter("JOB_" + jobId + "_" + zsid);
        return value != null && value.equals(Boolean.TRUE.toString());
    }

    public String getter(String s) {
        return (String)STORE.get(s);
    }

    public boolean isPostTestUser(Request request) {
        HashSet<String> set = new HashSet();

        for(int i = 0; i < 10; ++i) {
            set.add("sathish.nj+te" + i + "@zohotest.com");
            set.add("hari.babu+tce" + i + "@zohotest.com");
            set.add("ruthna.s+post" + i + "@zohotest.com");
        }

        set.add("ruthna.s+te1@zohotest.com");
        set.add("balaji.kannank+10@zohotest.com");
        set.add("mahesh.vj+1@zohotest.com");
        set.add("manigandan.mr+01dc@zohotest.com");
        set.add("venkatesh.mn+36@zohotest.com");
        User user = DMIAMUtil.getCurrentUser();
        return set.contains(user != null ? user.getPrimaryEmail() : "");
    }

    public boolean isGetTestUser() {
        HashSet<String> set = new HashSet();

        for(int i = 0; i < 10; ++i) {
            set.add("sathish.nj+t" + i + "@zohotest.com");
            set.add("ruthna.s+get" + i + "@zohotest.com");
        }

        set.add("ruthna.s+1@zohotest.com");
        set.add("venkatesh.mn+31@zohotest.com");
        set.add("balaji.kannank+8@zohotest.com");
        set.add("mahesh.vj+7@zohotest.com");
        set.add("bala.arun+d0@zohotest.com");
        set.add("pavithra.siva+87@zohotest.com");
        User user = DMIAMUtil.getCurrentUser();
        return set.contains(user != null ? user.getPrimaryEmail() : "");
    }

    private void setThreadLocals() throws Exception {
        User user = DMIAMUtil.getCurrentUser();
        if (user != null) {
            Org org = DMIAMUtil.getCurrentUserOrg();
            AppAccount appAccount = null;
            String zaid;
            if (org != null) {
                zaid = String.valueOf(user.getZOID());
                appAccount = IAMCommunicator.getAppAccFromCache(zaid);
                if (appAccount == null) {
                    appAccount = IAMCommunicator.setAppAccToCache(zaid);
                }

                if (appAccount == null && DMMultiAppAccountUtil.isMultiAppAccountSupported()) {
                    List<AppAccount> appAccounts = DMMultiAppAccountUtil.getAppAccountsForOrg(Long.valueOf(zaid));
                    appAccounts.stream().filter((appacc) -> {
                        return IAMCommunicator.getAppAccMemberFromCache(String.valueOf(appacc.getZaaid()), String.valueOf(user.getZuid())) != null;
                    }).findAny();
                }
            }

            zaid = null;
            String zaaid = appAccount.getZaaid();
            String zuid = String.valueOf(user.getZUID());
            SASThreadLocal.setThreadLocalForDomain(zaaid);
            AccountMember appAccMember = IAMCommunicator.getAppAccMemberFromCache(zaaid, zuid);
            if (appAccMember == null) {
                IAMCommunicator.setAppAccMemberToCache(zaaid, zuid);
            }

            if (DMIAMUtil.getZaaid() == null || DMCloudThreadLocalFlowSwitch.isRedundantSetAllowed()) {
                DMIAMUtil.setZaaid(zaaid);
            }

            if (zaaid != null && !zaaid.isEmpty()) {
                String productName = ApiCloudFactoryProvider.getApplicationAPI().getCustProductName();
                String productCode = DMIAMUtil.getProductCodeFromDB();
                DMCloudThreadLocal.setCustomerProduct(productName);
                DMCloudThreadLocal.setCustomerProductCode(productCode);
                if (!CustomerInfoUtil.getInstance().isMSP()) {
                    Long customerId = DMIAMUtilService.getDBCustomerID();
                    if (customerId != null) {
                        CustomerInfoThreadLocal.setCustomerId(customerId + "");
                    }
                }
            }
        }

    }
}
