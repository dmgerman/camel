begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.stomp
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

begin_class
annotation|@
name|UriParams
DECL|class|StompConfiguration
specifier|public
class|class
name|StompConfiguration
implements|implements
name|Cloneable
block|{
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"tcp://localhost:61613"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|brokerURL
specifier|private
name|String
name|brokerURL
init|=
literal|"tcp://localhost:61613"
decl_stmt|;
annotation|@
name|UriParam
DECL|field|login
specifier|private
name|String
name|login
decl_stmt|;
annotation|@
name|UriParam
DECL|field|passcode
specifier|private
name|String
name|passcode
decl_stmt|;
annotation|@
name|UriParam
DECL|field|host
specifier|private
name|String
name|host
decl_stmt|;
comment|/**      * Returns a copy of this configuration      */
DECL|method|copy ()
specifier|public
name|StompConfiguration
name|copy
parameter_list|()
block|{
try|try
block|{
name|StompConfiguration
name|copy
init|=
operator|(
name|StompConfiguration
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
comment|/**      * The URI of the Stomp broker to connect to      */
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
comment|/**      * The username      */
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
comment|/**      * The password      */
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
block|}
end_class

end_unit

