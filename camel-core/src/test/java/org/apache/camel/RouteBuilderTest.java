begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
package|;
end_package

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|DestinationBuilder
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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|RouteBuilderTest
specifier|public
class|class
name|RouteBuilderTest
extends|extends
name|TestCase
block|{
DECL|method|testSimpleRoute ()
specifier|public
name|void
name|testSimpleRoute
parameter_list|()
throws|throws
name|Exception
block|{
name|RouteBuilder
argument_list|<
name|Exchange
argument_list|>
name|builder
init|=
operator|new
name|RouteBuilder
argument_list|<
name|Exchange
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"seda://a"
argument_list|)
operator|.
name|to
argument_list|(
literal|"seda://b"
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|Map
argument_list|<
name|Endpoint
argument_list|<
name|Exchange
argument_list|>
argument_list|,
name|List
argument_list|<
name|Processor
argument_list|<
name|Exchange
argument_list|>
argument_list|>
argument_list|>
name|routeMap
init|=
name|builder
operator|.
name|getRouteMap
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|Endpoint
argument_list|<
name|Exchange
argument_list|>
argument_list|,
name|List
argument_list|<
name|Processor
argument_list|<
name|Exchange
argument_list|>
argument_list|>
argument_list|>
argument_list|>
name|routes
init|=
name|routeMap
operator|.
name|entrySet
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Number routes created"
argument_list|,
literal|1
argument_list|,
name|routes
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Endpoint
argument_list|<
name|Exchange
argument_list|>
argument_list|,
name|List
argument_list|<
name|Processor
argument_list|<
name|Exchange
argument_list|>
argument_list|>
argument_list|>
name|route
range|:
name|routes
control|)
block|{
name|Endpoint
argument_list|<
name|Exchange
argument_list|>
name|key
init|=
name|route
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"From endpoint"
argument_list|,
literal|"seda://a"
argument_list|,
name|key
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Processor
argument_list|<
name|Exchange
argument_list|>
argument_list|>
name|processors
init|=
name|route
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Number of processors"
argument_list|,
literal|1
argument_list|,
name|processors
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Processor
name|processor
init|=
name|processors
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Processor should be a SendProcessor but was: "
operator|+
name|processor
operator|+
literal|" with type: "
operator|+
name|processor
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|processor
operator|instanceof
name|SendProcessor
argument_list|)
expr_stmt|;
name|SendProcessor
name|sendProcessor
init|=
operator|(
name|SendProcessor
operator|)
name|processor
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Endpoint URI"
argument_list|,
literal|"seda://b"
argument_list|,
name|sendProcessor
operator|.
name|getDestination
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testSimpleRouteWithHeaderPredicate ()
specifier|public
name|void
name|testSimpleRouteWithHeaderPredicate
parameter_list|()
throws|throws
name|Exception
block|{
name|RouteBuilder
name|builder
init|=
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"seda://a"
argument_list|)
operator|.
name|filter
argument_list|(
name|headerEquals
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"seda://b"
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|Map
argument_list|<
name|Endpoint
argument_list|<
name|Exchange
argument_list|>
argument_list|,
name|List
argument_list|<
name|Processor
argument_list|<
name|Exchange
argument_list|>
argument_list|>
argument_list|>
name|routeMap
init|=
name|builder
operator|.
name|getRouteMap
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|Endpoint
argument_list|<
name|Exchange
argument_list|>
argument_list|,
name|List
argument_list|<
name|Processor
argument_list|<
name|Exchange
argument_list|>
argument_list|>
argument_list|>
argument_list|>
name|routes
init|=
name|routeMap
operator|.
name|entrySet
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Number routes created"
argument_list|,
literal|1
argument_list|,
name|routes
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Endpoint
argument_list|<
name|Exchange
argument_list|>
argument_list|,
name|List
argument_list|<
name|Processor
argument_list|<
name|Exchange
argument_list|>
argument_list|>
argument_list|>
name|route
range|:
name|routes
control|)
block|{
name|Endpoint
argument_list|<
name|Exchange
argument_list|>
name|key
init|=
name|route
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"From endpoint"
argument_list|,
literal|"seda://a"
argument_list|,
name|key
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Processor
argument_list|<
name|Exchange
argument_list|>
argument_list|>
name|processors
init|=
name|route
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Number of processors"
argument_list|,
literal|1
argument_list|,
name|processors
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Processor
name|processor
init|=
name|processors
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Processor should be a FilterProcessor but was: "
operator|+
name|processor
operator|+
literal|" with type: "
operator|+
name|processor
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|processor
operator|instanceof
name|FilterProcessor
argument_list|)
expr_stmt|;
name|FilterProcessor
name|filterProcessor
init|=
operator|(
name|FilterProcessor
operator|)
name|processor
decl_stmt|;
name|SendProcessor
name|sendProcessor
init|=
operator|(
name|SendProcessor
operator|)
name|filterProcessor
operator|.
name|getProcessor
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Endpoint URI"
argument_list|,
literal|"seda://b"
argument_list|,
name|sendProcessor
operator|.
name|getDestination
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Created map: "
operator|+
name|routeMap
argument_list|)
expr_stmt|;
block|}
DECL|method|testSimpleRouteWithChoice ()
specifier|public
name|void
name|testSimpleRouteWithChoice
parameter_list|()
throws|throws
name|Exception
block|{
name|RouteBuilder
name|builder
init|=
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"seda://a"
argument_list|)
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|(
name|headerEquals
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"seda://b"
argument_list|)
operator|.
name|when
argument_list|(
name|headerEquals
argument_list|(
literal|"foo"
argument_list|,
literal|"cheese"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"seda://c"
argument_list|)
operator|.
name|otherwise
argument_list|()
operator|.
name|to
argument_list|(
literal|"seda://d"
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
block|}
block|}
end_class

end_unit

