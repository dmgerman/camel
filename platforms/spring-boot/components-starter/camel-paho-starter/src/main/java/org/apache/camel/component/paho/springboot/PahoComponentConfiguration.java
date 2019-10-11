begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.paho.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|paho
operator|.
name|springboot
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Generated
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|SocketFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|HostnameVerifier
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|paho
operator|.
name|PahoPersistence
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|boot
operator|.
name|ComponentConfigurationPropertiesCommon
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_comment
comment|/**  * Component for communicating with MQTT message brokers using Eclipse Paho MQTT  * Client.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.SpringBootAutoConfigurationMojo"
argument_list|)
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.paho"
argument_list|)
DECL|class|PahoComponentConfiguration
specifier|public
class|class
name|PahoComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * Whether to enable auto configuration of the paho component. This is      * enabled by default.      */
DECL|field|enabled
specifier|private
name|Boolean
name|enabled
decl_stmt|;
comment|/**      * To use the shared Paho configuration      */
DECL|field|configuration
specifier|private
name|PahoConfigurationNestedConfiguration
name|configuration
decl_stmt|;
comment|/**      * To use a shared Paho client. The option is a      * org.eclipse.paho.client.mqttv3.MqttClient type.      */
DECL|field|client
specifier|private
name|String
name|client
decl_stmt|;
comment|/**      * The URL of the MQTT broker.      */
DECL|field|brokerUrl
specifier|private
name|String
name|brokerUrl
decl_stmt|;
comment|/**      * Whether the component should use basic property binding (Camel 2.x) or      * the newer property binding with additional capabilities      */
DECL|field|basicPropertyBinding
specifier|private
name|Boolean
name|basicPropertyBinding
init|=
literal|false
decl_stmt|;
DECL|method|getConfiguration ()
specifier|public
name|PahoConfigurationNestedConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration ( PahoConfigurationNestedConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|PahoConfigurationNestedConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|getClient ()
specifier|public
name|String
name|getClient
parameter_list|()
block|{
return|return
name|client
return|;
block|}
DECL|method|setClient (String client)
specifier|public
name|void
name|setClient
parameter_list|(
name|String
name|client
parameter_list|)
block|{
name|this
operator|.
name|client
operator|=
name|client
expr_stmt|;
block|}
DECL|method|getBrokerUrl ()
specifier|public
name|String
name|getBrokerUrl
parameter_list|()
block|{
return|return
name|brokerUrl
return|;
block|}
DECL|method|setBrokerUrl (String brokerUrl)
specifier|public
name|void
name|setBrokerUrl
parameter_list|(
name|String
name|brokerUrl
parameter_list|)
block|{
name|this
operator|.
name|brokerUrl
operator|=
name|brokerUrl
expr_stmt|;
block|}
DECL|method|getBasicPropertyBinding ()
specifier|public
name|Boolean
name|getBasicPropertyBinding
parameter_list|()
block|{
return|return
name|basicPropertyBinding
return|;
block|}
DECL|method|setBasicPropertyBinding (Boolean basicPropertyBinding)
specifier|public
name|void
name|setBasicPropertyBinding
parameter_list|(
name|Boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|this
operator|.
name|basicPropertyBinding
operator|=
name|basicPropertyBinding
expr_stmt|;
block|}
DECL|class|PahoConfigurationNestedConfiguration
specifier|public
specifier|static
class|class
name|PahoConfigurationNestedConfiguration
block|{
DECL|field|CAMEL_NESTED_CLASS
specifier|public
specifier|static
specifier|final
name|Class
name|CAMEL_NESTED_CLASS
init|=
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|paho
operator|.
name|PahoConfiguration
operator|.
name|class
decl_stmt|;
comment|/**          * MQTT client identifier. The identifier must be unique.          */
DECL|field|clientId
specifier|private
name|String
name|clientId
decl_stmt|;
comment|/**          * The URL of the MQTT broker.          */
DECL|field|brokerUrl
specifier|private
name|String
name|brokerUrl
init|=
literal|"tcp://localhost:1883"
decl_stmt|;
comment|/**          * Client quality of service level (0-2).          */
DECL|field|qos
specifier|private
name|Integer
name|qos
init|=
literal|2
decl_stmt|;
comment|/**          * Retain option          */
DECL|field|retained
specifier|private
name|Boolean
name|retained
init|=
literal|false
decl_stmt|;
comment|/**          * Client persistence to be used - memory or file.          */
DECL|field|persistence
specifier|private
name|PahoPersistence
name|persistence
init|=
name|PahoPersistence
operator|.
name|MEMORY
decl_stmt|;
comment|/**          * Base directory used by file persistence. Will by default use user          * directory.          */
DECL|field|filePersistenceDirectory
specifier|private
name|String
name|filePersistenceDirectory
decl_stmt|;
comment|/**          * Username to be used for authentication against the MQTT broker          */
DECL|field|userName
specifier|private
name|String
name|userName
decl_stmt|;
comment|/**          * Password to be used for authentication against the MQTT broker          */
DECL|field|password
specifier|private
name|String
name|password
decl_stmt|;
comment|/**          * Sets the keep alive interval. This value, measured in seconds,          * defines the maximum time interval between messages sent or received.          * It enables the client to detect if the server is no longer available,          * without having to wait for the TCP/IP timeout. The client will ensure          * that at least one message travels across the network within each keep          * alive period. In the absence of a data-related message during the          * time period, the client sends a very small ping message, which the          * server will acknowledge. A value of 0 disables keepalive processing          * in the client.<p> The default value is 60 seconds</p>          */
DECL|field|keepAliveInterval
specifier|private
name|Integer
name|keepAliveInterval
init|=
literal|60
decl_stmt|;
comment|/**          * Sets the max inflight. please increase this value in a high traffic          * environment.<p> The default value is 10</p>          */
DECL|field|maxInflight
specifier|private
name|Integer
name|maxInflight
init|=
literal|10
decl_stmt|;
comment|/**          * Sets the "Last Will and Testament" (LWT) for the connection. In the          * event that this client unexpectedly loses its connection to the          * server, the server will publish a message to itself using the          * supplied details. The topic to publish to The byte payload for the          * message. The quality of service to publish the message at (0, 1 or          * 2). Whether or not the message should be retained.          */
DECL|field|willTopic
specifier|private
name|String
name|willTopic
decl_stmt|;
comment|/**          * Sets the "Last Will and Testament" (LWT) for the connection. In the          * event that this client unexpectedly loses its connection to the          * server, the server will publish a message to itself using the          * supplied details. The topic to publish to The byte payload for the          * message. The quality of service to publish the message at (0, 1 or          * 2). Whether or not the message should be retained.          */
DECL|field|willPayload
specifier|private
name|String
name|willPayload
decl_stmt|;
comment|/**          * Sets the "Last Will and Testament" (LWT) for the connection. In the          * event that this client unexpectedly loses its connection to the          * server, the server will publish a message to itself using the          * supplied details. The topic to publish to The byte payload for the          * message. The quality of service to publish the message at (0, 1 or          * 2). Whether or not the message should be retained.          */
DECL|field|willQos
specifier|private
name|Integer
name|willQos
decl_stmt|;
comment|/**          * Sets the "Last Will and Testament" (LWT) for the connection. In the          * event that this client unexpectedly loses its connection to the          * server, the server will publish a message to itself using the          * supplied details. The topic to publish to The byte payload for the          * message. The quality of service to publish the message at (0, 1 or          * 2). Whether or not the message should be retained.          */
DECL|field|willRetained
specifier|private
name|Boolean
name|willRetained
init|=
literal|false
decl_stmt|;
comment|/**          * Sets the SocketFactory to use. This allows an application to apply          * its own policies around the creation of network sockets. If using an          * SSL connection, an SSLSocketFactory can be used to supply          * application-specific security settings.          */
DECL|field|socketFactory
specifier|private
name|SocketFactory
name|socketFactory
decl_stmt|;
comment|/**          * Sets the SSL properties for the connection.<p> Note that these          * properties are only valid if an implementation of the Java Secure          * Socket Extensions (JSSE) is available. These properties are          *<em>not</em> used if a custom SocketFactory has been set. The          * following properties can be used:</p><dl>          *<dt>com.ibm.ssl.protocol</dt><dd>One of: SSL, SSLv3, TLS, TLSv1,          * SSL_TLS.</dd><dt>com.ibm.ssl.contextProvider<dd>Underlying JSSE          * provider. For example "IBMJSSE2" or "SunJSSE"</dd>          *<dt>com.ibm.ssl.keyStore</dt><dd>The name of the file that contains          * the KeyStore object that you want the KeyManager to use. For example          * /mydir/etc/key.p12</dd><dt>com.ibm.ssl.keyStorePassword</dt><dd>The          * password for the KeyStore object that you want the KeyManager to use.          * The password can either be in plain-text, or may be obfuscated using          * the static method:          *<code>com.ibm.micro.security.Password.obfuscate(char[]          * password)</code>. This obfuscates the password using a simple and          * insecure XOR and Base64 encoding mechanism. Note that this is only a          * simple scrambler to obfuscate clear-text passwords.</dd>          *<dt>com.ibm.ssl.keyStoreType</dt><dd>Type of key store, for example          * "PKCS12", "JKS", or "JCEKS".</dd>          *<dt>com.ibm.ssl.keyStoreProvider</dt><dd>Key store provider, for          * example "IBMJCE" or "IBMJCEFIPS".</dd>          *<dt>com.ibm.ssl.trustStore</dt><dd>The name of the file that          * contains the KeyStore object that you want the TrustManager to          * use.</dd><dt>com.ibm.ssl.trustStorePassword</dt><dd>The password          * for the TrustStore object that you want the TrustManager to use. The          * password can either be in plain-text, or may be obfuscated using the          * static method:<code>com.ibm.micro.security.Password.obfuscate(char[]          * password)</code>. This obfuscates the password using a simple and          * insecure XOR and Base64 encoding mechanism. Note that this is only a          * simple scrambler to obfuscate clear-text passwords.</dd>          *<dt>com.ibm.ssl.trustStoreType</dt><dd>The type of KeyStore object          * that you want the default TrustManager to use. Same possible values          * as "keyStoreType".</dd><dt>com.ibm.ssl.trustStoreProvider</dt>          *<dd>Trust store provider, for example "IBMJCE" or "IBMJCEFIPS".</dd>          *<dt>com.ibm.ssl.enabledCipherSuites</dt><dd>A list of which ciphers          * are enabled. Values are dependent on the provider, for example:          * SSL_RSA_WITH_AES_128_CBC_SHA;SSL_RSA_WITH_3DES_EDE_CBC_SHA.</dd>          *<dt>com.ibm.ssl.keyManager</dt><dd>Sets the algorithm that will be          * used to instantiate a KeyManagerFactory object instead of using the          * default algorithm available in the platform. Example values:          * "IbmX509" or "IBMJ9X509".</dd><dt>com.ibm.ssl.trustManager</dt>          *<dd>Sets the algorithm that will be used to instantiate a          * TrustManagerFactory object instead of using the default algorithm          * available in the platform. Example values: "PKIX" or          * "IBMJ9X509".</dd></dl>          */
DECL|field|sslClientProps
specifier|private
name|Properties
name|sslClientProps
decl_stmt|;
comment|/**          * Whether SSL HostnameVerifier is enabled or not. The default value is          * true.          */
DECL|field|httpsHostnameVerificationEnabled
specifier|private
name|Boolean
name|httpsHostnameVerificationEnabled
init|=
literal|true
decl_stmt|;
comment|/**          * Sets the HostnameVerifier for the SSL connection. Note that it will          * be used after handshake on a connection and you should do actions by          * yourself when hostname is verified error.<p> There is no default          * HostnameVerifier</p>          */
DECL|field|sslHostnameVerifier
specifier|private
name|HostnameVerifier
name|sslHostnameVerifier
decl_stmt|;
comment|/**          * Sets whether the client and server should remember state across          * restarts and reconnects.<ul><li>If set to false both the client and          * server will maintain state across restarts of the client, the server          * and the connection. As state is maintained:<ul><li>Message delivery          * will be reliable meeting the specified QOS even if the client, server          * or connection are restarted.<li>The server will treat a subscription          * as durable.</ul><li>If set to true the client and server will not          * maintain state across restarts of the client, the server or the          * connection. This means<ul><li>Message delivery to the specified QOS          * cannot be maintained if the client, server or connection are          * restarted<li>The server will treat a subscription as non-durable          *</ul></ul>          */
DECL|field|cleanSession
specifier|private
name|Boolean
name|cleanSession
init|=
literal|true
decl_stmt|;
comment|/**          * Sets the connection timeout value. This value, measured in seconds,          * defines the maximum time interval the client will wait for the          * network connection to the MQTT server to be established. The default          * timeout is 30 seconds. A value of 0 disables timeout processing          * meaning the client will wait until the network connection is made          * successfully or fails.          */
DECL|field|connectionTimeout
specifier|private
name|Integer
name|connectionTimeout
init|=
literal|30
decl_stmt|;
comment|/**          * Set a list of one or more serverURIs the client may connect to.          * Multiple servers can be separated by comma.<p> Each          *<code>serverURI</code> specifies the address of a server that the          * client may connect to. Two types of connection are supported          *<code>tcp://</code> for a TCP connection and<code>ssl://</code> for          * a TCP connection secured by SSL/TLS. For example:<ul>          *<li><code>tcp://localhost:1883</code></li>          *<li><code>ssl://localhost:8883</code></li></ul> If the port is not          * specified, it will default to 1883 for<code>tcp://</code>" URIs, and          * 8883 for<code>ssl://</code> URIs.<p> If serverURIs is set then it          * overrides the serverURI parameter passed in on the constructor of the          * MQTT client.<p> When an attempt to connect is initiated the client          * will start with the first serverURI in the list and work through the          * list until a connection is established with a server. If a connection          * cannot be made to any of the servers then the connect attempt fails.          *<p> Specifying a list of servers that a client may connect to has          * several uses:<ol><li>High Availability and reliable message          * delivery<p> Some MQTT servers support a high availability feature          * where two or more "equal" MQTT servers share state. An MQTT client          * can connect to any of the "equal" servers and be assured that          * messages are reliably delivered and durable subscriptions are          * maintained no matter which server the client connects to.</p><p>          * The cleansession flag must be set to false if durable subscriptions          * and/or reliable message delivery is required.</p></li><li>Hunt          * List<p> A set of servers may be specified that are not "equal" (as          * in the high availability option). As no state is shared across the          * servers reliable message delivery and durable subscriptions are not          * valid. The cleansession flag must be set to true if the hunt list          * mode is used</p></li></ol>          */
DECL|field|serverURIs
specifier|private
name|String
name|serverURIs
decl_stmt|;
comment|/**          * Sets the MQTT version. The default action is to connect with version          * 3.1.1, and to fall back to 3.1 if that fails. Version 3.1.1 or 3.1          * can be selected specifically, with no fall back, by using the          * MQTT_VERSION_3_1_1 or MQTT_VERSION_3_1 options respectively.          */
DECL|field|mqttVersion
specifier|private
name|Integer
name|mqttVersion
decl_stmt|;
comment|/**          * Sets whether the client will automatically attempt to reconnect to          * the server if the connection is lost.<ul><li>If set to false, the          * client will not attempt to automatically reconnect to the server in          * the event that the connection is lost.</li><li>If set to true, in          * the event that the connection is lost, the client will attempt to          * reconnect to the server. It will initially wait 1 second before it          * attempts to reconnect, for every failed reconnect attempt, the delay          * will double until it is at 2 minutes at which point the delay will          * stay at 2 minutes.</li></ul>          */
DECL|field|automaticReconnect
specifier|private
name|Boolean
name|automaticReconnect
init|=
literal|true
decl_stmt|;
comment|/**          * Get the maximum time (in millis) to wait between reconnects          */
DECL|field|maxReconnectDelay
specifier|private
name|Integer
name|maxReconnectDelay
init|=
literal|128000
decl_stmt|;
comment|/**          * Sets the Custom WebSocket Headers for the WebSocket Connection.          */
DECL|field|customWebSocketHeaders
specifier|private
name|Properties
name|customWebSocketHeaders
decl_stmt|;
comment|/**          * Set the time in seconds that the executor service should wait when          * terminating before forcefully terminating. It is not recommended to          * change this value unless you are absolutely sure that you need to.          */
DECL|field|executorServiceTimeout
specifier|private
name|Integer
name|executorServiceTimeout
init|=
literal|1
decl_stmt|;
DECL|method|getClientId ()
specifier|public
name|String
name|getClientId
parameter_list|()
block|{
return|return
name|clientId
return|;
block|}
DECL|method|setClientId (String clientId)
specifier|public
name|void
name|setClientId
parameter_list|(
name|String
name|clientId
parameter_list|)
block|{
name|this
operator|.
name|clientId
operator|=
name|clientId
expr_stmt|;
block|}
DECL|method|getBrokerUrl ()
specifier|public
name|String
name|getBrokerUrl
parameter_list|()
block|{
return|return
name|brokerUrl
return|;
block|}
DECL|method|setBrokerUrl (String brokerUrl)
specifier|public
name|void
name|setBrokerUrl
parameter_list|(
name|String
name|brokerUrl
parameter_list|)
block|{
name|this
operator|.
name|brokerUrl
operator|=
name|brokerUrl
expr_stmt|;
block|}
DECL|method|getQos ()
specifier|public
name|Integer
name|getQos
parameter_list|()
block|{
return|return
name|qos
return|;
block|}
DECL|method|setQos (Integer qos)
specifier|public
name|void
name|setQos
parameter_list|(
name|Integer
name|qos
parameter_list|)
block|{
name|this
operator|.
name|qos
operator|=
name|qos
expr_stmt|;
block|}
DECL|method|getRetained ()
specifier|public
name|Boolean
name|getRetained
parameter_list|()
block|{
return|return
name|retained
return|;
block|}
DECL|method|setRetained (Boolean retained)
specifier|public
name|void
name|setRetained
parameter_list|(
name|Boolean
name|retained
parameter_list|)
block|{
name|this
operator|.
name|retained
operator|=
name|retained
expr_stmt|;
block|}
DECL|method|getPersistence ()
specifier|public
name|PahoPersistence
name|getPersistence
parameter_list|()
block|{
return|return
name|persistence
return|;
block|}
DECL|method|setPersistence (PahoPersistence persistence)
specifier|public
name|void
name|setPersistence
parameter_list|(
name|PahoPersistence
name|persistence
parameter_list|)
block|{
name|this
operator|.
name|persistence
operator|=
name|persistence
expr_stmt|;
block|}
DECL|method|getFilePersistenceDirectory ()
specifier|public
name|String
name|getFilePersistenceDirectory
parameter_list|()
block|{
return|return
name|filePersistenceDirectory
return|;
block|}
DECL|method|setFilePersistenceDirectory (String filePersistenceDirectory)
specifier|public
name|void
name|setFilePersistenceDirectory
parameter_list|(
name|String
name|filePersistenceDirectory
parameter_list|)
block|{
name|this
operator|.
name|filePersistenceDirectory
operator|=
name|filePersistenceDirectory
expr_stmt|;
block|}
DECL|method|getUserName ()
specifier|public
name|String
name|getUserName
parameter_list|()
block|{
return|return
name|userName
return|;
block|}
DECL|method|setUserName (String userName)
specifier|public
name|void
name|setUserName
parameter_list|(
name|String
name|userName
parameter_list|)
block|{
name|this
operator|.
name|userName
operator|=
name|userName
expr_stmt|;
block|}
DECL|method|getPassword ()
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
name|password
return|;
block|}
DECL|method|setPassword (String password)
specifier|public
name|void
name|setPassword
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|this
operator|.
name|password
operator|=
name|password
expr_stmt|;
block|}
DECL|method|getKeepAliveInterval ()
specifier|public
name|Integer
name|getKeepAliveInterval
parameter_list|()
block|{
return|return
name|keepAliveInterval
return|;
block|}
DECL|method|setKeepAliveInterval (Integer keepAliveInterval)
specifier|public
name|void
name|setKeepAliveInterval
parameter_list|(
name|Integer
name|keepAliveInterval
parameter_list|)
block|{
name|this
operator|.
name|keepAliveInterval
operator|=
name|keepAliveInterval
expr_stmt|;
block|}
DECL|method|getMaxInflight ()
specifier|public
name|Integer
name|getMaxInflight
parameter_list|()
block|{
return|return
name|maxInflight
return|;
block|}
DECL|method|setMaxInflight (Integer maxInflight)
specifier|public
name|void
name|setMaxInflight
parameter_list|(
name|Integer
name|maxInflight
parameter_list|)
block|{
name|this
operator|.
name|maxInflight
operator|=
name|maxInflight
expr_stmt|;
block|}
DECL|method|getWillTopic ()
specifier|public
name|String
name|getWillTopic
parameter_list|()
block|{
return|return
name|willTopic
return|;
block|}
DECL|method|setWillTopic (String willTopic)
specifier|public
name|void
name|setWillTopic
parameter_list|(
name|String
name|willTopic
parameter_list|)
block|{
name|this
operator|.
name|willTopic
operator|=
name|willTopic
expr_stmt|;
block|}
DECL|method|getWillPayload ()
specifier|public
name|String
name|getWillPayload
parameter_list|()
block|{
return|return
name|willPayload
return|;
block|}
DECL|method|setWillPayload (String willPayload)
specifier|public
name|void
name|setWillPayload
parameter_list|(
name|String
name|willPayload
parameter_list|)
block|{
name|this
operator|.
name|willPayload
operator|=
name|willPayload
expr_stmt|;
block|}
DECL|method|getWillQos ()
specifier|public
name|Integer
name|getWillQos
parameter_list|()
block|{
return|return
name|willQos
return|;
block|}
DECL|method|setWillQos (Integer willQos)
specifier|public
name|void
name|setWillQos
parameter_list|(
name|Integer
name|willQos
parameter_list|)
block|{
name|this
operator|.
name|willQos
operator|=
name|willQos
expr_stmt|;
block|}
DECL|method|getWillRetained ()
specifier|public
name|Boolean
name|getWillRetained
parameter_list|()
block|{
return|return
name|willRetained
return|;
block|}
DECL|method|setWillRetained (Boolean willRetained)
specifier|public
name|void
name|setWillRetained
parameter_list|(
name|Boolean
name|willRetained
parameter_list|)
block|{
name|this
operator|.
name|willRetained
operator|=
name|willRetained
expr_stmt|;
block|}
DECL|method|getSocketFactory ()
specifier|public
name|SocketFactory
name|getSocketFactory
parameter_list|()
block|{
return|return
name|socketFactory
return|;
block|}
DECL|method|setSocketFactory (SocketFactory socketFactory)
specifier|public
name|void
name|setSocketFactory
parameter_list|(
name|SocketFactory
name|socketFactory
parameter_list|)
block|{
name|this
operator|.
name|socketFactory
operator|=
name|socketFactory
expr_stmt|;
block|}
DECL|method|getSslClientProps ()
specifier|public
name|Properties
name|getSslClientProps
parameter_list|()
block|{
return|return
name|sslClientProps
return|;
block|}
DECL|method|setSslClientProps (Properties sslClientProps)
specifier|public
name|void
name|setSslClientProps
parameter_list|(
name|Properties
name|sslClientProps
parameter_list|)
block|{
name|this
operator|.
name|sslClientProps
operator|=
name|sslClientProps
expr_stmt|;
block|}
DECL|method|getHttpsHostnameVerificationEnabled ()
specifier|public
name|Boolean
name|getHttpsHostnameVerificationEnabled
parameter_list|()
block|{
return|return
name|httpsHostnameVerificationEnabled
return|;
block|}
DECL|method|setHttpsHostnameVerificationEnabled ( Boolean httpsHostnameVerificationEnabled)
specifier|public
name|void
name|setHttpsHostnameVerificationEnabled
parameter_list|(
name|Boolean
name|httpsHostnameVerificationEnabled
parameter_list|)
block|{
name|this
operator|.
name|httpsHostnameVerificationEnabled
operator|=
name|httpsHostnameVerificationEnabled
expr_stmt|;
block|}
DECL|method|getSslHostnameVerifier ()
specifier|public
name|HostnameVerifier
name|getSslHostnameVerifier
parameter_list|()
block|{
return|return
name|sslHostnameVerifier
return|;
block|}
DECL|method|setSslHostnameVerifier (HostnameVerifier sslHostnameVerifier)
specifier|public
name|void
name|setSslHostnameVerifier
parameter_list|(
name|HostnameVerifier
name|sslHostnameVerifier
parameter_list|)
block|{
name|this
operator|.
name|sslHostnameVerifier
operator|=
name|sslHostnameVerifier
expr_stmt|;
block|}
DECL|method|getCleanSession ()
specifier|public
name|Boolean
name|getCleanSession
parameter_list|()
block|{
return|return
name|cleanSession
return|;
block|}
DECL|method|setCleanSession (Boolean cleanSession)
specifier|public
name|void
name|setCleanSession
parameter_list|(
name|Boolean
name|cleanSession
parameter_list|)
block|{
name|this
operator|.
name|cleanSession
operator|=
name|cleanSession
expr_stmt|;
block|}
DECL|method|getConnectionTimeout ()
specifier|public
name|Integer
name|getConnectionTimeout
parameter_list|()
block|{
return|return
name|connectionTimeout
return|;
block|}
DECL|method|setConnectionTimeout (Integer connectionTimeout)
specifier|public
name|void
name|setConnectionTimeout
parameter_list|(
name|Integer
name|connectionTimeout
parameter_list|)
block|{
name|this
operator|.
name|connectionTimeout
operator|=
name|connectionTimeout
expr_stmt|;
block|}
DECL|method|getServerURIs ()
specifier|public
name|String
name|getServerURIs
parameter_list|()
block|{
return|return
name|serverURIs
return|;
block|}
DECL|method|setServerURIs (String serverURIs)
specifier|public
name|void
name|setServerURIs
parameter_list|(
name|String
name|serverURIs
parameter_list|)
block|{
name|this
operator|.
name|serverURIs
operator|=
name|serverURIs
expr_stmt|;
block|}
DECL|method|getMqttVersion ()
specifier|public
name|Integer
name|getMqttVersion
parameter_list|()
block|{
return|return
name|mqttVersion
return|;
block|}
DECL|method|setMqttVersion (Integer mqttVersion)
specifier|public
name|void
name|setMqttVersion
parameter_list|(
name|Integer
name|mqttVersion
parameter_list|)
block|{
name|this
operator|.
name|mqttVersion
operator|=
name|mqttVersion
expr_stmt|;
block|}
DECL|method|getAutomaticReconnect ()
specifier|public
name|Boolean
name|getAutomaticReconnect
parameter_list|()
block|{
return|return
name|automaticReconnect
return|;
block|}
DECL|method|setAutomaticReconnect (Boolean automaticReconnect)
specifier|public
name|void
name|setAutomaticReconnect
parameter_list|(
name|Boolean
name|automaticReconnect
parameter_list|)
block|{
name|this
operator|.
name|automaticReconnect
operator|=
name|automaticReconnect
expr_stmt|;
block|}
DECL|method|getMaxReconnectDelay ()
specifier|public
name|Integer
name|getMaxReconnectDelay
parameter_list|()
block|{
return|return
name|maxReconnectDelay
return|;
block|}
DECL|method|setMaxReconnectDelay (Integer maxReconnectDelay)
specifier|public
name|void
name|setMaxReconnectDelay
parameter_list|(
name|Integer
name|maxReconnectDelay
parameter_list|)
block|{
name|this
operator|.
name|maxReconnectDelay
operator|=
name|maxReconnectDelay
expr_stmt|;
block|}
DECL|method|getCustomWebSocketHeaders ()
specifier|public
name|Properties
name|getCustomWebSocketHeaders
parameter_list|()
block|{
return|return
name|customWebSocketHeaders
return|;
block|}
DECL|method|setCustomWebSocketHeaders (Properties customWebSocketHeaders)
specifier|public
name|void
name|setCustomWebSocketHeaders
parameter_list|(
name|Properties
name|customWebSocketHeaders
parameter_list|)
block|{
name|this
operator|.
name|customWebSocketHeaders
operator|=
name|customWebSocketHeaders
expr_stmt|;
block|}
DECL|method|getExecutorServiceTimeout ()
specifier|public
name|Integer
name|getExecutorServiceTimeout
parameter_list|()
block|{
return|return
name|executorServiceTimeout
return|;
block|}
DECL|method|setExecutorServiceTimeout (Integer executorServiceTimeout)
specifier|public
name|void
name|setExecutorServiceTimeout
parameter_list|(
name|Integer
name|executorServiceTimeout
parameter_list|)
block|{
name|this
operator|.
name|executorServiceTimeout
operator|=
name|executorServiceTimeout
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

