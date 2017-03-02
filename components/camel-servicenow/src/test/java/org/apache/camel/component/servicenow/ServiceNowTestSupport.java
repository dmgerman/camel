begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.servicenow
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|servicenow
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

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
name|CamelContext
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
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

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
DECL|class|ServiceNowTestSupport
class|class
name|ServiceNowTestSupport
extends|extends
name|CamelTestSupport
block|{
DECL|field|LOGGER
specifier|protected
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ServiceNowTestSupport
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|configureServicenowComponent
argument_list|(
name|super
operator|.
name|createCamelContext
argument_list|()
argument_list|)
return|;
block|}
DECL|method|configureServicenowComponent (CamelContext camelContext)
specifier|protected
name|CamelContext
name|configureServicenowComponent
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|userName
init|=
name|getSystemPropertyOrEnvVar
argument_list|(
literal|"servicenow.username"
argument_list|)
decl_stmt|;
name|String
name|password
init|=
name|getSystemPropertyOrEnvVar
argument_list|(
literal|"servicenow.password"
argument_list|)
decl_stmt|;
name|String
name|oauthClientId
init|=
name|getSystemPropertyOrEnvVar
argument_list|(
literal|"servicenow.oauth2.client.id"
argument_list|)
decl_stmt|;
name|String
name|oauthClientSecret
init|=
name|getSystemPropertyOrEnvVar
argument_list|(
literal|"servicenow.oauth2.client.secret"
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|userName
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|password
argument_list|)
condition|)
block|{
name|ServiceNowComponent
name|component
init|=
operator|new
name|ServiceNowComponent
argument_list|()
decl_stmt|;
name|component
operator|.
name|setUserName
argument_list|(
name|userName
argument_list|)
expr_stmt|;
name|component
operator|.
name|setPassword
argument_list|(
name|password
argument_list|)
expr_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|oauthClientId
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|oauthClientSecret
argument_list|)
condition|)
block|{
name|component
operator|.
name|setOauthClientId
argument_list|(
name|oauthClientId
argument_list|)
expr_stmt|;
name|component
operator|.
name|setOauthClientSecret
argument_list|(
name|oauthClientSecret
argument_list|)
expr_stmt|;
block|}
name|camelContext
operator|.
name|addComponent
argument_list|(
literal|"servicenow"
argument_list|,
name|component
argument_list|)
expr_stmt|;
block|}
return|return
name|camelContext
return|;
block|}
DECL|method|getSystemPropertyOrEnvVar (String systemProperty)
specifier|protected
name|String
name|getSystemPropertyOrEnvVar
parameter_list|(
name|String
name|systemProperty
parameter_list|)
block|{
name|String
name|answer
init|=
name|System
operator|.
name|getProperty
argument_list|(
name|systemProperty
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|answer
argument_list|)
condition|)
block|{
name|String
name|envProperty
init|=
name|systemProperty
operator|.
name|toUpperCase
argument_list|()
operator|.
name|replaceAll
argument_list|(
literal|"[.-]"
argument_list|,
literal|"_"
argument_list|)
decl_stmt|;
name|answer
operator|=
name|System
operator|.
name|getenv
argument_list|(
name|envProperty
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|kvBuilder ()
specifier|protected
specifier|static
name|KVBuilder
name|kvBuilder
parameter_list|()
block|{
return|return
operator|new
name|KVBuilder
argument_list|(
operator|new
name|HashMap
argument_list|<>
argument_list|()
argument_list|)
return|;
block|}
DECL|method|kvBuilder (Map<String, Object> headers)
specifier|protected
specifier|static
name|KVBuilder
name|kvBuilder
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|)
block|{
return|return
operator|new
name|KVBuilder
argument_list|(
name|headers
argument_list|)
return|;
block|}
DECL|class|KVBuilder
specifier|protected
specifier|static
specifier|final
class|class
name|KVBuilder
block|{
DECL|field|headers
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
decl_stmt|;
DECL|method|KVBuilder (Map<String, Object> headers)
specifier|private
name|KVBuilder
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|)
block|{
name|this
operator|.
name|headers
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|headers
argument_list|)
expr_stmt|;
block|}
DECL|method|put (String key, Object val)
specifier|public
name|KVBuilder
name|put
parameter_list|(
name|String
name|key
parameter_list|,
name|Object
name|val
parameter_list|)
block|{
name|headers
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|val
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|put (ServiceNowParam key, Object val)
specifier|public
name|KVBuilder
name|put
parameter_list|(
name|ServiceNowParam
name|key
parameter_list|,
name|Object
name|val
parameter_list|)
block|{
name|headers
operator|.
name|put
argument_list|(
name|key
operator|.
name|getHeader
argument_list|()
argument_list|,
name|val
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|build ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|build
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|this
operator|.
name|headers
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

