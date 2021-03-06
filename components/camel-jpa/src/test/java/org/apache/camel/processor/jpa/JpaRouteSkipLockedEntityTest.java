begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.jpa
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|jpa
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Connection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|SQLException
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
name|concurrent
operator|.
name|locks
operator|.
name|Condition
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|locks
operator|.
name|ReentrantLock
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
name|apache
operator|.
name|camel
operator|.
name|examples
operator|.
name|VersionedItem
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
name|spring
operator|.
name|SpringRouteBuilder
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
name|Ignore
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
annotation|@
name|Ignore
argument_list|(
literal|"Need the fix of OPENJPA-2461"
argument_list|)
DECL|class|JpaRouteSkipLockedEntityTest
specifier|public
class|class
name|JpaRouteSkipLockedEntityTest
extends|extends
name|AbstractJpaTest
block|{
DECL|field|SELECT_ALL_STRING
specifier|protected
specifier|static
specifier|final
name|String
name|SELECT_ALL_STRING
init|=
literal|"select x from "
operator|+
name|VersionedItem
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|" x"
decl_stmt|;
DECL|field|count
specifier|private
name|int
name|count
decl_stmt|;
DECL|field|lock
specifier|private
specifier|final
name|ReentrantLock
name|lock
init|=
operator|new
name|ReentrantLock
argument_list|()
decl_stmt|;
DECL|field|cond1
specifier|private
name|Condition
name|cond1
init|=
name|lock
operator|.
name|newCondition
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|testRouteJpa ()
specifier|public
name|void
name|testRouteJpa
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock1
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result1"
argument_list|)
decl_stmt|;
name|mock1
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock2
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result2"
argument_list|)
decl_stmt|;
name|mock2
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"jpa://"
operator|+
name|VersionedItem
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
operator|new
name|VersionedItem
argument_list|(
literal|"one"
argument_list|)
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"jpa://"
operator|+
name|VersionedItem
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
operator|new
name|VersionedItem
argument_list|(
literal|"two"
argument_list|)
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"jpa://"
operator|+
name|VersionedItem
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
operator|new
name|VersionedItem
argument_list|(
literal|"three"
argument_list|)
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"jpa://"
operator|+
name|VersionedItem
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
operator|new
name|VersionedItem
argument_list|(
literal|"four"
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|startRoute
argument_list|(
literal|"second"
argument_list|)
expr_stmt|;
name|this
operator|.
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|startRoute
argument_list|(
literal|"first"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|//force test to wait till finished
name|this
operator|.
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|stopRoute
argument_list|(
literal|"first"
argument_list|)
expr_stmt|;
name|this
operator|.
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|stopRoute
argument_list|(
literal|"second"
argument_list|)
expr_stmt|;
name|setLockTimeout
argument_list|(
literal|60
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|list
init|=
name|entityManager
operator|.
name|createQuery
argument_list|(
name|selectAllString
argument_list|()
argument_list|)
operator|.
name|getResultList
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|list
operator|.
name|size
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
block|{
return|return
operator|new
name|SpringRouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|String
name|options
init|=
literal|"?skipLockedEntity=true"
decl_stmt|;
comment|//&lockModeType=PESSIMISTIC_FORCE_INCREMENT";
name|from
argument_list|(
literal|"jpa://"
operator|+
name|VersionedItem
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
name|options
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"first"
argument_list|)
operator|.
name|autoStartup
argument_list|(
literal|false
argument_list|)
operator|.
name|bean
argument_list|(
operator|new
name|WaitLatch
argument_list|()
argument_list|)
operator|.
name|log
argument_list|(
literal|"route1: ${body}"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result1"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"jpa2://select"
operator|+
name|options
operator|+
literal|"&query=select s from VersionedItem s"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"second"
argument_list|)
operator|.
name|autoStartup
argument_list|(
literal|false
argument_list|)
operator|.
name|bean
argument_list|(
operator|new
name|WaitLatch
argument_list|()
argument_list|)
operator|.
name|log
argument_list|(
literal|"route2: ${body}"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result2"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|routeXml ()
specifier|protected
name|String
name|routeXml
parameter_list|()
block|{
return|return
literal|"org/apache/camel/processor/jpa/springJpaRouteSkipLockedTest.xml"
return|;
block|}
annotation|@
name|Override
DECL|method|selectAllString ()
specifier|protected
name|String
name|selectAllString
parameter_list|()
block|{
return|return
name|SELECT_ALL_STRING
return|;
block|}
DECL|class|WaitLatch
specifier|public
class|class
name|WaitLatch
block|{
DECL|method|onMessage (VersionedItem body)
specifier|public
name|void
name|onMessage
parameter_list|(
name|VersionedItem
name|body
parameter_list|)
throws|throws
name|Exception
block|{
name|lock
operator|.
name|lock
argument_list|()
expr_stmt|;
try|try
block|{
name|count
operator|++
expr_stmt|;
comment|// if (count != 1) {
name|cond1
operator|.
name|signal
argument_list|()
expr_stmt|;
comment|// }
comment|// if not last
if|if
condition|(
name|count
operator|!=
literal|4
condition|)
block|{
name|cond1
operator|.
name|await
argument_list|()
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|lock
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
block|}
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
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|setLockTimeout
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
DECL|method|setLockTimeout (int timeout)
specifier|public
name|void
name|setLockTimeout
parameter_list|(
name|int
name|timeout
parameter_list|)
throws|throws
name|SQLException
block|{
name|entityManager
operator|.
name|getTransaction
argument_list|()
operator|.
name|begin
argument_list|()
expr_stmt|;
name|Connection
name|connection
init|=
name|entityManager
operator|.
name|unwrap
argument_list|(
name|java
operator|.
name|sql
operator|.
name|Connection
operator|.
name|class
argument_list|)
decl_stmt|;
name|connection
operator|.
name|createStatement
argument_list|()
operator|.
name|execute
argument_list|(
literal|"CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY('derby.locks.waitTimeout', '"
operator|+
name|timeout
operator|+
literal|"')"
argument_list|)
expr_stmt|;
name|entityManager
operator|.
name|getTransaction
argument_list|()
operator|.
name|commit
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

