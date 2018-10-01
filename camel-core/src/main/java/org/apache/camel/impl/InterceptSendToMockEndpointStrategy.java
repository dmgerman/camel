begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
package|;
end_package

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
name|mock
operator|.
name|MockEndpoint
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
name|EndpointStrategy
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
name|EndpointHelper
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
name|StringHelper
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
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
import|;
end_import

begin_comment
comment|/**  * A {@link EndpointStrategy} which is capable of mocking endpoints.  *<p/>  * This strategy will only apply when new endpoints are being created. If you want to replace  * existing endpoints, you will have to remove them from the {@link org.apache.camel.CamelContext} beforehand.  */
end_comment

begin_class
DECL|class|InterceptSendToMockEndpointStrategy
specifier|public
class|class
name|InterceptSendToMockEndpointStrategy
implements|implements
name|EndpointStrategy
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
name|InterceptSendToMockEndpointStrategy
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|pattern
specifier|private
specifier|final
name|String
name|pattern
decl_stmt|;
DECL|field|skip
specifier|private
name|boolean
name|skip
decl_stmt|;
comment|/**      * Mock all endpoints.      */
DECL|method|InterceptSendToMockEndpointStrategy ()
specifier|public
name|InterceptSendToMockEndpointStrategy
parameter_list|()
block|{
name|this
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**      * Mock endpoints based on the given pattern.      *      * @param pattern the pattern.      * @see EndpointHelper#matchEndpoint(org.apache.camel.CamelContext, String, String)      */
DECL|method|InterceptSendToMockEndpointStrategy (String pattern)
specifier|public
name|InterceptSendToMockEndpointStrategy
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
name|this
argument_list|(
name|pattern
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
comment|/**      * Mock endpoints based on the given pattern.      *      * @param pattern the pattern.      * @param skip<tt>true</tt> to skip sending after the detour to the original endpoint      * @see EndpointHelper#matchEndpoint(org.apache.camel.CamelContext, String, String)      */
DECL|method|InterceptSendToMockEndpointStrategy (String pattern, boolean skip)
specifier|public
name|InterceptSendToMockEndpointStrategy
parameter_list|(
name|String
name|pattern
parameter_list|,
name|boolean
name|skip
parameter_list|)
block|{
name|this
operator|.
name|pattern
operator|=
name|pattern
expr_stmt|;
name|this
operator|.
name|skip
operator|=
name|skip
expr_stmt|;
block|}
DECL|method|registerEndpoint (String uri, Endpoint endpoint)
specifier|public
name|Endpoint
name|registerEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|)
block|{
if|if
condition|(
name|endpoint
operator|instanceof
name|InterceptSendToEndpoint
condition|)
block|{
comment|// endpoint already decorated
return|return
name|endpoint
return|;
block|}
elseif|else
if|if
condition|(
name|endpoint
operator|instanceof
name|MockEndpoint
condition|)
block|{
comment|// we should not intercept mock endpoints
return|return
name|endpoint
return|;
block|}
elseif|else
if|if
condition|(
name|matchPattern
argument_list|(
name|uri
argument_list|,
name|endpoint
argument_list|,
name|pattern
argument_list|)
condition|)
block|{
comment|// if pattern is null then it mean to match all
comment|// only proxy if the uri is matched decorate endpoint with our proxy
comment|// should be false by default
name|InterceptSendToEndpoint
name|proxy
init|=
operator|new
name|InterceptSendToEndpoint
argument_list|(
name|endpoint
argument_list|,
name|skip
argument_list|)
decl_stmt|;
comment|// create mock endpoint which we will use as interceptor
comment|// replace :// from scheme to make it easy to lookup the mock endpoint without having double :// in uri
name|String
name|key
init|=
literal|"mock:"
operator|+
name|endpoint
operator|.
name|getEndpointKey
argument_list|()
operator|.
name|replaceFirst
argument_list|(
literal|"://"
argument_list|,
literal|":"
argument_list|)
decl_stmt|;
comment|// strip off parameters as well
if|if
condition|(
name|key
operator|.
name|contains
argument_list|(
literal|"?"
argument_list|)
condition|)
block|{
name|key
operator|=
name|StringHelper
operator|.
name|before
argument_list|(
name|key
argument_list|,
literal|"?"
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|info
argument_list|(
literal|"Adviced endpoint [{}] with mock endpoint [{}]"
argument_list|,
name|uri
argument_list|,
name|key
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|endpoint
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getEndpoint
argument_list|(
name|key
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|Producer
name|producer
decl_stmt|;
try|try
block|{
name|producer
operator|=
name|mock
operator|.
name|createProducer
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
comment|// allow custom logic
name|producer
operator|=
name|onInterceptEndpoint
argument_list|(
name|uri
argument_list|,
name|endpoint
argument_list|,
name|mock
argument_list|,
name|producer
argument_list|)
expr_stmt|;
name|proxy
operator|.
name|setDetour
argument_list|(
name|producer
argument_list|)
expr_stmt|;
return|return
name|proxy
return|;
block|}
else|else
block|{
comment|// no proxy so return regular endpoint
return|return
name|endpoint
return|;
block|}
block|}
comment|/**      * Does the pattern match the endpoint?      *      * @param uri          the uri      * @param endpoint     the endpoint      * @param pattern      the pattern      * @return<tt>true</tt> to match and therefore intercept,<tt>false</tt> if not matched and should not intercept      */
DECL|method|matchPattern (String uri, Endpoint endpoint, String pattern)
specifier|protected
name|boolean
name|matchPattern
parameter_list|(
name|String
name|uri
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|,
name|String
name|pattern
parameter_list|)
block|{
return|return
name|uri
operator|==
literal|null
operator|||
name|pattern
operator|==
literal|null
operator|||
name|EndpointHelper
operator|.
name|matchEndpoint
argument_list|(
name|endpoint
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|uri
argument_list|,
name|pattern
argument_list|)
return|;
block|}
comment|/**      * Callback when an endpoint was intercepted with the given mock endpoint      *      * @param uri          the uri      * @param endpoint     the endpoint      * @param mockEndpoint the mocked endpoint      * @param mockProducer the mock producer      * @return the mock producer      */
DECL|method|onInterceptEndpoint (String uri, Endpoint endpoint, MockEndpoint mockEndpoint, Producer mockProducer)
specifier|protected
name|Producer
name|onInterceptEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|,
name|MockEndpoint
name|mockEndpoint
parameter_list|,
name|Producer
name|mockProducer
parameter_list|)
block|{
return|return
name|mockProducer
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"InterceptSendToMockEndpointStrategy"
return|;
block|}
block|}
end_class

end_unit

