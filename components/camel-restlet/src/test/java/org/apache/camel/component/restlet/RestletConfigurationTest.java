begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.restlet
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|restlet
package|;
end_package

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
name|builder
operator|.
name|RouteBuilder
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
name|restlet
operator|.
name|engine
operator|.
name|Engine
import|;
end_import

begin_import
import|import
name|org
operator|.
name|restlet
operator|.
name|engine
operator|.
name|converter
operator|.
name|ConverterHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|restlet
operator|.
name|ext
operator|.
name|gson
operator|.
name|GsonConverter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|restlet
operator|.
name|ext
operator|.
name|jackson
operator|.
name|JacksonConverter
import|;
end_import

begin_class
DECL|class|RestletConfigurationTest
specifier|public
class|class
name|RestletConfigurationTest
extends|extends
name|RestletTestSupport
block|{
annotation|@
name|Override
DECL|method|doPreSetup ()
specifier|protected
name|void
name|doPreSetup
parameter_list|()
block|{
name|assertPresent
argument_list|(
name|GsonConverter
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertPresent
argument_list|(
name|JacksonConverter
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConfiguration ()
specifier|public
name|void
name|testConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
name|assertPresent
argument_list|(
name|GsonConverter
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertPresent
argument_list|(
name|JacksonConverter
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
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
name|restConfiguration
argument_list|()
operator|.
name|component
argument_list|(
literal|"restlet"
argument_list|)
operator|.
name|componentProperty
argument_list|(
literal|"enabledConverters"
argument_list|,
literal|"JacksonConverter"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"restlet:http://localhost:"
operator|+
name|portNum
operator|+
literal|"/users/1/basic"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:reply"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|findByType (Class<T> type)
specifier|protected
parameter_list|<
name|T
extends|extends
name|ConverterHelper
parameter_list|>
name|Optional
argument_list|<
name|ConverterHelper
argument_list|>
name|findByType
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|Engine
operator|.
name|getInstance
argument_list|()
operator|.
name|getRegisteredConverters
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|type
operator|::
name|isInstance
argument_list|)
operator|.
name|findFirst
argument_list|()
return|;
block|}
DECL|method|assertPresent (Class<T> type)
specifier|protected
parameter_list|<
name|T
extends|extends
name|ConverterHelper
parameter_list|>
name|void
name|assertPresent
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|type
operator|.
name|getSimpleName
argument_list|()
argument_list|,
name|findByType
argument_list|(
name|type
argument_list|)
operator|.
name|isPresent
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|assertNotPresent (Class<T> type)
specifier|protected
parameter_list|<
name|T
extends|extends
name|ConverterHelper
parameter_list|>
name|void
name|assertNotPresent
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|assertFalse
argument_list|(
name|type
operator|.
name|getSimpleName
argument_list|()
argument_list|,
name|findByType
argument_list|(
name|type
argument_list|)
operator|.
name|isPresent
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

