begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.stringtemplate
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|stringtemplate
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
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
DECL|class|StringTemplateCustomDelimiterTest
specifier|public
class|class
name|StringTemplateCustomDelimiterTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|DIRECT_BRACE
specifier|private
specifier|static
specifier|final
name|String
name|DIRECT_BRACE
init|=
literal|"direct:brace"
decl_stmt|;
DECL|field|DIRECT_DOLLAR
specifier|private
specifier|static
specifier|final
name|String
name|DIRECT_DOLLAR
init|=
literal|"direct:dollar"
decl_stmt|;
annotation|@
name|Test
DECL|method|testWithBraceDelimiter ()
specifier|public
name|void
name|testWithBraceDelimiter
parameter_list|()
block|{
name|Exchange
name|response
init|=
name|template
operator|.
name|request
argument_list|(
name|DIRECT_BRACE
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
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
literal|"Yay !"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"With brace delimiter Yay !"
argument_list|,
name|response
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
operator|.
name|toString
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testWithDollarDelimiter ()
specifier|public
name|void
name|testWithDollarDelimiter
parameter_list|()
block|{
name|Exchange
name|response
init|=
name|template
operator|.
name|request
argument_list|(
name|DIRECT_DOLLAR
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
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
literal|"Yay !"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"With identical dollar delimiter Yay !"
argument_list|,
name|response
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
operator|.
name|toString
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
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
name|DIRECT_BRACE
argument_list|)
operator|.
name|to
argument_list|(
literal|"string-template:org/apache/camel/component/stringtemplate/custom-delimiter-brace.tm?delimiterStart={&delimiterStop=}"
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|DIRECT_DOLLAR
argument_list|)
operator|.
name|to
argument_list|(
literal|"string-template:org/apache/camel/component/stringtemplate/custom-delimiter-dollar.tm?delimiterStart=$&delimiterStop=$"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

