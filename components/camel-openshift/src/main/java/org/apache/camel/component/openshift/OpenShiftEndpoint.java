begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.openshift
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|openshift
package|;
end_package

begin_import
import|import
name|com
operator|.
name|openshift
operator|.
name|client
operator|.
name|IApplication
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
name|Component
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
name|Consumer
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
name|Exchange
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
name|Processor
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
name|Producer
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
name|impl
operator|.
name|ScheduledPollEndpoint
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
name|UriEndpoint
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
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * To manage your Openshift 2.x applications.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.14.0"
argument_list|,
name|scheme
operator|=
literal|"openshift"
argument_list|,
name|title
operator|=
literal|"OpenShift"
argument_list|,
name|syntax
operator|=
literal|"openshift:clientId"
argument_list|,
name|consumerClass
operator|=
name|OpenShiftConsumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"cloud,paas"
argument_list|)
DECL|class|OpenShiftEndpoint
specifier|public
class|class
name|OpenShiftEndpoint
extends|extends
name|ScheduledPollEndpoint
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
DECL|field|clientId
specifier|private
name|String
name|clientId
decl_stmt|;
annotation|@
name|UriParam
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
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
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
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
name|UriParam
DECL|field|domain
specifier|private
name|String
name|domain
decl_stmt|;
annotation|@
name|UriParam
DECL|field|server
specifier|private
name|String
name|server
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|enums
operator|=
literal|"list,start,stop,restart,state,getStandaloneCartridge,getEmbeddedCartridges,addEmbeddedCartridge,removeEmbeddedCartridge,"
operator|+
literal|"scaleUp,scaleDown,getGitUrl,getDeploymentType,setDeploymentType,getAllEnvironmentVariables,addEnvironmentVariable,addMultipleEnvironmentVariables,"
operator|+
literal|"updateEnvironmentVariable,getEnvironmentVariableValue,removeEnvironmentVariable,getGearProfile,addAlias,removeAlias,getAliases"
argument_list|)
DECL|field|operation
specifier|private
name|String
name|operation
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|application
specifier|private
name|String
name|application
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|enums
operator|=
literal|"pojo,json"
argument_list|)
DECL|field|mode
specifier|private
name|String
name|mode
decl_stmt|;
DECL|method|OpenShiftEndpoint (String endpointUri, Component component)
specifier|public
name|OpenShiftEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjectHelper
operator|.
name|notEmpty
argument_list|(
name|clientId
argument_list|,
literal|"clientId"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notEmpty
argument_list|(
name|username
argument_list|,
literal|"username"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notEmpty
argument_list|(
name|password
argument_list|,
literal|"password"
argument_list|,
name|this
argument_list|)
expr_stmt|;
return|return
operator|new
name|OpenShiftProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|ObjectHelper
operator|.
name|notEmpty
argument_list|(
name|clientId
argument_list|,
literal|"clientId"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notEmpty
argument_list|(
name|username
argument_list|,
literal|"username"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notEmpty
argument_list|(
name|password
argument_list|,
literal|"password"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|Consumer
name|consumer
init|=
operator|new
name|OpenShiftConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|configureConsumer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
return|return
name|consumer
return|;
block|}
DECL|method|createExchange (IApplication application)
specifier|public
name|Exchange
name|createExchange
parameter_list|(
name|IApplication
name|application
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|super
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|application
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
annotation|@
name|Override
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
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
comment|/**      * The username to login to openshift server.      */
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
comment|/**      * The password for login to openshift server.      */
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
comment|/**      * The client id      */
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
DECL|method|getDomain ()
specifier|public
name|String
name|getDomain
parameter_list|()
block|{
return|return
name|domain
return|;
block|}
comment|/**      * Domain name. If not specified then the default domain is used.      */
DECL|method|setDomain (String domain)
specifier|public
name|void
name|setDomain
parameter_list|(
name|String
name|domain
parameter_list|)
block|{
name|this
operator|.
name|domain
operator|=
name|domain
expr_stmt|;
block|}
DECL|method|getServer ()
specifier|public
name|String
name|getServer
parameter_list|()
block|{
return|return
name|server
return|;
block|}
comment|/**      * Url to the openshift server.      * If not specified then the default value from the local openshift configuration file ~/.openshift/express.conf is used.      * And if that fails as well then "openshift.redhat.com" is used.      */
DECL|method|setServer (String server)
specifier|public
name|void
name|setServer
parameter_list|(
name|String
name|server
parameter_list|)
block|{
name|this
operator|.
name|server
operator|=
name|server
expr_stmt|;
block|}
DECL|method|getOperation ()
specifier|public
name|String
name|getOperation
parameter_list|()
block|{
return|return
name|operation
return|;
block|}
comment|/**      * The operation to perform which can be: list, start, stop, restart, and state.      * The list operation returns information about all the applications in json format.      * The state operation returns the state such as: started, stopped etc.      * The other operations does not return any value.      */
DECL|method|setOperation (String operation)
specifier|public
name|void
name|setOperation
parameter_list|(
name|String
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
DECL|method|setOperation (OpenShiftOperation operation)
specifier|public
name|void
name|setOperation
parameter_list|(
name|OpenShiftOperation
name|operation
parameter_list|)
block|{
name|this
operator|.
name|operation
operator|=
name|operation
operator|.
name|name
argument_list|()
expr_stmt|;
block|}
DECL|method|getApplication ()
specifier|public
name|String
name|getApplication
parameter_list|()
block|{
return|return
name|application
return|;
block|}
comment|/**      * The application name to start, stop, restart, or get the state.      */
DECL|method|setApplication (String application)
specifier|public
name|void
name|setApplication
parameter_list|(
name|String
name|application
parameter_list|)
block|{
name|this
operator|.
name|application
operator|=
name|application
expr_stmt|;
block|}
DECL|method|getMode ()
specifier|public
name|String
name|getMode
parameter_list|()
block|{
return|return
name|mode
return|;
block|}
comment|/**      * Whether to output the message body as a pojo or json. For pojo the message is a List<com.openshift.client.IApplication> type.      */
DECL|method|setMode (String mode)
specifier|public
name|void
name|setMode
parameter_list|(
name|String
name|mode
parameter_list|)
block|{
name|this
operator|.
name|mode
operator|=
name|mode
expr_stmt|;
block|}
block|}
end_class

end_unit

