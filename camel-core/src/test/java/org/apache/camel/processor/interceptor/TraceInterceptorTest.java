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
name|builder
operator|.
name|RouteBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|classextension
operator|.
name|EasyMock
import|;
end_import

begin_class
DECL|class|TraceInterceptorTest
specifier|public
class|class
name|TraceInterceptorTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|formatter
specifier|private
name|TraceFormatter
name|formatter
decl_stmt|;
DECL|field|tracer
specifier|private
name|Tracer
name|tracer
decl_stmt|;
annotation|@
name|Override
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|formatter
operator|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|TraceFormatter
operator|.
name|class
argument_list|)
expr_stmt|;
name|tracer
operator|=
operator|new
name|Tracer
argument_list|()
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
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
block|{
name|tracer
operator|.
name|setFormatter
argument_list|(
name|formatter
argument_list|)
expr_stmt|;
name|getContext
argument_list|()
operator|.
name|addInterceptStrategy
argument_list|(
name|tracer
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:a"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:a"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|testTracerInterceptor ()
specifier|public
name|void
name|testTracerInterceptor
parameter_list|()
throws|throws
name|Exception
block|{
name|EasyMock
operator|.
name|reset
argument_list|(
name|formatter
argument_list|)
expr_stmt|;
name|formatter
operator|.
name|format
argument_list|(
name|EasyMock
operator|.
name|isA
argument_list|(
name|TraceInterceptor
operator|.
name|class
argument_list|)
argument_list|,
name|EasyMock
operator|.
name|isA
argument_list|(
name|Exchange
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
literal|"Test"
argument_list|)
operator|.
name|atLeastOnce
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|formatter
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:a"
argument_list|,
literal|"<hello>world!</hello>"
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|formatter
argument_list|)
expr_stmt|;
block|}
DECL|method|testTracerDisabledInterceptor ()
specifier|public
name|void
name|testTracerDisabledInterceptor
parameter_list|()
throws|throws
name|Exception
block|{
name|tracer
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
try|try
block|{
name|testTracerInterceptor
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"The tracer should not work"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|ex
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"ex should AssertionError"
argument_list|,
name|ex
operator|instanceof
name|AssertionError
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

