begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.issues
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|issues
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

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
name|Expression
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
name|Message
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

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
DECL|class|SplitPropertiesFileIssueTest
specifier|public
class|class
name|SplitPropertiesFileIssueTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|body
specifier|private
name|String
name|body
init|=
literal|"foo=1"
operator|+
name|LS
operator|+
literal|"bar=2"
operator|+
name|LS
operator|+
literal|"bar=3"
operator|+
name|LS
operator|+
literal|"foo=4"
decl_stmt|;
annotation|@
name|Override
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteDirectory
argument_list|(
literal|"target/data/file/splitprop"
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSplitPropertiesFileAndRoute ()
specifier|public
name|void
name|testSplitPropertiesFileAndRoute
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|foo
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:foo"
argument_list|)
decl_stmt|;
name|foo
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"[foo=1, foo=4]"
argument_list|)
expr_stmt|;
comment|// after the file is routed it should be moved to done
name|foo
operator|.
name|expectedFileExists
argument_list|(
literal|"target/data/file/splitprop/done/myprop.txt"
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|MockEndpoint
name|bar
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:bar"
argument_list|)
decl_stmt|;
name|bar
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"[bar=2, bar=3]"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file://target/data/file/splitprop"
argument_list|,
name|body
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"myprop.txt"
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
literal|"file://target/data/file/splitprop?initialDelay=0&delay=10&move=done"
argument_list|)
operator|.
name|convertBodyTo
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|split
argument_list|(
operator|new
name|MyCustomExpression
argument_list|()
argument_list|)
operator|.
name|recipientList
argument_list|(
name|header
argument_list|(
literal|"myCustomDestination"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|MyCustomExpression
specifier|private
specifier|static
class|class
name|MyCustomExpression
implements|implements
name|Expression
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|evaluate (Exchange exchange, Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|evaluate
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
comment|// must copy from the original exchange as Camel holds information about the file in progress
name|Message
name|msg1
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|copy
argument_list|()
decl_stmt|;
name|Message
name|msg2
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|copy
argument_list|()
decl_stmt|;
comment|// now we use our own expressions to split the file as we like it
comment|// what we return is just the list of the two Camel Message objects
comment|// which contains the splitted data (our way)
name|List
argument_list|<
name|Message
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|answer
operator|.
name|add
argument_list|(
name|msg1
argument_list|)
expr_stmt|;
name|answer
operator|.
name|add
argument_list|(
name|msg2
argument_list|)
expr_stmt|;
comment|// split the original body into two data lists
comment|// can be done a bit prettier than this code
comment|// but its just for show and tell how to use Expressions
name|List
argument_list|<
name|String
argument_list|>
name|data1
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|data2
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|String
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
index|[]
name|lines
init|=
name|body
operator|.
name|split
argument_list|(
name|LS
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|line
range|:
name|lines
control|)
block|{
if|if
condition|(
name|line
operator|.
name|startsWith
argument_list|(
literal|"foo"
argument_list|)
condition|)
block|{
name|data1
operator|.
name|add
argument_list|(
name|line
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|data2
operator|.
name|add
argument_list|(
name|line
argument_list|)
expr_stmt|;
block|}
block|}
comment|// as we use the recipientList afterwards we set the destination
comment|// as well on our message where we want to route it
comment|// as we are an unit test then just store the list using toString so its easier to test
name|msg1
operator|.
name|setBody
argument_list|(
name|data1
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|msg1
operator|.
name|setHeader
argument_list|(
literal|"myCustomDestination"
argument_list|,
literal|"mock:foo"
argument_list|)
expr_stmt|;
name|msg2
operator|.
name|setBody
argument_list|(
name|data2
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|msg2
operator|.
name|setHeader
argument_list|(
literal|"myCustomDestination"
argument_list|,
literal|"mock:bar"
argument_list|)
expr_stmt|;
comment|// just cast it to T as its safe as its Object anyway for custom expressions
return|return
operator|(
name|T
operator|)
name|answer
return|;
block|}
block|}
block|}
end_class

end_unit

