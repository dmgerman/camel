begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.linkedin.api
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|linkedin
operator|.
name|api
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

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
name|ws
operator|.
name|rs
operator|.
name|WebApplicationException
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
name|linkedin
operator|.
name|api
operator|.
name|model
operator|.
name|Error
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|Bus
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|BusFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|client
operator|.
name|JAXRSClientFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|client
operator|.
name|WebClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|AfterClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
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

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
operator|.
name|DAYS
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
operator|.
name|MILLISECONDS
import|;
end_import

begin_comment
comment|/**  * Base class for resource tests.  */
end_comment

begin_class
DECL|class|AbstractResourceIntegrationTest
specifier|public
specifier|abstract
class|class
name|AbstractResourceIntegrationTest
extends|extends
name|Assert
block|{
DECL|field|LOG
specifier|protected
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|PeopleResourceIntegrationTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|DEFAULT_FIELDS
specifier|protected
specifier|static
specifier|final
name|String
name|DEFAULT_FIELDS
init|=
literal|""
decl_stmt|;
DECL|field|DEFAULT_EXPIRY
specifier|public
specifier|static
specifier|final
name|long
name|DEFAULT_EXPIRY
init|=
name|MILLISECONDS
operator|.
name|convert
argument_list|(
literal|60
argument_list|,
name|DAYS
argument_list|)
operator|+
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
DECL|field|requestFilter
specifier|protected
specifier|static
name|LinkedInOAuthRequestFilter
name|requestFilter
decl_stmt|;
DECL|field|properties
specifier|protected
specifier|static
name|Properties
name|properties
decl_stmt|;
DECL|field|token
specifier|protected
specifier|static
name|OAuthToken
name|token
decl_stmt|;
DECL|field|resourceList
specifier|private
specifier|static
name|List
argument_list|<
name|Object
argument_list|>
name|resourceList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|BeforeClass
DECL|method|beforeClass ()
specifier|public
specifier|static
name|void
name|beforeClass
parameter_list|()
throws|throws
name|Exception
block|{
name|properties
operator|=
operator|new
name|Properties
argument_list|()
expr_stmt|;
name|properties
operator|.
name|load
argument_list|(
name|AbstractResourceIntegrationTest
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"/test-options.properties"
argument_list|)
argument_list|)
expr_stmt|;
name|requestFilter
operator|=
name|createOAuthHelper
argument_list|()
expr_stmt|;
block|}
DECL|method|createOAuthHelper ()
specifier|private
specifier|static
name|LinkedInOAuthRequestFilter
name|createOAuthHelper
parameter_list|()
throws|throws
name|IOException
block|{
specifier|final
name|String
name|userName
init|=
name|properties
operator|.
name|getProperty
argument_list|(
literal|"userName"
argument_list|)
decl_stmt|;
specifier|final
name|String
name|userPassword
init|=
name|properties
operator|.
name|getProperty
argument_list|(
literal|"userPassword"
argument_list|)
decl_stmt|;
specifier|final
name|String
name|clientId
init|=
name|properties
operator|.
name|getProperty
argument_list|(
literal|"clientId"
argument_list|)
decl_stmt|;
specifier|final
name|String
name|clientSecret
init|=
name|properties
operator|.
name|getProperty
argument_list|(
literal|"clientSecret"
argument_list|)
decl_stmt|;
specifier|final
name|String
name|redirectUri
init|=
name|properties
operator|.
name|getProperty
argument_list|(
literal|"redirectUri"
argument_list|)
decl_stmt|;
specifier|final
name|String
name|accessToken
init|=
name|properties
operator|.
name|getProperty
argument_list|(
literal|"accessToken"
argument_list|)
decl_stmt|;
specifier|final
name|String
name|expiryTime
init|=
name|properties
operator|.
name|getProperty
argument_list|(
literal|"expiryTime"
argument_list|)
decl_stmt|;
specifier|final
name|OAuthScope
index|[]
name|scopes
decl_stmt|;
specifier|final
name|String
name|scope
init|=
name|properties
operator|.
name|getProperty
argument_list|(
literal|"scope"
argument_list|)
decl_stmt|;
if|if
condition|(
name|scope
operator|!=
literal|null
condition|)
block|{
name|scopes
operator|=
name|OAuthScope
operator|.
name|fromValues
argument_list|(
name|scope
operator|.
name|split
argument_list|(
literal|","
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|scopes
operator|=
literal|null
expr_stmt|;
block|}
comment|// check if accessToken is set
if|if
condition|(
name|accessToken
operator|!=
literal|null
condition|)
block|{
name|token
operator|=
operator|new
name|OAuthToken
argument_list|(
literal|null
argument_list|,
name|accessToken
argument_list|,
operator|(
name|expiryTime
operator|!=
literal|null
operator|)
condition|?
name|Long
operator|.
name|parseLong
argument_list|(
name|expiryTime
argument_list|)
else|:
name|DEFAULT_EXPIRY
argument_list|)
expr_stmt|;
block|}
specifier|final
name|OAuthSecureStorage
name|secureStorage
init|=
operator|new
name|OAuthSecureStorage
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|OAuthToken
name|getOAuthToken
parameter_list|()
block|{
return|return
name|token
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|saveOAuthToken
parameter_list|(
name|OAuthToken
name|newToken
parameter_list|)
block|{
name|token
operator|=
name|newToken
expr_stmt|;
block|}
block|}
decl_stmt|;
specifier|final
name|OAuthParams
name|oAuthParams
init|=
operator|new
name|OAuthParams
argument_list|(
name|userName
argument_list|,
name|userPassword
argument_list|,
name|secureStorage
argument_list|,
name|clientId
argument_list|,
name|clientSecret
argument_list|,
name|redirectUri
argument_list|,
name|scopes
argument_list|)
decl_stmt|;
return|return
operator|new
name|LinkedInOAuthRequestFilter
argument_list|(
name|oAuthParams
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|,
literal|null
argument_list|)
return|;
block|}
annotation|@
name|AfterClass
DECL|method|afterClass ()
specifier|public
specifier|static
name|void
name|afterClass
parameter_list|()
throws|throws
name|Exception
block|{
comment|// close all proxies
for|for
control|(
name|Object
name|resource
range|:
name|resourceList
control|)
block|{
try|try
block|{
name|WebClient
operator|.
name|client
argument_list|(
name|resource
argument_list|)
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ignore
parameter_list|)
block|{             }
block|}
if|if
condition|(
name|requestFilter
operator|!=
literal|null
condition|)
block|{
name|requestFilter
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|// TODO save and load token from test-options.properties
block|}
DECL|method|getResource (Class<T> resourceClass)
specifier|protected
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|getResource
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|resourceClass
parameter_list|)
block|{
if|if
condition|(
name|requestFilter
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
name|AbstractResourceIntegrationTest
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".beforeClass must be invoked before getResource"
argument_list|)
throw|;
block|}
name|Bus
name|bus
init|=
name|BusFactory
operator|.
name|getThreadDefaultBus
argument_list|()
decl_stmt|;
name|bus
operator|.
name|setProperty
argument_list|(
literal|"allow.empty.path.template.value"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
specifier|final
name|T
name|resource
init|=
name|JAXRSClientFactory
operator|.
name|create
argument_list|(
name|LinkedInOAuthRequestFilter
operator|.
name|BASE_ADDRESS
argument_list|,
name|resourceClass
argument_list|,
comment|//            Arrays.asList(new Object[] { requestFilter, new LinkedInExceptionResponseFilter() } ));
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Object
index|[]
block|{
name|requestFilter
block|,
operator|new
name|EnumQueryParamConverterProvider
argument_list|()
block|}
argument_list|)
argument_list|)
decl_stmt|;
name|resourceList
operator|.
name|add
argument_list|(
name|resource
argument_list|)
expr_stmt|;
return|return
name|resource
return|;
block|}
DECL|method|execute (Runnable runnable)
specifier|protected
name|void
name|execute
parameter_list|(
name|Runnable
name|runnable
parameter_list|)
block|{
try|try
block|{
name|runnable
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WebApplicationException
name|e
parameter_list|)
block|{
specifier|final
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|linkedin
operator|.
name|api
operator|.
name|model
operator|.
name|Error
name|error
init|=
name|e
operator|.
name|getResponse
argument_list|()
operator|.
name|readEntity
argument_list|(
name|Error
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|error
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|error
argument_list|(
literal|"Error: {}"
argument_list|,
name|error
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|LinkedInException
argument_list|(
name|error
argument_list|,
name|e
operator|.
name|getResponse
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

