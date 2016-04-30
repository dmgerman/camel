begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.elsql
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|elsql
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|impl
operator|.
name|JndiRegistry
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
name|After
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
name|springframework
operator|.
name|jdbc
operator|.
name|datasource
operator|.
name|embedded
operator|.
name|EmbeddedDatabase
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
name|embedded
operator|.
name|EmbeddedDatabaseBuilder
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
name|embedded
operator|.
name|EmbeddedDatabaseType
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|instanceOf
import|;
end_import

begin_class
DECL|class|ElSqlProducerStreamListTest
specifier|public
class|class
name|ElSqlProducerStreamListTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|db
specifier|private
name|EmbeddedDatabase
name|db
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
name|jndi
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
comment|// this is the database we create with some initial data for our unit test
name|db
operator|=
operator|new
name|EmbeddedDatabaseBuilder
argument_list|()
operator|.
name|setType
argument_list|(
name|EmbeddedDatabaseType
operator|.
name|DERBY
argument_list|)
operator|.
name|addScript
argument_list|(
literal|"sql/createAndPopulateDatabase.sql"
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"dataSource"
argument_list|,
name|db
argument_list|)
expr_stmt|;
return|return
name|jndi
return|;
block|}
annotation|@
name|Test
DECL|method|testReturnAnIterator ()
specifier|public
name|void
name|testReturnAnIterator
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"testmsg"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|resultBodyAt
argument_list|(
name|mock
argument_list|,
literal|0
argument_list|)
argument_list|,
name|instanceOf
argument_list|(
name|Iterator
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSplit ()
specifier|public
name|void
name|testSplit
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:withSplit"
argument_list|,
literal|"testmsg"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|resultBodyAt
argument_list|(
name|mock
argument_list|,
literal|0
argument_list|)
argument_list|,
name|instanceOf
argument_list|(
name|Map
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|resultBodyAt
argument_list|(
name|mock
argument_list|,
literal|1
argument_list|)
argument_list|,
name|instanceOf
argument_list|(
name|Map
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|resultBodyAt
argument_list|(
name|mock
argument_list|,
literal|2
argument_list|)
argument_list|,
name|instanceOf
argument_list|(
name|Map
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSplitWithModel ()
specifier|public
name|void
name|testSplitWithModel
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:withSplitModel"
argument_list|,
literal|"testmsg"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|resultBodyAt
argument_list|(
name|mock
argument_list|,
literal|0
argument_list|)
argument_list|,
name|instanceOf
argument_list|(
name|Project
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|resultBodyAt
argument_list|(
name|mock
argument_list|,
literal|1
argument_list|)
argument_list|,
name|instanceOf
argument_list|(
name|Project
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|resultBodyAt
argument_list|(
name|mock
argument_list|,
literal|2
argument_list|)
argument_list|,
name|instanceOf
argument_list|(
name|Project
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|resultBodyAt (MockEndpoint result, int index)
specifier|private
name|Object
name|resultBodyAt
parameter_list|(
name|MockEndpoint
name|result
parameter_list|,
name|int
name|index
parameter_list|)
block|{
return|return
name|result
operator|.
name|assertExchangeReceived
argument_list|(
name|index
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
return|;
block|}
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
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
name|db
operator|.
name|shutdown
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
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|getContext
argument_list|()
operator|.
name|getComponent
argument_list|(
literal|"elsql"
argument_list|,
name|ElsqlComponent
operator|.
name|class
argument_list|)
operator|.
name|setDataSource
argument_list|(
name|db
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"elsql:allProjects:elsql/projects.elsql?outputType=StreamList"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:stream"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:withSplit"
argument_list|)
operator|.
name|to
argument_list|(
literal|"elsql:allProjects:elsql/projects.elsql?outputType=StreamList"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:stream"
argument_list|)
operator|.
name|split
argument_list|(
name|body
argument_list|()
argument_list|)
operator|.
name|streaming
argument_list|()
operator|.
name|to
argument_list|(
literal|"log:row"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|end
argument_list|()
expr_stmt|;
name|from
argument_list|(
literal|"direct:withSplitModel"
argument_list|)
operator|.
name|to
argument_list|(
literal|"elsql:allProjects:elsql/projects.elsql?outputType=StreamList&outputClass=org.apache.camel.component.elsql.Project"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:stream"
argument_list|)
operator|.
name|split
argument_list|(
name|body
argument_list|()
argument_list|)
operator|.
name|streaming
argument_list|()
operator|.
name|to
argument_list|(
literal|"log:row"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|end
argument_list|()
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

