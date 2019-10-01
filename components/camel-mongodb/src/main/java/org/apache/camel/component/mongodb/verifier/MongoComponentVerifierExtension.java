begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mongodb.verifier
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mongodb
operator|.
name|verifier
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
name|com
operator|.
name|mongodb
operator|.
name|MongoClient
import|;
end_import

begin_import
import|import
name|com
operator|.
name|mongodb
operator|.
name|MongoClientOptions
import|;
end_import

begin_import
import|import
name|com
operator|.
name|mongodb
operator|.
name|MongoClientURI
import|;
end_import

begin_import
import|import
name|com
operator|.
name|mongodb
operator|.
name|MongoSecurityException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|mongodb
operator|.
name|MongoTimeoutException
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
name|extension
operator|.
name|verifier
operator|.
name|DefaultComponentVerifierExtension
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
name|extension
operator|.
name|verifier
operator|.
name|ResultBuilder
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
name|extension
operator|.
name|verifier
operator|.
name|ResultErrorBuilder
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
name|extension
operator|.
name|verifier
operator|.
name|ResultErrorHelper
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
name|mongodb
operator|.
name|conf
operator|.
name|MongoConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bson
operator|.
name|Document
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|CastUtils
operator|.
name|cast
import|;
end_import

begin_class
DECL|class|MongoComponentVerifierExtension
specifier|public
class|class
name|MongoComponentVerifierExtension
extends|extends
name|DefaultComponentVerifierExtension
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|MongoComponentVerifierExtension
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|CONNECTION_TIMEOUT
specifier|private
specifier|static
specifier|final
name|int
name|CONNECTION_TIMEOUT
init|=
literal|2000
decl_stmt|;
DECL|method|MongoComponentVerifierExtension ()
specifier|public
name|MongoComponentVerifierExtension
parameter_list|()
block|{
name|super
argument_list|(
literal|"mongodb"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|verifyParameters (Map<String, Object> parameters)
specifier|public
name|Result
name|verifyParameters
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
name|ResultBuilder
name|builder
init|=
name|ResultBuilder
operator|.
name|withStatusAndScope
argument_list|(
name|Result
operator|.
name|Status
operator|.
name|OK
argument_list|,
name|Scope
operator|.
name|PARAMETERS
argument_list|)
operator|.
name|error
argument_list|(
name|ResultErrorHelper
operator|.
name|requiresOption
argument_list|(
literal|"host"
argument_list|,
name|parameters
argument_list|)
argument_list|)
operator|.
name|error
argument_list|(
name|ResultErrorHelper
operator|.
name|requiresOption
argument_list|(
literal|"user"
argument_list|,
name|parameters
argument_list|)
argument_list|)
operator|.
name|error
argument_list|(
name|ResultErrorHelper
operator|.
name|requiresOption
argument_list|(
literal|"password"
argument_list|,
name|parameters
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|builder
operator|.
name|build
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|verifyConnectivity (Map<String, Object> parameters)
specifier|public
name|Result
name|verifyConnectivity
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
return|return
name|ResultBuilder
operator|.
name|withStatusAndScope
argument_list|(
name|Result
operator|.
name|Status
operator|.
name|OK
argument_list|,
name|Scope
operator|.
name|CONNECTIVITY
argument_list|)
operator|.
name|error
argument_list|(
name|parameters
argument_list|,
name|this
operator|::
name|verifyCredentials
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|verifyCredentials (ResultBuilder builder, Map<String, Object> parameters)
specifier|private
name|void
name|verifyCredentials
parameter_list|(
name|ResultBuilder
name|builder
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
block|{
name|MongoConfiguration
name|mongoConf
init|=
operator|new
name|MongoConfiguration
argument_list|(
name|cast
argument_list|(
name|parameters
argument_list|)
argument_list|)
decl_stmt|;
name|MongoClientOptions
operator|.
name|Builder
name|optionsBuilder
init|=
name|MongoClientOptions
operator|.
name|builder
argument_list|()
decl_stmt|;
comment|// Avoid retry and long timeout
name|optionsBuilder
operator|.
name|connectTimeout
argument_list|(
name|CONNECTION_TIMEOUT
argument_list|)
expr_stmt|;
name|optionsBuilder
operator|.
name|serverSelectionTimeout
argument_list|(
name|CONNECTION_TIMEOUT
argument_list|)
expr_stmt|;
name|optionsBuilder
operator|.
name|maxWaitTime
argument_list|(
name|CONNECTION_TIMEOUT
argument_list|)
expr_stmt|;
name|MongoClientURI
name|connectionURI
init|=
operator|new
name|MongoClientURI
argument_list|(
name|mongoConf
operator|.
name|getMongoClientURI
argument_list|()
argument_list|,
name|optionsBuilder
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Testing connection against {}"
argument_list|,
name|connectionURI
argument_list|)
expr_stmt|;
try|try
init|(
name|MongoClient
name|mongoClient
init|=
operator|new
name|MongoClient
argument_list|(
name|connectionURI
argument_list|)
init|)
block|{
comment|// Just ping the server
name|mongoClient
operator|.
name|getDatabase
argument_list|(
name|connectionURI
operator|.
name|getDatabase
argument_list|()
argument_list|)
operator|.
name|runCommand
argument_list|(
name|Document
operator|.
name|parse
argument_list|(
literal|"{ ping: 1 }"
argument_list|)
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Testing connection successful!"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MongoSecurityException
name|e
parameter_list|)
block|{
name|ResultErrorBuilder
name|errorBuilder
init|=
name|ResultErrorBuilder
operator|.
name|withCodeAndDescription
argument_list|(
name|VerificationError
operator|.
name|StandardCode
operator|.
name|AUTHENTICATION
argument_list|,
name|String
operator|.
name|format
argument_list|(
literal|"Unable to authenticate %s against %s authentication database!"
argument_list|,
name|mongoConf
operator|.
name|getUser
argument_list|()
argument_list|,
name|mongoConf
operator|.
name|getAdminDB
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|builder
operator|.
name|error
argument_list|(
name|errorBuilder
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MongoTimeoutException
name|e
parameter_list|)
block|{
comment|// When there is any connection exception, the driver tries to reconnect until timeout is reached
comment|// wrapping the original socket exception into a timeout exception
name|String
name|description
init|=
literal|null
decl_stmt|;
name|VerificationError
operator|.
name|StandardCode
name|code
init|=
name|VerificationError
operator|.
name|StandardCode
operator|.
name|GENERIC
decl_stmt|;
if|if
condition|(
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"com.mongodb.MongoSecurityException"
argument_list|)
condition|)
block|{
name|code
operator|=
name|VerificationError
operator|.
name|StandardCode
operator|.
name|AUTHENTICATION
expr_stmt|;
name|description
operator|=
name|String
operator|.
name|format
argument_list|(
literal|"Unable to authenticate %s against %s authentication database!"
argument_list|,
name|mongoConf
operator|.
name|getUser
argument_list|()
argument_list|,
name|mongoConf
operator|.
name|getAdminDB
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"com.mongodb.MongoSocket"
argument_list|)
operator|&&
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Exception"
argument_list|)
condition|)
block|{
name|description
operator|=
name|String
operator|.
name|format
argument_list|(
literal|"Unable to connect to %s!"
argument_list|,
name|mongoConf
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|description
operator|=
name|String
operator|.
name|format
argument_list|(
literal|"Generic exception while connecting to %s!"
argument_list|,
name|mongoConf
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|ResultErrorBuilder
name|errorBuilder
init|=
name|ResultErrorBuilder
operator|.
name|withCodeAndDescription
argument_list|(
name|code
argument_list|,
name|String
operator|.
name|format
argument_list|(
name|description
argument_list|)
argument_list|)
decl_stmt|;
name|builder
operator|.
name|error
argument_list|(
name|errorBuilder
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

