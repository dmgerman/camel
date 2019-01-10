begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.nagios
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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
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
name|RuntimeCamelException
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
name|Metadata
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
name|UriParam
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
name|UriParams
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
name|UriPath
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
name|StringHelper
import|;
end_import

begin_class
annotation|@
name|UriParams
DECL|class|NagiosConfiguration
specifier|public
class|class
name|NagiosConfiguration
implements|implements
name|Cloneable
block|{
DECL|field|nagiosSettings
specifier|private
specifier|transient
name|NagiosSettings
name|nagiosSettings
decl_stmt|;
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|host
specifier|private
name|String
name|host
decl_stmt|;
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|port
specifier|private
name|int
name|port
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"5000"
argument_list|)
DECL|field|connectionTimeout
specifier|private
name|int
name|connectionTimeout
init|=
literal|5000
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"5000"
argument_list|)
DECL|field|timeout
specifier|private
name|int
name|timeout
init|=
literal|5000
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|password
specifier|private
name|String
name|password
decl_stmt|;
annotation|@
name|Deprecated
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|)
DECL|field|encryptionMethod
specifier|private
name|NagiosEncryptionMethod
name|encryptionMethod
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|)
DECL|field|encryption
specifier|private
name|Encryption
name|encryption
init|=
name|Encryption
operator|.
name|NONE
decl_stmt|;
comment|/**      * Returns a copy of this configuration      */
DECL|method|copy ()
specifier|public
name|NagiosConfiguration
name|copy
parameter_list|()
block|{
try|try
block|{
name|NagiosConfiguration
name|copy
init|=
operator|(
name|NagiosConfiguration
operator|)
name|clone
argument_list|()
decl_stmt|;
return|return
name|copy
return|;
block|}
catch|catch
parameter_list|(
name|CloneNotSupportedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|configure (URI uri)
specifier|public
name|void
name|configure
parameter_list|(
name|URI
name|uri
parameter_list|)
block|{
name|String
name|value
init|=
name|uri
operator|.
name|getHost
argument_list|()
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|setHost
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
name|int
name|port
init|=
name|uri
operator|.
name|getPort
argument_list|()
decl_stmt|;
if|if
condition|(
name|port
operator|>
literal|0
condition|)
block|{
name|setPort
argument_list|(
name|port
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getNagiosSettings ()
specifier|public
specifier|synchronized
name|NagiosSettings
name|getNagiosSettings
parameter_list|()
block|{
if|if
condition|(
name|nagiosSettings
operator|==
literal|null
condition|)
block|{
comment|// validate parameters
name|StringHelper
operator|.
name|notEmpty
argument_list|(
name|host
argument_list|,
literal|"host"
argument_list|,
name|this
argument_list|)
expr_stmt|;
if|if
condition|(
name|port
operator|<=
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Port must be a positive number on "
operator|+
name|this
argument_list|)
throw|;
block|}
comment|// create settings
name|nagiosSettings
operator|=
operator|new
name|NagiosSettings
argument_list|()
expr_stmt|;
name|nagiosSettings
operator|.
name|setConnectTimeout
argument_list|(
name|getConnectionTimeout
argument_list|()
argument_list|)
expr_stmt|;
name|nagiosSettings
operator|.
name|setTimeout
argument_list|(
name|getTimeout
argument_list|()
argument_list|)
expr_stmt|;
name|nagiosSettings
operator|.
name|setNagiosHost
argument_list|(
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
name|nagiosSettings
operator|.
name|setPort
argument_list|(
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|nagiosSettings
operator|.
name|setPassword
argument_list|(
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
name|nagiosSettings
operator|.
name|setEncryption
argument_list|(
name|encryption
argument_list|)
expr_stmt|;
block|}
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
comment|/**      * This is the address of the Nagios host where checks should be send.      */
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
name|int
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
comment|/**      * The port number of the host.      */
DECL|method|setPort (int port)
specifier|public
name|void
name|setPort
parameter_list|(
name|int
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
name|int
name|getConnectionTimeout
parameter_list|()
block|{
return|return
name|connectionTimeout
return|;
block|}
comment|/**      * Connection timeout in millis.      */
DECL|method|setConnectionTimeout (int connectionTimeout)
specifier|public
name|void
name|setConnectionTimeout
parameter_list|(
name|int
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
name|int
name|getTimeout
parameter_list|()
block|{
return|return
name|timeout
return|;
block|}
comment|/**      * Sending timeout in millis.      */
DECL|method|setTimeout (int timeout)
specifier|public
name|void
name|setTimeout
parameter_list|(
name|int
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
comment|/**      * Password to be authenticated when sending checks to Nagios.      */
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
comment|/**      * To specify an encryption method.      * @deprecated use the {@link #encryption} query parameter instead.      */
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
comment|/**      * To specify an encryption method.      */
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
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"NagiosConfiguration[host="
operator|+
name|host
operator|+
literal|":"
operator|+
name|port
operator|+
literal|", connectionTimeout="
operator|+
name|connectionTimeout
operator|+
literal|", timeout="
operator|+
name|timeout
operator|+
literal|", encryptionMethod="
operator|+
name|encryptionMethod
operator|+
literal|", encryption="
operator|+
name|encryption
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

