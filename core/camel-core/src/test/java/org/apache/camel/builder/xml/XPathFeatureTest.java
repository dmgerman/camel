begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder.xml
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|xml
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileNotFoundException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|SAXParseException
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
name|ContextTestSupport
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
name|NoTypeConversionAvailableException
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
name|RuntimeCamelException
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
name|TypeConversionException
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
name|converter
operator|.
name|jaxp
operator|.
name|XmlConverter
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
name|apache
operator|.
name|camel
operator|.
name|language
operator|.
name|xpath
operator|.
name|XPathBuilder
operator|.
name|xpath
import|;
end_import

begin_class
DECL|class|XPathFeatureTest
specifier|public
class|class
name|XPathFeatureTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|DOM_BUILDER_FACTORY_FEATURE
specifier|public
specifier|static
specifier|final
name|String
name|DOM_BUILDER_FACTORY_FEATURE
init|=
name|XmlConverter
operator|.
name|DOCUMENT_BUILDER_FACTORY_FEATURE
decl_stmt|;
DECL|field|XML_DATA
specifier|public
specifier|static
specifier|final
name|String
name|XML_DATA
init|=
literal|"<!DOCTYPE foo [ "
operator|+
literal|"<!ELEMENT foo ANY><!ENTITY xxe SYSTEM \"file:///bin/test.sh\">]><test>&xxe;</test>"
decl_stmt|;
DECL|field|XML_DATA_INVALID
specifier|public
specifier|static
specifier|final
name|String
name|XML_DATA_INVALID
init|=
literal|"<!DOCTYPE foo [ "
operator|+
literal|"<!ELEMENT foo ANY><!ENTITY xxe SYSTEM \"file:///bin/test.sh\">]><test>&xxe;</test><notwellformed>"
decl_stmt|;
annotation|@
name|Override
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|resetCoreConverters
argument_list|()
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
DECL|method|resetCoreConverters ()
specifier|private
name|void
name|resetCoreConverters
parameter_list|()
throws|throws
name|Exception
block|{
comment|/*          * Field field =          * CoreStaticTypeConverterLoader.class.getDeclaredField("INSTANCE");          * field.setAccessible(true); Field modifiersField =          * Field.class.getDeclaredField("modifiers");          * modifiersField.setAccessible(true); modifiersField.setInt(field,          * field.getModifiers()& ~Modifier.FINAL); Constructor<?> cns =          * CoreStaticTypeConverterLoader.class.getDeclaredConstructor();          * cns.setAccessible(true); field.set(null, cns.newInstance());          */
block|}
annotation|@
name|Override
DECL|method|isUseRouteBuilder ()
specifier|public
name|boolean
name|isUseRouteBuilder
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Test
DECL|method|testXPathResult ()
specifier|public
name|void
name|testXPathResult
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|result
init|=
operator|(
name|String
operator|)
name|xpath
argument_list|(
literal|"/"
argument_list|)
operator|.
name|stringResult
argument_list|()
operator|.
name|evaluate
argument_list|(
name|createExchange
argument_list|(
name|XML_DATA
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong result"
argument_list|,
literal|"  "
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testXPath ()
specifier|public
name|void
name|testXPath
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Set this feature will enable the external general entities
name|System
operator|.
name|setProperty
argument_list|(
name|DOM_BUILDER_FACTORY_FEATURE
operator|+
literal|":"
operator|+
literal|"http://xml.org/sax/features/external-general-entities"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
try|try
block|{
name|xpath
argument_list|(
literal|"/"
argument_list|)
operator|.
name|stringResult
argument_list|()
operator|.
name|evaluate
argument_list|(
name|createExchange
argument_list|(
name|XML_DATA
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expect an Exception here"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TypeConversionException
name|ex
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"Get a wrong exception cause: "
operator|+
name|ex
operator|.
name|getCause
argument_list|()
operator|.
name|getClass
argument_list|()
operator|+
literal|" instead of "
operator|+
name|FileNotFoundException
operator|.
name|class
argument_list|,
name|ex
operator|.
name|getCause
argument_list|()
operator|instanceof
name|FileNotFoundException
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|System
operator|.
name|clearProperty
argument_list|(
name|DOM_BUILDER_FACTORY_FEATURE
operator|+
literal|":"
operator|+
literal|"http://xml.org/sax/features/external-general-entities"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testXPathNoTypeConverter ()
specifier|public
name|void
name|testXPathNoTypeConverter
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
comment|// define a class without type converter as document type
name|xpath
argument_list|(
literal|"/"
argument_list|)
operator|.
name|documentType
argument_list|(
name|Exchange
operator|.
name|class
argument_list|)
operator|.
name|stringResult
argument_list|()
operator|.
name|evaluate
argument_list|(
name|createExchange
argument_list|(
name|XML_DATA
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expect an Exception here"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeCamelException
name|ex
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"Get a wrong exception cause: "
operator|+
name|ex
operator|.
name|getCause
argument_list|()
operator|.
name|getClass
argument_list|()
operator|+
literal|" instead of "
operator|+
name|NoTypeConversionAvailableException
operator|.
name|class
argument_list|,
name|ex
operator|.
name|getCause
argument_list|()
operator|instanceof
name|NoTypeConversionAvailableException
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testXPathResultOnInvalidData ()
specifier|public
name|void
name|testXPathResultOnInvalidData
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|xpath
argument_list|(
literal|"/"
argument_list|)
operator|.
name|stringResult
argument_list|()
operator|.
name|evaluate
argument_list|(
name|createExchange
argument_list|(
name|XML_DATA_INVALID
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expect an Exception here"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TypeConversionException
name|ex
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"Get a wrong exception cause: "
operator|+
name|ex
operator|.
name|getCause
argument_list|()
operator|.
name|getClass
argument_list|()
operator|+
literal|" instead of "
operator|+
name|SAXParseException
operator|.
name|class
argument_list|,
name|ex
operator|.
name|getCause
argument_list|()
operator|instanceof
name|SAXParseException
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createExchange (Object xml)
specifier|protected
name|Exchange
name|createExchange
parameter_list|(
name|Object
name|xml
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|createExchangeWithBody
argument_list|(
name|context
argument_list|,
name|xml
argument_list|)
decl_stmt|;
return|return
name|exchange
return|;
block|}
block|}
end_class

end_unit

