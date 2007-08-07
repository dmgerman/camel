begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|Exchange
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
name|Route
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
name|TestSupport
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
name|EventDrivenConsumerRoute
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
name|processor
operator|.
name|FilterProcessor
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
name|processor
operator|.
name|LoggingErrorHandler
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
name|processor
operator|.
name|SendProcessor
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|ErrorHandlerTest
specifier|public
class|class
name|ErrorHandlerTest
extends|extends
name|TestSupport
block|{
comment|/*      * public void testOverloadingTheDefaultErrorHandler() throws Exception { //      * START SNIPPET: e1 RouteBuilder<Exchange> builder = new RouteBuilder<Exchange>() {      * public void configure() { errorHandler(loggingErrorHandler("FOO.BAR"));      * from("seda:a").to("seda:b"); } }; // END SNIPPET: e1 Map<Endpoint<Exchange>,      * Processor> routeMap = builder.getRouteMap(); Set<Map.Entry<Endpoint<Exchange>,      * Processor>> routes = routeMap.entrySet(); assertEquals("Number routes      * created", 1, routes.size()); for (Map.Entry<Endpoint<Exchange>,      * Processor> route : routes) { Endpoint<Exchange> key = route.getKey();      * assertEquals("From endpoint", "seda:a", key.getEndpointUri()); Processor      * processor = route.getValue(); LoggingErrorHandler loggingProcessor =      * assertIsInstanceOf(LoggingErrorHandler.class, processor); } } public void      * testOverloadingTheHandlerOnASingleRoute() throws Exception { // START      * SNIPPET: e2 RouteBuilder<Exchange> builder = new RouteBuilder<Exchange>() {      * public void configure() {      * from("seda:a").errorHandler(loggingErrorHandler("FOO.BAR")).to("seda:b"); //      * this route will use the default error handler, DeadLetterChannel      * from("seda:b").to("seda:c"); } }; // END SNIPPET: e2 Map<Endpoint<Exchange>,      * Processor> routeMap = builder.getRouteMap(); log.debug(routeMap); Set<Map.Entry<Endpoint<Exchange>,      * Processor>> routes = routeMap.entrySet(); assertEquals("Number routes      * created", 2, routes.size()); for (Map.Entry<Endpoint<Exchange>,      * Processor> route : routes) { Endpoint<Exchange> key = route.getKey();      * String endpointUri = key.getEndpointUri(); Processor processor =      * route.getValue(); if (endpointUri.equals("seda:a")) { LoggingErrorHandler      * loggingProcessor = assertIsInstanceOf(LoggingErrorHandler.class,      * processor); Processor outputProcessor = loggingProcessor.getOutput();      * SendProcessor sendProcessor = assertIsInstanceOf(SendProcessor.class,      * outputProcessor); } else { assertEquals("From endpoint", "seda:b",      * endpointUri); DeadLetterChannel deadLetterChannel =      * assertIsInstanceOf(DeadLetterChannel.class, processor); Processor      * outputProcessor = deadLetterChannel.getOutput(); SendProcessor      * sendProcessor = assertIsInstanceOf(SendProcessor.class, outputProcessor); } } }      * public void testConfigureDeadLetterChannel() throws Exception { // START      * SNIPPET: e3 RouteBuilder<Exchange> builder = new RouteBuilder<Exchange>() {      * public void configure() { errorHandler(deadLetterChannel("seda:errors"));      * from("seda:a").to("seda:b"); } }; // END SNIPPET: e3 Map<Endpoint<Exchange>,      * Processor> routeMap = builder.getRouteMap(); Set<Map.Entry<Endpoint<Exchange>,      * Processor>> routes = routeMap.entrySet(); assertEquals("Number routes      * created", 1, routes.size()); for (Map.Entry<Endpoint<Exchange>,      * Processor> route : routes) { Endpoint<Exchange> key = route.getKey();      * assertEquals("From endpoint", "seda:a", key.getEndpointUri()); Processor      * processor = route.getValue(); DeadLetterChannel deadLetterChannel =      * assertIsInstanceOf(DeadLetterChannel.class, processor); Endpoint      * deadLetterEndpoint = assertIsInstanceOf(Endpoint.class,      * deadLetterChannel.getDeadLetter()); assertEndpointUri(deadLetterEndpoint,      * "seda:errors"); } } public void      * testConfigureDeadLetterChannelWithCustomRedeliveryPolicy() throws      * Exception { // START SNIPPET: e4 RouteBuilder<Exchange> builder = new      * RouteBuilder<Exchange>() { public void configure() {      * errorHandler(deadLetterChannel("seda:errors").maximumRedeliveries(2).useExponentialBackOff());      * from("seda:a").to("seda:b"); } }; // END SNIPPET: e4 Map<Endpoint<Exchange>,      * Processor> routeMap = builder.getRouteMap(); Set<Map.Entry<Endpoint<Exchange>,      * Processor>> routes = routeMap.entrySet(); assertEquals("Number routes      * created", 1, routes.size()); for (Map.Entry<Endpoint<Exchange>,      * Processor> route : routes) { Endpoint<Exchange> key = route.getKey();      * assertEquals("From endpoint", "seda:a", key.getEndpointUri()); Processor      * processor = route.getValue(); DeadLetterChannel deadLetterChannel =      * assertIsInstanceOf(DeadLetterChannel.class, processor); Endpoint      * deadLetterEndpoint = assertIsInstanceOf(Endpoint.class,      * deadLetterChannel.getDeadLetter()); assertEndpointUri(deadLetterEndpoint,      * "seda:errors"); RedeliveryPolicy redeliveryPolicy =      * deadLetterChannel.getRedeliveryPolicy();      * assertEquals("getMaximumRedeliveries()", 2,      * redeliveryPolicy.getMaximumRedeliveries());      * assertEquals("isUseExponentialBackOff()", true,      * redeliveryPolicy.isUseExponentialBackOff()); } }      */
DECL|method|testDisablingInheritenceOfErrorHandlers ()
specifier|public
name|void
name|testDisablingInheritenceOfErrorHandlers
parameter_list|()
throws|throws
name|Exception
block|{
comment|// START SNIPPET: e5
name|RouteBuilder
name|builder
init|=
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"seda:a"
argument_list|)
operator|.
name|errorHandler
argument_list|(
name|loggingErrorHandler
argument_list|(
literal|"FOO.BAR"
argument_list|)
argument_list|)
operator|.
name|filter
argument_list|(
name|body
argument_list|()
operator|.
name|isInstanceOf
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|inheritErrorHandler
argument_list|(
literal|false
argument_list|)
operator|.
name|to
argument_list|(
literal|"seda:b"
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
comment|// END SNIPPET: e5
name|List
argument_list|<
name|Route
argument_list|>
name|routes
init|=
name|builder
operator|.
name|getRouteList
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Number routes created"
argument_list|,
literal|1
argument_list|,
name|routes
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Route
argument_list|<
name|Exchange
argument_list|>
name|route
range|:
name|routes
control|)
block|{
name|Endpoint
argument_list|<
name|Exchange
argument_list|>
name|key
init|=
name|route
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"From endpoint"
argument_list|,
literal|"seda:a"
argument_list|,
name|key
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|EventDrivenConsumerRoute
name|consumerRoute
init|=
name|assertIsInstanceOf
argument_list|(
name|EventDrivenConsumerRoute
operator|.
name|class
argument_list|,
name|route
argument_list|)
decl_stmt|;
name|Processor
name|processor
init|=
name|consumerRoute
operator|.
name|getProcessor
argument_list|()
decl_stmt|;
name|LoggingErrorHandler
name|loggingProcessor
init|=
name|assertIsInstanceOf
argument_list|(
name|LoggingErrorHandler
operator|.
name|class
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|FilterProcessor
name|filterProcessor
init|=
name|assertIsInstanceOf
argument_list|(
name|FilterProcessor
operator|.
name|class
argument_list|,
name|loggingProcessor
operator|.
name|getOutput
argument_list|()
argument_list|)
decl_stmt|;
name|SendProcessor
name|sendProcessor
init|=
name|assertIsInstanceOf
argument_list|(
name|SendProcessor
operator|.
name|class
argument_list|,
name|filterProcessor
operator|.
name|getProcessor
argument_list|()
argument_list|)
decl_stmt|;
block|}
block|}
block|}
end_class

end_unit

