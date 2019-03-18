begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.cluster
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|cluster
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|Optional
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
name|cluster
operator|.
name|CamelClusterMember
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
name|cluster
operator|.
name|CamelClusterService
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
name|cluster
operator|.
name|CamelClusterView
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
name|file
operator|.
name|cluster
operator|.
name|FileLockClusterService
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
name|support
operator|.
name|cluster
operator|.
name|AbstractCamelClusterService
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
name|support
operator|.
name|cluster
operator|.
name|AbstractCamelClusterView
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
name|support
operator|.
name|cluster
operator|.
name|ClusterServiceSelectors
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
name|support
operator|.
name|cluster
operator|.
name|ClusterServiceHelper
operator|.
name|lookupService
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
name|support
operator|.
name|cluster
operator|.
name|ClusterServiceHelper
operator|.
name|mandatoryLookupService
import|;
end_import

begin_class
DECL|class|ClusterServiceSelectorTest
specifier|public
class|class
name|ClusterServiceSelectorTest
block|{
annotation|@
name|Test
DECL|method|testDefaultSelector ()
specifier|public
name|void
name|testDefaultSelector
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
literal|null
decl_stmt|;
try|try
block|{
name|DummyClusterService1
name|service1
init|=
operator|new
name|DummyClusterService1
argument_list|()
decl_stmt|;
name|context
operator|=
operator|new
name|DefaultCamelContext
argument_list|()
expr_stmt|;
name|context
operator|.
name|addService
argument_list|(
name|service1
argument_list|)
expr_stmt|;
name|Optional
argument_list|<
name|CamelClusterService
argument_list|>
name|lookup
init|=
name|lookupService
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|lookup
operator|.
name|isPresent
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|service1
argument_list|,
name|lookup
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Test
DECL|method|testDefaultSelectorFailure ()
specifier|public
name|void
name|testDefaultSelectorFailure
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
literal|null
decl_stmt|;
try|try
block|{
name|context
operator|=
operator|new
name|DefaultCamelContext
argument_list|()
expr_stmt|;
name|context
operator|.
name|addService
argument_list|(
operator|new
name|DummyClusterService1
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|addService
argument_list|(
operator|new
name|DummyClusterService2
argument_list|()
argument_list|)
expr_stmt|;
name|Optional
argument_list|<
name|CamelClusterService
argument_list|>
name|lookup
init|=
name|lookupService
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|lookup
operator|.
name|isPresent
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Test
DECL|method|testSelectSingle ()
specifier|public
name|void
name|testSelectSingle
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
literal|null
decl_stmt|;
try|try
block|{
name|DummyClusterService1
name|service1
init|=
operator|new
name|DummyClusterService1
argument_list|()
decl_stmt|;
name|context
operator|=
operator|new
name|DefaultCamelContext
argument_list|()
expr_stmt|;
name|context
operator|.
name|addService
argument_list|(
name|service1
argument_list|)
expr_stmt|;
name|CamelClusterService
operator|.
name|Selector
name|selector
init|=
name|ClusterServiceSelectors
operator|.
name|single
argument_list|()
decl_stmt|;
name|Optional
argument_list|<
name|CamelClusterService
argument_list|>
name|lookup
init|=
name|lookupService
argument_list|(
name|context
argument_list|,
name|selector
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|lookup
operator|.
name|isPresent
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|service1
argument_list|,
name|lookup
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Test
DECL|method|testSelectSingleFailure ()
specifier|public
name|void
name|testSelectSingleFailure
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
literal|null
decl_stmt|;
try|try
block|{
name|context
operator|=
operator|new
name|DefaultCamelContext
argument_list|()
expr_stmt|;
name|context
operator|.
name|addService
argument_list|(
operator|new
name|DummyClusterService1
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|addService
argument_list|(
operator|new
name|DummyClusterService2
argument_list|()
argument_list|)
expr_stmt|;
name|CamelClusterService
operator|.
name|Selector
name|selector
init|=
name|ClusterServiceSelectors
operator|.
name|single
argument_list|()
decl_stmt|;
name|Optional
argument_list|<
name|CamelClusterService
argument_list|>
name|lookup
init|=
name|lookupService
argument_list|(
name|context
argument_list|,
name|selector
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|lookup
operator|.
name|isPresent
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Test
DECL|method|testSelectFirst ()
specifier|public
name|void
name|testSelectFirst
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
literal|null
decl_stmt|;
try|try
block|{
name|context
operator|=
operator|new
name|DefaultCamelContext
argument_list|()
expr_stmt|;
name|context
operator|.
name|addService
argument_list|(
operator|new
name|DummyClusterService1
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|addService
argument_list|(
operator|new
name|DummyClusterService2
argument_list|()
argument_list|)
expr_stmt|;
name|CamelClusterService
operator|.
name|Selector
name|selector
init|=
name|ClusterServiceSelectors
operator|.
name|first
argument_list|()
decl_stmt|;
name|Optional
argument_list|<
name|CamelClusterService
argument_list|>
name|lookup
init|=
name|lookupService
argument_list|(
name|context
argument_list|,
name|selector
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|lookup
operator|.
name|isPresent
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Test
DECL|method|testSelectByType ()
specifier|public
name|void
name|testSelectByType
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
literal|null
decl_stmt|;
try|try
block|{
name|context
operator|=
operator|new
name|DefaultCamelContext
argument_list|()
expr_stmt|;
name|context
operator|.
name|addService
argument_list|(
operator|new
name|DummyClusterService1
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|addService
argument_list|(
operator|new
name|DummyClusterService2
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|lookupService
argument_list|(
name|context
argument_list|,
name|ClusterServiceSelectors
operator|.
name|type
argument_list|(
name|DummyClusterService1
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|isPresent
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|lookupService
argument_list|(
name|context
argument_list|,
name|ClusterServiceSelectors
operator|.
name|type
argument_list|(
name|DummyClusterService2
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|isPresent
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|lookupService
argument_list|(
name|context
argument_list|,
name|ClusterServiceSelectors
operator|.
name|type
argument_list|(
name|FileLockClusterService
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|isPresent
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Test
DECL|method|testSelectByAttribute ()
specifier|public
name|void
name|testSelectByAttribute
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
literal|null
decl_stmt|;
try|try
block|{
name|DummyClusterService1
name|service1
init|=
operator|new
name|DummyClusterService1
argument_list|()
decl_stmt|;
name|service1
operator|.
name|setAttribute
argument_list|(
literal|"service.type"
argument_list|,
literal|"zookeeper"
argument_list|)
expr_stmt|;
name|DummyClusterService2
name|service2
init|=
operator|new
name|DummyClusterService2
argument_list|()
decl_stmt|;
name|service2
operator|.
name|setAttribute
argument_list|(
literal|"service.type"
argument_list|,
literal|"file"
argument_list|)
expr_stmt|;
name|context
operator|=
operator|new
name|DefaultCamelContext
argument_list|()
expr_stmt|;
name|context
operator|.
name|addService
argument_list|(
name|service1
argument_list|)
expr_stmt|;
name|context
operator|.
name|addService
argument_list|(
name|service2
argument_list|)
expr_stmt|;
name|Optional
argument_list|<
name|CamelClusterService
argument_list|>
name|lookup
decl_stmt|;
name|lookup
operator|=
name|lookupService
argument_list|(
name|context
argument_list|,
name|ClusterServiceSelectors
operator|.
name|attribute
argument_list|(
literal|"service.type"
argument_list|,
literal|"zookeeper"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|lookup
operator|.
name|isPresent
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|service1
argument_list|,
name|lookup
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|lookup
operator|=
name|lookupService
argument_list|(
name|context
argument_list|,
name|ClusterServiceSelectors
operator|.
name|attribute
argument_list|(
literal|"service.type"
argument_list|,
literal|"file"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|lookup
operator|.
name|isPresent
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|service2
argument_list|,
name|lookup
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|lookup
operator|=
name|lookupService
argument_list|(
name|context
argument_list|,
name|ClusterServiceSelectors
operator|.
name|attribute
argument_list|(
literal|"service.type"
argument_list|,
literal|"consul"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|lookup
operator|.
name|isPresent
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Test
DECL|method|testSelectByOrder ()
specifier|public
name|void
name|testSelectByOrder
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
literal|null
decl_stmt|;
try|try
block|{
name|DummyClusterService1
name|service1
init|=
operator|new
name|DummyClusterService1
argument_list|()
decl_stmt|;
name|service1
operator|.
name|setOrder
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|DummyClusterService2
name|service2
init|=
operator|new
name|DummyClusterService2
argument_list|()
decl_stmt|;
name|service2
operator|.
name|setOrder
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|context
operator|=
operator|new
name|DefaultCamelContext
argument_list|()
expr_stmt|;
name|context
operator|.
name|addService
argument_list|(
name|service1
argument_list|)
expr_stmt|;
name|context
operator|.
name|addService
argument_list|(
name|service2
argument_list|)
expr_stmt|;
name|CamelClusterService
operator|.
name|Selector
name|selector
init|=
name|ClusterServiceSelectors
operator|.
name|order
argument_list|()
decl_stmt|;
name|Optional
argument_list|<
name|CamelClusterService
argument_list|>
name|lookup
init|=
name|lookupService
argument_list|(
name|context
argument_list|,
name|selector
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|lookup
operator|.
name|isPresent
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|service2
argument_list|,
name|lookup
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Test
DECL|method|testSelectByOrderFailure ()
specifier|public
name|void
name|testSelectByOrderFailure
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
literal|null
decl_stmt|;
try|try
block|{
name|DummyClusterService1
name|service1
init|=
operator|new
name|DummyClusterService1
argument_list|()
decl_stmt|;
name|service1
operator|.
name|setOrder
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|DummyClusterService2
name|service2
init|=
operator|new
name|DummyClusterService2
argument_list|()
decl_stmt|;
name|service2
operator|.
name|setOrder
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|DummyClusterService2
name|service3
init|=
operator|new
name|DummyClusterService2
argument_list|()
decl_stmt|;
name|service3
operator|.
name|setOrder
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|context
operator|=
operator|new
name|DefaultCamelContext
argument_list|()
expr_stmt|;
name|context
operator|.
name|addService
argument_list|(
name|service1
argument_list|)
expr_stmt|;
name|context
operator|.
name|addService
argument_list|(
name|service2
argument_list|)
expr_stmt|;
name|context
operator|.
name|addService
argument_list|(
name|service3
argument_list|)
expr_stmt|;
name|CamelClusterService
operator|.
name|Selector
name|selector
init|=
name|ClusterServiceSelectors
operator|.
name|order
argument_list|()
decl_stmt|;
name|Optional
argument_list|<
name|CamelClusterService
argument_list|>
name|lookup
init|=
name|lookupService
argument_list|(
name|context
argument_list|,
name|selector
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|lookup
operator|.
name|isPresent
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Test
DECL|method|testMandatoryLookup ()
specifier|public
name|void
name|testMandatoryLookup
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
literal|null
decl_stmt|;
try|try
block|{
name|DummyClusterService1
name|service1
init|=
operator|new
name|DummyClusterService1
argument_list|()
decl_stmt|;
name|context
operator|=
operator|new
name|DefaultCamelContext
argument_list|()
expr_stmt|;
name|context
operator|.
name|addService
argument_list|(
name|service1
argument_list|)
expr_stmt|;
name|CamelClusterService
operator|.
name|Selector
name|selector
init|=
name|ClusterServiceSelectors
operator|.
name|single
argument_list|()
decl_stmt|;
name|CamelClusterService
name|lookup
init|=
name|mandatoryLookupService
argument_list|(
name|context
argument_list|,
name|selector
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|lookup
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|service1
argument_list|,
name|lookup
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Test
DECL|method|testMandatoryLookupWithoutSelector ()
specifier|public
name|void
name|testMandatoryLookupWithoutSelector
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
literal|null
decl_stmt|;
try|try
block|{
name|DummyClusterService1
name|service1
init|=
operator|new
name|DummyClusterService1
argument_list|()
decl_stmt|;
name|context
operator|=
operator|new
name|DefaultCamelContext
argument_list|()
expr_stmt|;
name|context
operator|.
name|addService
argument_list|(
name|service1
argument_list|)
expr_stmt|;
name|CamelClusterService
name|lookup
init|=
name|mandatoryLookupService
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|lookup
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|service1
argument_list|,
name|lookup
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalStateException
operator|.
name|class
argument_list|)
DECL|method|testMandatoryLookupFailure ()
specifier|public
name|void
name|testMandatoryLookupFailure
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
literal|null
decl_stmt|;
try|try
block|{
name|context
operator|=
operator|new
name|DefaultCamelContext
argument_list|()
expr_stmt|;
name|mandatoryLookupService
argument_list|(
name|context
argument_list|,
name|ClusterServiceSelectors
operator|.
name|single
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalStateException
operator|.
name|class
argument_list|)
DECL|method|testMandatoryLookupFailureWithoutSelector ()
specifier|public
name|void
name|testMandatoryLookupFailureWithoutSelector
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
literal|null
decl_stmt|;
try|try
block|{
name|context
operator|=
operator|new
name|DefaultCamelContext
argument_list|()
expr_stmt|;
name|mandatoryLookupService
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|// **************************************
comment|// Helpers
comment|// **************************************
DECL|class|DummyClusterService1
specifier|private
specifier|final
class|class
name|DummyClusterService1
extends|extends
name|AbstractCamelClusterService
block|{
DECL|method|DummyClusterService1 ()
specifier|public
name|DummyClusterService1
parameter_list|()
block|{         }
annotation|@
name|Override
DECL|method|createView (String namespace)
specifier|protected
name|CamelClusterView
name|createView
parameter_list|(
name|String
name|namespace
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|DummyClusterServiceView
argument_list|(
name|this
argument_list|,
name|namespace
argument_list|)
return|;
block|}
block|}
DECL|class|DummyClusterService2
specifier|private
specifier|final
class|class
name|DummyClusterService2
extends|extends
name|AbstractCamelClusterService
block|{
DECL|method|DummyClusterService2 ()
specifier|public
name|DummyClusterService2
parameter_list|()
block|{         }
annotation|@
name|Override
DECL|method|createView (String namespace)
specifier|protected
name|CamelClusterView
name|createView
parameter_list|(
name|String
name|namespace
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|DummyClusterServiceView
argument_list|(
name|this
argument_list|,
name|namespace
argument_list|)
return|;
block|}
block|}
DECL|class|DummyClusterServiceView
specifier|private
specifier|final
class|class
name|DummyClusterServiceView
extends|extends
name|AbstractCamelClusterView
block|{
DECL|method|DummyClusterServiceView (CamelClusterService cluster, String namespace)
specifier|public
name|DummyClusterServiceView
parameter_list|(
name|CamelClusterService
name|cluster
parameter_list|,
name|String
name|namespace
parameter_list|)
block|{
name|super
argument_list|(
name|cluster
argument_list|,
name|namespace
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getLeader ()
specifier|public
name|Optional
argument_list|<
name|CamelClusterMember
argument_list|>
name|getLeader
parameter_list|()
block|{
return|return
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getLocalMember ()
specifier|public
name|CamelClusterMember
name|getLocalMember
parameter_list|()
block|{
return|return
operator|new
name|DummyClusterServiceMember
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getMembers ()
specifier|public
name|List
argument_list|<
name|CamelClusterMember
argument_list|>
name|getMembers
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{         }
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{         }
DECL|class|DummyClusterServiceMember
specifier|private
specifier|final
class|class
name|DummyClusterServiceMember
implements|implements
name|CamelClusterMember
block|{
DECL|field|leader
specifier|private
specifier|final
name|boolean
name|leader
decl_stmt|;
DECL|field|local
specifier|private
specifier|final
name|boolean
name|local
decl_stmt|;
DECL|method|DummyClusterServiceMember (boolean leader, boolean local)
specifier|public
name|DummyClusterServiceMember
parameter_list|(
name|boolean
name|leader
parameter_list|,
name|boolean
name|local
parameter_list|)
block|{
name|this
operator|.
name|leader
operator|=
name|leader
expr_stmt|;
name|this
operator|.
name|local
operator|=
name|local
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isLeader ()
specifier|public
name|boolean
name|isLeader
parameter_list|()
block|{
return|return
name|leader
return|;
block|}
annotation|@
name|Override
DECL|method|isLocal ()
specifier|public
name|boolean
name|isLocal
parameter_list|()
block|{
return|return
name|local
return|;
block|}
annotation|@
name|Override
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|getClusterService
argument_list|()
operator|.
name|getId
argument_list|()
return|;
block|}
block|}
block|}
block|}
end_class

end_unit

