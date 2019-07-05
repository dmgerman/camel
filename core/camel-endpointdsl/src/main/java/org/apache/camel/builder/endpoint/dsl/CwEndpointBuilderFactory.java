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
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

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
comment|/**  * The aws-cw component is used for sending metrics to an Amazon CloudWatch.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|CwEndpointBuilderFactory
specifier|public
interface|interface
name|CwEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the AWS CloudWatch component.      */
DECL|interface|CwEndpointBuilder
specifier|public
interface|interface
name|CwEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedCwEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedCwEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * To use the AmazonCloudWatch as the client.          *           * The option is a:          *<code>com.amazonaws.services.cloudwatch.AmazonCloudWatch</code> type.          *           * Group: producer          */
DECL|method|amazonCwClient (Object amazonCwClient)
specifier|default
name|CwEndpointBuilder
name|amazonCwClient
parameter_list|(
name|Object
name|amazonCwClient
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"amazonCwClient"
argument_list|,
name|amazonCwClient
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To use the AmazonCloudWatch as the client.          *           * The option will be converted to a          *<code>com.amazonaws.services.cloudwatch.AmazonCloudWatch</code> type.          *           * Group: producer          */
DECL|method|amazonCwClient (String amazonCwClient)
specifier|default
name|CwEndpointBuilder
name|amazonCwClient
parameter_list|(
name|String
name|amazonCwClient
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"amazonCwClient"
argument_list|,
name|amazonCwClient
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The metric name.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|name (String name)
specifier|default
name|CwEndpointBuilder
name|name
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"name"
argument_list|,
name|name
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To define a proxy host when instantiating the CW client.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|proxyHost (String proxyHost)
specifier|default
name|CwEndpointBuilder
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
comment|/**          * To define a proxy port when instantiating the CW client.          *           * The option is a:<code>java.lang.Integer</code> type.          *           * Group: producer          */
DECL|method|proxyPort (Integer proxyPort)
specifier|default
name|CwEndpointBuilder
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
comment|/**          * To define a proxy port when instantiating the CW client.          *           * The option will be converted to a<code>java.lang.Integer</code>          * type.          *           * Group: producer          */
DECL|method|proxyPort (String proxyPort)
specifier|default
name|CwEndpointBuilder
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
comment|/**          * The region in which CW client needs to work.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|region (String region)
specifier|default
name|CwEndpointBuilder
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
comment|/**          * The metric timestamp.          *           * The option is a:<code>java.util.Date</code> type.          *           * Group: producer          */
DECL|method|timestamp (Date timestamp)
specifier|default
name|CwEndpointBuilder
name|timestamp
parameter_list|(
name|Date
name|timestamp
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"timestamp"
argument_list|,
name|timestamp
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The metric timestamp.          *           * The option will be converted to a<code>java.util.Date</code> type.          *           * Group: producer          */
DECL|method|timestamp (String timestamp)
specifier|default
name|CwEndpointBuilder
name|timestamp
parameter_list|(
name|String
name|timestamp
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"timestamp"
argument_list|,
name|timestamp
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The metric unit.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|unit (String unit)
specifier|default
name|CwEndpointBuilder
name|unit
parameter_list|(
name|String
name|unit
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"unit"
argument_list|,
name|unit
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The metric value.          *           * The option is a:<code>java.lang.Double</code> type.          *           * Group: producer          */
DECL|method|value (Double value)
specifier|default
name|CwEndpointBuilder
name|value
parameter_list|(
name|Double
name|value
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"value"
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The metric value.          *           * The option will be converted to a<code>java.lang.Double</code> type.          *           * Group: producer          */
DECL|method|value (String value)
specifier|default
name|CwEndpointBuilder
name|value
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"value"
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Amazon AWS Access Key.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: security          */
DECL|method|accessKey (String accessKey)
specifier|default
name|CwEndpointBuilder
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
comment|/**          * Amazon AWS Secret Key.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: security          */
DECL|method|secretKey (String secretKey)
specifier|default
name|CwEndpointBuilder
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
comment|/**      * Advanced builder for endpoint for the AWS CloudWatch component.      */
DECL|interface|AdvancedCwEndpointBuilder
specifier|public
interface|interface
name|AdvancedCwEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|CwEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|CwEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedCwEndpointBuilder
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
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( String basicPropertyBinding)
specifier|default
name|AdvancedCwEndpointBuilder
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
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous (boolean synchronous)
specifier|default
name|AdvancedCwEndpointBuilder
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
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous (String synchronous)
specifier|default
name|AdvancedCwEndpointBuilder
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
comment|/**      * AWS CloudWatch (camel-aws-cw)      * The aws-cw component is used for sending metrics to an Amazon CloudWatch.      *       * Category: cloud,monitoring      * Available as of version: 2.11      * Maven coordinates: org.apache.camel:camel-aws-cw      *       * Syntax:<code>aws-cw:namespace</code>      *       * Path parameter: namespace (required)      * The metric namespace      */
DECL|method|cw (String path)
specifier|default
name|CwEndpointBuilder
name|cw
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|CwEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|CwEndpointBuilder
implements|,
name|AdvancedCwEndpointBuilder
block|{
specifier|public
name|CwEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"aws-cw"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|CwEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

