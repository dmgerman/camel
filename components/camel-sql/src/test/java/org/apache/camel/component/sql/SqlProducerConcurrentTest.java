begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sql
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sql
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Callable
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
name|ConcurrentHashMap
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
name|ExecutorService
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
name|Executors
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
name|Future
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|sql
operator|.
name|DataSource
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
name|builder
operator|.
name|RouteBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jdbc
operator|.
name|core
operator|.
name|JdbcTemplate
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jdbc
operator|.
name|datasource
operator|.
name|SingleConnectionDataSource
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|SqlProducerConcurrentTest
specifier|public
class|class
name|SqlProducerConcurrentTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|driverClass
specifier|protected
name|String
name|driverClass
init|=
literal|"org.hsqldb.jdbcDriver"
decl_stmt|;
DECL|field|url
specifier|protected
name|String
name|url
init|=
literal|"jdbc:hsqldb:mem:camel_jdbc"
decl_stmt|;
DECL|field|user
specifier|protected
name|String
name|user
init|=
literal|"sa"
decl_stmt|;
DECL|field|password
specifier|protected
name|String
name|password
init|=
literal|""
decl_stmt|;
DECL|field|ds
specifier|private
name|DataSource
name|ds
decl_stmt|;
DECL|field|jdbcTemplate
specifier|private
name|JdbcTemplate
name|jdbcTemplate
decl_stmt|;
DECL|method|testNoConcurrentProducers ()
specifier|public
name|void
name|testNoConcurrentProducers
parameter_list|()
throws|throws
name|Exception
block|{
name|doSendMessages
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
DECL|method|testConcurrentProducers ()
specifier|public
name|void
name|testConcurrentProducers
parameter_list|()
throws|throws
name|Exception
block|{
name|doSendMessages
argument_list|(
literal|10
argument_list|,
literal|5
argument_list|)
expr_stmt|;
block|}
DECL|method|doSendMessages (int files, int poolSize)
specifier|private
name|void
name|doSendMessages
parameter_list|(
name|int
name|files
parameter_list|,
name|int
name|poolSize
parameter_list|)
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
name|files
argument_list|)
expr_stmt|;
name|ExecutorService
name|executor
init|=
name|Executors
operator|.
name|newFixedThreadPool
argument_list|(
name|poolSize
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|Integer
argument_list|,
name|Future
argument_list|>
name|responses
init|=
operator|new
name|ConcurrentHashMap
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|files
condition|;
name|i
operator|++
control|)
block|{
specifier|final
name|int
name|index
init|=
name|i
decl_stmt|;
name|Future
name|out
init|=
name|executor
operator|.
name|submit
argument_list|(
operator|new
name|Callable
argument_list|<
name|Object
argument_list|>
argument_list|()
block|{
specifier|public
name|Object
name|call
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|id
init|=
name|index
operator|%
literal|3
decl_stmt|;
return|return
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:simple"
argument_list|,
literal|""
operator|+
name|id
argument_list|)
return|;
block|}
block|}
argument_list|)
decl_stmt|;
name|responses
operator|.
name|put
argument_list|(
name|index
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|files
argument_list|,
name|responses
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|files
condition|;
name|i
operator|++
control|)
block|{
name|List
name|rows
init|=
operator|(
name|List
operator|)
name|responses
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|Map
name|columns
init|=
operator|(
name|Map
operator|)
name|rows
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|i
operator|%
literal|3
operator|==
literal|0
condition|)
block|{
name|assertEquals
argument_list|(
literal|"Camel"
argument_list|,
name|columns
operator|.
name|get
argument_list|(
literal|"PROJECT"
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|i
operator|%
literal|3
operator|==
literal|1
condition|)
block|{
name|assertEquals
argument_list|(
literal|"AMQ"
argument_list|,
name|columns
operator|.
name|get
argument_list|(
literal|"PROJECT"
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertEquals
argument_list|(
literal|"Linux"
argument_list|,
name|columns
operator|.
name|get
argument_list|(
literal|"PROJECT"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|Class
operator|.
name|forName
argument_list|(
name|driverClass
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|jdbcTemplate
operator|=
operator|new
name|JdbcTemplate
argument_list|(
name|ds
argument_list|)
expr_stmt|;
name|jdbcTemplate
operator|.
name|execute
argument_list|(
literal|"create table projects (id integer primary key,"
operator|+
literal|"project varchar(10), license varchar(5))"
argument_list|)
expr_stmt|;
name|jdbcTemplate
operator|.
name|execute
argument_list|(
literal|"insert into projects values (0, 'Camel', 'ASF')"
argument_list|)
expr_stmt|;
name|jdbcTemplate
operator|.
name|execute
argument_list|(
literal|"insert into projects values (1, 'AMQ', 'ASF')"
argument_list|)
expr_stmt|;
name|jdbcTemplate
operator|.
name|execute
argument_list|(
literal|"insert into projects values (2, 'Linux', 'GPL')"
argument_list|)
expr_stmt|;
block|}
DECL|method|tearDown ()
specifier|protected
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
name|JdbcTemplate
name|jdbcTemplate
init|=
operator|new
name|JdbcTemplate
argument_list|(
name|ds
argument_list|)
decl_stmt|;
name|jdbcTemplate
operator|.
name|execute
argument_list|(
literal|"drop table projects"
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
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|ds
operator|=
operator|new
name|SingleConnectionDataSource
argument_list|(
name|url
argument_list|,
name|user
argument_list|,
name|password
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|getContext
argument_list|()
operator|.
name|getComponent
argument_list|(
literal|"sql"
argument_list|,
name|SqlComponent
operator|.
name|class
argument_list|)
operator|.
name|setDataSource
argument_list|(
name|ds
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:simple"
argument_list|)
operator|.
name|to
argument_list|(
literal|"sql:select * from projects where id = # order by id"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

