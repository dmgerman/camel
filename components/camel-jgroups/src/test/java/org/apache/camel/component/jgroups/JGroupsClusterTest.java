begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jgroups
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jgroups
package|;
end_package

begin_import
import|import static
name|java
operator|.
name|lang
operator|.
name|String
operator|.
name|format
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|UUID
operator|.
name|randomUUID
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
name|impl
operator|.
name|DefaultCamelContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jgroups
operator|.
name|JGroupsFilters
operator|.
name|dropNonCoordinatorViews
import|;
end_import

begin_class
DECL|class|JGroupsClusterTest
specifier|public
class|class
name|JGroupsClusterTest
extends|extends
name|Assert
block|{
comment|// Tested state
DECL|field|master
name|String
name|master
decl_stmt|;
DECL|field|nominationCount
name|int
name|nominationCount
decl_stmt|;
comment|// Routing fixtures
DECL|field|jgroupsEndpoint
name|String
name|jgroupsEndpoint
init|=
name|format
argument_list|(
literal|"jgroups:%s?enableViewMessages=true"
argument_list|,
name|randomUUID
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|firstCamelContext
name|DefaultCamelContext
name|firstCamelContext
decl_stmt|;
DECL|field|secondCamelContext
name|DefaultCamelContext
name|secondCamelContext
decl_stmt|;
DECL|class|Builder
class|class
name|Builder
extends|extends
name|RouteBuilder
block|{
annotation|@
name|Override
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
name|jgroupsEndpoint
argument_list|)
operator|.
name|filter
argument_list|(
name|dropNonCoordinatorViews
argument_list|()
argument_list|)
operator|.
name|process
argument_list|(
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
name|String
name|camelContextName
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|camelContextName
operator|.
name|equals
argument_list|(
name|master
argument_list|)
condition|)
block|{
name|master
operator|=
name|camelContextName
expr_stmt|;
name|nominationCount
operator|++
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
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
name|firstCamelContext
operator|=
operator|new
name|DefaultCamelContext
argument_list|()
expr_stmt|;
name|firstCamelContext
operator|.
name|setName
argument_list|(
literal|"firstNode"
argument_list|)
expr_stmt|;
name|firstCamelContext
operator|.
name|addRoutes
argument_list|(
operator|new
name|Builder
argument_list|()
argument_list|)
expr_stmt|;
name|secondCamelContext
operator|=
operator|new
name|DefaultCamelContext
argument_list|()
expr_stmt|;
name|secondCamelContext
operator|.
name|setName
argument_list|(
literal|"secondNode"
argument_list|)
expr_stmt|;
name|secondCamelContext
operator|.
name|addRoutes
argument_list|(
operator|new
name|Builder
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// Tests
annotation|@
name|Test
DECL|method|shouldElectSecondNode ()
specifier|public
name|void
name|shouldElectSecondNode
parameter_list|()
throws|throws
name|Exception
block|{
comment|// When
name|firstCamelContext
operator|.
name|start
argument_list|()
expr_stmt|;
name|String
name|firstMaster
init|=
name|master
decl_stmt|;
name|secondCamelContext
operator|.
name|start
argument_list|()
expr_stmt|;
name|firstCamelContext
operator|.
name|stop
argument_list|()
expr_stmt|;
name|String
name|secondMaster
init|=
name|master
decl_stmt|;
name|secondCamelContext
operator|.
name|stop
argument_list|()
expr_stmt|;
comment|// Then
name|assertEquals
argument_list|(
name|firstCamelContext
operator|.
name|getName
argument_list|()
argument_list|,
name|firstMaster
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|secondCamelContext
operator|.
name|getName
argument_list|()
argument_list|,
name|secondMaster
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|nominationCount
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldKeepMaster ()
specifier|public
name|void
name|shouldKeepMaster
parameter_list|()
throws|throws
name|Exception
block|{
comment|// When
name|firstCamelContext
operator|.
name|start
argument_list|()
expr_stmt|;
name|String
name|firstMaster
init|=
name|master
decl_stmt|;
name|secondCamelContext
operator|.
name|start
argument_list|()
expr_stmt|;
name|secondCamelContext
operator|.
name|stop
argument_list|()
expr_stmt|;
name|String
name|secondMaster
init|=
name|master
decl_stmt|;
name|firstCamelContext
operator|.
name|stop
argument_list|()
expr_stmt|;
comment|// Then
name|assertEquals
argument_list|(
name|firstCamelContext
operator|.
name|getName
argument_list|()
argument_list|,
name|firstMaster
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|firstCamelContext
operator|.
name|getName
argument_list|()
argument_list|,
name|secondMaster
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|nominationCount
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldElectSecondNodeAndReturnToFirst ()
specifier|public
name|void
name|shouldElectSecondNodeAndReturnToFirst
parameter_list|()
throws|throws
name|Exception
block|{
comment|// When
name|firstCamelContext
operator|.
name|start
argument_list|()
expr_stmt|;
name|String
name|firstMaster
init|=
name|master
decl_stmt|;
name|secondCamelContext
operator|.
name|start
argument_list|()
expr_stmt|;
name|firstCamelContext
operator|.
name|stop
argument_list|()
expr_stmt|;
name|String
name|secondMaster
init|=
name|master
decl_stmt|;
name|firstCamelContext
operator|.
name|start
argument_list|()
expr_stmt|;
name|String
name|masterAfterRestartOfFirstNode
init|=
name|master
decl_stmt|;
name|secondCamelContext
operator|.
name|stop
argument_list|()
expr_stmt|;
name|String
name|finalMaster
init|=
name|master
decl_stmt|;
name|firstCamelContext
operator|.
name|stop
argument_list|()
expr_stmt|;
comment|// Then
name|assertEquals
argument_list|(
name|firstCamelContext
operator|.
name|getName
argument_list|()
argument_list|,
name|firstMaster
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|secondCamelContext
operator|.
name|getName
argument_list|()
argument_list|,
name|secondMaster
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|secondCamelContext
operator|.
name|getName
argument_list|()
argument_list|,
name|masterAfterRestartOfFirstNode
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|firstCamelContext
operator|.
name|getName
argument_list|()
argument_list|,
name|finalMaster
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|nominationCount
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

