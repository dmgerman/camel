begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.interceptor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|interceptor
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
name|mock
operator|.
name|MockEndpoint
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|DefaultTraceFormatterTest
specifier|public
class|class
name|DefaultTraceFormatterTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|testDefaultTraceFormatter ()
specifier|public
name|void
name|testDefaultTraceFormatter
parameter_list|()
block|{
name|getFormatter
argument_list|()
expr_stmt|;
block|}
DECL|method|testFormat ()
specifier|public
name|void
name|testFormat
parameter_list|()
throws|throws
name|Exception
block|{
name|Tracer
name|tracer
init|=
operator|new
name|Tracer
argument_list|()
decl_stmt|;
name|tracer
operator|.
name|setFormatter
argument_list|(
name|getFormatter
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|addInterceptStrategy
argument_list|(
name|tracer
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|send
argument_list|(
literal|"direct:start"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
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
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"foo"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Bye World"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"bar"
argument_list|,
literal|456
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
literal|"quote"
argument_list|,
literal|"Camel is cool"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testWithException ()
specifier|public
name|void
name|testWithException
parameter_list|()
throws|throws
name|Exception
block|{
name|Tracer
name|tracer
init|=
operator|new
name|Tracer
argument_list|()
decl_stmt|;
name|tracer
operator|.
name|setFormatter
argument_list|(
name|getFormatter
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|addInterceptStrategy
argument_list|(
name|tracer
argument_list|)
expr_stmt|;
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:fail"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
DECL|method|testNoShow ()
specifier|public
name|void
name|testNoShow
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultTraceFormatter
name|formatter
init|=
name|getFormatter
argument_list|()
decl_stmt|;
name|formatter
operator|.
name|setShowBreadCrumb
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|formatter
operator|.
name|setShowExchangeId
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|formatter
operator|.
name|setShowShortExchangeId
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|formatter
operator|.
name|setShowNode
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|Tracer
name|tracer
init|=
operator|new
name|Tracer
argument_list|()
decl_stmt|;
name|tracer
operator|.
name|setFormatter
argument_list|(
name|formatter
argument_list|)
expr_stmt|;
name|context
operator|.
name|addInterceptStrategy
argument_list|(
name|tracer
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
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
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:fail"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:mid"
argument_list|)
operator|.
name|throwException
argument_list|(
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Damn"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|getFormatter ()
specifier|private
name|DefaultTraceFormatter
name|getFormatter
parameter_list|()
block|{
name|DefaultTraceFormatter
name|formatter
init|=
operator|new
name|DefaultTraceFormatter
argument_list|()
decl_stmt|;
name|formatter
operator|.
name|setBreadCrumbLength
argument_list|(
literal|30
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|30
argument_list|,
name|formatter
operator|.
name|getBreadCrumbLength
argument_list|()
argument_list|)
expr_stmt|;
name|formatter
operator|.
name|setMaxChars
argument_list|(
literal|500
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|500
argument_list|,
name|formatter
operator|.
name|getMaxChars
argument_list|()
argument_list|)
expr_stmt|;
name|formatter
operator|.
name|setNodeLength
argument_list|(
literal|20
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|20
argument_list|,
name|formatter
operator|.
name|getNodeLength
argument_list|()
argument_list|)
expr_stmt|;
name|formatter
operator|.
name|setShowBody
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|formatter
operator|.
name|isShowBody
argument_list|()
argument_list|)
expr_stmt|;
name|formatter
operator|.
name|setBreadCrumbLength
argument_list|(
literal|40
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|40
argument_list|,
name|formatter
operator|.
name|getBreadCrumbLength
argument_list|()
argument_list|)
expr_stmt|;
name|formatter
operator|.
name|setShowBody
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|formatter
operator|.
name|isShowBody
argument_list|()
argument_list|)
expr_stmt|;
name|formatter
operator|.
name|setShowBodyType
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|formatter
operator|.
name|isShowBodyType
argument_list|()
argument_list|)
expr_stmt|;
name|formatter
operator|.
name|setShowBreadCrumb
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|formatter
operator|.
name|isShowBreadCrumb
argument_list|()
argument_list|)
expr_stmt|;
name|formatter
operator|.
name|setShowExchangeId
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|formatter
operator|.
name|isShowExchangeId
argument_list|()
argument_list|)
expr_stmt|;
name|formatter
operator|.
name|setShowException
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|formatter
operator|.
name|isShowException
argument_list|()
argument_list|)
expr_stmt|;
name|formatter
operator|.
name|setShowExchangePattern
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|formatter
operator|.
name|isShowExchangePattern
argument_list|()
argument_list|)
expr_stmt|;
name|formatter
operator|.
name|setShowHeaders
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|formatter
operator|.
name|isShowHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|formatter
operator|.
name|setShowNode
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|formatter
operator|.
name|isShowNode
argument_list|()
argument_list|)
expr_stmt|;
name|formatter
operator|.
name|setShowOutBody
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|formatter
operator|.
name|isShowOutBody
argument_list|()
argument_list|)
expr_stmt|;
name|formatter
operator|.
name|setShowOutBodyType
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|formatter
operator|.
name|isShowOutBodyType
argument_list|()
argument_list|)
expr_stmt|;
name|formatter
operator|.
name|setShowOutHeaders
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|formatter
operator|.
name|isShowOutHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|formatter
operator|.
name|setShowProperties
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|formatter
operator|.
name|isShowProperties
argument_list|()
argument_list|)
expr_stmt|;
name|formatter
operator|.
name|setShowShortExchangeId
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|formatter
operator|.
name|isShowShortExchangeId
argument_list|()
argument_list|)
expr_stmt|;
name|formatter
operator|.
name|setShowRouteId
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|formatter
operator|.
name|isShowRouteId
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|formatter
return|;
block|}
block|}
end_class

end_unit

