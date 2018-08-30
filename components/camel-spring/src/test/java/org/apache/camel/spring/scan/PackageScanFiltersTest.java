begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.scan
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|scan
package|;
end_package

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
name|util
operator|.
name|LinkedHashSet
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
name|junit
operator|.
name|Assert
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
name|AnnotatedWithAnyPackageScanFilter
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
name|AnnotatedWithPackageScanFilter
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
name|AssignableToPackageScanFilter
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
name|CompositePackageScanFilter
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
name|InvertingPackageScanFilter
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
name|spi
operator|.
name|PackageScanFilter
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
name|scan
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
name|spring
operator|.
name|scan
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
name|spring
operator|.
name|scan
operator|.
name|c
operator|.
name|ScanTargetThree
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
name|CollectionHelper
import|;
end_import

begin_class
DECL|class|PackageScanFiltersTest
specifier|public
class|class
name|PackageScanFiltersTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
DECL|method|testAssignableToPackageScanFilter ()
specifier|public
name|void
name|testAssignableToPackageScanFilter
parameter_list|()
block|{
name|AssignableToPackageScanFilter
name|filter
init|=
operator|new
name|AssignableToPackageScanFilter
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|filter
operator|.
name|matches
argument_list|(
name|ScanTargetOne
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|filter
operator|=
operator|new
name|AssignableToPackageScanFilter
argument_list|(
name|ScanTargetOne
operator|.
name|class
argument_list|)
expr_stmt|;
name|validateFilter
argument_list|(
name|filter
argument_list|,
name|ScanTargetOne
operator|.
name|class
argument_list|)
expr_stmt|;
name|filter
operator|=
operator|new
name|AssignableToPackageScanFilter
argument_list|(
name|ScanTargetOne
operator|.
name|class
argument_list|)
expr_stmt|;
name|validateFilter
argument_list|(
name|filter
argument_list|,
name|ScanTargetTwo
operator|.
name|class
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|classes
init|=
operator|new
name|LinkedHashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|classes
operator|.
name|add
argument_list|(
name|ScanTargetOne
operator|.
name|class
argument_list|)
expr_stmt|;
name|classes
operator|.
name|add
argument_list|(
name|ScanTargetThree
operator|.
name|class
argument_list|)
expr_stmt|;
name|filter
operator|=
operator|new
name|AssignableToPackageScanFilter
argument_list|(
name|classes
argument_list|)
expr_stmt|;
name|validateFilter
argument_list|(
name|filter
argument_list|,
name|ScanTargetOne
operator|.
name|class
argument_list|)
expr_stmt|;
name|validateFilter
argument_list|(
name|filter
argument_list|,
name|ScanTargetTwo
operator|.
name|class
argument_list|)
expr_stmt|;
name|validateFilter
argument_list|(
name|filter
argument_list|,
name|ScanTargetThree
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|filter
operator|.
name|toString
argument_list|()
operator|.
name|contains
argument_list|(
literal|"ScanTargetOne"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|filter
operator|.
name|toString
argument_list|()
operator|.
name|contains
argument_list|(
literal|"ScanTargetThree"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAnnotatedWithAnyPackageScanFilter ()
specifier|public
name|void
name|testAnnotatedWithAnyPackageScanFilter
parameter_list|()
block|{
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
name|LinkedHashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|annotations
operator|.
name|add
argument_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|scan
operator|.
name|ScannableOne
operator|.
name|class
argument_list|)
expr_stmt|;
name|annotations
operator|.
name|add
argument_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|scan
operator|.
name|ScannableTwo
operator|.
name|class
argument_list|)
expr_stmt|;
name|AnnotatedWithAnyPackageScanFilter
name|filter
init|=
operator|new
name|AnnotatedWithAnyPackageScanFilter
argument_list|(
name|annotations
argument_list|)
decl_stmt|;
name|Class
argument_list|<
name|ScanTargetOne
argument_list|>
name|type
init|=
name|ScanTargetOne
operator|.
name|class
decl_stmt|;
name|validateFilter
argument_list|(
name|filter
argument_list|,
name|type
argument_list|)
expr_stmt|;
name|validateFilter
argument_list|(
name|filter
argument_list|,
name|ScanTargetThree
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"annotated with any @[[interface org.apache.camel.spring.scan.ScannableOne, interface org.apache.camel.spring.scan.ScannableTwo]]"
argument_list|,
name|filter
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAnnotatedWithPackageScanFilter ()
specifier|public
name|void
name|testAnnotatedWithPackageScanFilter
parameter_list|()
block|{
name|AnnotatedWithPackageScanFilter
name|filter
init|=
operator|new
name|AnnotatedWithPackageScanFilter
argument_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|scan
operator|.
name|ScannableOne
operator|.
name|class
argument_list|)
decl_stmt|;
name|validateFilter
argument_list|(
name|filter
argument_list|,
name|ScanTargetOne
operator|.
name|class
argument_list|)
expr_stmt|;
name|validateFilter
argument_list|(
name|filter
argument_list|,
name|ScanTargetTwo
operator|.
name|class
argument_list|)
expr_stmt|;
name|filter
operator|=
operator|new
name|AnnotatedWithPackageScanFilter
argument_list|(
name|ScannableTwo
operator|.
name|class
argument_list|)
expr_stmt|;
name|validateFilter
argument_list|(
name|filter
argument_list|,
name|ScanTargetThree
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"annotated with @ScannableTwo"
argument_list|,
name|filter
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCompositePackageScanFilter ()
specifier|public
name|void
name|testCompositePackageScanFilter
parameter_list|()
block|{
name|PackageScanFilter
name|one
init|=
operator|new
name|AnnotatedWithPackageScanFilter
argument_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|scan
operator|.
name|ScannableOne
operator|.
name|class
argument_list|)
decl_stmt|;
name|PackageScanFilter
name|two
init|=
operator|new
name|AssignableToPackageScanFilter
argument_list|(
name|ScanTargetOne
operator|.
name|class
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|PackageScanFilter
argument_list|>
name|filters
init|=
name|CollectionHelper
operator|.
name|createSetContaining
argument_list|(
name|one
argument_list|,
name|two
argument_list|)
decl_stmt|;
name|CompositePackageScanFilter
name|filter
init|=
operator|new
name|CompositePackageScanFilter
argument_list|(
name|filters
argument_list|)
decl_stmt|;
name|validateFilter
argument_list|(
name|filter
argument_list|,
name|ScanTargetOne
operator|.
name|class
argument_list|)
expr_stmt|;
name|validateFilter
argument_list|(
name|filter
argument_list|,
name|ScanTargetTwo
operator|.
name|class
argument_list|)
expr_stmt|;
name|filter
operator|=
operator|new
name|CompositePackageScanFilter
argument_list|()
expr_stmt|;
name|filter
operator|.
name|addFilter
argument_list|(
name|one
argument_list|)
expr_stmt|;
name|filter
operator|.
name|addFilter
argument_list|(
name|two
argument_list|)
expr_stmt|;
name|validateFilter
argument_list|(
name|filter
argument_list|,
name|ScanTargetOne
operator|.
name|class
argument_list|)
expr_stmt|;
name|validateFilter
argument_list|(
name|filter
argument_list|,
name|ScanTargetTwo
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInvertingFilter ()
specifier|public
name|void
name|testInvertingFilter
parameter_list|()
block|{
name|InvertingPackageScanFilter
name|filter
init|=
operator|new
name|InvertingPackageScanFilter
argument_list|(
operator|new
name|AnnotatedWithPackageScanFilter
argument_list|(
name|ScannableOne
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|validateFilter
argument_list|(
name|filter
argument_list|,
name|ScanTargetThree
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"![annotated with @ScannableOne]"
argument_list|,
name|filter
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|validateFilter (PackageScanFilter filter, Class<?> type)
specifier|private
name|void
name|validateFilter
parameter_list|(
name|PackageScanFilter
name|filter
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|filter
operator|.
name|matches
argument_list|(
name|type
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|new
name|InvertingPackageScanFilter
argument_list|(
name|filter
argument_list|)
operator|.
name|matches
argument_list|(
name|type
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

