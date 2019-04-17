begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder.xml
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|xml
package|;
end_package

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
name|support
operator|.
name|builder
operator|.
name|xml
operator|.
name|Namespaces
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
DECL|class|DefaultNamespaceContextTest
specifier|public
class|class
name|DefaultNamespaceContextTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testDefaultNamespaceContextEmpty ()
specifier|public
name|void
name|testDefaultNamespaceContextEmpty
parameter_list|()
throws|throws
name|Exception
block|{
name|XPathBuilder
name|builder
init|=
name|XPathBuilder
operator|.
name|xpath
argument_list|(
literal|"/foo"
argument_list|)
decl_stmt|;
name|builder
operator|.
name|start
argument_list|()
expr_stmt|;
name|DefaultNamespaceContext
name|context
init|=
name|builder
operator|.
name|getNamespaceContext
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|String
name|uri
init|=
name|context
operator|.
name|getNamespaceURI
argument_list|(
literal|"foo"
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|uri
argument_list|)
expr_stmt|;
name|String
name|prefix
init|=
name|context
operator|.
name|getPrefix
argument_list|(
literal|"foo"
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|prefix
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|String
argument_list|>
name|it
init|=
name|context
operator|.
name|getPrefixes
argument_list|(
literal|"foo"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|it
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDefaultNamespaceContextPre ()
specifier|public
name|void
name|testDefaultNamespaceContextPre
parameter_list|()
throws|throws
name|Exception
block|{
name|XPathBuilder
name|builder
init|=
name|XPathBuilder
operator|.
name|xpath
argument_list|(
literal|"/foo"
argument_list|)
operator|.
name|namespace
argument_list|(
literal|"pre"
argument_list|,
literal|"http://acme/cheese"
argument_list|)
decl_stmt|;
name|builder
operator|.
name|start
argument_list|()
expr_stmt|;
name|DefaultNamespaceContext
name|context
init|=
name|builder
operator|.
name|getNamespaceContext
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|String
name|uri
init|=
name|context
operator|.
name|getNamespaceURI
argument_list|(
literal|"pre"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"http://acme/cheese"
argument_list|,
name|uri
argument_list|)
expr_stmt|;
name|String
name|prefix
init|=
name|context
operator|.
name|getPrefix
argument_list|(
literal|"http://acme/cheese"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"pre"
argument_list|,
name|prefix
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|String
argument_list|>
name|it
init|=
name|context
operator|.
name|getPrefixes
argument_list|(
literal|"http://acme/cheese"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|it
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"pre"
argument_list|,
name|it
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDefaultNamespaceContextDualNamespaces ()
specifier|public
name|void
name|testDefaultNamespaceContextDualNamespaces
parameter_list|()
throws|throws
name|Exception
block|{
name|XPathBuilder
name|builder
init|=
name|XPathBuilder
operator|.
name|xpath
argument_list|(
literal|"/foo"
argument_list|)
operator|.
name|namespace
argument_list|(
literal|"pre"
argument_list|,
literal|"http://acme/cheese"
argument_list|)
operator|.
name|namespace
argument_list|(
literal|"bar"
argument_list|,
literal|"http://acme/bar"
argument_list|)
decl_stmt|;
name|builder
operator|.
name|start
argument_list|()
expr_stmt|;
name|DefaultNamespaceContext
name|context
init|=
name|builder
operator|.
name|getNamespaceContext
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|String
name|uri
init|=
name|context
operator|.
name|getNamespaceURI
argument_list|(
literal|"pre"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"http://acme/cheese"
argument_list|,
name|uri
argument_list|)
expr_stmt|;
name|String
name|uri2
init|=
name|context
operator|.
name|getNamespaceURI
argument_list|(
literal|"bar"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"http://acme/bar"
argument_list|,
name|uri2
argument_list|)
expr_stmt|;
name|String
name|prefix
init|=
name|context
operator|.
name|getPrefix
argument_list|(
literal|"http://acme/cheese"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"pre"
argument_list|,
name|prefix
argument_list|)
expr_stmt|;
name|String
name|prefix2
init|=
name|context
operator|.
name|getPrefix
argument_list|(
literal|"http://acme/bar"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|prefix2
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|String
argument_list|>
name|it
init|=
name|context
operator|.
name|getPrefixes
argument_list|(
literal|"http://acme/cheese"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|it
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"pre"
argument_list|,
name|it
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|String
argument_list|>
name|it2
init|=
name|context
operator|.
name|getPrefixes
argument_list|(
literal|"http://acme/bar"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|it2
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|it2
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDefaultNamespaceContextParent ()
specifier|public
name|void
name|testDefaultNamespaceContextParent
parameter_list|()
throws|throws
name|Exception
block|{
name|XPathBuilder
name|builder
init|=
name|XPathBuilder
operator|.
name|xpath
argument_list|(
literal|"/foo"
argument_list|)
decl_stmt|;
name|builder
operator|.
name|start
argument_list|()
expr_stmt|;
name|DefaultNamespaceContext
name|context
init|=
name|builder
operator|.
name|getNamespaceContext
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|String
name|uri
init|=
name|context
operator|.
name|getNamespaceURI
argument_list|(
literal|"in"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Namespaces
operator|.
name|IN_NAMESPACE
argument_list|,
name|uri
argument_list|)
expr_stmt|;
name|String
name|prefix
init|=
name|context
operator|.
name|getPrefix
argument_list|(
name|Namespaces
operator|.
name|IN_NAMESPACE
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"in"
argument_list|,
name|prefix
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|String
argument_list|>
name|it
init|=
name|context
operator|.
name|getPrefixes
argument_list|(
name|Namespaces
operator|.
name|IN_NAMESPACE
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|it
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"in"
argument_list|,
name|it
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|uri2
init|=
name|context
operator|.
name|getNamespaceURI
argument_list|(
literal|"out"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Namespaces
operator|.
name|OUT_NAMESPACE
argument_list|,
name|uri2
argument_list|)
expr_stmt|;
name|String
name|uri3
init|=
name|context
operator|.
name|getNamespaceURI
argument_list|(
literal|"env"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Namespaces
operator|.
name|ENVIRONMENT_VARIABLES
argument_list|,
name|uri3
argument_list|)
expr_stmt|;
name|String
name|uri4
init|=
name|context
operator|.
name|getNamespaceURI
argument_list|(
literal|"system"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Namespaces
operator|.
name|SYSTEM_PROPERTIES_NAMESPACE
argument_list|,
name|uri4
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDefaultNamespaceContextCtr ()
specifier|public
name|void
name|testDefaultNamespaceContextCtr
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultNamespaceContext
name|context
init|=
operator|new
name|DefaultNamespaceContext
argument_list|()
decl_stmt|;
comment|// should not have any namespaces
name|String
name|uri
init|=
name|context
operator|.
name|getNamespaceURI
argument_list|(
literal|"in"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|uri
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDefaultNamespaceContextAnotherCtr ()
specifier|public
name|void
name|testDefaultNamespaceContextAnotherCtr
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|"http://acme/cheese"
argument_list|)
expr_stmt|;
name|DefaultNamespaceContext
name|context
init|=
operator|new
name|DefaultNamespaceContext
argument_list|(
literal|null
argument_list|,
name|map
argument_list|)
decl_stmt|;
comment|// should not have any default namespaces
name|String
name|uri
init|=
name|context
operator|.
name|getNamespaceURI
argument_list|(
literal|"in"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|uri
argument_list|)
expr_stmt|;
name|String
name|uri2
init|=
name|context
operator|.
name|getNamespaceURI
argument_list|(
literal|"foo"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"http://acme/cheese"
argument_list|,
name|uri2
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

