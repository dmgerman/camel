begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.nagios.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|nagios
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
name|com
operator|.
name|googlecode
operator|.
name|jsendnsca
operator|.
name|NagiosSettings
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|jsendnsca
operator|.
name|encryption
operator|.
name|Encryption
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
name|nagios
operator|.
name|NagiosEncryptionMethod
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
name|DeprecatedConfigurationProperty
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
comment|/**  * To send passive checks to Nagios using JSendNSCA.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
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
literal|"camel.component.nagios"
argument_list|)
DECL|class|NagiosComponentConfiguration
specifier|public
class|class
name|NagiosComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * To use a shared NagiosConfiguration      */
DECL|field|configuration
specifier|private
name|NagiosConfigurationNestedConfiguration
name|configuration
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
name|NagiosConfigurationNestedConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration ( NagiosConfigurationNestedConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|NagiosConfigurationNestedConfiguration
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
DECL|class|NagiosConfigurationNestedConfiguration
specifier|public
specifier|static
class|class
name|NagiosConfigurationNestedConfiguration
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
name|nagios
operator|.
name|NagiosConfiguration
operator|.
name|class
decl_stmt|;
annotation|@
name|NestedConfigurationProperty
DECL|field|nagiosSettings
specifier|private
name|NagiosSettings
name|nagiosSettings
decl_stmt|;
comment|/**          * This is the address of the Nagios host where checks should be send.          */
DECL|field|host
specifier|private
name|String
name|host
decl_stmt|;
comment|/**          * The port number of the host.          */
DECL|field|port
specifier|private
name|Integer
name|port
decl_stmt|;
comment|/**          * Connection timeout in millis.          */
DECL|field|connectionTimeout
specifier|private
name|Integer
name|connectionTimeout
init|=
literal|5000
decl_stmt|;
comment|/**          * Sending timeout in millis.          */
DECL|field|timeout
specifier|private
name|Integer
name|timeout
init|=
literal|5000
decl_stmt|;
comment|/**          * Password to be authenticated when sending checks to Nagios.          */
DECL|field|password
specifier|private
name|String
name|password
decl_stmt|;
comment|/**          * To specify an encryption method.          *           * @deprecated use the {@link #encryption} query parameter instead.          */
annotation|@
name|Deprecated
DECL|field|encryptionMethod
specifier|private
name|NagiosEncryptionMethod
name|encryptionMethod
decl_stmt|;
comment|/**          * To specify an encryption method.          */
DECL|field|encryption
specifier|private
name|Encryption
name|encryption
decl_stmt|;
DECL|method|getNagiosSettings ()
specifier|public
name|NagiosSettings
name|getNagiosSettings
parameter_list|()
block|{
return|return
name|nagiosSettings
return|;
block|}
DECL|method|setNagiosSettings (NagiosSettings nagiosSettings)
specifier|public
name|void
name|setNagiosSettings
parameter_list|(
name|NagiosSettings
name|nagiosSettings
parameter_list|)
block|{
name|this
operator|.
name|nagiosSettings
operator|=
name|nagiosSettings
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
DECL|method|getPort ()
specifier|public
name|Integer
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
DECL|method|setPort (Integer port)
specifier|public
name|void
name|setPort
parameter_list|(
name|Integer
name|port
parameter_list|)
block|{
name|this
operator|.
name|port
operator|=
name|port
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
DECL|method|getTimeout ()
specifier|public
name|Integer
name|getTimeout
parameter_list|()
block|{
return|return
name|timeout
return|;
block|}
DECL|method|setTimeout (Integer timeout)
specifier|public
name|void
name|setTimeout
parameter_list|(
name|Integer
name|timeout
parameter_list|)
block|{
name|this
operator|.
name|timeout
operator|=
name|timeout
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
annotation|@
name|Deprecated
annotation|@
name|DeprecatedConfigurationProperty
DECL|method|getEncryptionMethod ()
specifier|public
name|NagiosEncryptionMethod
name|getEncryptionMethod
parameter_list|()
block|{
return|return
name|encryptionMethod
return|;
block|}
annotation|@
name|Deprecated
DECL|method|setEncryptionMethod (NagiosEncryptionMethod encryptionMethod)
specifier|public
name|void
name|setEncryptionMethod
parameter_list|(
name|NagiosEncryptionMethod
name|encryptionMethod
parameter_list|)
block|{
name|this
operator|.
name|encryptionMethod
operator|=
name|encryptionMethod
expr_stmt|;
block|}
DECL|method|getEncryption ()
specifier|public
name|Encryption
name|getEncryption
parameter_list|()
block|{
return|return
name|encryption
return|;
block|}
DECL|method|setEncryption (Encryption encryption)
specifier|public
name|void
name|setEncryption
parameter_list|(
name|Encryption
name|encryption
parameter_list|)
block|{
name|this
operator|.
name|encryption
operator|=
name|encryption
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

