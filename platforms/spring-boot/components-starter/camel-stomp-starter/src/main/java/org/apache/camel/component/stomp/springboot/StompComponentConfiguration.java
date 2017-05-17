begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.stomp.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|stomp
operator|.
name|springboot
package|;
end_package

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
comment|/**  * The stomp component is used for communicating with Stomp compliant message  * brokers.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
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
literal|"camel.component.stomp"
argument_list|)
DECL|class|StompComponentConfiguration
specifier|public
class|class
name|StompComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * To use the shared stomp configuration      */
DECL|field|configuration
specifier|private
name|StompConfigurationNestedConfiguration
name|configuration
decl_stmt|;
comment|/**      * The URI of the Stomp broker to connect to      */
DECL|field|brokerURL
specifier|private
name|String
name|brokerURL
decl_stmt|;
comment|/**      * The username      */
DECL|field|login
specifier|private
name|String
name|login
decl_stmt|;
comment|/**      * The password      */
DECL|field|passcode
specifier|private
name|String
name|passcode
decl_stmt|;
comment|/**      * The virtual host      */
DECL|field|host
specifier|private
name|String
name|host
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
DECL|method|getConfiguration ()
specifier|public
name|StompConfigurationNestedConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration ( StompConfigurationNestedConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|StompConfigurationNestedConfiguration
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
DECL|method|getBrokerURL ()
specifier|public
name|String
name|getBrokerURL
parameter_list|()
block|{
return|return
name|brokerURL
return|;
block|}
DECL|method|setBrokerURL (String brokerURL)
specifier|public
name|void
name|setBrokerURL
parameter_list|(
name|String
name|brokerURL
parameter_list|)
block|{
name|this
operator|.
name|brokerURL
operator|=
name|brokerURL
expr_stmt|;
block|}
DECL|method|getLogin ()
specifier|public
name|String
name|getLogin
parameter_list|()
block|{
return|return
name|login
return|;
block|}
DECL|method|setLogin (String login)
specifier|public
name|void
name|setLogin
parameter_list|(
name|String
name|login
parameter_list|)
block|{
name|this
operator|.
name|login
operator|=
name|login
expr_stmt|;
block|}
DECL|method|getPasscode ()
specifier|public
name|String
name|getPasscode
parameter_list|()
block|{
return|return
name|passcode
return|;
block|}
DECL|method|setPasscode (String passcode)
specifier|public
name|void
name|setPasscode
parameter_list|(
name|String
name|passcode
parameter_list|)
block|{
name|this
operator|.
name|passcode
operator|=
name|passcode
expr_stmt|;
block|}
DECL|method|getHost ()
specifier|public
name|String
name|getHost
parameter_list|()
block|{
return|return
name|host
return|;
block|}
DECL|method|setHost (String host)
specifier|public
name|void
name|setHost
parameter_list|(
name|String
name|host
parameter_list|)
block|{
name|this
operator|.
name|host
operator|=
name|host
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
DECL|class|StompConfigurationNestedConfiguration
specifier|public
specifier|static
class|class
name|StompConfigurationNestedConfiguration
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
name|stomp
operator|.
name|StompConfiguration
operator|.
name|class
decl_stmt|;
comment|/**          * The URI of the Stomp broker to connect to          */
DECL|field|brokerURL
specifier|private
name|String
name|brokerURL
init|=
literal|"tcp://localhost:61613"
decl_stmt|;
comment|/**          * The virtual host name          */
DECL|field|host
specifier|private
name|String
name|host
decl_stmt|;
comment|/**          * The username          */
DECL|field|login
specifier|private
name|String
name|login
decl_stmt|;
comment|/**          * The password          */
DECL|field|passcode
specifier|private
name|String
name|passcode
decl_stmt|;
comment|/**          * To configure security using SSLContextParameters          */
annotation|@
name|NestedConfigurationProperty
DECL|field|sslContextParameters
specifier|private
name|SSLContextParameters
name|sslContextParameters
decl_stmt|;
DECL|method|getBrokerURL ()
specifier|public
name|String
name|getBrokerURL
parameter_list|()
block|{
return|return
name|brokerURL
return|;
block|}
DECL|method|setBrokerURL (String brokerURL)
specifier|public
name|void
name|setBrokerURL
parameter_list|(
name|String
name|brokerURL
parameter_list|)
block|{
name|this
operator|.
name|brokerURL
operator|=
name|brokerURL
expr_stmt|;
block|}
DECL|method|getHost ()
specifier|public
name|String
name|getHost
parameter_list|()
block|{
return|return
name|host
return|;
block|}
DECL|method|setHost (String host)
specifier|public
name|void
name|setHost
parameter_list|(
name|String
name|host
parameter_list|)
block|{
name|this
operator|.
name|host
operator|=
name|host
expr_stmt|;
block|}
DECL|method|getLogin ()
specifier|public
name|String
name|getLogin
parameter_list|()
block|{
return|return
name|login
return|;
block|}
DECL|method|setLogin (String login)
specifier|public
name|void
name|setLogin
parameter_list|(
name|String
name|login
parameter_list|)
block|{
name|this
operator|.
name|login
operator|=
name|login
expr_stmt|;
block|}
DECL|method|getPasscode ()
specifier|public
name|String
name|getPasscode
parameter_list|()
block|{
return|return
name|passcode
return|;
block|}
DECL|method|setPasscode (String passcode)
specifier|public
name|void
name|setPasscode
parameter_list|(
name|String
name|passcode
parameter_list|)
block|{
name|this
operator|.
name|passcode
operator|=
name|passcode
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
block|}
block|}
end_class

end_unit

