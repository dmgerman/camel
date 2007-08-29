begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jdbc
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jdbc
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
name|CamelTemplate
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
name|Endpoint
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
name|util
operator|.
name|jndi
operator|.
name|JndiContext
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
name|DriverManager
import|;
end_import

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
name|HashMap
import|;
end_import

begin_comment
comment|/**  * @version $Revision: 520220 $  */
end_comment

begin_class
DECL|class|JdbcRouteTest
specifier|public
class|class
name|JdbcRouteTest
extends|extends
name|TestCase
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
comment|/*     protected String driverClass = "org.apache.derby.jdbc.EmbeddedDriver";     protected String url = "jdbc:derby:target/testdb;create=true"; */
DECL|field|connection
specifier|protected
name|Connection
name|connection
decl_stmt|;
DECL|method|testPojoRoutes ()
specifier|public
name|void
name|testPojoRoutes
parameter_list|()
throws|throws
name|Exception
block|{
name|DataSource
name|ds
init|=
operator|new
name|TestDataSource
argument_list|(
name|url
argument_list|,
name|user
argument_list|,
name|password
argument_list|)
decl_stmt|;
comment|// START SNIPPET: register
name|JndiContext
name|context
init|=
operator|new
name|JndiContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|bind
argument_list|(
literal|"testdb"
argument_list|,
name|ds
argument_list|)
expr_stmt|;
name|CamelContext
name|camelContext
init|=
operator|new
name|DefaultCamelContext
argument_list|(
name|context
argument_list|)
decl_stmt|;
comment|// END SNIPPET: register
comment|// START SNIPPET: route
comment|// lets add simple route
name|camelContext
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
literal|"direct:hello"
argument_list|)
operator|.
name|to
argument_list|(
literal|"jdbc:testdb?readSize=100"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
comment|// END SNIPPET: route
name|camelContext
operator|.
name|start
argument_list|()
expr_stmt|;
comment|// START SNIPPET: invoke
name|Endpoint
argument_list|<
name|Exchange
argument_list|>
name|endpoint
init|=
name|camelContext
operator|.
name|getEndpoint
argument_list|(
literal|"direct:hello"
argument_list|)
decl_stmt|;
name|CamelTemplate
argument_list|<
name|Exchange
argument_list|>
name|template
init|=
operator|new
name|CamelTemplate
argument_list|<
name|Exchange
argument_list|>
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"select * from customer"
argument_list|)
expr_stmt|;
name|Exchange
name|out
init|=
name|template
operator|.
name|send
argument_list|(
name|endpoint
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|out
operator|.
name|getOut
argument_list|()
argument_list|)
expr_stmt|;
name|ArrayList
argument_list|<
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|data
init|=
name|out
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|(
name|ArrayList
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"out body could not be converted to an ArrayList - was: "
operator|+
name|out
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|,
name|data
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|data
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// END SNIPPET: invoke
name|camelContext
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
comment|/**      *      */
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
name|Class
operator|.
name|forName
argument_list|(
name|driverClass
argument_list|)
expr_stmt|;
comment|// sysinfo.main(new String[] { "JdbcRouteTest.setUp()" });
name|connection
operator|=
name|DriverManager
operator|.
name|getConnection
argument_list|(
name|url
argument_list|,
name|user
argument_list|,
name|password
argument_list|)
expr_stmt|;
name|connection
operator|.
name|createStatement
argument_list|()
operator|.
name|execute
argument_list|(
literal|"create table customer (id varchar(15), name varchar(10))"
argument_list|)
expr_stmt|;
name|connection
operator|.
name|createStatement
argument_list|()
operator|.
name|executeUpdate
argument_list|(
literal|"insert into customer values('cust1','jstrachan')"
argument_list|)
expr_stmt|;
name|connection
operator|.
name|createStatement
argument_list|()
operator|.
name|executeUpdate
argument_list|(
literal|"insert into customer values('cust2','nsandhu')"
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
if|if
condition|(
name|connection
operator|!=
literal|null
condition|)
block|{
name|connection
operator|.
name|createStatement
argument_list|()
operator|.
name|execute
argument_list|(
literal|"drop table customer"
argument_list|)
expr_stmt|;
name|connection
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|// DriverManager.getConnection("jdbc:derby;shutdown=true").close();
block|}
block|}
end_class

end_unit

