begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.opentracing
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|opentracing
package|;
end_package

begin_import
import|import
name|io
operator|.
name|opentracing
operator|.
name|tag
operator|.
name|Tags
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
name|RoutesBuilder
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|TwoServiceTest
specifier|public
class|class
name|TwoServiceTest
extends|extends
name|CamelOpenTracingTestSupport
block|{
DECL|field|testdata
specifier|private
specifier|static
name|SpanTestData
index|[]
name|testdata
init|=
block|{
operator|new
name|SpanTestData
argument_list|()
operator|.
name|setLabel
argument_list|(
literal|"ServiceB server"
argument_list|)
operator|.
name|setUri
argument_list|(
literal|"direct://ServiceB"
argument_list|)
operator|.
name|setOperation
argument_list|(
literal|"ServiceB"
argument_list|)
operator|.
name|setKind
argument_list|(
name|Tags
operator|.
name|SPAN_KIND_SERVER
argument_list|)
operator|.
name|setParentId
argument_list|(
literal|1
argument_list|)
block|,
operator|new
name|SpanTestData
argument_list|()
operator|.
name|setLabel
argument_list|(
literal|"ServiceB client"
argument_list|)
operator|.
name|setUri
argument_list|(
literal|"direct://ServiceB"
argument_list|)
operator|.
name|setOperation
argument_list|(
literal|"ServiceB"
argument_list|)
operator|.
name|setKind
argument_list|(
name|Tags
operator|.
name|SPAN_KIND_CLIENT
argument_list|)
operator|.
name|setParentId
argument_list|(
literal|2
argument_list|)
block|,
operator|new
name|SpanTestData
argument_list|()
operator|.
name|setLabel
argument_list|(
literal|"ServiceA server"
argument_list|)
operator|.
name|setUri
argument_list|(
literal|"direct://ServiceA"
argument_list|)
operator|.
name|setOperation
argument_list|(
literal|"ServiceA"
argument_list|)
operator|.
name|setKind
argument_list|(
name|Tags
operator|.
name|SPAN_KIND_SERVER
argument_list|)
operator|.
name|setParentId
argument_list|(
literal|3
argument_list|)
block|,
operator|new
name|SpanTestData
argument_list|()
operator|.
name|setLabel
argument_list|(
literal|"ServiceA client"
argument_list|)
operator|.
name|setUri
argument_list|(
literal|"direct://ServiceA"
argument_list|)
operator|.
name|setOperation
argument_list|(
literal|"ServiceA"
argument_list|)
operator|.
name|setKind
argument_list|(
name|Tags
operator|.
name|SPAN_KIND_CLIENT
argument_list|)
block|}
decl_stmt|;
DECL|method|TwoServiceTest ()
specifier|public
name|TwoServiceTest
parameter_list|()
block|{
name|super
argument_list|(
name|testdata
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRoute ()
specifier|public
name|void
name|testRoute
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:ServiceA"
argument_list|,
literal|"Hello"
argument_list|)
expr_stmt|;
name|verify
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RoutesBuilder
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
literal|"direct:ServiceA"
argument_list|)
operator|.
name|log
argument_list|(
literal|"ServiceA has been called"
argument_list|)
operator|.
name|delay
argument_list|(
name|simple
argument_list|(
literal|"${random(1000,2000)}"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:ServiceB"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:ServiceB"
argument_list|)
operator|.
name|log
argument_list|(
literal|"ServiceB has been called"
argument_list|)
operator|.
name|delay
argument_list|(
name|simple
argument_list|(
literal|"${random(0,500)}"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

