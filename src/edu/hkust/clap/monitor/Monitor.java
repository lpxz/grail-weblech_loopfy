package edu.hkust.clap.monitor;

public class Monitor {

    public static synchronized void crashed(Throwable crashedException) {
    }

    public static synchronized void caredByClap(String name, Object o) {
    }

    public static void threadStartRun(long threadId) {
    }

    public static void threadExitRun(long threadId) {
    }

    public static void mainThreadStartRun(long threadId, String methodName, String[] args) {
    }

    public static void mainThreadStartRun0(long threadId, String methodName, String[] args) {
    }

    public static void accessSPE(int iid, long id) {
    }

    public static boolean youMayCrash(String methodName, Object objects[], String types[], long threadId) {
        return true;
    }

    public static void youAreOK() {
    }

    public static void loopBegin(int loopid) {
    }

    public static void loopInc(int loopid) {
    }

    public static void loopEnd(int loopid) {
    }

    public static void enterSyncMethodBefore(int iid, long id) {
    }

    public static void exitSyncMethodAfter(int iid, long id) {
    }

    public static void enterNonPrivateMethodAfter(int methoId, long threadId, String msig) {
    }

    public static void exitNonPrivateMethodBefore(int methoId, long threadId, String msig) {
    }

    public static void enterPrivateMethodAfter(int methoId, long threadId, String msig) {
    }

    public static void exitPrivateMethodBefore(int methoId, long threadId, String msig) {
    }

    public static void waitBefore(int iid, long id) {
    }

    public static void waitAfter(Object o, int iid, long id) {
    }

    public static void notifyBefore(Object o, int iid, long id) {
    }

    public static void notifyAllBefore(Object o, int iid, long id) {
    }

    public static void readBefore(int iid, long id) {
    }

    public static void writeBefore(int iid, long id) {
    }

    public static void readBeforeInstance(Object o, int iid, long id, int line, String methodSig, String jimpleCode) {
    }

    public static void writeBeforeInstance(Object o, int iid, long id, int line, String methodSig, String jimpleCode) {
    }

    public static void readBeforeStatic(int iid, long id, int line, String methodSig, String jimpleCode) {
    }

    public static void writeBeforeStatic(int iid, long id, int line, String methodSig, String jimpleCode) {
    }

    public static void enterMonitorAfter(Object o, int iid, long id) {
    }

    public static void exitMonitorBefore(Object o, int iid, long id) {
    }

    public static void startRunThreadBefore(Thread t, long id) {
    }

    public static void joinRunThreadAfter(Thread t, long id) {
    }
}
