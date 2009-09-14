begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.scan
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|scan
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Annotation
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLClassLoader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
name|impl
operator|.
name|DefaultPackageScanClassResolver
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
name|scan
operator|.
name|test
operator|.
name|ScannableOne
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
name|scan
operator|.
name|test
operator|.
name|ScannableTwo
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
name|scan
operator|.
name|test
operator|.
name|a
operator|.
name|ScanTargetOne
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
name|scan
operator|.
name|test
operator|.
name|b
operator|.
name|ScanTargetTwo
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
name|scan
operator|.
name|test
operator|.
name|c
operator|.
name|ScanTargetThree
import|;
end_import

begin_class
DECL|class|DefaultPackageScanClassResolverTest
specifier|public
class|class
name|DefaultPackageScanClassResolverTest
extends|extends
name|ScanTestSupport
block|{
DECL|field|resolver
specifier|private
name|DefaultPackageScanClassResolver
name|resolver
decl_stmt|;
DECL|field|annotations
specifier|private
name|Set
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
argument_list|>
name|annotations
init|=
operator|new
name|HashSet
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|scanPackage
specifier|private
name|String
name|scanPackage
init|=
literal|"org.apache.camel.impl.scan.test"
decl_stmt|;
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
name|resolver
operator|=
operator|new
name|DefaultPackageScanClassResolver
argument_list|()
expr_stmt|;
name|annotations
operator|.
name|add
argument_list|(
name|ScannableOne
operator|.
name|class
argument_list|)
expr_stmt|;
name|annotations
operator|.
name|add
argument_list|(
name|ScannableTwo
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|testFindByAnnotationWithoutExtraFilters ()
specifier|public
name|void
name|testFindByAnnotationWithoutExtraFilters
parameter_list|()
block|{
name|Set
argument_list|<
name|Class
argument_list|>
name|scanned
init|=
name|resolver
operator|.
name|findAnnotated
argument_list|(
name|ScannableOne
operator|.
name|class
argument_list|,
name|scanPackage
argument_list|)
decl_stmt|;
name|validateMatchingSetContains
argument_list|(
name|scanned
argument_list|,
name|ScanTargetOne
operator|.
name|class
argument_list|,
name|ScanTargetTwo
operator|.
name|class
argument_list|)
expr_stmt|;
name|scanned
operator|=
name|resolver
operator|.
name|findAnnotated
argument_list|(
name|ScannableTwo
operator|.
name|class
argument_list|,
name|scanPackage
argument_list|)
expr_stmt|;
name|validateMatchingSetContains
argument_list|(
name|scanned
argument_list|,
name|ScanTargetThree
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|testFindByAnnotationsWithoutExtraFilters ()
specifier|public
name|void
name|testFindByAnnotationsWithoutExtraFilters
parameter_list|()
block|{
name|Set
argument_list|<
name|Class
argument_list|>
name|scanned
init|=
name|resolver
operator|.
name|findAnnotated
argument_list|(
name|annotations
argument_list|,
name|scanPackage
argument_list|)
decl_stmt|;
name|validateMatchingSetContains
argument_list|(
name|scanned
argument_list|,
name|ScanTargetOne
operator|.
name|class
argument_list|,
name|ScanTargetTwo
operator|.
name|class
argument_list|,
name|ScanTargetThree
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|testFindImplementationsWithoutExtraFilters ()
specifier|public
name|void
name|testFindImplementationsWithoutExtraFilters
parameter_list|()
block|{
name|Set
argument_list|<
name|Class
argument_list|>
name|scanned
init|=
name|resolver
operator|.
name|findImplementations
argument_list|(
name|ScanTargetOne
operator|.
name|class
argument_list|,
name|scanPackage
argument_list|)
decl_stmt|;
name|validateMatchingSetContains
argument_list|(
name|scanned
argument_list|,
name|ScanTargetOne
operator|.
name|class
argument_list|,
name|ScanTargetTwo
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|testFindByAnnotationWithIncludePackageFilter ()
specifier|public
name|void
name|testFindByAnnotationWithIncludePackageFilter
parameter_list|()
block|{
name|filter
operator|.
name|addIncludePattern
argument_list|(
name|scanPackage
operator|+
literal|".b.*"
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|addFilter
argument_list|(
name|filter
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Class
argument_list|>
name|scanned
init|=
name|resolver
operator|.
name|findAnnotated
argument_list|(
name|ScannableOne
operator|.
name|class
argument_list|,
name|scanPackage
argument_list|)
decl_stmt|;
name|validateMatchingSetContains
argument_list|(
name|scanned
argument_list|,
name|ScanTargetTwo
operator|.
name|class
argument_list|)
expr_stmt|;
name|scanned
operator|=
name|resolver
operator|.
name|findAnnotated
argument_list|(
name|ScannableTwo
operator|.
name|class
argument_list|,
name|scanPackage
argument_list|)
expr_stmt|;
name|validateMatchingSetContains
argument_list|(
name|scanned
argument_list|)
expr_stmt|;
block|}
DECL|method|testFindByAnnotationsWithIncludePackageFilter ()
specifier|public
name|void
name|testFindByAnnotationsWithIncludePackageFilter
parameter_list|()
block|{
name|filter
operator|.
name|addIncludePattern
argument_list|(
name|scanPackage
operator|+
literal|".b.*"
argument_list|)
expr_stmt|;
name|filter
operator|.
name|addIncludePattern
argument_list|(
name|scanPackage
operator|+
literal|".c.*"
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|addFilter
argument_list|(
name|filter
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Class
argument_list|>
name|scanned
init|=
name|resolver
operator|.
name|findAnnotated
argument_list|(
name|annotations
argument_list|,
literal|"org.apache.camel.impl.scan"
argument_list|)
decl_stmt|;
name|validateMatchingSetContains
argument_list|(
name|scanned
argument_list|,
name|ScanTargetTwo
operator|.
name|class
argument_list|,
name|ScanTargetThree
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|testFindByAnnotationWithExcludePackageFilter ()
specifier|public
name|void
name|testFindByAnnotationWithExcludePackageFilter
parameter_list|()
block|{
name|filter
operator|.
name|addExcludePattern
argument_list|(
name|scanPackage
operator|+
literal|".b.*"
argument_list|)
expr_stmt|;
name|filter
operator|.
name|addExcludePattern
argument_list|(
name|scanPackage
operator|+
literal|".c.*"
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|addFilter
argument_list|(
name|filter
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Class
argument_list|>
name|scanned
init|=
name|resolver
operator|.
name|findAnnotated
argument_list|(
name|ScannableOne
operator|.
name|class
argument_list|,
name|scanPackage
argument_list|)
decl_stmt|;
name|validateMatchingSetContains
argument_list|(
name|scanned
argument_list|,
name|ScanTargetOne
operator|.
name|class
argument_list|)
expr_stmt|;
name|scanned
operator|=
name|resolver
operator|.
name|findAnnotated
argument_list|(
name|ScannableTwo
operator|.
name|class
argument_list|,
name|scanPackage
argument_list|)
expr_stmt|;
name|validateMatchingSetContains
argument_list|(
name|scanned
argument_list|)
expr_stmt|;
block|}
DECL|method|testFindByAnnotationsWithExcludePackageFilter ()
specifier|public
name|void
name|testFindByAnnotationsWithExcludePackageFilter
parameter_list|()
block|{
name|filter
operator|.
name|addExcludePattern
argument_list|(
name|scanPackage
operator|+
literal|".a.*"
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Class
argument_list|>
name|scanned
init|=
name|resolver
operator|.
name|findAnnotated
argument_list|(
name|annotations
argument_list|,
literal|"org.apache.camel.impl.scan"
argument_list|)
decl_stmt|;
name|validateMatchingSetContains
argument_list|(
name|scanned
argument_list|,
name|ScanTargetTwo
operator|.
name|class
argument_list|,
name|ScanTargetThree
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|testFindByFilterWithIncludePackageFilter ()
specifier|public
name|void
name|testFindByFilterWithIncludePackageFilter
parameter_list|()
block|{
name|filter
operator|.
name|addIncludePattern
argument_list|(
name|scanPackage
operator|+
literal|".**.ScanTarget*"
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|addFilter
argument_list|(
name|filter
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Class
argument_list|>
name|scanned
init|=
name|resolver
operator|.
name|findByFilter
argument_list|(
name|filter
argument_list|,
literal|"org.apache.camel.impl.scan"
argument_list|)
decl_stmt|;
name|validateMatchingSetContains
argument_list|(
name|scanned
argument_list|,
name|ScanTargetOne
operator|.
name|class
argument_list|,
name|ScanTargetTwo
operator|.
name|class
argument_list|,
name|ScanTargetThree
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|testFindImplementationsWithIncludePackageFilter ()
specifier|public
name|void
name|testFindImplementationsWithIncludePackageFilter
parameter_list|()
block|{
name|filter
operator|.
name|addIncludePattern
argument_list|(
name|scanPackage
operator|+
literal|".b.*"
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|addFilter
argument_list|(
name|filter
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Class
argument_list|>
name|scanned
init|=
name|resolver
operator|.
name|findImplementations
argument_list|(
name|ScanTargetOne
operator|.
name|class
argument_list|,
name|scanPackage
argument_list|)
decl_stmt|;
name|validateMatchingSetContains
argument_list|(
name|scanned
argument_list|,
name|ScanTargetTwo
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|testFindImplementationsWithExcludePackageFilter ()
specifier|public
name|void
name|testFindImplementationsWithExcludePackageFilter
parameter_list|()
block|{
name|filter
operator|.
name|addExcludePattern
argument_list|(
name|scanPackage
operator|+
literal|".a.*"
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|addFilter
argument_list|(
name|filter
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Class
argument_list|>
name|scanned
init|=
name|resolver
operator|.
name|findImplementations
argument_list|(
name|ScanTargetOne
operator|.
name|class
argument_list|,
name|scanPackage
argument_list|)
decl_stmt|;
name|validateMatchingSetContains
argument_list|(
name|scanned
argument_list|,
name|ScanTargetTwo
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|testFindByFilterPackageInJarUrl ()
specifier|public
name|void
name|testFindByFilterPackageInJarUrl
parameter_list|()
throws|throws
name|Exception
block|{
name|ClassLoader
name|savedClassLoader
init|=
literal|null
decl_stmt|;
try|try
block|{
name|savedClassLoader
operator|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
expr_stmt|;
name|URL
name|url
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/package_scan_test.jar"
argument_list|)
decl_stmt|;
name|URL
name|urls
index|[]
init|=
block|{
operator|new
name|URL
argument_list|(
literal|"jar:"
operator|+
name|url
operator|.
name|toString
argument_list|()
operator|+
literal|"!/"
argument_list|)
block|}
decl_stmt|;
name|URLClassLoader
name|classLoader
init|=
operator|new
name|URLClassLoader
argument_list|(
name|urls
argument_list|,
name|savedClassLoader
argument_list|)
decl_stmt|;
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|classLoader
argument_list|)
expr_stmt|;
name|filter
operator|.
name|addIncludePattern
argument_list|(
literal|"a.*.c.*"
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|addFilter
argument_list|(
name|filter
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Class
argument_list|>
name|scanned
init|=
name|resolver
operator|.
name|findByFilter
argument_list|(
name|filter
argument_list|,
literal|"a.b.c"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|scanned
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"class a.b.c.Test"
argument_list|,
name|scanned
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|savedClassLoader
operator|!=
literal|null
condition|)
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|savedClassLoader
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|testFindByFilterPackageInJarUrlWithPlusChars ()
specifier|public
name|void
name|testFindByFilterPackageInJarUrlWithPlusChars
parameter_list|()
throws|throws
name|Exception
block|{
name|ClassLoader
name|savedClassLoader
init|=
literal|null
decl_stmt|;
try|try
block|{
name|savedClassLoader
operator|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
expr_stmt|;
name|URL
name|url
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/package+scan+test.jar"
argument_list|)
decl_stmt|;
name|URL
name|urls
index|[]
init|=
block|{
operator|new
name|URL
argument_list|(
literal|"jar:"
operator|+
name|url
operator|.
name|toString
argument_list|()
operator|+
literal|"!/"
argument_list|)
block|}
decl_stmt|;
name|URLClassLoader
name|classLoader
init|=
operator|new
name|URLClassLoader
argument_list|(
name|urls
argument_list|,
name|savedClassLoader
argument_list|)
decl_stmt|;
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|classLoader
argument_list|)
expr_stmt|;
name|filter
operator|.
name|addIncludePattern
argument_list|(
literal|"a.*.c.*"
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|addFilter
argument_list|(
name|filter
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Class
argument_list|>
name|scanned
init|=
name|resolver
operator|.
name|findByFilter
argument_list|(
name|filter
argument_list|,
literal|"a.b.c"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|scanned
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"class a.b.c.Test"
argument_list|,
name|scanned
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|savedClassLoader
operator|!=
literal|null
condition|)
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|savedClassLoader
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

