begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
package|;
end_package

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
name|assertNotNull
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

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Field
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
name|Component
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
name|ComponentConfiguration
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
name|EndpointConfiguration
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
name|URIField
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

begin_comment
comment|/**  * @see ComponentConfigurationTest for tests using the {@link ComponentConfiguration} mechanism  */
end_comment

begin_class
DECL|class|ConfigurationHelperTest
specifier|public
class|class
name|ConfigurationHelperTest
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
name|ConfigurationHelperTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|URIDUMP_SCHEME
specifier|private
specifier|static
specifier|final
name|String
name|URIDUMP_SCHEME
init|=
literal|"uri-dump"
decl_stmt|;
DECL|field|DUMMY_SCHEME
specifier|private
specifier|static
specifier|final
name|String
name|DUMMY_SCHEME
init|=
literal|"dummy"
decl_stmt|;
DECL|field|context
specifier|private
name|CamelContext
name|context
decl_stmt|;
annotation|@
name|Before
DECL|method|createContext ()
specifier|public
name|void
name|createContext
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
name|Component
name|component
init|=
operator|new
name|ConfiguredComponent
argument_list|()
decl_stmt|;
name|context
operator|.
name|addComponent
argument_list|(
name|URIDUMP_SCHEME
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|context
operator|.
name|addComponent
argument_list|(
name|DUMMY_SCHEME
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
comment|// so that TypeConverters are available
block|}
annotation|@
name|After
DECL|method|destroyContext ()
specifier|public
name|void
name|destroyContext
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|context
operator|=
literal|null
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUrnNoQuery ()
specifier|public
name|void
name|testUrnNoQuery
parameter_list|()
throws|throws
name|Exception
block|{
name|EndpointConfiguration
name|cfg
init|=
name|ConfigurationHelper
operator|.
name|createConfiguration
argument_list|(
literal|"uri-dump:foo"
argument_list|,
name|context
argument_list|)
decl_stmt|;
name|logConfigurationObject
argument_list|(
name|cfg
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|URIDUMP_SCHEME
argument_list|,
name|cfg
operator|.
name|getParameter
argument_list|(
name|EndpointConfiguration
operator|.
name|URI_SCHEME
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|cfg
operator|.
name|getParameter
argument_list|(
name|EndpointConfiguration
operator|.
name|URI_SCHEME_SPECIFIC_PART
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"URNs don't set the authority field"
argument_list|,
literal|null
argument_list|,
name|cfg
operator|.
name|getParameter
argument_list|(
name|EndpointConfiguration
operator|.
name|URI_AUTHORITY
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"URNs don't set the userInfo field"
argument_list|,
literal|null
argument_list|,
name|cfg
operator|.
name|getParameter
argument_list|(
name|EndpointConfiguration
operator|.
name|URI_USER_INFO
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"URNs don't set the host field"
argument_list|,
literal|null
argument_list|,
name|cfg
operator|.
name|getParameter
argument_list|(
name|EndpointConfiguration
operator|.
name|URI_HOST
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"URNs don't set the port field"
argument_list|,
name|Integer
operator|.
name|valueOf
argument_list|(
operator|-
literal|1
argument_list|)
argument_list|,
name|cfg
operator|.
name|getParameter
argument_list|(
name|EndpointConfiguration
operator|.
name|URI_PORT
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"URNs don't set the path field"
argument_list|,
literal|null
argument_list|,
name|cfg
operator|.
name|getParameter
argument_list|(
name|EndpointConfiguration
operator|.
name|URI_PATH
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"URNs don't set the query field"
argument_list|,
literal|null
argument_list|,
name|cfg
operator|.
name|getParameter
argument_list|(
name|EndpointConfiguration
operator|.
name|URI_QUERY
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"URNs don't set the fragment field"
argument_list|,
literal|null
argument_list|,
name|cfg
operator|.
name|getParameter
argument_list|(
name|EndpointConfiguration
operator|.
name|URI_FRAGMENT
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUrnWithQuery ()
specifier|public
name|void
name|testUrnWithQuery
parameter_list|()
throws|throws
name|Exception
block|{
name|EndpointConfiguration
name|cfg
init|=
name|ConfigurationHelper
operator|.
name|createConfiguration
argument_list|(
literal|"uri-dump:hadrian@localhost:9001/context/path/?bar=true&baz=2#1234"
argument_list|,
name|context
argument_list|)
decl_stmt|;
name|logConfigurationObject
argument_list|(
name|cfg
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|URIDUMP_SCHEME
argument_list|,
name|cfg
operator|.
name|getParameter
argument_list|(
name|EndpointConfiguration
operator|.
name|URI_SCHEME
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"hadrian@localhost:9001/context/path/?bar=true&baz=2#1234"
argument_list|,
name|cfg
operator|.
name|getParameter
argument_list|(
name|EndpointConfiguration
operator|.
name|URI_SCHEME_SPECIFIC_PART
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"URNs don't set the authority field"
argument_list|,
literal|null
argument_list|,
name|cfg
operator|.
name|getParameter
argument_list|(
name|EndpointConfiguration
operator|.
name|URI_AUTHORITY
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"URNs don't set the userInfo field"
argument_list|,
literal|null
argument_list|,
name|cfg
operator|.
name|getParameter
argument_list|(
name|EndpointConfiguration
operator|.
name|URI_USER_INFO
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"URNs don't set the host field"
argument_list|,
literal|null
argument_list|,
name|cfg
operator|.
name|getParameter
argument_list|(
name|EndpointConfiguration
operator|.
name|URI_HOST
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"URNs don't set the port field"
argument_list|,
name|Integer
operator|.
name|valueOf
argument_list|(
operator|-
literal|1
argument_list|)
argument_list|,
name|cfg
operator|.
name|getParameter
argument_list|(
name|EndpointConfiguration
operator|.
name|URI_PORT
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"URNs don't set the path field"
argument_list|,
literal|null
argument_list|,
name|cfg
operator|.
name|getParameter
argument_list|(
name|EndpointConfiguration
operator|.
name|URI_PATH
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"URNs don't set the query field"
argument_list|,
literal|null
argument_list|,
name|cfg
operator|.
name|getParameter
argument_list|(
name|EndpointConfiguration
operator|.
name|URI_QUERY
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"URNs don't set the fragment field"
argument_list|,
literal|null
argument_list|,
name|cfg
operator|.
name|getParameter
argument_list|(
name|EndpointConfiguration
operator|.
name|URI_FRAGMENT
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUrlSimple ()
specifier|public
name|void
name|testUrlSimple
parameter_list|()
throws|throws
name|Exception
block|{
name|EndpointConfiguration
name|cfg
init|=
name|ConfigurationHelper
operator|.
name|createConfiguration
argument_list|(
literal|"uri-dump://foo"
argument_list|,
name|context
argument_list|)
decl_stmt|;
name|logConfigurationObject
argument_list|(
name|cfg
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|URIDUMP_SCHEME
argument_list|,
name|cfg
operator|.
name|getParameter
argument_list|(
name|EndpointConfiguration
operator|.
name|URI_SCHEME
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"//foo"
argument_list|,
name|cfg
operator|.
name|getParameter
argument_list|(
name|EndpointConfiguration
operator|.
name|URI_SCHEME_SPECIFIC_PART
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|cfg
operator|.
name|getParameter
argument_list|(
name|EndpointConfiguration
operator|.
name|URI_AUTHORITY
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|cfg
operator|.
name|getParameter
argument_list|(
name|EndpointConfiguration
operator|.
name|URI_USER_INFO
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|cfg
operator|.
name|getParameter
argument_list|(
name|EndpointConfiguration
operator|.
name|URI_HOST
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
operator|-
literal|1
argument_list|)
argument_list|,
name|cfg
operator|.
name|getParameter
argument_list|(
name|EndpointConfiguration
operator|.
name|URI_PORT
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|cfg
operator|.
name|getParameter
argument_list|(
name|EndpointConfiguration
operator|.
name|URI_PATH
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|cfg
operator|.
name|getParameter
argument_list|(
name|EndpointConfiguration
operator|.
name|URI_QUERY
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|cfg
operator|.
name|getParameter
argument_list|(
name|EndpointConfiguration
operator|.
name|URI_FRAGMENT
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUrlWithPath ()
specifier|public
name|void
name|testUrlWithPath
parameter_list|()
throws|throws
name|Exception
block|{
name|EndpointConfiguration
name|cfg
init|=
name|ConfigurationHelper
operator|.
name|createConfiguration
argument_list|(
literal|"uri-dump://foo/bar#defrag"
argument_list|,
name|context
argument_list|)
decl_stmt|;
name|logConfigurationObject
argument_list|(
name|cfg
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|URIDUMP_SCHEME
argument_list|,
name|cfg
operator|.
name|getParameter
argument_list|(
name|EndpointConfiguration
operator|.
name|URI_SCHEME
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"//foo/bar#defrag"
argument_list|,
name|cfg
operator|.
name|getParameter
argument_list|(
name|EndpointConfiguration
operator|.
name|URI_SCHEME_SPECIFIC_PART
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|cfg
operator|.
name|getParameter
argument_list|(
name|EndpointConfiguration
operator|.
name|URI_AUTHORITY
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|cfg
operator|.
name|getParameter
argument_list|(
name|EndpointConfiguration
operator|.
name|URI_USER_INFO
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|cfg
operator|.
name|getParameter
argument_list|(
name|EndpointConfiguration
operator|.
name|URI_HOST
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
operator|-
literal|1
argument_list|)
argument_list|,
name|cfg
operator|.
name|getParameter
argument_list|(
name|EndpointConfiguration
operator|.
name|URI_PORT
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/bar#defrag"
argument_list|,
name|cfg
operator|.
name|getParameter
argument_list|(
name|EndpointConfiguration
operator|.
name|URI_PATH
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|cfg
operator|.
name|getParameter
argument_list|(
name|EndpointConfiguration
operator|.
name|URI_QUERY
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|cfg
operator|.
name|getParameter
argument_list|(
name|EndpointConfiguration
operator|.
name|URI_FRAGMENT
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUrlWithQuery ()
specifier|public
name|void
name|testUrlWithQuery
parameter_list|()
throws|throws
name|Exception
block|{
name|EndpointConfiguration
name|cfg
init|=
name|ConfigurationHelper
operator|.
name|createConfiguration
argument_list|(
literal|"uri-dump://hadrian@localhost:9001/context/path/?bar=true&baz=2#none"
argument_list|,
name|context
argument_list|)
decl_stmt|;
name|logConfigurationObject
argument_list|(
name|cfg
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|URIDUMP_SCHEME
argument_list|,
name|cfg
operator|.
name|getParameter
argument_list|(
name|EndpointConfiguration
operator|.
name|URI_SCHEME
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"//hadrian@localhost:9001/context/path/?bar=true&baz=2#none"
argument_list|,
name|cfg
operator|.
name|getParameter
argument_list|(
name|EndpointConfiguration
operator|.
name|URI_SCHEME_SPECIFIC_PART
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"hadrian@localhost:9001"
argument_list|,
name|cfg
operator|.
name|getParameter
argument_list|(
name|EndpointConfiguration
operator|.
name|URI_AUTHORITY
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"hadrian"
argument_list|,
name|cfg
operator|.
name|getParameter
argument_list|(
name|EndpointConfiguration
operator|.
name|URI_USER_INFO
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"localhost"
argument_list|,
name|cfg
operator|.
name|getParameter
argument_list|(
name|EndpointConfiguration
operator|.
name|URI_HOST
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|9001
argument_list|)
argument_list|,
name|cfg
operator|.
name|getParameter
argument_list|(
name|EndpointConfiguration
operator|.
name|URI_PORT
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/context/path/"
argument_list|,
name|cfg
operator|.
name|getParameter
argument_list|(
name|EndpointConfiguration
operator|.
name|URI_PATH
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bar=true&baz=2#none"
argument_list|,
name|cfg
operator|.
name|getParameter
argument_list|(
name|EndpointConfiguration
operator|.
name|URI_QUERY
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|cfg
operator|.
name|getParameter
argument_list|(
name|EndpointConfiguration
operator|.
name|URI_FRAGMENT
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConfigurationFormat ()
specifier|public
name|void
name|testConfigurationFormat
parameter_list|()
throws|throws
name|Exception
block|{
name|EndpointConfiguration
name|config
init|=
name|ConfigurationHelper
operator|.
name|createConfiguration
argument_list|(
literal|"uri-dump:foo"
argument_list|,
name|context
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|config
operator|.
name|toUriString
argument_list|(
name|EndpointConfiguration
operator|.
name|UriFormat
operator|.
name|Canonical
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|config
operator|.
name|toUriString
argument_list|(
name|EndpointConfiguration
operator|.
name|UriFormat
operator|.
name|Provider
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|config
operator|.
name|toUriString
argument_list|(
name|EndpointConfiguration
operator|.
name|UriFormat
operator|.
name|Consumer
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|config
operator|.
name|toUriString
argument_list|(
name|EndpointConfiguration
operator|.
name|UriFormat
operator|.
name|Complete
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDummyConfiguration ()
specifier|public
name|void
name|testDummyConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|configUri
init|=
literal|"dummy://foobar?first=one&second=2"
decl_stmt|;
name|EndpointConfiguration
name|config
init|=
name|ConfigurationHelper
operator|.
name|createConfiguration
argument_list|(
name|configUri
argument_list|,
name|context
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|config
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|config
operator|instanceof
name|DummyConfiguration
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"one"
argument_list|,
name|config
operator|.
name|getParameter
argument_list|(
literal|"first"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|2
argument_list|)
argument_list|,
name|config
operator|.
name|getParameter
argument_list|(
literal|"second"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|logConfigurationObject (EndpointConfiguration config)
specifier|protected
specifier|static
name|void
name|logConfigurationObject
parameter_list|(
name|EndpointConfiguration
name|config
parameter_list|)
block|{
if|if
condition|(
name|config
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|LOG
operator|.
name|info
argument_list|(
literal|"{} ["
argument_list|,
name|config
operator|.
name|getClass
argument_list|()
operator|.
name|getCanonicalName
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"  uri={}"
argument_list|,
name|config
operator|.
name|getURI
argument_list|()
operator|.
name|toASCIIString
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"  fields:"
argument_list|)
expr_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
init|=
name|config
operator|.
name|getClass
argument_list|()
decl_stmt|;
name|Field
index|[]
name|fields
init|=
name|clazz
operator|.
name|getDeclaredFields
argument_list|()
decl_stmt|;
comment|// Put the Fields in a Map first for a prettier print
name|Map
argument_list|<
name|String
argument_list|,
name|Field
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|URIField
name|anno
init|=
literal|null
decl_stmt|;
for|for
control|(
specifier|final
name|Field
name|field
range|:
name|fields
control|)
block|{
name|anno
operator|=
name|field
operator|.
name|getAnnotation
argument_list|(
name|URIField
operator|.
name|class
argument_list|)
expr_stmt|;
name|String
name|key
init|=
name|anno
operator|==
literal|null
condition|?
name|field
operator|.
name|getName
argument_list|()
else|:
operator|(
name|EndpointConfiguration
operator|.
name|URI_QUERY
operator|.
name|equals
argument_list|(
name|anno
operator|.
name|parameter
argument_list|()
argument_list|)
condition|?
name|anno
operator|.
name|parameter
argument_list|()
else|:
name|anno
operator|.
name|component
argument_list|()
operator|)
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|field
argument_list|)
expr_stmt|;
block|}
comment|// Log standard URI components and remove them from the map
name|logConfigurationField
argument_list|(
name|config
argument_list|,
name|map
argument_list|,
name|EndpointConfiguration
operator|.
name|URI_SCHEME
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|logConfigurationField
argument_list|(
name|config
argument_list|,
name|map
argument_list|,
name|EndpointConfiguration
operator|.
name|URI_SCHEME_SPECIFIC_PART
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|logConfigurationField
argument_list|(
name|config
argument_list|,
name|map
argument_list|,
name|EndpointConfiguration
operator|.
name|URI_AUTHORITY
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|logConfigurationField
argument_list|(
name|config
argument_list|,
name|map
argument_list|,
name|EndpointConfiguration
operator|.
name|URI_USER_INFO
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|logConfigurationField
argument_list|(
name|config
argument_list|,
name|map
argument_list|,
name|EndpointConfiguration
operator|.
name|URI_HOST
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|logConfigurationField
argument_list|(
name|config
argument_list|,
name|map
argument_list|,
name|EndpointConfiguration
operator|.
name|URI_PORT
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|logConfigurationField
argument_list|(
name|config
argument_list|,
name|map
argument_list|,
name|EndpointConfiguration
operator|.
name|URI_PATH
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|logConfigurationField
argument_list|(
name|config
argument_list|,
name|map
argument_list|,
name|EndpointConfiguration
operator|.
name|URI_QUERY
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|logConfigurationField
argument_list|(
name|config
argument_list|,
name|map
argument_list|,
name|EndpointConfiguration
operator|.
name|URI_FRAGMENT
argument_list|,
literal|true
argument_list|)
expr_stmt|;
comment|// Log all other fields
for|for
control|(
name|Field
name|f
range|:
name|map
operator|.
name|values
argument_list|()
control|)
block|{
name|logConfigurationField
argument_list|(
name|config
argument_list|,
name|f
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|info
argument_list|(
literal|"]"
argument_list|)
expr_stmt|;
block|}
DECL|method|logConfigurationField (EndpointConfiguration config, Map<String, Field> fields, String key, boolean remove)
specifier|protected
specifier|static
name|void
name|logConfigurationField
parameter_list|(
name|EndpointConfiguration
name|config
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Field
argument_list|>
name|fields
parameter_list|,
name|String
name|key
parameter_list|,
name|boolean
name|remove
parameter_list|)
block|{
name|logConfigurationField
argument_list|(
name|config
argument_list|,
name|fields
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|remove
condition|)
block|{
name|fields
operator|.
name|remove
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|logConfigurationField (EndpointConfiguration config, Field field)
specifier|protected
specifier|static
name|void
name|logConfigurationField
parameter_list|(
name|EndpointConfiguration
name|config
parameter_list|,
name|Field
name|field
parameter_list|)
block|{
if|if
condition|(
name|field
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|URIField
name|anno
init|=
name|field
operator|.
name|getAnnotation
argument_list|(
name|URIField
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|anno
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"  @URIField(component = \"{}\", parameter = \"{}\")"
argument_list|,
name|anno
operator|.
name|component
argument_list|()
argument_list|,
name|anno
operator|.
name|parameter
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|info
argument_list|(
literal|"  {} {}={}"
argument_list|,
name|field
operator|.
name|getType
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|field
operator|.
name|getName
argument_list|()
argument_list|,
name|config
operator|.
name|getParameter
argument_list|(
name|field
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|class|ConfiguredComponent
specifier|private
specifier|static
class|class
name|ConfiguredComponent
implements|implements
name|Component
block|{
DECL|field|context
specifier|private
name|CamelContext
name|context
decl_stmt|;
annotation|@
name|Override
DECL|method|setCamelContext (CamelContext camelContext)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|context
operator|=
name|camelContext
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|context
return|;
block|}
annotation|@
name|Override
DECL|method|createEndpoint (String uri)
specifier|public
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|)
throws|throws
name|Exception
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|createComponentConfiguration ()
specifier|public
name|ComponentConfiguration
name|createComponentConfiguration
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|createConfiguration (String uri)
specifier|public
name|EndpointConfiguration
name|createConfiguration
parameter_list|(
name|String
name|uri
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|uri
operator|.
name|equals
argument_list|(
name|URIDUMP_SCHEME
argument_list|)
condition|)
block|{
return|return
operator|new
name|UriDumpConfiguration
argument_list|(
name|getCamelContext
argument_list|()
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|uri
operator|.
name|equals
argument_list|(
name|DUMMY_SCHEME
argument_list|)
condition|)
block|{
return|return
operator|new
name|DummyConfiguration
argument_list|(
name|getCamelContext
argument_list|()
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|useRawUri ()
specifier|public
name|boolean
name|useRawUri
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
block|}
DECL|class|UriDumpConfiguration
specifier|public
specifier|static
class|class
name|UriDumpConfiguration
extends|extends
name|DefaultEndpointConfiguration
block|{
DECL|field|scheme
specifier|private
name|String
name|scheme
decl_stmt|;
DECL|field|schemeSpecificPart
specifier|private
name|String
name|schemeSpecificPart
decl_stmt|;
DECL|field|authority
specifier|private
name|String
name|authority
decl_stmt|;
DECL|field|userInfo
specifier|private
name|String
name|userInfo
decl_stmt|;
DECL|field|host
specifier|private
name|String
name|host
decl_stmt|;
DECL|field|port
specifier|private
name|int
name|port
decl_stmt|;
DECL|field|path
specifier|private
name|String
name|path
decl_stmt|;
DECL|field|query
specifier|private
name|String
name|query
decl_stmt|;
DECL|field|fragment
specifier|private
name|String
name|fragment
decl_stmt|;
DECL|method|UriDumpConfiguration (CamelContext camelContext)
specifier|public
name|UriDumpConfiguration
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|super
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
block|}
DECL|method|setScheme (String scheme)
specifier|public
name|void
name|setScheme
parameter_list|(
name|String
name|scheme
parameter_list|)
block|{
name|this
operator|.
name|scheme
operator|=
name|scheme
expr_stmt|;
block|}
DECL|method|getScheme ()
specifier|public
name|String
name|getScheme
parameter_list|()
block|{
return|return
name|scheme
return|;
block|}
DECL|method|setSchemeSpecificPart (String schemeSpecificPart)
specifier|public
name|void
name|setSchemeSpecificPart
parameter_list|(
name|String
name|schemeSpecificPart
parameter_list|)
block|{
name|this
operator|.
name|schemeSpecificPart
operator|=
name|schemeSpecificPart
expr_stmt|;
block|}
DECL|method|getSchemeSpecificPart ()
specifier|public
name|String
name|getSchemeSpecificPart
parameter_list|()
block|{
return|return
name|schemeSpecificPart
return|;
block|}
DECL|method|setAuthority (String authority)
specifier|public
name|void
name|setAuthority
parameter_list|(
name|String
name|authority
parameter_list|)
block|{
name|this
operator|.
name|authority
operator|=
name|authority
expr_stmt|;
block|}
DECL|method|getAuthority ()
specifier|public
name|String
name|getAuthority
parameter_list|()
block|{
return|return
name|authority
return|;
block|}
DECL|method|setUserInfo (String userInfo)
specifier|public
name|void
name|setUserInfo
parameter_list|(
name|String
name|userInfo
parameter_list|)
block|{
name|this
operator|.
name|userInfo
operator|=
name|userInfo
expr_stmt|;
block|}
DECL|method|getUserInfo ()
specifier|public
name|String
name|getUserInfo
parameter_list|()
block|{
return|return
name|userInfo
return|;
block|}
DECL|method|setHost (String host)
specifier|public
name|void
name|setHost
parameter_list|(
name|String
name|host
parameter_list|)
block|{
name|this
operator|.
name|host
operator|=
name|host
expr_stmt|;
block|}
DECL|method|getHost ()
specifier|public
name|String
name|getHost
parameter_list|()
block|{
return|return
name|host
return|;
block|}
DECL|method|setPort (int port)
specifier|public
name|void
name|setPort
parameter_list|(
name|int
name|port
parameter_list|)
block|{
name|this
operator|.
name|port
operator|=
name|port
expr_stmt|;
block|}
DECL|method|getPort ()
specifier|public
name|int
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
DECL|method|setPath (String path)
specifier|public
name|void
name|setPath
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|this
operator|.
name|path
operator|=
name|path
expr_stmt|;
block|}
DECL|method|getPath ()
specifier|public
name|String
name|getPath
parameter_list|()
block|{
return|return
name|path
return|;
block|}
DECL|method|setQuery (String query)
specifier|public
name|void
name|setQuery
parameter_list|(
name|String
name|query
parameter_list|)
block|{
name|this
operator|.
name|query
operator|=
name|query
expr_stmt|;
block|}
DECL|method|getQuery ()
specifier|public
name|String
name|getQuery
parameter_list|()
block|{
return|return
name|query
return|;
block|}
DECL|method|setFragment (String fragment)
specifier|public
name|void
name|setFragment
parameter_list|(
name|String
name|fragment
parameter_list|)
block|{
name|this
operator|.
name|fragment
operator|=
name|fragment
expr_stmt|;
block|}
DECL|method|getFragment ()
specifier|public
name|String
name|getFragment
parameter_list|()
block|{
return|return
name|fragment
return|;
block|}
DECL|method|toUriString (UriFormat format)
specifier|public
name|String
name|toUriString
parameter_list|(
name|UriFormat
name|format
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
DECL|class|DummyConfiguration
specifier|public
specifier|static
class|class
name|DummyConfiguration
extends|extends
name|DefaultEndpointConfiguration
block|{
DECL|field|path
specifier|private
name|String
name|path
decl_stmt|;
annotation|@
name|URIField
argument_list|(
name|component
operator|=
literal|"query"
argument_list|,
name|parameter
operator|=
literal|"first"
argument_list|)
DECL|field|first
specifier|private
name|String
name|first
decl_stmt|;
annotation|@
name|URIField
argument_list|(
name|component
operator|=
literal|"query"
argument_list|,
name|parameter
operator|=
literal|"second"
argument_list|)
DECL|field|second
specifier|private
name|int
name|second
decl_stmt|;
DECL|method|DummyConfiguration (CamelContext camelContext)
name|DummyConfiguration
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|super
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
block|}
DECL|method|getPath ()
specifier|public
name|String
name|getPath
parameter_list|()
block|{
return|return
name|path
return|;
block|}
DECL|method|setPath (String path)
specifier|public
name|void
name|setPath
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|this
operator|.
name|path
operator|=
name|path
expr_stmt|;
block|}
DECL|method|getFirst ()
specifier|public
name|String
name|getFirst
parameter_list|()
block|{
return|return
name|first
return|;
block|}
DECL|method|setFirst (String first)
specifier|public
name|void
name|setFirst
parameter_list|(
name|String
name|first
parameter_list|)
block|{
name|this
operator|.
name|first
operator|=
name|first
expr_stmt|;
block|}
DECL|method|getSecond ()
specifier|public
name|int
name|getSecond
parameter_list|()
block|{
return|return
name|second
return|;
block|}
DECL|method|setSecond (int second)
specifier|public
name|void
name|setSecond
parameter_list|(
name|int
name|second
parameter_list|)
block|{
name|this
operator|.
name|second
operator|=
name|second
expr_stmt|;
block|}
DECL|method|toUriString (UriFormat format)
specifier|public
name|String
name|toUriString
parameter_list|(
name|UriFormat
name|format
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

