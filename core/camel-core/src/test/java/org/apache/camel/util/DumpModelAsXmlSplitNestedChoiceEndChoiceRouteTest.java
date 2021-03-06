begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
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
name|builder
operator|.
name|RouteBuilder
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|DumpModelAsXmlSplitNestedChoiceEndChoiceRouteTest
specifier|public
class|class
name|DumpModelAsXmlSplitNestedChoiceEndChoiceRouteTest
extends|extends
name|DumpModelAsXmlSplitNestedChoiceEndRouteTest
block|{
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
name|routeId
argument_list|(
literal|"myRoute"
argument_list|)
operator|.
name|split
argument_list|()
operator|.
name|body
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:sub"
argument_list|)
operator|.
name|id
argument_list|(
literal|"myMock"
argument_list|)
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|(
name|header
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:foo"
argument_list|)
comment|// eg
comment|// we
comment|// can
comment|// use
comment|// .endChoice()
comment|// here
comment|// also
operator|.
name|when
argument_list|(
name|header
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:bar"
argument_list|)
comment|// eg we can use
comment|// .endChoice() here
comment|// also
operator|.
name|otherwise
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:other"
argument_list|)
operator|.
name|endChoice
argument_list|()
comment|// end choice goes back to same level as choice (eg such as
comment|// ending the otherwise),
comment|// so we need a 2nd end to end the choice block in general
operator|.
name|end
argument_list|()
comment|// and then an end to end the splitter
operator|.
name|end
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:last"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

