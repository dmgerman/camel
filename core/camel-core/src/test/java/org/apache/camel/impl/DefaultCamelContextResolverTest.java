begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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
name|java
operator|.
name|io
operator|.
name|OutputStream
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
name|Component
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
name|Endpoint
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
name|Exchange
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
name|Expression
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
name|NoSuchLanguageException
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
name|Predicate
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
name|DataFormat
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
name|Language
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
name|DefaultComponent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|AfterClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
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
name|junit
operator|.
name|Assert
operator|.
name|assertFalse
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
name|assertNotNull
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
name|assertNull
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
name|assertTrue
import|;
end_import

begin_comment
comment|/**  * Tests if the default camel context is able to resolve components and data formats using both their real names and/or fallback names.  * Fallback names have been introduced to avoid name clash in some registries (eg. Spring application context) between components and other camel features.  */
end_comment

begin_class
DECL|class|DefaultCamelContextResolverTest
specifier|public
class|class
name|DefaultCamelContextResolverTest
block|{
DECL|field|context
specifier|private
specifier|static
name|CamelContext
name|context
decl_stmt|;
annotation|@
name|BeforeClass
DECL|method|createContext ()
specifier|public
specifier|static
name|void
name|createContext
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|=
operator|new
name|DefaultCamelContext
argument_list|()
expr_stmt|;
comment|// Add a component using its fallback name
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|bind
argument_list|(
literal|"green-component"
argument_list|,
operator|new
name|SampleComponent
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
comment|// Add a data format using its fallback name
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|bind
argument_list|(
literal|"green-dataformat"
argument_list|,
operator|new
name|SampleDataFormat
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
comment|// Add a language using its fallback name
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|bind
argument_list|(
literal|"green-language"
argument_list|,
operator|new
name|SampleLanguage
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
comment|// Add a component using both names
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|bind
argument_list|(
literal|"yellow"
argument_list|,
operator|new
name|SampleComponent
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|bind
argument_list|(
literal|"yellow-component"
argument_list|,
operator|new
name|SampleComponent
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
comment|// Add a data format using both names
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|bind
argument_list|(
literal|"red"
argument_list|,
operator|new
name|SampleDataFormat
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|bind
argument_list|(
literal|"red-dataformat"
argument_list|,
operator|new
name|SampleDataFormat
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
comment|// Add a language using both names
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|bind
argument_list|(
literal|"blue"
argument_list|,
operator|new
name|SampleLanguage
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|bind
argument_list|(
literal|"blue-language"
argument_list|,
operator|new
name|SampleLanguage
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
annotation|@
name|AfterClass
DECL|method|destroyContext ()
specifier|public
specifier|static
name|void
name|destroyContext
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|context
operator|=
literal|null
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testComponentWithFallbackNames ()
specifier|public
name|void
name|testComponentWithFallbackNames
parameter_list|()
throws|throws
name|Exception
block|{
name|Component
name|component
init|=
name|context
operator|.
name|getComponent
argument_list|(
literal|"green"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Component not found in registry"
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Wrong instance type of the Component"
argument_list|,
name|component
operator|instanceof
name|SampleComponent
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Here we should have the fallback Component"
argument_list|,
operator|(
operator|(
name|SampleComponent
operator|)
name|component
operator|)
operator|.
name|isFallback
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testComponentWithBothNames ()
specifier|public
name|void
name|testComponentWithBothNames
parameter_list|()
throws|throws
name|Exception
block|{
name|Component
name|component
init|=
name|context
operator|.
name|getComponent
argument_list|(
literal|"yellow"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Component not found in registry"
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Wrong instance type of the Component"
argument_list|,
name|component
operator|instanceof
name|SampleComponent
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Here we should NOT have the fallback Component"
argument_list|,
operator|(
operator|(
name|SampleComponent
operator|)
name|component
operator|)
operator|.
name|isFallback
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDataFormatWithFallbackNames ()
specifier|public
name|void
name|testDataFormatWithFallbackNames
parameter_list|()
throws|throws
name|Exception
block|{
name|DataFormat
name|dataFormat
init|=
name|context
operator|.
name|resolveDataFormat
argument_list|(
literal|"green"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"DataFormat not found in registry"
argument_list|,
name|dataFormat
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Wrong instance type of the DataFormat"
argument_list|,
name|dataFormat
operator|instanceof
name|SampleDataFormat
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Here we should have the fallback DataFormat"
argument_list|,
operator|(
operator|(
name|SampleDataFormat
operator|)
name|dataFormat
operator|)
operator|.
name|isFallback
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDataformatWithBothNames ()
specifier|public
name|void
name|testDataformatWithBothNames
parameter_list|()
throws|throws
name|Exception
block|{
name|DataFormat
name|dataFormat
init|=
name|context
operator|.
name|resolveDataFormat
argument_list|(
literal|"red"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"DataFormat not found in registry"
argument_list|,
name|dataFormat
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Wrong instance type of the DataFormat"
argument_list|,
name|dataFormat
operator|instanceof
name|SampleDataFormat
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Here we should NOT have the fallback DataFormat"
argument_list|,
operator|(
operator|(
name|SampleDataFormat
operator|)
name|dataFormat
operator|)
operator|.
name|isFallback
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testLanguageWithFallbackNames ()
specifier|public
name|void
name|testLanguageWithFallbackNames
parameter_list|()
throws|throws
name|Exception
block|{
name|Language
name|language
init|=
name|context
operator|.
name|resolveLanguage
argument_list|(
literal|"green"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Language not found in registry"
argument_list|,
name|language
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Wrong instance type of the Language"
argument_list|,
name|language
operator|instanceof
name|SampleLanguage
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Here we should have the fallback Language"
argument_list|,
operator|(
operator|(
name|SampleLanguage
operator|)
name|language
operator|)
operator|.
name|isFallback
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testLanguageWithBothNames ()
specifier|public
name|void
name|testLanguageWithBothNames
parameter_list|()
throws|throws
name|Exception
block|{
name|Language
name|language
init|=
name|context
operator|.
name|resolveLanguage
argument_list|(
literal|"blue"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Language not found in registry"
argument_list|,
name|language
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Wrong instance type of the Language"
argument_list|,
name|language
operator|instanceof
name|SampleLanguage
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Here we should NOT have the fallback Language"
argument_list|,
operator|(
operator|(
name|SampleLanguage
operator|)
name|language
operator|)
operator|.
name|isFallback
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNullLookupComponent ()
specifier|public
name|void
name|testNullLookupComponent
parameter_list|()
throws|throws
name|Exception
block|{
name|Component
name|component
init|=
name|context
operator|.
name|getComponent
argument_list|(
literal|"xxxxxxxxx"
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
literal|"Non-existent Component should be null"
argument_list|,
name|component
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNullLookupDataFormat ()
specifier|public
name|void
name|testNullLookupDataFormat
parameter_list|()
throws|throws
name|Exception
block|{
name|DataFormat
name|dataFormat
init|=
name|context
operator|.
name|resolveDataFormat
argument_list|(
literal|"xxxxxxxxx"
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
literal|"Non-existent DataFormat should be null"
argument_list|,
name|dataFormat
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|NoSuchLanguageException
operator|.
name|class
argument_list|)
DECL|method|testNullLookupLanguage ()
specifier|public
name|void
name|testNullLookupLanguage
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|resolveLanguage
argument_list|(
literal|"xxxxxxxxx"
argument_list|)
expr_stmt|;
block|}
DECL|class|SampleComponent
specifier|public
specifier|static
class|class
name|SampleComponent
extends|extends
name|DefaultComponent
block|{
DECL|field|fallback
specifier|private
name|boolean
name|fallback
decl_stmt|;
DECL|method|SampleComponent (boolean fallback)
name|SampleComponent
parameter_list|(
name|boolean
name|fallback
parameter_list|)
block|{
name|this
operator|.
name|fallback
operator|=
name|fallback
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Should not be called"
argument_list|)
throw|;
block|}
DECL|method|isFallback ()
specifier|public
name|boolean
name|isFallback
parameter_list|()
block|{
return|return
name|fallback
return|;
block|}
DECL|method|setFallback (boolean fallback)
specifier|public
name|void
name|setFallback
parameter_list|(
name|boolean
name|fallback
parameter_list|)
block|{
name|this
operator|.
name|fallback
operator|=
name|fallback
expr_stmt|;
block|}
block|}
DECL|class|SampleDataFormat
specifier|public
specifier|static
class|class
name|SampleDataFormat
implements|implements
name|DataFormat
block|{
DECL|field|fallback
specifier|private
name|boolean
name|fallback
decl_stmt|;
DECL|method|SampleDataFormat (boolean fallback)
name|SampleDataFormat
parameter_list|(
name|boolean
name|fallback
parameter_list|)
block|{
name|this
operator|.
name|fallback
operator|=
name|fallback
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|marshal (Exchange exchange, Object graph, OutputStream stream)
specifier|public
name|void
name|marshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|graph
parameter_list|,
name|OutputStream
name|stream
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Should not be called"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|unmarshal (Exchange exchange, InputStream stream)
specifier|public
name|Object
name|unmarshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|InputStream
name|stream
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Should not be called"
argument_list|)
throw|;
block|}
DECL|method|isFallback ()
specifier|public
name|boolean
name|isFallback
parameter_list|()
block|{
return|return
name|fallback
return|;
block|}
DECL|method|setFallback (boolean fallback)
specifier|public
name|void
name|setFallback
parameter_list|(
name|boolean
name|fallback
parameter_list|)
block|{
name|this
operator|.
name|fallback
operator|=
name|fallback
expr_stmt|;
block|}
block|}
DECL|class|SampleLanguage
specifier|public
specifier|static
class|class
name|SampleLanguage
implements|implements
name|Language
block|{
DECL|field|fallback
specifier|private
name|boolean
name|fallback
decl_stmt|;
DECL|method|SampleLanguage (boolean fallback)
name|SampleLanguage
parameter_list|(
name|boolean
name|fallback
parameter_list|)
block|{
name|this
operator|.
name|fallback
operator|=
name|fallback
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createPredicate (String expression)
specifier|public
name|Predicate
name|createPredicate
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Should not be called"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|createExpression (String expression)
specifier|public
name|Expression
name|createExpression
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Should not be called"
argument_list|)
throw|;
block|}
DECL|method|isFallback ()
specifier|public
name|boolean
name|isFallback
parameter_list|()
block|{
return|return
name|fallback
return|;
block|}
DECL|method|setFallback (boolean fallback)
specifier|public
name|void
name|setFallback
parameter_list|(
name|boolean
name|fallback
parameter_list|)
block|{
name|this
operator|.
name|fallback
operator|=
name|fallback
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

