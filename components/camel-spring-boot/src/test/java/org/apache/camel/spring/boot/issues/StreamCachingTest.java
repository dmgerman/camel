begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.boot.issues
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|boot
operator|.
name|issues
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileInputStream
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
import|import static
name|java
operator|.
name|util
operator|.
name|Arrays
operator|.
name|asList
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
name|EndpointInject
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
name|spring
operator|.
name|boot
operator|.
name|SpringTypeConverter
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
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
name|springframework
operator|.
name|core
operator|.
name|convert
operator|.
name|ConversionService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|convert
operator|.
name|support
operator|.
name|DefaultConversionService
import|;
end_import

begin_class
DECL|class|StreamCachingTest
specifier|public
class|class
name|StreamCachingTest
extends|extends
name|CamelTestSupport
block|{
comment|// this is not a spring boot test as its standalone Camel testing by extending CamelTestSupport
DECL|field|URI_END_OF_ROUTE
specifier|public
specifier|static
specifier|final
name|String
name|URI_END_OF_ROUTE
init|=
literal|"mock:end_of_route"
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
name|URI_END_OF_ROUTE
argument_list|)
DECL|field|endOfRoute
specifier|private
name|MockEndpoint
name|endOfRoute
decl_stmt|;
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|getTypeConverterRegistry
argument_list|()
operator|.
name|addFallbackTypeConverter
argument_list|(
name|springTypeConverter
argument_list|(
name|context
argument_list|,
operator|new
name|ConversionService
index|[]
block|{
operator|new
name|DefaultConversionService
argument_list|()
block|}
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:foo"
argument_list|)
operator|.
name|streamCaching
argument_list|()
operator|.
name|bean
argument_list|(
name|MyBean
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|URI_END_OF_ROUTE
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Test
DECL|method|streamCachingWithSpring ()
specifier|public
name|void
name|streamCachingWithSpring
parameter_list|()
throws|throws
name|Exception
block|{
name|endOfRoute
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
literal|"direct:foo"
argument_list|,
operator|new
name|FileInputStream
argument_list|(
operator|new
name|File
argument_list|(
literal|"src/test/resources/logback.xml"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|endOfRoute
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|class|MyBean
specifier|public
specifier|static
class|class
name|MyBean
block|{
DECL|method|someNumbers ()
specifier|public
name|List
argument_list|<
name|Integer
argument_list|>
name|someNumbers
parameter_list|()
block|{
return|return
name|asList
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|)
return|;
block|}
block|}
comment|/**      * Copied from org.apache.camel.spring.boot.TypeConversionConfiguration (they are package protected)      **/
DECL|method|springTypeConverter (CamelContext camelContext, ConversionService[] conversionServices)
name|SpringTypeConverter
name|springTypeConverter
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|ConversionService
index|[]
name|conversionServices
parameter_list|)
block|{
name|SpringTypeConverter
name|springTypeConverter
init|=
operator|new
name|SpringTypeConverter
argument_list|(
name|asList
argument_list|(
name|conversionServices
argument_list|)
argument_list|)
decl_stmt|;
name|camelContext
operator|.
name|getTypeConverterRegistry
argument_list|()
operator|.
name|addFallbackTypeConverter
argument_list|(
name|springTypeConverter
argument_list|,
literal|true
argument_list|)
expr_stmt|;
return|return
name|springTypeConverter
return|;
block|}
block|}
end_class

end_unit

