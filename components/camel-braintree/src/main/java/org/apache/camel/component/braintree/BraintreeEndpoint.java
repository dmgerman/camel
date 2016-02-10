begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.braintree
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|braintree
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|InvocationTargetException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
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
name|com
operator|.
name|braintreegateway
operator|.
name|BraintreeGateway
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
name|braintree
operator|.
name|internal
operator|.
name|BraintreeApiCollection
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
name|braintree
operator|.
name|internal
operator|.
name|BraintreeApiName
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
name|braintree
operator|.
name|internal
operator|.
name|BraintreeConstants
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
name|braintree
operator|.
name|internal
operator|.
name|BraintreePropertiesHelper
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
name|UriEndpoint
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
name|component
operator|.
name|AbstractApiEndpoint
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
name|component
operator|.
name|ApiMethod
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
name|component
operator|.
name|ApiMethodPropertiesHelper
import|;
end_import

begin_comment
comment|/**  * The braintree component is used for integrating with the Braintree Payment System.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"braintree"
argument_list|,
name|title
operator|=
literal|"Braintree"
argument_list|,
name|syntax
operator|=
literal|"braintree:name"
argument_list|,
name|consumerClass
operator|=
name|BraintreeConsumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"Braintree"
argument_list|)
DECL|class|BraintreeEndpoint
specifier|public
class|class
name|BraintreeEndpoint
extends|extends
name|AbstractApiEndpoint
argument_list|<
name|BraintreeApiName
argument_list|,
name|BraintreeConfiguration
argument_list|>
block|{
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"the method name"
argument_list|)
DECL|field|name
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
DECL|field|apiProxy
specifier|private
name|Object
name|apiProxy
decl_stmt|;
DECL|field|gateway
specifier|private
specifier|final
name|BraintreeGateway
name|gateway
decl_stmt|;
DECL|method|BraintreeEndpoint (String uri, BraintreeComponent component, BraintreeApiName apiName, String methodName, BraintreeConfiguration endpointConfiguration, BraintreeGateway gateway)
specifier|public
name|BraintreeEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|BraintreeComponent
name|component
parameter_list|,
name|BraintreeApiName
name|apiName
parameter_list|,
name|String
name|methodName
parameter_list|,
name|BraintreeConfiguration
name|endpointConfiguration
parameter_list|,
name|BraintreeGateway
name|gateway
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|,
name|apiName
argument_list|,
name|methodName
argument_list|,
name|BraintreeApiCollection
operator|.
name|getCollection
argument_list|()
operator|.
name|getHelper
argument_list|(
name|apiName
argument_list|)
argument_list|,
name|endpointConfiguration
argument_list|)
expr_stmt|;
name|this
operator|.
name|gateway
operator|=
name|gateway
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|methodName
expr_stmt|;
block|}
annotation|@
name|Override
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
name|BraintreeProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
annotation|@
name|Override
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
name|BraintreeConsumer
name|consumer
init|=
operator|new
name|BraintreeConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
decl_stmt|;
comment|// also set consumer.* properties
name|configureConsumer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
return|return
name|consumer
return|;
block|}
annotation|@
name|Override
DECL|method|getPropertiesHelper ()
specifier|protected
name|ApiMethodPropertiesHelper
argument_list|<
name|BraintreeConfiguration
argument_list|>
name|getPropertiesHelper
parameter_list|()
block|{
return|return
name|BraintreePropertiesHelper
operator|.
name|getHelper
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getThreadProfileName ()
specifier|protected
name|String
name|getThreadProfileName
parameter_list|()
block|{
return|return
name|BraintreeConstants
operator|.
name|THREAD_PROFILE_NAME
return|;
block|}
annotation|@
name|Override
DECL|method|afterConfigureProperties ()
specifier|protected
name|void
name|afterConfigureProperties
parameter_list|()
block|{
try|try
block|{
name|Method
name|method
init|=
name|gateway
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
name|apiName
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|method
operator|!=
literal|null
condition|)
block|{
name|apiProxy
operator|=
name|method
operator|.
name|invoke
argument_list|(
name|gateway
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid API name "
operator|+
name|apiName
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|InvocationTargetException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IllegalAccessException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|getApiProxy (ApiMethod method, Map<String, Object> args)
specifier|public
name|Object
name|getApiProxy
parameter_list|(
name|ApiMethod
name|method
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|args
parameter_list|)
block|{
return|return
name|apiProxy
return|;
block|}
block|}
end_class

end_unit

