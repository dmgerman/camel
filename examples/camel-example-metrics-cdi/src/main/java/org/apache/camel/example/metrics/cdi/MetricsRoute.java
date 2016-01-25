begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.metrics.cdi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|metrics
operator|.
name|cdi
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
name|LoggingLevel
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
name|RouteBuilder
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
name|cdi
operator|.
name|ContextName
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
name|metrics
operator|.
name|MetricsConstants
import|;
end_import

begin_class
annotation|@
name|ContextName
argument_list|(
literal|"camel-example-metrics-cdi"
argument_list|)
DECL|class|MetricsRoute
class|class
name|MetricsRoute
extends|extends
name|RouteBuilder
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|onException
argument_list|()
operator|.
name|handled
argument_list|(
literal|true
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|2
argument_list|)
operator|.
name|logStackTrace
argument_list|(
literal|false
argument_list|)
operator|.
name|logExhausted
argument_list|(
literal|false
argument_list|)
operator|.
name|log
argument_list|(
name|LoggingLevel
operator|.
name|ERROR
argument_list|,
literal|"Failed processing ${body}"
argument_list|)
operator|.
name|to
argument_list|(
literal|"metrics:meter:redelivery?mark=2"
argument_list|)
operator|.
name|to
argument_list|(
literal|"metrics:meter:error"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"timer:stream?period=1000"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"unreliable-service"
argument_list|)
operator|.
name|setBody
argument_list|(
name|header
argument_list|(
name|Exchange
operator|.
name|TIMER_COUNTER
argument_list|)
operator|.
name|prepend
argument_list|(
literal|"event #"
argument_list|)
argument_list|)
operator|.
name|log
argument_list|(
literal|"Processing ${body}..."
argument_list|)
operator|.
name|to
argument_list|(
literal|"metrics:meter:generated"
argument_list|)
operator|.
name|bean
argument_list|(
name|UnreliableService
operator|.
name|class
argument_list|)
operator|.
name|filter
argument_list|(
name|header
argument_list|(
name|Exchange
operator|.
name|REDELIVERED
argument_list|)
argument_list|)
operator|.
name|log
argument_list|(
name|LoggingLevel
operator|.
name|WARN
argument_list|,
literal|"Processed ${body} after ${header.CamelRedeliveryCounter} retries"
argument_list|)
operator|.
name|setHeader
argument_list|(
name|MetricsConstants
operator|.
name|HEADER_METER_MARK
argument_list|,
name|header
argument_list|(
name|Exchange
operator|.
name|REDELIVERY_COUNTER
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"metrics:meter:redelivery"
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|log
argument_list|(
literal|"Successfully processed ${body}"
argument_list|)
operator|.
name|to
argument_list|(
literal|"metrics:meter:success"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

