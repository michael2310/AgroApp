package com.example.myapplication.bt;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.db.UsersRepository;
import com.github.pires.obd.commands.ObdCommand;
import com.github.pires.obd.commands.protocol.EchoOffCommand;
import com.github.pires.obd.commands.protocol.LineFeedOffCommand;
import com.github.pires.obd.commands.protocol.ObdResetCommand;
import com.github.pires.obd.commands.protocol.SelectProtocolCommand;
import com.github.pires.obd.commands.protocol.SpacesOffCommand;
import com.github.pires.obd.commands.protocol.TimeoutCommand;
import com.github.pires.obd.enums.ObdProtocols;
import com.github.pires.obd.exceptions.MisunderstoodCommandException;
import com.github.pires.obd.exceptions.NoDataException;
import com.github.pires.obd.exceptions.NonNumericResponseException;
import com.github.pires.obd.exceptions.ResponseException;
import com.github.pires.obd.exceptions.UnableToConnectException;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BluetoothActivity extends AppCompatActivity {

    private final static int REQUEST_BT_ENABLE = 1;

    private BluetoothAdapter btAdapter;
    private Set<BluetoothDevice> btPairedDevices;
    private ArrayAdapter<BtDevice> btArrayAdapter;
    private ListView btDevicesListView;
    private ArrayAdapter<String> logsAdapter;
    private final ArrayList<String> logs = new ArrayList();
    private ArrayList<String> speedAvg = new ArrayList<>();
    private ArrayList<String> fuelAvg = new ArrayList<>();
    private ArrayList<String> fuelConsumptionAvg = new ArrayList<>();

    private Button btOn;
    private Button btOff;
    private Button btDevicesList;
    private Button btDiscover;
    private Button btReset;
    private Button btExport;

    private TextView textViewRPM;
    private TextView textViewSpeed;
    private TextView textViewMassAirflow;
    private TextView textViewAvg;
    private TextView textViewFuelAvg;


    private Handler logsHandler;

    private float numSpd, resultSpd, numFuel, resultFuel;



    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        logsAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, logs);
        speedAvg.clear();
        fuelAvg.clear();
        ListView logsView = findViewById(R.id.logsList);
        logsView.setAdapter(logsAdapter);
        logsHandler = new Handler(Looper.getMainLooper()) {
            @SuppressLint("SetTextI18n")
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                String data = msg.getData().getString("data");
                if(data.contains("RPM")) {
                    textViewRPM.setText(data);
                }

                if(data.contains("km/h")){
                    textViewSpeed.setText(data);
                    String dataToAvg = data.split("k")[0];
                    UsersRepository.getInstance().currentSpeed(dataToAvg + "km/h");

                    speedAvg.add(dataToAvg);
                }

                if(data.contains("g/s")){
                    String dataToFuel = data.split("g")[0].replace(",", ".");
                    double MAF = Double.parseDouble(dataToFuel);
                    double fuelFlow = (MAF * 3600) / (14.7f * 820) ;   // l/h
                    String convertedFuelFlow = String.format("%.2f", fuelFlow);
                    textViewMassAirflow.setText(convertedFuelFlow + "L/h");
                    UsersRepository.getInstance().currentConsumption(convertedFuelFlow + "L/h");
                    fuelAvg.add(convertedFuelFlow);
                }

                logs.add(msg.getData().getString("data"));
                logsAdapter.notifyDataSetChanged();
            }
        };


        btOn = (Button) findViewById(R.id.on);
        btOff = (Button) findViewById(R.id.off);
        btDiscover = (Button) findViewById(R.id.discover);
        btDevicesList = (Button) findViewById(R.id.PairedBtn);
        btReset = (Button) findViewById(R.id.resetAvg);

        textViewRPM = (TextView) findViewById(R.id.textViewRPMOBD);
        textViewSpeed = (TextView) findViewById(R.id.textViewSpeed);
        textViewMassAirflow = (TextView) findViewById(R.id.textViewMassAirFlow);
        textViewAvg = (TextView) findViewById(R.id.textViewAvg);
        textViewFuelAvg = (TextView) findViewById(R.id.textViewFuelAvg);


        btArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        btAdapter = BluetoothAdapter.getDefaultAdapter();

        btDevicesListView = (ListView) findViewById(R.id.devicesListView);
        btDevicesListView.setAdapter(btArrayAdapter);
        btDevicesListView.setOnItemClickListener(mDeviceClickListener);


        if (btArrayAdapter == null) {
            Toast.makeText(getApplicationContext(), "Nie znaleziono modułu Bt", Toast.LENGTH_SHORT).show();
        } else {


            btOn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bluetoothOn(v);
                }
            });

            btOff.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bluetoothOff(v);
                }
            });

            btDevicesList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listPairedDevices(v);
                }
            });

            btDiscover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    discover(v);
                }
            });

            btReset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fuelAvg.clear();
                    speedAvg.clear();
                }
            });
        }
        runHandlerSpeedAvg();
        runHandlerFuelConsumption();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }



    private void runHandlerSpeedAvg(){

        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int sizeSpd = 0;
                resultSpd = 0;
                for (int i = 0; i < speedAvg.size(); i++){
                     sizeSpd = speedAvg.size();
                    numSpd = Float.parseFloat(speedAvg.get(i));
                    resultSpd += numSpd;
                }
                textViewAvg.setText(String.format("%.2f", (resultSpd / sizeSpd)) + "km/h");
                UsersRepository.getInstance().carDataAvgSpeed(String.format("%.2f", (resultSpd / sizeSpd)) + "km/h");
                handler.postDelayed(this, 5000);
            }
        });
    }

    private void runHandlerFuelConsumption(){

        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                int sizeFuel = 0;
                resultFuel = 0;
                for (int i = 0; i < fuelAvg.size(); i++){
                    sizeFuel = fuelAvg.size();
                    String convertedFuelAvf = fuelAvg.get(i).replace(",", ".");
//                    numFuel = Float.parseFloat(fuelAvg.get(i));
                    numFuel = Float.parseFloat(convertedFuelAvf);
                    resultFuel += numFuel;
                }
                textViewFuelAvg.setText(String.format("%.2f",(resultFuel / sizeFuel)) + "L/h");
                UsersRepository.getInstance().carDataAvgFuel(String.format("%.2f",(resultFuel / sizeFuel)) + "L/h");
                handler.postDelayed(this, 5000);
            }
        });
    }

    private void bluetoothOn(View view) {
        if (!btAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_BT_ENABLE);
            Toast.makeText(getApplicationContext(), "Bluetooth turned on", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getApplicationContext(), "Bluetooth is already on", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent Data) {
        super.onActivityResult(requestCode, resultCode, Data);
        if (requestCode == REQUEST_BT_ENABLE) {
        }
    }

    private void bluetoothOff(View view) {
        btAdapter.disable();
        Toast.makeText(getApplicationContext(), "Bluetooth turned Off", Toast.LENGTH_SHORT).show();
    }

    private void discover(View view) {
        if (btAdapter.isDiscovering()) {
            btAdapter.cancelDiscovery();
            Toast.makeText(getApplicationContext(), "Discovery stopped", Toast.LENGTH_SHORT).show();
        } else {
            if (btAdapter.isEnabled()) {
                btArrayAdapter.clear();
                btAdapter.startDiscovery();
                Toast.makeText(getApplicationContext(), "Discovery started", Toast.LENGTH_SHORT).show();
                registerReceiver(blReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
            } else {
                Toast.makeText(getApplicationContext(), "Bluetooth not on", Toast.LENGTH_SHORT).show();
            }
        }
    }

    final BroadcastReceiver blReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                btArrayAdapter.add(new BtDevice(device));
                btArrayAdapter.notifyDataSetChanged();
            }
        }
    };

    private void listPairedDevices(View view) {
        btPairedDevices = btAdapter.getBondedDevices();
        if (btAdapter.isEnabled()) {
            for (BluetoothDevice device : btPairedDevices)
                btArrayAdapter.add(new BtDevice(device));

            Toast.makeText(getApplicationContext(), "Show Paired Devices", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(getApplicationContext(), "Bluetooth not on", Toast.LENGTH_SHORT).show();
    }


    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int pos, long id) {

            if (!btAdapter.isEnabled()) {
                Toast.makeText(getBaseContext(), "Bluetooth not on", Toast.LENGTH_SHORT).show();
                return;
            }
            String info = ((TextView) v).getText().toString();
            final String bluetootPosition = info.substring(info.length() - 17);
            new Thread() {
                public void run() {
                    Log.d("BLUETOOTH UTILS", "1-Thread started");
                    sendMessageToUI("1-Thread started");
                    BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice device = btAdapter.getRemoteDevice(bluetootPosition);
                    final String PBAP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
                    Log.d("BLUETOOTH UTILS", "2-Device: " + device);
                    sendMessageToUI("2-Device: " + device);
                    BluetoothSocket socket = null;
                    InputStream tmpIn = null;
                    OutputStream tmpOut = null;
                    try {
                        socket = device.createInsecureRfcommSocketToServiceRecord(ParcelUuid.fromString(PBAP_UUID).getUuid());
                        Log.d("BLUETOOTH UTILS", "3-createRfcommSocketToServiceRecord");
                        sendMessageToUI("3-createRfcommSocketToServiceRecord");
                        Log.d("BLUETOOTH UTILS", "4-cancelDiscovery");
                        socket.connect();
                        Thread.sleep(500);
                        Log.d("BLUETOOTH UTILS", "5-connect");
                        sendMessageToUI("5-connect");
                        tmpIn = socket.getInputStream();
                        Thread.sleep(100);
                        tmpOut = socket.getOutputStream();
                        Thread.sleep(100);
                        Log.d("BLUETOOTH UTILS", "6-getInputStream,getOutputStream");
                        sendMessageToUI("6-getInputStream,getOutputStream");
                        boolean initError = initConnection(tmpIn, tmpOut);
                        Log.d("BLUETOOTH UTILS", "7-initConnection initError = " + initError);
                        sendMessageToUI("7-initConnection initError = " + initError);
                        Thread.sleep(500);
                        if (!initError) {
                            readParamsInLoop(tmpIn, tmpOut);
                        }
                    } catch (Throwable e) {
                        Log.e("BLUETOOTH UTILS", "8-error: " + e);
                        sendMessageToUI("8-error: " + e);
                        e.printStackTrace();
                        if (socket != null) {
                            try {
                                socket.close();
                            } catch (Throwable error) {
                                Log.e("BLUETOOTH UTILS", "8a-error: " + error);
                            }
                            try {
                                if (tmpIn != null) {
                                    tmpIn.close();
                                }
                            } catch (Throwable error) {
                                Log.e("BLUETOOTH UTILS", "8b-error: " + error);
                            }
                            try {
                                if (tmpOut != null) {
                                    tmpOut.close();
                                }
                            } catch (Throwable error) {
                                Log.e("BLUETOOTH UTILS", "8c-error: " + error);
                            }
                            socket = null;
                            tmpOut = null;
                            tmpIn = null;
                        }
                    }
                }
            }.start();
        }
    };

    private void sendMessageToUI(String message) {
        Message msg = Message.obtain();
        msg.what = 0;
        Bundle b = new Bundle();
        b.putString("data", message);
        msg.setData(b);
        logsHandler.sendMessage(msg);
    }


    private void readParamsInLoop(InputStream tmpIn, OutputStream tmpOut) throws InterruptedException {
        boolean isError = false;
        while (!isError) {
            isError = readParams(tmpIn, tmpOut);
            Log.e("BLUETOOTH UTILS", "9-readParamsInLoop: " + isError);
        }
    }

    private boolean readParams(InputStream tmpIn, OutputStream tmpOut) throws InterruptedException {

        List<ObdCommand> dataCommands = new ArrayList<>();
        dataCommands.add(new RPMCommand());
        dataCommands.add(new ConsumptionRateCommand());
        dataCommands.add(new SpeedCommand());
        dataCommands.add(new FuelLevelCommand());
        dataCommands.add(new DistanceMILOnCommand());
        dataCommands.add(new MassAirFlowCommand());

        boolean error = false;



        for (ObdCommand command : dataCommands) {
            String cmd = "";
            String temp = "";

            try {
                command.run(tmpIn, tmpOut);
//                cmd = "DATA:" + command.getCalculatedResult() + command.getName() + " Data:" + command.getResult() + " formatted: " + command.getFormattedResult() + " unit:" + command.getCalculatedResult() + " " + command.getResultUnit();
               // cmd = " formatted: " + command.getFormattedResult() + " unit:" + command.getCalculatedResult() + " " + command.getResultUnit();
                cmd = command.getFormattedResult();
                sendMessageToUI(cmd);
            } catch (NoDataException e) {
             //   Log.d("BLUETOOTH UTILS", "NO DATA EXCEPTON " + command.getName());
                temp = "Socket: OK Streams:OK  DATA:Warning Cause:\n" + "NO DATA EXCEPTON";
            } catch (UnableToConnectException e) {
                Log.d("BLUETOOTH UTILS", "UNABLE TO CONNECT EXCEPTON " + command.getName() + e);
                temp = "Socket: OK Streams:OK  DATA:Warning Cause:\n" + "UNABLE TO CONNECT EXCEPTON" + " CMD:" + command.getName();
            } catch (NonNumericResponseException e) {
                Log.d("BLUETOOTH UTILS", "NonNumericResponseException EXCEPTON " + command.getName() + e);
                temp = "Socket: OK Streams:OK  DATA:Warning Cause:\n" + "NonNumericResponseException" + " CMD:" + command.getName();
            } catch (MisunderstoodCommandException me) {
                temp = "Socket: OK Streams:OK  DATA:Warning Cause:\n" + "MisunderstoodCommandException" + " CMD:" + command.getName();
            } catch (IndexOutOfBoundsException me) {
                temp = "Socket: OK Streams:OK  DATA:Warning Cause:\n" + "IndexOutOfBoundsException" + " CMD:" + command.getName();
            } catch (Exception e) {
                if (e instanceof ResponseException && (e.getMessage().contains("STOPPED") || e.getMessage().contains("CANERROR")
                        || e.getMessage().contains("?"))) {
                    temp = "Socket: OK Streams:OK  DATA:Warning Cause:\n" + e.getMessage() + " CMD:" + command.getName();
                } else {
                    Log.d("BLUETOOTH UTILS", "Exception " + e.getClass() + " :" + command.getName() + " " + e);
                    temp = "Socket: Clousing Streams:Clousing  Data:error Exeption:" + e + " CMD:" + command.getName();
                    error = true;
                    Log.d("BLUETOOTH UTILS", "cmd: " + cmd);
                    break;
                }
            }
            Log.d("BLUETOOTH UTILS", "cmd: " + cmd);
            Thread.sleep(100);
        }
        return error;
    }

    private boolean initConnection(InputStream tmpIn, OutputStream tmpOut) throws InterruptedException {
        ObdCommand resetCmd;
        boolean resetOK = false;
        for (int i = 0; i < 10; i++) {
            try {
                resetCmd = new ObdResetCommand();
                resetCmd.run(tmpIn, tmpOut);
                resetOK = true;
                break;
            } catch (Exception e) {
                Log.e("BTU", "initconnection exeption" + e);
            }
        }
        if (!resetOK) return false;

        List<ObdCommand> commands = new ArrayList<>();
        commands.add(new EchoOffCommand());
        commands.add(new LineFeedOffCommand());
        commands.add(new SpacesOffCommand());
        commands.add(new TimeoutCommand(600));
        commands.add(new SelectProtocolCommand(ObdProtocols.AUTO));

        boolean error = false;

        for (ObdCommand command : commands) {
            for (int i = 0; i < 5; i++) {
                try {
                    command.run(tmpIn, tmpOut);
                    Log.e("BTU INIT", "Command" + command.getName() + " " + command.getResult());
                    sendMessageToUI("Command" + command.getName() + " " + command.getResult());
                    Thread.sleep(100);
                    break;
                } catch (Throwable e) {
                    Thread.sleep(300);
                    if (e instanceof MisunderstoodCommandException && command.getName().equals("CRAEngineCommand")) {
                        Log.e("INIT OBD2", e.getMessage() + ", " + e.getClass());
                    } else {

                        Log.e("INIT OBD2", e.getMessage() + ", " + e.getClass());
                        if (i == 4) {
                            error = true;
                        }
                    }
                }
            }
        }
        return error;
    }

}