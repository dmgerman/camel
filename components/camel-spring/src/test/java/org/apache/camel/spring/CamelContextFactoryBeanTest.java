begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Reader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBContext
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|DefaultModelJAXBContextFactory
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
name|DefaultUuidGenerator
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
name|SimpleUuidGenerator
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
name|ModelJAXBContextFactory
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
name|UuidGenerator
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
name|IOHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|custommonkey
operator|.
name|xmlunit
operator|.
name|XMLAssert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|custommonkey
operator|.
name|xmlunit
operator|.
name|XMLUnit
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|StaticApplicationContext
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|CamelContextFactoryBeanTest
specifier|public
class|class
name|CamelContextFactoryBeanTest
extends|extends
name|TestCase
block|{
DECL|field|factory
specifier|private
name|CamelContextFactoryBean
name|factory
decl_stmt|;
DECL|method|setUp ()
specifier|protected
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
name|factory
operator|=
operator|new
name|CamelContextFactoryBean
argument_list|()
expr_stmt|;
name|factory
operator|.
name|setId
argument_list|(
literal|"camelContext"
argument_list|)
expr_stmt|;
block|}
DECL|method|testGetDefaultUuidGenerator ()
specifier|public
name|void
name|testGetDefaultUuidGenerator
parameter_list|()
throws|throws
name|Exception
block|{
name|factory
operator|.
name|setApplicationContext
argument_list|(
operator|new
name|StaticApplicationContext
argument_list|()
argument_list|)
expr_stmt|;
name|factory
operator|.
name|afterPropertiesSet
argument_list|()
expr_stmt|;
name|UuidGenerator
name|uuidGenerator
init|=
name|factory
operator|.
name|getContext
argument_list|()
operator|.
name|getUuidGenerator
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|uuidGenerator
operator|instanceof
name|DefaultUuidGenerator
argument_list|)
expr_stmt|;
block|}
DECL|method|testGetCustomUuidGenerator ()
specifier|public
name|void
name|testGetCustomUuidGenerator
parameter_list|()
throws|throws
name|Exception
block|{
name|StaticApplicationContext
name|applicationContext
init|=
operator|new
name|StaticApplicationContext
argument_list|()
decl_stmt|;
name|applicationContext
operator|.
name|registerSingleton
argument_list|(
literal|"uuidGenerator"
argument_list|,
name|SimpleUuidGenerator
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setApplicationContext
argument_list|(
name|applicationContext
argument_list|)
expr_stmt|;
name|factory
operator|.
name|afterPropertiesSet
argument_list|()
expr_stmt|;
name|UuidGenerator
name|uuidGenerator
init|=
name|factory
operator|.
name|getContext
argument_list|()
operator|.
name|getUuidGenerator
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|uuidGenerator
operator|instanceof
name|SimpleUuidGenerator
argument_list|)
expr_stmt|;
block|}
DECL|method|testSetEndpoints ()
specifier|public
name|void
name|testSetEndpoints
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Create a new Camel context and add an endpoint
name|CamelContextFactoryBean
name|camelContext
init|=
operator|new
name|CamelContextFactoryBean
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|CamelEndpointFactoryBean
argument_list|>
name|endpoints
init|=
operator|new
name|LinkedList
argument_list|<
name|CamelEndpointFactoryBean
argument_list|>
argument_list|()
decl_stmt|;
name|CamelEndpointFactoryBean
name|endpoint
init|=
operator|new
name|CamelEndpointFactoryBean
argument_list|()
decl_stmt|;
name|endpoint
operator|.
name|setId
argument_list|(
literal|"endpoint1"
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setUri
argument_list|(
literal|"mock:end"
argument_list|)
expr_stmt|;
name|endpoints
operator|.
name|add
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|setEndpoints
argument_list|(
name|endpoints
argument_list|)
expr_stmt|;
comment|// Compare the new context with our reference context
name|Reader
name|expectedContext
init|=
literal|null
decl_stmt|;
try|try
block|{
name|expectedContext
operator|=
operator|new
name|InputStreamReader
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/org/apache/camel/spring/context-with-endpoint.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|createdContext
init|=
name|contextAsString
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
name|XMLUnit
operator|.
name|setIgnoreWhitespace
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|XMLAssert
operator|.
name|assertXMLEqual
argument_list|(
name|expectedContext
argument_list|,
operator|new
name|StringReader
argument_list|(
name|createdContext
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|expectedContext
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|contextAsString (CamelContextFactoryBean context)
specifier|private
name|String
name|contextAsString
parameter_list|(
name|CamelContextFactoryBean
name|context
parameter_list|)
throws|throws
name|Exception
block|{
name|StringWriter
name|stringOut
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|JAXBContext
name|jaxb
init|=
name|JAXBContext
operator|.
name|newInstance
argument_list|(
name|CamelContextFactoryBean
operator|.
name|class
argument_list|)
decl_stmt|;
name|jaxb
operator|.
name|createMarshaller
argument_list|()
operator|.
name|marshal
argument_list|(
name|context
argument_list|,
name|stringOut
argument_list|)
expr_stmt|;
return|return
name|stringOut
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|testCustomModelJAXBContextFactory ()
specifier|public
name|void
name|testCustomModelJAXBContextFactory
parameter_list|()
throws|throws
name|Exception
block|{
name|StaticApplicationContext
name|applicationContext
init|=
operator|new
name|StaticApplicationContext
argument_list|()
decl_stmt|;
name|applicationContext
operator|.
name|registerSingleton
argument_list|(
literal|"customModelJAXBContextFactory"
argument_list|,
name|CustomModelJAXBContextFactory
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setApplicationContext
argument_list|(
name|applicationContext
argument_list|)
expr_stmt|;
name|factory
operator|.
name|afterPropertiesSet
argument_list|()
expr_stmt|;
name|ModelJAXBContextFactory
name|modelJAXBContextFactory
init|=
name|factory
operator|.
name|getContext
argument_list|()
operator|.
name|getModelJAXBContextFactory
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|modelJAXBContextFactory
operator|instanceof
name|CustomModelJAXBContextFactory
argument_list|)
expr_stmt|;
block|}
DECL|class|CustomModelJAXBContextFactory
specifier|private
specifier|static
class|class
name|CustomModelJAXBContextFactory
extends|extends
name|DefaultModelJAXBContextFactory
block|{
comment|// Do nothing here
block|}
block|}
end_class

end_unit

