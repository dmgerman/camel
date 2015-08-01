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
name|services
operator|.
name|simpledb
operator|.
name|AmazonSimpleDB
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
DECL|class|SdbConfiguration
specifier|public
class|class
name|SdbConfiguration
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
DECL|field|domainName
specifier|private
name|String
name|domainName
decl_stmt|;
annotation|@
name|UriParam
DECL|field|amazonSDBClient
specifier|private
name|AmazonSimpleDB
name|amazonSDBClient
decl_stmt|;
annotation|@
name|UriParam
DECL|field|accessKey
specifier|private
name|String
name|accessKey
decl_stmt|;
annotation|@
name|UriParam
DECL|field|secretKey
specifier|private
name|String
name|secretKey
decl_stmt|;
annotation|@
name|UriParam
DECL|field|amazonSdbEndpoint
specifier|private
name|String
name|amazonSdbEndpoint
decl_stmt|;
annotation|@
name|UriParam
DECL|field|maxNumberOfDomains
specifier|private
name|Integer
name|maxNumberOfDomains
decl_stmt|;
annotation|@
name|UriParam
DECL|field|consistentRead
specifier|private
name|boolean
name|consistentRead
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"PutAttributes"
argument_list|)
DECL|field|operation
specifier|private
name|SdbOperations
name|operation
init|=
name|SdbOperations
operator|.
name|PutAttributes
decl_stmt|;
annotation|@
name|UriParam
DECL|field|proxyHost
specifier|private
name|String
name|proxyHost
decl_stmt|;
annotation|@
name|UriParam
DECL|field|proxyPort
specifier|private
name|Integer
name|proxyPort
decl_stmt|;
comment|/**      * The region with which the AWS-SDB client wants to work with.      */
DECL|method|setAmazonSdbEndpoint (String amazonSdbEndpoint)
specifier|public
name|void
name|setAmazonSdbEndpoint
parameter_list|(
name|String
name|amazonSdbEndpoint
parameter_list|)
block|{
name|this
operator|.
name|amazonSdbEndpoint
operator|=
name|amazonSdbEndpoint
expr_stmt|;
block|}
DECL|method|getAmazonSdbEndpoint ()
specifier|public
name|String
name|getAmazonSdbEndpoint
parameter_list|()
block|{
return|return
name|amazonSdbEndpoint
return|;
block|}
DECL|method|getAccessKey ()
specifier|public
name|String
name|getAccessKey
parameter_list|()
block|{
return|return
name|accessKey
return|;
block|}
comment|/**      * Amazon AWS Access Key      */
DECL|method|setAccessKey (String accessKey)
specifier|public
name|void
name|setAccessKey
parameter_list|(
name|String
name|accessKey
parameter_list|)
block|{
name|this
operator|.
name|accessKey
operator|=
name|accessKey
expr_stmt|;
block|}
DECL|method|getSecretKey ()
specifier|public
name|String
name|getSecretKey
parameter_list|()
block|{
return|return
name|secretKey
return|;
block|}
comment|/**      * Amazon AWS Secret Key      */
DECL|method|setSecretKey (String secretKey)
specifier|public
name|void
name|setSecretKey
parameter_list|(
name|String
name|secretKey
parameter_list|)
block|{
name|this
operator|.
name|secretKey
operator|=
name|secretKey
expr_stmt|;
block|}
DECL|method|getAmazonSDBClient ()
specifier|public
name|AmazonSimpleDB
name|getAmazonSDBClient
parameter_list|()
block|{
return|return
name|amazonSDBClient
return|;
block|}
comment|/**      * To use the AmazonSimpleDB as the client      */
DECL|method|setAmazonSDBClient (AmazonSimpleDB amazonSDBClient)
specifier|public
name|void
name|setAmazonSDBClient
parameter_list|(
name|AmazonSimpleDB
name|amazonSDBClient
parameter_list|)
block|{
name|this
operator|.
name|amazonSDBClient
operator|=
name|amazonSDBClient
expr_stmt|;
block|}
DECL|method|getDomainName ()
specifier|public
name|String
name|getDomainName
parameter_list|()
block|{
return|return
name|domainName
return|;
block|}
comment|/**      * The name of the domain currently worked with.      */
DECL|method|setDomainName (String domainName)
specifier|public
name|void
name|setDomainName
parameter_list|(
name|String
name|domainName
parameter_list|)
block|{
name|this
operator|.
name|domainName
operator|=
name|domainName
expr_stmt|;
block|}
DECL|method|getOperation ()
specifier|public
name|SdbOperations
name|getOperation
parameter_list|()
block|{
return|return
name|operation
return|;
block|}
comment|/**      * Operation to perform      */
DECL|method|setOperation (SdbOperations operation)
specifier|public
name|void
name|setOperation
parameter_list|(
name|SdbOperations
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
DECL|method|getMaxNumberOfDomains ()
specifier|public
name|Integer
name|getMaxNumberOfDomains
parameter_list|()
block|{
return|return
name|maxNumberOfDomains
return|;
block|}
comment|/**      * The maximum number of domain names you want returned. The range is 1 to 100.      */
DECL|method|setMaxNumberOfDomains (Integer maxNumberOfDomains)
specifier|public
name|void
name|setMaxNumberOfDomains
parameter_list|(
name|Integer
name|maxNumberOfDomains
parameter_list|)
block|{
name|this
operator|.
name|maxNumberOfDomains
operator|=
name|maxNumberOfDomains
expr_stmt|;
block|}
DECL|method|isConsistentRead ()
specifier|public
name|boolean
name|isConsistentRead
parameter_list|()
block|{
return|return
name|consistentRead
return|;
block|}
comment|/**      * Determines whether or not strong consistency should be enforced when data is read.      */
DECL|method|setConsistentRead (boolean consistentRead)
specifier|public
name|void
name|setConsistentRead
parameter_list|(
name|boolean
name|consistentRead
parameter_list|)
block|{
name|this
operator|.
name|consistentRead
operator|=
name|consistentRead
expr_stmt|;
block|}
comment|/**      * To define a proxy host when instantiating the SQS client      */
DECL|method|getProxyHost ()
specifier|public
name|String
name|getProxyHost
parameter_list|()
block|{
return|return
name|proxyHost
return|;
block|}
DECL|method|setProxyHost (String proxyHost)
specifier|public
name|void
name|setProxyHost
parameter_list|(
name|String
name|proxyHost
parameter_list|)
block|{
name|this
operator|.
name|proxyHost
operator|=
name|proxyHost
expr_stmt|;
block|}
comment|/**      * To define a proxy port when instantiating the SQS client      */
DECL|method|getProxyPort ()
specifier|public
name|Integer
name|getProxyPort
parameter_list|()
block|{
return|return
name|proxyPort
return|;
block|}
DECL|method|setProxyPort (Integer proxyPort)
specifier|public
name|void
name|setProxyPort
parameter_list|(
name|Integer
name|proxyPort
parameter_list|)
block|{
name|this
operator|.
name|proxyPort
operator|=
name|proxyPort
expr_stmt|;
block|}
block|}
end_class

end_unit

