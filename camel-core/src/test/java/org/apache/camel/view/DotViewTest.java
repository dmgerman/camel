begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.view
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|view
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
name|builder
operator|.
name|xml
operator|.
name|XPathBuilder
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|xml
operator|.
name|XPathBuilder
operator|.
name|xpath
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|DotViewTest
specifier|public
class|class
name|DotViewTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|outputDirectory
specifier|protected
name|String
name|outputDirectory
init|=
literal|"target/site/cameldoc"
decl_stmt|;
DECL|method|testGenerateFiles ()
specifier|public
name|void
name|testGenerateFiles
parameter_list|()
throws|throws
name|Exception
block|{
name|RouteDotGenerator
name|generator
init|=
operator|new
name|RouteDotGenerator
argument_list|(
name|outputDirectory
argument_list|)
decl_stmt|;
name|generator
operator|.
name|drawRoutes
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
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
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|MulticastRoute
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|PipelineRoute
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|AnotherPipelineRoute
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|FromToRoute
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|ChoiceRoute
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|FilterRoute
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|ComplexRoute
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|FromToBeanRoute
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RoutingSlipRoute
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|AggreagateRoute
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|ResequenceRoute
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|class|MulticastRoute
specifier|static
class|class
name|MulticastRoute
extends|extends
name|RouteBuilder
block|{
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"seda:multicast.in"
argument_list|)
operator|.
name|multicast
argument_list|()
operator|.
name|to
argument_list|(
literal|"seda:multicast.out1"
argument_list|,
literal|"seda:multicast.out2"
argument_list|,
literal|"seda:multicast.out3"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|PipelineRoute
specifier|static
class|class
name|PipelineRoute
extends|extends
name|RouteBuilder
block|{
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"seda:pipeline.in"
argument_list|)
operator|.
name|to
argument_list|(
literal|"seda:pipeline.out1"
argument_list|,
literal|"seda:pipeline.out2"
argument_list|,
literal|"seda:pipeline.out3"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|AnotherPipelineRoute
specifier|static
class|class
name|AnotherPipelineRoute
extends|extends
name|RouteBuilder
block|{
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"seda:pipeline.in"
argument_list|)
operator|.
name|pipeline
argument_list|(
literal|"seda:pipeline.out1"
argument_list|,
literal|"seda:pipeline.out2"
argument_list|,
literal|"seda:pipeline.out3"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|FromToRoute
specifier|static
class|class
name|FromToRoute
extends|extends
name|RouteBuilder
block|{
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"seda:foo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"seda:bar"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|FromToBeanRoute
specifier|static
class|class
name|FromToBeanRoute
extends|extends
name|RouteBuilder
block|{
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"seda:foo"
argument_list|)
operator|.
name|beanRef
argument_list|(
literal|"myBean"
argument_list|,
literal|"myMethod"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|RoutingSlipRoute
specifier|static
class|class
name|RoutingSlipRoute
extends|extends
name|RouteBuilder
block|{
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"seda:foo"
argument_list|)
operator|.
name|routingSlip
argument_list|(
literal|"splipHeader"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|AggreagateRoute
specifier|static
class|class
name|AggreagateRoute
extends|extends
name|RouteBuilder
block|{
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"seda:foo"
argument_list|)
operator|.
name|aggregate
argument_list|(
name|constant
argument_list|(
literal|"messageId"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"seda:aggregated"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|ResequenceRoute
specifier|static
class|class
name|ResequenceRoute
extends|extends
name|RouteBuilder
block|{
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"seda:foo"
argument_list|)
operator|.
name|resequence
argument_list|(
name|constant
argument_list|(
literal|"seqNum"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"seda:bar"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|ChoiceRoute
specifier|static
class|class
name|ChoiceRoute
extends|extends
name|RouteBuilder
block|{
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"file:target/foo/xyz?noop=true"
argument_list|)
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|(
name|xpath
argument_list|(
literal|"/person/city = 'London'"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"file:target/messages/uk"
argument_list|)
operator|.
name|otherwise
argument_list|()
operator|.
name|to
argument_list|(
literal|"file:target/messages/others"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|FilterRoute
specifier|static
class|class
name|FilterRoute
extends|extends
name|RouteBuilder
block|{
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"file:target/foo/bar?noop=true"
argument_list|)
operator|.
name|filter
argument_list|(
name|header
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"file:target/xyz?noop=true"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|ComplexRoute
specifier|static
class|class
name|ComplexRoute
extends|extends
name|RouteBuilder
block|{
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"file:target/xyz?noop=true"
argument_list|)
operator|.
name|filter
argument_list|(
name|header
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
operator|.
name|recipientList
argument_list|(
name|header
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
operator|.
name|split
argument_list|(
name|XPathBuilder
operator|.
name|xpath
argument_list|(
literal|"/invoice/lineItems"
argument_list|)
argument_list|)
operator|.
name|throttle
argument_list|(
literal|3
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

