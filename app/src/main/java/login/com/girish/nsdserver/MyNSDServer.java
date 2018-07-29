package login.com.girish.nsdserver;

import android.app.Service;
import android.content.Intent;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;

public class MyNSDServer extends Service {
    private NsdManager nsdManager;
    private int port_no ;
    private String SERVICE_NAME = "nsd_server";
    private String SERVICE_TYPE ="_http._tcp.";
    private NsdManager.RegistrationListener mRegistrationListener = new NsdManager.RegistrationListener() {
        @Override
        public void onRegistrationFailed(NsdServiceInfo nsdServiceInfo, int i) {
            Log.i("NSDSERVER","onRegFailed() Error "+i);
        }

        @Override
        public void onUnregistrationFailed(NsdServiceInfo nsdServiceInfo, int i) {
            Log.i("NSDSERVER","onUnRegFailed() Error "+i);
        }

        @Override
        public void onServiceRegistered(NsdServiceInfo nsdServiceInfo) {
            String service_name  = nsdServiceInfo.getServiceName();
            Log.i("NSDSERVER","onSerReg()  "+service_name);
        }

        @Override
        public void onServiceUnregistered(NsdServiceInfo nsdServiceInfo) {
            String service_name  = nsdServiceInfo.getServiceName();
            Log.i("NSDSERVER","onSerUnReg()  "+service_name);
        }
    };


    public MyNSDServer() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public void onCreate() {
        super.onCreate();
        nsdManager = (NsdManager) getSystemService(NSD_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        NsdServiceInfo nsdServiceInfo = new NsdServiceInfo();
        nsdServiceInfo.setServiceName(SERVICE_NAME);
        nsdServiceInfo.setServiceType(SERVICE_TYPE);

        try {
            ServerSocket serverSocket = new ServerSocket(0);
            port_no = serverSocket.getLocalPort();
            nsdServiceInfo.setPort(port_no);
        } catch (IOException e) {
            e.printStackTrace();
        }

        nsdManager.registerService(nsdServiceInfo,NsdManager.PROTOCOL_DNS_SD,mRegistrationListener);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (nsdManager!=null){
            nsdManager.unregisterService(mRegistrationListener);
        }
    }
}
