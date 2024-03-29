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
comment|/**  * The mock component is used for testing routes and mediation rules using  * mocks.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|MockEndpointBuilderFactory
specifier|public
interface|interface
name|MockEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the Mock component.      */
DECL|interface|MockEndpointBuilder
specifier|public
interface|interface
name|MockEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedMockEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedMockEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Sets a grace period after which the mock endpoint will re-assert to          * ensure the preliminary assertion is still valid. This is used for          * example to assert that exactly a number of messages arrives. For          * example if expectedMessageCount(int) was set to 5, then the assertion          * is satisfied when 5 or more message arrives. To ensure that exactly 5          * messages arrives, then you would need to wait a little period to          * ensure no further message arrives. This is what you can use this          * method for. By default this period is disabled.          *           * The option is a:<code>long</code> type.          *           * Group: producer          */
DECL|method|assertPeriod (long assertPeriod)
specifier|default
name|MockEndpointBuilder
name|assertPeriod
parameter_list|(
name|long
name|assertPeriod
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"assertPeriod"
argument_list|,
name|assertPeriod
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets a grace period after which the mock endpoint will re-assert to          * ensure the preliminary assertion is still valid. This is used for          * example to assert that exactly a number of messages arrives. For          * example if expectedMessageCount(int) was set to 5, then the assertion          * is satisfied when 5 or more message arrives. To ensure that exactly 5          * messages arrives, then you would need to wait a little period to          * ensure no further message arrives. This is what you can use this          * method for. By default this period is disabled.          *           * The option will be converted to a<code>long</code> type.          *           * Group: producer          */
DECL|method|assertPeriod (String assertPeriod)
specifier|default
name|MockEndpointBuilder
name|assertPeriod
parameter_list|(
name|String
name|assertPeriod
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"assertPeriod"
argument_list|,
name|assertPeriod
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Specifies the expected number of message exchanges that should be          * received by this endpoint. Beware: If you want to expect that 0          * messages, then take extra care, as 0 matches when the tests starts,          * so you need to set a assert period time to let the test run for a          * while to make sure there are still no messages arrived; for that use          * setAssertPeriod(long). An alternative is to use NotifyBuilder, and          * use the notifier to know when Camel is done routing some messages,          * before you call the assertIsSatisfied() method on the mocks. This          * allows you to not use a fixed assert period, to speedup testing          * times. If you want to assert that exactly n'th message arrives to          * this mock endpoint, then see also the setAssertPeriod(long) method          * for further details.          *           * The option is a:<code>int</code> type.          *           * Group: producer          */
DECL|method|expectedCount (int expectedCount)
specifier|default
name|MockEndpointBuilder
name|expectedCount
parameter_list|(
name|int
name|expectedCount
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"expectedCount"
argument_list|,
name|expectedCount
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Specifies the expected number of message exchanges that should be          * received by this endpoint. Beware: If you want to expect that 0          * messages, then take extra care, as 0 matches when the tests starts,          * so you need to set a assert period time to let the test run for a          * while to make sure there are still no messages arrived; for that use          * setAssertPeriod(long). An alternative is to use NotifyBuilder, and          * use the notifier to know when Camel is done routing some messages,          * before you call the assertIsSatisfied() method on the mocks. This          * allows you to not use a fixed assert period, to speedup testing          * times. If you want to assert that exactly n'th message arrives to          * this mock endpoint, then see also the setAssertPeriod(long) method          * for further details.          *           * The option will be converted to a<code>int</code> type.          *           * Group: producer          */
DECL|method|expectedCount (String expectedCount)
specifier|default
name|MockEndpointBuilder
name|expectedCount
parameter_list|(
name|String
name|expectedCount
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"expectedCount"
argument_list|,
name|expectedCount
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether assertIsSatisfied() should fail fast at the first          * detected failed expectation while it may otherwise wait for all          * expected messages to arrive before performing expectations          * verifications. Is by default true. Set to false to use behavior as in          * Camel 2.x.          *           * The option is a:<code>boolean</code> type.          *           * Group: producer          */
DECL|method|failFast (boolean failFast)
specifier|default
name|MockEndpointBuilder
name|failFast
parameter_list|(
name|boolean
name|failFast
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"failFast"
argument_list|,
name|failFast
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether assertIsSatisfied() should fail fast at the first          * detected failed expectation while it may otherwise wait for all          * expected messages to arrive before performing expectations          * verifications. Is by default true. Set to false to use behavior as in          * Camel 2.x.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: producer          */
DECL|method|failFast (String failFast)
specifier|default
name|MockEndpointBuilder
name|failFast
parameter_list|(
name|String
name|failFast
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"failFast"
argument_list|,
name|failFast
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the producer should be started lazy (on the first message).          * By starting lazy you can use this to allow CamelContext and routes to          * startup in situations where a producer may otherwise fail during          * starting and cause the route to fail being started. By deferring this          * startup to be lazy then the startup failure can be handled during          * routing messages via Camel's routing error handlers. Beware that when          * the first message is processed then creating and starting the          * producer may take a little time and prolong the total processing time          * of the processing.          *           * The option is a:<code>boolean</code> type.          *           * Group: producer          */
DECL|method|lazyStartProducer (boolean lazyStartProducer)
specifier|default
name|MockEndpointBuilder
name|lazyStartProducer
parameter_list|(
name|boolean
name|lazyStartProducer
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"lazyStartProducer"
argument_list|,
name|lazyStartProducer
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the producer should be started lazy (on the first message).          * By starting lazy you can use this to allow CamelContext and routes to          * startup in situations where a producer may otherwise fail during          * starting and cause the route to fail being started. By deferring this          * startup to be lazy then the startup failure can be handled during          * routing messages via Camel's routing error handlers. Beware that when          * the first message is processed then creating and starting the          * producer may take a little time and prolong the total processing time          * of the processing.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: producer          */
DECL|method|lazyStartProducer (String lazyStartProducer)
specifier|default
name|MockEndpointBuilder
name|lazyStartProducer
parameter_list|(
name|String
name|lazyStartProducer
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"lazyStartProducer"
argument_list|,
name|lazyStartProducer
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * A number that is used to turn on throughput logging based on groups          * of the size.          *           * The option is a:<code>int</code> type.          *           * Group: producer          */
DECL|method|reportGroup (int reportGroup)
specifier|default
name|MockEndpointBuilder
name|reportGroup
parameter_list|(
name|int
name|reportGroup
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"reportGroup"
argument_list|,
name|reportGroup
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * A number that is used to turn on throughput logging based on groups          * of the size.          *           * The option will be converted to a<code>int</code> type.          *           * Group: producer          */
DECL|method|reportGroup (String reportGroup)
specifier|default
name|MockEndpointBuilder
name|reportGroup
parameter_list|(
name|String
name|reportGroup
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"reportGroup"
argument_list|,
name|reportGroup
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets the minimum expected amount of time (in millis) the          * assertIsSatisfied() will wait on a latch until it is satisfied.          *           * The option is a:<code>long</code> type.          *           * Group: producer          */
DECL|method|resultMinimumWaitTime ( long resultMinimumWaitTime)
specifier|default
name|MockEndpointBuilder
name|resultMinimumWaitTime
parameter_list|(
name|long
name|resultMinimumWaitTime
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"resultMinimumWaitTime"
argument_list|,
name|resultMinimumWaitTime
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets the minimum expected amount of time (in millis) the          * assertIsSatisfied() will wait on a latch until it is satisfied.          *           * The option will be converted to a<code>long</code> type.          *           * Group: producer          */
DECL|method|resultMinimumWaitTime ( String resultMinimumWaitTime)
specifier|default
name|MockEndpointBuilder
name|resultMinimumWaitTime
parameter_list|(
name|String
name|resultMinimumWaitTime
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"resultMinimumWaitTime"
argument_list|,
name|resultMinimumWaitTime
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets the maximum amount of time (in millis) the assertIsSatisfied()          * will wait on a latch until it is satisfied.          *           * The option is a:<code>long</code> type.          *           * Group: producer          */
DECL|method|resultWaitTime (long resultWaitTime)
specifier|default
name|MockEndpointBuilder
name|resultWaitTime
parameter_list|(
name|long
name|resultWaitTime
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"resultWaitTime"
argument_list|,
name|resultWaitTime
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets the maximum amount of time (in millis) the assertIsSatisfied()          * will wait on a latch until it is satisfied.          *           * The option will be converted to a<code>long</code> type.          *           * Group: producer          */
DECL|method|resultWaitTime (String resultWaitTime)
specifier|default
name|MockEndpointBuilder
name|resultWaitTime
parameter_list|(
name|String
name|resultWaitTime
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"resultWaitTime"
argument_list|,
name|resultWaitTime
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Specifies to only retain the first n'th number of received Exchanges.          * This is used when testing with big data, to reduce memory consumption          * by not storing copies of every Exchange this mock endpoint receives.          * Important: When using this limitation, then the getReceivedCounter()          * will still return the actual number of received Exchanges. For          * example if we have received 5000 Exchanges, and have configured to          * only retain the first 10 Exchanges, then the getReceivedCounter()          * will still return 5000 but there is only the first 10 Exchanges in          * the getExchanges() and getReceivedExchanges() methods. When using          * this method, then some of the other expectation methods is not          * supported, for example the expectedBodiesReceived(Object...) sets a          * expectation on the first number of bodies received. You can configure          * both setRetainFirst(int) and setRetainLast(int) methods, to limit          * both the first and last received.          *           * The option is a:<code>int</code> type.          *           * Group: producer          */
DECL|method|retainFirst (int retainFirst)
specifier|default
name|MockEndpointBuilder
name|retainFirst
parameter_list|(
name|int
name|retainFirst
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"retainFirst"
argument_list|,
name|retainFirst
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Specifies to only retain the first n'th number of received Exchanges.          * This is used when testing with big data, to reduce memory consumption          * by not storing copies of every Exchange this mock endpoint receives.          * Important: When using this limitation, then the getReceivedCounter()          * will still return the actual number of received Exchanges. For          * example if we have received 5000 Exchanges, and have configured to          * only retain the first 10 Exchanges, then the getReceivedCounter()          * will still return 5000 but there is only the first 10 Exchanges in          * the getExchanges() and getReceivedExchanges() methods. When using          * this method, then some of the other expectation methods is not          * supported, for example the expectedBodiesReceived(Object...) sets a          * expectation on the first number of bodies received. You can configure          * both setRetainFirst(int) and setRetainLast(int) methods, to limit          * both the first and last received.          *           * The option will be converted to a<code>int</code> type.          *           * Group: producer          */
DECL|method|retainFirst (String retainFirst)
specifier|default
name|MockEndpointBuilder
name|retainFirst
parameter_list|(
name|String
name|retainFirst
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"retainFirst"
argument_list|,
name|retainFirst
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Specifies to only retain the last n'th number of received Exchanges.          * This is used when testing with big data, to reduce memory consumption          * by not storing copies of every Exchange this mock endpoint receives.          * Important: When using this limitation, then the getReceivedCounter()          * will still return the actual number of received Exchanges. For          * example if we have received 5000 Exchanges, and have configured to          * only retain the last 20 Exchanges, then the getReceivedCounter() will          * still return 5000 but there is only the last 20 Exchanges in the          * getExchanges() and getReceivedExchanges() methods. When using this          * method, then some of the other expectation methods is not supported,          * for example the expectedBodiesReceived(Object...) sets a expectation          * on the first number of bodies received. You can configure both          * setRetainFirst(int) and setRetainLast(int) methods, to limit both the          * first and last received.          *           * The option is a:<code>int</code> type.          *           * Group: producer          */
DECL|method|retainLast (int retainLast)
specifier|default
name|MockEndpointBuilder
name|retainLast
parameter_list|(
name|int
name|retainLast
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"retainLast"
argument_list|,
name|retainLast
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Specifies to only retain the last n'th number of received Exchanges.          * This is used when testing with big data, to reduce memory consumption          * by not storing copies of every Exchange this mock endpoint receives.          * Important: When using this limitation, then the getReceivedCounter()          * will still return the actual number of received Exchanges. For          * example if we have received 5000 Exchanges, and have configured to          * only retain the last 20 Exchanges, then the getReceivedCounter() will          * still return 5000 but there is only the last 20 Exchanges in the          * getExchanges() and getReceivedExchanges() methods. When using this          * method, then some of the other expectation methods is not supported,          * for example the expectedBodiesReceived(Object...) sets a expectation          * on the first number of bodies received. You can configure both          * setRetainFirst(int) and setRetainLast(int) methods, to limit both the          * first and last received.          *           * The option will be converted to a<code>int</code> type.          *           * Group: producer          */
DECL|method|retainLast (String retainLast)
specifier|default
name|MockEndpointBuilder
name|retainLast
parameter_list|(
name|String
name|retainLast
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"retainLast"
argument_list|,
name|retainLast
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Allows a sleep to be specified to wait to check that this endpoint          * really is empty when expectedMessageCount(int) is called with zero.          *           * The option is a:<code>long</code> type.          *           * Group: producer          */
DECL|method|sleepForEmptyTest (long sleepForEmptyTest)
specifier|default
name|MockEndpointBuilder
name|sleepForEmptyTest
parameter_list|(
name|long
name|sleepForEmptyTest
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"sleepForEmptyTest"
argument_list|,
name|sleepForEmptyTest
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Allows a sleep to be specified to wait to check that this endpoint          * really is empty when expectedMessageCount(int) is called with zero.          *           * The option will be converted to a<code>long</code> type.          *           * Group: producer          */
DECL|method|sleepForEmptyTest (String sleepForEmptyTest)
specifier|default
name|MockEndpointBuilder
name|sleepForEmptyTest
parameter_list|(
name|String
name|sleepForEmptyTest
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"sleepForEmptyTest"
argument_list|,
name|sleepForEmptyTest
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the Mock component.      */
DECL|interface|AdvancedMockEndpointBuilder
specifier|public
interface|interface
name|AdvancedMockEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|MockEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|MockEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Sets whether to make a deep copy of the incoming Exchange when          * received at this mock endpoint. Is by default true.          *           * The option is a:<code>boolean</code> type.          *           * Group: producer (advanced)          */
DECL|method|copyOnExchange ( boolean copyOnExchange)
specifier|default
name|AdvancedMockEndpointBuilder
name|copyOnExchange
parameter_list|(
name|boolean
name|copyOnExchange
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"copyOnExchange"
argument_list|,
name|copyOnExchange
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether to make a deep copy of the incoming Exchange when          * received at this mock endpoint. Is by default true.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: producer (advanced)          */
DECL|method|copyOnExchange (String copyOnExchange)
specifier|default
name|AdvancedMockEndpointBuilder
name|copyOnExchange
parameter_list|(
name|String
name|copyOnExchange
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"copyOnExchange"
argument_list|,
name|copyOnExchange
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedMockEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|doSetProperty
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
name|AdvancedMockEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|String
name|basicPropertyBinding
parameter_list|)
block|{
name|doSetProperty
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
name|AdvancedMockEndpointBuilder
name|synchronous
parameter_list|(
name|boolean
name|synchronous
parameter_list|)
block|{
name|doSetProperty
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
name|AdvancedMockEndpointBuilder
name|synchronous
parameter_list|(
name|String
name|synchronous
parameter_list|)
block|{
name|doSetProperty
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
comment|/**      * Mock (camel-mock)      * The mock component is used for testing routes and mediation rules using      * mocks.      *       * Category: core,testing      * Since: 1.0      * Maven coordinates: org.apache.camel:camel-mock      *       * Syntax:<code>mock:name</code>      *       * Path parameter: name (required)      * Name of mock endpoint      */
DECL|method|mock (String path)
specifier|default
name|MockEndpointBuilder
name|mock
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|MockEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|MockEndpointBuilder
implements|,
name|AdvancedMockEndpointBuilder
block|{
specifier|public
name|MockEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"mock"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|MockEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

