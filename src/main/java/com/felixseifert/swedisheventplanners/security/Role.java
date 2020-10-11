package com.felixseifert.swedisheventplanners.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Role {

    ADMINISTRATION_MANAGER(ForAnnotation.ADMINISTRATION_MANAGER),
    CUSTOMER_SERVICE_OFFICER(ForAnnotation.CUSTOMER_SERVICE_OFFICER),
    SENIOR_CUSTOMER_SERVICE_OFFICER(ForAnnotation.SENIOR_CUSTOMER_SERVICE_OFFICER),
    SENIOR_HR_MANAGER(ForAnnotation.SENIOR_HR_MANAGER),
    HR_ASSISTANT(ForAnnotation.HR_ASSISTANT),
    HR_EMPLOYEE(ForAnnotation.HR_EMPLOYEE),
    MARKETING_OFFICER(ForAnnotation.MARKETING_OFFICER),
    MARKETING_ASSISTANT(ForAnnotation.MARKETING_ASSISTANT),
    MARKETING_EMPLOYEE(ForAnnotation.MARKETING_EMPLOYEE),
    FINANCIAL_MANAGER(ForAnnotation.FINANCIAL_MANAGER),
    ACCOUNTANT(ForAnnotation.ACCOUNTANT),
    PRODUCTION_MANAGER(ForAnnotation.PRODUCTION_MANAGER),
    PRODUCTION_SUB_TEAM(ForAnnotation.PRODUCTION_SUB_TEAM),
    SERVICES_MANAGER(ForAnnotation.SERVICES_MANAGER),
    SERVICES_SUB_TEAM(ForAnnotation.SERVICES_SUB_TEAM),
    VICE_PRESIDENT(ForAnnotation.VICE_PRESIDENT),
    SECRETARY(ForAnnotation.SECRETARY),
    CLIENT_VIEWER(ForAnnotation.CLIENT_VIEWER),
    EMPLOYEE_VIEWER(ForAnnotation.EMPLOYEE_VIEWER),
    STAFF_VIEWER(ForAnnotation.STAFF_VIEWER),
    CLIENT(ForAnnotation.CLIENT);

    @Getter
    private final String value;

    @Override
    public String toString() {
        return this.value;
    }


    public static class ForAnnotation {

        private static final String PREFIX = "ROLE_";

        private static final String ADMINISTRATION_MANAGER = "AdministrationManager";

        public static final String ADMINISTRATION_MANAGER_WITH_PREFIX = PREFIX + ADMINISTRATION_MANAGER;

        private static final String CUSTOMER_SERVICE_OFFICER = "CustomerServiceOfficer";

        public static final String CUSTOMER_SERVICE_OFFICER_WITH_PREFIX = PREFIX + CUSTOMER_SERVICE_OFFICER;

        private static final String SENIOR_CUSTOMER_SERVICE_OFFICER = "SeniorCustomerServiceOfficer";

        public static final String SENIOR_CUSTOMER_SERVICE_OFFICER_WITH_PREFIX = PREFIX + SENIOR_CUSTOMER_SERVICE_OFFICER;

        private static final String SENIOR_HR_MANAGER = "SeniorHRManager";

        public static final String SENIOR_HR_MANAGER_WITH_PREFIX = PREFIX + SENIOR_HR_MANAGER;

        private static final String HR_ASSISTANT = "HRAssistant";

        public static final String HR_ASSISTANT_WITH_PREFIX = PREFIX + HR_ASSISTANT;

        private static final String HR_EMPLOYEE = "HREmployee";

        public static final String HR_EMPLOYEE_WITH_PREFIX = PREFIX + HR_EMPLOYEE;

        private static final String MARKETING_OFFICER = "MarketingOfficer";

        public static final String MARKETING_OFFICER_WITH_PREFIX = PREFIX + MARKETING_OFFICER;

        private static final String MARKETING_ASSISTANT = "MarketingAssistant";

        public static final String MARKETING_ASSISTANT_WITH_PREFIX = PREFIX + MARKETING_ASSISTANT;

        private static final String MARKETING_EMPLOYEE = "MarketingEmployee";

        public static final String MARKETING_EMPLOYEE_WITH_PREFIX = PREFIX + MARKETING_EMPLOYEE;

        private static final String FINANCIAL_MANAGER = "FinancialManager";

        public static final String FINANCIAL_MANAGER_WITH_PREFIX = PREFIX + FINANCIAL_MANAGER;

        private static final String ACCOUNTANT = "Accountant";

        public static final String ACCOUNTANT_WITH_PREFIX = PREFIX + ACCOUNTANT;

        private static final String PRODUCTION_MANAGER = "ProductionManager";

        public static final String PRODUCTION_MANAGER_WITH_PREFIX = PREFIX + PRODUCTION_MANAGER;

        private static final String PRODUCTION_SUB_TEAM = "ProductionSubTeam";

        public static final String PRODUCTION_SUB_TEAM_WITH_PREFIX = PREFIX + PRODUCTION_SUB_TEAM;

        private static final String SERVICES_MANAGER = "ServicesManager";

        public static final String SERVICES_MANAGER_WITH_PREFIX = PREFIX + SERVICES_MANAGER;

        private static final String SERVICES_SUB_TEAM = "ServicesSubTeam";

        public static final String SERVICES_SUB_TEAM_WITH_PREFIX = PREFIX + SERVICES_SUB_TEAM;

        private static final String VICE_PRESIDENT = "VicePresident";

        public static final String VICE_PRESIDENT_WITH_PREFIX = PREFIX + VICE_PRESIDENT;

        private static final String SECRETARY = "Secretary";

        public static final String SECRETARY_WITH_PREFIX = PREFIX + SECRETARY;

        private static final String CLIENT_VIEWER = "ClientViewer";

        public static final String CLIENT_VIEWER_WITH_PREFIX = PREFIX + CLIENT_VIEWER;

        private static final String EMPLOYEE_VIEWER = "EmployeeViewer";

        public static final String EMPLOYEE_VIEWER_WITH_PREFIX = PREFIX + EMPLOYEE_VIEWER;

        private static final String STAFF_VIEWER = "StaffViewer";

        public static final String STAFF_VIEWER_WITH_PREFIX = PREFIX + STAFF_VIEWER;

        private static final String CLIENT = "Client";

        public static final String CLIENT_WITH_PREFIX = PREFIX + CLIENT;
    }
}
