begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.twitter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|twitter
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
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
name|java
operator|.
name|util
operator|.
name|Properties
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

begin_class
DECL|class|CamelTwitterTestSupport
specifier|public
class|class
name|CamelTwitterTestSupport
extends|extends
name|CamelTestSupport
block|{
DECL|field|consumerKey
specifier|protected
name|String
name|consumerKey
decl_stmt|;
DECL|field|consumerSecret
specifier|protected
name|String
name|consumerSecret
decl_stmt|;
DECL|field|accessToken
specifier|protected
name|String
name|accessToken
decl_stmt|;
DECL|field|accessTokenSecret
specifier|protected
name|String
name|accessTokenSecret
decl_stmt|;
DECL|method|CamelTwitterTestSupport ()
specifier|public
name|CamelTwitterTestSupport
parameter_list|()
block|{
name|Properties
name|properties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
comment|// Load from env
name|addProperty
argument_list|(
name|properties
argument_list|,
literal|"consumer.key"
argument_list|,
literal|"CAMEL_TWITTER_CONSUMER_KEY"
argument_list|)
expr_stmt|;
name|addProperty
argument_list|(
name|properties
argument_list|,
literal|"consumer.secret"
argument_list|,
literal|"CAMEL_TWITTER_CONSUMER_SECRET"
argument_list|)
expr_stmt|;
name|addProperty
argument_list|(
name|properties
argument_list|,
literal|"access.token"
argument_list|,
literal|"CAMEL_TWITTER_ACCESS_TOKEN"
argument_list|)
expr_stmt|;
name|addProperty
argument_list|(
name|properties
argument_list|,
literal|"access.token.secret"
argument_list|,
literal|"CAMEL_TWITTER_ACCESS_TOKE_SECRET"
argument_list|)
expr_stmt|;
comment|// if any of the properties is not set, load test-options.properties
if|if
condition|(
operator|!
name|properties
operator|.
name|containsKey
argument_list|(
literal|"consumer.key"
argument_list|)
operator|||
operator|!
name|properties
operator|.
name|containsKey
argument_list|(
literal|"consumer.secret"
argument_list|)
operator|||
operator|!
name|properties
operator|.
name|containsKey
argument_list|(
literal|"access.token"
argument_list|)
operator|||
operator|!
name|properties
operator|.
name|containsKey
argument_list|(
literal|"access.token.secret"
argument_list|)
condition|)
block|{
name|URL
name|url
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/test-options.properties"
argument_list|)
decl_stmt|;
try|try
init|(
name|InputStream
name|inStream
init|=
name|url
operator|.
name|openStream
argument_list|()
init|)
block|{
name|properties
operator|.
name|load
argument_list|(
name|inStream
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|IllegalAccessError
argument_list|(
literal|"test-options.properties could not be found"
argument_list|)
throw|;
block|}
block|}
name|consumerKey
operator|=
name|properties
operator|.
name|getProperty
argument_list|(
literal|"consumer.key"
argument_list|)
expr_stmt|;
name|consumerSecret
operator|=
name|properties
operator|.
name|getProperty
argument_list|(
literal|"consumer.secret"
argument_list|)
expr_stmt|;
name|accessToken
operator|=
name|properties
operator|.
name|getProperty
argument_list|(
literal|"access.token"
argument_list|)
expr_stmt|;
name|accessTokenSecret
operator|=
name|properties
operator|.
name|getProperty
argument_list|(
literal|"access.token.secret"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|consumerKey
argument_list|,
literal|"consumer.key"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|consumerSecret
argument_list|,
literal|"consumer.secret"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|accessToken
argument_list|,
literal|"access.token"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|accessTokenSecret
argument_list|,
literal|"access.token.secret"
argument_list|)
expr_stmt|;
block|}
DECL|method|getUriTokens ()
specifier|protected
name|String
name|getUriTokens
parameter_list|()
block|{
return|return
literal|"consumerKey="
operator|+
name|consumerKey
operator|+
literal|"&consumerSecret="
operator|+
name|consumerSecret
operator|+
literal|"&accessToken="
operator|+
name|accessToken
operator|+
literal|"&accessTokenSecret="
operator|+
name|accessTokenSecret
return|;
block|}
DECL|method|getParameters ()
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getParameters
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"consumerKey"
argument_list|,
name|this
operator|.
name|consumerKey
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"consumerSecret"
argument_list|,
name|this
operator|.
name|consumerSecret
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"accessToken"
argument_list|,
name|this
operator|.
name|accessToken
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"accessTokenSecret"
argument_list|,
name|this
operator|.
name|accessTokenSecret
argument_list|)
expr_stmt|;
return|return
name|parameters
return|;
block|}
DECL|method|addProperty (Properties properties, String name, String envName)
specifier|protected
name|void
name|addProperty
parameter_list|(
name|Properties
name|properties
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|envName
parameter_list|)
block|{
if|if
condition|(
operator|!
name|properties
operator|.
name|containsKey
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|String
name|value
init|=
name|System
operator|.
name|getenv
argument_list|(
name|envName
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|value
argument_list|)
condition|)
block|{
name|properties
operator|.
name|setProperty
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

