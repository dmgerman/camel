begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.schematron.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|schematron
operator|.
name|processor
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Source
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Templates
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|TransformerException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|TransformerFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|URIResolver
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamSource
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|saxon
operator|.
name|TransformerFactoryImpl
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
name|schematron
operator|.
name|constant
operator|.
name|Constants
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
name|schematron
operator|.
name|util
operator|.
name|Utils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|io
operator|.
name|IOUtils
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
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

begin_comment
comment|/**  * SchematronEngine Unit Test.  */
end_comment

begin_class
DECL|class|SchematronProcessorTest
specifier|public
class|class
name|SchematronProcessorTest
block|{
DECL|field|logger
specifier|private
name|Logger
name|logger
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|SchematronProcessorTest
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Test
DECL|method|testValidXML ()
specifier|public
name|void
name|testValidXML
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|payload
init|=
name|IOUtils
operator|.
name|toString
argument_list|(
name|ClassLoader
operator|.
name|getSystemResourceAsStream
argument_list|(
literal|"xml/article-1.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|logger
operator|.
name|info
argument_list|(
literal|"Validating payload: {}"
argument_list|,
name|payload
argument_list|)
expr_stmt|;
comment|// validate
name|String
name|result
init|=
name|getProcessor
argument_list|(
literal|"sch/schematron-1.sch"
argument_list|,
literal|null
argument_list|)
operator|.
name|validate
argument_list|(
name|payload
argument_list|)
decl_stmt|;
name|logger
operator|.
name|info
argument_list|(
literal|"Schematron Report: {}"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|Integer
operator|.
name|valueOf
argument_list|(
name|Utils
operator|.
name|evaluate
argument_list|(
literal|"count(//svrl:failed-assert)"
argument_list|,
name|result
argument_list|)
argument_list|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|Integer
operator|.
name|valueOf
argument_list|(
name|Utils
operator|.
name|evaluate
argument_list|(
literal|"count(//svrl:successful-report)"
argument_list|,
name|result
argument_list|)
argument_list|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInvalidXMLWithClientResolver ()
specifier|public
name|void
name|testInvalidXMLWithClientResolver
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|payload
init|=
name|IOUtils
operator|.
name|toString
argument_list|(
name|ClassLoader
operator|.
name|getSystemResourceAsStream
argument_list|(
literal|"xml/article-3.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|logger
operator|.
name|info
argument_list|(
literal|"Validating payload: {}"
argument_list|,
name|payload
argument_list|)
expr_stmt|;
comment|// validate
name|String
name|result
init|=
name|getProcessor
argument_list|(
literal|"sch/schematron-3.sch"
argument_list|,
operator|new
name|ClientUriResolver
argument_list|()
argument_list|)
operator|.
name|validate
argument_list|(
name|payload
argument_list|)
decl_stmt|;
name|logger
operator|.
name|info
argument_list|(
literal|"Schematron Report: {}"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"A title should be at least two characters"
argument_list|,
name|Utils
operator|.
name|evaluate
argument_list|(
literal|"//svrl:failed-assert/svrl:text"
argument_list|,
name|result
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|Integer
operator|.
name|valueOf
argument_list|(
name|Utils
operator|.
name|evaluate
argument_list|(
literal|"count(//svrl:successful-report)"
argument_list|,
name|result
argument_list|)
argument_list|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInValidXML ()
specifier|public
name|void
name|testInValidXML
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|payload
init|=
name|IOUtils
operator|.
name|toString
argument_list|(
name|ClassLoader
operator|.
name|getSystemResourceAsStream
argument_list|(
literal|"xml/article-2.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|logger
operator|.
name|info
argument_list|(
literal|"Validating payload: {}"
argument_list|,
name|payload
argument_list|)
expr_stmt|;
comment|// validate
name|String
name|result
init|=
name|getProcessor
argument_list|(
literal|"sch/schematron-2.sch"
argument_list|,
literal|null
argument_list|)
operator|.
name|validate
argument_list|(
name|payload
argument_list|)
decl_stmt|;
name|logger
operator|.
name|info
argument_list|(
literal|"Schematron Report: {}"
argument_list|,
name|result
argument_list|)
expr_stmt|;
comment|// should throw two assertions because of the missing chapters in the XML.
name|assertEquals
argument_list|(
literal|"A chapter should have a title"
argument_list|,
name|Utils
operator|.
name|evaluate
argument_list|(
literal|"//svrl:failed-assert/svrl:text"
argument_list|,
name|result
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"'chapter' element has more than one title present"
argument_list|,
name|Utils
operator|.
name|evaluate
argument_list|(
literal|"//svrl:successful-report/svrl:text"
argument_list|,
name|result
argument_list|)
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns schematron processor      *      * @param schematron      * @param clientResolver      * @return      */
DECL|method|getProcessor (final String schematron, final URIResolver clientResolver)
specifier|private
name|SchematronProcessor
name|getProcessor
parameter_list|(
specifier|final
name|String
name|schematron
parameter_list|,
specifier|final
name|URIResolver
name|clientResolver
parameter_list|)
block|{
name|TransformerFactory
name|factory
init|=
operator|new
name|TransformerFactoryImpl
argument_list|()
decl_stmt|;
name|factory
operator|.
name|setURIResolver
argument_list|(
operator|new
name|ClassPathURIResolver
argument_list|(
name|Constants
operator|.
name|SCHEMATRON_TEMPLATES_ROOT_DIR
argument_list|,
name|clientResolver
argument_list|)
argument_list|)
expr_stmt|;
name|Templates
name|rules
init|=
name|TemplatesFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|getTemplates
argument_list|(
name|ClassLoader
operator|.
name|getSystemResourceAsStream
argument_list|(
name|schematron
argument_list|)
argument_list|,
name|factory
argument_list|)
decl_stmt|;
return|return
name|SchematronProcessorFactory
operator|.
name|newScehamtronEngine
argument_list|(
name|rules
argument_list|)
return|;
block|}
DECL|class|ClientUriResolver
class|class
name|ClientUriResolver
implements|implements
name|URIResolver
block|{
annotation|@
name|Override
DECL|method|resolve (String href, String base)
specifier|public
name|Source
name|resolve
parameter_list|(
name|String
name|href
parameter_list|,
name|String
name|base
parameter_list|)
throws|throws
name|TransformerException
block|{
return|return
operator|new
name|StreamSource
argument_list|(
name|ClientUriResolver
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"custom-resolver/"
operator|.
name|concat
argument_list|(
name|href
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

