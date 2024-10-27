//package com.funix.fx21990.asm4.model;
//
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class testAccount {
//    //testLoanAccountIsAccepted
//
//
//    //TestSavingAcocunt
////    @Test
////    public void testSavingAccountIsAccepted() {
////        SavingsAccount account = new SavingsAccount();
////        account.setBalance(1000000);
////        boolean result = account.isAccepted(1000000);
////        assertEquals(true, result);
////    }
//
//
//    //CustomerById
////    @Test
////    public void testgetCustomerByid() {
////        DigitalBank digitalBank = new DigitalBank();
////        Customer customer = new Customer(a);
////        customer.setCustomerId("123456789012");
////        digitalBank.addCustomer(customer);
////        Customer customerById = digitalBank.getCustomerById("123456789011");
//////        Customer customerById1 = digitalBank.getCustomerById("123456789012");
////        assertNotNull(customerById);
////        assertEquals(customer, customerById);
//    }
//
//    //boolean CustomerExisted
//    @Test
//    public void customerExisted() {
//        Bank bank = new Bank();
//        Customer customer = new Customer();
//        customer.setCustomerId("123456789012");
//        bank.addCustomer(customer);
////       //kiem so cccd có trung hay không
//        boolean customerid1 = bank.isCustomerExisted("123456789011");
//        assertEquals(true, customerid1);
//    }
//    ///•	boolean isCustomerPremium()
//    @Test
//    void testIsCustomerNotPremium() {
//        // Create accounts
//        Account account1 = new Account("123456", 100000);
//        Account account2 = new Account("123455", 100000);
//        Customer customer = new Customer();
//        //   customer.addAccount(account1);
//        customer.addAccount(account2);
//        // Test the isPremium  = Normal or Premium
//        String result = customer.isPremium();
//        assertEquals("Premium", result);
//
//    }
//
//    //Test Account Premium
//    @Test
//    public void test_AccoutPremium() {
//        Account account2 = new Account("123455", 900000000);
//        String result = String.valueOf(account2.isPremium());
//        assertEquals("True", result);
//    }
//
//
//    //Test aaccoutnExisted()
//    @Test
//    public void accoutnExisted() {
//        Bank bank = new Bank();
//
//        // Tạo khách hàng và tài khoản
//        Customer customer1 = new Customer();
//        customer1.addAccount(new Account("123456", 900000));
//        customer1.addAccount(new Account("123123", 8000));
//        bank.addCustomer(customer1);
//
//        // Test tài khoản tồn tại
//        assertEquals("True", bank.isAccountNumberExisted("123123"));
//
//        // Test tài khoản không tồn tại
//        assertEquals("false", bank.isAccountNumberExisted("123455"));
//    }
//}
