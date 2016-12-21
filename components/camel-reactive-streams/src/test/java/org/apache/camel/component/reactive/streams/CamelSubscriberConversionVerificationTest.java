begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.reactive.streams
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|reactive
operator|.
name|streams
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
name|component
operator|.
name|reactive
operator|.
name|streams
operator|.
name|api
operator|.
name|CamelReactiveStreams
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
name|DefaultCamelContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|reactivestreams
operator|.
name|Subscriber
import|;
end_import

begin_import
import|import
name|org
operator|.
name|reactivestreams
operator|.
name|tck
operator|.
name|SubscriberBlackboxVerification
import|;
end_import

begin_import
import|import
name|org
operator|.
name|reactivestreams
operator|.
name|tck
operator|.
name|TestEnvironment
import|;
end_import

begin_class
DECL|class|CamelSubscriberConversionVerificationTest
specifier|public
class|class
name|CamelSubscriberConversionVerificationTest
extends|extends
name|SubscriberBlackboxVerification
argument_list|<
name|Integer
argument_list|>
block|{
DECL|field|context
specifier|private
name|CamelContext
name|context
decl_stmt|;
DECL|method|CamelSubscriberConversionVerificationTest ()
specifier|public
name|CamelSubscriberConversionVerificationTest
parameter_list|()
block|{
name|super
argument_list|(
operator|new
name|TestEnvironment
argument_list|(
literal|2000L
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createSubscriber ()
specifier|public
name|Subscriber
argument_list|<
name|Integer
argument_list|>
name|createSubscriber
parameter_list|()
block|{
name|this
operator|.
name|context
operator|=
operator|new
name|DefaultCamelContext
argument_list|()
expr_stmt|;
name|RouteBuilder
name|builder
init|=
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"reactive-streams:sub?maxInflightExchanges=20"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:INFO"
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|Subscriber
argument_list|<
name|Integer
argument_list|>
name|sub
init|=
name|CamelReactiveStreams
operator|.
name|get
argument_list|(
name|context
argument_list|)
operator|.
name|getSubscriber
argument_list|(
literal|"sub"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
try|try
block|{
name|builder
operator|.
name|addRoutesToCamelContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
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
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
return|return
name|sub
return|;
block|}
annotation|@
name|Override
DECL|method|createElement (int element)
specifier|public
name|Integer
name|createElement
parameter_list|(
name|int
name|element
parameter_list|)
block|{
return|return
name|element
return|;
block|}
block|}
end_class

end_unit

