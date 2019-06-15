begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder.endpoint.dsl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|endpoint
operator|.
name|dsl
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Generated
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
name|builder
operator|.
name|EndpointConsumerBuilder
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
name|builder
operator|.
name|EndpointProducerBuilder
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
name|builder
operator|.
name|endpoint
operator|.
name|AbstractEndpointBuilder
import|;
end_import

begin_comment
comment|/**  * The aws-ddb component is used for storing and retrieving data from Amazon's  * DynamoDB service.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|DdbEndpointBuilderFactory
specifier|public
interface|interface
name|DdbEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the AWS DynamoDB component.      */
DECL|interface|DdbEndpointBuilder
specifier|public
specifier|static
interface|interface
name|DdbEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedDdbEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedDdbEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * The name of the table currently worked with.          * The option is a<code>java.lang.String</code> type.          * @group producer          */
DECL|method|tableName (String tableName)
specifier|default
name|DdbEndpointBuilder
name|tableName
parameter_list|(
name|String
name|tableName
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"tableName"
argument_list|,
name|tableName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To use the AmazonDynamoDB as the client.          * The option is a          *<code>com.amazonaws.services.dynamodbv2.AmazonDynamoDB</code> type.          * @group producer          */
DECL|method|amazonDDBClient (Object amazonDDBClient)
specifier|default
name|DdbEndpointBuilder
name|amazonDDBClient
parameter_list|(
name|Object
name|amazonDDBClient
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"amazonDDBClient"
argument_list|,
name|amazonDDBClient
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To use the AmazonDynamoDB as the client.          * The option will be converted to a          *<code>com.amazonaws.services.dynamodbv2.AmazonDynamoDB</code> type.          * @group producer          */
DECL|method|amazonDDBClient (String amazonDDBClient)
specifier|default
name|DdbEndpointBuilder
name|amazonDDBClient
parameter_list|(
name|String
name|amazonDDBClient
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"amazonDDBClient"
argument_list|,
name|amazonDDBClient
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Determines whether or not strong consistency should be enforced when          * data is read.          * The option is a<code>boolean</code> type.          * @group producer          */
DECL|method|consistentRead (boolean consistentRead)
specifier|default
name|DdbEndpointBuilder
name|consistentRead
parameter_list|(
name|boolean
name|consistentRead
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"consistentRead"
argument_list|,
name|consistentRead
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Determines whether or not strong consistency should be enforced when          * data is read.          * The option will be converted to a<code>boolean</code> type.          * @group producer          */
DECL|method|consistentRead (String consistentRead)
specifier|default
name|DdbEndpointBuilder
name|consistentRead
parameter_list|(
name|String
name|consistentRead
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"consistentRead"
argument_list|,
name|consistentRead
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Attribute name when creating table.          * The option is a<code>java.lang.String</code> type.          * @group producer          */
DECL|method|keyAttributeName (String keyAttributeName)
specifier|default
name|DdbEndpointBuilder
name|keyAttributeName
parameter_list|(
name|String
name|keyAttributeName
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"keyAttributeName"
argument_list|,
name|keyAttributeName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Attribute type when creating table.          * The option is a<code>java.lang.String</code> type.          * @group producer          */
DECL|method|keyAttributeType (String keyAttributeType)
specifier|default
name|DdbEndpointBuilder
name|keyAttributeType
parameter_list|(
name|String
name|keyAttributeType
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"keyAttributeType"
argument_list|,
name|keyAttributeType
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * What operation to perform.          * The option is a          *<code>org.apache.camel.component.aws.ddb.DdbOperations</code> type.          * @group producer          */
DECL|method|operation (DdbOperations operation)
specifier|default
name|DdbEndpointBuilder
name|operation
parameter_list|(
name|DdbOperations
name|operation
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"operation"
argument_list|,
name|operation
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * What operation to perform.          * The option will be converted to a          *<code>org.apache.camel.component.aws.ddb.DdbOperations</code> type.          * @group producer          */
DECL|method|operation (String operation)
specifier|default
name|DdbEndpointBuilder
name|operation
parameter_list|(
name|String
name|operation
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"operation"
argument_list|,
name|operation
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To define a proxy host when instantiating the DDB client.          * The option is a<code>java.lang.String</code> type.          * @group producer          */
DECL|method|proxyHost (String proxyHost)
specifier|default
name|DdbEndpointBuilder
name|proxyHost
parameter_list|(
name|String
name|proxyHost
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"proxyHost"
argument_list|,
name|proxyHost
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To define a proxy port when instantiating the DDB client.          * The option is a<code>java.lang.Integer</code> type.          * @group producer          */
DECL|method|proxyPort (Integer proxyPort)
specifier|default
name|DdbEndpointBuilder
name|proxyPort
parameter_list|(
name|Integer
name|proxyPort
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"proxyPort"
argument_list|,
name|proxyPort
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To define a proxy port when instantiating the DDB client.          * The option will be converted to a<code>java.lang.Integer</code>          * type.          * @group producer          */
DECL|method|proxyPort (String proxyPort)
specifier|default
name|DdbEndpointBuilder
name|proxyPort
parameter_list|(
name|String
name|proxyPort
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"proxyPort"
argument_list|,
name|proxyPort
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The provisioned throughput to reserve for reading resources from your          * table.          * The option is a<code>java.lang.Long</code> type.          * @group producer          */
DECL|method|readCapacity (Long readCapacity)
specifier|default
name|DdbEndpointBuilder
name|readCapacity
parameter_list|(
name|Long
name|readCapacity
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"readCapacity"
argument_list|,
name|readCapacity
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The provisioned throughput to reserve for reading resources from your          * table.          * The option will be converted to a<code>java.lang.Long</code> type.          * @group producer          */
DECL|method|readCapacity (String readCapacity)
specifier|default
name|DdbEndpointBuilder
name|readCapacity
parameter_list|(
name|String
name|readCapacity
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"readCapacity"
argument_list|,
name|readCapacity
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The region in which DDB client needs to work.          * The option is a<code>java.lang.String</code> type.          * @group producer          */
DECL|method|region (String region)
specifier|default
name|DdbEndpointBuilder
name|region
parameter_list|(
name|String
name|region
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"region"
argument_list|,
name|region
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The provisioned throughput to reserved for writing resources to your          * table.          * The option is a<code>java.lang.Long</code> type.          * @group producer          */
DECL|method|writeCapacity (Long writeCapacity)
specifier|default
name|DdbEndpointBuilder
name|writeCapacity
parameter_list|(
name|Long
name|writeCapacity
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"writeCapacity"
argument_list|,
name|writeCapacity
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The provisioned throughput to reserved for writing resources to your          * table.          * The option will be converted to a<code>java.lang.Long</code> type.          * @group producer          */
DECL|method|writeCapacity (String writeCapacity)
specifier|default
name|DdbEndpointBuilder
name|writeCapacity
parameter_list|(
name|String
name|writeCapacity
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"writeCapacity"
argument_list|,
name|writeCapacity
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Amazon AWS Access Key.          * The option is a<code>java.lang.String</code> type.          * @group security          */
DECL|method|accessKey (String accessKey)
specifier|default
name|DdbEndpointBuilder
name|accessKey
parameter_list|(
name|String
name|accessKey
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"accessKey"
argument_list|,
name|accessKey
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Amazon AWS Secret Key.          * The option is a<code>java.lang.String</code> type.          * @group security          */
DECL|method|secretKey (String secretKey)
specifier|default
name|DdbEndpointBuilder
name|secretKey
parameter_list|(
name|String
name|secretKey
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"secretKey"
argument_list|,
name|secretKey
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the AWS DynamoDB component.      */
DECL|interface|AdvancedDdbEndpointBuilder
specifier|public
specifier|static
interface|interface
name|AdvancedDdbEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|DdbEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|DdbEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedDdbEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"basicPropertyBinding"
argument_list|,
name|basicPropertyBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option will be converted to a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( String basicPropertyBinding)
specifier|default
name|AdvancedDdbEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|String
name|basicPropertyBinding
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"basicPropertyBinding"
argument_list|,
name|basicPropertyBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|synchronous (boolean synchronous)
specifier|default
name|AdvancedDdbEndpointBuilder
name|synchronous
parameter_list|(
name|boolean
name|synchronous
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"synchronous"
argument_list|,
name|synchronous
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          * The option will be converted to a<code>boolean</code> type.          * @group advanced          */
DECL|method|synchronous (String synchronous)
specifier|default
name|AdvancedDdbEndpointBuilder
name|synchronous
parameter_list|(
name|String
name|synchronous
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"synchronous"
argument_list|,
name|synchronous
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Proxy enum for      *<code>org.apache.camel.component.aws.ddb.DdbOperations</code> enum.      */
DECL|enum|DdbOperations
specifier|public
specifier|static
enum|enum
name|DdbOperations
block|{
DECL|enumConstant|BatchGetItems
DECL|enumConstant|DeleteItem
DECL|enumConstant|DeleteTable
DECL|enumConstant|DescribeTable
DECL|enumConstant|GetItem
DECL|enumConstant|PutItem
DECL|enumConstant|Query
DECL|enumConstant|Scan
DECL|enumConstant|UpdateItem
DECL|enumConstant|UpdateTable
name|BatchGetItems
block|,
name|DeleteItem
block|,
name|DeleteTable
block|,
name|DescribeTable
block|,
name|GetItem
block|,
name|PutItem
block|,
name|Query
block|,
name|Scan
block|,
name|UpdateItem
block|,
name|UpdateTable
block|;     }
comment|/**      * The aws-ddb component is used for storing and retrieving data from      * Amazon's DynamoDB service. Creates a builder to build endpoints for the      * AWS DynamoDB component.      */
DECL|method|ddb (String path)
specifier|default
name|DdbEndpointBuilder
name|ddb
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|DdbEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|DdbEndpointBuilder
implements|,
name|AdvancedDdbEndpointBuilder
block|{
specifier|public
name|DdbEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"aws-ddb"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|DdbEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

