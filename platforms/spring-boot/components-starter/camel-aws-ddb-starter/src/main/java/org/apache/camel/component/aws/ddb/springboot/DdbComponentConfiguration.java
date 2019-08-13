begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.ddb.springboot
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
name|ddb
operator|.
name|springboot
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
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|dynamodbv2
operator|.
name|AmazonDynamoDB
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
name|ddb
operator|.
name|DdbOperations
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
name|spring
operator|.
name|boot
operator|.
name|ComponentConfigurationPropertiesCommon
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_comment
comment|/**  * The aws-ddb component is used for storing and retrieving data from Amazon's  * DynamoDB service.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.SpringBootAutoConfigurationMojo"
argument_list|)
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.aws-ddb"
argument_list|)
DECL|class|DdbComponentConfiguration
specifier|public
class|class
name|DdbComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * Whether to enable auto configuration of the aws-ddb component. This is      * enabled by default.      */
DECL|field|enabled
specifier|private
name|Boolean
name|enabled
decl_stmt|;
comment|/**      * The AWS DDB default configuration      */
DECL|field|configuration
specifier|private
name|DdbConfigurationNestedConfiguration
name|configuration
decl_stmt|;
comment|/**      * Amazon AWS Access Key      */
DECL|field|accessKey
specifier|private
name|String
name|accessKey
decl_stmt|;
comment|/**      * Amazon AWS Secret Key      */
DECL|field|secretKey
specifier|private
name|String
name|secretKey
decl_stmt|;
comment|/**      * The region in which DDB client needs to work      */
DECL|field|region
specifier|private
name|String
name|region
decl_stmt|;
comment|/**      * Whether the component should use basic property binding (Camel 2.x) or      * the newer property binding with additional capabilities      */
DECL|field|basicPropertyBinding
specifier|private
name|Boolean
name|basicPropertyBinding
init|=
literal|false
decl_stmt|;
DECL|method|getConfiguration ()
specifier|public
name|DdbConfigurationNestedConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration ( DdbConfigurationNestedConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|DdbConfigurationNestedConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
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
DECL|method|getRegion ()
specifier|public
name|String
name|getRegion
parameter_list|()
block|{
return|return
name|region
return|;
block|}
DECL|method|setRegion (String region)
specifier|public
name|void
name|setRegion
parameter_list|(
name|String
name|region
parameter_list|)
block|{
name|this
operator|.
name|region
operator|=
name|region
expr_stmt|;
block|}
DECL|method|getBasicPropertyBinding ()
specifier|public
name|Boolean
name|getBasicPropertyBinding
parameter_list|()
block|{
return|return
name|basicPropertyBinding
return|;
block|}
DECL|method|setBasicPropertyBinding (Boolean basicPropertyBinding)
specifier|public
name|void
name|setBasicPropertyBinding
parameter_list|(
name|Boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|this
operator|.
name|basicPropertyBinding
operator|=
name|basicPropertyBinding
expr_stmt|;
block|}
DECL|class|DdbConfigurationNestedConfiguration
specifier|public
specifier|static
class|class
name|DdbConfigurationNestedConfiguration
block|{
DECL|field|CAMEL_NESTED_CLASS
specifier|public
specifier|static
specifier|final
name|Class
name|CAMEL_NESTED_CLASS
init|=
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
name|ddb
operator|.
name|DdbConfiguration
operator|.
name|class
decl_stmt|;
comment|/**          * Amazon AWS Access Key          */
DECL|field|accessKey
specifier|private
name|String
name|accessKey
decl_stmt|;
comment|/**          * Amazon AWS Secret Key          */
DECL|field|secretKey
specifier|private
name|String
name|secretKey
decl_stmt|;
comment|/**          * To use the AmazonDynamoDB as the client          */
DECL|field|amazonDDBClient
specifier|private
name|AmazonDynamoDB
name|amazonDDBClient
decl_stmt|;
comment|/**          * The name of the table currently worked with.          */
DECL|field|tableName
specifier|private
name|String
name|tableName
decl_stmt|;
comment|/**          * What operation to perform          */
DECL|field|operation
specifier|private
name|DdbOperations
name|operation
init|=
name|DdbOperations
operator|.
name|PutItem
decl_stmt|;
comment|/**          * Determines whether or not strong consistency should be enforced when          * data is read.          */
DECL|field|consistentRead
specifier|private
name|Boolean
name|consistentRead
init|=
literal|false
decl_stmt|;
comment|/**          * The provisioned throughput to reserve for reading resources from your          * table          */
DECL|field|readCapacity
specifier|private
name|Long
name|readCapacity
decl_stmt|;
comment|/**          * The provisioned throughput to reserved for writing resources to your          * table          */
DECL|field|writeCapacity
specifier|private
name|Long
name|writeCapacity
decl_stmt|;
comment|/**          * Attribute name when creating table          */
DECL|field|keyAttributeName
specifier|private
name|String
name|keyAttributeName
decl_stmt|;
comment|/**          * Attribute type when creating table          */
DECL|field|keyAttributeType
specifier|private
name|String
name|keyAttributeType
decl_stmt|;
comment|/**          * To define a proxy host when instantiating the DDB client          */
DECL|field|proxyHost
specifier|private
name|String
name|proxyHost
decl_stmt|;
comment|/**          * To define a proxy port when instantiating the DDB client. When using          * this parameter, the configuration will expect the capitalized name of          * the region (for example AP_EAST_1) You'll need to use the name          * Regions.EU_WEST_1.name()          */
DECL|field|proxyPort
specifier|private
name|Integer
name|proxyPort
decl_stmt|;
comment|/**          * The region in which DDB client needs to work          */
DECL|field|region
specifier|private
name|String
name|region
decl_stmt|;
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
DECL|method|getAmazonDDBClient ()
specifier|public
name|AmazonDynamoDB
name|getAmazonDDBClient
parameter_list|()
block|{
return|return
name|amazonDDBClient
return|;
block|}
DECL|method|setAmazonDDBClient (AmazonDynamoDB amazonDDBClient)
specifier|public
name|void
name|setAmazonDDBClient
parameter_list|(
name|AmazonDynamoDB
name|amazonDDBClient
parameter_list|)
block|{
name|this
operator|.
name|amazonDDBClient
operator|=
name|amazonDDBClient
expr_stmt|;
block|}
DECL|method|getTableName ()
specifier|public
name|String
name|getTableName
parameter_list|()
block|{
return|return
name|tableName
return|;
block|}
DECL|method|setTableName (String tableName)
specifier|public
name|void
name|setTableName
parameter_list|(
name|String
name|tableName
parameter_list|)
block|{
name|this
operator|.
name|tableName
operator|=
name|tableName
expr_stmt|;
block|}
DECL|method|getOperation ()
specifier|public
name|DdbOperations
name|getOperation
parameter_list|()
block|{
return|return
name|operation
return|;
block|}
DECL|method|setOperation (DdbOperations operation)
specifier|public
name|void
name|setOperation
parameter_list|(
name|DdbOperations
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
DECL|method|getConsistentRead ()
specifier|public
name|Boolean
name|getConsistentRead
parameter_list|()
block|{
return|return
name|consistentRead
return|;
block|}
DECL|method|setConsistentRead (Boolean consistentRead)
specifier|public
name|void
name|setConsistentRead
parameter_list|(
name|Boolean
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
DECL|method|getReadCapacity ()
specifier|public
name|Long
name|getReadCapacity
parameter_list|()
block|{
return|return
name|readCapacity
return|;
block|}
DECL|method|setReadCapacity (Long readCapacity)
specifier|public
name|void
name|setReadCapacity
parameter_list|(
name|Long
name|readCapacity
parameter_list|)
block|{
name|this
operator|.
name|readCapacity
operator|=
name|readCapacity
expr_stmt|;
block|}
DECL|method|getWriteCapacity ()
specifier|public
name|Long
name|getWriteCapacity
parameter_list|()
block|{
return|return
name|writeCapacity
return|;
block|}
DECL|method|setWriteCapacity (Long writeCapacity)
specifier|public
name|void
name|setWriteCapacity
parameter_list|(
name|Long
name|writeCapacity
parameter_list|)
block|{
name|this
operator|.
name|writeCapacity
operator|=
name|writeCapacity
expr_stmt|;
block|}
DECL|method|getKeyAttributeName ()
specifier|public
name|String
name|getKeyAttributeName
parameter_list|()
block|{
return|return
name|keyAttributeName
return|;
block|}
DECL|method|setKeyAttributeName (String keyAttributeName)
specifier|public
name|void
name|setKeyAttributeName
parameter_list|(
name|String
name|keyAttributeName
parameter_list|)
block|{
name|this
operator|.
name|keyAttributeName
operator|=
name|keyAttributeName
expr_stmt|;
block|}
DECL|method|getKeyAttributeType ()
specifier|public
name|String
name|getKeyAttributeType
parameter_list|()
block|{
return|return
name|keyAttributeType
return|;
block|}
DECL|method|setKeyAttributeType (String keyAttributeType)
specifier|public
name|void
name|setKeyAttributeType
parameter_list|(
name|String
name|keyAttributeType
parameter_list|)
block|{
name|this
operator|.
name|keyAttributeType
operator|=
name|keyAttributeType
expr_stmt|;
block|}
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
DECL|method|getRegion ()
specifier|public
name|String
name|getRegion
parameter_list|()
block|{
return|return
name|region
return|;
block|}
DECL|method|setRegion (String region)
specifier|public
name|void
name|setRegion
parameter_list|(
name|String
name|region
parameter_list|)
block|{
name|this
operator|.
name|region
operator|=
name|region
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

