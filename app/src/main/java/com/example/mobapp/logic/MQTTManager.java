package com.example.mobapp.logic;

import android.content.Context;
import android.util.Log;

import com.example.mobapp.MainActivity;
import com.example.mobapp.fairytale.Fairytale;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * The manager that manages all of the MQTT connections
 */
public class MQTTManager {
    private static MQTTManager manager = null;

    public static MQTTManager getManager() {
        return manager;
    }

    public static final String LOGTAG = "MQTTManager: ";

    // The url of the MQTT broker
    private static final String BROKER_HOST_URL = "tcp://sendlab.nl:11884";

    // The username to connect to the broker
    private static final String USERNAME = "ti";

    // The password to connect to the broker
    private static final String PASSWORD = "tiavans";

    public static final UUID UID = UUID.randomUUID();
    private static final String CLIENT_ID = "MobApp" + UID.toString();
    private static final int QUALITY_OF_SERVICE = 0;

    private final MqttAndroidClient mqttAndroidClient;

    /**
     * Constructor for the MQTTManager
     * @param context
     */
    public MQTTManager(Context context) {
        if (manager == null)
            manager = this;

        Log.i(LOGTAG, "The user id: " + this.UID);
        // Show the automatically generated random client ID
        Log.i(LOGTAG, "Client ID (random) is " + CLIENT_ID);
        // Create the MQTT client, using the URL of the MQTT broker and the client ID
        mqttAndroidClient = new MqttAndroidClient(context, BROKER_HOST_URL, CLIENT_ID);
        // Set up callbacks to handle events from the MQTT broker
        mqttAndroidClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                Log.d(LOGTAG, "MQTT client lost connection to broker, cause: ");
                cause.printStackTrace();
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.d(LOGTAG, "MQTT client received message " + message + " on topic " + topic);
                //todo Check what topic the message is for and handle accordingly
                for (Fairytale tale : Fairytale.fairytales) {
                    if (topic.contains(tale.getTopic())) {
                        tale.MessageReceived(message);
                    }
                }

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                Log.d(LOGTAG, "MQTT client delivery complete");
            }
        });

        // Connect the MQTT client to the MQTT broker
        connectToBroker(mqttAndroidClient, CLIENT_ID);
    }

    /**
     * Starts connection to the broker
     * @param client  The android device trying to connect
     * @param clientId  The is of the client
     */
    public void connectToBroker(MqttAndroidClient client, String clientId) {
        // Set up connection options for the connection to the MQTT broker
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);

        String topic = MainActivity.topicLocation + UID;
        byte[] payload = "Disconnected".getBytes();
        options.setWill(topic, payload, QUALITY_OF_SERVICE, true);

        options.setUserName(USERNAME);
        options.setPassword(PASSWORD.toCharArray());
        // Add more options if necessary
        try {
            // Try to connect to the MQTT broker
            IMqttToken token = client.connect(options, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d(LOGTAG, "MQTT client is now connected to MQTT broker");
//                    publishMessage(topic, "Connected");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.e(LOGTAG, "MQTT client failed to connect to MQTT broker: " +
                            exception.getLocalizedMessage());
                    //todo notify user
                }
            });
        } catch (MqttException e) {
            Log.e(LOGTAG, "MQTT exception while connecting to MQTT broker, reason: " +
                    e.getReasonCode() + ", msg: " + e.getMessage() + ", cause: " + e.getCause());
            e.printStackTrace();
        }
    }

    /**
     * Disconnects the connection to the broker
     */
    public void disconnectFromBroker() {
        try {
            // Try to disconnect from the MQTT broker
            IMqttToken token = this.mqttAndroidClient.disconnect();
            // Set up callbacks to handle the result
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d(LOGTAG, "MQTT client is now disconnected from MQTT broker");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.e(LOGTAG, "MQTT failed to disconnect from MQTT broker: " +
                            exception.getLocalizedMessage());
                }
            });
        } catch (MqttException e) {
            Log.e(LOGTAG, "MQTT exception while disconnecting from MQTT broker, reason: " +
                    e.getReasonCode() + ", msg: " + e.getMessage() + ", cause: " + e.getCause());
            e.printStackTrace();
        }
    }

    /**
     * Sends a new message to the broker, given a topic
     * @param topic  The topic to send the message to
     * @param msg  The message to send
     */
    public void publishMessage(String topic, String msg) {
        try {
            // Convert the message to a UTF-8 encoded byte array
            // Store it in an MqttMessage
            byte[] encodedPayload = msg.getBytes(StandardCharsets.UTF_8);
            MqttMessage message = new MqttMessage(encodedPayload);

            // Set parameters for the message
            message.setQos(QUALITY_OF_SERVICE);
            message.setRetained(false);
            // Publish the message via the MQTT broker
            this.mqttAndroidClient.publish(topic, message);
            Log.d(LOGTAG, "publishMessage: " + message);
        } catch (MqttException e) {
            Log.e(LOGTAG, "MQTT exception while publishing topic to MQTT broker, msg: " + e.getMessage() +
                    ", cause: " + e.getCause());
            e.printStackTrace();
        }
    }

    /**
     * Subscribs the broker to the given topic
     * @param topic  The topic to subscribe to
     */
    public void subscribeToTopic(final String topic) {
        try {
            // Try to subscribe to the topic
            IMqttToken token = this.mqttAndroidClient.subscribe(topic, QUALITY_OF_SERVICE);
            // Set up callbacks to handle the result
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d(LOGTAG, "MQTT client is now subscribed to topic " + topic);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.e(LOGTAG, "MQTT failed to subscribe to topic " + topic + " because: " + exception);
                }
            });
        } catch (MqttException e) {
            Log.e(LOGTAG, "MQTT exception while subscribing to topic on MQTT broker, reason: " +
                    e.getReasonCode() + ", msg: " + e.getMessage() + ", cause: " + e.getCause());
            e.printStackTrace();
        }
    }

    /**
     * Unsubscribes from a given topic
     * @param topic  The topic to unsubscribe from
     */
    public void unsubscribeToTopic(final String topic) {
        try {
            // Try to unsubscribe to the topic
            IMqttToken token = this.mqttAndroidClient.unsubscribe(topic);
            // Set up callbacks to handle the result
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d(LOGTAG, "MQTT client is now unsubscribed to topic " + topic);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.e(LOGTAG, "MQTT client failed to unsubscribe to topic " + topic + " because: " +
                            exception.getLocalizedMessage());
                }
            });
        } catch (MqttException e) {
            Log.e(LOGTAG, "MQTT exception while unsubscribing from topic on MQTT broker, reason: " +
                    e.getReasonCode() + ", msg: " + e.getMessage() + ", cause: " + e.getCause());
            e.printStackTrace();
        }
    }

    /**
     * disconnect the connection to the broker when the app is closed normally
     * @throws Throwable  The Exception thrown when finalizing goes wrong
     */
    @Override
    protected void finalize() throws Throwable {
        disconnectFromBroker();
        super.finalize();
    }
}
