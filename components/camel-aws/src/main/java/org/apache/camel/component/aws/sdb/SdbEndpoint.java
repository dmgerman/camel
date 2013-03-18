begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.sdb
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|aws
operator|.
name|sdb
package|;
end_package

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|auth
operator|.
name|AWSCredentials
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|auth
operator|.
name|BasicAWSCredentials
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpledb
operator|.
name|AmazonSimpleDB
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpledb
operator|.
name|AmazonSimpleDBClient
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpledb
operator|.
name|model
operator|.
name|CreateDomainRequest
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpledb
operator|.
name|model
operator|.
name|DomainMetadataRequest
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpledb
operator|.
name|model
operator|.
name|NoSuchDomainException
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
name|component
operator|.
name|aws
operator|.
name|s3
operator|.
name|S3Endpoint
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

begin_comment
comment|/**  * Defines the<a href="http://camel.apache.org/aws.html">AWS SDB Endpoint</a>.    *  */
end_comment

begin_class
DECL|class|SdbEndpoint
specifier|public
class|class
name|SdbEndpoint
extends|extends
name|ScheduledPollEndpoint
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
name|S3Endpoint
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|configuration
specifier|private
name|SdbConfiguration
name|configuration
decl_stmt|;
annotation|@
name|Deprecated
DECL|method|SdbEndpoint (String uri, CamelContext context, SdbConfiguration configuration)
specifier|public
name|SdbEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|CamelContext
name|context
parameter_list|,
name|SdbConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|context
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|SdbEndpoint (String uri, Component component, SdbConfiguration configuration)
specifier|public
name|SdbEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|Component
name|component
parameter_list|,
name|SdbConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"You cannot receive messages from this endpoint"
argument_list|)
throw|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|SdbProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
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
annotation|@
name|Override
DECL|method|doStart ()
specifier|public
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|AmazonSimpleDB
name|sdbClient
init|=
name|getSdbClient
argument_list|()
decl_stmt|;
name|String
name|domainName
init|=
name|getConfiguration
argument_list|()
operator|.
name|getDomainName
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Querying whether domain [{}] already exists..."
argument_list|,
name|domainName
argument_list|)
expr_stmt|;
try|try
block|{
name|sdbClient
operator|.
name|domainMetadata
argument_list|(
operator|new
name|DomainMetadataRequest
argument_list|(
name|domainName
argument_list|)
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Domain [{}] already exists"
argument_list|,
name|domainName
argument_list|)
expr_stmt|;
return|return;
block|}
catch|catch
parameter_list|(
name|NoSuchDomainException
name|ase
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Domain [{}] doesn't exist yet"
argument_list|,
name|domainName
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Creating domain [{}]..."
argument_list|,
name|domainName
argument_list|)
expr_stmt|;
name|sdbClient
operator|.
name|createDomain
argument_list|(
operator|new
name|CreateDomainRequest
argument_list|(
name|domainName
argument_list|)
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Domain [{}] created"
argument_list|,
name|domainName
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getConfiguration ()
specifier|public
name|SdbConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|getSdbClient ()
specifier|public
name|AmazonSimpleDB
name|getSdbClient
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getAmazonSDBClient
argument_list|()
operator|!=
literal|null
condition|?
name|configuration
operator|.
name|getAmazonSDBClient
argument_list|()
else|:
name|createSdbClient
argument_list|()
return|;
block|}
DECL|method|createSdbClient ()
name|AmazonSimpleDB
name|createSdbClient
parameter_list|()
block|{
name|AWSCredentials
name|credentials
init|=
operator|new
name|BasicAWSCredentials
argument_list|(
name|configuration
operator|.
name|getAccessKey
argument_list|()
argument_list|,
name|configuration
operator|.
name|getSecretKey
argument_list|()
argument_list|)
decl_stmt|;
name|AmazonSimpleDB
name|client
init|=
operator|new
name|AmazonSimpleDBClient
argument_list|(
name|credentials
argument_list|)
decl_stmt|;
if|if
condition|(
name|configuration
operator|.
name|getAmazonSdbEndpoint
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|client
operator|.
name|setEndpoint
argument_list|(
name|configuration
operator|.
name|getAmazonSdbEndpoint
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|configuration
operator|.
name|setAmazonSDBClient
argument_list|(
name|client
argument_list|)
expr_stmt|;
return|return
name|client
return|;
block|}
block|}
end_class

end_unit

