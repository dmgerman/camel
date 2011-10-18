begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.osgi.sql
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|osgi
operator|.
name|sql
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
name|RuntimeCamelException
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
name|component
operator|.
name|sql
operator|.
name|SqlComponent
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
name|sql
operator|.
name|SqlConstants
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
name|itest
operator|.
name|osgi
operator|.
name|OSGiIntegrationTestSupport
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
name|SpringCamelContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|derby
operator|.
name|jdbc
operator|.
name|EmbeddedDataSource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|testing
operator|.
name|Helper
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
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|Option
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|junit
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|junit
operator|.
name|JUnit4TestRunner
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|dao
operator|.
name|DataAccessException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|dao
operator|.
name|EmptyResultDataAccessException
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

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|osgi
operator|.
name|context
operator|.
name|support
operator|.
name|OsgiBundleXmlApplicationContext
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
name|Iterator
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

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|CoreOptions
operator|.
name|equinox
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|CoreOptions
operator|.
name|felix
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|CoreOptions
operator|.
name|mavenBundle
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|OptionUtils
operator|.
name|combine
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|container
operator|.
name|def
operator|.
name|PaxRunnerOptions
operator|.
name|scanFeatures
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|container
operator|.
name|def
operator|.
name|PaxRunnerOptions
operator|.
name|workingDirectory
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|JUnit4TestRunner
operator|.
name|class
argument_list|)
DECL|class|SqlRouteTest
specifier|public
class|class
name|SqlRouteTest
extends|extends
name|OSGiIntegrationTestSupport
block|{
DECL|field|driverClass
specifier|protected
name|String
name|driverClass
init|=
literal|"org.apache.derby.jdbc.EmbeddedDriver"
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
DECL|field|applicationContext
specifier|protected
name|OsgiBundleXmlApplicationContext
name|applicationContext
decl_stmt|;
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
name|applicationContext
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|setThreadContextClassLoader
argument_list|()
expr_stmt|;
name|applicationContext
operator|=
operator|new
name|OsgiBundleXmlApplicationContext
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"org/apache/camel/itest/osgi/sql/springSqlRouteContext.xml"
block|}
argument_list|)
expr_stmt|;
if|if
condition|(
name|bundleContext
operator|!=
literal|null
condition|)
block|{
name|applicationContext
operator|.
name|setBundleContext
argument_list|(
name|bundleContext
argument_list|)
expr_stmt|;
name|applicationContext
operator|.
name|refresh
argument_list|()
expr_stmt|;
block|}
name|ds
operator|=
operator|(
name|DataSource
operator|)
name|applicationContext
operator|.
name|getBean
argument_list|(
literal|"dataSource"
argument_list|)
expr_stmt|;
name|jdbcTemplate
operator|=
operator|new
name|JdbcTemplate
argument_list|(
name|ds
argument_list|)
expr_stmt|;
return|return
name|SpringCamelContext
operator|.
name|springCamelContext
argument_list|(
name|applicationContext
argument_list|)
return|;
block|}
annotation|@
name|Test
DECL|method|testListBody ()
specifier|public
name|void
name|testListBody
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
name|List
argument_list|<
name|Object
argument_list|>
name|body
init|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|body
operator|.
name|add
argument_list|(
literal|"ASF"
argument_list|)
expr_stmt|;
name|body
operator|.
name|add
argument_list|(
literal|"Camel"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:list"
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|received
init|=
name|assertIsInstanceOf
argument_list|(
name|List
operator|.
name|class
argument_list|,
name|mock
operator|.
name|getReceivedExchanges
argument_list|()
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
argument_list|()
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|firstRow
init|=
name|assertIsInstanceOf
argument_list|(
name|Map
operator|.
name|class
argument_list|,
name|received
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|firstRow
operator|.
name|get
argument_list|(
literal|"ID"
argument_list|)
argument_list|)
expr_stmt|;
comment|// unlikely to have accidental ordering with 3 rows x 3 columns
for|for
control|(
name|Object
name|obj
range|:
name|received
control|)
block|{
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|row
init|=
name|assertIsInstanceOf
argument_list|(
name|Map
operator|.
name|class
argument_list|,
name|obj
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"not preserving key ordering for a given row keys: "
operator|+
name|row
operator|.
name|keySet
argument_list|()
argument_list|,
name|isOrdered
argument_list|(
name|row
operator|.
name|keySet
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testLowNumberOfParameter ()
specifier|public
name|void
name|testLowNumberOfParameter
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:list"
argument_list|,
literal|"ASF"
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeCamelException
name|e
parameter_list|)
block|{
comment|// should have DataAccessException thrown
name|assertTrue
argument_list|(
literal|"Exception thrown is wrong"
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
operator|instanceof
name|DataAccessException
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testInsert ()
specifier|public
name|void
name|testInsert
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
literal|"direct:insert"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|10
block|,
literal|"test"
block|,
literal|"test"
block|}
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
try|try
block|{
name|String
name|projectName
init|=
operator|(
name|String
operator|)
name|jdbcTemplate
operator|.
name|queryForObject
argument_list|(
literal|"select project from projects where id = 10"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"test"
argument_list|,
name|projectName
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|EmptyResultDataAccessException
name|e
parameter_list|)
block|{
name|fail
argument_list|(
literal|"no row inserted"
argument_list|)
expr_stmt|;
block|}
name|Integer
name|actualUpdateCount
init|=
name|mock
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SqlConstants
operator|.
name|SQL_UPDATE_COUNT
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
operator|(
name|Integer
operator|)
literal|1
argument_list|,
name|actualUpdateCount
argument_list|)
expr_stmt|;
block|}
DECL|method|isOrdered (Set<?> keySet)
specifier|private
name|boolean
name|isOrdered
parameter_list|(
name|Set
argument_list|<
name|?
argument_list|>
name|keySet
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"isOrdered() requires the following keys: id, project, license"
argument_list|,
name|keySet
operator|.
name|contains
argument_list|(
literal|"id"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"isOrdered() requires the following keys: id, project, license"
argument_list|,
name|keySet
operator|.
name|contains
argument_list|(
literal|"project"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"isOrdered() requires the following keys: id, project, license"
argument_list|,
name|keySet
operator|.
name|contains
argument_list|(
literal|"license"
argument_list|)
argument_list|)
expr_stmt|;
comment|// the implementation uses a case insensitive Map
specifier|final
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|keySet
operator|.
name|iterator
argument_list|()
decl_stmt|;
return|return
literal|"id"
operator|.
name|equalsIgnoreCase
argument_list|(
name|assertIsInstanceOf
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|it
operator|.
name|next
argument_list|()
argument_list|)
argument_list|)
operator|&&
literal|"project"
operator|.
name|equalsIgnoreCase
argument_list|(
name|assertIsInstanceOf
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|it
operator|.
name|next
argument_list|()
argument_list|)
argument_list|)
operator|&&
literal|"license"
operator|.
name|equalsIgnoreCase
argument_list|(
name|assertIsInstanceOf
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|it
operator|.
name|next
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Configuration
DECL|method|configure ()
specifier|public
specifier|static
name|Option
index|[]
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|Option
index|[]
name|options
init|=
name|combine
argument_list|(
comment|// Default karaf environment
name|Helper
operator|.
name|getDefaultOptions
argument_list|(
comment|// this is how you set the default log level when using pax logging (logProfile)
name|Helper
operator|.
name|setLogLevel
argument_list|(
literal|"WARN"
argument_list|)
argument_list|)
argument_list|,
comment|// install the spring.
name|scanFeatures
argument_list|(
name|getKarafFeatureUrl
argument_list|()
argument_list|,
literal|"spring"
argument_list|)
argument_list|,
comment|// using the features to install the camel components
name|scanFeatures
argument_list|(
name|getCamelKarafFeatureUrl
argument_list|()
argument_list|,
literal|"camel-core"
argument_list|,
literal|"camel-spring"
argument_list|,
literal|"camel-test"
argument_list|,
literal|"camel-sql"
argument_list|)
argument_list|,
name|mavenBundle
argument_list|()
operator|.
name|groupId
argument_list|(
literal|"org.apache.derby"
argument_list|)
operator|.
name|artifactId
argument_list|(
literal|"derby"
argument_list|)
operator|.
name|version
argument_list|(
literal|"10.4.2.0"
argument_list|)
argument_list|,
name|workingDirectory
argument_list|(
literal|"target/paxrunner/"
argument_list|)
argument_list|,
name|felix
argument_list|()
argument_list|,
name|equinox
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|options
return|;
block|}
block|}
end_class

end_unit

