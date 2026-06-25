package com.example.demo;

public class exceptions {
    /**
     * 自訂的業務邏輯例外：當訂單金額無效時拋出。
     * 這是一個「受檢例外」(Checked Exception)，因為它直接繼承自 Exception。
     */
    public static class InvalidOrderAmountException extends Exception {

        /**
         * 建構子，允許傳入詳細的錯誤訊息。
         * @param message 錯誤訊息，例如 "訂單金額不可為負數"
         */
        public InvalidOrderAmountException(String message) {
            // 呼叫父類別 (Exception) 的建構子，將錯誤訊息傳遞上去
            super(message);
        }
    }
}
