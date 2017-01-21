begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.sns
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
name|sns
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
name|Endpoint
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
name|UriEndpointComponent
import|;
end_import

begin_class
DECL|class|SnsComponent
specifier|public
class|class
name|SnsComponent
extends|extends
name|UriEndpointComponent
block|{
DECL|method|SnsComponent ()
specifier|public
name|SnsComponent
parameter_list|()
block|{
name|super
argument_list|(
name|SnsEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|SnsComponent (CamelContext context)
specifier|public
name|SnsComponent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|,
name|SnsEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|SnsConfiguration
name|configuration
init|=
operator|new
name|SnsConfiguration
argument_list|()
decl_stmt|;
name|setProperties
argument_list|(
name|configuration
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
if|if
condition|(
name|remaining
operator|==
literal|null
operator|||
name|remaining
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Topic name must be specified."
argument_list|)
throw|;
block|}
if|if
condition|(
name|remaining
operator|.
name|startsWith
argument_list|(
literal|"arn:"
argument_list|)
condition|)
block|{
name|configuration
operator|.
name|setTopicArn
argument_list|(
name|remaining
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|configuration
operator|.
name|setTopicName
argument_list|(
name|remaining
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configuration
operator|.
name|getAmazonSNSClient
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"AmazonSNSClient must be specified"
argument_list|)
throw|;
block|}
name|SnsEndpoint
name|endpoint
init|=
operator|new
name|SnsEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|configuration
argument_list|)
decl_stmt|;
return|return
name|endpoint
return|;
block|}
block|}
end_class

end_unit

