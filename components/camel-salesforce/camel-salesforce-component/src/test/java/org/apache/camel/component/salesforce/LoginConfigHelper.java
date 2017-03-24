begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|salesforce
package|;
end_package

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
name|java
operator|.
name|util
operator|.
name|MissingResourceException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ResourceBundle
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
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
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|jsse
operator|.
name|KeyStoreParameters
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
operator|.
name|isNotEmpty
import|;
end_import

begin_class
DECL|class|LoginConfigHelper
specifier|public
specifier|final
class|class
name|LoginConfigHelper
block|{
DECL|field|INSTANCE
specifier|private
specifier|static
specifier|final
name|LoginConfigHelper
name|INSTANCE
init|=
operator|new
name|LoginConfigHelper
argument_list|()
decl_stmt|;
DECL|field|configuration
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|configuration
decl_stmt|;
DECL|method|LoginConfigHelper ()
specifier|private
name|LoginConfigHelper
parameter_list|()
block|{
name|configuration
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
try|try
block|{
specifier|final
name|ResourceBundle
name|properties
init|=
name|ResourceBundle
operator|.
name|getBundle
argument_list|(
literal|"test-salesforce-login"
argument_list|)
decl_stmt|;
name|properties
operator|.
name|keySet
argument_list|()
operator|.
name|forEach
argument_list|(
name|k
lambda|->
name|configuration
operator|.
name|put
argument_list|(
name|k
argument_list|,
name|properties
operator|.
name|getString
argument_list|(
name|k
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
specifier|final
name|MissingResourceException
name|ignored
parameter_list|)
block|{
comment|// ignoring if missing
block|}
name|System
operator|.
name|getenv
argument_list|()
operator|.
name|keySet
argument_list|()
operator|.
name|stream
argument_list|()
comment|//
operator|.
name|filter
argument_list|(
name|k
lambda|->
name|k
operator|.
name|startsWith
argument_list|(
literal|"SALESFORCE_"
argument_list|)
operator|&&
name|isNotEmpty
argument_list|(
name|System
operator|.
name|getenv
argument_list|(
name|k
argument_list|)
argument_list|)
argument_list|)
operator|.
name|forEach
argument_list|(
name|k
lambda|->
name|configuration
operator|.
name|put
argument_list|(
name|fromEnvName
argument_list|(
name|k
argument_list|)
argument_list|,
name|System
operator|.
name|getenv
argument_list|(
name|k
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|System
operator|.
name|getProperties
argument_list|()
operator|.
name|keySet
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|String
operator|.
name|class
operator|::
name|cast
argument_list|)
operator|.
name|filter
argument_list|(
name|k
lambda|->
name|k
operator|.
name|startsWith
argument_list|(
literal|"salesforce."
argument_list|)
operator|&&
name|isNotEmpty
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
name|k
argument_list|)
argument_list|)
argument_list|)
operator|.
name|forEach
argument_list|(
name|k
lambda|->
name|configuration
operator|.
name|put
argument_list|(
name|k
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
name|k
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|fromEnvName (final String envVariable)
specifier|private
name|String
name|fromEnvName
parameter_list|(
specifier|final
name|String
name|envVariable
parameter_list|)
block|{
return|return
name|envVariable
operator|.
name|replaceAll
argument_list|(
literal|"_"
argument_list|,
literal|"."
argument_list|)
operator|.
name|toLowerCase
argument_list|()
return|;
block|}
DECL|method|createLoginConfig ()
name|SalesforceLoginConfig
name|createLoginConfig
parameter_list|()
block|{
specifier|final
name|SalesforceLoginConfig
name|loginConfig
init|=
operator|new
name|SalesforceLoginConfig
argument_list|()
decl_stmt|;
specifier|final
name|String
name|explicitType
init|=
name|configuration
operator|.
name|get
argument_list|(
literal|"salesforce.auth.type"
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|explicitType
argument_list|)
condition|)
block|{
name|loginConfig
operator|.
name|setType
argument_list|(
name|AuthenticationType
operator|.
name|valueOf
argument_list|(
name|explicitType
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|loginConfig
operator|.
name|setClientId
argument_list|(
name|configuration
operator|.
name|get
argument_list|(
literal|"salesforce.client.id"
argument_list|)
argument_list|)
expr_stmt|;
name|loginConfig
operator|.
name|setClientSecret
argument_list|(
name|configuration
operator|.
name|get
argument_list|(
literal|"salesforce.client.secret"
argument_list|)
argument_list|)
expr_stmt|;
name|loginConfig
operator|.
name|setUserName
argument_list|(
name|configuration
operator|.
name|get
argument_list|(
literal|"salesforce.username"
argument_list|)
argument_list|)
expr_stmt|;
name|loginConfig
operator|.
name|setPassword
argument_list|(
name|configuration
operator|.
name|get
argument_list|(
literal|"salesforce.password"
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
name|KeyStoreParameters
name|keystore
init|=
operator|new
name|KeyStoreParameters
argument_list|()
decl_stmt|;
name|keystore
operator|.
name|setResource
argument_list|(
name|configuration
operator|.
name|get
argument_list|(
literal|"salesforce.keystore.resource"
argument_list|)
argument_list|)
expr_stmt|;
name|keystore
operator|.
name|setPassword
argument_list|(
name|configuration
operator|.
name|get
argument_list|(
literal|"salesforce.keystore.password"
argument_list|)
argument_list|)
expr_stmt|;
name|keystore
operator|.
name|setType
argument_list|(
name|configuration
operator|.
name|get
argument_list|(
literal|"salesforce.keystore.type"
argument_list|)
argument_list|)
expr_stmt|;
name|keystore
operator|.
name|setProvider
argument_list|(
name|configuration
operator|.
name|get
argument_list|(
literal|"salesforce.keystore.provider"
argument_list|)
argument_list|)
expr_stmt|;
name|loginConfig
operator|.
name|setKeystore
argument_list|(
name|keystore
argument_list|)
expr_stmt|;
name|validate
argument_list|(
name|loginConfig
argument_list|)
expr_stmt|;
return|return
name|loginConfig
return|;
block|}
DECL|method|validate (final SalesforceLoginConfig loginConfig)
name|void
name|validate
parameter_list|(
specifier|final
name|SalesforceLoginConfig
name|loginConfig
parameter_list|)
block|{
try|try
block|{
name|loginConfig
operator|.
name|validate
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
specifier|final
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"To run integration tests Salesforce Authentication information is"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"needed."
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"You need to specify the configuration for running tests by either"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"specifying environment variables, Maven properties or create a Java"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"properties file at:"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"camel/components/camel-salesforce/test-salesforce-login.properties"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"With authentication information to access a Salesforce instance."
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"You can use:"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"camel/components/camel-salesforce/test-salesforce-login.properties.sample"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"as reference. A free Salesforce developer account can be obtained at:"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"https://developer.salesforce.com"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Properties that you need to set:"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"| Maven or properties file     | Environment variable         | Use    |"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"|------------------------------+------------------------------+--------|"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"| salesforce.client.id         | SALESFORCE_CLIENT_ID         | ALL    |"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"| salesforce.client.secret     | SALESFORCE_CLIENT_SECRET     | UP, RT |"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"| salesforce.username          | SALESFORCE_USERNAME          | UP, JWT|"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"| salesforce.password          | SALESFORCE_PASSWORD          | UP     |"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"| salesforce.refreshToken      | SALESFORCE_REFRESH_TOKEN     | RT     |"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"| salesforce.keystore.path     | SALESFORCE_KEYSTORE_PATH     | JWT    |"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"| salesforce.keystore.type     | SALESFORCE_KEYSTORE_TYPE     | JWT    |"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"| salesforce.keystore.password | SALESFORCE_KEYSTORE_PASSWORD | JWT    |"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"| salesforce.login.url         | SALESFORCE_LOGIN_URL         | ALL    |"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"* ALL - required allways"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"* UP  - when using username and password authentication"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"* RT  - when using refresh token flow"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"* JWT - when using JWT flow"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"You can force authentication type to be one of USERNAME_PASSWORD,"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"REFRESH_TOKEN or JWT by setting `salesforce.auth.type` (or "
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"`SALESFORCE_AUTH_TYPE` for environment variables)."
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Examples:"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Using environment:"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"$ export SALESFORCE_CLIENT_ID=..."
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"$ export SALESFORCE_CLIENT_SECRET=..."
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"$ export ...others..."
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"or using Maven properties:"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"$ mvn -Pintegration -Dsalesforce.client.id=... \\"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  -Dsalesforce.client.secret=... ..."
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|getLoginConfig ()
specifier|public
specifier|static
name|SalesforceLoginConfig
name|getLoginConfig
parameter_list|()
block|{
return|return
name|INSTANCE
operator|.
name|createLoginConfig
argument_list|()
return|;
block|}
block|}
end_class

end_unit

