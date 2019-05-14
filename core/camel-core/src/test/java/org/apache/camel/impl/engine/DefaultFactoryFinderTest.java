begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.engine
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|engine
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

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
name|net
operator|.
name|URL
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|NoFactoryAvailableException
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
name|ClassResolver
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
name|Injector
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
name|hamcrest
operator|.
name|Matchers
operator|.
name|matchesPattern
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|core
operator|.
name|IsCollectionContaining
operator|.
name|hasItem
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|core
operator|.
name|IsInstanceOf
operator|.
name|instanceOf
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
name|assertSame
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
name|assertThat
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
name|fail
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|mock
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|when
import|;
end_import

begin_class
DECL|class|DefaultFactoryFinderTest
specifier|public
class|class
name|DefaultFactoryFinderTest
block|{
DECL|class|TestImplA
specifier|public
specifier|static
class|class
name|TestImplA
implements|implements
name|TestType
block|{     }
DECL|class|TestImplB
specifier|public
specifier|static
class|class
name|TestImplB
implements|implements
name|TestType
block|{     }
DECL|interface|TestType
specifier|public
interface|interface
name|TestType
block|{     }
DECL|field|TEST_RESOURCE_PATH
specifier|private
specifier|static
specifier|final
name|String
name|TEST_RESOURCE_PATH
init|=
literal|"/org/apache/camel/impl/"
decl_stmt|;
DECL|field|factoryFinder
specifier|final
name|DefaultFactoryFinder
name|factoryFinder
init|=
operator|new
name|DefaultFactoryFinder
argument_list|(
operator|new
name|DefaultClassResolver
argument_list|()
argument_list|,
name|TEST_RESOURCE_PATH
argument_list|)
decl_stmt|;
annotation|@
name|Test
DECL|method|shouldComplainIfClassResolverCannotResolveClass ()
specifier|public
name|void
name|shouldComplainIfClassResolverCannotResolveClass
parameter_list|()
throws|throws
name|IOException
block|{
specifier|final
name|ClassResolver
name|classResolver
init|=
name|mock
argument_list|(
name|ClassResolver
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|String
name|properties
init|=
literal|"class="
operator|+
name|TestImplA
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
name|when
argument_list|(
name|classResolver
operator|.
name|loadResourceAsStream
argument_list|(
literal|"/org/apache/camel/impl/TestImplA"
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|properties
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|classResolver
operator|.
name|resolveClass
argument_list|(
name|TestImplA
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
specifier|final
name|DefaultFactoryFinder
name|factoryFinder
init|=
operator|new
name|DefaultFactoryFinder
argument_list|(
name|classResolver
argument_list|,
name|TEST_RESOURCE_PATH
argument_list|)
decl_stmt|;
try|try
block|{
name|factoryFinder
operator|.
name|findClass
argument_list|(
literal|"TestImplA"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown ClassNotFoundException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
specifier|final
name|ClassNotFoundException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|TestImplA
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|shouldComplainIfInstanceTypeIsNotAsExpected ()
specifier|public
name|void
name|shouldComplainIfInstanceTypeIsNotAsExpected
parameter_list|()
throws|throws
name|ClassNotFoundException
throws|,
name|IOException
block|{
specifier|final
name|Injector
name|injector
init|=
name|mock
argument_list|(
name|Injector
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|TestImplA
name|expected
init|=
operator|new
name|TestImplA
argument_list|()
decl_stmt|;
name|when
argument_list|(
name|injector
operator|.
name|newInstance
argument_list|(
name|TestImplA
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|expected
argument_list|)
expr_stmt|;
try|try
block|{
name|factoryFinder
operator|.
name|newInstances
argument_list|(
literal|"TestImplA"
argument_list|,
name|injector
argument_list|,
name|TestImplB
operator|.
name|class
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"ClassCastException should have been thrown"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
specifier|final
name|ClassCastException
name|e
parameter_list|)
block|{
specifier|final
name|String
name|message
init|=
name|e
operator|.
name|getMessage
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|message
argument_list|,
name|matchesPattern
argument_list|(
literal|"Not instanceof org\\.apache\\.camel\\.impl\\.engine\\.DefaultFactoryFinderTest\\$TestImplB "
operator|+
literal|"value: org\\.apache\\.camel\\.impl\\.engine\\.DefaultFactoryFinderTest\\$TestImplA.*"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|shouldComplainIfUnableToCreateNewInstances ()
specifier|public
name|void
name|shouldComplainIfUnableToCreateNewInstances
parameter_list|()
throws|throws
name|ClassNotFoundException
throws|,
name|IOException
block|{
try|try
block|{
name|factoryFinder
operator|.
name|newInstance
argument_list|(
literal|"TestImplX"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"NoFactoryAvailableException should have been thrown"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
specifier|final
name|NoFactoryAvailableException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Cannot find factory class for resource: TestImplX"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|shouldComplainNoClassKeyInPropertyFile ()
specifier|public
name|void
name|shouldComplainNoClassKeyInPropertyFile
parameter_list|()
throws|throws
name|ClassNotFoundException
block|{
try|try
block|{
name|factoryFinder
operator|.
name|findClass
argument_list|(
literal|"TestImplNoProperty"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"NoFactoryAvailableException should have been thrown"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
specifier|final
name|IOException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Expected property is missing: class"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|shouldCreateNewInstances ()
specifier|public
name|void
name|shouldCreateNewInstances
parameter_list|()
throws|throws
name|ClassNotFoundException
throws|,
name|IOException
block|{
specifier|final
name|Object
name|instance
init|=
name|factoryFinder
operator|.
name|newInstance
argument_list|(
literal|"TestImplA"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|instance
argument_list|,
name|instanceOf
argument_list|(
name|TestImplA
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldCreateNewInstancesWithInjector ()
specifier|public
name|void
name|shouldCreateNewInstancesWithInjector
parameter_list|()
throws|throws
name|ClassNotFoundException
throws|,
name|IOException
block|{
specifier|final
name|Injector
name|injector
init|=
name|mock
argument_list|(
name|Injector
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|TestImplA
name|expected
init|=
operator|new
name|TestImplA
argument_list|()
decl_stmt|;
name|when
argument_list|(
name|injector
operator|.
name|newInstance
argument_list|(
name|TestImplA
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|expected
argument_list|)
expr_stmt|;
specifier|final
name|List
argument_list|<
name|TestType
argument_list|>
name|instances
init|=
name|factoryFinder
operator|.
name|newInstances
argument_list|(
literal|"TestImplA"
argument_list|,
name|injector
argument_list|,
name|TestType
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|instances
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|instances
argument_list|,
name|hasItem
argument_list|(
name|expected
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|expected
argument_list|,
name|instances
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldFindSingleClass ()
specifier|public
name|void
name|shouldFindSingleClass
parameter_list|()
throws|throws
name|ClassNotFoundException
throws|,
name|IOException
block|{
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
init|=
name|factoryFinder
operator|.
name|findClass
argument_list|(
literal|"TestImplA"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|TestImplA
operator|.
name|class
argument_list|,
name|clazz
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldFindSingleClassFromClassMap ()
specifier|public
name|void
name|shouldFindSingleClassFromClassMap
parameter_list|()
throws|throws
name|ClassNotFoundException
throws|,
name|IOException
block|{
specifier|final
name|DefaultFactoryFinder
name|factoryFinder
init|=
operator|new
name|DefaultFactoryFinder
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|factoryFinder
operator|.
name|addToClassMap
argument_list|(
literal|"prefixkey"
argument_list|,
parameter_list|()
lambda|->
name|TestImplA
operator|.
name|class
argument_list|)
expr_stmt|;
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
init|=
name|factoryFinder
operator|.
name|findClass
argument_list|(
literal|"key"
argument_list|,
literal|"prefix"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|TestImplA
operator|.
name|class
argument_list|,
name|clazz
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldFindSingleClassWithPropertyPrefix ()
specifier|public
name|void
name|shouldFindSingleClassWithPropertyPrefix
parameter_list|()
throws|throws
name|ClassNotFoundException
throws|,
name|IOException
block|{
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
init|=
name|factoryFinder
operator|.
name|findClass
argument_list|(
literal|"TestImplA"
argument_list|,
literal|"prefix."
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|TestImplA
operator|.
name|class
argument_list|,
name|clazz
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldFindSingleClassWithPropertyPrefixAndExpectedType ()
specifier|public
name|void
name|shouldFindSingleClassWithPropertyPrefixAndExpectedType
parameter_list|()
throws|throws
name|ClassNotFoundException
throws|,
name|IOException
block|{
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
init|=
name|factoryFinder
operator|.
name|findClass
argument_list|(
literal|"TestImplA"
argument_list|,
literal|"prefix."
argument_list|,
name|TestType
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|TestImplA
operator|.
name|class
argument_list|,
name|clazz
argument_list|)
expr_stmt|;
block|}
DECL|method|urlFor (final Class<?> clazz)
name|URL
name|urlFor
parameter_list|(
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
block|{
specifier|final
name|String
name|resourceName
init|=
name|clazz
operator|.
name|getPackage
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|replace
argument_list|(
literal|'.'
argument_list|,
literal|'/'
argument_list|)
operator|+
literal|"/"
operator|+
name|clazz
operator|.
name|getSimpleName
argument_list|()
operator|+
literal|".properties"
decl_stmt|;
specifier|final
name|ClassLoader
name|classLoader
init|=
name|clazz
operator|.
name|getClassLoader
argument_list|()
decl_stmt|;
return|return
name|classLoader
operator|.
name|getResource
argument_list|(
name|resourceName
argument_list|)
return|;
block|}
block|}
end_class

end_unit

