begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.core.osgi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|core
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
name|InputStream
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|OsgiClassResolverTest
specifier|public
class|class
name|OsgiClassResolverTest
extends|extends
name|CamelOsgiTestSupport
block|{
annotation|@
name|Test
DECL|method|testResolveClass ()
specifier|public
name|void
name|testResolveClass
parameter_list|()
block|{
name|ClassResolver
name|classResolver
init|=
name|getClassResolver
argument_list|()
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|routeBuilder
init|=
name|classResolver
operator|.
name|resolveClass
argument_list|(
literal|"org.apache.camel.core.osgi.test.MyRouteBuilder"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"The class of routeBuilder should not be null."
argument_list|,
name|routeBuilder
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testResolverResource ()
specifier|public
name|void
name|testResolverResource
parameter_list|()
block|{
name|ClassResolver
name|classResolver
init|=
name|getClassResolver
argument_list|()
decl_stmt|;
name|InputStream
name|is
init|=
name|classResolver
operator|.
name|loadResourceAsStream
argument_list|(
literal|"META-INF/services/org/apache/camel/TypeConverterLoader"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"The InputStream should not be null."
argument_list|,
name|is
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

