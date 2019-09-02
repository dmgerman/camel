begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|DefaultCamelContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
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

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|SqlConsumerOutputTypeSelectOneTest
specifier|public
class|class
name|SqlConsumerOutputTypeSelectOneTest
block|{
DECL|field|db
specifier|private
name|EmbeddedDatabase
name|db
decl_stmt|;
DECL|field|camel1
specifier|private
name|DefaultCamelContext
name|camel1
decl_stmt|;
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
name|camel1
operator|=
operator|new
name|DefaultCamelContext
argument_list|()
expr_stmt|;
name|camel1
operator|.
name|setName
argument_list|(
literal|"camel-1"
argument_list|)
expr_stmt|;
name|camel1
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
name|db
argument_list|)
expr_stmt|;
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
name|camel1
operator|.
name|stop
argument_list|()
expr_stmt|;
name|db
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSelectOneWithClass ()
specifier|public
name|void
name|testSelectOneWithClass
parameter_list|()
throws|throws
name|Exception
block|{
name|camel1
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
literal|"sql:select * from projects where id=3?outputType=SelectOne&outputClass=org.apache.camel.component.sql.ProjectModel&initialDelay=0&delay=50"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|camel1
operator|.
name|start
argument_list|()
expr_stmt|;
name|MockEndpoint
name|mock
init|=
operator|(
name|MockEndpoint
operator|)
name|camel1
operator|.
name|getEndpoint
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
name|mock
operator|.
name|assertIsSatisfied
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Exchange
argument_list|>
name|exchanges
init|=
name|mock
operator|.
name|getReceivedExchanges
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertThat
argument_list|(
name|exchanges
operator|.
name|size
argument_list|()
argument_list|,
name|CoreMatchers
operator|.
name|is
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|ProjectModel
name|result
init|=
name|exchanges
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|ProjectModel
operator|.
name|class
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertThat
argument_list|(
name|result
operator|.
name|getId
argument_list|()
argument_list|,
name|CoreMatchers
operator|.
name|is
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertThat
argument_list|(
name|result
operator|.
name|getProject
argument_list|()
argument_list|,
name|CoreMatchers
operator|.
name|is
argument_list|(
literal|"Linux"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertThat
argument_list|(
name|result
operator|.
name|getLicense
argument_list|()
argument_list|,
name|CoreMatchers
operator|.
name|is
argument_list|(
literal|"XXX"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSelectOneWithoutClass ()
specifier|public
name|void
name|testSelectOneWithoutClass
parameter_list|()
throws|throws
name|Exception
block|{
name|camel1
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
literal|"sql:select * from projects where id=3?outputType=SelectOne&initialDelay=0&delay=50"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|camel1
operator|.
name|start
argument_list|()
expr_stmt|;
name|MockEndpoint
name|mock
init|=
operator|(
name|MockEndpoint
operator|)
name|camel1
operator|.
name|getEndpoint
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
name|mock
operator|.
name|assertIsSatisfied
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Exchange
argument_list|>
name|exchanges
init|=
name|mock
operator|.
name|getReceivedExchanges
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertThat
argument_list|(
name|exchanges
operator|.
name|size
argument_list|()
argument_list|,
name|CoreMatchers
operator|.
name|is
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|result
init|=
name|exchanges
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Map
operator|.
name|class
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertThat
argument_list|(
operator|(
name|Integer
operator|)
name|result
operator|.
name|get
argument_list|(
literal|"ID"
argument_list|)
argument_list|,
name|CoreMatchers
operator|.
name|is
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertThat
argument_list|(
operator|(
name|String
operator|)
name|result
operator|.
name|get
argument_list|(
literal|"PROJECT"
argument_list|)
argument_list|,
name|CoreMatchers
operator|.
name|is
argument_list|(
literal|"Linux"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertThat
argument_list|(
operator|(
name|String
operator|)
name|result
operator|.
name|get
argument_list|(
literal|"LICENSE"
argument_list|)
argument_list|,
name|CoreMatchers
operator|.
name|is
argument_list|(
literal|"XXX"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSelectOneSingleColumn ()
specifier|public
name|void
name|testSelectOneSingleColumn
parameter_list|()
throws|throws
name|Exception
block|{
name|camel1
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
literal|"sql:select project from projects where id=3?outputType=SelectOne&initialDelay=0&delay=50"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|camel1
operator|.
name|start
argument_list|()
expr_stmt|;
name|MockEndpoint
name|mock
init|=
operator|(
name|MockEndpoint
operator|)
name|camel1
operator|.
name|getEndpoint
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
name|mock
operator|.
name|assertIsSatisfied
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Exchange
argument_list|>
name|exchanges
init|=
name|mock
operator|.
name|getReceivedExchanges
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertThat
argument_list|(
name|exchanges
operator|.
name|size
argument_list|()
argument_list|,
name|CoreMatchers
operator|.
name|is
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|result
init|=
name|exchanges
operator|.
name|get
argument_list|(
literal|0
argument_list|)
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
name|Assert
operator|.
name|assertThat
argument_list|(
name|result
argument_list|,
name|CoreMatchers
operator|.
name|is
argument_list|(
literal|"Linux"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSelectOneSingleColumnCount ()
specifier|public
name|void
name|testSelectOneSingleColumnCount
parameter_list|()
throws|throws
name|Exception
block|{
name|camel1
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
literal|"sql:select count(*) from projects?outputType=SelectOne&initialDelay=0&delay=50"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|camel1
operator|.
name|start
argument_list|()
expr_stmt|;
name|MockEndpoint
name|mock
init|=
operator|(
name|MockEndpoint
operator|)
name|camel1
operator|.
name|getEndpoint
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
name|mock
operator|.
name|assertIsSatisfied
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Exchange
argument_list|>
name|exchanges
init|=
name|mock
operator|.
name|getReceivedExchanges
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertThat
argument_list|(
name|exchanges
operator|.
name|size
argument_list|()
argument_list|,
name|CoreMatchers
operator|.
name|is
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|Long
name|result
init|=
name|exchanges
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Long
operator|.
name|class
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertThat
argument_list|(
name|result
argument_list|,
name|CoreMatchers
operator|.
name|is
argument_list|(
literal|3L
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

