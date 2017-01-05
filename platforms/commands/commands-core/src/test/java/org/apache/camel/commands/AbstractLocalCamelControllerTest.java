begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.commands
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|commands
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
name|java
operator|.
name|util
operator|.
name|Map
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
name|impl
operator|.
name|DefaultCamelContext
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
name|ExplicitCamelContextNameStrategy
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
name|model
operator|.
name|transformer
operator|.
name|CustomTransformerDefinition
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
name|DataType
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
name|Transformer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_class
DECL|class|AbstractLocalCamelControllerTest
specifier|public
class|class
name|AbstractLocalCamelControllerTest
block|{
DECL|field|localCamelController
specifier|private
specifier|final
name|DummyCamelController
name|localCamelController
decl_stmt|;
DECL|field|context
specifier|private
specifier|final
name|CamelContext
name|context
decl_stmt|;
DECL|method|AbstractLocalCamelControllerTest ()
specifier|public
name|AbstractLocalCamelControllerTest
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|=
operator|new
name|DefaultCamelContext
argument_list|()
expr_stmt|;
name|context
operator|.
name|setNameStrategy
argument_list|(
operator|new
name|ExplicitCamelContextNameStrategy
argument_list|(
literal|"context1"
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
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
literal|"direct:start1"
argument_list|)
operator|.
name|id
argument_list|(
literal|"route1"
argument_list|)
operator|.
name|delay
argument_list|(
literal|100
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result1"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start2"
argument_list|)
operator|.
name|id
argument_list|(
literal|"route2"
argument_list|)
operator|.
name|delay
argument_list|(
literal|100
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result2"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start3"
argument_list|)
operator|.
name|id
argument_list|(
literal|"route3"
argument_list|)
operator|.
name|delay
argument_list|(
literal|100
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result3"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|CustomTransformerDefinition
name|def
init|=
operator|new
name|CustomTransformerDefinition
argument_list|()
decl_stmt|;
name|def
operator|.
name|setType
argument_list|(
name|DummyTransformer
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|def
operator|.
name|setFrom
argument_list|(
literal|"xml:foo"
argument_list|)
expr_stmt|;
name|def
operator|.
name|setTo
argument_list|(
literal|"json:bar"
argument_list|)
expr_stmt|;
name|context
operator|.
name|getTransformers
argument_list|()
operator|.
name|add
argument_list|(
name|def
argument_list|)
expr_stmt|;
name|localCamelController
operator|=
operator|new
name|DummyCamelController
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Before
DECL|method|startContext ()
specifier|public
name|void
name|startContext
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|stopContext ()
specifier|public
name|void
name|stopContext
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBrowseInflightExchangesWithMoreRoutes ()
specifier|public
name|void
name|testBrowseInflightExchangesWithMoreRoutes
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|createProducerTemplate
argument_list|()
operator|.
name|asyncSendBody
argument_list|(
literal|"direct:start1"
argument_list|,
literal|"Start one"
argument_list|)
expr_stmt|;
name|context
operator|.
name|createProducerTemplate
argument_list|()
operator|.
name|asyncSendBody
argument_list|(
literal|"direct:start2"
argument_list|,
literal|"Start two"
argument_list|)
expr_stmt|;
name|context
operator|.
name|createProducerTemplate
argument_list|()
operator|.
name|asyncSendBody
argument_list|(
literal|"direct:start3"
argument_list|,
literal|"Start three"
argument_list|)
expr_stmt|;
comment|// let the exchange proceed
name|Thread
operator|.
name|sleep
argument_list|(
literal|50
argument_list|)
expr_stmt|;
specifier|final
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|inflightExchanges
init|=
name|localCamelController
operator|.
name|browseInflightExchanges
argument_list|(
literal|"context1"
argument_list|,
literal|null
argument_list|,
literal|0
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Context should contain three inflight exchanges"
argument_list|,
literal|3
argument_list|,
name|inflightExchanges
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBrowseInflightExchangesWithNoRoutes ()
specifier|public
name|void
name|testBrowseInflightExchangesWithNoRoutes
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|inflightExchanges
init|=
name|localCamelController
operator|.
name|browseInflightExchanges
argument_list|(
literal|"context1"
argument_list|,
literal|null
argument_list|,
literal|0
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Context without routes should not have any inflight exchanges"
argument_list|,
name|inflightExchanges
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBrowseInflightExchangesWithOneRoute ()
specifier|public
name|void
name|testBrowseInflightExchangesWithOneRoute
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|createProducerTemplate
argument_list|()
operator|.
name|asyncSendBody
argument_list|(
literal|"direct:start1"
argument_list|,
literal|"Start one"
argument_list|)
expr_stmt|;
comment|// let the exchange proceed
name|Thread
operator|.
name|sleep
argument_list|(
literal|50
argument_list|)
expr_stmt|;
specifier|final
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|inflightExchanges
init|=
name|localCamelController
operator|.
name|browseInflightExchanges
argument_list|(
literal|"context1"
argument_list|,
literal|null
argument_list|,
literal|0
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Context should contain one inflight exchange"
argument_list|,
literal|1
argument_list|,
name|inflightExchanges
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBrowseInflightExchangesWithSpecificRoute ()
specifier|public
name|void
name|testBrowseInflightExchangesWithSpecificRoute
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|createProducerTemplate
argument_list|()
operator|.
name|asyncSendBody
argument_list|(
literal|"direct:start1"
argument_list|,
literal|"Start one"
argument_list|)
expr_stmt|;
name|context
operator|.
name|createProducerTemplate
argument_list|()
operator|.
name|asyncSendBody
argument_list|(
literal|"direct:start2"
argument_list|,
literal|"Start two"
argument_list|)
expr_stmt|;
name|context
operator|.
name|createProducerTemplate
argument_list|()
operator|.
name|asyncSendBody
argument_list|(
literal|"direct:start3"
argument_list|,
literal|"Start three"
argument_list|)
expr_stmt|;
comment|// let the exchanges proceed
name|Thread
operator|.
name|sleep
argument_list|(
literal|50
argument_list|)
expr_stmt|;
specifier|final
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|inflightExchanges
init|=
name|localCamelController
operator|.
name|browseInflightExchanges
argument_list|(
literal|"context1"
argument_list|,
literal|"route2"
argument_list|,
literal|0
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Context should contain one inflight exchange for specific route"
argument_list|,
literal|1
argument_list|,
name|inflightExchanges
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testTransformer ()
specifier|public
name|void
name|testTransformer
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|resolveTransformer
argument_list|(
operator|new
name|DataType
argument_list|(
literal|"xml:foo"
argument_list|)
argument_list|,
operator|new
name|DataType
argument_list|(
literal|"json:bar"
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|transformers
init|=
name|localCamelController
operator|.
name|getTransformers
argument_list|(
literal|"context1"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|transformers
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|dummyTransformer
init|=
name|transformers
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"context1"
argument_list|,
name|dummyTransformer
operator|.
name|get
argument_list|(
literal|"camelContextName"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"DummyTransformer[scheme='null', from='xml:foo', to='json:bar']"
argument_list|,
name|dummyTransformer
operator|.
name|get
argument_list|(
literal|"description"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|dummyTransformer
operator|.
name|get
argument_list|(
literal|"scheme"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"xml:foo"
argument_list|,
name|dummyTransformer
operator|.
name|get
argument_list|(
literal|"from"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"json:bar"
argument_list|,
name|dummyTransformer
operator|.
name|get
argument_list|(
literal|"to"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Started"
argument_list|,
name|dummyTransformer
operator|.
name|get
argument_list|(
literal|"state"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|class|DummyTransformer
specifier|public
specifier|static
class|class
name|DummyTransformer
extends|extends
name|Transformer
block|{
annotation|@
name|Override
DECL|method|transform (Message message, DataType from, DataType to)
specifier|public
name|void
name|transform
parameter_list|(
name|Message
name|message
parameter_list|,
name|DataType
name|from
parameter_list|,
name|DataType
name|to
parameter_list|)
throws|throws
name|Exception
block|{         }
block|}
block|}
end_class

end_unit

