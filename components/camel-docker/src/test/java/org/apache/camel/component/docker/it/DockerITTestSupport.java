begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.docker.it
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|docker
operator|.
name|it
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
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
name|component
operator|.
name|docker
operator|.
name|DockerComponent
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
name|docker
operator|.
name|DockerConfiguration
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
name|PropertyBindingSupport
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

begin_class
DECL|class|DockerITTestSupport
specifier|public
class|class
name|DockerITTestSupport
extends|extends
name|CamelTestSupport
block|{
DECL|field|TEST_OPTIONS_PROPERTIES
specifier|private
specifier|static
specifier|final
name|String
name|TEST_OPTIONS_PROPERTIES
init|=
literal|"/test-options.properties"
decl_stmt|;
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
comment|// Read Docker component configuration properties
name|Properties
name|properties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|properties
operator|.
name|load
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|TEST_OPTIONS_PROPERTIES
argument_list|)
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|options
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|properties
operator|.
name|entrySet
argument_list|()
operator|.
name|forEach
argument_list|(
name|e
lambda|->
name|options
operator|.
name|put
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|DockerConfiguration
name|configuration
init|=
operator|new
name|DockerConfiguration
argument_list|()
decl_stmt|;
name|PropertyBindingSupport
operator|.
name|bindProperties
argument_list|(
name|context
argument_list|,
name|configuration
argument_list|,
name|options
argument_list|)
expr_stmt|;
name|DockerComponent
name|component
init|=
operator|new
name|DockerComponent
argument_list|()
decl_stmt|;
name|component
operator|.
name|setConfiguration
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|context
operator|.
name|addComponent
argument_list|(
literal|"docker"
argument_list|,
name|component
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
block|}
end_class

end_unit

