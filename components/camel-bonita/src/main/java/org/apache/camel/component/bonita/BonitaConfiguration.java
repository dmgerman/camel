begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.bonita
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|bonita
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|bonita
operator|.
name|util
operator|.
name|BonitaOperation
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

begin_class
annotation|@
name|UriParams
DECL|class|BonitaConfiguration
specifier|public
class|class
name|BonitaConfiguration
implements|implements
name|Cloneable
block|{
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|operation
specifier|private
name|BonitaOperation
name|operation
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"localhost"
argument_list|)
DECL|field|hostname
specifier|private
name|String
name|hostname
init|=
literal|"localhost"
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"8080"
argument_list|)
DECL|field|port
specifier|private
name|String
name|port
init|=
literal|"8080"
decl_stmt|;
annotation|@
name|UriParam
DECL|field|processName
specifier|private
name|String
name|processName
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
DECL|field|username
specifier|private
name|String
name|username
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
DECL|method|getHostname ()
specifier|public
name|String
name|getHostname
parameter_list|()
block|{
return|return
name|hostname
return|;
block|}
comment|/**      * Hostname where Bonita engine runs      */
DECL|method|setHostname (String hostname)
specifier|public
name|void
name|setHostname
parameter_list|(
name|String
name|hostname
parameter_list|)
block|{
name|this
operator|.
name|hostname
operator|=
name|hostname
expr_stmt|;
block|}
DECL|method|getPort ()
specifier|public
name|String
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
comment|/**      * Port of the server hosting Bonita engine      */
DECL|method|setPort (String port)
specifier|public
name|void
name|setPort
parameter_list|(
name|String
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
DECL|method|getProcessName ()
specifier|public
name|String
name|getProcessName
parameter_list|()
block|{
return|return
name|processName
return|;
block|}
comment|/**      * Name of the process involved in the operation      */
DECL|method|setProcessName (String processName)
specifier|public
name|void
name|setProcessName
parameter_list|(
name|String
name|processName
parameter_list|)
block|{
name|this
operator|.
name|processName
operator|=
name|processName
expr_stmt|;
block|}
DECL|method|getOperation ()
specifier|public
name|BonitaOperation
name|getOperation
parameter_list|()
block|{
return|return
name|operation
return|;
block|}
comment|/**      * Operation to use      */
DECL|method|setOperation (BonitaOperation operation)
specifier|public
name|void
name|setOperation
parameter_list|(
name|BonitaOperation
name|operation
parameter_list|)
block|{
name|this
operator|.
name|operation
operator|=
name|operation
expr_stmt|;
block|}
DECL|method|setParameters (Map<String, Object> parameters)
specifier|public
name|void
name|setParameters
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
block|{
if|if
condition|(
name|parameters
operator|.
name|get
argument_list|(
literal|"hostname"
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|hostname
operator|=
operator|(
name|String
operator|)
name|parameters
operator|.
name|get
argument_list|(
literal|"hostname"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|parameters
operator|.
name|get
argument_list|(
literal|"port"
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|port
operator|=
operator|(
name|String
operator|)
name|parameters
operator|.
name|get
argument_list|(
literal|"port"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|parameters
operator|.
name|get
argument_list|(
literal|"processName"
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|processName
operator|=
operator|(
name|String
operator|)
name|parameters
operator|.
name|get
argument_list|(
literal|"processName"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getUsername ()
specifier|public
name|String
name|getUsername
parameter_list|()
block|{
return|return
name|username
return|;
block|}
comment|/**      * Username to authenticate to Bonita engine.      */
DECL|method|setUsername (String username)
specifier|public
name|void
name|setUsername
parameter_list|(
name|String
name|username
parameter_list|)
block|{
name|this
operator|.
name|username
operator|=
name|username
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
comment|/**      * Password to authenticate to Bonita engine.      */
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
block|}
end_class

end_unit

