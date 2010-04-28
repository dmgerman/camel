begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.osgi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|osgi
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Converter
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
name|RoutesBuilder
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
name|osgi
operator|.
name|test
operator|.
name|MyRouteBuilder
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
name|osgi
operator|.
name|test
operator|.
name|MyTypeConverter
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
name|osgi
operator|.
name|framework
operator|.
name|BundleContext
import|;
end_import

begin_class
DECL|class|OsgiPackageScanClassResolverTest
specifier|public
class|class
name|OsgiPackageScanClassResolverTest
extends|extends
name|CamelOsgiTestSupport
block|{
annotation|@
name|Test
DECL|method|testOsgiResolverFindAnnotatedTest ()
specifier|public
name|void
name|testOsgiResolverFindAnnotatedTest
parameter_list|()
throws|throws
name|IOException
block|{
name|BundleContext
name|context
init|=
name|Activator
operator|.
name|getBundle
argument_list|()
operator|.
name|getBundleContext
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"The BundleContext should not be null"
argument_list|,
name|context
argument_list|)
expr_stmt|;
name|OsgiPackageScanClassResolver
name|resolver
init|=
operator|new
name|OsgiPackageScanClassResolver
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|String
index|[]
name|packageNames
init|=
block|{
literal|"org.apache.camel.osgi.test"
block|}
decl_stmt|;
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|classes
init|=
name|resolver
operator|.
name|findAnnotated
argument_list|(
name|Converter
operator|.
name|class
argument_list|,
name|packageNames
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"There should find a class"
argument_list|,
name|classes
operator|.
name|size
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Find a wrong class"
argument_list|,
name|classes
operator|.
name|contains
argument_list|(
name|MyTypeConverter
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testOsgiResolverFindImplementationTest ()
specifier|public
name|void
name|testOsgiResolverFindImplementationTest
parameter_list|()
block|{
name|BundleContext
name|context
init|=
name|Activator
operator|.
name|getBundle
argument_list|()
operator|.
name|getBundleContext
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"The BundleContext should not be null"
argument_list|,
name|context
argument_list|)
expr_stmt|;
name|OsgiPackageScanClassResolver
name|resolver
init|=
operator|new
name|OsgiPackageScanClassResolver
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|String
index|[]
name|packageNames
init|=
block|{
literal|"org.apache.camel.osgi.test"
block|}
decl_stmt|;
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|classes
init|=
name|resolver
operator|.
name|findImplementations
argument_list|(
name|RoutesBuilder
operator|.
name|class
argument_list|,
name|packageNames
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"There should find a class"
argument_list|,
name|classes
operator|.
name|size
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Find a wrong class"
argument_list|,
name|classes
operator|.
name|contains
argument_list|(
name|MyRouteBuilder
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

