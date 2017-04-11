begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ahc.ws.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ahc
operator|.
name|ws
operator|.
name|springboot
package|;
end_package

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
name|ahc
operator|.
name|AhcBinding
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
name|spi
operator|.
name|HeaderFilterStrategy
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
name|util
operator|.
name|jsse
operator|.
name|SSLContextParameters
import|;
end_import

begin_import
import|import
name|org
operator|.
name|asynchttpclient
operator|.
name|AsyncHttpClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|asynchttpclient
operator|.
name|AsyncHttpClientConfig
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
name|NestedConfigurationProperty
import|;
end_import

begin_comment
comment|/**  * To exchange data with external Websocket servers using Async Http Client.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.ahc-ws"
argument_list|)
DECL|class|WsComponentConfiguration
specifier|public
class|class
name|WsComponentConfiguration
block|{
comment|/**      * To use a custom AsyncHttpClient      */
annotation|@
name|NestedConfigurationProperty
DECL|field|client
specifier|private
name|AsyncHttpClient
name|client
decl_stmt|;
comment|/**      * To use a custom AhcBinding which allows to control how to bind between      * AHC and Camel.      */
annotation|@
name|NestedConfigurationProperty
DECL|field|binding
specifier|private
name|AhcBinding
name|binding
decl_stmt|;
comment|/**      * To configure the AsyncHttpClient to use a custom      * com.ning.http.client.AsyncHttpClientConfig instance.      */
annotation|@
name|NestedConfigurationProperty
DECL|field|clientConfig
specifier|private
name|AsyncHttpClientConfig
name|clientConfig
decl_stmt|;
comment|/**      * Reference to a org.apache.camel.util.jsse.SSLContextParameters in the      * Registry. Note that configuring this option will override any SSL/TLS      * configuration options provided through the clientConfig option at the      * endpoint or component level.      */
annotation|@
name|NestedConfigurationProperty
DECL|field|sslContextParameters
specifier|private
name|SSLContextParameters
name|sslContextParameters
decl_stmt|;
comment|/**      * Whether to allow java serialization when a request uses      * context-type=application/x-java-serialized-object This is by default      * turned off. If you enable this then be aware that Java will deserialize      * the incoming data from the request to Java and that can be a potential      * security risk.      */
DECL|field|allowJavaSerializedObject
specifier|private
name|Boolean
name|allowJavaSerializedObject
init|=
literal|false
decl_stmt|;
comment|/**      * Enable usage of global SSL context parameters.      */
DECL|field|useGlobalSslContextParameters
specifier|private
name|Boolean
name|useGlobalSslContextParameters
init|=
literal|false
decl_stmt|;
comment|/**      * To use a custom org.apache.camel.spi.HeaderFilterStrategy to filter      * header to and from Camel message.      */
annotation|@
name|NestedConfigurationProperty
DECL|field|headerFilterStrategy
specifier|private
name|HeaderFilterStrategy
name|headerFilterStrategy
decl_stmt|;
comment|/**      * Whether the component should resolve property placeholders on itself when      * starting. Only properties which are of String type can use property      * placeholders.      */
DECL|field|resolvePropertyPlaceholders
specifier|private
name|Boolean
name|resolvePropertyPlaceholders
init|=
literal|true
decl_stmt|;
DECL|method|getClient ()
specifier|public
name|AsyncHttpClient
name|getClient
parameter_list|()
block|{
return|return
name|client
return|;
block|}
DECL|method|setClient (AsyncHttpClient client)
specifier|public
name|void
name|setClient
parameter_list|(
name|AsyncHttpClient
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
DECL|method|getBinding ()
specifier|public
name|AhcBinding
name|getBinding
parameter_list|()
block|{
return|return
name|binding
return|;
block|}
DECL|method|setBinding (AhcBinding binding)
specifier|public
name|void
name|setBinding
parameter_list|(
name|AhcBinding
name|binding
parameter_list|)
block|{
name|this
operator|.
name|binding
operator|=
name|binding
expr_stmt|;
block|}
DECL|method|getClientConfig ()
specifier|public
name|AsyncHttpClientConfig
name|getClientConfig
parameter_list|()
block|{
return|return
name|clientConfig
return|;
block|}
DECL|method|setClientConfig (AsyncHttpClientConfig clientConfig)
specifier|public
name|void
name|setClientConfig
parameter_list|(
name|AsyncHttpClientConfig
name|clientConfig
parameter_list|)
block|{
name|this
operator|.
name|clientConfig
operator|=
name|clientConfig
expr_stmt|;
block|}
DECL|method|getSslContextParameters ()
specifier|public
name|SSLContextParameters
name|getSslContextParameters
parameter_list|()
block|{
return|return
name|sslContextParameters
return|;
block|}
DECL|method|setSslContextParameters ( SSLContextParameters sslContextParameters)
specifier|public
name|void
name|setSslContextParameters
parameter_list|(
name|SSLContextParameters
name|sslContextParameters
parameter_list|)
block|{
name|this
operator|.
name|sslContextParameters
operator|=
name|sslContextParameters
expr_stmt|;
block|}
DECL|method|getAllowJavaSerializedObject ()
specifier|public
name|Boolean
name|getAllowJavaSerializedObject
parameter_list|()
block|{
return|return
name|allowJavaSerializedObject
return|;
block|}
DECL|method|setAllowJavaSerializedObject (Boolean allowJavaSerializedObject)
specifier|public
name|void
name|setAllowJavaSerializedObject
parameter_list|(
name|Boolean
name|allowJavaSerializedObject
parameter_list|)
block|{
name|this
operator|.
name|allowJavaSerializedObject
operator|=
name|allowJavaSerializedObject
expr_stmt|;
block|}
DECL|method|getUseGlobalSslContextParameters ()
specifier|public
name|Boolean
name|getUseGlobalSslContextParameters
parameter_list|()
block|{
return|return
name|useGlobalSslContextParameters
return|;
block|}
DECL|method|setUseGlobalSslContextParameters ( Boolean useGlobalSslContextParameters)
specifier|public
name|void
name|setUseGlobalSslContextParameters
parameter_list|(
name|Boolean
name|useGlobalSslContextParameters
parameter_list|)
block|{
name|this
operator|.
name|useGlobalSslContextParameters
operator|=
name|useGlobalSslContextParameters
expr_stmt|;
block|}
DECL|method|getHeaderFilterStrategy ()
specifier|public
name|HeaderFilterStrategy
name|getHeaderFilterStrategy
parameter_list|()
block|{
return|return
name|headerFilterStrategy
return|;
block|}
DECL|method|setHeaderFilterStrategy ( HeaderFilterStrategy headerFilterStrategy)
specifier|public
name|void
name|setHeaderFilterStrategy
parameter_list|(
name|HeaderFilterStrategy
name|headerFilterStrategy
parameter_list|)
block|{
name|this
operator|.
name|headerFilterStrategy
operator|=
name|headerFilterStrategy
expr_stmt|;
block|}
DECL|method|getResolvePropertyPlaceholders ()
specifier|public
name|Boolean
name|getResolvePropertyPlaceholders
parameter_list|()
block|{
return|return
name|resolvePropertyPlaceholders
return|;
block|}
DECL|method|setResolvePropertyPlaceholders ( Boolean resolvePropertyPlaceholders)
specifier|public
name|void
name|setResolvePropertyPlaceholders
parameter_list|(
name|Boolean
name|resolvePropertyPlaceholders
parameter_list|)
block|{
name|this
operator|.
name|resolvePropertyPlaceholders
operator|=
name|resolvePropertyPlaceholders
expr_stmt|;
block|}
block|}
end_class

end_unit

