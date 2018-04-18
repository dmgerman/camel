begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.routebox
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|routebox
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
name|ProducerTemplate
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
name|routebox
operator|.
name|demo
operator|.
name|Book
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
name|routebox
operator|.
name|demo
operator|.
name|BookCatalog
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
name|routebox
operator|.
name|demo
operator|.
name|RouteboxDemoTestSupport
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
name|routebox
operator|.
name|demo
operator|.
name|SimpleRouteBuilder
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
name|DefaultProducerTemplate
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
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
DECL|class|RouteboxDefaultContextAndRouteBuilderTest
specifier|public
class|class
name|RouteboxDefaultContextAndRouteBuilderTest
extends|extends
name|RouteboxDemoTestSupport
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|RouteboxSedaTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|template
specifier|private
name|ProducerTemplate
name|template
decl_stmt|;
DECL|field|routeboxUri
specifier|private
name|String
name|routeboxUri
init|=
literal|"routebox:multipleRoutes?innerRegistry=#registry&routeBuilders=#routes&dispatchStrategy=#strategy"
decl_stmt|;
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
name|registry
init|=
operator|new
name|JndiRegistry
argument_list|(
name|createJndiContext
argument_list|()
argument_list|)
decl_stmt|;
comment|// Wire the routeDefinitions& dispatchStrategy to the outer camelContext where the routebox is declared
name|List
argument_list|<
name|RouteBuilder
argument_list|>
name|routes
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|routes
operator|.
name|add
argument_list|(
operator|new
name|SimpleRouteBuilder
argument_list|()
argument_list|)
expr_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"registry"
argument_list|,
name|createInnerRegistry
argument_list|()
argument_list|)
expr_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"routes"
argument_list|,
name|routes
argument_list|)
expr_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"strategy"
argument_list|,
operator|new
name|SimpleRouteDispatchStrategy
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|registry
return|;
block|}
DECL|method|createInnerRegistry ()
specifier|private
name|JndiRegistry
name|createInnerRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|innerRegistry
init|=
operator|new
name|JndiRegistry
argument_list|(
name|createJndiContext
argument_list|()
argument_list|)
decl_stmt|;
name|BookCatalog
name|catalogBean
init|=
operator|new
name|BookCatalog
argument_list|()
decl_stmt|;
name|innerRegistry
operator|.
name|bind
argument_list|(
literal|"library"
argument_list|,
name|catalogBean
argument_list|)
expr_stmt|;
return|return
name|innerRegistry
return|;
block|}
annotation|@
name|Override
DECL|method|isUseRouteBuilder ()
specifier|public
name|boolean
name|isUseRouteBuilder
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Test
DECL|method|testRouteboxUsingDefaultContextAndRouteBuilder ()
specifier|public
name|void
name|testRouteboxUsingDefaultContextAndRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|=
operator|new
name|DefaultProducerTemplate
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|template
operator|.
name|start
argument_list|()
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
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
name|routeboxUri
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:Routes operation performed?showAll=true"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Beginning Test ---> testRouteboxUsingDefaultContextAndRouteBuilder()"
argument_list|)
expr_stmt|;
name|Book
name|book
init|=
operator|new
name|Book
argument_list|(
literal|"Sir Arthur Conan Doyle"
argument_list|,
literal|"The Adventures of Sherlock Holmes"
argument_list|)
decl_stmt|;
name|String
name|response
init|=
name|sendAddToCatalogRequest
argument_list|(
name|template
argument_list|,
name|routeboxUri
argument_list|,
literal|"addToCatalog"
argument_list|,
name|book
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Book with Author "
operator|+
name|book
operator|.
name|getAuthor
argument_list|()
operator|+
literal|" and title "
operator|+
name|book
operator|.
name|getTitle
argument_list|()
operator|+
literal|" added to Catalog"
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|book
operator|=
name|sendFindBookRequest
argument_list|(
name|template
argument_list|,
name|routeboxUri
argument_list|,
literal|"findBook"
argument_list|,
literal|"Sir Arthur Conan Doyle"
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Received book with author {} and title {}"
argument_list|,
name|book
operator|.
name|getAuthor
argument_list|()
argument_list|,
name|book
operator|.
name|getTitle
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The Adventures of Sherlock Holmes"
argument_list|,
name|book
operator|.
name|getTitle
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Completed Test ---> testRouteboxUsingDefaultContextAndRouteBuilder()"
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

