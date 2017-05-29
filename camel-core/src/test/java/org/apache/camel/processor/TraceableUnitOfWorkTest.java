begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
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
name|ContextTestSupport
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
name|RouteNode
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
name|impl
operator|.
name|JndiRegistry
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
name|management
operator|.
name|InstrumentationProcessor
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
name|TracedRouteNodes
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Ignore
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
annotation|@
name|Deprecated
annotation|@
name|Ignore
DECL|class|TraceableUnitOfWorkTest
specifier|public
class|class
name|TraceableUnitOfWorkTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|testSendingSomeMessages ()
specifier|public
name|void
name|testSendingSomeMessages
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|out
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello London"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Failed at: sendTo(bean://bar)"
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|jndi
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"foo"
argument_list|,
operator|new
name|MyFooBean
argument_list|()
argument_list|)
expr_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"bar"
argument_list|,
operator|new
name|MyBarBean
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|jndi
return|;
block|}
comment|// START SNIPPET: e1
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|errorHandler
argument_list|(
name|deadLetterChannel
argument_list|(
literal|"mock:error"
argument_list|)
operator|.
name|redeliveryDelay
argument_list|(
literal|0
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
comment|// must enable tracer to trace the route path taken during runtime
name|context
operator|.
name|setTracing
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// let our my error processor handle all exceptions
name|onException
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
operator|.
name|handled
argument_list|(
literal|true
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|MyErrorProcessor
argument_list|()
argument_list|)
expr_stmt|;
comment|// our route where an exception can be thrown from either foo or bar bean
comment|// so we have enable tracing so we can check it at runtime to get the actual
comment|// node path taken
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"bean:foo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"bean:bar"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
comment|// END SNIPPET: e1
comment|// START SNIPPET: e2
DECL|class|MyErrorProcessor
specifier|private
specifier|static
class|class
name|MyErrorProcessor
implements|implements
name|Processor
block|{
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|TracedRouteNodes
name|traced
init|=
name|exchange
operator|.
name|getUnitOfWork
argument_list|()
operator|.
name|getTracedRouteNodes
argument_list|()
decl_stmt|;
comment|// get the list of intercepted nodes
name|List
argument_list|<
name|RouteNode
argument_list|>
name|list
init|=
name|traced
operator|.
name|getNodes
argument_list|()
decl_stmt|;
comment|// get the 3rd last as its the bean
name|Processor
name|last
init|=
name|list
operator|.
name|get
argument_list|(
name|list
operator|.
name|size
argument_list|()
operator|-
literal|3
argument_list|)
operator|.
name|getProcessor
argument_list|()
decl_stmt|;
comment|// wrapped by JMX
if|if
condition|(
name|last
operator|instanceof
name|InstrumentationProcessor
condition|)
block|{
name|InstrumentationProcessor
name|ip
init|=
operator|(
name|InstrumentationProcessor
operator|)
name|last
decl_stmt|;
name|last
operator|=
name|ip
operator|.
name|getProcessor
argument_list|()
expr_stmt|;
block|}
comment|// set error message
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setFault
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Failed at: "
operator|+
name|last
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"MyErrorProcessor"
return|;
block|}
block|}
comment|// END SNIPPET: e2
DECL|class|MyFooBean
specifier|public
class|class
name|MyFooBean
block|{
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Foo okay"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|MyBarBean
specifier|public
class|class
name|MyBarBean
block|{
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Damm Bar"
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

